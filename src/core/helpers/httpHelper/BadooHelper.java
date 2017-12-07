package core.helpers.httpHelper;

import core.configs.ConfigLoader;
import core.constans.LocationData;
import core.constans.Sites;
import core.random.RandomUtils;
import exceptions.BadooException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Coverage class for HttpClient-apache-http-client
 * Created by serdyuk on 9/18/16.
 */
public class BadooHelper {

    HttpClient http = new HttpClient();
    RandomUtils rand = new RandomUtils();
    HashMap<String, String> outputData = new HashMap<>();
    JSONObject json;
    String content;

    protected String siteLink, email, password, country, gender, location, userId, autologinLink, trafficSource, ip_address;

    protected String adminDomain;
    protected String adminPassword;

    public static boolean isRel = false;

    /**
     * @param data HashMap with user Data :
     *             Email            (For registration, find in admin panel)
     *             siteLink         (For registration)
     *             password         (For registration)
     *             gender           (For registration)
     *             location         (Primary, require =true)
     *             userId           (Find by userId)
     *             autologin        (Paid actions)
     *             trafficSource    (For Registration)
     *             adminDomain      (Admin domain link)
     *             isRel            (set environment links to rel)
     * @return this
     */
    public BadooHelper setData(HashMap<String, String> data) {

        email = data.get("email");
        siteLink = data.get("siteLink");
        password = data.get("password");
        gender = data.get("gender");
        location = data.get("location");
        userId = data.get("userId");
        autologinLink = data.get("autologin");
        trafficSource = data.get("trafficSource");

        if (location == null) {
            throw new NullPointerException("setData[location] is required. Example : YorHashMap.put('location', 'USA')");

        } else {
            country = LocationData.valueOf(location)
                    .getName();
            ip_address = LocationData.valueOf(location)
                    .getIp();

            if (country == null || ip_address == null) {
                throw new NullPointerException("unable to find location " + location + " in LocationDataClass");
            }
        }

        if (siteLink == null) {
            throw new NullPointerException("siteData[siteLink] is required. Example : YouHashMap.put('siteLink'), http[s]://[www].[site].[com]");
        } else {
            if (siteLink.contains("rel")) {
                isRel = true;
                adminDomain = ConfigLoader.STREAM_TEST_PLATFORM_ADMIN_DOMAIN;
                adminPassword = ConfigLoader.STREAM_TEST_PLATFORM_PASSWORD;
            } else {
                adminDomain = ConfigLoader.STREAM_LIVE_ADMIN_DOMAIN;
                adminPassword = ConfigLoader.STREAM_LIVE_ADMIN_PASSWORD;
            }
        }
        return this;
    }

    /**
     * Method for get\post user registration on dating sites;
     * Require :   siteLink, email, gender, password, location
     *
     * @param mobSiteRegistration boolean true - if need mobile registration;
     * @return this;
     */
    public BadooHelper datingRegistration(boolean mobSiteRegistration) {
        if (!siteLink.contains("://")) {
            if (isRel) {
                siteLink += "http://" + siteLink;
            } else {
                siteLink = "https://" + siteLink;
            }
            System.out.println(siteLink);
        }

        if (!siteLink.contains("www")) {
            String[] parseSite = siteLink.split("://");
            siteLink = parseSite[0] + "://www." + parseSite[1];
        }

        http.mobileUserAgent = mobSiteRegistration;

        if (mobSiteRegistration) {
            String[] parseSite = siteLink.split("www");
            this.siteLink = parseSite[0] + "m" + parseSite[1];
        }

        List<NameValuePair> regForm = new ArrayList<>();

        regForm.add(new BasicNameValuePair("UserForm[day]", Integer.toString(rand.randomNumber(1, 29))));
        regForm.add(new BasicNameValuePair("UserForm[email]", email));
        regForm.add(new BasicNameValuePair("UserForm[gender]", gender));
        regForm.add(new BasicNameValuePair("UserForm[location]", country));
        regForm.add(new BasicNameValuePair("UserForm[month]", Integer.toString(rand.randomNumber(1, 12))));
        regForm.add(new BasicNameValuePair("UserForm[year]", Integer.toString(rand.randomNumber(1980, 1991))));
        regForm.add(new BasicNameValuePair("UserForm[transferId]", "59f3a50635d243058cdb19318be3af81"));
        regForm.add(new BasicNameValuePair("UserForm[password]", password));
        regForm.add(new BasicNameValuePair("UserForm[lid]", "447ae708a0a911e292b2d4bed9a94a8f"));
        regForm.add(new BasicNameValuePair("UserForm[login]", rand.randomScreenName()));

        http.get(siteLink + "/admin2/");
        http.setCookie(siteLink, "ip_address", ip_address);
        try {
            http.execute();
            http.get(siteLink + trafficSource);
            http.post(siteLink + "/user/register", regForm);
            http.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Dating user registered with email: "+email);
        http.execute();
        http.get(siteLink + trafficSource);
        http.post(siteLink + "/user/register", regForm);
        http.execute();

        System.out.println("Dating user registered with email: " + email);

        return this;
    }


    /**
     * Method for get\post user registration on dating sites;
     * Require :   siteLink, email, password, location
     *
     * @param mobSiteRegistration boolean true - if need mobile registration;
     * @return this;
     */
    public BadooHelper camsRegistration(boolean mobSiteRegistration) {
        http.mobileUserAgent = mobSiteRegistration;
        http.get(siteLink + trafficSource).execute();
        if (isRel) {
            siteLink = Sites.HTTPS_STREAM_PLATFORM_TEST_WEB;
        }

        if (!siteLink.contains("www")) {
            String[] parseSite = siteLink.split("cams");
            siteLink = "https://www.cams" + parseSite[1];
        }

        if (mobSiteRegistration) {
            String[] parseSite = siteLink.split("cams");
            siteLink = "https://m.cams" + parseSite[1];
        }

        if (siteLink.contains("hwtool") && siteLink.contains("https")) {
            String[] parseSite = siteLink.split("https");
            siteLink = "http" + parseSite[1];
        }


        List<NameValuePair> regForm = new ArrayList<>();

        regForm.add(new BasicNameValuePair("webCamSite\\models\\UserFormcams[email]", email));
        regForm.add(new BasicNameValuePair("webCamSite\\models\\UserFormcams[password]", password));
        regForm.add(new BasicNameValuePair("webCamSite\\models\\UserFormcams[first_name]", rand.randomScreenName()));
        regForm.add(new BasicNameValuePair("webCamSite\\models\\UserFormcams[isAdult]", "1"));

        http.get(siteLink + trafficSource);
//        setSplitLocation();
        http.setCookie(siteLink, "ip_address", ip_address);
        http.execute();
        //.setHeader(HttpHeaders.REFERER, siteLink)
        http.post(siteLink + "/user/register", regForm);
        http.execute();
        System.out.println("Cams user registered with email: " + email);

        return this;
    }

    /**
     * Method for find user in dating admin panel
     * Required : email
     *
     * @return this
     */
    public BadooHelper findDatingUser() {
        String authorization = ConfigLoader.IPA_LOGIN + ":" + ConfigLoader.IPA_PASSWORD;
        byte[] base64 = authorization.getBytes();
        authorization = Base64.getEncoder()
                .encodeToString(base64);

        http.get(ConfigLoader.BADOO_LIVE_ADMIN_DOMAIN);
        http.setHeader("Authorization", "Basic " + authorization);
        http.execute();
        try {
            http.get(ConfigLoader.BADOO_LIVE_ADMIN_DOMAIN + "/user/find/?FindUserForm[user]=" + URLEncoder.encode(email, "UTF-8") + "&json=true");
            http.execute();
            json = new JSONObject(http.getContent()).getJSONObject(email);
            json = json.getJSONObject(json.keys()
                    .next());
            http.get(ConfigLoader.BADOO_LIVE_ADMIN_DOMAIN + "/user/markTester?userId=" + json.getString("userId") + "&tester=1");
            http.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            //обработчик 500й в админке
            for (Map.Entry<Integer, String> resp : http.getStatusCode().entrySet()) {
                if (!resp.getKey().equals(200)) {
                    try {
                        throw new BadooException("Phoenix admin Panel in not available. HTTP Response: " + resp.getKey() + " " + resp.getValue());
                    } catch (BadooException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            //обработчик неверных данных авторизации в админке
            if (http.getContent().contains("Sign in")) {
                try {
                    throw new BadooException("Can't access to admin panel! Login: " + ConfigLoader.STREAM_LIVE_ADMIN_DOMAIN +
                            " Password: " + adminPassword + " is incorrect!");
                } catch (BadooException e1) {
                    e1.printStackTrace();
                }
            }
            throw new JSONException(e);

        }
        return this;

    }

    /**
     * Required : email
     *
     * @return this
     */
    public BadooHelper findCamsUser() {

        loginAdminPanel();

        try {
            http.get(adminDomain + "/user/find/?FindUserForm[user]=" + URLEncoder.encode(email, "UTF-8") + "&json=true")
                    .execute();
            json = new JSONObject(http.getContent()).getJSONObject(email);
            json = json.getJSONObject(json.keys()
                    .next());
            http.get(adminDomain + "/user/markTester?userId=" + json.getString("userId") + "&tester=1");
            http.execute();
            //проверка, попал ли юзер в сплит
//            if (!System.getProperty("camssplit").equals("undefined")){
//                for (camsSplits camsSplit: camsSplits.values()){
//                    if (camsSplit.getSplitName().toLowerCase().equals(System.getProperty("camssplit").toLowerCase())){
//                        try {
//                            outputData.put("split", camsSplit.getSplitId());
//                            outputData.put("splitgroup",
//                                    String.valueOf(UserSplit.getSplitGroup(camsSplit.getGroupsCount(), json.getString("userId"))));
//                        } catch (NoSuchAlgorithmException e) {
//                            e.printStackTrace();
//                        }
//                        //зафорсить сплит из camsSplits
//                        if (System.getProperty("forcesplit").equals("true")) {
//                            forceSplit(camsSplit);
//                        }
//                    }
//                }
//            }
            // а это костыль для того, чтобы тестовый юзер мог видеть тестовые модели в серче
            if (System.getProperty("showtestmodels").equals("true")) {
                showTestModels();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            for (Map.Entry<Integer, String> resp : http.getStatusCode().entrySet()) {
                if (!resp.getKey().equals(200)) {
                    try {
                        throw new BadooException("Phoenix admin Panel in not available. HTTP Response: " + resp.getKey() + " " + resp.getValue());
                    } catch (BadooException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            if (http.getContent().contains("Sign in")) {
                try {
                    throw new BadooException("Can't access to admin panel! Login: " + ConfigLoader.STREAM_LIVE_ADMIN_DOMAIN +
                            " Password: " + adminPassword + " is incorrect!");
                } catch (BadooException e1) {
                    e1.printStackTrace();
                }
            }
            throw new JSONException(e);

        }
        return this;
    }

    /**
     * Required UserId
     *
     * @return this
     */
//    public BadooHelper findCamsUserById() {
//
//        loginAdminPanel();
//        http.get(adminDomain + "/user/find/?FindUserForm[user]=" + userId + "&json=true");
//        http.execute();
//        content = http.getContent();
//        try {
//            json = new JSONObject(http.getContent()).getJSONArray(userId)
//                    .getJSONObject(0);
//            http.get(adminDomain + "/user/markTester?userId=" + json.getString("userId") + "&tester=1");
//            http.execute();
//            //проверка, попал ли юзер в сплит
////            if (!System.getProperty("camssplit").equals("undefined")){
////                for (camsSplits camsSplit: camsSplits.values()){
////                    if (camsSplit.getSplitName().toLowerCase().equals(System.getProperty("camssplit").toLowerCase())){
////                        try {
////                            outputData.put("split", camsSplit.getSplitId());
////                            outputData.put("splitgroup",
////                                    String.valueOf(UserSplit.getSplitGroup(camsSplit.getGroupsCount(), json.getString("userId"))));
////                        } catch (NoSuchAlgorithmException e) {
////                            e.printStackTrace();
////                        }
////                        //зафорсить сплит из camsSplits
////                        if (System.getProperty("forcesplit").equals("true")) {
////                            forceSplit(camsSplit);
////                        }
////                    }
////                }
////            }
//            // а это костыль для того, чтобы тестовый юзер мог видеть тестовые модели в серче
//            if (System.getProperty("showtestmodels").equals("true")) {
//                showTestModels();
//            }
//        } catch (JSONException e) {
//            for (Map.Entry<Integer,String> resp: http.getStatusCode().entrySet()){
//                if (!resp.getKey().equals(200)){
//                    throw new BadooException("Phoenix admin Panel in not available. HTTP Response: " + resp.getKey() + " " +  resp.getValue());
//                }
//            }
//            if (http.getContent().contains("Sign in")){
//                throw new BadooException("Can't access to admin panel! Login: " + ConfigLoader.CAMS_LIVE_ADMIN_LOGIN +
//                        " Password: " + adminPassword + " is incorrect!");
//            }
//            throw new JSONException(e);
//
//        }
//        return this;
//    }

    /**
     * final method , for grab data from previous actions - registration, find user, etc.
     *
     * @return HashMap userData
     */
    public HashMap<String, String> getData() {


        outputData.put("email", json.getString("email"));
        outputData.put("login", json.getString("login"));
        outputData.put("gender", json.getString("gender"));
        outputData.put("location", json.getString("country"));
        outputData.put("trafficSource", json.getString("trafficSource"));
        outputData.put("siteName", json.getString("siteName")
                .toLowerCase());
        outputData.put("siteUrl", json.getString("siteUrl"));
        outputData.put("registrationTime", json.getString("registered_time"));
        outputData.put("platform", json.getString("registrationPlatform"));
        outputData.put("userId", json.getString("userId"));
        outputData.put("autologinKey", json.getString("autologinKey"));
        outputData.put("autologinLink", siteLink + "/site/autologin/key/" + json.getString("autologinKey"));

        return outputData;
    }

    private void showTestModels() {
        HashMap<String, Object> form = new HashMap<>();
        form.put("YII_CSRF_TOKEN", "");
        form.put("admin\\models\\webCam\\ShowTestModelForm[userId]", json.getString("userId"));
        form.put("admin\\models\\webCam\\ShowTestModelForm[showTestModel]", "1");
        http.get(adminDomain + "/admin/webCam/userShowTestModel");
        http.post(adminDomain + "/admin/webCam/userShowTestModel", form);
        http.execute();
        System.out.println("User " + json.getString("userId") + " is able to see test models in search.");
    }

//    private void forceSplit(camsSplits camsSplit) {
//        HashMap<String, Object> form = new HashMap<>();
//        form.put("YII_CSRF_TOKEN", "");
//        form.put("admin\\models\\splitSystem\\ForceSplitForm[userId]", json.getString("userId"));
//        form.put("admin\\models\\splitSystem\\ForceSplitForm[splitName]", camsSplit.getSplitId());
//        form.put("admin\\models\\splitSystem\\ForceSplitForm[group]", System.getProperty("splitgroup"));
//        http.get(adminDomain + "/admin/splitSystem/forceSplit");
//        http.post(adminDomain + "/admin/splitSystem/forceSplit", form);
//        http.execute();
//        outputData.put("split", camsSplit.getSplitId());
//        outputData.put("splitgroup", System.getProperty("splitgroup"));
//
//    }

    private void loginAdminPanel() {
        HashMap<String, Object> form = new HashMap<>();
        form.put("YII_CSRF_TOKEN", "");
        form.put("AdminLoginForm[login]", ConfigLoader.STREAM_LIVE_ADMIN_DOMAIN);
        form.put("AdminLoginForm[password]", adminPassword);

        http.get(adminDomain + "/admin/base/login");
        http.post(adminDomain + "/admin/base/login", form);
        http.execute();
    }

//    private void setSplitLocation(){
//        if (!System.getProperty("camssplit").equals("undefined")){
//            for (camsSplits split: camsSplits.values()){
//                if (System.getProperty("camssplit").toLowerCase().equals(split.getSplitName())){
//                    this.country = split.getSplitLocation().toUpperCase();
//                    for (LocationCountriesData location: LocationCountriesData.values()){
//                        if (this.country.equals(location.getCountry())){
//                            this.ip_address = location.getIp();
//                        }
//                    }
//                }
//            }
//        }
//    }

    public String getContent() {
        return content;
    }

    public JSONObject getUserJson() {
        return json;
    }

    public BuyMembership buyMembership() {
        return new BuyMembership(this);
    }

    public BuyFeatures buyFeatures() {
        return new BuyFeatures(this);
    }

    public BuyCamCredits buyLiveCamCredits() {
        return new BuyCamCredits(this);
    }
}
