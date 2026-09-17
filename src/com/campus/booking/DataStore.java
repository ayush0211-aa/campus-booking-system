package com.campus.booking;

import java.io.*;
import java.util.*;

public class DataStore {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + File.separator + "users.csv";
    private static final String RESOURCES_FILE = DATA_DIR + File.separator + "resources.csv";
    private static final String BOOKINGS_FILE = DATA_DIR + File.separator + "bookings.csv";

    public DataStore() {
        initStorage();
    }

    private void initStorage() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        ensureFileExists(USERS_FILE, () -> {
            List<User> defaults = List.of(
                new User("admin", "System Administrator", AuthService.hashPassword("admin123"), Role.ADMIN),
                new User("FAC01", "Prof. Alan Turing", AuthService.hashPassword("fac123"), Role.FACULTY),
                new User("STU01", "Grace Hopper", AuthService.hashPassword("stu123"), Role.STUDENT)
            );
            saveUsers(defaults);
        });

        ensureFileExists(RESOURCES_FILE, () -> {
            List<Resource> defaults = List.of(
                new Resource("LAB-101", "High-Performance Compute Lab", "Lab", 40),
                new Resource("LAB-102", "Embedded Systems Lab", "Lab", 30),
                new Resource("SEM-201", "Auditorium Hall A", "Hall", 150)
            );
            saveResources(defaults);
        });

        ensureFileExists(BOOKINGS_FILE, () -> saveBookings(new ArrayList<>()));
    }

    private void ensureFileExists(String path, Runnable onInit) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
                onInit.run();
            } catch (IOException e) {
                System.err.println("Storage setup failed: " + e.getMessage());
            }
        }
    }

    public synchronized List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    User u = User.fromCsv(line.trim());
                    if (u != null) list.add(u);
                }
            }
        } catch (IOException ignored) {}
        return list;
    }

    public synchronized void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                writer.write(u.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public synchronized List<Resource> loadResources() {
        List<Resource> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Resource r = Resource.fromCsv(line.trim());
                    if (r != null) list.add(r);
                }
            }
        } catch (IOException ignored) {}
        return list;
    }

    public synchronized void saveResources(List<Resource> resources) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESOURCES_FILE))) {
            for (Resource r : resources) {
                writer.write(r.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving resources: " + e.getMessage());
        }
    }

    public synchronized List<Booking> loadBookings() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Booking b = Booking.fromCsv(line.trim());
                    if (b != null) list.add(b);
                }
            }
        } catch (IOException ignored) {}
        return list;
    }

    public synchronized void saveBookings(List<Booking> bookings) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking b : bookings) {
                writer.write(b.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
}