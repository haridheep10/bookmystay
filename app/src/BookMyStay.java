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

// Booking Request (from UC11)
class BookingRequest {
    String guestName;
    int roomId;

    public BookingRequest(String guestName, int roomId) {
        this.guestName = guestName;
        this.roomId = roomId;
    }
}

// Hotel Inventory with synchronization (UC11)
class HotelInventory {
    private boolean[] rooms = new boolean[5];

    public synchronized boolean bookRoom(int roomId, String guest) {
        if (roomId < 0 || roomId >= rooms.length) return false;

        if (!rooms[roomId]) {
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            rooms[roomId] = true;
            System.out.println("SUCCESS: Room " + roomId + " booked for " + guest);
            return true;
        } else {
            System.out.println("FAILURE: Room " + roomId + " already taken. Guest: " + guest);
            return false;
        }
    }
}

public class BookMyStay {

    // Existing system (from main)
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

    // Booking logic
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

    // Cancellation logic
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

        // ✅ Existing features
        processBooking("Guest1", "Standard");
        cancelBooking("B101");

        // ✅ New concurrency feature (UC11)
        HotelInventory hotelInventory = new HotelInventory();

        Runnable t1 = () -> hotelInventory.bookRoom(1, "Alice");
        Runnable t2 = () -> hotelInventory.bookRoom(1, "Bob");
        Runnable t3 = () -> hotelInventory.bookRoom(2, "Charlie");

        System.out.println("\n--- Concurrent Booking Simulation ---");

        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
    }
}