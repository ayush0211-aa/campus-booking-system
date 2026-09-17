package com.campus.booking;

public class Booking {
    private String bookingId;
    private String resourceId;
    private String userId;
    private String date;
    private String timeSlot;
    private String status;

    public Booking(String bookingId, String resourceId, String userId, String date, String timeSlot, String status) {
        this.bookingId = bookingId;
        this.resourceId = resourceId;
        this.userId = userId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public String getBookingId() { return bookingId; }
    public String getResourceId() { return resourceId; }
    public String getUserId() { return userId; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s,%s", bookingId, resourceId, userId, date, timeSlot, status);
    }

    public static Booking fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 6) return null;
        return new Booking(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}