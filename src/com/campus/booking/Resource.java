package com.campus.booking;

public class Resource {
    private String id;
    private String name;
    private String category;
    private int capacity;

    public Resource(String id, String name, String category, int capacity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getCapacity() { return capacity; }

    public String toCsv() {
        return String.format("%s,%s,%s,%d", id, name, category, capacity);
    }

    public static Resource fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) return null;
        return new Resource(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
    }
}