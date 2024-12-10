package com.group11.classicmodels;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * CRUD operations for database interaction.
 * Handles Create, Read, Update and Delete operations on a database.
 * @author Christian Douglas Farnes Fancy
 */
public class CRUD {

    private DatabaseConnection dbConnection; // Holds the database connection object

    /**
     * Constructor to initialize CRUD operations with a database connection.
     * @param dbConnection DatabaseConnection object for database connectivity
     */
    public CRUD(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Inserts a new record into the specified table.
     * @param tableName The name of the table where the record will be inserted
     * @param columnValues A map of column names and their corresponding values for the new record
     */
    public void insert(String tableName, Map<String, Object> columnValues) {
        // Building the INSERT SQL query dynamically based on input parameters
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (String column : columnValues.keySet()) {
            columns.append(column).append(",");
            values.append("?").append(",");
        }

        // Removing the trailing comma from columns and values
        columns.setLength(columns.length() - 1);
        values.setLength(values.length() - 1);

        // Forming the final SQL query
        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";

        // Executing the query
        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
            int i = 1;
            for (Object value : columnValues.values()) {
                pst.setObject(i++, value);
            }
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record Inserted");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting record: " + e.getMessage());
        }
    }

 // Read (Select)
    /**
     * Retrieves records from a specified table based on the given criteria.
     * @param tableName The name of the table to read from.
     * @param columns The columns to select in the query.
     * @param whereClause The WHERE clause for filtering records. Can be null or empty for no filtering.
     * @param whereArgs The arguments for the WHERE clause, if any.
     */
    public void read(String tableName, String[] columns, String whereClause, Object[] whereArgs) {
        // Building the SELECT SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT ");
        queryBuilder.append(String.join(", ", columns)); // Joining column names with commas
        queryBuilder.append(" FROM ").append(tableName);
        if (whereClause != null && !whereClause.isEmpty()) {
            queryBuilder.append(" WHERE ").append(whereClause); // Adding a WHERE clause if present
        }

        // Executing the query and handling results
        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(queryBuilder.toString())) {
            int i = 1;
            for (Object arg : whereArgs) {
                pst.setObject(i++, arg); // Setting the WHERE clause arguments
            }

           // ResultSet rs = pst.executeQuery(); // Executing the query
            // Process the result set here
            JOptionPane.showMessageDialog(null, "Records Retrieved");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading records: " + e.getMessage());
        }
    }

    // Update
    /**
     * Updates records in a specified table.
     * @param tableName The name of the table where records are updated.
     * @param updatedValues A map of column names and their new values for updating.
     * @param whereClause The WHERE clause to specify which records to update.
     * @param whereArgs The arguments for the WHERE clause.
     */
    public void update(String tableName, Map<String, Object> updatedValues, String whereClause, Object[] whereArgs) {
        // Declare and initialize 'i' at the beginning
        int i;

        // First, check the current state of the records
        StringBuilder selectQuery = new StringBuilder("SELECT * FROM ");
        selectQuery.append(tableName).append(" WHERE ").append(whereClause);

        try (PreparedStatement selectPst = dbConnection.getConnection().prepareStatement(selectQuery.toString())) {
            i = 1;
            for (Object arg : whereArgs) {
                selectPst.setObject(i++, arg);
            }

            ResultSet rs = selectPst.executeQuery();
            if (!rs.isBeforeFirst()) { // Check if the ResultSet is empty
                JOptionPane.showMessageDialog(null, "No records found to update.");
                return;
            }

            boolean changesDetected = false;
            while (rs.next()) {
                for (Map.Entry<String, Object> entry : updatedValues.entrySet()) {
                    Object currentValue = rs.getObject(entry.getKey());
                    if (currentValue == null && entry.getValue() != null || currentValue != null && !currentValue.equals(entry.getValue())) {
                        changesDetected = true;
                        break;
                    }
                }
                if (changesDetected) {
                    break;
                }
            }

            if (!changesDetected) {
                JOptionPane.showMessageDialog(null, "No changes detected. Update skipped.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking record: " + e.getMessage());
            return;
        }

        // Building the UPDATE SQL query
        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(tableName).append(" SET ");

        i = 0;
        for (String column : updatedValues.keySet()) {
            queryBuilder.append(column).append("=?");
            if (i < updatedValues.size() - 1) {
                queryBuilder.append(", ");
            }
            i++;
        }

        queryBuilder.append(" WHERE ").append(whereClause);

        // Executing the update
        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(queryBuilder.toString())) {
            i = 1;
            for (Object value : updatedValues.values()) {
                pst.setObject(i++, value);
            }

            for (Object arg : whereArgs) {
                pst.setObject(i++, arg);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Record Updated");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating record: " + e.getMessage());
        }
    }



    // Delete
    /**
     * Deletes records from a specified table based on a condition.
     * @param tableName The name of the table from which records will be deleted.
     * @param whereClause The WHERE clause to specify which records to delete.
     * @param whereArgs The arguments for the WHERE clause.
     */
    public void delete(String tableName, String whereClause, Object[] whereArgs) {
        // Building the DELETE SQL query
        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;

        // Executing the query
        try (PreparedStatement pst = dbConnection.getConnection().prepareStatement(query)) {
            int i = 1;
            for (Object arg : whereArgs) {
                pst.setObject(i++, arg); // Setting the WHERE clause arguments
            }
            pst.executeUpdate(); // Executing the delete
            JOptionPane.showMessageDialog(null, "Record Deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting record: " + e.getMessage());
        }
    }

}
