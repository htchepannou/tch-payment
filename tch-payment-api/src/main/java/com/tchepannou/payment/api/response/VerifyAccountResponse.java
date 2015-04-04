package com.tchepannou.payment.api.response;

import com.tchepannou.payment.api.PaymentResponse;

/**
 * User: herve
 * Date: 14-07-20 2:40 PM
 */
public interface VerifyAccountResponse extends PaymentResponse
{
    public static enum Option
    {
        checkout_button,
        direct_payment,
        recurring_payment
    }

    boolean supportOption (Option option);
    boolean supportsCurrency (String currencyCode);
}
