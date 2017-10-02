package test.tests_API;

import com.google.common.net.HttpHeaders;
import core.dataProvider.BadooProvider;
import core.helpers.httpHelper.HttpClient;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.AbstractTestScenario;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Test for check WhoIs on our sites
 * Check address, copyRights etc.
 *
 * Created by sergey on 12/23/16.
 * mvn -Dtest=WhoisCheck test clean
 *
 * Created by serdyuk on 5/10/17.
 */

    public class WhoisCheck extends AbstractTestScenario {
        private HttpClient http = new HttpClient();

        @DataProvider(name = "dp")
        public Object[][] dataProvider() throws SQLException {
            BadooProvider provider = new BadooProvider();
            return provider.getTable1SiteName();
        }

        @Test(dataProvider = "dp")
        public void executeTest(String site) throws IOException {
            http.get("http://api.bulkwhoisapi.com/whoisAPI.php?domain=" + site + ".com&token=7d3f08b98ab9f69ae15060a5b58ef1ee");
            http.setHeader(HttpHeaders.X_REQUESTED_WITH, "XMLHttpRequest");
            http.execute();

            JSONObject result = http.getJSON();
            String resultString = result.toString().toLowerCase();

            Assert.assertEquals(resultString.contains("holding"), false);
        }
}
