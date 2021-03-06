package core.helpers.httpHelper;

import com.google.common.net.HttpHeaders;
import core.configs.ConfigLoader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by serdyuk on 5/10/17.
 */
public class BuyMembership {

    private BadooHelper helper;
    private final HttpClient http;
    private final JSONObject json;
    private HashMap<String, String> packages = new HashMap<>();

    String currency;

    public BuyMembership(BadooHelper BadooHelper) {
        helper = BadooHelper;
        json = helper.getUserJson();
        http = helper.http;
        try {
            JSONObject data = json.getJSONObject("membershipPackages");
            JSONArray names = data.names();
            for (int i = 0; i < data.length(); i++) {
                JSONObject item = data.getJSONObject((String) names.get(i));
                switch ((String) item.getJSONObject("package").get("interval")) {
                    case "3":
                        packages.put("threeDays", (String) names.get(i));
                        break;
                    case "28":
                        packages.put("oneMonth", (String) names.get(i));
                        break;
                    case "84":
                        packages.put("threeMonths", (String) names.get(i));
                        break;
                    case "168":
                        packages.put("sixMonths", (String) names.get(i));
                        break;
                }
            }
            currency = data.getJSONObject((String) names.get(1)).getJSONObject("price").getJSONObject("currency").getJSONObject("literal").getString("code");
        } catch (JSONException e) {
            System.out.println("cant find packages");
        }
    }

    public BadooHelper threeDays() {
        processing(packages.get("threeDays"));
        helper.outputData.put("membership", "3days");
        return helper;
    }

    public BadooHelper oneMonth() {
        processing(packages.get("oneMonth"));
        helper.outputData.put("membership", "oneMonth");

        return helper;
    }

    public BadooHelper threeMonth() {
        processing(packages.get("threeMonth"));
        helper.outputData.put("membership", "threeMonth");
        return helper;
    }

    public BadooHelper sixMonth() {
        processing(packages.get("sixMonth"));
        helper.outputData.put("membership", "sixMonth");
        return helper;
    }

    private void processing(String packageId) {
        http.get(helper.siteLink + "/site/autologin/key/" + json.getString("autologinKey"));
        http.execute();

        http.get(helper.siteLink + "/pay/membership");
        http.execute();

        List<NameValuePair> payForm = new ArrayList<>();
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[user_id]", json.getString("userId")));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[product_id]", "1"));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[domain]", json.getString("siteName")));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[currency_code]", currency));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[locale]", "en"));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[source_type]", ""));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[hidePaymentForm]", ""));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[cur_order_id]", ""));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[from_user_id]", ""));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[prevVia]", "toolbar_upgrade_button"));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[ccashAdditionalPackage]", ""));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[isAdditionalPackageRepeated]", "1"));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[package_id]", packageId));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[card_number]", ConfigLoader.MASTER_CARD_NUMBER_FULL_STRING));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[expiration_date_m]", ConfigLoader.CARDS_MONTH_EXPIRED));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[expiration_date_y]", ConfigLoader.CARDS_YEAR_EXPIRED));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[card_holder]", ConfigLoader.CARDS_CARD_HOLDER));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[security_number]", ConfigLoader.CARDS_CVV));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[name_first]", ConfigLoader.CARDS_CARD_HOLDER_FIRST));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[name_last]", ConfigLoader.CARDS_CARD_HOLDER_LAST));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[address]", json.getString("city")));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[city]", json.getString("city")));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[postal_code]", "1"));
        payForm.add(new BasicNameValuePair("CreditCardPaymentForm[country_code]", json.getString("country")));
        payForm.add(new BasicNameValuePair("ajax", "subscription"));
        payForm.add(new BasicNameValuePair("yt0", "Pay now!"));

        http.setHeader(HttpHeaders.ACCEPT, "application/json, text/javascript, */*; q=0.01");
        http.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
        http.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        http.setHeader(HttpHeaders.CONNECTION, "keep-alive");

        http.post(helper.siteLink + "/pay/pay?product_id=1" +
                        "&domain=" + json.getString("siteName") +
                        "&user_id=" + json.getString("userId") +
                        "&user_country=" + json.getString("country") +
                        "&user_locale=en&via=membership",
                payForm);

        http.execute();
    }

}
