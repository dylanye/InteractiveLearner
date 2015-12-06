import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by dylan on 6-12-15.
 */
public class TestCon {

    public static void main(String[] args){
        Connection connection = null;

        try {
            connection = ConnectionConfig.getConnection();
            if (connection != null){
                System.out.println("Connection is established");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (connection != null){
                try {
                    connection.close();
                    System.out.println("Connection is closed");
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
