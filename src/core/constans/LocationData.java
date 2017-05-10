package core.constans;

/**
 * Enum for locations data. Code, city, ip
 * Created by serdyuk on 5/10/17.
 */
public enum LocationData {


    GBR("GBR","London","213.171.197.181"),

    ENG("ENG","London","213.171.197.181"),

    USA("USA","New York","69.147.251.50"),

    AUS("AUS","Sydney","223.252.33.75"),

    NZL("NZL","New Roads","49.50.242.137"),

    IRL("IRL","Dublin","176.34.184.134"),

    CAN("CAN","Ottawa","199.167.19.29"),

    FRA("FRA","Paris","91.121.80.205"),

    ESP("ESP","Madrid","91.142.213.109"),

    NOR("NOR","Oslo","81.27.33.8"),

    DNK("DNK","Aarhus","94.231.110.81"),

    ITA("ITA","Verona","195.88.7.112"),

    SWE("SWE","Stockholm","91.189.44.162"),

    SWI("CHE","Zurich","92.42.186.167"),

    GER("GER","Berlin","80.237.249.248"),

    DEU("DEU","Berlin","80.237.249.248"),

    AUT("AUT","Graz","149.154.153.92"),

    FIN("FIN","Helsinki","212.94.78.134"),

    ROU("ROU","Bucuresti","188.214.23.53"),

    CZE("CZE","Praha","81.2.216.55"),

    BEL("BEL","Brussel","81.95.121.251"),

    JPN("JPN","Tokyo","110.50.241.70"),

    POL("POL","Katowice","95.171.198.220"),

    ARG("ARG","San Martin","131.100.180.0"),

    PRT("PRT","Braga","5.249.91.233"),

    BRA("BRA","Brasilia","81.95.121.251");

    /**
     * Constructor
     */

    private final String name;
    private final String city;
    private final String ip;

    LocationData(String name, String city, String ip) {

        this.city = city;
        this.ip = ip;
        this.name = name;

    }

    /**
     * method getIp
     * return ip value according to location
     */

    public String getIp() {

        return this.ip;

    }

    public String getName() {

        return this.name;

    }

    public String getCity() {

        return this.city;

    }
}
