/**
 * Book My Stay App
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Demonstrates read-only access to centralized inventory.
 * Only available rooms are displayed without modifying system state.
 *
 * @author YourName
 * @version 4.1
 */

import java.util.*;

// -------------------- DOMAIN MODEL --------------------

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

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // intentionally unavailable
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose all keys for search
    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

// -------------------- SEARCH SERVICE --------------------

/**
 * Handles read-only search operations.
 */
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.roomCatalog = new HashMap<>();

        // Build lookup for room details
        for (Room room : rooms) {
            roomCatalog.put(room.getRoomType(), room);
        }
    }

    public void searchAvailableRooms() {

        System.out.println("------ Available Rooms ------");

        for (String roomType : inventory.getRoomTypes()) {

            int available = inventory.getAvailability(roomType);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(roomType);

                if (room != null) {
                    room.displayDetails();
                    System.out.println("Available: " + available);
                    System.out.println();
                }
            }
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize domain objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 4.1\n");

        // Perform search (read-only)
        searchService.searchAvailableRooms();

        System.out.println("======================================");
    }
}