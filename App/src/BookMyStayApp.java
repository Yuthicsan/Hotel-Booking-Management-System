/**
 * Book My Stay App
 *
 * Use Case 7: Add-On Service Selection
 *
 * Demonstrates:
 * - One-to-Many relationship (Reservation → Services)
 * - Map + List usage
 * - Cost aggregation
 * - Extension without modifying booking logic
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// -------------------- ADD-ON SERVICE --------------------

class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void display() {
        System.out.println(serviceName + " - ₹" + cost);
    }
}

// -------------------- ADD-ON SERVICE MANAGER --------------------

class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("➕ Added: " + service.getServiceName() +
                " to Reservation " + reservationId);
    }

    // Display all services for a reservation
    public void displayServices(String reservationId) {

        System.out.println("\nServices for Reservation: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService service : services) {
            service.display();
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getCost();
            }
        }

        return total;
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 7.0\n");

        // Example reservation IDs (from UC6)
        String res1 = "SR101";
        String res2 = "DR202";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService spa = new AddOnService("Spa Access", 1500);
        AddOnService pickup = new AddOnService("Airport Pickup", 800);

        // Add services to reservations
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);
        manager.addService(res1, spa);

        manager.addService(res2, wifi);
        manager.addService(res2, pickup);

        // Display selected services
        manager.displayServices(res1);
        manager.displayServices(res2);

        // Display total cost
        System.out.println("\nTotal Add-On Cost for " + res1 + ": ₹" +
                manager.calculateTotalCost(res1));

        System.out.println("Total Add-On Cost for " + res2 + ": ₹" +
                manager.calculateTotalCost(res2));

        System.out.println("\n======================================");
    }
}