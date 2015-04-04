/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.api;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This interface represent a service that handle payments.
 * 
 * @author herve
 */
public class PaymentService
{
    //-- Attributes
    private PaymentGateway _gateway;
    private List<PaymentListener> _listeners;

    //-- Constructor
    public PaymentService (PaymentGateway gateway)
    {
        _gateway = gateway;
        _listeners = new LinkedList<PaymentListener> ();
    }


    //-- PaymentService overrides
    public PaymentResponse process (PaymentRequest request)
        throws IOException
    {
        PaymentResponse response = _gateway.process (request);
        notifyListeners (request, response);
        return response;
    }

    public void addPaymentListener (PaymentListener listener)
    {
        _listeners.add(listener);
    }
    
    public void removeAllPaymentListeners ()
    {
        _listeners.clear ();
    }


    //-- Private method
    private void notifyListeners (PaymentRequest request, PaymentResponse response)
    {
        for (PaymentListener listener : _listeners)
        {
            listener.onProcess (request, response);
        }
    }
}