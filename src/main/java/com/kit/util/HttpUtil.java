package com.kit.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLContext;

/**
 */
public class HttpUtil {

    public static HttpClient getClient() {
        try {
            SSLContext context = new SSLContextBuilder()
                    .loadTrustMaterial(null, (arg0, arg1) -> true)
                    .build();

            return HttpClients.custom()
                    .setHostnameVerifier(new AllowAllHostnameVerifier())
                    .setSslcontext(context)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return HttpClients.createDefault();
        }
    }

}
