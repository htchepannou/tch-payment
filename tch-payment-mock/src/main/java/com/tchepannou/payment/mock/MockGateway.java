/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.mock;

import com.tchepannou.payment.api.PaymentGateway;
import com.tchepannou.payment.api.PaymentRequest;
import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.response.AuthorizeResponse;
import com.tchepannou.payment.api.response.CancelRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.CancelResponse;
import com.tchepannou.payment.api.response.CaptureResponse;
import com.tchepannou.payment.api.response.CreateRecurringPaymentProfileResponse;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.RefundResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * AIM implementation of {@link PaymentResponse}.
 * 
 * @author herve
 */
public class MockGateway
    implements PaymentGateway,
               PaymentResponse,
               AuthorizeResponse,
               CancelResponse,
               CaptureResponse,
               DirectPaymentResponse,
               RefundResponse,
               CreateRecurringPaymentProfileResponse,
               CancelRecurringPaymentProfileResponse
{
    //-- Attribute
    private boolean _approved;
    private String _code;
    private String _transactionId;
    private String _reasonText;
    private String _authorizationCode;
    private String _profileId;

    //-- Public method
    public void init (Properties properties)
    {
    }

    public PaymentResponse process (PaymentRequest request)
        throws IOException
    {
        return this;
    }

    //-- Getter/Setter
    public boolean isApproved ()
    {
        return _approved;
    }

    public void setApproved (boolean approved)
    {
        this._approved = approved;
    }

    public String getCode ()
    {
        return _code;
    }

    public void setCode (String code)
    {
        this._code = code;
    }

    public String getTransactionId ()
    {
        return _transactionId;
    }

    public void setTransactionId (String transactionId)
    {
        this._transactionId = transactionId;
    }

    public String getReasonText ()
    {
        return _reasonText;
    }

    /** */
    public void setReasonText (String reasonText)
    {
        this._reasonText = reasonText;
    }

    public String getAuthorizationCode ()
    {
        return _authorizationCode;
    }

    public void setAuthorizationCode (String authorizationCode)
    {
        this._authorizationCode = authorizationCode;
    }

    public String getProfileId ()
    {
        return _profileId;
    }

    public void setProfileId (String profileId)
    {
        this._profileId = profileId;
    }
}
