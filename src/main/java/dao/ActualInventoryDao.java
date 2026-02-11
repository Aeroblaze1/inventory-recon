package dao;

import model.ActualInventorySnapshot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActualInventoryDao {

    public ActualInventorySnapshot findLatest(
            Connection conn, String sku, String warehouseId) throws Exception {

        String sql =
                "SELECT id, sku, warehouse_id, quantity, snapshot_timestamp " +
                        "FROM actual_inventory_snapshot " +
                        "WHERE sku = ? AND warehouse_id = ? " +
                        "ORDER BY snapshot_timestamp DESC " +
                        "LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setString(2, warehouseId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            ActualInventorySnapshot a = new ActualInventorySnapshot();
            a.setId(rs.getLong("id"));
            a.setSku(rs.getString("sku"));
            a.setWarehouseId(rs.getString("warehouse_id"));
            a.setQuantity(rs.getInt("quantity"));
            a.setSnapshotTimestamp(rs.getTimestamp("snapshot_timestamp"));

            return a;
        }
    }
}
