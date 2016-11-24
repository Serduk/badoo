package core.jdbc;


import mySQL.DatabaseUtils;

import java.sql.SQLException;

/**
 * DB Connector class
 * Created by serdyuk on 11/24/16.
 */
public class BadooJDBC {


    private DatabaseUtils db;

    public BadooJDBC() {
        db = new DatabaseUtils();
    }


    /**
     * @return result of table "locations"
     * @throws SQLException test.example.dataproviderExample.DataProviderLocationExample
     */
    public Object[][] getLocations() throws SQLException {
        db.connect();
//        Object[][] result = db.query("Select location, city from locations");
        Object[][] result = db.query("Select location from locations");
        db.disconnect();
        return result;
    }


    /**
     * @return Badoo Site Locales
     * @throws SQLException
     */
    public Object[][] getBadooLocales() throws SQLException {
        db.connect();
        Object[][] result = db.query("Select locale From BadooLocales");
        db.disconnect();
        return result;
    }

    /**
     * @return result of table "al_sites"
     * @throws SQLException see example @ test.example.dataproviderExample.dataProviderSitesExample
     */
    public Object[][] getAGSites() throws SQLException {
        db.connect();
        Object[][] result = db.query("SELECT siteLink from al");
        db.disconnect();
        return result;
    }

    public Object[][] getAllSites() throws SQLException {
        db.connect();
        Object[][] result = db.query(
                "SELECT siteName, siteLive from all_sites union " +
                        "SELECT siteName, siteLive from allTop_sites union " +
                        "SELECT siteName, siteLive from allTopS_sites");
        db.disconnect();
        return result;
    }

    public Object[][] getAllSitesNew() throws SQLException {
        db.connect();
        Object[][] result = db.query(
                "SELECT siteLink from al union " +
                        "SELECT siteLink from all union " +
                        "SELECT siteLink from allTop"
        );
        db.disconnect();
        return result;
    }

    /**
     * @param array1 (any Object[][])
     * @param array2 (any Object[][])
     * @return Object[][]
     * for each array 1 add each array2
     * <p>
     * see example @ test.example.dataproviderExample.DataProviderMixedExample
     */
    public Object[][] getMixed(Object[][] array1, Object[][] array2) {

        //pre init (max x, max y)
        int maxRows = array1.length * array2.length;
        int maxCols = array1[0].length + array2[0].length;


        //setting new empty Object[x][y]
        Object[][] endResult = new Object[maxRows][maxCols];

        //setting Object counter;
        int count = 0;

        //for each row of array1 do:
        for (Object[] anArray1 : array1) {

            //for each row of array2 do:
            for (Object[] anArray2 : array2) {

                //Object[counter] = merged array1.row with array2.row
                System.arraycopy(anArray1, 0, endResult[count], 0, anArray1.length);
                System.arraycopy(anArray2, 0, endResult[count], anArray1.length, anArray2.length);
                count++;
            }
        }
        //return complete result
        return endResult;
    }

    /**
     * @return Object[][] TrafficSources
     * @throws SQLException
     */
    public Object[][] getTrafficSources() throws SQLException {
        db.connect();
        Object[][] result = db.query(
                "SELECT link FROM trafficSources where name = 'Aff CPA' or name = 'Aff DDA' or name = 'SEO'");
        db.disconnect();
        return result;
    }


}
