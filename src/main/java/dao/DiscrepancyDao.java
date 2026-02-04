package dao;

import model.InventoryDiscrepancy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiscrepancyDao {

    public InventoryDiscrepancy findActive(
            Connection conn, String sku, String warehouseId) throws Exception {

        String sql = """
            SELECT * FROM inventory_discrepancy
            WHERE sku = ? AND warehouse_id = ? AND active_flag = true
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setString(2, warehouseId);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            InventoryDiscrepancy d = new InventoryDiscrepancy();
            d.setId(rs.getLong("id"));
            d.setStatus(rs.getString("status"));
            d.setExpectedQuantity(rs.getInt("expected_quantity"));
            d.setActualQuantity(rs.getInt("actual_quantity"));
            return d;
        }
    }
}
