/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.paypal;

import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.response.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Authorize.NET implementation of {@link PaymentResponse}.
 * 
 * @author herve
 */
public class NVPResponse
    implements PaymentResponse,
               AuthorizeResponse,
               BillOutstandingPaymentResponse,
               CancelRecurringPaymentProfileResponse,
               CancelResponse,
               CaptureResponse,
               CreateRecurringPaymentProfileResponse,
               DirectPaymentResponse,
               RefundResponse,
               UpdateRecurringPaymentsProfileResponse
{
    //~ Static fields/initializers --------------
    public static final String SUCCESS = "success";
    public static final String INDEX_ACK = "ACK";
    public static final String INDEX_CODE = "L_ERRORCODE0";
    public static final String L_LONGMESSAGE0 = "L_LONGMESSAGE0";
    public static final String L_SHORTMESSAGE0 = "L_SHORTMESSAGE0";
    public static final String INDEX_TRANSACTION_ID = "TRANSACTIONID";
    public static final String INDEX_REFUNDTRANSACTION_ID = "REFUNDTRANSACTIONID";
    public static final String INDEX_PROFILE_ID = "PROFILEID";
    public static final String INVOICE_NUMBER = "INVNUM";
    
    
    //~ Instance fields ---------------------------
    private Map<String, String> _responseData = new HashMap<String, String> ();
    private Map<String, String> _requestData = new HashMap<String, String> ();

    //--  Constructor  ----------------------------
    private NVPResponse (Map<String, String> requestData)
    {
        _requestData = requestData;
    }
    public NVPResponse (Map<String, String> requestData, Map<String, String> responseData)
    {
        _requestData = requestData;
        _responseData = responseData;
    }

    //~ Methods -----------------------------------
    public static NVPResponse parse (String text, Map<String, String> requestData)
        throws IOException
    {
        NVPResponse response = new NVPResponse (requestData);
        String[] tokens = text.split ("&");
        Map<String, String> map = new HashMap<String, String> ();
        for ( String token: tokens )
        {
            String[] nvp = token.split ("=");
            String name = nvp[0];
            String value = URLDecoder.decode (nvp[1], "UTF-8");
            map.put (name, value);
        }
        response._responseData = map;

        return response;
    }

    //-- PaymentResponse overrides -------------
    public boolean isApproved ()
    {
        return SUCCESS.equalsIgnoreCase (_responseData.get (INDEX_ACK));
    }

    public String getCode ()
    {
        return getToken (INDEX_CODE);
    }

    public String getReasonText ()
    {
        String shortMessage = getToken (L_SHORTMESSAGE0);
        String longMessage = getToken (L_LONGMESSAGE0);

        return shortMessage + ". " + longMessage;
    }

    public String getTransactionId ()
    {
        String txid = getToken (INDEX_TRANSACTION_ID);
        return txid == null || txid.length () == 0
            ? getToken (INDEX_REFUNDTRANSACTION_ID)
            : txid;
    }

    public String getAuthorizationCode ()
    {
        return null;
    }

    public String getProfileId ()
    {
        return getToken (INDEX_PROFILE_ID);
    }

    public String getInvoiceNumber ()
    {
        return getToken (INVOICE_NUMBER);
    }

    public Map<String, String> getRequestData ()
    {
        return Collections.unmodifiableMap (_requestData);
    }

    public Map<String, String> getResponseData ()
    {
        return Collections.unmodifiableMap (_responseData);
    }

    //-- Object overrides  ----------------------
    @Override
    public String toString ()
    {
        return "code=" + getCode ()
            + ", reasonText=" + getReasonText ()
            + ", transactionId=" + getTransactionId ()
            + ", authorizationCode=" + getAuthorizationCode ();
    }

    //-- Private Methods  -----------------------
    private String getToken (String index)
    {
        return _responseData.get (index);
    }
}
