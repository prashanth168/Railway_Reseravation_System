# Railway_Reseravation_System
# 🚆 Railway Reservation System

A complete Java-based Railway Reservation System with MySQL database, Swing GUI, and full booking workflow.

## Features

✔ **User Management**  
- User registration & authentication
- Role-based access (Admin/Customer)
- Profile management

✔ **Train Operations**  
- CRUD operations for trains
- Seat availability tracking
- Route management

✔ **Booking System**  
- Train search & seat selection
- Multi-passenger booking
- PDF ticket generation

✔ **Payment Processing**  
- Secure payment gateway integration
- Refund handling
- Transaction records

✔ **Admin Controls**  
- Booking management
- Cancellation processing
- Revenue reports

## Technology Stack

**Backend**:
- Java 11+
- MySQL 8.0
- JDBC for database connectivity

**Frontend**:
- Java Swing GUI
- JFreeChart for reports
- iTextPDF for tickets

**Utilities**:
- Password hashing (SHA-256)
- Email notifications
- PDF generation

## Installation Guide

### Prerequisites
- Java JDK 11+
- MySQL Server 8.0
- Maven (for dependency management)

### Setup Steps

1. **Database Setup**:
```sql
CREATE DATABASE railway_reservation_system;
USE railway_reservation_system;
-- Run schema.sql from resources folder
