# Composite Project & Development History

**Project:** Smart Campus Resource & Lab Slot Booking System  
**Author:** Ayush Arora  
**Repository:** https://github.com/ayush0211-aa/campus-booking-system  
**Date:** September 17, 2026  
**Language / Environment:** Core Java 17, POSIX Terminal, VS Code  

---

## 1. Project Inception & Problem Scoping
* **Context**: Academic computing facilities and seminar halls experienced uncoordinated scheduling overlaps and paper-based booking conflicts.
* **Objective**: Construct a headless, pure Core Java CLI system with role-based access control, SHA-256 credential hashing, deterministic conflict detection, and flat-file CSV storage.
* **Deliverables Defined**:
  * 10 modular Core Java source files (`com.campus.booking` package).
  * Unit test assertion harness (`--test`).
  * Structured documentation (`statement.md`, `README.md`, `composite-history.md`).
  * 15-Section formal technical project report.

---

## 2. Iterative Development & Engineering Milestones

### Phase 1: Problem Definition & Documentation Setup
* Authored `statement.md` defining core scope, user roles (`STUDENT`, `FACULTY`, `ADMIN`), and non-functional requirements.
* Initialized project directory structure under standard Maven/Java convention: `src/com/campus/booking/`.
* Added `.gitignore` to omit compiled `.class` binaries, `bin/` directories, and OS metadata files.

### Phase 2: Domain Entity Modeling
* **`Role.java`**: Implemented type-safe enumeration for role-based authorization scopes.
* **`User.java`**: Defined user model with SHA-256 password hash encapsulation and CSV (de)serialization logic.
* **`Resource.java`**: Built model encapsulating campus infrastructure (lab/hall, code, seating capacity).
* **`Booking.java`**: Created reservation lifecycle entity tracking booking state (`CONFIRMED`, `CANCELLED`), timestamps, and user associations.

### Phase 3: Storage & Service Layer Implementation
* **`DataStore.java`**: Implemented thread-safe CSV persistence engine using `BufferedReader`/`BufferedWriter` with automated root folder and initial seed data creation (`data/users.csv`, `data/resources.csv`, `data/bookings.csv`).
* **`AuthService.java`**: Implemented password security routines using standard `java.security.MessageDigest` (SHA-256) and user session retrieval.
* **`BookingService.java`**: Developed collision prevention logic using Java Streams to enforce unique (resource + date + timeslot) combinations.
* **`SlotUnavailableException.java` & `AuthenticationException.java`**: Custom checked/runtime exception types for application error handling.

### Phase 4: CLI Driver & Interactive Event Loop
* **`Main.java`**: Designed terminal REPL supporting ANSI color sequences, session state persistence, dynamic menu routing based on user permissions, and argument parsing.
* Built embedded test runner executing built-in assertion tests via `--test`.

### Phase 5: Debugging, Build & Toolchain Validation
* **Package Root Path Resolution**: Configured `.vscode/settings.json` with `"java.project.sourcePaths": ["src"]` to synchronize language server indexing with the standard compilation root.
* **Terminal Directory Isolation**: Resolved shell execution contexts between user home directory (`~`) and workspace root (`campus-booking`).
* Executed end-to-end assertion testing via `java -ea -cp bin com.campus.booking.Main --test` with 100% test pass rate.

---

## 3. Git Version Control & Commit Audit Trail

| Commit Hash | Author | Scope | Commit Message |
| :--- | :--- | :--- | :--- |
| `0f2d84a` | Ayush Arora | `docs` | `docs: add project statement and system documentation` |
| `a916233` | Ayush Arora | `feat` | `feat: implement CLI booking engine and role-based auth` |
| `HEAD` | Ayush Arora | `docs` | `docs: add composite project and development history` |

---

## 4. Verification & Validation Summary

### Test Suite Execution Output
```text
Running automated assertion tests...
[PASS] Cryptographic Hash Test (SHA-256 digest validation)
[PASS] Slot Availability Test (Pre-booking check)
[PASS] Slot Lockout Test (Immediate conflict rejection)
[PASS] Slot Cancellation Test (Slot release post-cancellation)
All automated test assertions passed.
