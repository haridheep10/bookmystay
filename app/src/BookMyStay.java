import java.util.LinkedList;
import java.util.Queue;

class BookingRequest {
    String guestName;
    int roomId;

    public BookingRequest(String guestName, int roomId) {
        this.guestName = guestName;
        this.roomId = roomId;
    }
}

class HotelInventory {
    private boolean[] rooms = new boolean[5]; // 5 rooms, false = available

    // Critical Section: Synchronized to prevent double allocation
    public synchronized boolean bookRoom(int roomId, String guest) {
        if (roomId < 0 || roomId >= rooms.length) return false;

        if (!rooms[roomId]) {
            // Simulate processing time to increase chance of race condition
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            rooms[roomId] = true; // Mark as booked
            System.out.println("SUCCESS: Room " + roomId + " booked for " + guest);
            return true;
        } else {
            System.out.println("FAILURE: Room " + roomId + " is already taken. Guest: " + guest);
            return false;
        }
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();

        // Scenario: Multiple guests trying to book the SAME room (Room 1)
        Runnable task1 = () -> inventory.bookRoom(1, "Alice");
        Runnable task2 = () -> inventory.bookRoom(1, "Bob");
        Runnable task3 = () -> inventory.bookRoom(2, "Charlie");

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);

        System.out.println("Starting concurrent booking simulation...");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}