package core.browser;

/**
 * Created by serdyuk on 6/3/16.
 */
public class ChromeProperties {
    private String unixPath = "resources/chromedriver_x64";
    private String winPath = "resources/chromedriver.exe";

    String osName = System.getProperty("os.name").toLowerCase();

    public String osDetection() {
        if (osName.contains("linux") || osName.contains("nix")) {
            return unixPath;
        } else if (osName.contains("mac") || osName.contains("osX")) {
            return "not found";
        } else {
            return winPath;
        }
    }
}
