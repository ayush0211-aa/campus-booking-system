package com.campus.booking;

public class User {
    private String id;
    private String name;
    private String passwordHash;
    private Role role;

    public User(String id, String name, String passwordHash, Role role) {
        this.id = id;
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }

    public String toCsv() {
        return String.format("%s,%s,%s,%s", id, name, passwordHash, role.name());
    }

    public static User fromCsv(String line) {
        String[] parts = line.split(",");
        if (parts.length < 4) return null;
        return new User(parts[0], parts[1], parts[2], Role.valueOf(parts[3]));
    }
}