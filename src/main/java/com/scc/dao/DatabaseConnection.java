package dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO layer: handles the SQLite connection and table creation.
 * All JDBC / SQL for setup lives here.
 */
public class DatabaseConnection {

    // The database file. No folder in the name = it is created in your PROJECT FOLDER.
    private static final String URL = "jdbc:sqlite:petclinic.db";

    private DatabaseConnection() { }

    /** Opens a new connection to the SQLite database file (created automatically if missing). */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the petclinic table if it does not exist yet. IDs start at 1 and auto-increment. */
    public static void initializeDatabase() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS petclinic ("      // <-- changed from pet_records
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "pet_name TEXT NOT NULL, "
                + "species TEXT NOT NULL, "
                + "vaccine TEXT NOT NULL, "
                + "fee REAL NOT NULL)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}