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
}
