package service;

import dao.*;
import model.ActualInventorySnapshot;
import model.ExpectedInventory;
import model.InventoryDiscrepancy;
import util.DbConnectionFactory;
import util.TransactionManager;

import java.sql.Connection;

public class ReconciliationService {

    private final ExpectedInventoryDao expectedInventoryDao = new ExpectedInventoryDao();
    private final ActualInventoryDao actualInventoryDao = new ActualInventoryDao();
    private final DiscrepancyDao discrepancyDao = new DiscrepancyDao();
    private final DiscrepancyHistoryDao historyDao = new DiscrepancyHistoryDao();
    private final AuditLogDao auditLogDao = new AuditLogDao();

    public void reconcile(String sku, String warehouseId, String operator) {

        Connection conn = null;

        try {
            conn = DbConnectionFactory.getConnection();

            ExpectedInventory expected =
                    expectedInventoryDao.findLatest(conn, sku, warehouseId);

            ActualInventorySnapshot actual =
                    actualInventoryDao.findLatest(conn, sku, warehouseId);

            if (expected == null || actual == null) {
                TransactionManager.commit(conn);
                return;
            }

            int expectedQty = expected.getQuantity();
            int actualQty = actual.getQuantity();

            if (expectedQty == actualQty) {
                TransactionManager.commit(conn);
                return;
            }

            int difference = expectedQty - actualQty;

            InventoryDiscrepancy existing =
                    discrepancyDao.findActive(conn, sku, warehouseId);

            if (existing == null) {
                InventoryDiscrepancy d = new InventoryDiscrepancy();
                d.setSku(sku);
                d.setWarehouseId(warehouseId);
                d.setExpectedQuantity(expectedQty);
                d.setActualQuantity(actualQty);
                d.setDifference(difference);
                d.setStatus("OPEN");

                long discrepancyId = discrepancyDao.insert(conn, d);

                historyDao.insert(
                        conn,
                        discrepancyId,
                        null,
                        "OPEN",
                        operator,
                        "Detected during reconciliation"
                );

                auditLogDao.log(
                        conn,
                        "DISCREPANCY",
                        discrepancyId,
                        "CREATE",
                        null,
                        buildValue(expectedQty, actualQty),
                        operator
                );

            } else {
                if (existing.getExpectedQuantity() != expectedQty ||
                        existing.getActualQuantity() != actualQty) {

                    String before = buildValue(
                            existing.getExpectedQuantity(),
                            existing.getActualQuantity()
                    );

                    discrepancyDao.updateQuantities(
                            conn,
                            existing.getId(),
                            expectedQty,
                            actualQty,
                            difference
                    );

                    String after = buildValue(expectedQty, actualQty);

                    auditLogDao.log(
                            conn,
                            "DISCREPANCY",
                            existing.getId(),
                            "UPDATE",
                            before,
                            after,
                            operator
                    );
                }
            }

            TransactionManager.commit(conn);

        } catch (Exception e) {
            TransactionManager.rollback(conn);
            throw new RuntimeException("Reconciliation failed", e);
        } finally {
            TransactionManager.close(conn);
        }
    }

    private String buildValue(int expectedQty, int actualQty) {
        return "expected=" + expectedQty + ",actual=" + actualQty;
    }
}
