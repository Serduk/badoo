package core.helpers.httpHelper;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base Http Apache Client wrapper-class
 * Created by serdyuk on 9/18/16.
 */

public class HttpClient {

    protected HttpPost postRequest;
    protected HttpGet getRequest;
    protected CookieStore cookieStore;

    protected CloseableHttpClient client;
    protected CloseableHttpResponse response;
    protected HttpEntity entity;

    protected String content;
    public boolean mobileUserAgent = false;

    public HttpClient() {
        cookieStore = new BasicCookieStore();
    }

    /**
     * @param url String, where need to send GET
     * @return HttpClient
     */
    public core.helpers.httpHelper.HttpClient get(String url) {

        getRequest = new HttpGet(url);
        return this;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    /**
     * @param url  String, where need to send POST
     * @param post Hasmap of Post data OR List<NameValuePair>
     * @return HttpClient
     * @throws UnsupportedEncodingException
     */
    public core.helpers.httpHelper.HttpClient post(String url, HashMap<String, String> post) throws UnsupportedEncodingException {

        List<NameValuePair> form = new ArrayList<>();
        for (Map.Entry<String, String> str : post.entrySet()) {
            form.add(new BasicNameValuePair(str.getKey(), str.getValue()));
        }
        postRequest = new HttpPost(url);
        postRequest.setEntity(new UrlEncodedFormEntity(form));

        return this;
    }

    /**
     * @param url  String, where need to send POST
     * @param post List of Post data OR List<NameValuePair>
     * @return HttpClient
     * @throws UnsupportedEncodingException
     */
    public core.helpers.httpHelper.HttpClient post(String url, List<NameValuePair> post) throws UnsupportedEncodingException {
        postRequest = new HttpPost(url);
        postRequest.setEntity(new UrlEncodedFormEntity(post));

        return this;
    }


    /**
     * @param key   name of header
     * @param value value of header
     * @return HttpClient
     */
    public core.helpers.httpHelper.HttpClient setHeader(String key, String value) {
        if (getRequest != null) {
            getRequest.addHeader(key, value);
        }
        if (postRequest != null) {
            postRequest.addHeader(key, value);
        }

        return this;
    }

    /**
     * @param url   String, where need to set cookie
     * @param key   name of cookie
     * @param value value of cookie
     * @return HttpClient
     */
    public core.helpers.httpHelper.HttpClient setCookie(String url, String key, String value) {
        BasicClientCookie cookie = new BasicClientCookie(key, value);
        cookieStore.addCookie(cookie);
        String domain = url.split("//")[1];
        cookie.setDomain(domain);
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        return this;
    }

    /**
     * This method is need to execute query;
     *
     * @return HttpClient
     * @throws IOException
     */
    public core.helpers.httpHelper.HttpClient execute() throws IOException {
        HttpClientContext context = HttpClientContext.create();
        try {
            String mobSite = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.3";
            if (mobileUserAgent) {
                client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultCookieStore(cookieStore).setUserAgent(mobSite).build();
            } else {
                client = HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultCookieStore(cookieStore).build();
            }
            if (getRequest != null) {
                if (mobileUserAgent) {
                    getRequest.setHeader(HttpHeaders.USER_AGENT, mobSite);
                }
                response = client.execute(getRequest);
            }
            if (postRequest != null) {
                if (mobileUserAgent) {
                    postRequest.setHeader(HttpHeaders.USER_AGENT, mobSite);
                }
                response = client.execute(postRequest);
            }
            if (getRequest != null) {
                HttpHost target = context.getTargetHost();
                List<URI> redirectLocations = context.getRedirectLocations();
                URI location = URIUtils.resolve(getRequest.getURI(), target, redirectLocations);
                System.out.println(location.toASCIIString());
            }
            if (postRequest != null) {
                System.out.println(getCurrentUrl());
            }
            entity = response.getEntity();
            content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            postRequest = null;
            return this;

        } catch (Exception e) {
            throw new IOException(e + "\n failed to execute");
        }
    }

    /**
     * @return current cookies
     */
    public List<Cookie> getCookies() {
        return cookieStore.getCookies();
    }

    public String getCookiesString() {
        List<Cookie> cookies = cookieStore.getCookies();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            sb.append(cookies.get(i));
            if (i != cookies.size() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public JSONObject getJSON() {
        return new JSONObject(content);
    }

    /**
     * @return Current Headers
     * @throws IOException
     */
    public Header[] getHeaders() throws IOException {
        if (getRequest != null) {
            return getRequest.getAllHeaders();
        }
        if (postRequest != null) {
            return postRequest.getAllHeaders();
        } else {
            throw new IOException("no headers");
        }
    }

    /**
     * @return return current URL
     * @throws IOException
     */
    public String getCurrentUrl() throws IOException {
        if (getRequest != null) {
            return getRequest.getURI().toString();
        }
        if (postRequest != null) {
            return postRequest.getURI().toString();
        } else {
            throw new IOException("no url");
        }
    }


    public core.helpers.httpHelper.HttpClient setXHRHeader() {

        this.setHeader(com.google.common.net.HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        return this;
    }

    /**
     * @return current executed content
     */
    public String getContent() {
        return this.content;
    }


}
