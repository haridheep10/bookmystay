import java.io.*;
import java.util.*;

// Serializable Booking Class
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

    // Initialize default data
    public void initializeDefaultData() {
        inventory.put("Deluxe", 10);
        inventory.put("Suite", 5);
        bookingHistory.add(new Booking("B001", "Alice", "Deluxe"));
        System.out.println("Initialized with default data.");
    }

    // SAVE SYSTEM STATE
    public void saveSystemState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(inventory);
            oos.writeObject(bookingHistory);
            System.out.println("SUCCESS: Data saved to file.");
        } catch (IOException e) {
            System.out.println("ERROR: Saving failed.");
        }
    }

    // LOAD SYSTEM STATE
    @SuppressWarnings("unchecked")
    public void loadSystemState() {
        File file = new File(STORAGE_FILE);

        if (!file.exists()) {
            System.out.println("No saved file found. Starting fresh.");
            initializeDefaultData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            inventory = (Map<String, Integer>) ois.readObject();
            bookingHistory = (List<Booking>) ois.readObject();
            System.out.println("SUCCESS: Data loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ERROR: Loading failed. Resetting data.");
            initializeDefaultData();
        }
    }

    // DISPLAY DATA
    public void displayStatus() {
        System.out.println("\n--- SYSTEM STATE ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Bookings: " + bookingHistory);
        System.out.println("---------------------");
    }

    public static void main(String[] args) {
        BookMyStay app = new BookMyStay();

        // Load previous data
        app.loadSystemState();
        app.displayStatus();

        // Add new booking
        System.out.println("\nAdding new booking...");
        app.bookingHistory.add(new Booking("B002", "Bob", "Suite"));

        // Save updated data
        app.saveSystemState();

        System.out.println("Program finished.");
    }
}