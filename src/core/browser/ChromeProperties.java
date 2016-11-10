package core.browser;

/**
 * Created by serdyuk on 6/3/16.
 */
public class ChromeProperties {
    private String osName = System.getProperty("os.name").toLowerCase();
    private String osArch = System.getProperty("os.arch").toLowerCase();

    public String osDetection() {
        if (osName.contains("linux") || osName.contains("nix")) {
            if (osArch.contains("64")) {
                return "resources/chromedriver_x64";
            }
            return "resources/chromedriver";
        } else if (osName.contains("mac") || osName.contains("osX")) {
            return "resources/chromedriver_osX";
        } else {
            return "resources/chromedriver.exe";
        }
    }
}
