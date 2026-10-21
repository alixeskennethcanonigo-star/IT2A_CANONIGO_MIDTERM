package dao;

import model.PetRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO layer: all SQL statements for the pet_records table live here.
 * The UI layer and Main.java never touch SQL or JDBC.
 */
public class ScenarioDao {

    private static final String INSERT_SQL =
            "INSERT INTO petclinic (pet_name, species, vaccine, fee) VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, pet_name, species, vaccine, fee FROM petclinic ORDER BY id";

    public ScenarioDao() throws SQLException {
        dao.DatabaseConnection.initializeDatabase();
    }

    /** [1] Add Record: inserts a new record using a PreparedStatement. */
    public void insert(PetRecord record) throws SQLException {
        try (Connection conn = dao.DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, record.getPetName());
            ps.setString(2, record.getSpecies());
            ps.setString(3, record.getVaccine());
            ps.setDouble(4, record.getFee());
            ps.executeUpdate();
        }
    }

    /** [2] View Records: fetches every record from the database. */
    public List<PetRecord> findAll() throws SQLException {
        List<PetRecord> records = new ArrayList<>();

        try (Connection conn = dao.DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                records.add(new PetRecord(
                        rs.getInt("id"),
                        rs.getString("pet_name"),
                        rs.getString("species"),
                        rs.getString("vaccine"),
                        rs.getDouble("fee")));
            }
        }
        return records;
    }
}