package mySQL;

import core.configs.ConfigLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * DataBase connector  & query executor
 * Created by serdyuk on 11/24/16.
 */
public class DatabaseUtils {


    private ConfigLoader config = new ConfigLoader();

    private Connection connection;
    private Properties properties;

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", config.DB_USER);
            properties.setProperty("password", config.DB_PASS);
        }
        return properties;
    }

    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String url = config.DB_URL + "/" + config.DB_NAME;
                connection = DriverManager.getConnection(url, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param query String, Mysql -lang query
     * @return data in HashMap type object like a [StringNumber]{columnName, columnValue}
     * @throws SQLException
     */
    public Object[][] query(String query) throws SQLException {

        Statement st = connection.createStatement();
        ResultSet queryResult = st.executeQuery(query);
        ResultSetMetaData resultMeta = queryResult.getMetaData();
        int columnCount = resultMeta.getColumnCount();

        ArrayList<Object[]> result = new ArrayList<>();

        while (queryResult.next()) {
            Object[] str = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                Object obj = queryResult.getObject(i+1);
                str[i] = obj;
            }
            result.add(str);
        }

        int resultLength = result.size();

        Object[][] finalResult = new Object[resultLength][columnCount];
        for(int i = 0; i < resultLength; i++) {
            finalResult[i] =result.get(i);
        }
        return finalResult;

    }

    public void update(String query) throws SQLException {
        Statement st = connection.createStatement();
        st.executeQuery(query);
    }

    public void insert(String query) throws SQLException {
        Statement st = connection.createStatement();
        st.executeQuery(query);
    }
}
