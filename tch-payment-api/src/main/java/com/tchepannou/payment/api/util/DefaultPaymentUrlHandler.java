/*
 * Copyright 2013 Herve Tchepannou Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tchepannou.payment.api.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 * @author herve
 */
public class DefaultPaymentUrlHandler
    implements PaymentUrlHandler
{
    //-- Static Attributes
    public static final String UTF_8 = "utf-8";
    
    
    //-- PaymentUrlHandler overrides
    public String post (URL url, Map<String, String> params)
        throws IOException
    {
        StringBuilder sb = new StringBuilder ();
        for (String name : params.keySet ())
        {
            String value = params.get (name);
            if (value != null)
            {
                value = URLEncoder.encode (value, UTF_8);
                
                if (sb.length () > 0)
                {
                    sb.append('&');
                }
                sb.append(name).append('=').append (value);
            }
        }
        
        return post (url, sb.toString ());
    }
    
    
    //-- Private
    protected String post (URL url, String params)
        throws IOException
    {
        HttpURLConnection cnn = null;
        try
        {
            // Create cnn
            cnn = ( HttpURLConnection ) url.openConnection ();
            cnn.setRequestMethod ("POST");
            cnn.setRequestProperty ("Content-Type", "application/x-www-form-urlencoded");
            cnn.setConnectTimeout(60000);
            cnn.setReadTimeout(60000);            
            cnn.setUseCaches (false);
            cnn.setDoInput (true);
            cnn.setDoOutput (true);

            // Send request
            DataOutputStream wr = new DataOutputStream (cnn.getOutputStream ());
            wr.writeBytes (params);
            wr.flush ();
            wr.close ();

            // Get Response
            InputStream is = cnn.getInputStream ();
            BufferedReader rd = new BufferedReader (new InputStreamReader (is));
            try
            {
                String line;
                StringBuffer response = new StringBuffer ();
                while ( (line = rd.readLine ()) != null )
                {
                    if (response.length () > 0)
                    {
                        response.append("\n");
                    }
                    response.append (line);
                }
                return response.toString ();
            }
            finally
            {
                rd.close ();
            }
        }
        finally
        {

            if ( cnn != null )
            {
                cnn.disconnect ();
            }
        }
    }
    
}
