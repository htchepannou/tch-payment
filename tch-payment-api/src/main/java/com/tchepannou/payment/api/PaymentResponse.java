/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api;

/**
 * This class encapsulate the response from a payment request
 * 
 * @author herve
 */
public interface PaymentResponse
{
    //-- Getter/Setter ----------------
    public boolean isApproved ();
    
    public String getCode ();
    
    public String getReasonText ();

    public String getTransactionId ();
}
