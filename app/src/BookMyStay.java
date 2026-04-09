import java.util.*;

public class BookMyStay {

    // Simulating inventory: Room Type -> Available Count
    private static Map<String, Integer> inventory = new HashMap<>();
    // Tracking active bookings: Booking ID -> Room Details (Type:RoomID)
    private static Map<String, String> activeBookings = new HashMap<>();
    // Stack to track recently released room IDs for rollback history
    private static Stack<String> releasedRoomsStack = new Stack<>();

    static {
        // Initializing inventory
        inventory.put("Deluxe", 5);
        inventory.put("Standard", 10);

        // Simulating some initial confirmed bookings
        activeBookings.put("B101", "Deluxe:D-101");
        activeBookings.put("B102", "Standard:S-202");
    }

    /**
     * Goal: Enable safe cancellation and inventory rollback.
     */
    public static void cancelBooking(String bookingId) {
        System.out.println("\n--- Initiating Cancellation for ID: " + bookingId + " ---");

        // 1. Validation: Ensure the reservation exists
        if (!activeBookings.containsKey(bookingId)) {
            System.out.println("Error: Cancellation failed. Booking ID not found or already cancelled.");
            return;
        }

        // 2. Extract details for rollback
        String bookingDetails = activeBookings.get(bookingId);
        String[] parts = bookingDetails.split(":");
        String roomType = parts[0];
        String roomId = parts[1];

        // 3. Rollback Operation: Release Room ID to Stack (LIFO)
        releasedRoomsStack.push(roomId);
        System.out.println("Step 1: Room " + roomId + " added to rollback stack.");

        // 4. Inventory Restoration: Increment count immediately
        inventory.put(roomType, inventory.get(roomType) + 1);
        System.out.println("Step 2: Inventory for " + roomType + " restored. New count: " + inventory.get(roomType));

        // 5. State Update: Remove from active bookings
        activeBookings.remove(bookingId);
        System.out.println("Step 3: Booking history updated. Cancellation Complete.");
    }

    public static void displayStatus() {
        System.out.println("\n--- Current System State ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Active Bookings: " + activeBookings.keySet());
        System.out.println("Recently Released Rooms (Stack): " + releasedRoomsStack);
    }

    public static void main(String[] args) {
        displayStatus();

        // Perform cancellation
        cancelBooking("B101");

        // Attempting to cancel a non-existent booking (Validation check)
        cancelBooking("B999");

        displayStatus();
    }
}