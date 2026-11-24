/**
 * A show-type attraction.
 * Runs on schedule whether or not an audience is waiting.
 * Not inspectable.
 */
public class Show extends Attraction {

    /**
     * Constructs a Show attraction.
     * @param id unique show ID
     * @param name show title
     * @param capacityPerCycle audience capacity per performance
     */
    public Show(String id, String name, int capacityPerCycle) {
        super(id, name, capacityPerCycle);
    }

    /**
     * Show rule: runs as long as an operator/performer is assigned.
     * @return true if staff is present
     */
    @Override
    public boolean canRunCycle() {
        return operator != null;
    }

    @Override
    public void runCycle() {
        if (operator == null) {
            System.out.println(name + " cannot run: no performer assigned.");
            return;
        }
        cycleCount++;
        int served = 0;
        for (int i = 0; i < capacityPerCycle && !waitingLine.isEmpty(); i++) {
            Visitor v = removeNextVisitor();
            addVisitorToHistory(v);
            served++;
        }
        System.out.println(name + " performance " + cycleCount + " started. Audience size: " + served);
    }

    @Override
    public String toString() {
        return "Show [ID=" + id + ", Name=" + name + ", Capacity=" + capacityPerCycle + "]";
    }
}
