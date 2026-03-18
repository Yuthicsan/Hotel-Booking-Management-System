/**
 * Book My Stay App
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates how HashMap is used to centralize and manage
 * room availability efficiently.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// -------------------- DOMAIN MODEL --------------------

// Abstract Room class
abstract class Room {

    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getBeds() {
        return beds;
    }

    public double getPrice() {
        return price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// -------------------- INVENTORY MANAGEMENT --------------------

/**
 * RoomInventory acts as the single source of truth
 * for all room availability.
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial availability setup
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (controlled)
    public void updateAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("Room type not found: " + roomType);
        }
    }

    // Display entire inventory
    public void displayInventory() {
        System.out.println("------ Current Room Inventory ------");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize rooms (Domain)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Initialize inventory (Centralized state)
        RoomInventory inventory = new RoomInventory();

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 3.1\n");

        // Display Room Details + Availability
        System.out.println("--- Single Room ---");
        singleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(singleRoom.getRoomType()) + "\n");

        System.out.println("--- Double Room ---");
        doubleRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getRoomType()) + "\n");

        System.out.println("--- Suite Room ---");
        suiteRoom.displayDetails();
        System.out.println("Available: " + inventory.getAvailability(suiteRoom.getRoomType()) + "\n");

        // Display full inventory
        inventory.displayInventory();

        System.out.println("======================================");
    }
}