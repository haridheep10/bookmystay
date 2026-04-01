import java.util.*;

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

    public void decrementRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }
}

class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

class BookingRequestQueue {

    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.guestName);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasRequests() {
        return !requestQueue.isEmpty();
    }

    public void displayQueue() {
        System.out.println("\n===== Booking Request Queue =====");
        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
        System.out.println("---------------------------------");
    }
}

class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> roomAllocationMap = new HashMap<>();

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (queue.hasRequests()) {

            Reservation reservation = queue.getNextRequest();
            String roomType = reservation.roomType;

            if (inventory.getAvailability(roomType) > 0) {

                String roomId = roomType.replace(" ", "") + "-" + UUID.randomUUID().toString().substring(0, 5);

                while (allocatedRoomIds.contains(roomId)) {
                    roomId = roomType.replace(" ", "") + "-" + UUID.randomUUID().toString().substring(0, 5);
                }

                allocatedRoomIds.add(roomId);

                roomAllocationMap.putIfAbsent(roomType, new HashSet<>());
                roomAllocationMap.get(roomType).add(roomId);

                inventory.decrementRoom(roomType);

                System.out.println("\nReservation Confirmed");
                System.out.println("Guest: " + reservation.guestName);
                System.out.println("Room Type: " + roomType);
                System.out.println("Assigned Room ID: " + roomId);
                System.out.println("-----------------------------");

            } else {
                System.out.println("Reservation Failed for " + reservation.guestName + " (No rooms available)");
            }
        }
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

        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Arjun", "Single Room"));
        queue.addRequest(new Reservation("Priya", "Double Room"));
        queue.addRequest(new Reservation("Rahul", "Suite Room"));

        queue.displayQueue();

        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);
    }
}