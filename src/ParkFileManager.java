import java.io.*;

/**
 * Utility class for saving and loading park state to/from a text file.
 * Handles missing files and corrupted data gracefully.
 */
public class ParkFileManager {

    /**
     * Writes the full park state to a text file using comma-separated records.
     * Uses try-with-resources to guarantee the file stream is closed.
     * @param park the park manager to save
     * @param filePath destination file path
     */
    public static void saveParkToFile(ParkManager park, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(park.getAllAttractions().size() + "\n");

            for (Attraction a : park.getAllAttractions().values()) {
                String type = a instanceof Ride ? "Ride" : "Show";
                String operatorLine = "null";
                if (a.getOperator() != null) {
                    Staff op = a.getOperator();
                    operatorLine = op.getId() + "," + op.getName() + "," + op.getAge() + "," + op.getPosition();
                }
                writer.write(type + "," + a.getId() + "," + a.getName() + "," + a.getCapacityPerCycle()
                        + "," + a.getCycleCount() + "," + operatorLine + "\n");

                // save waiting line
                writer.write(a.getWaitingLine().size() + "\n");
                for (Visitor v : a.getWaitingLine()) {
                    writer.write(v.getId() + "," + v.getName() + "," + v.getAge() + "," + v.getTicketType() + "\n");
                }

                // save visit history
                writer.write(a.getVisitHistory().size() + "\n");
                for (Visitor v : a.getVisitHistory()) {
                    writer.write(v.getId() + "," + v.getName() + "," + v.getAge() + "," + v.getTicketType() + "\n");
                }
            }
            System.out.println("Park data successfully saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save park data: " + e.getMessage());
        }
    }

    /**
     * Reconstructs a ParkManager from a saved text file.
     * Gracefully handles missing files and malformed lines.
     * @param filePath source file path
     * @return reconstructed ParkManager, or empty manager on failure
     */
    public static ParkManager loadParkFromFile(String filePath) {
        ParkManager park = new ParkManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int attractionCount = Integer.parseInt(reader.readLine());

            for (int i = 0; i < attractionCount; i++) {
                String[] attrParts = reader.readLine().split(",");
                String type = attrParts[0];
                String id = attrParts[1];
                String name = attrParts[2];
                int capacity = Integer.parseInt(attrParts[3]);
                int cycles = Integer.parseInt(attrParts[4]);

                Attraction attraction;
                if (type.equals("Ride")) {
                    attraction = new Ride(id, name, capacity);
                } else {
                    attraction = new Show(id, name, capacity);
                }

                // restore operator
                if (!attrParts[5].equals("null")) {
                    int opId = Integer.parseInt(attrParts[5]);
                    String opName = attrParts[6];
                    int opAge = Integer.parseInt(attrParts[7]);
                    String opPos = attrParts[8];
                    attraction.assignOperator(new Staff(opId, opName, opAge, opPos));
                }

                // restore waiting line
                int waitSize = Integer.parseInt(reader.readLine());
                for (int j = 0; j < waitSize; j++) {
                    String[] vParts = reader.readLine().split(",");
                    attraction.addVisitorToLine(new Visitor(
                            Integer.parseInt(vParts[0]), vParts[1],
                            Integer.parseInt(vParts[2]), vParts[3]
                    ));
                }

                // restore visit history
                int historySize = Integer.parseInt(reader.readLine());
                for (int j = 0; j < historySize; j++) {
                    String[] vParts = reader.readLine().split(",");
                    attraction.addVisitorToHistory(new Visitor(
                            Integer.parseInt(vParts[0]), vParts[1],
                            Integer.parseInt(vParts[2]), vParts[3]
                    ));
                }

                // restore cycle counter
                for (int c = 0; c < cycles; c++) attraction.cycleCount++;
                park.addAttraction(attraction);
            }
            System.out.println("Park data successfully loaded from " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("Load failed: File not found at " + filePath);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Load failed: Corrupted file format - " + e.getMessage());
        }
        return park;
    }
}
