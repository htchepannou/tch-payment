/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.PaymentMethod;
import com.tchepannou.payment.api.PaymentRequest;

/**
 * This class contains information for a payment authorization
 * 
 * @author herve
 */
public class UpdateRecurringPaymentsProfileRequest
    implements PaymentRequest
{
    //-- Attributes ----------
    private String _profileId;
    private double _amountValue;
    private String _amountCurrency;
    private PaymentMethod _paymentMethod;
    private Address _billingAddress;
    private String  _description;
    private String  _customerEmail;
    private double _outstandingAmount = -1;


    //-- Getter/Setter
    public Address getBillingAddress ()
    {
        return _billingAddress;
    }

    public void setBillingAddress (Address billingAddress)
    {
        this._billingAddress = billingAddress;
    }

    public String getDescription ()
    {
        return _description;
    }

    public void setDescription (String description)
    {
        this._description = description;
    }

    public String getCustomerEmail ()
    {
        return _customerEmail;
    }

    public void setCustomerEmail (String customerEmail)
    {
        this._customerEmail = customerEmail;
    }

    public String getProfileId ()
    {
        return _profileId;
    }

    public void setProfileId (String profileId)
    {
        this._profileId = profileId;
    }

    public double getOutstandingAmount ()
    {
        return _outstandingAmount;
    }

    public void setOutstandingAmount (double outstandingAmount)
    {
        this._outstandingAmount = outstandingAmount;
    }

    public double getAmountValue ()
    {
        return _amountValue;
    }

    public String getAmountCurrency ()
    {
        return _amountCurrency;
    }

    public PaymentMethod getPaymentMethod ()
    {
        return _paymentMethod;
    }

    public void setAmountValue (double amountValue)
    {
        this._amountValue = amountValue;
    }

    public void setAmountCurrency (String amountCurrency)
    {
        this._amountCurrency = amountCurrency;
    }

    public void setPaymentMethod (PaymentMethod paymentMethod)
    {
        this._paymentMethod = paymentMethod;
    }

}