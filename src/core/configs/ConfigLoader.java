package core.configs;

/**
 * Class with all passwords and keyDatas
 * Created by serdyuk on 6/12/16.
 */
public class ConfigLoader {
    /**
     * USER PATHS
     */
    public static final String FTP_URL = "screencloud.com";
    public static final String SCREENSHARE_URL = "https://screencloud.com";
    /**
     * FOR UNIX user/home/  OR C:/currentUser/
     */
    public static final String REPORTS_DIR = System.getProperty("user.home") + "/Pictures/testScreenShots";

    /**
    * Access conditional
    * */
    public static final String IPA_LOGIN = "";
    public static final String IPA_PASSWORD = "";

    /**
     * Condition for login on screenCloud
     */
    public static final String SCREEN_CLOUD_LOGIN = "mylogin@mail.com";
    public static final String SCREEN_CLOUD_PASSWORD = "myPassword";

    public static final String ADCLARITY_LOGIN = "myAdclarityLogin";
    public static final String ADCLARITY_PASSWORD = "myPassword";

    /**
     * DATA BASE CONNECTIONS
     */
    public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306";
    public static final String DB_NAME = "badoo";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "111";

    public static final  String STREAM_TEST_PLATFORM_ADMIN_DOMAIN = "TestEnvironmentAdminLogin";
    public static final  String STREAM_TEST_PLATFORM_PASSWORD = "TestEnvironmentAdminPassword";

    public static final  String STREAM_LIVE_ADMIN_DOMAIN = "LiveEnvironmentAdminLogin";
    public static final  String STREAM_LIVE_ADMIN_PASSWORD = "LiveEnvironmentAdminPassword";

    public static final String BADOO_LIVE_ADMIN_DOMAIN = "https://badoo.com/admin";

    /**
     * Webdriver instances
     */
    public final static String REMOTE_WEBDRIVER_HUB = "http://192.168.12.27:4444/wd/hub";
    public final static String LOCAL_WEBDRIVER_HUB = "http://127.0.0.1:4444/wd/hub";


    /**
     * Visa card data:
     */
    public final static String   VISA_CARD_NUMBER_FULL_STRING = "8888888888888888";
    public final static String[] VISA_CARD_NUMBER             = new String[]{"8888", "8888", "8888", "8888"};

    /**
     * Master card number:
     */
    public final static String   MASTER_CARD_NUMBER_FULL_STRING = "1111111111111111";
    public final static String[] MASTER_CARD_NUMBER             = new String[]{"1111", "1111", "1111", "1111"};


    /**
     * new Master card
     */
    public final static String   MASTER_CARD_NUMBER_FULL_JOHN       = "1111111111111111";
    public final static String[] MASTER_CARD_NUMBER_FULL_JOHN_ARRAY = new String[]{"1111", "1111", "1111", "1111"};
    public final static String   CARD_HOLDER_JOHN_Doe           = "John Doe";

    /**
     * Cards Data
     */
    public final static String   CARDS_MONTH_EXPIRED     = "10";
    public final static String   CARDS_YEAR_EXPIRED      = "2020";
    public final static String[] CARDS_EXPIRED_DATE      = new String[]{"10", "2020"};
    public final static String   CARDS_CVV               = "555";
    public final static String   CARDS_CARD_HOLDER       = "John Doe";
    public final static String   CARDS_CARD_HOLDER_FIRST = "John";
    public final static String   CARDS_CARD_HOLDER_LAST  = "Doe";
}
