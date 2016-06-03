package core.constans;

/**
 * Created by serdyuk on 6/3/16.
 */
public class LocaleData {


    /**
     * @param language String;
     * @return language String;
     */

    public String getLocale(String language) {
        switch (language) {
            case "USA":
                language = "en-us";
                break;
            case "AUS":
                language = "en-au";
                break;
            case "NZL":
                language = "nl";
                break;
            case "IRL":
                language = "ga";
                break;
            case "CAN":
                language = "en-ca";
                break;
            case "FRA":
                language = "fr";
                break;
            case "SPA":
                language = "es";
                break;
            case "ESP":
                language = "es";
                break;
            case "NOR":
                language = "no";
                break;
            case "DNK":
                language = "da";
                break;
            case "ITA":
                language = "it";
                break;
            case "SWE":
                language = "sv";
                break;
            case "SWI":
                language = "de-ch";
                break;
            case "GER":
                language = "de";
                break;
            default:
                language = "en";
                break;
        }
        return language;
    }

}
