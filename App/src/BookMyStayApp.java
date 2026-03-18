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

/**
 * Main Application Class
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("========== Book My Stay App ==========");
        System.out.println("Version: 2.1\n");

        // Single Room
        System.out.println("--- Single Room ---");
        singleRoom.displayDetails();
        System.out.println("Available: " + singleAvailability + "\n");

        // Double Room
        System.out.println("--- Double Room ---");
        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability + "\n");

        // Suite Room
        System.out.println("--- Suite Room ---");
        suiteRoom.displayDetails();
        System.out.println("Available: " + suiteAvailability + "\n");

        System.out.println("======================================");
    }
}