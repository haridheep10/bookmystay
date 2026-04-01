// Abstract Room Class
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: ₹" + price);
    }
}

// Single Room Class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

// Double Room Class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// Main Application Class
public class BookMyStay{

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App =====");
        System.out.println("Room Availability\n");

        // Creating room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display Single Room
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println("-----------------------------");

        // Display Double Room
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println("-----------------------------");

        // Display Suite Room
        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
        System.out.println("-----------------------------");

        System.out.println("Application Terminated.");
    }
}