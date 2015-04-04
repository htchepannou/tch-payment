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
public class AuthorizeRequest
        implements PaymentRequest
{
    //-- Attributes ----------
    private String  _invoiceNumber;
    private String  _invoiceDescription;
    private String  _customerIP;
    private String  _customerId;
    private String  _customerEmail;
    private Address _billingAddress;
    private double _amountValue;
    private String _amountCurrency;
    private PaymentMethod _paymentMethod;

    //-- Getter/Setter  -----    
    public String getInvoiceNumber()
    {
        return _invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this._invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDescription()
    {
        return _invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription)
    {
        this._invoiceDescription = invoiceDescription;
    }

    public String getCustomerIP()
    {
        return _customerIP;
    }

    public void setCustomerIP(String customerIP)
    {
        this._customerIP = customerIP;
    }

    public String getCustomerId()
    {
        return _customerId;
    }

    public void setCustomerId(String customerId)
    {
        this._customerId = customerId;
    }

    public String getCustomerEmail ()
    {
        return _customerEmail;
    }

    public Address getBillingAddress ()
    {
        return _billingAddress;
    }

    public void setCustomerEmail (String customerEmail)
    {
        this._customerEmail = customerEmail;
    }

    public void setBillingAddress (Address billingAddress)
    {
        this._billingAddress = billingAddress;
    }

    public double getAmountValue()
    {
        return _amountValue;
    }
    public void setAmountValue(double amountValue)
    {
        this._amountValue = amountValue;
    }

    public String getAmountCurrency()
    {
        return _amountCurrency;
    }
    public void setAmountCurrency(String amountCurrency)
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
