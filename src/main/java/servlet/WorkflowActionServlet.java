package servlet;

import service.WorkflowService;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class WorkflowActionServlet extends HttpServlet {

    private final WorkflowService workflowService = new WorkflowService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long id = Long.parseLong(req.getParameter("id"));
        String newStatus = req.getParameter("action");

        workflowService.changeStatus(
                id,
                newStatus,
                "admin_user",
                "Manual admin transition"
        );

        resp.sendRedirect("discrepancies");
    }
}
