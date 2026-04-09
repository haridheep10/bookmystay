import java.io.*;
import java.util.*;

// Classes must implement Serializable to be saved to a file
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    String bookingId;
    String guestName;
    String roomType;

    public Booking(String bookingId, String guestName, String roomType) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "ID: " + bookingId + " | Guest: " + guestName + " | Room: " + roomType;
    }
}

public class BookMyStay {
    private static final String STORAGE_FILE = "system_state.ser";
    private Map<String, Integer> inventory = new HashMap<>();
    private List<Booking> bookingHistory = new ArrayList<>();

    public void initializeDefaultData() {
        inventory.put("Deluxe", 10);
        inventory.put("Suite", 5);
        bookingHistory.add(new Booking("B001", "Alice", "Deluxe"));
        System.out.println("Initialized with default in-memory data.");
    }

    // --- PERSISTENCE LOGIC ---

    public void saveSystemState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(inventory);
            oos.writeObject(bookingHistory);
            System.out.println("SUCCESS: System state serialized and saved to " + STORAGE_FILE);
        } catch (IOException e) {
            System.err.println("ERROR: Could not save state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSystemState() {
        File file = new File(STORAGE_FILE);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            initializeDefaultData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            inventory = (Map<String, Integer>) ois.readObject();
            bookingHistory = (List<Booking>) ois.readObject();
            System.out.println("SUCCESS: System state recovered from " + STORAGE_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERROR: Recovery failed (corrupted file). Resetting to defaults.");
            initializeDefaultData();
        }
    }

    // --- DISPLAY LOGIC ---

    public void displayStatus() {
        System.out.println("\n--- Current System State ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookingHistory);
        System.out.println("---------------------------\n");
    }

    public static void main(String[] args) {
        BookMyStay app = new BookMyStay();

        // 1. Attempt to restore data from previous run
        app.loadSystemState();
        app.displayStatus();

        // 2. Simulate a new booking
        System.out.println("Processing new booking...");
        app.bookingHistory.add(new Booking("B002", "Bob", "Suite"));

        // 3. Save state before shutdown
        app.saveSystemState();
        System.out.println("System shutting down safely.");
    }
}