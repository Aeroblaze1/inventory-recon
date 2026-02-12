package dao;

import model.InventoryDiscrepancy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DiscrepancyDao {

    public InventoryDiscrepancy findActive(
            Connection conn, String sku, String warehouseId) throws Exception {

        String sql =
                "SELECT id, sku, warehouse_id, expected_quantity, actual_quantity, " +
                        "difference, status, active_flag " +
                        "FROM inventory_discrepancy " +
                        "WHERE sku = ? AND warehouse_id = ? AND active_flag = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setString(2, warehouseId);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            InventoryDiscrepancy d = new InventoryDiscrepancy();
            d.setId(rs.getLong("id"));
            d.setSku(rs.getString("sku"));
            d.setWarehouseId(rs.getString("warehouse_id"));
            d.setExpectedQuantity(rs.getInt("expected_quantity"));
            d.setActualQuantity(rs.getInt("actual_quantity"));
            d.setDifference(rs.getInt("difference"));
            d.setStatus(rs.getString("status"));
            d.setActive(rs.getBoolean("active_flag"));

            return d;
        }
    }

    public long insert(
            Connection conn, InventoryDiscrepancy d) throws Exception {

        String sql =
                "INSERT INTO inventory_discrepancy " +
                        "(sku, warehouse_id, expected_quantity, actual_quantity, difference, " +
                        "status, detected_at, active_flag) " +
                        "VALUES (?, ?, ?, ?, ?, ?, NOW(), true)";

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getSku());
            ps.setString(2, d.getWarehouseId());
            ps.setInt(3, d.getExpectedQuantity());
            ps.setInt(4, d.getActualQuantity());
            ps.setInt(5, d.getDifference());
            ps.setString(6, d.getStatus());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (!keys.next()) {
                throw new RuntimeException("Failed to get generated discrepancy id");
            }

            return keys.getLong(1);
        }
    }

    public void updateQuantities(
            Connection conn,
            long discrepancyId,
            int expectedQuantity,
            int actualQuantity,
            int difference) throws Exception {

        String sql =
                "UPDATE inventory_discrepancy " +
                        "SET expected_quantity = ?, actual_quantity = ?, difference = ? " +
                        "WHERE id = ? AND active_flag = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, expectedQuantity);
            ps.setInt(2, actualQuantity);
            ps.setInt(3, difference);
            ps.setLong(4, discrepancyId);

            ps.executeUpdate();
        }
    }

    public InventoryDiscrepancy findById(Connection conn, long id) throws Exception {

        String sql = "SELECT * FROM inventory_discrepancy WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            return mapRow(rs);
        }
    }

    public void updateStatus(Connection conn, long id, String status) throws Exception {

        String sql =
                "UPDATE inventory_discrepancy " +
                        "SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void deactivate(Connection conn, long id) throws Exception {

        String sql =
                "UPDATE inventory_discrepancy " +
                        "SET active_flag = false WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    public List<InventoryDiscrepancy> findAllActive(Connection conn) throws Exception {

        String sql = "SELECT * FROM inventory_discrepancy WHERE active_flag = true";

        List<InventoryDiscrepancy> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                InventoryDiscrepancy d = new InventoryDiscrepancy();
                d.setId(rs.getLong("id"));
                d.setSku(rs.getString("sku"));
                d.setWarehouseId(rs.getString("warehouse_id"));
                d.setExpectedQuantity(rs.getInt("expected_quantity"));
                d.setActualQuantity(rs.getInt("actual_quantity"));
                d.setDifference(rs.getInt("difference"));
                d.setStatus(rs.getString("status"));
                d.setActive(rs.getBoolean("active_flag"));
                list.add(d);
            }
        }

        return list;
    }


    //privatre mapper helper
    private InventoryDiscrepancy mapRow(ResultSet rs) throws Exception {

        InventoryDiscrepancy d = new InventoryDiscrepancy();
        d.setId(rs.getLong("id"));
        d.setSku(rs.getString("sku"));
        d.setWarehouseId(rs.getString("warehouse_id"));
        d.setExpectedQuantity(rs.getInt("expected_quantity"));
        d.setActualQuantity(rs.getInt("actual_quantity"));
        d.setDifference(rs.getInt("difference"));
        d.setStatus(rs.getString("status"));
        d.setActive(rs.getBoolean("active_flag"));

        return d;
    }

}
