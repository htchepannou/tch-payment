/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tchepannou.payment.api.response;

import com.tchepannou.payment.api.PaymentResponse;

/**
 *
 * @author herve
 */
public interface AuthorizeResponse
    extends PaymentResponse
{
    public String getAuthorizationCode ();
}
