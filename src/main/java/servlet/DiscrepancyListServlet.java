package servlet;

import dao.DiscrepancyDao;
import model.InventoryDiscrepancy;
import util.DbConnectionFactory;
import util.TransactionManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DiscrepancyListServlet extends HttpServlet {

    private final DiscrepancyDao dao = new DiscrepancyDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Connection conn = null;

        try {
            conn = DbConnectionFactory.getConnection();
            List<InventoryDiscrepancy> list = dao.findAllActive(conn);

            req.setAttribute("discrepancies", list);

            TransactionManager.commit(conn);

            RequestDispatcher dispatcher =
                    req.getRequestDispatcher("/WEB-INF/views/discrepancies.jsp");
            dispatcher.forward(req, resp);

        } catch (Exception e) {
            TransactionManager.rollback(conn);
            throw new ServletException(e);
        } finally {
            TransactionManager.close(conn);
        }
    }
}
