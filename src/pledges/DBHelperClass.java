/**
 *
 * @author Brandon Wilson <brandon@nyneaxis.com>
 * @date Aug 9, 2012
 * @email exdox77@gmail.com
 *
 * Beginning of Java Class DBHelperClass
 */
package pledges;

import static java.lang.System.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DBHelperClass {

    final private static String databaseName = "pledgesDatabase";
    private static String tableName = "PLEDGES";
    private static String databaseURL = "jdbc:derby:" + getProperty("user.home") + getProperty("file.separator") + databaseName + ";create=true";
    private static Connection conn = null;
    private static Statement statement = null;

    /**
     * Constructor for DBHelperClass(), not currently used.
     */
    public DBHelperClass() {
    }

    /**
     * Connects to database, if no database exists it will create one. Once
     * database is created/connected it will check for default table. If no
     * table exists it will create one
     *
     * @return true - if operation is successful. false otherwise.
     */
    private Boolean createConnection() {
        if (connectDatabase()) {
            try {
                DatabaseMetaData meta = conn.getMetaData();

                statement = conn.createStatement();
                ResultSet rs = meta.getTables(conn.getCatalog(), null, tableName, null);
                if (!rs.next()) {
                    String createTableSQL = "CREATE TABLE " + tableName
                            + " (PledgeID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                            + "  Name CHAR(125),"
                            + "  Charity CHAR(50),"
                            + "  Pledge CHAR(25),"
                            + "  CONSTRAINT primary_key PRIMARY KEY (PledgeID))";

                    statement.execute(createTableSQL);
                    out.printf("Created Table: %s\r\n", tableName);
                    return true;
                } else {
                    out.printf("Table %s Exists\r\n", tableName);
                    return true;
                }
            } catch (SQLException ex) {
                out.println(String.format("Error Occurred while Executing Command: %s \n%s", databaseName, ex.toString()));
                return false;
            }
        }
        return false;
    }

    /**
     * Selects all records from database.
     *
     * @return ArrayList of String arrays with records from database. null if no
     * data available.
     */
    public ArrayList selectAllFromDatabase() {
        ArrayList<String[]> allFromDatabase = new ArrayList<>();
        try {
            if (createConnection()) {
                out.println("Getting all records");
                try {
                    statement = conn.createStatement();
                    ResultSet rs = statement.executeQuery(String.format("SELECT * FROM %s", tableName));
                    while (rs.next()) {

                        String id = rs.getString("PledgeID");
                        String name = rs.getString("Name");
                        String charity = rs.getString("Charity");
                        String pledge = rs.getString("Pledge");
                        String[] string = new String[]{name, charity, pledge};
                        allFromDatabase.add(string);
                    }
                    return allFromDatabase;
                } catch (SQLException ex) {
                    out.printf("Database error 1 %s\r\n", ex);
                } finally {
                    closeDatabase();
                }
            }
        } catch (Exception ex) {
            out.printf("Database error 2 %s\r\n", ex);
        }
        return null;
    }

    /**
     * Insert new record into database.
     *
     * @param name Name of charity donor
     * @param charity Name of charity
     * @param pledge Amount of pledge
     * @return true if it was successful. false otherwise.
     */
    public Boolean insertRecord(String name, String charity, String pledge) {
        try {
            if (createConnection()) {
                out.println("Inserting new records");
                try {
                    statement = conn.createStatement();
                    String insertRecordSQL = String.format("INSERT INTO %s(Name, Charity, Pledge) VALUES('%s', '%s', '%s')",
                            tableName, name, charity, pledge);
                    statement.executeUpdate(insertRecordSQL);
                    out.println("New records inserted");
                } catch (SQLException ex) {
                    out.println(String.format("Error Occurred while Executing Command: %s \n%s", databaseName, ex.toString()));
                } finally {
                    closeDatabase();
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            out.printf("Database error %s\r\n", ex);
        }
        return false;
    }

    /**
     * Creates connection to database.
     *
     * @return true if the connection was successful. false otherwise.
     */
    private Boolean connectDatabase() {
        out.printf("Connecting to Database: %s\r\n", databaseName);
        try {
            conn = DriverManager.getConnection(databaseURL);
            out.println("Database Connection Successful");
            return true;
        } catch (SQLException ex) {
            out.println(String.format("Error Occurred while connecting to database: %s \n%s", databaseName, ex.toString()));
            JOptionPane.showMessageDialog(null, String.format("Error Occurred while connecting to Database: %s", databaseName));
            return false;
        }
    }

    /**
     * Closes connection to the database.
     *
     * @return true if the database closed successful. false otherwise.
     */
    public Boolean closeDatabase() {
        try {
            if (conn.isValid(60)) {
                try {
                    conn.close();
                    out.printf("Closing Database %s\r\n", databaseName);
                    return true;
                } catch (SQLException e) {
                    out.printf("Error Occurred while closing database: \r\n%s", e.toString());
                    return false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBHelperClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
    /**
     *
     * End of Java Class DBHelperClass
     *
     */