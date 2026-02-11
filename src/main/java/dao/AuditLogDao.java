package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AuditLogDao {

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
