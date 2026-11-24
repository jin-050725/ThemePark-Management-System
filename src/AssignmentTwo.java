import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main demonstration class for PROG2004 Assignment 2.
 * Runs a scripted simulation covering all 8 parts of the specification.
 */
public class AssignmentTwo {
    public static void main(String[] args) {
        System.out.println("===== PROG2004 Assignment 2 - Theme Park Management System =====");

        // ==================================================
        // Part 1: Modelling the park's people
        // ==================================================
        System.out.println("\n========== Part 1: Park Personnel ==========");
        Staff s1 = new Staff(101, "Alice", 32, "Ride Operator");
        Staff s2 = new Staff(102, "Bob", 28, "Maintenance Technician");
        Staff s3 = new Staff(103, "Charlie", 35, "Performer");

        Visitor v1 = new Visitor(201, "David", 25, "Adult");
        Visitor v2 = new Visitor(202, "Ella", 12, "Child");
        Visitor v3 = new Visitor(203, "Frank", 30, "Adult");
        Visitor v4 = new Visitor(204, "Grace", 8, "Child");

        System.out.println("Staff members:");
        System.out.println(s1);
        System.out.println(s2);

        System.out.println("\nVisitors (unsorted):");
        List<Visitor> visitors = new ArrayList<>();
        visitors.add(v1); visitors.add(v2); visitors.add(v3); visitors.add(v4);
        for (Visitor v : visitors) System.out.println(v);

        Collections.sort(visitors);
        System.out.println("\nVisitors (sorted by age):");
        for (Visitor v : visitors) System.out.println(v);

        // ==================================================
        // Part 2: Attractions and inspectable facilities
        // ==================================================
        System.out.println("\n========== Part 2: Attractions & Inspections ==========");
        Ride rollerCoaster = new Ride("R001", "Thunder Coaster", 2);
        Show magicShow = new Show("S001", "Magic Wonderland", 10);
        Toilet toiletA = new Toilet("T001", "Main Entrance");

        rollerCoaster.assignOperator(s1);
        magicShow.assignOperator(s3);

        // staff inspects a ride
        s2.performInspection(rollerCoaster, "Passed - all safety systems nominal");
        System.out.println("Ride under inspection? " + rollerCoaster.isUnderInspection());
        rollerCoaster.endInspection();

        // staff inspects a toilet
        s2.performInspection(toiletA, "Passed - clean and fully stocked");
        toiletA.endInspection();

        // ==================================================
        // Part 3: The waiting line
        // ==================================================
        System.out.println("\n========== Part 3: Waiting Line ==========");
        rollerCoaster.addVisitorToLine(v1);
        rollerCoaster.addVisitorToLine(v2);
        rollerCoaster.addVisitorToLine(v3);
        rollerCoaster.displayWaitingLine();

        rollerCoaster.removeNextVisitor();
        rollerCoaster.displayWaitingLine();

        // ==================================================
        // Part 4: Visit history
        // ==================================================
        System.out.println("\n========== Part 4: Visit History ==========");
        rollerCoaster.addVisitorToHistory(v1);
        rollerCoaster.addVisitorToHistory(v2);
        rollerCoaster.addVisitorToHistory(v3);
        rollerCoaster.addVisitorToHistory(v4);

        System.out.println("Total entries in history: " + rollerCoaster.getHistorySize());
        System.out.println("Is David in history? " + rollerCoaster.isVisitorInHistory(v1));

        rollerCoaster.displayHistoryByAge();
        rollerCoaster.displayHistoryByNameAndId();

        // ==================================================
        // Part 5: Operating an attraction
        // ==================================================
        System.out.println("\n========== Part 5: Attraction Operation ==========");
        Ride testRide = new Ride("R002", "Merry Go Round", 2);
        testRide.assignOperator(s1);
        testRide.addVisitorToLine(v1);
        testRide.addVisitorToLine(v2);
        testRide.addVisitorToLine(v3);

        System.out.println("\nBefore cycles - cycles: " + testRide.getCycleCount()
                + ", waiting: " + testRide.getWaitingLineSize()
                + ", history: " + testRide.getHistorySize());

        testRide.runCycle();
        testRide.runCycle();
        testRide.runCycle(); // should refuse - empty line

        System.out.println("\nAfter cycles - cycles: " + testRide.getCycleCount()
                + ", waiting: " + testRide.getWaitingLineSize()
                + ", history: " + testRide.getHistorySize());

        // test: inspection blocks operation
        System.out.println("\n--- Test: ride closed for inspection ---");
        s2.performInspection(testRide, "Under scheduled maintenance");
        testRide.runCycle();
        testRide.endInspection();

        // test: show runs even with empty audience
        System.out.println("\n--- Test: show with empty audience ---");
        Show testShow = new Show("S002", "Puppet Show", 5);
        testShow.assignOperator(s3);
        testShow.runCycle(); // runs regardless
        System.out.println("Show cycles run: " + testShow.getCycleCount());

        // ==================================================
        // Part 6: Managing the park
        // ==================================================
        System.out.println("\n========== Part 6: Park Management ==========");
        ParkManager park = new ParkManager();
        park.addAttraction(rollerCoaster);
        park.addAttraction(magicShow);
        park.addAttraction(testRide);

        park.getAttractionById("R001");
        park.printAllAttractionStats();
        System.out.println("Total distinct visitors park-wide: " + park.getDistinctVisitorCount());

        // ==================================================
        // Part 7: Backup and restore
        // ==================================================
        System.out.println("\n========== Part 7: Backup & Restore ==========");
        String savePath = "park_data.txt";
        ParkFileManager.saveParkToFile(park, savePath);

        // load into a fresh park instance and verify
        ParkManager loadedPark = ParkFileManager.loadParkFromFile(savePath);
        loadedPark.printAllAttractionStats();
        System.out.println("Loaded park distinct visitors: " + loadedPark.getDistinctVisitorCount());

        // test error handling: missing file
        System.out.println("\n--- Test: loading non-existent file ---");
        ParkFileManager.loadParkFromFile("nonexistent_file.txt");

        // ==================================================
        // Part 8: Concurrent park operation
        // ==================================================
        System.out.println("\n========== Part 8: Concurrent Operation ==========");
        AtomicInteger totalParkVisits = new AtomicInteger(0);

        Ride concurrentRide = new Ride("R003", "Bumper Cars", 3);
        Show concurrentShow = new Show("S003", "Parade Spectacular", 20);
        concurrentRide.assignOperator(s1);
        concurrentShow.assignOperator(s3);

        // populate waiting lines
        for (int i = 0; i < 10; i++) {
            concurrentRide.addVisitorToLine(new Visitor(300 + i, "Visitor" + i, 20 + i, "Adult"));
            concurrentShow.addVisitorToLine(new Visitor(400 + i, "Guest" + i, 15 + i, "Child"));
        }

        // define concurrent tasks
        Runnable rideTask = () -> {
            for (int i = 0; i < 4; i++) {
                int before = concurrentRide.getHistorySize();
                concurrentRide.runCycle();
                int served = concurrentRide.getHistorySize() - before;
                totalParkVisits.addAndGet(served);
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };

        Runnable showTask = () -> {
            for (int i = 0; i < 3; i++) {
                int before = concurrentShow.getHistorySize();
                concurrentShow.runCycle();
                int served = concurrentShow.getHistorySize() - before;
                totalParkVisits.addAndGet(served);
                try { Thread.sleep(150); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        };

        // execute with thread pool
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(rideTask);
        pool.submit(showTask);
        pool.shutdown();

        try {
            pool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nAll concurrent attractions have finished.");
        System.out.println("Final park-wide total visits: " + totalParkVisits.get());

        System.out.println("\n===== End of Simulation =====");
    }
}
