package com.campus.booking;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DataStore dataStore = new DataStore();
    private static final AuthService authService = new AuthService(dataStore);
    private static final BookingService bookingService = new BookingService(dataStore);
    private static User currentUser = null;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--test")) {
            runAutomatedTests();
            return;
        }

        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showAuthMenu() {
        System.out.println("\n==================================================");
        System.out.println("  SMART CAMPUS RESOURCE & LAB SLOT BOOKING SYSTEM ");
        System.out.println("==================================================");
        System.out.println("1. Login");
        System.out.println("2. Register Student Account");
        System.out.println("3. Exit");
        System.out.print("Select Option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> handleLogin();
            case "2" -> handleRegister();
            case "3" -> {
                System.out.println("Exiting terminal. Goodbye.");
                System.exit(0);
            }
            default -> System.out.println("[!] Invalid choice. Enter 1, 2, or 3.");
        }
    }

    private static void handleLogin() {
        System.out.print("Enter User ID: ");
        String uid = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine().trim();

        try {
            currentUser = authService.authenticate(uid, pwd);
            System.out.printf("\n[✓] Login successful. Welcome %s (Role: %s)\n", currentUser.getName(), currentUser.getRole());
        } catch (AuthenticationException e) {
            System.out.printf("[!] %s\n", e.getMessage());
        }
    }

    private static void handleRegister() {
        System.out.print("Choose User ID: ");
        String uid = scanner.nextLine().trim();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String pwd = scanner.nextLine().trim();

        try {
            authService.registerUser(uid, name, pwd, Role.STUDENT);
            System.out.println("[✓] Account created successfully. You may now log in.");
        } catch (Exception e) {
            System.out.printf("[!] Registration Failed: %s\n", e.getMessage());
        }
    }

    private static void showMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. View Available Resources (Labs & Halls)");
        System.out.println("2. Book a Resource Slot");
        System.out.println("3. View My Active Bookings");
        System.out.println("4. Cancel a Booking");

        if (currentUser.getRole() == Role.ADMIN) {
            System.out.println("5. [ADMIN] View All Global Bookings");
            System.out.println("6. [ADMIN] Add New Resource");
        }

        System.out.println("0. Logout");
        System.out.print("Select Option: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> listResources();
            case "2" -> handleBooking();
            case "3" -> viewUserBookings();
            case "4" -> handleCancellation();
            case "5" -> {
                if (currentUser.getRole() == Role.ADMIN) viewAllBookings();
                else System.out.println("[!] Unauthorized action.");
            }
            case "6" -> {
                if (currentUser.getRole() == Role.ADMIN) handleAddResource();
                else System.out.println("[!] Unauthorized action.");
            }
            case "0" -> {
                System.out.println("[✓] Logged out successfully.");
                currentUser = null;
            }
            default -> System.out.println("[!] Invalid choice.");
        }
    }

    private static void listResources() {
        List<Resource> list = dataStore.loadResources();
        System.out.println("\n================ Registered Resources ================");
        System.out.printf("%-10s %-30s %-12s %-8s\n", "ID", "Name", "Category", "Capacity");
        System.out.println("----------------------------------------------------------------");
        for (Resource r : list) {
            System.out.printf("%-10s %-30s %-12s %-8d\n", r.getId(), r.getName(), r.getCategory(), r.getCapacity());
        }
    }

    private static void handleBooking() {
        listResources();
        System.out.print("\nEnter Resource ID to reserve: ");
        String rId = scanner.nextLine().trim().toUpperCase();

        boolean exists = dataStore.loadResources().stream().anyMatch(r -> r.getId().equalsIgnoreCase(rId));
        if (!exists) {
            System.out.println("[!] Resource ID not found.");
            return;
        }

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        System.out.println("\nAvailable Standard Time Slots:");
        for (int i = 0; i < BookingService.SLOTS.length; i++) {
            String slot = BookingService.SLOTS[i];
            boolean avail = bookingService.isSlotAvailable(rId, date, slot);
            System.out.printf("  %d. %s [%s]\n", (i + 1), slot, avail ? "AVAILABLE" : "OCCUPIED");
        }

        System.out.print("Select slot number (1-4): ");
        try {
            int slotIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (slotIdx < 0 || slotIdx >= BookingService.SLOTS.length) {
                System.out.println("[!] Invalid slot selection.");
                return;
            }

            String chosenSlot = BookingService.SLOTS[slotIdx];
            Booking b = bookingService.createBooking(rId, currentUser.getId(), date, chosenSlot);
            System.out.println("\n[✓] SUCCESS: Reservation Confirmed!");
            System.out.printf("    Booking ID: %s | Resource: %s | Date: %s | Slot: %s\n",
                    b.getBookingId(), b.getResourceId(), b.getDate(), b.getTimeSlot());
        } catch (NumberFormatException e) {
            System.out.println("[!] Input must be a valid number.");
        } catch (SlotUnavailableException e) {
            System.out.printf("[!] Conflict: %s\n", e.getMessage());
        }
    }

    private static void viewUserBookings() {
        List<Booking> list = bookingService.getBookingsByUser(currentUser.getId());
        displayBookings(list);
    }

    private static void viewAllBookings() {
        List<Booking> list = bookingService.getAllBookings();
        displayBookings(list);
    }

    private static void displayBookings(List<Booking> list) {
        System.out.println("\n================ Reservation Records ================");
        if (list.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.printf("%-10s %-10s %-10s %-12s %-16s %-10s\n",
                "Booking ID", "Resource", "User", "Date", "Slot", "Status");
        System.out.println("------------------------------------------------------------------");
        for (Booking b : list) {
            System.out.printf("%-10s %-10s %-10s %-12s %-16s %-10s\n",
                    b.getBookingId(), b.getResourceId(), b.getUserId(), b.getDate(), b.getTimeSlot(), b.getStatus());
        }
    }

    private static void handleCancellation() {
        System.out.print("Enter Booking ID to cancel: ");
        String bId = scanner.nextLine().trim();
        boolean success = bookingService.cancelBooking(bId, currentUser.getId(), currentUser.getRole() == Role.ADMIN);
        if (success) {
            System.out.println("[✓] Booking marked as CANCELLED.");
        } else {
            System.out.println("[!] Booking ID not found or unauthorized to cancel.");
        }
    }

    private static void handleAddResource() {
        System.out.print("Enter Resource Code (e.g., LAB-103): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Resource Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Category (Lab/Hall/Room): ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter Capacity: ");
        try {
            int cap = Integer.parseInt(scanner.nextLine().trim());
            List<Resource> list = dataStore.loadResources();
            list.add(new Resource(id, name, category, cap));
            dataStore.saveResources(list);
            System.out.println("[✓] Resource added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("[!] Capacity must be an integer.");
        }
    }

    private static void runAutomatedTests() {
        System.out.println("Running automated assertion tests...");
        try {
            assert AuthService.hashPassword("test").equals(AuthService.hashPassword("test")) : "Hash symmetry failure";
            System.out.println("[PASS] Cryptographic Hash Test");

            boolean slotInitial = bookingService.isSlotAvailable("TEST-LAB", "2026-10-01", "09:00 - 11:00");
            assert slotInitial : "Initial slot should be available";
            System.out.println("[PASS] Slot Availability Test");

            Booking b = bookingService.createBooking("TEST-LAB", "TEST-USER", "2026-10-01", "09:00 - 11:00");
            assert !bookingService.isSlotAvailable("TEST-LAB", "2026-10-01", "09:00 - 11:00") : "Slot should be locked";
            System.out.println("[PASS] Slot Lockout Test");

            bookingService.cancelBooking(b.getBookingId(), "TEST-USER", false);
            assert bookingService.isSlotAvailable("TEST-LAB", "2026-10-01", "09:00 - 11:00") : "Slot should be released";
            System.out.println("[PASS] Slot Cancellation Test");

            System.out.println("All automated test assertions passed.");
        } catch (Exception e) {
            System.err.printf("[FAIL] Assertion Error: %s\n", e.getMessage());
        }
    }
}