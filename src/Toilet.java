/**
 * Represents a toilet facility in the park.
 * Not an attraction, but implements Inspectable.
 */
public class Toilet implements Inspectable {
    private String id;
    private String location;
    private boolean underInspection;
    private String inspectionResult;

    /**
     * Constructs a Toilet facility.
     * @param id unique facility ID
     * @param location physical location in the park
     */
    public Toilet(String id, String location) {
        this.id = id;
        this.location = location;
        this.underInspection = false;
        this.inspectionResult = "Not inspected yet";
    }

    @Override
    public void startInspection(String result) {
        this.underInspection = true;
        this.inspectionResult = result;
        System.out.println("Toilet " + id + " is closed for inspection.");
    }

    @Override
    public void endInspection() {
        this.underInspection = false;
        System.out.println("Toilet " + id + " has been reopened.");
    }

    @Override
    public boolean isUnderInspection() {
        return underInspection;
    }

    @Override
    public String getInspectionResult() {
        return inspectionResult;
    }

    @Override
    public String toString() {
        return "Toilet [ID=" + id + ", Location=" + location + "]";
    }
}
