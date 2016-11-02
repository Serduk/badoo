package core.constans;
/**
 * Free proxy list
 * Actual list always here: http://www.gatherproxy.com/ru/proxylistbycountry
 * Created by sergey on 10/25/16.
 */
public enum LocationsProxy {

    CAN("158.69.210.29", "8080"),
    USA("166.62.86.208", "80");


    /**
     * Constructor
     */

    private final String proxy;
    private final String port;

    LocationsProxy(String proxy, String port) {
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
