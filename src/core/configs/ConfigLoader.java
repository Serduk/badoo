package core.configs;

/**
 * Class with all passwords and keyDatas
 * Created by serdyuk on 6/12/16.
 */
public class ConfigLoader {
    /**
     * USER PATHS
     */
    public final String FTP_URL = "screencloud.com";
    public final String SCREENSHARE_URL = "https://screencloud.com";
    /**
     * FOR UNIX user/home/  OR C:/currentUser/
     */
    public final String REPORTS_DIR = System.getProperty("user.home") + "/Pictures/testScreenShots";

    /**
     * Condition for login on screenCloud
     */
    public final String SCREEN_CLOUD_LOGIN = "mylogin@mail.com";
    public final String SCREEN_CLOUD_PASSWORD = "myPassword";

    public static final String ADCLARITY_LOGIN = "myAdclarityLogin";
    public static final String ADCLARITY_PASSWORD = "myPassword";

    /**
     * DATA BASE CONNECTIONS
     */
    public final String DB_URL = "jdbc:mysql://127.0.0.1:3306";
    public final String DB_NAME = "badoo";
    public final String DB_USER = "root";
    public final String DB_PASS = "111";
}
