import service.ReconciliationService;

public class TestRunner {

    public static void main(String[] args) {

        ReconciliationService service = new ReconciliationService();

        service.reconcile("SKU-1", "WH-1", "system");

        System.out.println("Reconciliation finished.");
    }
}
