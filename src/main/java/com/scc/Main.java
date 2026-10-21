import dao.ScenarioDao;
import ui.ConsoleUI;

import java.sql.SQLException;

// The program starts here. It only creates the DAO and starts the menu.
// No SQL in this file.
public class Main {
    public static void main(String[] args) {
        try {
            ScenarioDao dao = new ScenarioDao();
            ConsoleUI ui = new ConsoleUI(dao);
            ui.start();
        } catch (SQLException e) {
            System.out.println("Could not start the system: " + e.getMessage());
        }
    }
}