package com.tchepannou.payment.api.request;

import com.tchepannou.payment.api.PaymentRequest;

/**
 * Test the account
 */
public class VerifyAccountRequest implements PaymentRequest
{
    //-- Attributes
    private String firstName;
    private String lastName;
    private String email;


    //-- Getter/Setter
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}

