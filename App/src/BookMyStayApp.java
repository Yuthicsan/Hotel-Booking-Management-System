/**
 * Book My Stay App
 *
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Demonstrates:
 * - Multi-threaded booking requests
 * - Shared mutable state
 * - Synchronized access for critical sections
 * - Prevention of double booking
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------

class Reservation {
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    // Thread-safe allocation
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    // Thread-safe rollback (for completeness)
    public synchronized void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public synchronized void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {
    private RoomInventory inventory;
    private Set<String> allocatedReservations = Collections.synchronizedSet(new HashSet<>());

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(String reservationId, String roomType) {
        synchronized (this) {
            if (allocatedReservations.contains(reservationId)) {
                System.out.println("❌ Reservation ID already allocated: " + reservationId);
                return;
            }

            boolean allocated = inventory.allocateRoom(roomType);
            if (allocated) {
                allocatedReservations.add(reservationId);
                System.out.println("✅ Booking Confirmed: " + reservationId + " | Room: " + roomType);
            } else {
                System.out.println("❌ Booking Failed: No availability for " + roomType + " | ID: " + reservationId);
            }
        }
    }
}

// -------------------- BOOKING TASK (THREAD) --------------------

class BookingTask implements Runnable {
    private BookingService service;
    private String reservationId;
    private String roomType;

    public BookingTask(BookingService service, String reservationId, String roomType) {
        this.service = service;
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    @Override
    public void run() {
        service.processBooking(reservationId, roomType);
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 11.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Simulate concurrent booking requests
        List<Thread> threads = new ArrayList<>();

        threads.add(new Thread(new BookingTask(bookingService, "SR101", "Single Room")));
        threads.add(new Thread(new BookingTask(bookingService, "SR102", "Single Room")));
        threads.add(new Thread(new BookingTask(bookingService, "DR201", "Double Room")));
        threads.add(new Thread(new BookingTask(bookingService, "DR202", "Double Room")));
        threads.add(new Thread(new BookingTask(bookingService, "ST301", "Suite Room")));
        threads.add(new Thread(new BookingTask(bookingService, "ST302", "Suite Room"))); // Exceed inventory

        // Start all threads
        for (Thread t : threads) t.start();

        // Wait for all threads to finish
        for (Thread t : threads) t.join();

        System.out.println("\nFinal Inventory:");
        inventory.displayInventory();

        System.out.println("\n======================================");
    }
}