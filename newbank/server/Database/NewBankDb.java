package newbank.server.Database;
import java.sql.*;

public class NewBankDb {

    Connection connection = null;

    public Connection createConnection(){

        try{
            System.out.println(getClass());
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:NewBank.db");
            System.out.println("Opened database successfully");
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            //createConnection();
        }

        return connection;
    }

    public void closeConnection(){
        try{
            connection.close();
            System.out.println("Closed database successfully");

        }
        catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           // closeConnection();
        }
    }
}
