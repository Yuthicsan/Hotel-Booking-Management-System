/**
 * Book My Stay App
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Demonstrates:
 * - FIFO booking processing using Queue
 * - Unique room allocation using Set
 * - Mapping room types to allocated rooms
 * - Immediate inventory synchronization
 * - Prevention of double-booking
 *
 * @author YourName
 * @version 6.0
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
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public Set<String> getRoomTypes() {
        return inventory.keySet();
    }
}

// -------------------- BOOKING REQUEST --------------------

class BookingRequest {

    private String customerName;
    private String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();
    private RoomInventory inventory;

    // Prevent duplicate room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> allocationMap = new HashMap<>();

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    public void processBookings() {

        while (!requestQueue.isEmpty()) {

            BookingRequest request = requestQueue.poll();
            String roomType = request.getRoomType();

            System.out.println("\nProcessing booking for: " + request.getCustomerName());

            // Step 1: Check availability
            if (inventory.getAvailability(roomType) <= 0) {
                System.out.println("❌ No rooms available for " + roomType);
                continue;
            }

            // Step 2: Generate unique room ID
            String roomId;
            do {
                roomId = roomType.substring(0, 2).toUpperCase() + new Random().nextInt(1000);
            } while (allocatedRoomIds.contains(roomId));

            // Step 3: Atomic allocation
            allocatedRoomIds.add(roomId);

            allocationMap
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            // Step 4: Update inventory immediately
            inventory.decrement(roomType);

            // Step 5: Confirm booking
            System.out.println("✅ Booking Confirmed");
            System.out.println("Customer: " + request.getCustomerName());
            System.out.println("Room Type: " + roomType);
            System.out.println("Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\n------ Allocation Summary ------");
        for (String type : allocationMap.keySet()) {
            System.out.println(type + " -> " + allocationMap.get(type));
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize rooms (same as UC4)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 6.0\n");

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        bookingService.addRequest(new BookingRequest("Alice", "Single Room"));
        bookingService.addRequest(new BookingRequest("Bob", "Double Room"));
        bookingService.addRequest(new BookingRequest("Charlie", "Single Room"));
        bookingService.addRequest(new BookingRequest("David", "Suite Room"));
        bookingService.addRequest(new BookingRequest("Eve", "Suite Room")); // should fail

        // Process bookings
        bookingService.processBookings();

        // Show final allocation
        bookingService.displayAllocations();

        System.out.println("\n======================================");
    }
}