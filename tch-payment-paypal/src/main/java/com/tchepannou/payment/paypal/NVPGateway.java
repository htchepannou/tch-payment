/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.paypal;

import com.paypal.svcs.services.AdaptiveAccountsService;
import com.paypal.svcs.types.aa.GetVerifiedStatusRequest;
import com.paypal.svcs.types.aa.GetVerifiedStatusResponse;
import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.PaymentGateway;
import com.tchepannou.payment.api.PaymentMethod;
import com.tchepannou.payment.api.PaymentRequest;
import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.request.BillOutstandingAmountRequest;
import com.tchepannou.payment.api.request.CancelRecurringPaymentProfileRequest;
import com.tchepannou.payment.api.request.CreateRecurringPaymentProfileRequest;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.RefundRequest;
import com.tchepannou.payment.api.request.UpdateRecurringPaymentsProfileRequest;
import com.tchepannou.payment.api.request.VerifyAccountRequest;
import com.tchepannou.payment.api.response.CancelRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.CreateRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.RefundResponse;
import com.tchepannou.payment.api.response.VerifyAccountResponse;
import com.tchepannou.payment.api.util.DefaultPaymentUrlHandler;
import com.tchepannou.payment.api.util.PaymentUrlHandler;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Implementation of {@link PaymentGateway} for Paypal NVP API.
 *
 * @author herve
 */
public class NVPGateway
    implements PaymentGateway
{
    //-- Static Attributes  -------------------    
    private static final String UTF8 = "UTF-8";
    private static final DecimalFormat AMOUNT_FORMATER = new DecimalFormat ("0.##");
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_SIGNATURE = "signature";
    public static final String PARAM_SANDBOX_EMAIL_ADDRESS = "sandbox.email";
    public static final String PARAM_MODE = "mode";

    public static final String PARAM_URL = "url";
    public static final String PARAM_PORT = "port";
    public static final String PARAM_NOTIFY_URL = "NOTIFYURL";
    public static final String FIELD_METHOD = "METHOD";
    public static final String FIELD_CC_TYPE = "CREDITCARDTYPE";
    public static final String FIELD_CC_NUMBER = "ACCT";
    public static final String FIELD_CC_CVV2 = "CVV2";
    public static final String FIELD_CC_EXPIRY_DATE = "EXPDATE";
    public static final String FIELD_AMOUNT = "AMT";
    public static final String FIELD_FIRSTNAME = "FIRSTNAME";
    public static final String FIELD_LASTNAME = "LASTNAME";
    public static final String FIELD_IP_ADDRESS = "IPADDRESS";
    public static final String FIELD_STREET = "STREET";
    public static final String FIELD_CITY = "CITY";
    public static final String FIELD_COUNTRY = "COUNTRYCODE";
    public static final String FIELD_ZIP = "ZIP";
    public static final String FIELD_STATE = "STATE";
    public static final String FIELD_PAYMENT_ACTION = "PAYMENTACTION";
    public static final String FIELD_CURRENCY_CODE = "CURRENCYCODE";
    public static final String FIELD_VERSION = "VERSION";
    public static final String FIELD_USER = "USER";
    public static final String FIELD_PASSWORD = "PWD";
    public static final String FIELD_SIGNATURE = "SIGNATURE";
    public static final String FIELD_DESCRIPTION = "DESC";
    public static final String FIELD_MAX_FAILED_PAYMENTS = "MAXFAILEDPAYMENTS";
    public static final String FIELD_AUTO_BILL_AMOUNT = "AUTOBILLAMT";
    public static final String FIELD_BILLING_PERIOD = "BILLINGPERIOD";
    public static final String FIELD_BILLING_FREQUENCY = "BILLINGFREQUENCY";
    public static final String FIELD_PROFILE_REFERENCE = "PROFILEREFERENCE";
    public static final String FIELD_PROFILE_START_DATE = "PROFILESTARTDATE";
    public static final String FIELD_PROFILE_ID = "PROFILEID";
    public static final String FIELD_OUTSTANDIND_AMOUNT = "OUTSTANDINGAMT";
    public static final String FIELD_ACTION = "ACTION";
    public static final String FIELD_TRANSACTIONID = "TRANSACTIONID";
    public static final String FIELD_REFUNDTYPE = "REFUNDTYPE";
    public static final String FIELD_INVOICE_NUMBER = "INVNUM";
    public static final String FIELD_TRIAL_BILLING_PERIOD = "TRIALBILLINGPERIOD";
    public static final String FIELD_TRIAL_BILLING_FREQUENCY = "TRIALBILLINGFREQUENCY";
    public static final String FIELD_TRIAL_TOTAL_BILLING_CYCLES = "TRIALTOTALBILLINGCYCLES";
    public static final String FIELD_TRIAL_AMOUNT = "TRIALAMT";
    public static final String VALUE_VERSION = "51.0";
    public static final String VALUE_PAYMENT_ACTION_SALE = "Sale";
    public static final String VALUE_METHOD_DO_DIRECT_PAYMENT = "DoDirectPayment";
    public static final String VALUE_METHOD_GET_TRANSACTION_DETAILS = "GetTransactionDetails";
    public static final String VALUE_METHOD_CREATE_RECURING_PAYMENT_PROFILE = "CreateRecurringPaymentsProfile";
    public static final String VALUE_METHOD_MANAGE_RECURING_PAYMENT_PROFILE_STATUS = "ManageRecurringPaymentsProfileStatus";
    public static final String VALUE_METHOD_REFUND = "RefundTransaction";
    public static final String VALUE_METHOD_BILL_OUTSTANDING_AMOUNT = "BillOutstandingAmount";
    public static final String VALUE_METHOD_UPDATE_RECURING_PAYMENT_PROFILE = "UpdateRecurringPaymentsProfile";
    public static final String VALUE_PORT = "443";
    public static final String VALUE_MODE_LIVE = "live";
    public static final String VALUE_MODE_SANDBOX = "sandbox";
    
    
    //-- Properties
    private Properties _properties;
    //private PaymentUrlHandler _handler = new DefaultPaymentUrlHandler ();
    
    
    //-- Constructor
    public NVPGateway ()
    {
        
    }
    
    @Deprecated
    public NVPGateway (PaymentUrlHandler handler)
    {
    }
    

    //-- PaymentGateway overrides
    /**
     * Initialize the gateway. The gateway configuration parameters of the
     * gateway are: <ul> <li>
     * <code>api_username</code>: User name <li>
     * <code>api_password</code>: Password <li>
     * <code>signature</code>: Paypay signature <li>
     * <code>notify_url</code>: IPN Notification URL
     *
     * @param properties Configuration properties.
     */
    public void init (Properties properties)
    {
        _properties = properties;
    }

    public PaymentResponse process (PaymentRequest request)
        throws IOException
    {
        if ( request instanceof DirectPaymentRequest )
        {
            return doDirectPayment (( DirectPaymentRequest ) request);
        }
        else if ( request instanceof CreateRecurringPaymentProfileRequest )
        {
            return doCreateRecurringPaymentsProfile (( CreateRecurringPaymentProfileRequest ) request);
        }
        else if ( request instanceof CancelRecurringPaymentProfileRequest )
        {
            return doCancelRecurringPaymentsProfile (( CancelRecurringPaymentProfileRequest ) request);
        }
        else if ( request instanceof RefundRequest )
        {
            return doRefund (( RefundRequest ) request);
        }
        else if ( request instanceof UpdateRecurringPaymentsProfileRequest )
        {
            return doUpdateRecurringPaymentsProfile (( UpdateRecurringPaymentsProfileRequest ) request);
        }
        else if ( request instanceof BillOutstandingAmountRequest )
        {
            return doBillOutstandingAmount (( BillOutstandingAmountRequest ) request);
        }
        else if (request instanceof VerifyAccountRequest)
        {
            return doVerifyAccount((VerifyAccountRequest)request);
        }
        else
        {
            throw new IllegalStateException ("Invalid request: " + request);
        }
    }

    //-- Protected methods  -------------------
    private VerifyAccountResponse doVerifyAccount(VerifyAccountRequest request)
            throws IOException
    {
        GetVerifiedStatusRequest req = new GetVerifiedStatusRequest();
        req.setFirstName(request.getFirstName());
        req.setLastName(request.getLastName());
        req.setEmailAddress(request.getEmail());
        req.setMatchCriteria("NAME");

        Map config = new HashMap();
        config.put("acct1.UserName", _properties.getProperty(PARAM_USERNAME));
        config.put("acct1.Password", _properties.getProperty(PARAM_PASSWORD));
        config.put("acct1.Signature", _properties.getProperty(PARAM_SIGNATURE));
        config.put("acct1.AppId", "APP-80W284485P519543T");
        config.put("sandbox.EmailAddress", _properties.getProperty(PARAM_SANDBOX_EMAIL_ADDRESS));
        config.put("mode", _properties.getProperty(PARAM_MODE));
        AdaptiveAccountsService service = new AdaptiveAccountsService(config);
        try
        {
            final GetVerifiedStatusResponse resp = service.getVerifiedStatus(req);
            return new VerifyAccountResponse()
            {
                public boolean isApproved()
                {
                    return resp != null && "SUCCESS".equalsIgnoreCase(resp.getResponseEnvelope().getAck().toString());
                }

                public boolean supportOption(Option option)
                {
                    if (resp == null)
                    {
                        return false;
                    }
                    String type = resp.getUserInfo().getAccountType();
                    if ("Personal".equals(type))
                    {
                        return Option.checkout_button.equals(option);
                    }
                    if ("Premier".equals(type) || "Business".equals(type))
                    {
                        return Option.checkout_button.equals(option)
                            || Option.direct_payment.equals(option)
                            || Option.recurring_payment.equals(option)
                        ;
                    }
                    return false;
                }

                public boolean supportsCurrency(String currencyCode)
                {
                    return true;
                }

                public String getCode()
                {
                    return null;
                }

                public String getReasonText()
                {
                    return null;
                }

                public String getTransactionId()
                {
                    return null;
                }
            };
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    private DirectPaymentResponse doDirectPayment (DirectPaymentRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_DO_DIRECT_PAYMENT, params);
        put (FIELD_PAYMENT_ACTION, VALUE_PAYMENT_ACTION_SALE, params);
        put (FIELD_IP_ADDRESS, request.getCustomerIP (), params);
        put (FIELD_INVOICE_NUMBER, request.getInvoiceNumber (), params);
        put (FIELD_AMOUNT, formatAmount (request.getAmountValue ()), params);
        put (FIELD_CURRENCY_CODE, request.getAmountCurrency (), params);

        Address billing = request.getBillingAddress ();
        if ( billing != null )
        {
            put (FIELD_FIRSTNAME, billing.getFirstName (), 25, params);
            put (FIELD_LASTNAME, billing.getLastName (), 25, params);
            put (FIELD_STREET, billing.getStreet (), 100, params);
            put (FIELD_CITY, billing.getCity (), 40, params);
            put (FIELD_STATE, billing.getState (), 40, params);
            put (FIELD_COUNTRY, billing.getCountry (), 2, params);
            put (FIELD_ZIP, billing.getZipCode (), 20, params);
        }

        PaymentMethod method = request.getPaymentMethod ();
        initParameters (request, method, params);

        return doProcess (params);
    }

    private RefundResponse doRefund (RefundRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_REFUND, params);
        put (FIELD_TRANSACTIONID, request.getTransactionId (), params);
        put (FIELD_REFUNDTYPE, "Partial", params);
        put (FIELD_AMOUNT, formatAmount (request.getAmountValue ()), params);
        put (FIELD_CURRENCY_CODE, request.getAmountCurrency (), params);

        return doProcess (params);
    }

    private CreateRecurringPaymentProfileResponse doCreateRecurringPaymentsProfile (CreateRecurringPaymentProfileRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_CREATE_RECURING_PAYMENT_PROFILE, params);
        put (FIELD_DESCRIPTION, request.getDescription (), 127, params);
        put (FIELD_MAX_FAILED_PAYMENTS, "1", params);
        put (FIELD_AUTO_BILL_AMOUNT, "AddToNextBilling", params);
        put (FIELD_BILLING_PERIOD, request.getBillingPeriod (), params);
        put (FIELD_BILLING_FREQUENCY, toFrequency (request.getBillingFrequency ()), params);
        put (FIELD_PROFILE_START_DATE, toUTCDate (request.getStartDate ()), params);
        put (FIELD_PROFILE_REFERENCE, request.getInvoiceNumber (), params);
        put (FIELD_AMOUNT, formatAmount (request.getAmountValue ()), params);
        put (FIELD_CURRENCY_CODE, request.getAmountCurrency (), params);

        Address billing = request.getBillingAddress ();
        if ( billing != null )
        {
            put (FIELD_FIRSTNAME, billing.getFirstName (), 25, params);
            put (FIELD_LASTNAME, billing.getLastName (), 25, params);
            put (FIELD_STREET, billing.getStreet (), 100, params);
            put (FIELD_CITY, billing.getCity (), 40, params);
            put (FIELD_STATE, billing.getState (), 40, params);
            put (FIELD_COUNTRY, billing.getCountry (), 2, params);
            put (FIELD_ZIP, billing.getZipCode (), 20, params);
        }

        double trialAmount = request.getTrialAmountValue ();
        if ( trialAmount > 0 )
        {
            put (FIELD_TRIAL_AMOUNT, formatAmount (trialAmount), params);
            put (FIELD_TRIAL_BILLING_FREQUENCY, toFrequency (request.getTrialBillingFrequency ()), params);
            put (FIELD_TRIAL_BILLING_PERIOD, request.getTrialBillingPeriod (), params);
            put (FIELD_TRIAL_TOTAL_BILLING_CYCLES, "" + request.getTrialTotalBillingCycles (), params);
        }

        PaymentMethod method = request.getPaymentMethod ();
        initParameters (request, method, params);

        return doProcess (params);
    }

    private CancelRecurringPaymentProfileResponse doCancelRecurringPaymentsProfile (CancelRecurringPaymentProfileRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_MANAGE_RECURING_PAYMENT_PROFILE_STATUS, params);
        put (FIELD_PROFILE_ID, request.getProfileId (), params);
        put (FIELD_ACTION, "Cancel", params);

        return doProcess (params);
    }

    protected PaymentResponse doUpdateRecurringPaymentsProfile (UpdateRecurringPaymentsProfileRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_UPDATE_RECURING_PAYMENT_PROFILE, params);
        put (FIELD_PROFILE_ID, request.getProfileId (), params);
        put (FIELD_AUTO_BILL_AMOUNT, "AddToNextBilling", params);

        if ( request.getOutstandingAmount () >= 0 )
        {
            put (FIELD_OUTSTANDIND_AMOUNT, formatAmount (request.getOutstandingAmount ()), params);
        }

        Address billing = request.getBillingAddress ();
        if ( billing != null )
        {
            put (FIELD_FIRSTNAME, billing.getFirstName (), 25, params);
            put (FIELD_LASTNAME, billing.getLastName (), 25, params);
            put (FIELD_STREET, billing.getStreet (), 100, params);
            put (FIELD_CITY, billing.getCity (), 40, params);
            put (FIELD_STATE, billing.getState (), 40, params);
            put (FIELD_COUNTRY, billing.getCountry (), 2, params);
            put (FIELD_ZIP, billing.getZipCode (), 20, params);
        }

        if ( request.getAmountValue () > 0 )
        {
            put (FIELD_AMOUNT, formatAmount (request.getAmountValue ()), params);
            put (FIELD_CURRENCY_CODE, request.getAmountCurrency (), params);
        }

        PaymentMethod method = request.getPaymentMethod ();
        if ( method instanceof CreditCard )
        {
            CreditCard cc = ( CreditCard ) method;
            put (FIELD_CC_TYPE, cc.getType (), params);
            put (FIELD_CC_NUMBER, cc.getNumber (), params);
            put (FIELD_CC_EXPIRY_DATE, formatExpiryDate (cc), params);
            put (FIELD_CC_CVV2, cc.getVerificationCode (), params);
        }

        return doProcess (params);
    }

    protected PaymentResponse doBillOutstandingAmount (BillOutstandingAmountRequest request)
        throws IOException
    {
        Map<String, String> params = new HashMap<String, String> ();
        initParameters (params);

        put (FIELD_METHOD, VALUE_METHOD_BILL_OUTSTANDING_AMOUNT, params);
        put (FIELD_PROFILE_ID, request.getProfileId (), params);
        put (FIELD_AMOUNT, formatAmount (request.getAmountValue ()), params);
        put (FIELD_CURRENCY_CODE, request.getAmountCurrency (), params);
        return doProcess (params);
    }

    private NVPResponse doProcess (Map<String, String> params) throws IOException
    {
        String url = getProperty (PARAM_URL, null);
        int port = toInt (getProperty (PARAM_PORT, VALUE_PORT), 443);
        if (port != 443)
        {
            url += ":" + port;
        }
        
        HttpPost post = createPostMethod (url,params);
        HttpClient client = HttpClientBuilder.create().build();                
        HttpResponse resp = client.execute (post);

        String body = EntityUtils.toString (resp.getEntity ());
        return NVPResponse.parse (body, params);
    }
    
    private HttpPost createPostMethod (String url, Map<String, String> params) throws UnsupportedEncodingException
    {
        HttpPost post = new HttpPost(url);
        post.addHeader ("Content-type", "text/html; charset=" + UTF8);

        /* Parameters of the request */
        List<NameValuePair> values = new ArrayList<> ();
        Iterator it = params.keySet().iterator();
        for (int i = 0; it.hasNext(); i++)
        {
            String name = it.next().toString();
            String value = (String) params.get(name);
            if (!isEmpty(value))
            {
                values.add (new BasicNameValuePair (name, value));
            }
        }
        post.setEntity (new UrlEncodedFormEntity (values, UTF8));
        return post;
    }
    

    private void initParameters (Map<String, String> params)
    {
        put (FIELD_USER, getProperty (PARAM_USERNAME), params);
        put (FIELD_PASSWORD, getProperty (PARAM_PASSWORD), params);
        put (FIELD_SIGNATURE, getProperty (PARAM_SIGNATURE), params);
        put (FIELD_VERSION, VALUE_VERSION, params);
        put (PARAM_NOTIFY_URL, getProperty (PARAM_NOTIFY_URL), params);
    }

    private void initParameters (PaymentRequest request, PaymentMethod method, Map<String, String> params)
    {
        if ( method instanceof CreditCard )
        {
            CreditCard cc = ( CreditCard ) method;

            put (FIELD_CC_TYPE, cc.getType (), params);
            put (FIELD_CC_NUMBER, cc.getNumber (), params);
            put (FIELD_CC_EXPIRY_DATE, formatExpiryDate (cc), params);
            put (FIELD_CC_CVV2, cc.getVerificationCode (), params);
        }
    }

    private void put (String name, String value, Map<String, String> params)
    {
        put (name, value, -1, params);
    }

    private void put (String name, String value, int maxlen, Map<String, String> params)
    {
        if ( !isEmpty (value) )
        {
            String xvalue = value;
            if ( maxlen > 0 && value.length () > maxlen )
            {
                xvalue = value.substring (0, maxlen);
            }
            params.put (name, xvalue);
        }
    }

    private String getProperty (String name)
    {
        return getProperty (name, null);
    }

    private String getProperty (String name, String defaultValue)
    {
        String value = _properties.getProperty (name);
        return isEmpty (value)
            ? defaultValue
            : value;
    }

    private String formatAmount (double amount)
    {
        return AMOUNT_FORMATER.format (amount);
    }

    private String formatExpiryDate (CreditCard cc)
    {
        int mm = cc.getExpiryMonth ();
        int yy = cc.getExpiryYear ();
        String str = String.valueOf (mm) + String.valueOf (yy);

        return mm < 10
            ? "0" + str
            : str;
    }

    private String toUTCDate (Date date)
    {
        return new SimpleDateFormat ("yyyyy-MM-dd HH:mm:ss z").format (date);
    }

    protected String toFrequency (int frequency)
    {
        if ( frequency <= 0 )
        {
            return "1";
        }
        else
        {
            return String.valueOf (frequency);
        }
    }

    private int toInt (String str, int defaultValue)
    {
        try
        {
            return Integer.parseInt (str);
        }
        catch ( Exception e )
        {
            return defaultValue;
        }
    }

    private boolean isEmpty (String str)
    {
        return str == null || str.length () == 0;
    }
}
