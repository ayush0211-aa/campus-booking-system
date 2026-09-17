# Project Statement: Smart Campus Resource & Lab Slot Booking System

## 1. Problem Statement
University campus facilities—such as high-performance computing labs, specialized hardware workstations, and seminar halls—frequently experience scheduling overlaps, unauthorized access, and lack of reservation transparency. Paper logs and uncoordinated verbal requests lead to unresolvable booking collisions, underutilized infrastructure, and zero audit accountability.

## 2. Scope of the Project
This system delivers an automated, pure command-line interface (CLI) terminal application for resource scheduling. It provides role-based authentication, maintains campus equipment and lab inventories, detects time-slot collisions before confirmation, and persists operational records through deterministic flat-file storage without external server overhead.

## 3. Target Users
- **Students**: Reserve slots for coursework, project simulations, and academic study groups.
- **Faculty**: Reserve departmental computing labs, specialized equipment, and lecture halls for classes or workshops.
- **Administrators**: Register infrastructure assets, inspect global reservation registries, and perform administrative booking overrides.

## 4. High-Level Features
- **Role-Based Access Control (RBAC)**: Secure access gating for Student, Faculty, and Admin personas using cryptographic password hashing (SHA-256).
- **Resource Inventory Management**: Full operational tracking of campus labs and halls, detailing unique room codes, functional names, categories, and seating capacities.
- **Collision-Free Reservation Engine**: Time-slot verification ensuring no overlapping bookings can occur for the same resource on a given date.
- **Audit Logging & Self-Service Cancellation**: Historical transaction logging with self-service cancellation for students and global oversight for administrators.
- **Zero-Dependency Flat-File Persistence**: Automated initialization and synchronization of state across local CSV data stores.