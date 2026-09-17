package com.campus.booking;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingService {
    private final DataStore dataStore;
    public static final String[] SLOTS = {
        "09:00 - 11:00",
        "11:00 - 13:00",
        "14:00 - 16:00",
        "16:00 - 18:00"
    };

    public BookingService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public synchronized Booking createBooking(String resourceId, String userId, String date, String timeSlot) 
            throws SlotUnavailableException {
        
        List<Booking> bookings = dataStore.loadBookings();
        boolean conflict = bookings.stream().anyMatch(b -> 
            b.getResourceId().equalsIgnoreCase(resourceId) &&
            b.getDate().equals(date) &&
            b.getTimeSlot().equalsIgnoreCase(timeSlot) &&
            b.getStatus().equals("CONFIRMED")
        );

        if (conflict) {
            throw new SlotUnavailableException(
                "Slot " + timeSlot + " on " + date + " is already booked for " + resourceId
            );
        }

        String id = "BKG-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Booking newBooking = new Booking(id, resourceId.toUpperCase(), userId, date, timeSlot, "CONFIRMED");
        bookings.add(newBooking);
        dataStore.saveBookings(bookings);
        return newBooking;
    }

    public synchronized boolean cancelBooking(String bookingId, String userId, boolean isAdmin) {
        List<Booking> bookings = dataStore.loadBookings();
        for (Booking b : bookings) {
            if (b.getBookingId().equalsIgnoreCase(bookingId)) {
                if (isAdmin || b.getUserId().equalsIgnoreCase(userId)) {
                    b.setStatus("CANCELLED");
                    dataStore.saveBookings(bookings);
                    return true;
                }
            }
        }
        return false;
    }

    public List<Booking> getBookingsByUser(String userId) {
        return dataStore.loadBookings().stream()
                .filter(b -> b.getUserId().equalsIgnoreCase(userId))
                .collect(Collectors.toList());
    }

    public List<Booking> getAllBookings() {
        return dataStore.loadBookings();
    }

    public boolean isSlotAvailable(String resourceId, String date, String timeSlot) {
        return dataStore.loadBookings().stream().noneMatch(b -> 
            b.getResourceId().equalsIgnoreCase(resourceId) &&
            b.getDate().equals(date) &&
            b.getTimeSlot().equalsIgnoreCase(timeSlot) &&
            b.getStatus().equals("CONFIRMED")
        );
    }
}