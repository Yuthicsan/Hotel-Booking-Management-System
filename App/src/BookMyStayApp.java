/**
 * Book My Stay App
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Demonstrates:
 * - Stack for rollback (LIFO)
 * - Safe cancellation of bookings
 * - Inventory restoration
 * - Validation of cancellation requests
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// -------------------- RESERVATION MODEL --------------------

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
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void increment(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory: " + inventory);
    }
}

// -------------------- CANCELLATION SERVICE --------------------

class CancellationService {

    private Map<String, Reservation> activeBookings = new HashMap<>();
    private Set<String> cancelledBookings = new HashSet<>();

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    private RoomInventory inventory;

    public CancellationService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Add confirmed booking (simulate UC6 output)
    public void addBooking(Reservation reservation) {
        activeBookings.put(reservation.getReservationId(), reservation);
    }

    // Cancel booking
    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!activeBookings.containsKey(reservationId)) {
            System.out.println("❌ Invalid reservation ID");
            return;
        }

        // Prevent duplicate cancellation
        if (cancelledBookings.contains(reservationId)) {
            System.out.println("❌ Booking already cancelled");
            return;
        }

        Reservation reservation = activeBookings.get(reservationId);

        // Step 1: Push to rollback stack
        rollbackStack.push(reservationId);

        // Step 2: Restore inventory
        inventory.increment(reservation.getRoomType());

        // Step 3: Mark as cancelled
        cancelledBookings.add(reservationId);

        System.out.println("✅ Booking Cancelled Successfully");
        System.out.println("Room Type Restored: " + reservation.getRoomType());
    }

    // Display rollback history
    public void displayRollbackStack() {
        System.out.println("\nRollback Stack (Recent First): " + rollbackStack);
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 10.0\n");

        RoomInventory inventory = new RoomInventory();
        CancellationService service = new CancellationService(inventory);

        // Simulate confirmed bookings (from UC6)
        service.addBooking(new Reservation("SR101", "Single Room"));
        service.addBooking(new Reservation("DR202", "Double Room"));
        service.addBooking(new Reservation("ST301", "Suite Room"));

        inventory.displayInventory();

        // Perform cancellations
        service.cancelBooking("SR101");
        service.cancelBooking("DR202");

        // Invalid cases
        service.cancelBooking("SR101"); // already cancelled
        service.cancelBooking("XX999"); // not exists

        // Display rollback stack
        service.displayRollbackStack();

        // Final inventory
        inventory.displayInventory();

        System.out.println("\n======================================");
    }
}