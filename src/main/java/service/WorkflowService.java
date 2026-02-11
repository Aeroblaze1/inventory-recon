package service;

import dao.AuditLogDao;
import dao.DiscrepancyDao;
import dao.DiscrepancyHistoryDao;
import model.InventoryDiscrepancy;
import util.DbConnectionFactory;
import util.TransactionManager;

import java.sql.Connection;
import java.util.Map;
import java.util.Set;

public class WorkflowService {

    private final DiscrepancyDao discrepancyDao = new DiscrepancyDao();
    private final DiscrepancyHistoryDao historyDao = new DiscrepancyHistoryDao();
    private final AuditLogDao auditLogDao = new AuditLogDao();

    private static final Map<String, Set<String>> VALID_TRANSITIONS = Map.of(
            "OPEN", Set.of("IN_REVIEW"),
            "IN_REVIEW", Set.of("RESOLVED"),
            "RESOLVED", Set.of("CLOSED"),
            "CLOSED", Set.of()
    );

    public void changeStatus(long discrepancyId,
                             String newStatus,
                             String operator,
                             String reason) {

        Connection conn = null;

        try {
            conn = DbConnectionFactory.getConnection();

            InventoryDiscrepancy existing =
                    discrepancyDao.findById(conn, discrepancyId);

            if (existing == null) {
                throw new RuntimeException("Discrepancy not found");
            }

            String currentStatus = existing.getStatus();

            if (!isValidTransition(currentStatus, newStatus)) {
                throw new RuntimeException(
                        "Invalid transition: " + currentStatus + " → " + newStatus
                );
            }

            discrepancyDao.updateStatus(conn, discrepancyId, newStatus);

            if ("CLOSED".equals(newStatus)) {
                discrepancyDao.deactivate(conn, discrepancyId);
            }

            historyDao.insert(
                    conn,
                    discrepancyId,
                    currentStatus,
                    newStatus,
                    operator,
                    reason
            );

            auditLogDao.log(
                    conn,
                    "DISCREPANCY",
                    discrepancyId,
                    "STATE_CHANGE",
                    currentStatus,
                    newStatus,
                    operator
            );

            TransactionManager.commit(conn);

        } catch (Exception e) {
            TransactionManager.rollback(conn);
            throw new RuntimeException("Workflow transition failed", e);
        } finally {
            TransactionManager.close(conn);
        }
    }

    private boolean isValidTransition(String from, String to) {
        return VALID_TRANSITIONS.containsKey(from)
                && VALID_TRANSITIONS.get(from).contains(to);
    }
}
