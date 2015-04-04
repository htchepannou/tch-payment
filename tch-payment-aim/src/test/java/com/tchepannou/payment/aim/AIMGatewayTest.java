/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.aim;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.request.AuthorizeRequest;
import com.tchepannou.payment.api.request.CancelRequest;
import com.tchepannou.payment.api.request.CaptureRequest;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.RefundRequest;
import com.tchepannou.payment.api.response.AuthorizeResponse;
import com.tchepannou.payment.api.response.CancelResponse;
import com.tchepannou.payment.api.response.CaptureResponse;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.RefundResponse;
import java.util.Properties;
import junit.framework.TestCase;

/**
 *
 * @author herve
 */
public class AIMGatewayTest extends TestCase
{

    private AIMGateway _gateway;
    private CreditCard _cc;

    public AIMGatewayTest(String testName)
    {
        super(testName);
    }

    // TestCase overrides 
    @Override
    protected void setUp()
    {
        Properties props = new Properties();
        props.put("url", "https://test.authorize.net/gateway/transact.dll");
        props.put("x_login", "6zz6m5N4Et");
        props.put("x_tran_key", "9V9wUv6Yd92t27t5");

        _gateway = new AIMGateway();
        _gateway.init(props);
        _cc = new CreditCard();
        _cc.setExpiryMonth (12);
        _cc.setExpiryYear (2020);
        _cc.setType (CreditCard.TYPE_MASTERCARD);
        _cc.setNumber ("5555555555554444");
        _cc.setVerificationCode ("123");
    }

    // Test 
    public void testProcess_Authorize()
        throws Exception
    {
        AuthorizeRequest req = new AuthorizeRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1000 + 100 * Math.random());
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        req.setCustomerEmail ("foo@mail.com");
        req.setCustomerIP ("100.1.1.1");
        req.setCustomerId ("1");

        Address billing = new Address ();
        billing.setFirstName ("Herve");
        billing.setLastName ("Tchepannou");
        billing.setState ("QC");
        billing.setCountry ("CA");
        billing.setCity ("Montreal");
        billing.setStreet ("1100 Linton");
        billing.setZipCode ("H1X3H1");
        req.setBillingAddress (billing);

        AuthorizeResponse resp = (AuthorizeResponse)_gateway.process(req);

        /* Test */
        assertTrue("approved - " + resp.getReasonText(), resp.isApproved());
        assertFalse("no transaction ID", isEmpty(resp.getTransactionId()));
        assertFalse("no authorizationCode", isEmpty(resp.getAuthorizationCode()));
    }

    public void testProcess_Capture()
        throws Exception
    {
        /* auth */
        AuthorizeRequest req = new AuthorizeRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1000 + 100 * Math.random());
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        AuthorizeResponse resp = (AuthorizeResponse)_gateway.process(req);

        /* capture */
        CaptureRequest req2 = new CaptureRequest();
        req2.setTransactionId (resp.getTransactionId ());
        CaptureResponse resp2 = (CaptureResponse)_gateway.process(req2);

        /* Test */
        assertTrue("approved - " + resp2.getReasonText(), resp2.isApproved());
        assertFalse("no transaction ID", isEmpty(resp2.getTransactionId()));
    }

    public void testProcess_DirectPayment() throws Exception
    {
        DirectPaymentRequest req = new DirectPaymentRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1000 + 100 * Math.random());
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);

        /* Test */
        assertTrue("approved - " + resp.getReasonText(), resp.isApproved());
        assertFalse("no transaction ID", isEmpty(resp.getTransactionId()));
        assertFalse("no authorizationCode", isEmpty(resp.getAuthorizationCode()));
    }

    public void testProcess_Cancel() throws Exception
    {
        /* Auth + Capture */
        AuthorizeRequest req = new AuthorizeRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1000 + 100 * Math.random());
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        AuthorizeResponse resp = (AuthorizeResponse)_gateway.process(req);

        /* Cancel */
        CancelRequest req2 = new CancelRequest();
        req2.setTransactionId(resp.getTransactionId ());
        CancelResponse resp2 = (CancelResponse)_gateway.process(req2);

        /* Test */
        assertTrue("approved - " + resp2.getReasonText(), resp2.isApproved());
        assertFalse("no transaction ID", isEmpty(resp2.getTransactionId()));
    }

    public void testProcess_Refund() throws Exception
    {
        /* Auth + Capture */
        DirectPaymentRequest req = new DirectPaymentRequest();
        req.setPaymentMethod(_cc);
        req.setAmountValue(1000 + 100 * Math.random());
        req.setAmountCurrency("USD");
        req.setInvoiceNumber(String.valueOf(System.currentTimeMillis()));
        req.setInvoiceDescription(getName());
        DirectPaymentResponse resp = (DirectPaymentResponse)_gateway.process(req);
        
        /* Cancel */
        RefundRequest req2 = new RefundRequest();
        req2.setPaymentMethod(_cc);
        req2.setTransactionId(resp.getTransactionId ());
        req2.setAmountValue(req.getAmountValue()-100);
        req2.setAmountCurrency(req.getAmountCurrency());
        req2.setPaymentMethod (req.getPaymentMethod ());
        RefundResponse resp2 = (RefundResponse)_gateway.process(req2);

        /* Test */
//        assertTrue("approved - " + resp2.getReasonText(), resp2.isApproved());
//        assertFalse("no transaction ID", StringUtil.isEmpty(resp2.getTransactionId()));
    }
    
    private boolean isEmpty (String str)
    {
        return str == null || str.length () == 0;
    }
}
