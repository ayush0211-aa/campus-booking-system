# Smart Campus Resource & Lab Slot Booking System

A pure Core Java CLI application engineered for scheduling and managing campus lab resources without external database engines or graphical dependencies.

---

## Technologies Used
- **Language**: Java 17 (Compatible with Java 11+)
- **Cryptography**: SHA-256 (`java.security.MessageDigest`)
- **Persistence**: Plain-text CSV flat-file storage via `java.io`
- **Architecture**: Layered CLI / Service / Model / Repository architecture

---

## Features
- **User Authentication**: Role-based access control (`STUDENT`, `FACULTY`, `ADMIN`) with SHA-256 password hashing.
- **Resource Discovery**: Real-time listing of campus labs and halls with assigned seat capacities.
- **Conflict Prevention**: Automated slot checks preventing duplicate bookings for the same room and time.
- **Booking Management**: View personal reservations or cancel active slots.
- **Admin Controls**: Administrative privileges to inspect all bookings and register new campus resources.

---

## Installation & Execution

### 1. Prerequisites
Ensure you have the Java Development Kit (JDK 11 or higher) installed:
```bash
java -version
javac -version