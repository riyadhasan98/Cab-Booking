import java.util.*;

interface Bookable {
    void bookCab(String userName, String pickupLocation, String dropLocation);
}

abstract class Vehicle {
    private String id;
    private String type;
    private boolean available;

    public Vehicle(String id, String type) {
        this.id = id;
        this.type = type;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract void showDetails();
}

class Cab extends Vehicle implements Bookable {
    public Cab(String id, String type) {
        super(id, type);
    }

    @Override
    public void showDetails() {
        System.out.println("Cab ID: " + getId() + ", Type: " + getType() + ", Available: " + (isAvailable() ? "Yes" : "No"));
    }

    @Override
    public void bookCab(String userName, String pickupLocation, String dropLocation) {
        if (isAvailable()) {
            setAvailable(false);
            System.out.println("Cab " + getId() + " booked for " + userName + " from " + pickupLocation + " to " + dropLocation);
        } else {
            System.out.println("Sorry, this cab is not available.");
        }
    }
}

class Booking {
    private String bookingId;
    private String userName;
    private Cab cab;
    private String pickupLocation;
    private String dropLocation;
    private Date bookingTime;

    public Booking(String bookingId, String userName, Cab cab, String pickupLocation, String dropLocation) {
        this.bookingId = bookingId;
        this.userName = userName;
        this.cab = cab;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.bookingTime = new Date();
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + ", User: " + userName + ", Cab: " + cab.getId() +
                ", Pickup: " + pickupLocation + ", Drop: " + dropLocation + ", Time: " + bookingTime;
    }
}

class CabBookingSystem {
    private Cab[] cabs;
    private Booking[] bookings;
    private int bookingCount;
    private Map<String, String> users;
    private String currentUser;
    private Scanner scanner;

    public CabBookingSystem() {
        cabs = new Cab[4];
        bookings = new Booking[10];
        bookingCount = 0;
        users = new HashMap<>();
        currentUser = null;
        scanner = new Scanner(System.in);
        initializeCabs();
    }

    private void initializeCabs() {
        cabs[0] = new Cab("CAB001", "Sedan");
        cabs[1] = new Cab("CAB002", "SUV");
        cabs[2] = new Cab("CAB003", "Hatchback");
        cabs[3] = new Cab("CAB004", "Luxury");
    }

    private void registerUser() {
        System.out.print("\nEnter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        users.put(username, password);
        System.out.println("Registration successful! You can now log in.");
    }

    private boolean loginUser() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();

        if (!users.containsKey(username)) {
            System.out.println("Username not found.");
            return false;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (users.get(username).equals(password)) {
            currentUser = username;
            System.out.println("Login successful! Welcome " + username + ".");
            return true;
        } else {
            System.out.println("Incorrect password.");
            return false;
        }
    }

    private void logoutUser() {
        if (currentUser != null) {
            System.out.println(currentUser + " has logged out.");
            currentUser = null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    private void showAvailableCabs() {
        System.out.println("\nAvailable Cabs:");
        boolean found = false;
        for (Cab cab : cabs) {
            if (cab != null && cab.isAvailable()) {
                cab.showDetails();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No cabs available at the moment.");
        }
    }

    private void bookCab() {
        if (currentUser == null) {
            System.out.println("You must log in first to book a cab.");
            return;
        }
        showAvailableCabs();
        System.out.print("\nEnter Cab ID to book: ");
        String cabId = scanner.nextLine();
        Cab selectedCab = findCabById(cabId);
        if (selectedCab == null || !selectedCab.isAvailable()) {
            System.out.println("Cab is not available or invalid Cab ID.");
            return;
        }
        System.out.print("Enter pickup location: ");
        String pickupLocation = scanner.nextLine();
        System.out.print("Enter drop location: ");
        String dropLocation = scanner.nextLine();
        if (bookingCount >= bookings.length) {
            System.out.println("Booking limit reached. Cannot book more cabs.");
            return;
        }
        String bookingId = "BOOK" + (bookingCount + 1);
        selectedCab.bookCab(currentUser, pickupLocation, dropLocation);
        bookings[bookingCount] = new Booking(bookingId, currentUser, selectedCab, pickupLocation, dropLocation);
        bookingCount++;
        System.out.println("\nBooking Confirmed!");
        System.out.println(bookings[bookingCount - 1]);
    }

    private Cab findCabById(String cabId) {
        for (Cab cab : cabs) {
            if (cab != null && cab.getId().equals(cabId)) {
                return cab;
            }
        }
        return null;
    }

    private void viewBookings() {
        if (currentUser == null) {
            System.out.println("You must log in first to view bookings.");
            return;
        }
        System.out.println("\nAll Bookings for " + currentUser + ":");
        boolean found = false;
        for (int i = 0; i < bookingCount; i++) {
            if (bookings[i].toString().contains(currentUser)) {
                System.out.println(bookings[i]);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bookings found.");
        }
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== Cab Booking System ===");
            if (currentUser == null) {
                System.out.println("1. Register");
                System.out.println("2. Login");
            } else {
                System.out.println("Logged in as: " + currentUser);
            }
            System.out.println("3. Logout");
            System.out.println("4. View Available Cabs");
            System.out.println("5. Book a Cab");
            System.out.println("6. View All Bookings");
            System.out.println("7. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    if (currentUser == null) registerUser();
                    else System.out.println("Invalid choice.");
                    break;
                case 2:
                    if (currentUser == null) loginUser();
                    else System.out.println("Invalid choice.");
                    break;
                case 3:
                    logoutUser();
                    break;
                case 4:
                    showAvailableCabs();
                    break;
                case 5:
                    bookCab();
                    break;
                case 6:
                    viewBookings();
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CabBookingSystem system = new CabBookingSystem();
        system.showMenu();
    }
}
