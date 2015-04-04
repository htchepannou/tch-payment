/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.paypal;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.PaymentGateway;
import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.request.CancelRecurringPaymentProfileRequest;
import com.tchepannou.payment.api.request.CreateRecurringPaymentProfileRequest;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.RefundRequest;
import com.tchepannou.payment.api.request.UpdateRecurringPaymentsProfileRequest;
import com.tchepannou.payment.api.request.VerifyAccountRequest;
import com.tchepannou.payment.api.response.CancelRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.CreateRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.RefundResponse;
import junit.framework.TestCase;

import java.util.Date;
import java.util.Properties;

/**
 *
 * @author herve
 */
public class NVPGatewayTest extends TestCase
{
    private static String _profileId;
    private PaymentGateway _gateway;
    private CreditCard _cc;
    private Address _address;

    public NVPGatewayTest (String testName)
    {
        super (testName);
    }

    // TestCase overrides 
    @Override
    protected void setUp ()
    {
        System.out.println("\n\n== " + getName () + " ======================");
        
        Properties props = new Properties ();
        props.put (NVPGateway.PARAM_URL, "https://api-3t.sandbox.paypal.com/nvp");
//        props.put (NVPGateway.PARAM_USERNAME, "herve-facilitator_api1.tchepannou.com");
//        props.put (NVPGateway.PARAM_SIGNATURE, "AiPC9BjkCyDFQXbSkoZcgqH3hpacAmeiKiHFfoRhXf-WIBcJGzSaeBjq");
//        props.put (NVPGateway.PARAM_PASSWORD, "1405883488");
        props.put (NVPGateway.PARAM_USERNAME, "test_1267734062_biz_api1.gmail.com");
        props.put (NVPGateway.PARAM_SIGNATURE, "AGvHPZbhpAM.vQ3exKmQbgTICT2vAZiVn8maRKpqoJe7e5Yha2lOD6zg");
        props.put (NVPGateway.PARAM_PASSWORD, "1267734067");

        _gateway = new NVPGateway ();
        _gateway.init (props);

        _cc = new CreditCard ();
        _cc.setExpiryMonth (7);
        _cc.setExpiryYear (2015);
        _cc.setType (CreditCard.TYPE_VISA);
        _cc.setNumber ("4539498824096554");
        _cc.setVerificationCode ("123");

        _address = new Address ();
        _address.setFirstName ("Ray");
        _address.setLastName ("Sponsible");
//        _address.setCity ("New York");
//        _address.setCountry ("US");
//        _address.setState ("NY");
//        _address.setStreet ("3030 Linton");
//        _address.setZipCode ("111111");        
    }

    // Test
    public void testVefiryAccount () throws  Exception
    {
        // Given
        VerifyAccountRequest req = new VerifyAccountRequest();
        req.setEmail("herve.tchepannou@gmail.com");
        req.setFirstName("Herve");
        req.setLastName("Tchepannou");

        Properties props = new Properties ();
        props.put (NVPGateway.PARAM_URL, "https://api-3t.sandbox.paypal.com/nvp");
        props.put (NVPGateway.PARAM_USERNAME, "herve-facilitator_api1.tchepannou.com");
        props.put (NVPGateway.PARAM_SIGNATURE, "AiPC9BjkCyDFQXbSkoZcgqH3hpacAmeiKiHFfoRhXf-WIBcJGzSaeBjq");
        props.put (NVPGateway.PARAM_PASSWORD, "1405883488");
        props.put (NVPGateway.PARAM_SANDBOX_EMAIL_ADDRESS, "herve-facilitator@tchepannou.com");
        props.put (NVPGateway.PARAM_MODE, NVPGateway.VALUE_MODE_SANDBOX);
        _gateway.init (props);

        // When
        PaymentResponse resp = _gateway.process(req);

        // Then
        assertTrue(resp.isApproved());
    }
    public void testVerifyInvalidEmailAccount () throws Exception
    {
        // Given
        VerifyAccountRequest req = new VerifyAccountRequest();
        req.setEmail("ray.sponsible@gmail.com");
        req.setFirstName("Herve");
        req.setLastName("Tchepannou");

        Properties props = new Properties ();
        props.put (NVPGateway.PARAM_URL, "https://api-3t.sandbox.paypal.com/nvp");
        props.put (NVPGateway.PARAM_USERNAME, "herve-facilitator_api1.tchepannou.com");
        props.put (NVPGateway.PARAM_SIGNATURE, "AiPC9BjkCyDFQXbSkoZcgqH3hpacAmeiKiHFfoRhXf-WIBcJGzSaeBjq");
        props.put (NVPGateway.PARAM_PASSWORD, "1405883488");
        props.put (NVPGateway.PARAM_SANDBOX_EMAIL_ADDRESS, "herve-facilitator@tchepannou.com");
        props.put (NVPGateway.PARAM_MODE, NVPGateway.VALUE_MODE_SANDBOX);
        _gateway.init (props);

        // When
        PaymentResponse resp = _gateway.process(req);

        // Then
        assertFalse(resp.isApproved());

    }

    public void testDirectPayment ()
        throws Exception
    {
        DirectPaymentRequest req = new DirectPaymentRequest();
        req.setPaymentMethod(_cc);
        req.setBillingAddress (_address);
        req.setAmountValue(1500.2);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        PaymentResponse resp = _gateway.process(req);

        /* Test */
        assertTrue("approved - " + resp.getReasonText(), resp.isApproved());
        assertFalse("no transaction ID", isEmpty(resp.getTransactionId()));
    }

    public void testRefund () throws Exception
    {
        /* payment */
        DirectPaymentRequest req = new DirectPaymentRequest();
        req.setPaymentMethod(_cc);
        req.setBillingAddress (_address);
        req.setAmountValue(1500.2);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        PaymentResponse resp = _gateway.process(req);

        assertTrue("approved - " + resp.getReasonText(), resp.isApproved());

        /* refund */
        RefundRequest r = new RefundRequest ();
        r.setAmountValue (1000);
        r.setAmountCurrency ("USD");
        r.setTransactionId (resp.getTransactionId ());
        RefundResponse resp2 = (RefundResponse)_gateway.process(r);

        /* Test */
        assertTrue("approved - " + resp2.getReasonText() + " - " + r.getTransactionId (), resp2.isApproved());
        assertFalse("no transaction ID", isEmpty(resp.getTransactionId()));
    }
    

    public void testCreateRecurringPaymentsProfileRequest () throws Exception
    {
        CreateRecurringPaymentProfileRequest req = new CreateRecurringPaymentProfileRequest ();
        req.setPaymentMethod(_cc);
        req.setBillingAddress (_address);
        req.setAmountValue(500);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setDescription (getName ());
        req.setStartDate (new Date ());
        req.setBillingPeriod (CreateRecurringPaymentProfileRequest.PERIOD_MONTHLY);
        CreateRecurringPaymentProfileResponse resp = (CreateRecurringPaymentProfileResponse)_gateway.process(req);
        System.out.println ("Result: " + resp.getCode () + " - " + resp.getReasonText ());
        System.out.println ("profileID: " + resp.getProfileId ());
        
        /* Test */
        assertTrue("approved: " + resp.getReasonText(), resp.isApproved());
        assertFalse("no profile ID", isEmpty(resp.getProfileId ()));
        
        _profileId = resp.getProfileId ();
    }
    public void testCreateRecurringPaymentsProfileRequestWithTrial () throws Exception
    {
        CreateRecurringPaymentProfileRequest req = new CreateRecurringPaymentProfileRequest();
        req.setPaymentMethod(_cc);
        req.setBillingAddress (_address);
        req.setAmountValue(500);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setDescription (getName ());
        req.setStartDate (new Date ());
        req.setBillingPeriod (CreateRecurringPaymentProfileRequest.PERIOD_MONTHLY);
        req.setTrialAmountValue (400);
        req.setTrialBillingFrequency (req.getBillingFrequency ());
        req.setTrialBillingPeriod (req.getBillingPeriod ());
        req.setTrialTotalBillingCycles (1);
        CreateRecurringPaymentProfileResponse resp = (CreateRecurringPaymentProfileResponse)_gateway.process(req);
        System.out.println ("Result: " + resp.getCode () + " - " + resp.getReasonText ());
        System.out.println ("profileID: " + resp.getProfileId ());

        /* Test */
        assertTrue("approved: " + resp.getReasonText(), resp.isApproved());
        assertFalse("no profile ID", isEmpty(resp.getProfileId ()));
        
        _profileId = resp.getProfileId ();
    }
    
    public void testUpdateRecurringPaymentProfile_UpdateCreditCard () throws Exception
    {
        UpdateRecurringPaymentsProfileRequest req = new UpdateRecurringPaymentsProfileRequest ();
        CreditCard cc = new CreditCard ();
        cc.setExpiryMonth (1);
        cc.setExpiryYear (2020);
        cc.setNumber ("4344081718055715");
        cc.setType ("VISA");
        cc.setVerificationCode ("123");
        
        req.setProfileId (_profileId);
        req.setPaymentMethod (cc);
        CreateRecurringPaymentProfileResponse resp = (CreateRecurringPaymentProfileResponse)_gateway.process(req);
        System.out.println ("Result: " + resp.getCode () + " - " + resp.getReasonText ());
        System.out.println ("profileID: " + resp.getProfileId ());

        /* Test */
        assertTrue("approved: " + resp.getReasonText(), resp.isApproved());
    }
    
    public void testCancelRecurringPaymentsProfileRequestWithTrial () throws Exception
    {
        CancelRecurringPaymentProfileRequest req = new CancelRecurringPaymentProfileRequest();
        req.setProfileId (_profileId);
        CancelRecurringPaymentProfileResponse resp = (CancelRecurringPaymentProfileResponse)_gateway.process(req);
        System.out.println ("Result: " + resp.getCode () + " - " + resp.getReasonText ());

        /* Test */
        assertTrue("approved: " + resp.getReasonText(), resp.isApproved());
        assertFalse("no profile ID", isEmpty(resp.getProfileId ()));
    }    

    public void testCancelRecurringPaymentsProfileRequest () throws Exception
    {
        CreateRecurringPaymentProfileRequest req = new CreateRecurringPaymentProfileRequest ();
        req.setPaymentMethod(_cc);
//        req.setBillingAddress (_address);
        req.setAmountValue(500);
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setDescription (getName ());
        req.setStartDate (new Date ());
        req.setBillingPeriod (CreateRecurringPaymentProfileRequest.PERIOD_MONTHLY);
        CreateRecurringPaymentProfileResponse resp = (CreateRecurringPaymentProfileResponse)_gateway.process(req);

        assertTrue("approved - " + resp.getReasonText(), resp.isApproved());

        CancelRecurringPaymentProfileRequest r = new CancelRecurringPaymentProfileRequest ();
        r.setProfileId (resp.getProfileId ());
        CancelRecurringPaymentProfileResponse resp2 = (CancelRecurringPaymentProfileResponse)_gateway.process(r);

        /* Test */
        assertTrue("approved - " + resp2.getReasonText(), resp2.isApproved());
        assertFalse("no profile ID", isEmpty(resp2.getProfileId ()));
    }
    
    private boolean isEmpty (String str)
    {
        return str == null || str.length () == 0;
    }
}
