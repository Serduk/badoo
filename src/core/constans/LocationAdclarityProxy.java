package core.constans;

/**
 * Actual list for proxy in Adclarity settings.
 * How get actual proxy: https://goo.gl/xuboFx
 * Created by sergey on 11/1/16.
 */
public enum LocationAdclarityProxy {
    AUS("p-au1.biscience.com", "80"),
    CAN("p-ca.biscience.com", "80"),
    DNK("p-dk1.biscience.com", "80"),
    FIN("p-fi1.biscience.com", "80"),
    FRA("p-fr1.biscience.com", "80"),
    IRL("p-ie1.biscience.com", "80"),
    ITA("p-it1.biscience.com", "80"),
    SPA("p-es1.biscience.com", "80"),
    SWE("p-se1.biscience.com", "80"),
    USA("p-us2.biscience.com", "80");


    /**
     * Constructor
     */

    private final String proxy;
    private final String port;

    LocationAdclarityProxy(String proxy, String port) {
        this.proxy = proxy;
        this.port = port;
    }


    /**
     * method getPort
     * return Port value according to Proxy
     */

    public String getPort() {
        return this.port;
    }

    public String getProxy() {
        return this.proxy;
    }
}
