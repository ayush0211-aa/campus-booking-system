# Smart Campus Resource & Lab Slot Booking System

A modular **Core Java Command-Line Interface (CLI)** application for conflict-free lab and seminar hall slot reservations. The system implements **Role-Based Access Control (RBAC)**, SHA-256 password hashing, deterministic slot collision detection, and synchronized CSV-based persistence without external databases or GUI dependencies.

---

## 📋 Table of Contents

* [Key Features](#-key-features)
* [System Architecture](#-system-architecture)
* [Project Structure](#-project-structure)
* [Storage Schema](#-storage-schema)
* [Prerequisites](#-prerequisites)
* [Compilation & Execution](#-compilation--execution)
* [Default Demonstration Accounts](#-default-demonstration-accounts)
* [Interactive CLI Walkthrough](#-interactive-cli-walkthrough)
* [Automated Testing](#-automated-testing)
* [Design Decisions](#-design-decisions)
* [Troubleshooting & FAQs](#-troubleshooting--faqs)

---

## 🚀 Key Features

* **Role-Based Access Control (RBAC)**
  Dedicated workflows and permissions for `STUDENT`, `FACULTY`, and `ADMIN` roles.

* **Password Security**
  Passwords are not stored in plaintext. The application uses Java's `MessageDigest` with SHA-256 hashing for credential storage.

* **Deterministic Conflict Prevention**
  Booking requests are validated against existing active reservations using the composite key:
  `resourceId + date + timeSlot`.

* **Thread-Safe CSV Persistence**
  `DataStore.java` uses synchronized operations to reduce race conditions and inconsistent file writes.

* **Complete Booking Lifecycle**
  Generates unique booking IDs, confirms reservations, produces transaction information, and supports cancellation.

* **Administrative Management**
  Administrators can provision campus resources and inspect institutional booking records.

* **Built-in Assertion Testing**
  A native `--test` mode validates hashing, availability checking, collision prevention, and cancellation recovery.

---

## 🏗️ System Architecture

The application follows a **four-tier architecture** based on Separation of Concerns (SoC).

```text
┌───────────────────────────────────────────────────────────────┐
│                    PRESENTATION TIER                          │
│                         Main.java                             │
│  • Terminal REPL          • Role-Based Menu Routing          │
│  • Input Validation       • ANSI Terminal Formatting         │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                    BUSINESS SERVICE TIER                      │
│                                                               │
│  AuthService.java              BookingService.java             │
│  • Authentication              • Conflict Detection           │
│  • SHA-256 Hashing              • Slot Management              │
│  • Session Identity             • Booking Lifecycle            │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                       DOMAIN MODEL TIER                       │
│                                                               │
│  User.java     Resource.java     Booking.java     Role.java    │
│                                                               │
│  • State Encapsulation                                          │
│  • CSV Serialization / Deserialization                          │
└───────────────────────────────┬───────────────────────────────┘
                                │
                                ▼
┌───────────────────────────────────────────────────────────────┐
│                    DATA PERSISTENCE TIER                       │
│                                                               │
│  DataStore.java                                                 │
│  • Synchronized CSV Persistence                                │
│                                                               │
│  data/users.csv     data/resources.csv     data/bookings.csv  │
└───────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```text
campus-booking/
├── .gitignore
├── README.md
├── statement.md
├── composite-history.md
├── report.html
├── bin/
├── data/
│   ├── users.csv
│   ├── resources.csv
│   └── bookings.csv
└── src/
    └── com/
        └── campus/
            └── booking/
                ├── Role.java
                ├── User.java
                ├── Resource.java
                ├── Booking.java
                ├── DataStore.java
                ├── AuthService.java
                ├── BookingService.java
                ├── SlotUnavailableException.java
                ├── AuthenticationException.java
                └── Main.java
```

### Core Components

| Component             | Responsibility                              |
| --------------------- | ------------------------------------------- |
| `Main.java`           | CLI interface, menu routing and test runner |
| `AuthService.java`    | Authentication and password hashing         |
| `BookingService.java` | Booking and conflict detection              |
| `DataStore.java`      | CSV persistence and synchronization         |
| `User.java`           | User entity and serialization               |
| `Resource.java`       | Campus resource model                       |
| `Booking.java`        | Reservation entity and lifecycle            |
| `Role.java`           | User privilege definitions                  |

---

## 💾 Storage Schema

Application state is persisted using three CSV files inside the `data/` directory.

### `data/users.csv`

Stores user identity, hashed credentials and roles.

```csv
userId,name,passwordHash,role
admin,System Administrator,8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918,ADMIN
STU01,Grace Hopper,c1800cf858d4a7fb96aa8a84617df0e107df6200236a287c8ec1766cc6dfa406,STUDENT
FAC01,Alan Turing,7014457e51c893aa8cc45c36f2b74fc7161e1b5aa778939c4d9326f2284cf635,FACULTY
```

### `data/resources.csv`

Stores available campus facilities.

```csv
id,name,category,capacity
LAB-101,High-Performance Compute Lab,Lab,40
LAB-102,Embedded Systems Lab,Lab,30
SEM-201,Auditorium Hall A,Hall,150
```

### `data/bookings.csv`

Stores active and historical booking transactions.

```csv
bookingId,resourceId,userId,date,timeSlot,status
BKG-9B21FA,LAB-101,STU01,2026-10-15,14:00 - 16:00,CONFIRMED
BKG-44C91E,LAB-102,FAC01,2026-10-16,09:00 - 11:00,CONFIRMED
```

---

## ☕ Prerequisites

### Java

* **JDK 11 or higher**
* Tested with **OpenJDK 17 LTS**

Verify your installation:

```bash
java -version
javac -version
```

### Operating System

The application is designed to be platform-agnostic and can run on:

* macOS
* Linux
* Unix
* Windows

### Dependencies

No external libraries are required.

The project uses the Java Standard Library, including:

```text
java.io
java.util
java.security
java.nio
```

---

## ▶️ Compilation & Execution

### 1. Clone the Repository

```bash
git clone <YOUR-REPOSITORY-URL>
cd campus-booking
```

### 2. Compile

Compile all Java source files into the `bin/` directory:

```bash
javac -d bin src/com/campus/booking/*.java
```

### 3. Run the Application

```bash
java -cp bin com.campus.booking.Main
```

### 4. Run Automated Tests

```bash
java -ea -cp bin com.campus.booking.Main --test
```

---

## 🔐 Default Demonstration Accounts

The application seeds demonstration profiles on the first launch.

| User ID | Password   | Role      | Permissions                                                         |
| ------- | ---------- | --------- | ------------------------------------------------------------------- |
| `admin` | `admin123` | `ADMIN`   | Global booking oversight, resource provisioning and user inspection |
| `STU01` | `stu123`   | `STUDENT` | Resource exploration, booking and personal cancellation             |
| `FAC01` | `fac123`   | `FACULTY` | Extended booking and departmental reservations                      |

> **Note:** These credentials are intended for demonstration/testing purposes. They should not be used in a production deployment.

---

## 🖥️ Interactive CLI Walkthrough

### 1. Authentication

```text
==================================================
   SMART CAMPUS RESOURCE & LAB SLOT BOOKING SYSTEM
==================================================
1. Login
2. Register Student Account
3. Exit
Select Option: 1

Enter User ID: STU01
Enter Password: ******

[✓] Login successful.
Welcome Grace Hopper (Role: STUDENT)
```

### 2. Resource & Slot Selection

```text
----------------- MAIN MENU -----------------
1. View Available Resources (Labs & Halls)
2. Book a Resource Slot
3. View My Active Bookings
4. Cancel a Booking
0. Logout

Select Option: 2

============== Registered Resources ==============
ID         Name                           Category     Capacity
----------------------------------------------------------------
LAB-101    High-Performance Compute Lab   Lab          40
LAB-102    Embedded Systems Lab           Lab          30
SEM-201    Auditorium Hall A              Hall         150

Enter Resource ID to reserve: LAB-101
Enter Date (YYYY-MM-DD): 2026-10-15

Available Standard Time Slots:
1. 09:00 - 11:00 [AVAILABLE]
2. 11:00 - 13:00 [AVAILABLE]
3. 14:00 - 16:00 [OCCUPIED]
4. 16:00 - 18:00 [AVAILABLE]

Select slot number (1-4): 3

[!] Conflict: Slot 14:00 - 16:00 on 2026-10-15
    is already booked for LAB-101
```

### 3. Successful Reservation

```text
Select slot number (1-4): 1

[✓] SUCCESS: Reservation Confirmed!

    Booking ID : BKG-8F2D1A
    Resource   : LAB-101
    Date       : 2026-10-15
    Time Slot  : 09:00 - 11:00
    User       : STU01
```

---

## 🧪 Automated Testing

Run:

```bash
java -ea -cp bin com.campus.booking.Main --test
```

### Execution Trace

```text
Running automated assertion tests...

[PASS] Cryptographic Hash Test
[PASS] Slot Availability Test
[PASS] Slot Lockout Test
[PASS] Slot Cancellation Test

All automated test assertions passed.
```

### Test Matrix

| Test ID | Component       | Target Method                      | Validation                                   |
| ------- | --------------- | ---------------------------------- | -------------------------------------------- |
| TC-01   | Cryptography    | `AuthService.hashPassword()`       | Validates deterministic SHA-256 hashing      |
| TC-02   | Availability    | `BookingService.isSlotAvailable()` | Confirms unreserved slots are available      |
| TC-03   | Collision Guard | `BookingService.createBooking()`   | Rejects duplicate reservations               |
| TC-04   | Slot Recovery   | `BookingService.cancelBooking()`   | Restores cancelled slots to available status |

---

## 🧠 Design Decisions

### 1. Headless CLI Architecture

A CLI eliminates GUI and desktop-library dependencies while keeping the application lightweight and suitable for terminal, SSH and container-based environments.

### 2. CSV-Based Persistence

CSV persistence avoids database-server setup, JDBC configuration and external database dependencies while keeping application data human-readable.

### 3. SHA-256 Password Hashing

Passwords are hashed before being stored rather than persisted as plaintext. The implementation uses Java's native `MessageDigest` API.

> For a production authentication system, a dedicated password-hashing/KDF such as Argon2id, bcrypt or scrypt with unique salts would generally be preferable to a raw SHA-256 digest.

### 4. Synchronized Persistence

`DataStore.java` uses synchronization around persistence operations to reduce race conditions and prevent inconsistent concurrent file writes.

### 5. Separation of Concerns

Authentication, booking logic, domain entities, presentation and persistence are separated into dedicated classes, making the system easier to maintain and extend.

---

## 🔧 Troubleshooting & FAQs

### `javac` returns `zsh: no matches found` or file-not-found errors

Make sure you are inside the project root:

```bash
cd campus-booking
```

Then run:

```bash
javac -d bin src/com/campus/booking/*.java
```

### VS Code displays `Incorrect Package` warnings

Configure the Java source path in:

```text
.vscode/settings.json
```

with:

```json
{
  "java.project.sourcePaths": ["src"]
}
```

### Changes to `users.csv` are not taking effect

Restart the CLI application after manually modifying CSV files so that the updated records are reloaded.

---

## 📌 Project Highlights

| Area                  | Implementation                     |
| --------------------- | ---------------------------------- |
| Language              | Java                               |
| Architecture          | Four-Tier / Separation of Concerns |
| Interface             | Command-Line Interface             |
| Authentication        | SHA-256 Password Hashing           |
| Authorization         | Role-Based Access Control          |
| Persistence           | Synchronized CSV Files             |
| Conflict Detection    | Deterministic Slot Validation      |
| Testing               | Java Assertions                    |
| External Dependencies | None                               |
| Supported JDK         | 11+                                |
| Tested JDK            | OpenJDK 17 LTS                     |

---

## 📄 Project Documentation

Additional project documentation is available in:

* `statement.md` — Problem definition and requirements
* `composite-history.md` — Engineering history and milestones
* `report.html` — Academic project report

---

## 👨‍💻 Project Status

**Status:** Functional Academic / Demonstration Project

The current implementation focuses on core Java architecture, authentication, resource management, booking conflict prevention, CSV persistence and automated assertion testing.
