package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DiscrepancyHistoryDao {

    public void insert(
            Connection conn,
            long discrepancyId,
            String previousStatus,
            String newStatus,
            String changedBy,
            String changeReason) throws Exception {

        String sql =
                "INSERT INTO discrepancy_state_history " +
                        "(discrepancy_id, previous_status, new_status, " +
                        "changed_by, change_reason, changed_at) " +
                        "VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, discrepancyId);
            ps.setString(2, previousStatus);
            ps.setString(3, newStatus);
            ps.setString(4, changedBy);
            ps.setString(5, changeReason);

            ps.executeUpdate();
        }
    }
}
