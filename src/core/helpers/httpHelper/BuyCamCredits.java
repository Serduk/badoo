package core.helpers.httpHelper;

import com.google.common.net.HttpHeaders;
import exceptions.BadooException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Buy Credits for cams
 * Created by serdyuk on 5/10/17.
 */
public class BuyCamCredits {
    private       BadooHelper helper;
    private final HttpClient    http;
    private final JSONObject json;
    private List<String> packages;
    String currency;

    public BuyCamCredits(BadooHelper BadooHelper) {
        helper = BadooHelper;
        json = helper.getUserJson();
        http = helper.http;

        http.get(helper.siteLink + "/site/autologin/key/" + json.getString("autologinKey"));
        http.execute();

        http.get(helper.siteLink + "/pay/liveCamCredits");
        http.execute();
        try {
            String pattern         = "<input id=\"package(.*?)\"";
            String currencyPattern = "WebcamCreditCardPaymentForm_currency_code\" type=\"hidden\" value=\"(.*?)\"";
            packages = new ArrayList<>();
            Matcher m  = Pattern.compile(pattern)
                    .matcher(http.content);
            Matcher m2 = Pattern.compile(currencyPattern)
                    .matcher(http.content);
            while (m.find()) {
                packages.add(m.group(1));
            }
            if (m2.find()) {
                currency = m2.group(1);
            } else {
                throw new BadooException("Currency not found");
            }
        }
        catch (PatternSyntaxException e) {
            System.out.println("patternException");
        }
        catch (BadooException e) {
            e.printStackTrace();
        }
    }

    public BadooHelper buy35() {
        processing(packages.get(0));
        helper.outputData.put("liveCamCredits", "35Credits");
        return helper;
    }

    public BadooHelper buy75() {
        processing(packages.get(1));
        helper.outputData.put("liveCamCredits", "75Credits");
        return helper;
    }

    private void processing(String packageId) {

        List<NameValuePair> payForm = new ArrayList<>();
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[user_id]", json.getString("userId")));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[product_id]", "21"));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[domain]", json.getString("siteName")));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[currency_code]", currency));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[locale]", "en"));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[hidePaymentForm]", "1"));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[cur_order_id]", ""));
        payForm.add(new BasicNameValuePair("handlerName", "LiveCamCredits"));
        payForm.add(new BasicNameValuePair("WebcamCreditCardPaymentForm[package_id]", packageId));

        http.setHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01");
        http.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
        http.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader(HttpHeaders.CONNECTION, "keep-alive");

        http.post(helper.siteLink + "/pay/pay?product_id=21" +
                        "&domain=" + json.getString("siteName") +
                        "&user_id=" + json.getString("userId") +
                        "&user_country=" + json.getString("country") +
                        "&user_locale=en" +
                        "&via=posttrans_pup_promo",
                payForm);
        http.execute();
    }

}
