/**
 * Common interface for all facilities that can be inspected by staff.
 * Defines a contract shared by unrelated types (Rides, Toilets, etc.).
 */
public interface Inspectable {
    /**
     * Starts an inspection and closes the facility temporarily.
     * @param result recorded outcome of the inspection
     */
    void startInspection(String result);

    /**
     * Ends the inspection and reopens the facility.
     */
    void endInspection();

    /**
     * Checks whether the facility is currently closed for inspection.
     * @return true if inspection is in progress
     */
    boolean isUnderInspection();

    /**
     * Returns the most recent inspection result.
     * @return inspection outcome string
     */
    String getInspectionResult();
}
