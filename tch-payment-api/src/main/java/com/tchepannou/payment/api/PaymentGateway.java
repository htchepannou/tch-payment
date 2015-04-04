/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tchepannou.payment.api;

import java.io.IOException;
import java.util.Properties;

/**
 * This interface represent a payment gateway
 * 
 * @author herve
 */
public interface PaymentGateway
{
    /**
     * Initialize the gateway
     * 
     * @param properties
     */
    public void init (Properties properties);
    
    /**
     * Process the payment.
     * 
     * @param request payment request
     * @return payment response.
     * 
     * @throws java.io.IOException if any IO error occurs with the Payment broker
     */
    public PaymentResponse process (PaymentRequest request)
            throws IOException;
}
