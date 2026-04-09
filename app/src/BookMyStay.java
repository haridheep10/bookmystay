import java.util.*;

// Custom Exceptions
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

public class BookMyStay {

    private static Map<String, Integer> inventory = new HashMap<>();
    private static Map<String, String> activeBookings = new HashMap<>();
    private static Stack<String> releasedRoomsStack = new Stack<>();
    private static List<String> validRoomTypes = Arrays.asList("Standard", "Deluxe", "Suite");

    static {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 5);

        activeBookings.put("B101", "Deluxe:D-101");
        activeBookings.put("B102", "Standard:S-202");
    }

    // ✅ Booking with validation
    public static void processBooking(String guestName, String roomType) {
        try {
            validateRoomType(roomType);
            validateInventory(roomType);

            inventory.put(roomType, inventory.get(roomType) - 1);
            System.out.println("Booking confirmed for " + guestName);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidRoomTypeException("Invalid room type");
        }
    }

    private static void validateInventory(String roomType) throws InsufficientInventoryException {
        if (inventory.getOrDefault(roomType, 0) <= 0) {
            throw new InsufficientInventoryException("No rooms available");
        }
    }

    // ✅ Cancellation logic
    public static void cancelBooking(String bookingId) {
        if (!activeBookings.containsKey(bookingId)) {
            System.out.println("Booking not found");
            return;
        }

        String[] parts = activeBookings.get(bookingId).split(":");
        String roomType = parts[0];
        String roomId = parts[1];

        releasedRoomsStack.push(roomId);
        inventory.put(roomType, inventory.get(roomType) + 1);
        activeBookings.remove(bookingId);

        System.out.println("Cancelled booking: " + bookingId);
    }

    public static void main(String[] args) {
        processBooking("Guest1", "Standard");
        cancelBooking("B101");
    }
}