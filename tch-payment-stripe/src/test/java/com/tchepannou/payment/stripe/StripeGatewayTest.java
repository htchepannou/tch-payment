package com.tchepannou.payment.stripe;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.VerifyAccountRequest;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.VerifyAccountResponse;
import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Properties;

/**
 * User: herve
 * Date: 14-07-20 1:39 PM
 */
public class StripeGatewayTest extends TestCase
{
    public static final String API_KEY = "sk_kWkNGw85HwgjC8gxK90sWlZuLchMP";

    private Properties _properties;
    private StripeGateway _gateway;
    private CreditCard _cc;
    private Address _billingAddress;

    public void setUp () throws Exception
    {
        Calendar cal = Calendar.getInstance();

        _properties = new Properties();
        _properties.setProperty(StripeGateway.CONFIG_API_KEY, API_KEY);
        _gateway = new StripeGateway();
        _gateway.init(_properties);

        _billingAddress = new Address();
        _billingAddress.setFirstName("Ray");
        _billingAddress.setLastName("Sponsible");

        /* See  https://stripe.com/docs/testing */
        _cc = new CreditCard();
        _cc.setExpiryMonth (1);
        _cc.setExpiryYear (cal.get(Calendar.YEAR) + 1);
        _cc.setType (CreditCard.TYPE_MASTERCARD);
        _cc.setNumber ("4242424242424242");
        _cc.setVerificationCode("123");
    }


    //-- Test
    public void test_verifyAccount () throws  Exception
    {
        // Given
        VerifyAccountRequest req = new VerifyAccountRequest();
        req.setEmail("herve.tchepannou@gmail.com");

        // When
        VerifyAccountResponse resp = (VerifyAccountResponse)_gateway.process(req);

        // Then
        assertTrue(resp.isApproved());
        assertNull(resp.getCode());
    }
    public void test_verifyAccount_invalid_email () throws  Exception
    {
        // Given
        VerifyAccountRequest req = new VerifyAccountRequest();
        req.setEmail("ray.sponsible@gmail.com");

        _properties.setProperty(StripeGateway.CONFIG_API_KEY, "???");

        // When
        VerifyAccountResponse resp = (VerifyAccountResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
    }
    public void test_verifyAccount_invalid_key () throws  Exception
    {
        // Given
        VerifyAccountRequest req = new VerifyAccountRequest();
        req.setEmail("herve.tchepannou@gmail.com");

        _properties.setProperty(StripeGateway.CONFIG_API_KEY, "???");

        // When
        VerifyAccountResponse resp = (VerifyAccountResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
    }


    //-- Direct payment
    public void test_directPayment () throws  Exception
    {
        // Given
        DirectPaymentRequest req = createDirectPaymentRequest();

        // When
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        // Then
        assertTrue(resp.isApproved());
        assertNull(resp.getCode());
        assertNotNull(resp.getTransactionId());
    }
    public void test_directPayment_card_declined () throws Exception
    {
        // Given
        _cc.setNumber("4000000000000002");
        DirectPaymentRequest req = createDirectPaymentRequest();

        // When
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
        assertEquals("card_declined", resp.getCode());
        assertNull(resp.getTransactionId());
    }
    public void test_directPayment_bad_cvc () throws Exception
    {
        // Given
        _cc.setNumber("4000000000000127");
        DirectPaymentRequest req = createDirectPaymentRequest();

        // When
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
        assertEquals("incorrect_cvc", resp.getCode());
        assertNull(resp.getTransactionId());
    }
    public void test_directPayment_expired_card () throws Exception
    {
        // Given
        _cc.setNumber("4000000000000069");
        DirectPaymentRequest req = createDirectPaymentRequest();

        // When
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
        assertEquals("expired_card", resp.getCode());
        assertNull(resp.getTransactionId());
    }
    public void test_directPayment_processing_error () throws Exception
    {
        // Given
        _cc.setNumber("4000000000000119");
        DirectPaymentRequest req = createDirectPaymentRequest();

        // When
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        // Then
        assertFalse(resp.isApproved());
        assertEquals("processing_error", resp.getCode());
        assertNull(resp.getTransactionId());
    }



    //-- Private
    private DirectPaymentRequest createDirectPaymentRequest ()
    {
        DirectPaymentRequest req = new DirectPaymentRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1500);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        req.setBillingAddress(_billingAddress);

        return req;
    }
    private boolean isEmpty (String str)
    {
        return str == null || str.length () == 0;
    }
}
