import java.util.*;

// Custom Exception for Invalid Room Types
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

// Custom Exception for Insufficient Inventory
class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

public class BookMyStay {
    private static Map<String, Integer> inventory = new HashMap<>();
    private static List<String> validRoomTypes = Arrays.asList("Standard", "Deluxe", "Suite");

    static {
        // Initialize inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 5);
    }

    public static void main(String[] args) {
        System.out.println("--- Book My Stay: Error Handling & Validation ---");

        // Test Case 1: Valid Booking
        processBooking("Guest_001", "Standard");

        // Test Case 2: Invalid Room Type (Triggers Exception)
        processBooking("Guest_002", "Penthouse");

        // Test Case 3: Out of Stock (Triggers Exception)
        processBooking("Guest_003", "Standard");
        processBooking("Guest_004", "Standard"); // This should fail

        System.out.println("\nFinal Inventory State: " + inventory);
    }

    /**
     * Processes booking with Fail-Fast validation logic.
     */
    public static void processBooking(String guestName, String roomType) {
        try {
            System.out.println("\nProcessing request for " + guestName + " (Room: " + roomType + ")...");

            // 1. Validate Room Type
            validateRoomType(roomType);

            // 2. Validate Inventory Availability
            validateInventory(roomType);

            // 3. Update State (Only reached if validations pass)
            inventory.put(roomType, inventory.get(roomType) - 1);
            System.out.println("SUCCESS: Booking confirmed for " + guestName);

        } catch (InvalidRoomTypeException | InsufficientInventoryException e) {
            // Graceful Failure Handling
            System.err.println("VALIDATION FAILURE: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("SYSTEM ERROR: An unexpected error occurred.");
        }
    }

    private static void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidRoomTypeException("Room type '" + roomType + "' is not supported.");
        }
    }

    private static void validateInventory(String roomType) throws InsufficientInventoryException {
        int count = inventory.getOrDefault(roomType, 0);
        if (count <= 0) {
            throw new InsufficientInventoryException("No " + roomType + " rooms available in inventory.");
        }
    }
}