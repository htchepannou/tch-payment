/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.PaymentMethod;
import com.tchepannou.payment.api.PaymentRequest;

/**
 * This class contains information for refunds
 * 
 * @author herve
 */
public class RefundRequest
    implements PaymentRequest
{
    //-- Attributes -------------
    private String _transactionId;
    private double _amountValue;
    private String _amountCurrency;
    private PaymentMethod _paymentMethod;

    //-- Getter/Setter  --------
    public String getTransactionId ()
    {
        return _transactionId;
    }
    public void setTransactionId (String transactionId)
    {
        this._transactionId = transactionId;
    }

    public double getAmountValue ()
    {
        return _amountValue;
    }
    public void setAmountValue (double amountValue)
    {
        this._amountValue = amountValue;
    }

    public String getAmountCurrency ()
    {
        return _amountCurrency;
    }
    public void setAmountCurrency (String amountCurrency)
    {
        this._amountCurrency = amountCurrency;
    }

    public PaymentMethod getPaymentMethod ()
    {
        return _paymentMethod;
    }
    public void setPaymentMethod (PaymentMethod paymentMethod)
    {
        _paymentMethod = paymentMethod;
    }
}
