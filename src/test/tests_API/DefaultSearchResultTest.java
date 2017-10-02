package test.tests_API;

import com.google.common.net.HttpHeaders;
import core.helpers.httpHelper.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Get profile list in search
 * Get ScreenName/Age/ID etc
 * This test check default profiles in search
 * For diff niche etc.
 * <p>
 * Created by sergey on 11/23/16.
 */
public class DefaultSearchResultTest {
    @Test
    public void getJsonFromSite() throws IOException {
        HttpClient http = new HttpClient();

        http.get("https://www.badoo.com/site/autologin/key/dbb36b33ad980629c30987a672ffab7e");
        http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        http.execute();

        JSONObject result = http.getJSON();
        System.out.println(result);

        http.get("https://www.badoo.com/search/?offset=0&limit=48&sortType=photo_quality");
        http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
        http.execute();

        JSONArray result1 = http.getJSON().getJSONObject("data").getJSONArray("users");
        int status = http.getJSON().getJSONObject("meta").getInt("code");
        Assert.assertTrue(status != 200, "Status for page load not 200");

        System.out.println(status);
        System.out.println(result1);

        for (int i = 0; i < result1.length(); i++) {
            JSONObject user = result1.getJSONObject(i);
//            System.out.println(user);
            String id = result1.getJSONObject(i).getString("id");
            System.out.println(id);
            if (result1.getJSONObject(i).getString("id").equals("cc54d75c12a011e68a6f101f74370270")) {
                System.out.println("TRUE");
                break;
            }
        }
    }
}
