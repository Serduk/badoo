package core.constans;

/**
 * Providers
 *
 * Created by serdyuk on 5/10/17.
 */
public final class CamsModelProviders {

    public static final int INT = 10;
    public static final int CAMS = 20;
    public static final int DNX = 30;
    public static final int PLACE = 40;
    public static final int LIVETES = 50;
    public static final int REE = 60;
    public static final int IMVE = 70;

    public static String getProviderName(int providerID) {
        String providerName = "";
        switch (providerID) {
            case 10:
                providerName = "INT";
                break;
            case 20:
                providerName = "CAMS";
                break;
            case 30:
                providerName = "DNX";
                break;
            case 40:
                providerName = "PLACE";
                break;
            case 50:
                providerName = "LIVETES";
                break;
            case 60:
                providerName = "REE";
                break;
            case  70:
                providerName = "IMVE";
                break;
        }
        return providerName;
    }

}
