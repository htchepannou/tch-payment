/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.aim;

import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.response.AuthorizeResponse;
import com.tchepannou.payment.api.response.CancelResponse;
import com.tchepannou.payment.api.response.CaptureResponse;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.RefundResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * AIM implementation of {@link PaymentResponse}.
 * 
 * @author herve
 */
public class AIMPResponse
    implements PaymentResponse,
               AuthorizeResponse,
               CancelResponse,
               CaptureResponse,
               DirectPaymentResponse,
               RefundResponse
{
    //~ Static fields/initializers --------------
    public static final String CODE_APPROVED = "1";
    private static final int INDEX_CODE = 1;
    private static final int INDEX_REASON_TEXT = 4;
    private static final int INDEX_AUTHORIZATION_CODE = 5;
    private static final int INDEX_TRANSACTION_ID = 7;

    //~ Instance fields ---------------------------
    private Map<String, String> _requestData = new HashMap<String, String>();
    private String[] _tokens;

    //--  Constructor  ----------------------------
    private AIMPResponse (Map<String, String> requestData)
    {
        _requestData = requestData;
    }

    //~ Methods -----------------------------------
    public static AIMPResponse parse (String text, String separator, Map<String, String> requestData)
    {
        AIMPResponse response = new AIMPResponse (requestData);
        response._tokens = text.split (separator);

        return response;
    }

    //-- PaymentResponse overrides -------------
    public boolean isApproved ()
    {
        return CODE_APPROVED.equals (getCode ());
    }

    public String getCode ()
    {
        return getToken(INDEX_CODE);
    }

    public String getReasonText ()
    {
        return getToken (INDEX_REASON_TEXT);
    }

    public String getTransactionId ()
    {
        return getToken (INDEX_TRANSACTION_ID);
    }

    public String getAuthorizationCode ()
    {
        return getToken (INDEX_AUTHORIZATION_CODE);
    }

    public Map<String, String> getRequestData() {
        return _requestData;
    }

    public String getResponseData (){
        StringBuilder sb = new StringBuilder();
        for (String token : _tokens){
            if (sb.length()>0){
                sb.append(',');
            }
            sb.append(token);
        }
        return sb.toString();
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
    private String getToken (int index)
    {
        return ( String ) _tokens[index - 1];
    }
}
