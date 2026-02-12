package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditLogDao {

    public List<Map<String, Object>> findByEntity(
            Connection conn,
            String entityType,
            long entityId) throws Exception {

        String sql =
                "SELECT action, " +
                        "       previous_value, " +
                        "       new_value, " +
                        "       performed_by, " +
                        "       performed_at " +
                        "FROM audit_log " +
                        "WHERE entity_type = ? " +
                        "  AND entity_id = ? " +
                        "ORDER BY performed_at ASC";

        List<Map<String, Object>> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entityType);
            ps.setLong(2, entityId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("action", rs.getString("action"));
                row.put("previous_value", rs.getString("previous_value"));
                row.put("new_value", rs.getString("new_value"));
                row.put("performed_by", rs.getString("performed_by"));
                row.put("performed_at", rs.getTimestamp("performed_at"));
                list.add(row);
            }
        }

        return list;
    }


    public void log(
            Connection conn,
            String entityType,
            long entityId,
            String action,
            String previousValue,
            String newValue,
            String performedBy) throws Exception {

        String sql =
                "INSERT INTO audit_log " +
                        "(entity_type, entity_id, action, previous_value, new_value, " +
                        "performed_by, performed_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entityType);
            ps.setLong(2, entityId);
            ps.setString(3, action);
            ps.setString(4, previousValue);
            ps.setString(5, newValue);
            ps.setString(6, performedBy);

            ps.executeUpdate();
        }
    }
}
