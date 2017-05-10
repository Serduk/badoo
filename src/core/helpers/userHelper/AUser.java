package core.helpers.userHelper;


import core.configs.ConfigLoader;
import core.helpers.httpHelper.BadooHelper;
import core.random.RandomUtils;

import java.util.HashMap;

/**
 * Abstract class for Users helper class
 * Created by Serdyuk on 13.06.2017.
 */
public class AUser {

    public final String site;
    public final String location;
    public final String trafficSource;
    public final String email;
    public final String password;
    public final String gender;
    public final String platform;

    public String userId;
    public String autologin;
    public String autologinKey;
    public String regPlatform;
    public String regLocation;
    public String screenName;
    public String ip;

    public HashMap<String, String> inputData;
    public HashMap<String, String> outputData;


    RandomUtils random;
    BadooHelper helper;


    public AUser(String site, String location, String source, String platform) {

        random = new RandomUtils();
        helper = new BadooHelper();

        this.site = site;
        this.location = location;
        this.trafficSource = source;
        this.platform = platform;
        this.email = random.randomEmail(ConfigLoader.TEST_EMAIL_FOR_REG_ON_SITES);
        this.password = ConfigLoader.TEST_PASSWORD_FOR_REG_ON_SITES;
        this.gender = ConfigLoader.TEST_GENDER_FOR_REG_ON_SITES;

        inputData = new HashMap<>();
        inputData.put("email", email);
        inputData.put("siteLink", site);
        inputData.put("location", location);
        inputData.put("trafficSource", source);
        inputData.put("password", password);
        inputData.put("gender", gender);


    }

}
