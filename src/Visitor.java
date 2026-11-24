/**
 * Represents a visitor to the theme park.
 * Implements Comparable to support sorting by age.
 */
public class Visitor implements Comparable<Visitor> {
    private int id;
    private String name;
    private int age;
    private String ticketType; // additional attribute: Adult / Child / Student

    /**
     * Constructs a Visitor with full details.
     * @param id unique numeric identifier
     * @param name full name of the visitor
     * @param age age of the visitor
     * @param ticketType type of ticket held
     */
    public Visitor(int id, String name, int age, String ticketType) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.ticketType = ticketType;
    }

    /**
     * Compares visitors by age in ascending order.
     * @param other the visitor to compare with
     * @return negative if this visitor is younger, positive if older
     */
    @Override
    public int compareTo(Visitor other) {
        return Integer.compare(this.age, other.age);
    }

    /**
     * Two visitors are considered equal if they share the same ID.
     * @param obj the object to compare
     * @return true if IDs match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Visitor visitor = (Visitor) obj;
        return id == visitor.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Returns a human-readable string of all visitor details.
     * @return formatted visitor information
     */
    @Override
    public String toString() {
        return "Visitor [ID=" + id + ", Name=" + name + ", Age=" + age + ", Ticket=" + ticketType + "]";
    }

    // Getter methods - controlled read access to encapsulated fields
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getTicketType() { return ticketType; }
}
