/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.PaymentRequest;

/**
 * This class contains information for cancelling a request.
 * 
 * @author herve
 */
public class CancelRequest
        implements PaymentRequest
{
    //-- Attributes -------------
    private String _transactionId;

    
    //-- Getter/Setter  --------
    public String getTransactionId()
    {
        return _transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this._transactionId = transactionId;
    }
}
