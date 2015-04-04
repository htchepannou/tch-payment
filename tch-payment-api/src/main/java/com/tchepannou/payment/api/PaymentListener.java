/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tchepannou.payment.api;

/**
 * This listener if called when a payment is performed
 * 
 * @author herve
 */
public interface PaymentListener
{
    /**
     * callback method called when a payment is performed
     *
     * @param req Payment request
     * @param resp Payment response
     */
    public void onProcess (PaymentRequest req, PaymentResponse resp);
}
