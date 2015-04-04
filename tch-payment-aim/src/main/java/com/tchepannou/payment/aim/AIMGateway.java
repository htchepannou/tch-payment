/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tchepannou.payment.aim;

import com.tchepannou.payment.api.Address;
import com.tchepannou.payment.api.CreditCard;
import com.tchepannou.payment.api.PaymentGateway;
import com.tchepannou.payment.api.PaymentMethod;
import com.tchepannou.payment.api.PaymentRequest;
import com.tchepannou.payment.api.PaymentResponse;
import com.tchepannou.payment.api.request.AuthorizeRequest;
import com.tchepannou.payment.api.request.CancelRequest;
import com.tchepannou.payment.api.request.CaptureRequest;
import com.tchepannou.payment.api.request.DirectPaymentRequest;
import com.tchepannou.payment.api.request.RefundRequest;
import com.tchepannou.payment.api.response.AuthorizeResponse;
import com.tchepannou.payment.api.response.CaptureResponse;
import com.tchepannou.payment.api.response.DirectPaymentResponse;
import com.tchepannou.payment.api.response.RefundResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * Implementation of {@link PaymentGateway} for Authorize.NET AIM.
 * 
 * @author herve
 */
public class AIMGateway
        implements PaymentGateway
{
    //-- Static Attributes  -------------------    
    private static final String UTF8 = "UTF-8";
    private static final DecimalFormat AMOUNT_FORMATER = new DecimalFormat ("0.##");

    public static final String PARAM_URL = "url";
    public static final String PARAM_PORT = "port";

    public static final String FIELD_TEST_REQUEST = "x_test_request";
    public static final String FIELD_VERSION = "x_version";
    public static final String FIELD_DELIM_DATA = "x_delim_data";
    public static final String FIELD_DELIM_CHAR = "x_delim_char";
    public static final String FIELD_RELAY_RESPONSE = "x_relay_response";
    public static final String FIELD_LOGIN = "x_login";
    public static final String FIELD_TRAN_KEY = "x_tran_key";
    public static final String FIELD_EMAIL = "x_email";
    public static final String FIELD_EMAIL_CUSTOMER = "x_email_customer";
    public static final String FIELD_EMAIL_MERCHAND = "x_merchand_email";
    public static final String FIELD_METHOD = "x_method";
    public static final String FIELD_CURRENCY_CODE = "x_currency_code";
    public static final String FIELD_TYPE = "x_type";
    public static final String FIELD_AMOUNT = "x_amount";
    public static final String FIELD_AUTH_CODE = "x_auth_code";
    public static final String FIELD_TRANS_ID = "x_trans_id";
    public static final String FIELD_INVOICE_NUM = "x_invoice_num";
    public static final String FIELD_INVOICE_DESC = "x_description";
    public static final String FIELD_CUSTOMER_ID = "x_cust_id";
    public static final String FIELD_CUSTOMER_IP = "x_customer_ip";
    public static final String FIELD_CARD_NUM = "x_card_num";
    public static final String FIELD_EXP_DATE = "x_exp_date";
    public static final String FIELD_CARD_CODE = "x_card_code";
    public static final String FIELD_BILL_TO_FIRSTNAME = "x_first_name";
    public static final String FIELD_BILL_TO_LASTNAME = "x_last_name";
    public static final String FIELD_BILL_TO_ADDRESS = "x_address";
    public static final String FIELD_BILL_TO_CITY = "x_city";
    public static final String FIELD_BILL_TO_STATE = "x_state";
    public static final String FIELD_BILL_TO_ZIP = "x_zip";
    public static final String FIELD_BILL_TO_COUNTRY = "x_country";
    public static final String FIELD_BILL_TO_PHONE = "x_phone";
    public static final String VALUE_TRUE = "TRUE";
    public static final String VALUE_FALSE = "FALSE";
    public static final String VALUE_VERSION = "3.1";
    public static final String VALUE_DELIM_CHAR = ",";
    public static final String VALUE_TYPE_AUTH = "AUTH_ONLY";
    public static final String VALUE_TYPE_AUTH_CAPTURE = "AUTH_CAPTURE";
    public static final String VALUE_TYPE_CAPTURE = "PRIOR_AUTH_CAPTURE";
    public static final String VALUE_TYPE_VOID = "VOID";
    public static final String VALUE_TYPE_CREDIT = "CREDIT";
    public static final String VALUE_METHOD_CC = "CC";
    public static final String VALUE_URL = "https://secure.authorize.net/gateway/transact.dll";
    public static final String VALUE_PORT = "443";


    //-- Properties  --------------------------
    private Properties _properties;

    //-- AIMGateway overrides  -------
    /**
     * Initialize the gateway.
     * The gateway configuration parameters of the gateway are:
     * <ul>
     *  <li> <code>url</code>: VALUE_URL of the gateway. if not provided, the request will be sent to <code>https://secure.authorize.net/gateway/transact.dll</code>
     *  <li> <code>x_login</code>: The merchand's login ID. (this is provided by Authorize.NET)
     *  <li> <code>x_tran_key</code>: The merchand's transactions key. (this is provided by Authorize.NET)
     *  <li> <code>x_test_request</code>: (TRUE/FALSE). It indicates if the transaction should be treated as test requests.
     *      Useful for testing/debugging, must be set to FALSE on production environment
     * </ul>
     * 
     * @param properties Configuration properties.
     */
    public void init(Properties properties)
    {
        _properties = properties;
    }

    public PaymentResponse process(PaymentRequest request)
            throws IOException
    {
        if (request instanceof AuthorizeRequest)
        {
            return doAuthorize((AuthorizeRequest) request);
        }
        else if (request instanceof CaptureRequest)
        {
            return doCapture((CaptureRequest) request);
        }
        else if (request instanceof DirectPaymentRequest)
        {
            return doDirectPayment((DirectPaymentRequest) request);
        }
        else if (request instanceof CancelRequest)
        {
            return doCancel((CancelRequest) request);
        }
        else if (request instanceof RefundRequest)
        {
            return doRefund((RefundRequest) request);
        }
        else
        {
            throw new IllegalStateException("Invalid request: " + request);
        }
    }

    //-- Private methods  -------------------
    private AuthorizeResponse doAuthorize(AuthorizeRequest request)
            throws IOException
    {
        Map<String, String> params = new HashMap<String, String>();
        initParameters(params);

        put(FIELD_TYPE, VALUE_TYPE_AUTH, params);
        put(FIELD_CUSTOMER_IP, request.getCustomerIP(), params);
        put(FIELD_CUSTOMER_ID, request.getCustomerId(), params);
        put(FIELD_EMAIL, request.getCustomerEmail (), params);
        put(FIELD_INVOICE_NUM, request.getInvoiceNumber(), params);
        put(FIELD_INVOICE_DESC, request.getInvoiceDescription(), params);
        put(FIELD_EMAIL_CUSTOMER, VALUE_TRUE, params);
        put(FIELD_AMOUNT, formatAmount(request.getAmountValue()), params);
        put(FIELD_CURRENCY_CODE, request.getAmountCurrency(), params);

        Address billing = request.getBillingAddress ();
        if (billing != null)
        {
            put(FIELD_BILL_TO_ADDRESS, billing.getStreet (), params);
            put(FIELD_BILL_TO_CITY, billing.getCity (), params);
            put(FIELD_BILL_TO_COUNTRY, billing.getCountry (), params);
            put(FIELD_BILL_TO_FIRSTNAME, billing.getFirstName (), params);
            put(FIELD_BILL_TO_LASTNAME, billing.getLastName (), params);
            put(FIELD_BILL_TO_STATE, billing.getState (), params);
            put(FIELD_BILL_TO_ZIP, billing.getZipCode (), params);
        }

        PaymentMethod method = request.getPaymentMethod ();
        initParameters (request, method, params);

        return doProcess(params);
    }

    private CaptureResponse doCapture(CaptureRequest request)
            throws IOException
    {
        Map<String, String> params = new HashMap<String, String>();
        initParameters(params);

        put(FIELD_TYPE, VALUE_TYPE_CAPTURE, params);
        put(FIELD_TRANS_ID, request.getTransactionId(), params);

        return doProcess(params);
    }

    private DirectPaymentResponse doDirectPayment(DirectPaymentRequest request)
            throws IOException
    {
        Map<String, String> params = new HashMap<String, String>();
        initParameters(params);

        put(FIELD_TYPE, VALUE_TYPE_AUTH_CAPTURE, params);
        put(FIELD_CUSTOMER_IP, request.getCustomerIP(), params);
        put(FIELD_CUSTOMER_ID, request.getCustomerId(), params);
        put(FIELD_EMAIL, request.getCustomerEmail (), params);
        put(FIELD_INVOICE_NUM, request.getInvoiceNumber(), params);
        put(FIELD_INVOICE_DESC, request.getInvoiceDescription(), params);
        put(FIELD_EMAIL_CUSTOMER, VALUE_TRUE, params);
        put(FIELD_AMOUNT, formatAmount(request.getAmountValue()), params);
        put(FIELD_CURRENCY_CODE, request.getAmountCurrency(), params);

        Address billing = request.getBillingAddress ();
        if (billing != null)
        {
            put(FIELD_BILL_TO_ADDRESS, billing.getStreet (), params);
            put(FIELD_BILL_TO_CITY, billing.getCity (), params);
            put(FIELD_BILL_TO_COUNTRY, billing.getCountry (), params);
            put(FIELD_BILL_TO_FIRSTNAME, billing.getFirstName (), params);
            put(FIELD_BILL_TO_LASTNAME, billing.getLastName (), params);
            put(FIELD_BILL_TO_STATE, billing.getState (), params);
            put(FIELD_BILL_TO_ZIP, billing.getZipCode (), params);
        }

        PaymentMethod method = request.getPaymentMethod ();
        initParameters (request, method, params);

        return doProcess(params);
    }

    private RefundResponse doRefund(RefundRequest request)
            throws IOException
    {
        Map<String, String> params = new HashMap<String, String>();
        initParameters(params);

        put(FIELD_TYPE, VALUE_TYPE_CREDIT, params);
        put(FIELD_TRANS_ID, request.getTransactionId(), params);
        put(FIELD_AMOUNT, formatAmount(request.getAmountValue()), params);
        put(FIELD_CURRENCY_CODE, request.getAmountCurrency(), params);

        PaymentMethod method = request.getPaymentMethod ();
        if (method instanceof CreditCard)
        {
            CreditCard cc = (CreditCard)method;
            put (FIELD_CARD_NUM, last4Digits (cc.getNumber ()), params);
        }

        return doProcess(params);
    }

    protected PaymentResponse doCancel(CancelRequest request)
            throws IOException
    {
        Map<String, String> params = new HashMap<String, String>();
        initParameters(params);

        put(FIELD_TYPE, VALUE_TYPE_VOID, params);
        put(FIELD_TRANS_ID, request.getTransactionId(), params);

        return doProcess(params);
    }




    private AIMPResponse doProcess(Map<String, String> params)
            throws IOException
    {        
        /* Create the POST method */
        HttpPost post = createPostMethod (params);

        /* Post the data */
        try
        {
            HttpClient client = HttpClientBuilder.create().build();                
            HttpResponse resp = client.execute (post);

            /* Return the response */
            String body = EntityUtils.toString (resp.getEntity ());
            return AIMPResponse.parse(body, params.get(FIELD_DELIM_CHAR));
        }
        finally
        {
            post.releaseConnection();
        }
    }

    private HttpPost createPostMethod (Map<String, String> params) throws UnsupportedEncodingException
    {
        String url = getProperty(PARAM_URL, VALUE_URL);
        HttpPost post = new HttpPost(url);
        post.addHeader ("Content-type", "text/html; charset=" + UTF8);

        /* Parameters of the request */
        List<NameValuePair> values = new ArrayList<> ();
        Iterator it = params.keySet().iterator();
        for (int i = 0; it.hasNext(); i++)
        {
            String name = it.next().toString();
            String value = (String) params.get(name);
            if (!isEmpty(value))
            {
                values.add (new BasicNameValuePair (name, value));
            }
        }
        post.setEntity (new UrlEncodedFormEntity (values, UTF8));
        return post;
    }

    /**
     * Set the required parameters
     * @param request
     * @param params
     */
    private void initParameters(Map<String, String> params)
    {
        put(FIELD_VERSION, VALUE_VERSION, params);
        put(FIELD_DELIM_DATA, VALUE_TRUE, params);
        put(FIELD_DELIM_CHAR, VALUE_DELIM_CHAR, params);
        put(FIELD_RELAY_RESPONSE, VALUE_FALSE, params);
        put(FIELD_LOGIN, getProperty(FIELD_LOGIN), params);
        put(FIELD_TRAN_KEY, getProperty(FIELD_TRAN_KEY), params);
        put(FIELD_TEST_REQUEST, getProperty(FIELD_TEST_REQUEST), params);
    }

    private void initParameters(PaymentRequest request, PaymentMethod method, Map<String, String> params)
    {
        if (method instanceof CreditCard)
        {
            CreditCard cc = (CreditCard)method;
            if (request instanceof RefundRequest)
            {
                put(FIELD_CARD_NUM, last4Digits (cc.getNumber()), params);
            }
            else
            {
                put (FIELD_METHOD, VALUE_METHOD_CC, params);
                put(FIELD_CARD_NUM, cc.getNumber(), params);
                put(FIELD_EXP_DATE, formatExpiryDate (cc), params);
                put(FIELD_CARD_CODE, cc.getVerificationCode(), params);
            }
        }
        else
        {
            throw new IllegalStateException ("Unsupported payment type: " + method);
        }
    }
    
    private void put(String name, String value, Map<String, String> params)
    {
        if (!isEmpty(value))
        {
            params.put(name, value);
        }
    }

    private String getProperty(String name)
    {
        return getProperty(name, null);
    }

    private String getProperty(String name, String defaultValue)
    {
        String value = _properties.getProperty(name);
        return isEmpty(value) ? defaultValue : value;
    }
    
    private String formatAmount (double amount)
    {
        return AMOUNT_FORMATER.format (amount);
    }

    private String formatExpiryDate (CreditCard cc)
    {
        int mm = cc.getExpiryMonth ();
        int yy = cc.getExpiryYear () % 100;
        String str = String.valueOf(mm) + String.valueOf (yy);

        return mm < 10 ? "0" + str : str;
    }

    private String last4Digits (String number)
    {
        return number.length () > 4 ? number.substring (number.length ()-4) : number;
    }
    
    private int toInt (String str, int defaultValue)
    {
        try
        {
            return Integer.parseInt (str);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }
    
    private boolean isEmpty (String str)
    {
        return str == null || str.length () == 0;
    }
}
