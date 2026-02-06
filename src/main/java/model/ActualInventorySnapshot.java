package model;

import java.sql.Timestamp;

public class ActualInventorySnapshot {

    private long id;
    private String sku;
    private String warehouseId;
    private int quantity;
    private Timestamp snapshotTimestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getSnapshotTimestamp() {
        return snapshotTimestamp;
    }

    public void setSnapshotTimestamp(Timestamp snapshotTimestamp) {
        this.snapshotTimestamp = snapshotTimestamp;
    }
}
