/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.PaymentRequest;

/**
 *
 * @author herve
 */
public class BillOutstandingAmountRequest
    implements PaymentRequest
{
    //-- Attributes
    private String _profileId;
    private double _amountValue;
    private String _amountCurrency;

    //-- Getter/Setter

    public String getProfileId ()
    {
        return _profileId;
    }

    public double getAmountValue ()
    {
        return _amountValue;
    }

    public String getAmountCurrency ()
    {
        return _amountCurrency;
    }

    public void setProfileId (String profileId)
    {
        this._profileId = profileId;
    }

    public void setAmountValue (double amountValue)
    {
        this._amountValue = amountValue;
    }

    public void setAmountCurrency (String amountCurrency)
    {
        this._amountCurrency = amountCurrency;
    }
    
}
