package core.constans;

import core.configs.ConfigLoader;
import core.utils.CSVUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * This Class check js for proxy is exist?
 * If already exist => delete old file and create new with conditions for locations
 * Created by sergey on 11/1/16.
 */
public class ConfirmAuthDataForProxy {
    public ConfirmAuthDataForProxy(String proxy) {
        this.proxy = proxy;
    }

    private String proxy;


    private ConfigLoader config = new ConfigLoader();
    private CSVUtils fileCreate = new CSVUtils();

    //Set path for tmp save proxy configs
    public String saveFile = ConfigLoader.REPORTS_DIR + "/proxyData/";

    private String jsForProxy;
    private String manifestJson;


    private void setJsForProxy() {
        LocationAdclarityProxy location = LocationAdclarityProxy.valueOf(proxy.toUpperCase());
        jsForProxy = "var config = {\n" +
                "    mode: \"fixed_servers\",\n" +
                "    rules: {\n" +
                "      singleProxy: {\n" +
                "        scheme: \"http\",\n" +
                "        host:" + " \"" + location.getProxy() + "\" ,\n" +
                "        port: parseInt(" + location.getPort() + ")\n" +
                "      },\n" +
                "      bypassList: [\"foobar.com\"]\n" +
                "    }\n" +
                "  };\n" +
                "\n" +
                "chrome.proxy.settings.set({value: config, scope: \"regular\"}, function() {});\n" +
                "\n" +
                "function callbackFn(details) {\n" +
                "    return {\n" +
                "        authCredentials: {\n" +
                "            username: \"" + ConfigLoader.ADCLARITY_LOGIN + "\",\n" +
                "            password: \"" + ConfigLoader.ADCLARITY_PASSWORD + "\"\n" +
                "        }\n" +
                "    };\n" +
                "}\n" +
                "\n" +
                "chrome.webRequest.onAuthRequired.addListener(\n" +
                "        callbackFn,\n" +
                "        {urls: [\"<all_urls>\"]},\n" +
                "        ['blocking']\n" +
                ");";
    }


    private void setManifestJson() {
        manifestJson = "{\n" +
                "    \"version\": \"1.0.0\",\n" +
                "    \"manifest_version\": 2,\n" +
                "    \"name\": \"Chrome Proxy\",\n" +
                "    \"permissions\": [\n" +
                "        \"proxy\",\n" +
                "        \"tabs\",\n" +
                "        \"unlimitedStorage\",\n" +
                "        \"storage\",\n" +
                "        \"<all_urls>\",\n" +
                "        \"webRequest\",\n" +
                "        \"webRequestBlocking\"\n" +
                "    ],\n" +
                "    \"background\": {\n" +
                "        \"scripts\": [\"background.js\"]\n" +
                "    },\n" +
                "    \"minimum_chrome_version\":\"22.0.0\"\n" +
                "}";
    }

    private String getJsForProxy() {
        return jsForProxy;
    }

    private String getManifestJson() {
        return manifestJson;
    }


    public void setProxy() {
        setJsForProxy();
        setManifestJson();

        try {
            System.out.println("Try delete folder with proxy param");
            FileUtils.forceDelete(new File(saveFile));
        } catch (IOException e1) {
            System.out.println("EMPTY FOLDER \n Skip deleting folder");
        }


        fileCreate.createAnyFile(saveFile, "background", ".js");
        fileCreate.createAnyFile(saveFile, "manifest", "json");

        try {
            FileUtils.writeStringToFile(new File(saveFile + "background.js"), getJsForProxy());
            FileUtils.writeStringToFile(new File(saveFile + "manifest.json"), getManifestJson());
        } catch (IOException e) {
            System.out.println("Something Wrong With File Creation");
        }
    }
}