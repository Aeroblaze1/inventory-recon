package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscrepancyHistoryDao {
    public List<Map<String, Object>> findByDiscrepancyId(
            Connection conn,
            long discrepancyId) throws Exception {

        String sql =
                "SELECT previous_status, " +
                        "       new_status, " +
                        "       changed_by, " +
                        "       change_reason, " +
                        "       changed_at " +
                        "FROM discrepancy_state_history " +
                        "WHERE discrepancy_id = ? " +
                        "ORDER BY changed_at ASC";

        List<Map<String, Object>> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, discrepancyId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("previous_status", rs.getString("previous_status"));
                row.put("new_status", rs.getString("new_status"));
                row.put("changed_by", rs.getString("changed_by"));
                row.put("change_reason", rs.getString("change_reason"));
                row.put("changed_at", rs.getTimestamp("changed_at"));
                list.add(row);
            }
        }

        return list;
    }

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
