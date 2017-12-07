package test.tests_API;

import com.google.common.net.HttpHeaders;
import core.configs.ConfigLoader;
import core.constans.CamsModelProviders;
import core.utils.CSVUtils;
import core.helpers.httpHelper.HttpClient;
import core.helpers.userHelper.DatingUser;
import core.helpers.userHelper.PaidStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.AbstractTestScenario;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sergey on 8/22/16.
 * Get model provider
 * Get model list
 * Get data from search, and create csv file with data model from search
 * This test use jSon, and parse all users from 2 source.
 * Get modelID, ModelName, ModelStatus, ModelProvider
 */

public class ModelProviderTest extends AbstractTestScenario {
    private String csvFile;
    private HashMap<String, String> reportData = new HashMap<>();

    @BeforeClass
    public void initAll() throws IOException {
        csvFile = csvWorker.createCsv("/reports/reportCamsSearchResultOnDating" + random.getRandomDateForRegUser());
        reportData.put("platform", "PLATFORM");
        reportData.put("userId", "USERID");
        reportData.put("autologin", "AUTOLOGIN");
        reportData.put("site", "SITELINK");
        reportData.put("location", "LOCATION");
        reportData.put("trafficSource", "TRAFFIC_SOURCE");
        reportData.put("search", "SEARCH");
        reportData.put("searchModelID", "MODEL_ID_ON_SEARCH");
        reportData.put("searchModelStatus", "CHAT_STATUS_SEARCH");
        reportData.put("searchScreenname", "SCREEN_NAME_SEARCH");
        reportData.put("whoIsOnCam", "WHO_IS_ON_CAM");
        reportData.put("camModelID", "MODEL_ID_ON_CAM");
        reportData.put("camModelStatus", "CHAT_STATUS_CAM");
        reportData.put("camScreenname", "SCREEN_NAME_CAM");

        csvWorker.writeToCsv(csvFile, reportData);
    }


    @DataProvider(name = "dp", parallel = false)
    public Iterator<Object[]> provider() throws IOException {
        CSVUtils csv = new CSVUtils();
        return csv.csvReader(ConfigLoader.REPORTS_DIR + "/" + "modelProviderTest.csv");
    }

    @Test(dataProvider = "dp")
    public void getJsonFromSite(String platform,
                                String siteLink,
                                String trafficSource,
                                String location) throws IOException {
        DatingUser user = null;
        user = new DatingUser(siteLink, location, trafficSource, platform);
        reportData.put("platform", platform);
        reportData.put("userId", user.userId);
        reportData.put("autologin", user.autologin);
        reportData.put("site", siteLink);
        reportData.put("location", location);
        reportData.put("trafficSource", trafficSource);
        reportData.put("search", "");
        reportData.put("searchModelID", "");
        reportData.put("searchModelStatus", "");
        reportData.put("searchScreenname", "");
        reportData.put("whoIsOnCam", "");
        reportData.put("camModelID", "");
        reportData.put("camModelStatus", "");
        reportData.put("camScreenname", "");

        csvWorker.writeToCsv(csvFile, reportData);

        reportData.put("platform", "");
        reportData.put("userId", "");
        reportData.put("autologin", "");
        reportData.put("site", "");
        reportData.put("location", "");
        reportData.put("trafficSource", "");

        user.buy(PaidStatus.features);

        HttpClient http = new HttpClient();

        http.get(user.autologin);
        http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        http.execute();

        JSONObject result = http.getJSON();
        System.out.println(result);

        http.get("https://" + user.site + "/search/?offset=0&limit=48&sortType=photo_quality");
        http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        http.execute();

        JSONArray result1 = http.getJSON()
                .getJSONObject("data")
                .getJSONArray("users");
        int       status  = http.getJSON()
                .getJSONObject("meta")
                .getInt("code");
        if (status != 200) {
            System.out.println("Status for page load not 200");
            Assert.assertFalse(false);
        }

        System.out.println(result1);
        System.out.println(status);

        for (int i = 0;i < result1.length();i++) {
            JSONObject models = result1.getJSONObject(i);
            try {
                System.out.println(models.getInt("modelProvider"));
                int    modelProvider = models.getInt("modelProvider");
                String modelId       = models.getString("modelId");
                String login         = models.getString("login");
                int    chatStatus    = models.getInt("chatStatus");

                CamsModelProviders.getProviderName(modelProvider);
                System.out.println("Model Provider will be " + CamsModelProviders.getProviderName(modelProvider));
                reportData.put("SEARCH", CamsModelProviders.getProviderName(modelProvider));
                reportData.put("searchModelID", modelId);
                reportData.put("searchModelStatus", String.valueOf(chatStatus));
                reportData.put("searchScreenname", login);
                csvWorker.writeToCsv(csvFile, reportData);
            }
            catch (JSONException ignored) {
                reportData.put("SEARCH", "Dating User");
                csvWorker.writeToCsv(csvFile, reportData);
            }
            finally {
                reportData.put("SEARCH", "");
                reportData.put("searchModelID", "");
                reportData.put("searchModelStatus", "");
                reportData.put("searchScreenname", "");
            }
        }

        System.out.println("==============================MODEL LIST PAGE=================================");

        http.get("https://" + user.site + "/webCamList");
        http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        http.execute();
        status = http.getJSON()
                .getJSONObject("meta")
                .getInt("code");
        if (status != 200) {
            System.out.println("Status for page load not 200");
            Assert.assertFalse(false);
        }

        JSONArray result2 = http.getJSON()
                .getJSONArray("data");
        for (int i = 0;i < result2.length();i++) {
            JSONObject models = result2.getJSONObject(i);
            try {
                System.out.println(models.getInt("modelProvider"));
                int    modelProvider = models.getInt("modelProvider");
                String modelId       = models.getString("modelId");
                String login         = models.getString("login");
                int    chatStatus    = models.getInt("chatStatus");

                CamsModelProviders.getProviderName(modelProvider);
                System.out.println("Model Provider will be " + CamsModelProviders.getProviderName(modelProvider));
                System.out.println(login);

                reportData.put("whoIsOnCam", CamsModelProviders.getProviderName(modelProvider));
                reportData.put("camModelID", modelId);
                reportData.put("camModelStatus", String.valueOf(chatStatus));
                reportData.put("camScreenname", login);
                csvWorker.writeToCsv(csvFile, reportData);

            }
            catch (JSONException ignored) {
                reportData.put("whoIsOnCam", "Something wrong");
                csvWorker.writeToCsv(csvFile, reportData);
            }
            finally {
                reportData.put("whoIsOnCam", "");
                reportData.put("camModelID", "");
                reportData.put("camModelStatus", "");
                reportData.put("camScreenname", "");
            }
        }
    }
}
