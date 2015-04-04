/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tchepannou.payment.api;

/**
 *
 * @author herve
 */
public class Address
{
    //-- Attributes
    private String _firstName;
    private String _lastName;
    private String _street;
    private String _city;
    private String _state;
    private String _zipCode;
    private String _country;


    //-- Constructor
    public Address ()
    {

    }

    //-- Getter/Setter
    public String getFirstName ()
    {
        return _firstName;
    }

    public String getLastName ()
    {
        return _lastName;
    }

    public String getStreet ()
    {
        return _street;
    }

    public String getCity ()
    {
        return _city;
    }

    public String getState ()
    {
        return _state;
    }

    public String getZipCode ()
    {
        return _zipCode;
    }

    public String getCountry ()
    {
        return _country;
    }

    public void setFirstName (String firstName)
    {
        this._firstName = firstName;
    }

    public void setLastName (String lastName)
    {
        this._lastName = lastName;
    }

    public void setStreet (String street)
    {
        this._street = street;
    }

    public void setCity (String city)
    {
        this._city = city;
    }

    public void setState (String state)
    {
        this._state = state;
    }

    public void setZipCode (String zipCode)
    {
        this._zipCode = zipCode;
    }

    public void setCountry (String country)
    {
        this._country = country;
    }

}
