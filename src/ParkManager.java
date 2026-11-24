import java.util.*;

/**
 * Central manager for all attractions in the theme park.
 * Provides fast lookup by ID and park-wide visitor statistics.
 */
public class ParkManager {
    private Map<String, Attraction> attractions; // key = attraction ID

    /**
     * Creates an empty park manager.
     */
    public ParkManager() {
        this.attractions = new HashMap<>();
    }

    /**
     * Registers a new attraction in the park.
     * @param attraction the attraction to add
     */
    public void addAttraction(Attraction attraction) {
        attractions.put(attraction.getId(), attraction);
        System.out.println("Registered attraction: " + attraction.getName() + " (ID: " + attraction.getId() + ")");
    }

    /**
     * Looks up an attraction directly by its unique ID.
     * O(1) lookup via HashMap.
     * @param id attraction identifier
     * @return matching attraction, or null if not found
     */
    public Attraction getAttractionById(String id) {
        Attraction a = attractions.get(id);
        if (a != null) {
            System.out.println("Found attraction for ID " + id + ": " + a.getName());
        } else {
            System.out.println("No attraction found with ID " + id);
        }
        return a;
    }

    /**
     * Prints attendance and cycle statistics for every attraction.
     */
    public void printAllAttractionStats() {
        System.out.println("\n=== Park Attendance Statistics ===");
        for (Attraction a : attractions.values()) {
            System.out.println(a.getName() + ": " + a.getHistorySize() + " visits, " + a.getCycleCount() + " cycles");
        }
    }

    /**
     * Counts how many distinct individual visitors have been served across the entire park.
     * Uses a Set to automatically deduplicate visitors by ID.
     * @return count of unique visitors park-wide
     */
    public int getDistinctVisitorCount() {
        Set<Visitor> allVisitors = new HashSet<>();
        for (Attraction a : attractions.values()) {
            allVisitors.addAll(a.getVisitHistory());
        }
        return allVisitors.size();
    }

    public Map<String, Attraction> getAllAttractions() {
        return new HashMap<>(attractions);
    }
}
