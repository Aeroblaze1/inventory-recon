package dao;

import model.ExpectedInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpectedInventoryDao {

    public ExpectedInventory findLatest(
            Connection conn, String sku, String warehouseId) throws Exception {

        String sql =
                "SELECT id, sku, warehouse_id, quantity, as_of_timestamp " +
                        "FROM expected_inventory " +
                        "WHERE sku = ? AND warehouse_id = ? " +
                        "ORDER BY as_of_timestamp DESC " +
                        "LIMIT 1";


        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setString(2, warehouseId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            ExpectedInventory e = new ExpectedInventory();
            e.setId(rs.getLong("id"));
            e.setSku(rs.getString("sku"));
            e.setWarehouseId(rs.getString("warehouse_id"));
            e.setQuantity(rs.getInt("quantity"));
            e.setAsOfTimestamp(rs.getTimestamp("as_of_timestamp"));

            return e;
        }
    }
}
