import java.util.*;

/**
 * Abstract base class for all park attractions.
 * Contains shared state and behaviour for waiting lines, visit history, and cycle logic.
 * Cannot be instantiated directly - subclasses define specific run rules.
 */
public abstract class Attraction {
    protected String id;
    protected String name;
    protected Staff operator;
    protected int capacityPerCycle; // maximum visitors served per cycle
    protected int cycleCount;       // total cycles run today
    protected Queue<Visitor> waitingLine;   // FIFO queue for waiting visitors
    protected List<Visitor> visitHistory;   // record of all visitors served

    /**
     * Constructs a base attraction.
     * @param id unique attraction identifier
     * @param name display name
     * @param capacityPerCycle maximum guests per operating cycle
     */
    public Attraction(String id, String name, int capacityPerCycle) {
        this.id = id;
        this.name = name;
        this.capacityPerCycle = capacityPerCycle;
        this.cycleCount = 0;
        this.waitingLine = new LinkedList<>();
        this.visitHistory = new ArrayList<>();
    }

    /**
     * Abstract rule: determines whether a cycle can run right now.
     * Implemented differently by each subclass.
     * @return true if the cycle may proceed
     */
    public abstract boolean canRunCycle();

    // ===== Part 3: Waiting Line Methods =====

    /**
     * Adds a visitor to the end of the waiting line.
     * @param visitor the visitor joining the queue
     */
    public void addVisitorToLine(Visitor visitor) {
        waitingLine.offer(visitor);
        System.out.println(visitor.getName() + " joined the waiting line for " + name);
    }

    /**
     * Removes and returns the next visitor from the front of the line.
     * @return the next visitor to be served, or null if line is empty
     */
    public Visitor removeNextVisitor() {
        Visitor next = waitingLine.poll();
        if (next != null) {
            System.out.println(next.getName() + " is entering " + name + " from the waiting line");
        }
        return next;
    }

    /**
     * Prints all visitors currently waiting, in arrival order.
     */
    public void displayWaitingLine() {
        System.out.println("=== Waiting Line: " + name + " (" + waitingLine.size() + " people) ===");
        for (Visitor v : waitingLine) {
            System.out.println(v);
        }
    }

    // ===== Part 4: Visit History Methods =====

    /**
     * Records a visitor in the served history.
     * @param visitor the visitor who was served
     */
    public void addVisitorToHistory(Visitor visitor) {
        visitHistory.add(visitor);
        System.out.println(visitor.getName() + " added to " + name + " visit history");
    }

    /**
     * Checks whether a visitor appears in the history.
     * @param visitor visitor to look up
     * @return true if visitor has been served before
     */
    public boolean isVisitorInHistory(Visitor visitor) {
        return visitHistory.contains(visitor);
    }

    /**
     * Returns the total number of visits recorded.
     * @return count of entries in history
     */
    public int getHistorySize() {
        return visitHistory.size();
    }

    /**
     * Displays history sorted by visitor age (natural ordering).
     */
    public void displayHistoryByAge() {
        List<Visitor> sorted = new ArrayList<>(visitHistory);
        Collections.sort(sorted);
        System.out.println("=== " + name + " History (sorted by age) ===");
        for (Visitor v : sorted) {
            System.out.println(v);
        }
    }

    /**
     * Displays history sorted by name, then by ID as tiebreaker.
     * Uses a separate Comparator independent of natural ordering.
     */
    public void displayHistoryByNameAndId() {
        List<Visitor> sorted = new ArrayList<>(visitHistory);
        sorted.sort(Comparator.comparing(Visitor::getName).thenComparingInt(Visitor::getId));
        System.out.println("=== " + name + " History (sorted by name + ID) ===");
        for (Visitor v : sorted) {
            System.out.println(v);
        }
    }

    // ===== Part 5: Cycle Operation =====

    /**
     * Runs one full operating cycle: moves visitors from line into history.
     * Subclasses override canRunCycle() to enforce their own rules.
     */
    public void runCycle() {
        if (!canRunCycle()) {
            System.out.println(name + " cannot run a cycle at this time.");
            if (operator == null) System.out.println("  Reason: No operator assigned.");
            return;
        }

        int served = 0;
        for (int i = 0; i < capacityPerCycle && !waitingLine.isEmpty(); i++) {
            Visitor v = removeNextVisitor();
            addVisitorToHistory(v);
            served++;
        }
        cycleCount++;
        System.out.println(name + " completed cycle " + cycleCount + " - served " + served + " visitors.");
    }

    /**
     * Assigns a staff member as the attraction operator.
     * @param staff the employee to assign
     */
    public void assignOperator(Staff staff) {
        this.operator = staff;
        System.out.println(staff.getName() + " assigned as operator of " + name);
    }

    /**
     * Removes the currently assigned operator.
     */
    public void removeOperator() {
        System.out.println(operator.getName() + " removed from " + name);
        this.operator = null;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getCycleCount() { return cycleCount; }
    public int getWaitingLineSize() { return waitingLine.size(); }
    public Staff getOperator() { return operator; }
    public Queue<Visitor> getWaitingLine() { return new LinkedList<>(waitingLine); }
    public List<Visitor> getVisitHistory() { return new ArrayList<>(visitHistory); }
    public int getCapacityPerCycle() { return capacityPerCycle; }
}
