package core.helpers.httpHelper;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Fluent Http client util;
 *
 * examples:
 * http.get("http://domain.com").execute().toString();
 *
 * http.
 *
 *
 * Created by sserdiuk on 9/7/17.
 */

public class HttpClientRepublished {

    public static final Logger log = Logger.getLogger(HttpClientRepublished.class);

    CloseableHttpResponse response;
    HttpUriRequest request;
    String body;
    List<BasicClientCookie> cookiesList;
    URI url;

    public HttpClientRepublished() {
        cookiesList = new ArrayList<>();
    }

    /**
     *
     * @param url target uri
     * @return this;
     */
    public HttpClientRepublished get(String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        request = new HttpGet(url);
        return this;
    }

    public HttpClientRepublished get(URI url) {
        this.url = url;

        request = new HttpGet(url);
        return this;
    }

    /**
     *
     * @param url target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClientRepublished post(String url, JSONObject jsonData) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = post;
        return this;
    }

    public HttpClientRepublished post(URI url, JSONObject jsonData) {

        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = post;
        return this;
    }

    /**
     *
     * @param url target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClientRepublished put(String url, JSONObject jsonData) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpPut put = new HttpPut(url);
        try {
            put.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = put;
        return this;
    }

    /**
     *
     * @param url target uri
     * @param jsonData data json format
     * @return this;
     */
    public HttpClientRepublished put(URI url, JSONObject jsonData) {
        this.url = url;

        HttpPut put = new HttpPut(url);
        try {
            put.setEntity(new StringEntity(jsonData.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request = put;
        return this;
    }

    /**
     *
     * @param url target uri
     * @return this;
     */
    public HttpClientRepublished delete(String url) {
        try {
            this.url = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        request = new HttpDelete(url);
        return this;
    }

    /**
     *
     * @param url target uri
     * @return this;
     */
    public HttpClientRepublished delete(URI url) {
        this.url = url;
        request = new HttpDelete(url);
        return this;
    }

    /**
     *
     * @param key header key
     * @param value header value
     * @return this;
     */
    public HttpClientRepublished addHeader(String key, String value) {

        request.addHeader(key, value);
        return this;
    }

    public HttpClientRepublished addCookies(String name, String value, Date expirationDate, String domain) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(expirationDate);
        cookie.setDomain(domain);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClientRepublished addCookies(String name, String value, String domain) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClientRepublished addCookies(String name, String value, Date expirationDate) {

        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setExpiryDate(expirationDate);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    public HttpClientRepublished addCookies(String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        if (!cookiesList.contains(cookie)) {
            cookiesList.add(cookie);
        }
        return this;
    }

    /**
     * Request execution method (get/post/put/delete/).execute()
     * After executing, can use response methods.
     * @return this
     */
    public HttpClientRepublished execute() {

        log.info("[HttpClient] Executing " + request.getMethod()
                .toUpperCase() + ":" + request.getURI() + "\n");

        HttpClientBuilder builder = HttpClients.custom();

        try {
            SSLContextBuilder ctxb = SSLContextBuilder.create();
            ctxb.loadTrustMaterial(new TrustSelfSignedStrategy());
            SSLContext ctx  = ctxb.build();
            SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(ctx, new DefaultHostnameVerifier());
            builder.setSSLSocketFactory(sslf);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        if (cookiesList.size() > 0) {
            CookieStore cookieStore = new BasicCookieStore();
            cookiesList.forEach(cookieStore::addCookie);
            builder.setDefaultCookieStore(cookieStore);
        }

        try {
            CloseableHttpClient client = builder.build();
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * if is able to convert response to JSONObject, it will return JSONObject
     * @return JSONObject|null
     */
    public JSONObject toJSON() {

        try {
            return new JSONObject(body);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     *
     * @return response as string
     */
    public String toString() {

        return body;
    }

    /**
     *
     * @return response code status
     */
    public int responseStatus() {

        return response.getStatusLine()
                .getStatusCode();
    }

    /**
     *
     * @return response headers[]
     */
    public Header[] responseHeaders() {

        return response.getAllHeaders();
    }

    public CloseableHttpResponse getResponse() {

        return response;
    }

}


