package servlet;

import dao.AuditLogDao;
import dao.DiscrepancyDao;
import dao.DiscrepancyHistoryDao;
import model.InventoryDiscrepancy;
import util.DbConnectionFactory;
import util.TransactionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class DiscrepancyDetailServlet extends HttpServlet {

    private final DiscrepancyDao discrepancyDao = new DiscrepancyDao();
    private final DiscrepancyHistoryDao historyDao = new DiscrepancyHistoryDao();
    private final AuditLogDao auditLogDao = new AuditLogDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));
        Connection conn = null;

        try {
            conn = DbConnectionFactory.getConnection();

            InventoryDiscrepancy d =
                    discrepancyDao.findById(conn, id);

            List<Map<String, Object>> history =
                    historyDao.findByDiscrepancyId(conn, id);

            List<Map<String, Object>> audit =
                    auditLogDao.findByEntity(conn, "DISCREPANCY", id);

            req.setAttribute("discrepancy", d);
            req.setAttribute("history", history);
            req.setAttribute("audit", audit);

            TransactionManager.commit(conn);

            RequestDispatcher dispatcher =
                    req.getRequestDispatcher("/WEB-INF/views/discrepancy-detail.jsp");
            dispatcher.forward(req, resp);

        } catch (Exception e) {
            TransactionManager.rollback(conn);
            throw new ServletException(e);
        } finally {
            TransactionManager.close(conn);
        }
    }
}
