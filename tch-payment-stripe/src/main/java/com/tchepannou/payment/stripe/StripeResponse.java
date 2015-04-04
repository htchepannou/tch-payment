package com.tchepannou.payment.stripe;

import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.VerifyAccountResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * User: herve
 * Date: 14-07-20 2:08 PM
 */
public class StripeResponse
        implements DirectPaymentResponse, VerifyAccountResponse
{
    //-- Attributes
    private String authorizationCode;
    private boolean approved;
    private String code;
    private String reasonText;
    private String transactionId;
    private List<Option> options = new ArrayList<Option>();
    private List<String> currencies = new ArrayList<String>();
    
    //-- Constructor
    public StripeResponse ()
    {        
    }
    
    //-- DirectResponse override
    @Override    
    public String getAuthorizationCode()
    {
        return authorizationCode;
    }

    @Override
    public boolean isApproved()
    {
        return approved;
    }

    @Override
    public String getCode()
    {
        return code;
    }

    @Override
    public String getReasonText()
    {
        return reasonText;
    }

    @Override
    public String getTransactionId()
    {
        return transactionId;
    }

    @Override
    public boolean supportOption(Option option)
    {
        return options.contains(option);
    }

    @Override
    public boolean supportsCurrency(String currencyCode)
    {
        return currencies.contains(currencyCode);
    }

    public static class Builder
    {
        private StripeResponse _resp = new StripeResponse();
        

        public StripeResponse build () { return _resp; }
        
        public Builder approved (boolean approved) { _resp.approved = approved; return this; }
        public Builder authorizationCode (String authorizationCode) { _resp.authorizationCode = authorizationCode; return this; }
        public Builder code (String code) { _resp.code = code; return this; }
        public Builder reasonText(String reasonText) { _resp.reasonText = reasonText; return this; }
        public Builder transactionId (String transactionId) { _resp.transactionId = transactionId ; return this; }
        public Builder currencies(List<String> currencies) { _resp.currencies = currencies; return this; }
        public Builder options(List<Option> options) { _resp.options = options; return this; }
    }
}
