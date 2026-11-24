/**
 * Represents an employee working at the theme park.
 * Staff can operate attractions and perform inspections.
 */
public class Staff {
    private int id;
    private String name;
    private int age;
    private String position; // additional attribute: e.g. Ride Operator, Maintenance

    /**
     * Constructs a Staff member.
     * @param id unique staff ID
     * @param name staff member name
     * @param age staff member age
     * @param position job role
     */
    public Staff(int id, String name, int age, String position) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
    }

    /**
     * Performs an inspection on any inspectable facility.
     * Demonstrates polymorphism: works with Rides, Toilets, and future facilities.
     * @param item the facility to inspect
     * @param result outcome of the inspection
     */
    public void performInspection(Inspectable item, String result) {
        System.out.println("Staff " + name + " begins inspection on " + item);
        item.startInspection(result);
        System.out.println("Inspection completed. Result: " + result);
    }

    @Override
    public String toString() {
        return "Staff [ID=" + id + ", Name=" + name + ", Age=" + age + ", Position=" + position + "]";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public int getAge() { return age; }
}
