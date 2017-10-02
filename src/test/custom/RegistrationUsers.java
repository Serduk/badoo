package test.custom;

/**
 * Created by serdyuk on 5/10/17.
 */

import core.configs.ConfigLoader;
import core.csvUtils.WorkWithCSV;
import core.helpers.httpHelper.BadooHelper;
import core.random.RandomUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * * This class get diff params from csv file and create users with this params.
 * You can Create (least now: Free, Paid (only MSH), Feature (MSH + Full Feature), credits (MSH + FULL + Credits)
 * Then User will be saved in csv file
 * You need for your csv file params next column:
 * 1. siteName: ex     badoo.com
 * 2. location: ex     USA
 * 3. TrafficSource: ex        /?utm_source=int
 * 4. gender: ex       male | female
 * 5. testEmail: ex        blabla@gmail.com
 * 6. userType: ex     credits | paid | free | features
 * <p>
 * badoo.com	USA	/?utm_source=int	male	blabla@gmail.com	credits	mob
 * <p>
 * Created by sergey on 5/23/16.
 */

public class RegistrationUsers {

    private RandomUtils random;
    private WorkWithCSV csv;
    private String csvFile;

    private HashMap<String, String> csvDataSaver = new HashMap<>();

    @BeforeClass
    public void init() throws IOException {
        random = new RandomUtils();

        csv = new WorkWithCSV();
        csvFile = csv.createCsv("userRegOutput" + random.getDateAndTime());
        csvDataSaver.put("timeRegistration", "TimeRegistration");
        csvDataSaver.put("site", "Site");
        csvDataSaver.put("locationReg", "Location");
        csvDataSaver.put("gender", "Gender");
        csvDataSaver.put("userStatus", "userType");
        csvDataSaver.put("platform", "Platform");
        csvDataSaver.put("trafficSource", "Traffic TrafficSource");
        csvDataSaver.put("email", "Email");
        csvDataSaver.put("autologinDating", "Autologin Dating");
        csvDataSaver.put("userId", "Dating User ID");
        csvDataSaver.put("camsAutologin", "CamsAutologin");
        csvDataSaver.put("camsID", "Cams User ID");

        csv.writeToCsv(csvFile, csvDataSaver);
    }


    @DataProvider(name = "dp", parallel = true)
    public Iterator<Object[]> getData() throws IOException {
        WorkWithCSV regUsers = new WorkWithCSV();
        return regUsers.csvGenerateUsers(ConfigLoader.REPORTS_DIR + "/" + "userReg.csv");
    }

    @Test(dataProvider = "dp")
    public void SaveUsers(String site,
                          String location,
                          String trafficSource,
                          String gender,
                          String email,
                          String status,
                          String platform) throws IOException, SQLException {
        String generateEmail = random.randomEmail(email);
        String camsDomain = "https://cams";

        System.out.println("site is " + site);
        System.out.println("location is " + location);
        System.out.println("TrafficSource is " + trafficSource);
        System.out.println("gender is " + gender);
        System.out.println("email is " + email);
        System.out.println("status is " + status);
        System.out.println("platform is " + platform);


        HashMap<String, String> userOutputData;


        csvDataSaver.put("timeRegistration", random.getDateAndTime());
        csvDataSaver.put("site", site);
        csvDataSaver.put("locationReg", location);
        csvDataSaver.put("gender", gender);
        csvDataSaver.put("userStatus", status);
        csvDataSaver.put("platform", platform);
        csvDataSaver.put("trafficSource", trafficSource);
        csvDataSaver.put("email", generateEmail);


        /*
         * Get data for registration users
         * */
        BadooHelper helper = new BadooHelper();

        HashMap<String, String> userInputData = new HashMap<>();

        System.out.println(" Start @ " + random.getDateAndTime() + " User : " + generateEmail);

        userInputData.put("email", generateEmail);
        userInputData.put("siteLink", site);
        userInputData.put("password", ConfigLoader.TEST_PASSWORD_FOR_REG_ON_SITES);
        userInputData.put("gender", gender);
        userInputData.put("location", location);
        userInputData.put("trafficSource", trafficSource);


        boolean mobOrWeb;
//        boolean maleFemale;

        if (platform.toLowerCase().contains("m") || platform.toLowerCase().equals("mob")) {
            System.out.println("Mob user");
            mobOrWeb = true;
        } else if (platform.toLowerCase().equals("web") || platform.toLowerCase().contains("w")) {
            System.out.println("Web user");
            mobOrWeb = false;
        } else {
            System.out.println("User Platform is undefined");
            mobOrWeb = false;
        }

        if (site.contains("rel")) {
            camsDomain += ".wc-rel.hwtool.net";
        } else {
            camsDomain += ".com";
        }

        /*
         * Reg User save to CSV
         * */
        userOutputData = helper
                .setData(userInputData)
                .datingRegistration(mobOrWeb)
                .findDatingUser()
                .getData();

        csvDataSaver.put("autologinDating", userOutputData.get("autologinLink"));
        csvDataSaver.put("userId", userOutputData.get("userId"));
        csvDataSaver.put("camsAutologin", "Null");
        csvDataSaver.put("camsID", "Null");

        switch (status) {
            case "paid":
                userOutputData = helper
                        .buyMembership().oneMonth()
                        .getData();
                break;
            case "features":
                userOutputData = helper
                        .buyMembership().oneMonth()
                        .buyFeatures().fullUpgrade()
                        .getData();
                break;
            case "credits":
                System.out.println(userInputData);

                userOutputData = helper
                        .buyMembership().oneMonth()
                        .buyFeatures().fullUpgrade()
                        .buyLiveCamCredits().buy75()
                        .findCamsUser()
                        .getData();

                csvDataSaver.put("camsAutologin", camsDomain + "/site/autologin/key/" + userOutputData.get("autologinKey"));
                csvDataSaver.put("camsID", userOutputData.get("userId"));

                System.out.println(userOutputData.get("autologinLink"));
                System.out.println(userOutputData);
                break;
            case "free":
            default:
                userOutputData = helper
                        .getData();
                break;
        }

        System.out.println("user registered @ " + random.getDateAndTime() + " UserId: " + userOutputData.get("userId"));

        /*
         * Save user in CSV
         * */
        csv.writeToCsv(csvFile, csvDataSaver);
    }
}
