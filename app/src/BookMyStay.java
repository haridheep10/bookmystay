import java.util.HashMap;
import java.util.Map;

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void displayInventory() {
        System.out.println("===== Available Rooms =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
            }
        }

        System.out.println("---------------------------");
    }
}

public class BookMyStay {

    abstract static class Room {
        protected String roomType;
        protected int beds;
        protected double price;

        public Room(String roomType, int beds, double price) {
            this.roomType = roomType;
            this.beds = beds;
            this.price = price;
        }

        public void displayRoomDetails(int availableRooms) {
            System.out.println("Room Type: " + roomType);
            System.out.println("Beds: " + beds);
            System.out.println("Price per night: ₹" + price);
            System.out.println("Available Rooms: " + availableRooms);
            System.out.println("-----------------------------");
        }
    }

    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 2000);
        }
    }

    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 3500);
        }
    }

    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 6000);
        }
    }

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = inventory.getAvailability("Single Room");
        int doubleAvailable = inventory.getAvailability("Double Room");
        int suiteAvailable = inventory.getAvailability("Suite Room");

        if (singleAvailable > 0) {
            single.displayRoomDetails(singleAvailable);
        }

        if (doubleAvailable > 0) {
            doubleRoom.displayRoomDetails(doubleAvailable);
        }

        if (suiteAvailable > 0) {
            suite.displayRoomDetails(suiteAvailable);
        }
    }
}