/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tchepannou.payment.api;

/**
 *
 * @author herve
 */
public class CreditCard 
        implements PaymentMethod
{
    //-- Static Attribute
    public  static final String TYPE_VISA = "Visa";
    public  static final String TYPE_MASTERCARD = "MasterCard";
    public  static final String TYPE_DISCOVER = "Discover";
    public  static final String TYPE_AMEX = "Amex";
    public  static final String TYPE_SWITCH = "Switch";
    public  static final String TYPE_SOLO = "Solo";

    //-- Attributes  --------------------
    private String _type;
    private String _number;
    private int    _expiryMonth;
    private int    _expiryYear;
    private String _verificationCode;

    //-- Constructors  ------------------
    public CreditCard ()
    {
        
    }
    
    //-- Getter/Setter  -----------------
    public String getMaskedNumber ()
    {
        StringBuilder sb = new StringBuilder ();
        int len = _number.length ();
        int mid = len - 4;
        for (int i=0 ; i<len ; i++)
        {
            if (i < mid)
            {
                sb.append('X');
            }
            else
            {
                sb.append (_number.charAt (i));
            }
        }
        return sb.toString ();
    }

    public String getNumber()
    {
        return _number;
    }

    public void setNumber(String number)
    {
        this._number = number;
    }

    public String getVerificationCode()
    {
        return _verificationCode;
    }

    public void setVerificationCode(String verificationCode)
    {
        this._verificationCode = verificationCode;
    }

    /**
     * @return the _type
     */
    public String getType ()
    {
        return _type;
    }

    /**
     * @param type the _type to set
     */
    public void setType (String type)
    {
        this._type = type;
    }

    /**
     * @return the _expiryMonth
     */
    public int getExpiryMonth ()
    {
        return _expiryMonth;
    }

    /**
     * @param expiryMonth the _expiryMonth to set
     */
    public void setExpiryMonth (int expiryMonth)
    {
        this._expiryMonth = expiryMonth;
    }

    /**
     * @return the _expiryYear
     */
    public int getExpiryYear ()
    {
        return _expiryYear;
    }

    /**
     * @param expiryYear the _expiryYear to set
     */
    public void setExpiryYear (int expiryYear)
    {
        this._expiryYear = expiryYear;
    }
}
