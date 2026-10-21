package ui;

import dao.ScenarioDao;
import model.PetRecord;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * UI layer: menu loop, input validation, and formatted output.
 * No SQL or JDBC code here - every database action goes through ScenarioDao.
 */
public class ConsoleUI {

    private static final String[] SPECIES = {"DOG", "CAT", "BIRD"};
    private static final String[] VACCINES = {"RABIES", "DHPP", "FVRCP"};
    private static final int MAX_NAME_LENGTH = 18;

    private static final String LINE_MENU = "=".repeat(53);
    private static final String LINE_TABLE_TITLE = "=".repeat(79);
    private static final String TABLE_BORDER =
            "+----+--------------------+------------+--------------+----------------+";

    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao;

    public ConsoleUI(ScenarioDao dao) {
        this.dao = dao;
    }

    /** Main menu loop. Runs until the user chooses [3] Exit System. */
    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();   // nextLine() only -> no leftover newline problem

            switch (input) {
                case "1":
                    addPetRecord();
                    break;
                case "2":
                    viewRecords();
                    break;
                case "3":
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                    break;
                default:
                    System.out.println("\nINVALID CHOICE: Please enter 1, 2, or 3.\n");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(LINE_MENU);
        System.out.println("          PET CLINIC VACCINATION LOG");
        System.out.println(LINE_MENU);
        System.out.println("[1] Add Pet Record");
        System.out.println("[2] View Vaccination Records");
        System.out.println("[3] Exit System");
        System.out.println(LINE_MENU);
    }

    // ---------------------------------------------------------------- [1] ADD
    private void addPetRecord() {
        System.out.println("\n--- ADD PET VACCINATION RECORD ---");

        String name = readPetName();
        String species = readChoice("Enter Species (DOG / CAT / BIRD): ", SPECIES);
        String vaccine = readChoice("Enter Vaccine Type (RABIES / DHPP / FVRCP): ", VACCINES);
        double fee = readFee();

        try {
            dao.insert(new PetRecord(name, species, vaccine, fee));
            System.out.println("\nSUCCESS: Pet vaccination record saved!\n");
        } catch (SQLException e) {
            System.out.println("\nERROR: Could not save record - " + e.getMessage() + "\n");
        }
    }

    private String readPetName() {
        while (true) {
            System.out.print("Enter Pet Name: ");
            String name = scanner.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("INVALID: Pet name cannot be empty.");
            } else if (name.length() > MAX_NAME_LENGTH) {
                System.out.println("INVALID: Pet name must be at most " + MAX_NAME_LENGTH + " characters.");
            } else {
                return name;
            }
        }
    }

    private String readChoice(String prompt, String[] allowed) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim().toUpperCase();

            for (String option : allowed) {
                if (option.equals(value)) {
                    return value;
                }
            }
            System.out.println("INVALID: Please enter one of " + String.join(", ", allowed) + ".");
        }
    }

    private double readFee() {
        while (true) {
            System.out.print("Enter Service Fee: ");
            String input = scanner.nextLine().trim();

            try {
                double fee = Double.parseDouble(input);
                if (fee < 0) {
                    System.out.println("INVALID: Fee cannot be negative.");
                } else {
                    return fee;
                }
            } catch (NumberFormatException e) {
                System.out.println("INVALID: Please enter a valid number (e.g., 500.00).");
            }
        }
    }

    // --------------------------------------------------------------- [2] VIEW
    private void viewRecords() {
        try {
            List<PetRecord> records = dao.findAll();

            System.out.println();
            System.out.println(LINE_TABLE_TITLE);
            System.out.println("                              PET VACCINATION RECORDS");
            System.out.println(LINE_TABLE_TITLE);
            System.out.println(TABLE_BORDER);
            System.out.printf("| %-2s | %-18s | %-10s | %-12s | %-14s |%n",
                    "ID", "PET NAME", "SPECIES", "VACCINE", "FEE");
            System.out.println(TABLE_BORDER);

            for (PetRecord r : records) {
                System.out.printf("| %-2d | %-18s | %-10s | %-12s | %-14.2f |%n",
                        r.getId(), r.getPetName(), r.getSpecies(), r.getVaccine(), r.getFee());
            }

            System.out.println(TABLE_BORDER);
            System.out.println("Total Records Displayed: " + records.size());
            System.out.println();
        } catch (SQLException e) {
            System.out.println("\nERROR: Could not load records - " + e.getMessage() + "\n");
        }
    }
}