package core.helpers.httpHelper;

import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Base Http Apache Client wrapper-class
 * Created by serdyuk on 9/18/16.
 */

public class HttpClient {
    protected HttpPost postRequest;
    protected HttpGet getRequest;
    protected CookieStore cookieStore;
    private Header[] requestHeaders;

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
    public HttpClient get(String url) {

        getRequest = new HttpGet(url);
        return this;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    /**
     * @param url  String, where need to send POST
     * @param post HashMap of Post data OR List<NameValuePair>
     * @return HttpClient
     */
    public HttpClient post(String url, HashMap<String, Object> post) {

        List<NameValuePair> form = post.entrySet()
                .stream()
                .map(str -> new BasicNameValuePair(str.getKey(), str.getValue()
                        .toString()))
                .collect(Collectors.toList());
        postRequest = new HttpPost(url);
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(form));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * @param url  String, where need to send POST
     * @param post List of Post data OR List<NameValuePair>
     * @return HttpClient
     */
    public HttpClient post(String url, List<NameValuePair> post) {
        postRequest = new HttpPost(url);
        try {
            postRequest.setEntity(new UrlEncodedFormEntity(post));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }


    public HttpClient post(String url, JSONObject json) {
        postRequest = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(json.toString());
            postRequest.setEntity(entity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpClient post(String url, String stringData) {
        postRequest = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(stringData);
            postRequest.setEntity(entity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * @param key   name of header
     * @param value value of header
     * @return HttpClient
     */
    public HttpClient setHeader(String key, String value) {
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
    public HttpClient setCookie(String url, String key, String value) {
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
     */
    public HttpClient execute() {
        HttpClientContext context = HttpClientContext.create();
        try {


            String mobSite = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.3";
            if (mobileUserAgent) {
                client = HttpClients.custom()
                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .setDefaultCookieStore(cookieStore)
                        .setUserAgent(mobSite)
                        .build();
            } else {
                client = HttpClients.custom()
                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .setDefaultCookieStore(cookieStore)
                        .build();
            }
            if (getRequest != null) {
                if (mobileUserAgent) {
                    getRequest.setHeader(HttpHeaders.USER_AGENT, mobSite);
                }
                requestHeaders = getRequest.getAllHeaders();
                response = client.execute(getRequest);
            }
            if (postRequest != null) {
                if (mobileUserAgent) {
                    postRequest.setHeader(HttpHeaders.USER_AGENT, mobSite);
                }
                requestHeaders = postRequest.getAllHeaders();
                response = client.execute(postRequest);
            }
            if (postRequest != null) {
                HttpHost target = context.getTargetHost();
                List<URI> redirectLocations = context.getRedirectLocations();
                URI location = URIUtils.resolve(postRequest.getURI(), target, redirectLocations);
                String locationUrl = location.toASCIIString();
                String restriction = restrictionStringFound(locationUrl);
                if (restriction != null && !Objects.equals(restriction, "")) {
                    System.out.println(locationUrl.replace(restriction, "****"));
                } else {
                    System.out.println(locationUrl);
                }
                entity = response.getEntity();
                content = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                postRequest = null;
                return this;

            }
            if (getRequest != null) {
                HttpHost target = context.getTargetHost();
                List<URI> redirectLocations = context.getRedirectLocations();
                URI location = URIUtils.resolve(getRequest.getURI(), target, redirectLocations);
                String locationUrl = location.toASCIIString();
                String restriction = restrictionStringFound(locationUrl);
                if (restriction != null && !Objects.equals(restriction, "")) {
                    System.out.println(locationUrl.replace(restriction, "****"));
                } else {
                    System.out.println(locationUrl);
                }

                entity = response.getEntity();
                content = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                postRequest = null;
                return this;
            }
        } catch (javax.net.ssl.SSLPeerUnverifiedException e) {
            //           костыль для просроченного сертификата, реализован только на метод Get, нужен для теста CheckPagesHTTPResponse
            SSLConnectionSocketFactory sslsf;
            try {
                sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(),
                        NoopHostnameVerifier.INSTANCE);
                Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                        .register("http", new PlainConnectionSocketFactory())
                        .register("https", sslsf)
                        .build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
                cm.setMaxTotal(100);
                SSLContextBuilder sslcontext = new SSLContextBuilder();
                sslcontext.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLSocketFactory(sslsf)
                        .setConnectionManager(cm)
                        .build();
                response = httpClient.execute(getRequest);
                return this;
            } catch (NoSuchAlgorithmException | IOException | KeyStoreException e1) {
                e1.printStackTrace();
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return this;

    }

    private String restrictionStringFound(String content) {

        String[] restrictions = new String[]{
//                ConfigLoader.API_USER_TOKEN,
//                ConfigLoader.PROVIDER_TOKEN,
//                ConfigLoader.STREAMER_PROVIDER_TOKEN,
//                ConfigLoader.STREAMER_API_USER_TOKEN
        };

        for (String restriction : restrictions) {
            if (content.contains(restriction)) {
                return restriction;
            }
        }
        return null;
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
     */
    public Header[] getResponseHeaders() {

        return response.getAllHeaders();

    }

    public Header[] getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * @return return current URL
     */
    public String getCurrentUrl() {
        if (getRequest != null) {
            return getRequest.getURI()
                    .toString();
        }
        if (postRequest != null) {
            return postRequest.getURI()
                    .toString();
        }
        return null;
    }

    public HttpClient setXHRHeader() {

        this.setHeader(com.google.common.net.HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        return this;
    }

    /**
     * @return current executed content
     */
    public String getContent() {
        return this.content;
    }


    public HashMap<Integer, String> getStatusCode() {
        HashMap<Integer, String> statusCode = new HashMap<>();
        statusCode.put(response.getStatusLine()
                .getStatusCode(), response.getStatusLine()
                .getReasonPhrase());
        return statusCode;

    }

    public String getCookieValueByName(String name) {

        List<Cookie> cookies = getCookies();
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
