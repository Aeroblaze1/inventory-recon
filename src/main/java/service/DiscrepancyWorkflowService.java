package service;

import dao.AuditLogDao;
import dao.DiscrepancyDao;
import dao.DiscrepancyHistoryDao;
import util.DbConnectionFactory;
import util.TransactionManager;

import java.sql.Connection;

public class DiscrepancyWorkflowService {

    public void moveToInReview(
            long discrepancyId, String operator, String reason) {

        Connection conn = null;

        try {
            conn = DbConnectionFactory.getConnection();

            // 1. validate transition
            // 2. update inventory_discrepancy
            // 3. insert discrepancy_state_history
            // 4. insert audit_log

            TransactionManager.commit(conn);

        } catch (Exception e) {
            TransactionManager.rollback(conn);
            throw new RuntimeException("State transition failed", e);
        } finally {
            TransactionManager.close(conn);
        }
    }
}
