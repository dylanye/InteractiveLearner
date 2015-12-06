package main.java.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by dylan on 6-12-15.
 */
public class ConnectionConfig {

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:/home/dylan/Documents/Development/InteractiveLearner/Database/AILearner.sqlite");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

}