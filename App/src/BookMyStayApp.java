/**
 * Book My Stay App
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Demonstrates:
 * - Serialization of inventory and bookings
 * - Deserialization on system startup
 * - Recovery of system state after restart
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// -------------------- RESERVATION --------------------

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return reservationId + " -> " + roomType;
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void allocateRoom(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// -------------------- PERSISTENCE SERVICE --------------------

class PersistenceService {

    private static final String FILE_NAME = "booking_system_state.ser";

    public static void saveState(RoomInventory inventory, List<Reservation> bookings) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventory);
            out.writeObject(bookings);
            System.out.println("✅ System state saved successfully.");
        } catch (IOException e) {
            System.out.println("❌ Failed to save system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("⚠ Persistence file not found. Starting with default state.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            RoomInventory inventory = (RoomInventory) in.readObject();
            List<Reservation> bookings = (List<Reservation>) in.readObject();
            System.out.println("✅ System state restored successfully.");
            return new Object[]{inventory, bookings};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Failed to restore system state: " + e.getMessage());
            return null;
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 12.0\n");

        RoomInventory inventory;
        List<Reservation> bookings;

        // Attempt to restore previous state
        Object[] restoredState = PersistenceService.loadState();

        if (restoredState != null) {
            inventory = (RoomInventory) restoredState[0];
            bookings = (List<Reservation>) restoredState[1];
        } else {
            inventory = new RoomInventory();
            bookings = new ArrayList<>();
        }

        // Display current inventory and bookings
        inventory.displayInventory();
        System.out.println("Current Bookings: " + bookings);

        // Simulate new bookings
        Reservation r1 = new Reservation("SR101", "Single Room");
        Reservation r2 = new Reservation("DR201", "Double Room");

        bookings.add(r1);
        bookings.add(r2);

        inventory.allocateRoom(r1.getRoomType());
        inventory.allocateRoom(r2.getRoomType());

        System.out.println("\nAfter new bookings:");
        inventory.displayInventory();
        System.out.println("Bookings: " + bookings);

        // Save state before exit
        PersistenceService.saveState(inventory, bookings);

        System.out.println("\nSystem ready for next restart.");
        System.out.println("======================================");
    }
}