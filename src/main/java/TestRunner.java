import service.ReconciliationService;
import service.WorkflowService;

public class TestRunner {

    public static void main(String[] args) {

        WorkflowService wf = new WorkflowService();

        wf.changeStatus(1, "IN_REVIEW", "ops_user", "Investigating variance");

        System.out.println("Status changed.");
    }
}
