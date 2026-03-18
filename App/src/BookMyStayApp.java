/**
 * Book My Stay App
 *
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Demonstrates how a Queue is used to handle booking requests
 * in a fair and ordered manner (FIFO).
 *
 * No inventory updates are performed at this stage.
 *
 * @author YourName
 * @version 5.1
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

    public void displayDetails() {
        System.out.println("Room Type: " + roomType +
                ", Beds: " + beds +
                ", Price: ₹" + price);
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

// -------------------- RESERVATION --------------------

/**
 * Represents a booking request from a guest.
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Guest: " + guestName + " | Requested: " + roomType);
    }
}

// -------------------- BOOKING QUEUE --------------------

/**
 * Manages booking requests using FIFO principle.
 */
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added:");
        reservation.display();
        System.out.println();
    }

    // View all requests (without removing)
    public void viewRequests() {
        System.out.println("------ Booking Request Queue ------");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 5.1\n");

        // Initialize queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate booking requests
        requestQueue.addRequest(new Reservation("Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("Bob", "Double Room"));
        requestQueue.addRequest(new Reservation("Charlie", "Suite Room"));
        requestQueue.addRequest(new Reservation("David", "Single Room"));

        // Display queue (FIFO order preserved)
        requestQueue.viewRequests();

        System.out.println("\n======================================");
    }
}