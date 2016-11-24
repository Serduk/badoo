package test.exapmles.dataproviderExample;

import core.jdbc.BadooJDBC;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Examples for dataProvider from SQL
 * Created by serdyuk on 11/24/16.
 */
public class DataProviderSiteExample {

    @DataProvider(name = "dp")
    public Object[][] dataProvider() throws SQLException {
        BadooJDBC pr = new BadooJDBC();

        return pr.getLocations();

    }

    @Test(dataProvider = "dp")
    public void connectionsTest(Object one, Object two, Object three, Object four, Object five) throws IOException, InterruptedException, SQLException {

        System.out.println("ID: " + one);
        System.out.println("LOCATION: " + two);
        System.out.println("CITY: " + three);
        System.out.println("IP_ADDRESS: " + four);
        System.out.println("LOCALE: " + five);
        System.out.println("------------------------------------");
    }
}
