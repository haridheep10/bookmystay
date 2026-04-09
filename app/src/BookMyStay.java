import java.util.*;

// Class representing an individual optional offering
class Service {
    private String name;
    private double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

// Manages the association between reservations and selected services
class AddOnServiceManager {
    // Map and List Combination: Mapping Reservation ID to a List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add a service to a specific reservation
    public void addServiceToReservation(String reservationId, Service service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
        System.out.println("Added " + service.getName() + " to Reservation: " + reservationId);
    }

    // Cost Aggregation: Calculate total additional cost
    public double calculateTotalAddOnCost(String reservationId) {
        List<Service> services = reservationServices.getOrDefault(reservationId, Collections.emptyList());
        return services.stream().mapToDouble(Service::getPrice).sum();
    }

    // Retrieve all services for a reservation
    public List<Service> getServicesForReservation(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }
}

public class BookMyStay {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample Reservation ID
        String resId = "RES-1001";

        // Define available services
        Service breakfast = new Service("Buffet Breakfast", 25.0);
        Service spa = new Service("Spa Treatment", 80.0);
        Service airportShuttle = new Service("Airport Shuttle", 45.0);

        System.out.println("--- Selecting Add-On Services ---");

        // Guest selects services
        manager.addServiceToReservation(resId, breakfast);
        manager.addServiceToReservation(resId, spa);
        manager.addServiceToReservation(resId, airportShuttle);

        // Display results
        System.out.println("\n--- Reservation Summary (" + resId + ") ---");
        System.out.println("Selected Services: " + manager.getServicesForReservation(resId));

        double totalCost = manager.calculateTotalAddOnCost(resId);
        System.out.println("Total Additional Cost: $" + totalCost);

        System.out.println("\nNote: Core booking and inventory state remain unchanged.");
    }
}