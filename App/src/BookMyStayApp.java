/**
 * Book My Stay App
 *
 * Use Case 9: Error Handling & Validation
 *
 * Demonstrates:
 * - Input validation
 * - Custom exceptions
 * - Fail-fast design
 * - Safe system behavior
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// -------------------- CUSTOM EXCEPTION --------------------

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- INVENTORY --------------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 0);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {

        int available = getAvailability(roomType);

        // Prevent negative inventory
        if (available <= 0) {
            throw new InvalidBookingException(
                    "❌ Cannot allocate room. No availability for: " + roomType
            );
        }

        inventory.put(roomType, available - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }
}

// -------------------- VALIDATOR --------------------

class BookingValidator {

    private RoomInventory inventory;

    public BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void validate(String customerName, String roomType)
            throws InvalidBookingException {

        // Validate customer name
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("❌ Customer name cannot be empty");
        }

        // Validate room type
        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException(
                    "❌ Invalid room type: " + roomType
            );
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException(
                    "❌ No rooms available for: " + roomType
            );
        }
    }
}

// -------------------- BOOKING SERVICE --------------------

class BookingService {

    private RoomInventory inventory;
    private BookingValidator validator;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.validator = new BookingValidator(inventory);
    }

    public void bookRoom(String customerName, String roomType) {

        try {
            // Step 1: Validate input (Fail-Fast)
            validator.validate(customerName, roomType);

            // Step 2: Allocate room
            inventory.decrement(roomType);

            // Step 3: Confirm booking
            System.out.println("✅ Booking Confirmed");
            System.out.println("Customer: " + customerName);
            System.out.println("Room Type: " + roomType);

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println(e.getMessage());
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 9.0\n");

        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.bookRoom("Alice", "Single Room");

        // Invalid: No availability
        bookingService.bookRoom("Bob", "Suite Room");

        // Invalid: Wrong room type
        bookingService.bookRoom("Charlie", "Luxury Room");

        // Invalid: Empty name
        bookingService.bookRoom("", "Double Room");

        // Valid booking
        bookingService.bookRoom("David", "Double Room");

        System.out.println("\n======================================");
    }
}