package core.helpers.httpHelper;

import core.configs.ConfigLoader;
import core.random.RandomUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Coverage class for HttpClient-apache-http-client
 * Created by serdyuk on 9/18/16.
 */
public class BadooHelper {


    HttpClient http     = new HttpClient();
    ConfigLoader config = new ConfigLoader();
    RandomUtils rand    = new RandomUtils();
//    DatabaseUtils db    = new DatabaseUtils();
    HashMap<String, String> outputData = new HashMap<>();
    JSONObject json;

    protected String siteLink, email, password, country, gender, location, userId, autologinLink, trafficSource, ip_address;

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
     * @return this
     * @throws SQLException
     */
    public BadooHelper setData(HashMap<String, String> data) throws SQLException, IOException {

        email = data.get("email");
        siteLink = data.get("siteLink");
        password = data.get("password");
        gender = data.get("gender");
        location = data.get("location");
        userId = data.get("userId");
        autologinLink = data.get("autologin");
        trafficSource = data.get("trafficSource");

//        if (location != null) {
//            db.connect();
//            Object[][] queryResult = db.query("select ip, city from locations where location='" + location + "'");
//            ip_address = (String) queryResult[0][0];
//            country = (String) queryResult[0][1];
//            db.disconnect();
//        } else {
//            throw new NullPointerException("setData[location] is required. Example : YorHasMap.put('location', 'USA')");
//        }

        if(siteLink != null){
            String[] httpParse = siteLink.split("://");
            if (httpParse.length < 2) {
                siteLink = "https://" + siteLink;
            }

            String[] wwwParse = siteLink.split("www");
            if (wwwParse.length < 2) {
                httpParse = siteLink.split("://");
                siteLink = "https://www." + httpParse[1];
            }
        }
        return this;
    }

    /**
     * Method for get\post user registration on dating sites;
     * Require :   siteLink, email, gender, password, location
     * @param mobSiteRegistration boolean true - if need mobile registration;
     * @return this;
     * @throws IOException
     */
    public BadooHelper datingRegistration(boolean mobSiteRegistration) {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Dating user registered with email: "+email);

        return this;
    }
}
