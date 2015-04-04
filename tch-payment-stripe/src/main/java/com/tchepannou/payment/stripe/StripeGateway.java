package com.tchepannou.payment.stripe;

import com.stripe.exception.CardException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.PaymentGateway;
import com.tchepannou.payment.api.PaymentRequest;
import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.VerifyAccountRequest;
import com.tchepannou.payment.api.response.VerifyAccountResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * User: herve
 * Date: 14-07-19 4:34 PM
 */
public class StripeGateway implements PaymentGateway
{
    //-- Static Attributes
    public static final String CONFIG_API_KEY = "stripe-api-key";

    private static final String PARAM_METADATA = "metadata";
    private static final String PARAM_DESCRIPTION = "description";
    private static final String PARAM_AMOUNT = "amount";
    private static final String PARAM_CURRENCY = "currency";
    private static final String PARAM_CARD = "card";

    private static final String PARAM_CARD_EXP_MONTH = "exp_month";
    private static final String PARAM_CARD_EXP_YEAR = "exp_year";
    private static final String PARAM_CARD_NUMBER = "number";
    private static final String PARAM_CARD_CVC = "cvc";
    private static final String PARAM_CARD_NAME = "name";
    private static final String PARAM_CARD_ADDRESS_LINE1 = "address_line1";
    private static final String PARAM_CARD_ADDRESS_CITY = "address_city";
    private static final String PARAM_CARD_ADDRESS_ZIP = "address_zip";
    private static final String PARAM_CARD_ADDRESS_STATE = "address_state";
    private static final String PARAM_CARD_ADDRESS_COUNTRY = "address_country";

    //-- Properties
    private Properties _properties = new Properties();


    //-- PaymentGateway
    @Override
    public void init (Properties properties)
    {
        _properties = properties;
    }

    @Override
    public PaymentResponse process (PaymentRequest request)
            throws IOException
    {
        if (request instanceof DirectPaymentRequest)
        {
            return doDirectPayment((DirectPaymentRequest) request);
        }
        else if (request instanceof VerifyAccountRequest)
        {
            return doVerifyAccount((VerifyAccountRequest) request);
        }
        else
        {
            throw new IllegalStateException("Invalid request: " + request);
        }
    }

    //-- Private
    private PaymentResponse doVerifyAccount(VerifyAccountRequest request)
    {
        String apiKey = _properties.getProperty(CONFIG_API_KEY);
        try
        {
            Account account = Account.retrieve(apiKey);
            List<VerifyAccountResponse.Option> options = new ArrayList();
            if (account.getChargeEnabled())
            {
                options.add(VerifyAccountResponse.Option.direct_payment);
                options.add(VerifyAccountResponse.Option.recurring_payment);
            }
            return new StripeResponse.Builder()
                    .approved(account !=  null && account.getEmail().equals(request.getEmail()))
                    .currencies(account.getCurrenciesSupported())
                    .options(options)
                    .build();
        }
        catch (Exception e)
        {
            return createResponse(e);
        }
    }

    private PaymentResponse doDirectPayment(DirectPaymentRequest request)
    {
        Map<String, Object> meta = new HashMap<String, Object> ();
        meta.put("invoice_id", request.getInvoiceNumber());

        Map<String, Object> card = new HashMap<String, Object> ();
        CreditCard cc = (CreditCard)request.getPaymentMethod();
        Address billingAddress = request.getBillingAddress();

        card.put(PARAM_CARD_NUMBER, cc.getNumber());
        card.put(PARAM_CARD_EXP_MONTH, cc.getExpiryMonth());
        card.put(PARAM_CARD_EXP_YEAR, cc.getExpiryYear());
        card.put(PARAM_CARD_CVC, cc.getVerificationCode());
        if (billingAddress != null)
        {
            String first = billingAddress.getFirstName();
            String last = billingAddress.getLastName();
            String name = (first != null ? first : "")
                    + " "
                    + (last != null ? last : "");

            card.put(PARAM_CARD_NAME, name.trim());
            card.put(PARAM_CARD_ADDRESS_LINE1, billingAddress.getStreet());
            card.put(PARAM_CARD_ADDRESS_CITY, billingAddress.getCity());
            card.put(PARAM_CARD_ADDRESS_STATE, billingAddress.getState());
            card.put(PARAM_CARD_ADDRESS_ZIP, billingAddress.getZipCode());
            card.put(PARAM_CARD_ADDRESS_COUNTRY, billingAddress.getCountry());
        }

        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put(PARAM_AMOUNT, formatAmount(request.getAmountValue(), request.getAmountCurrency()));
        chargeParams.put(PARAM_CURRENCY, request.getAmountCurrency());
        chargeParams.put(PARAM_DESCRIPTION, request.getInvoiceDescription());
        chargeParams.put(PARAM_METADATA, meta);
        chargeParams.put(PARAM_CARD, card);

        String apiKey = _properties.getProperty(CONFIG_API_KEY);
        try
        {
            final Charge charge = Charge.create(chargeParams, apiKey);
            return new StripeResponse.Builder()
                    .approved(charge.getFailureCode() == null)
                    .code(charge.getFailureCode())
                    .reasonText(charge.getFailureMessage())
                    .transactionId(charge.getId())
                    .build()
            ;
        }
        catch (Exception e)
        {
            return createResponse(e);
        }
    };


    //-- Protected
    protected int formatAmount(double amount, String currency)
    {
        Currency cur = Currency.getInstance(currency);
        int fraction = cur.getDefaultFractionDigits();
        if (fraction <= 0)
        {
            return (int)amount;
        }
        else
        {
            double unit = Math.pow(10, fraction);
            return (int)(amount * unit);
        }
    }

    protected StripeResponse createResponse (Exception e)
    {
        return new StripeResponse.Builder()
                .approved(false)
                .code(e instanceof CardException ? ((CardException) e).getCode() : null)
                .reasonText(e.getMessage())
                .build()
        ;
    }
}
