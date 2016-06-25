package core.configs;

/**
 * Created by serdyuk on 6/12/16.
 */
public class ConfigLoader {
    /**
     * USER PATHS
     */
    public final String FTP_URL = "screencloud.com";
    public final String SCREENSHARE_URL = "https://screencloud.com";
    /** FOR UNIX user/home/  OR C:/currentUser/*/
    public final String REPORTS_DIR = System.getProperty("user.home") + "/Pictures/testScreenShots";

    /**
     * Condition for login on screenCloud
     * */
    public final String SCREEN_CLOUD_LOGIN = "mylogin@mail.com";
    public final String SCREEN_CLOUD_PASSWORD = "myPassword";
}
