/**
 * Book My Stay App
 *
 * Use Case 8: Booking History & Reporting
 *
 * Demonstrates:
 * - List for historical tracking (insertion order)
 * - Booking history storage
 * - Reporting service (separation of concerns)
 * - Read-only reporting
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

// -------------------- RESERVATION MODEL --------------------

class Reservation {

    private String reservationId;
    private String customerName;
    private String roomType;

    public Reservation(String reservationId, String customerName, String roomType) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType);
    }
}

// -------------------- BOOKING HISTORY --------------------

class BookingHistory {

    // List maintains insertion order
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// -------------------- REPORT SERVICE --------------------

class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display all bookings
    public void displayAllBookings() {

        System.out.println("\n------ Booking History ------");

        List<Reservation> reservations = history.getAllReservations();

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {

        System.out.println("\n------ Booking Summary Report ------");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            roomCount.put(type, roomCount.getOrDefault(type, 0) + 1);
        }

        for (String type : roomCount.keySet()) {
            System.out.println(type + " Bookings: " + roomCount.get(type));
        }
    }
}

// -------------------- MAIN APPLICATION --------------------

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 8.0\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from UC6)
        history.addReservation(new Reservation("SR101", "Alice", "Single Room"));
        history.addReservation(new Reservation("DR202", "Bob", "Double Room"));
        history.addReservation(new Reservation("SR102", "Charlie", "Single Room"));
        history.addReservation(new Reservation("ST301", "David", "Suite Room"));

        // Initialize report service
        BookingReportService reportService = new BookingReportService(history);

        // Display all bookings
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummaryReport();

        System.out.println("\n======================================");
    }
}