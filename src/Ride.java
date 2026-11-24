/**
 * A ride-type attraction.
 * Runs only when there are waiting visitors, an operator is present, and it is not under inspection.
 * Implements Inspectable for safety checks.
 */
public class Ride extends Attraction implements Inspectable {
    private boolean closedForInspection;
    private String inspectionResult;

    /**
     * Constructs a Ride attraction.
     * @param id unique ride ID
     * @param name ride display name
     * @param capacityPerCycle riders per dispatch
     */
    public Ride(String id, String name, int capacityPerCycle) {
        super(id, name, capacityPerCycle);
        this.closedForInspection = false;
        this.inspectionResult = "Not inspected yet";
    }

    /**
     * Ride rule: may run only if operator exists, line is not empty, and not closed.
     * @return true if all conditions are met
     */
    @Override
    public boolean canRunCycle() {
        return operator != null && !waitingLine.isEmpty() && !closedForInspection;
    }

    @Override
    public void runCycle() {
        if (closedForInspection) {
            System.out.println(name + " cannot run: currently closed for safety inspection.");
            return;
        }
        if (operator == null) {
            System.out.println(name + " cannot run: no operator assigned.");
            return;
        }
        if (waitingLine.isEmpty()) {
            System.out.println(name + " cannot run: waiting line is empty.");
            return;
        }
        super.runCycle();
    }

    // ===== Inspectable Interface Implementation =====
    @Override
    public void startInspection(String result) {
        this.closedForInspection = true;
        this.inspectionResult = result;
        System.out.println("Ride " + name + " closed for safety inspection.");
    }

    @Override
    public void endInspection() {
        this.closedForInspection = false;
        System.out.println("Ride " + name + " inspection finished - reopened to public.");
    }

    @Override
    public boolean isUnderInspection() {
        return closedForInspection;
    }

    @Override
    public String getInspectionResult() {
        return inspectionResult;
    }

    @Override
    public String toString() {
        return "Ride [ID=" + id + ", Name=" + name + ", Capacity=" + capacityPerCycle + "]";
    }
}
