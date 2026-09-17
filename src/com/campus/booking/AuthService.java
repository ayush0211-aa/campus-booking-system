package com.campus.booking;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthService {
    private final DataStore dataStore;

    public AuthService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 missing in runtime", e);
        }
    }

    public User authenticate(String userId, String rawPassword) throws AuthenticationException {
        List<User> users = dataStore.loadUsers();
        String hashed = hashPassword(rawPassword);
        for (User u : users) {
            if (u.getId().equalsIgnoreCase(userId) && u.getPasswordHash().equals(hashed)) {
                return u;
            }
        }
        throw new AuthenticationException("Invalid User ID or Password.");
    }

    public void registerUser(String id, String name, String rawPassword, Role role) throws Exception {
        List<User> users = dataStore.loadUsers();
        for (User u : users) {
            if (u.getId().equalsIgnoreCase(id)) {
                throw new IllegalArgumentException("User ID already exists.");
            }
        }
        users.add(new User(id, name, hashPassword(rawPassword), role));
        dataStore.saveUsers(users);
    }
}