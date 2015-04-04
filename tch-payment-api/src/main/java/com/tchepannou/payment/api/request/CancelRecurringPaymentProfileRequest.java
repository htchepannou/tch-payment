/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.PaymentRequest;

/**
 * This class contains information for a payment authorization
 * 
 * @author herve
 */
public class CancelRecurringPaymentProfileRequest
        implements PaymentRequest
{
    //-- Attributes ----------
    private String _profileId;


    //-- Getter/Setter  -----    
    public String getProfileId ()
    {
        return _profileId;
    }

    public void setProfileId (String profileId)
    {
        this._profileId = profileId;
    }
}
