/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.PaymentMethod;
import com.tchepannou.payment.api.PaymentRequest;
import java.util.Date;

/**
 * This class contains information for a payment authorization
 * 
 * @author herve
 */
public class CreateRecurringPaymentProfileRequest
        implements PaymentRequest
{
    //-- Static Attribute
    public static final String PERIOD_MONTHLY = "Month";
    public static final String PERIOD_YEARLY = "Year";
    public static final String PERIOD_WEEKLY = "Week";


   //-- Attributes ----------
    private Address _billingAddress;
    private Date _startDate;
    private String _invoiceNumber;
    private String _description;
    private String _customerId;
    private String _customerEmail;
    private String _billingPeriod;
    private int _billingFrequency;
    private double _amountValue;
    private String _amountCurrency;
    private String _trialBillingPeriod;
    private int _trialBillingFrequency;
    private int _trialTotalBillingCycles;
    private double _trialAmountValue;
    private String _trialAmountCurrency;
    private PaymentMethod _paymentMethod;


    //-- Getter/Setter
    public Address getBillingAddress ()
    {
        return _billingAddress;
    }

    public void setBillingAddress (Address billingAddress)
    {
        this._billingAddress = billingAddress;
    }

    public Date getStartDate ()
    {
        return _startDate;
    }

    public void setStartDate (Date startDate)
    {
        this._startDate = startDate;
    }

    public String getInvoiceNumber ()
    {
        return _invoiceNumber;
    }

    public void setInvoiceNumber (String invoiceNumber)
    {
        this._invoiceNumber = invoiceNumber;
    }

    public String getDescription ()
    {
        return _description;
    }

    public void setDescription (String description)
    {
        this._description = description;
    }

    public String getBillingPeriod ()
    {
        return _billingPeriod;
    }

    public void setBillingPeriod (String billingPeriod)
    {
        this._billingPeriod = billingPeriod;
    }

    public String getCustomerEmail ()
    {
        return _customerEmail;
    }

    public void setCustomerEmail (String customerEmail)
    {
        this._customerEmail = customerEmail;
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

    public String getCustomerId ()
    {
        return _customerId;
    }

    public String getTrialBillingPeriod ()
    {
        return _trialBillingPeriod;
    }

    public double getTrialAmountValue ()
    {
        return _trialAmountValue;
    }

    public String getTrialAmountCurrency ()
    {
        return _trialAmountCurrency;
    }

    public void setCustomerId (String customerId)
    {
        this._customerId = customerId;
    }

    public void setTrialBillingPeriod (String trialBillingPeriod)
    {
        this._trialBillingPeriod = trialBillingPeriod;
    }

    public void setTrialAmountValue (double trialAmountValue)
    {
        this._trialAmountValue = trialAmountValue;
    }

    public void setTrialAmountCurrency (String trialAmountCurrency)
    {
        this._trialAmountCurrency = trialAmountCurrency;
    }

    public int getBillingFrequency ()
    {
        return _billingFrequency;
    }

    public void setBillingFrequency (int billingFrequency)
    {
        this._billingFrequency = billingFrequency;
    }

    public int getTrialBillingFrequency ()
    {
        return _trialBillingFrequency;
    }

    public void setTrialBillingFrequency (int trialBillingFrequency)
    {
        this._trialBillingFrequency = trialBillingFrequency;
    }

    public int getTrialTotalBillingCycles ()
    {
        return _trialTotalBillingCycles;
    }

    public void setTrialTotalBillingCycles (int trialTotalBillingCycles)
    {
        this._trialTotalBillingCycles = trialTotalBillingCycles;
    }
}