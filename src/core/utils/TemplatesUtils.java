package core.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class help read data from json templates
 * Put this data in any post queries
 * This helpers split and convert all data to correct format
 *
 * 1. Take standart and correct json like in "Body"
 * 2. Convert him to correct and standart post request
 * 3. Returned data for post request, second part (url, converted data)
 *
 * EXAMPLE IN USING:
 *      public JSONObject confirmOrderCancellation(String orderID) {
 *      String query = readTemplateWithPlaceholders("confirmOrderCancellation", new String[]{Base64Coder.orderNodeIdEncoded(orderID)});
 *      http.post(graphQLURL, query);
 *      http.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
 *      http.setHeader("X-Philz-Auth", "test_auth_token");
 *      http.execute();

 *      return http.getJSON();
 }
 *
 *
 * Created by sserdiuk on 12/7/17.
 */
public class TemplatesUtils {
    static public String readTemplate(String templateName) {
        byte[] encoded = new byte[0];
        try {
            encoded = Files.readAllBytes(Paths.get("src/core/requestTemplates/"+templateName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String raw = new String(encoded, Charset.defaultCharset());
        String prepared = raw.replace("\n", "").replace("\r", "")
                /*.replace(" ", "")*/.replace("\t", "");
        return prepared;

    }

    static public String inlinePlaceholders(String sourcedString, String[] placeholders) {
        String[] splitted = sourcedString.split("PLACEHOLDER");
        String inlined = splitted[0];
        for (int i =1 ; i < splitted.length; i++ ) {
            inlined += placeholders[i-1] + splitted[i];
        }
        return inlined;
    }

    static public String readTemplateWithPlaceholders(String templateName, String[] placeholders ) {
        try {
            return inlinePlaceholders(readTemplate(templateName), placeholders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
