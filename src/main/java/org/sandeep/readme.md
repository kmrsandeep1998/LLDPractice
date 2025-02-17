# Payment Service Database Schema

## Overview
This document provides a detailed description of the database schema for the Payment Service. The database consists of three main tables:
1. **users** - Stores user details.
2. **payment_methods** - Stores different payment methods linked to users.
3. **transactions** - Stores payment transactions made using registered payment methods.

---

## **1. users Table**
### **Schema Definition**
```sql
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
```
### **Description**
| Column   | Data Type      | Constraints               | Description                   |
|----------|--------------|--------------------------|-------------------------------|
| user_id  | INT          | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each user |
| name     | VARCHAR(255) | NOT NULL                 | Full name of the user        |
| email    | VARCHAR(255) | UNIQUE, NOT NULL         | Email of the user (must be unique) |

### **Example Data**
| user_id | name   | email                 |
|---------|--------|----------------------|
| 1       | Sandeep | sandeep@example.com |
| 2       | Alex   | alex@example.com    |

---

## **2. payment_methods Table**
### **Schema Definition**
```sql
CREATE TABLE IF NOT EXISTS payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    type ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'WALLET') NOT NULL,
    details TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
```
### **Description**
| Column       | Data Type      | Constraints                | Description                                      |
|-------------|--------------|---------------------------|--------------------------------------------------|
| id          | INT          | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each payment method       |
| user_id     | INT          | FOREIGN KEY (users.user_id) | Links payment method to a specific user        |
| name        | VARCHAR(255) | UNIQUE, NOT NULL          | Custom identifier for the payment method        |
| type        | ENUM         | NOT NULL                  | Payment method type (CREDIT_CARD, DEBIT_CARD, UPI, WALLET) |
| details     | TEXT         | NOT NULL                  | Stores sensitive details (Card No, UPI ID, Wallet ID) |

### **Example Data**
| id | user_id | name               | type       | details               |
|----|---------|--------------------|------------|------------------------|
| 1  | 1       | sandeepCreditCard  | CREDIT_CARD | 1234-5678-9012-3456    |
| 2  | 1       | sandeepPhonePe     | UPI        | sandeep@ybl           |
| 3  | 2       | alexPaytmWallet    | WALLET     | alex_paytm_wallet_001 |

---

## **3. transactions Table**
### **Schema Definition**
```sql
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    payment_method_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE CASCADE
);
```
### **Description**
| Column            | Data Type      | Constraints                  | Description                                    |
|------------------|--------------|-----------------------------|------------------------------------------------|
| id               | INT          | PRIMARY KEY, AUTO_INCREMENT | Unique identifier for each transaction       |
| payment_method_id | INT          | FOREIGN KEY (payment_methods.id) | Links transaction to a specific payment method |
| amount          | DECIMAL(10,2) | NOT NULL                    | Transaction amount                            |
| status         | VARCHAR(50)   | NOT NULL                    | Payment status (e.g., SUCCESS, FAILED, PENDING) |
| created_at     | TIMESTAMP     | DEFAULT CURRENT_TIMESTAMP   | Timestamp of when the transaction was made   |

### **Example Data**
| id | payment_method_id | amount  | status  | created_at             |
|----|-------------------|--------|---------|-------------------------|
| 1  | 1                 | 500.00 | SUCCESS | 2025-02-16 10:00:00     |
| 2  | 3                 | 1000.50 | PENDING | 2025-02-16 11:30:00     |

---

## **Relationships Between Tables**
- **`users.user_id` → `payment_methods.user_id`** (One-to-Many):
    - A user can have multiple payment methods.
- **`payment_methods.id` → `transactions.payment_method_id`** (One-to-Many):
    - A payment method can have multiple transactions.

### **ER Diagram Representation**
```
  Users (user_id) ────> Payment Methods (id) ────> Transactions (payment_method_id)
```

---

## **Constraints & Business Rules**
1. **A user must exist before adding a payment method.**
    - Enforced using `FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE`.
2. **A payment method must exist before making a transaction.**
    - Enforced using `FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE CASCADE`.
3. **Unique Constraints:**
    - `email` in `users` table must be unique.
    - `name` in `payment_methods` table must be unique per user.
4. **Automatic Timestamping:**
    - Transactions are automatically timestamped (`created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`).
5. **Cascade Deletion:**
    - If a **user** is deleted, all their **payment methods** and **transactions** are also deleted.
    - If a **payment method** is deleted, all related **transactions** are also deleted.

---

## **Conclusion**
This schema is designed to support a robust **payment management system** where users can register multiple payment methods and track their transactions efficiently. The schema ensures **data integrity**, **referential consistency**, and **flexibility** for future extensions (e.g., adding more payment types).

---

### **Future Enhancements**
- Add a `currency` column to `transactions`.
- Store hashed/encrypted payment details for security.
- Implement soft deletes using a `deleted_at` timestamp.

---

📌 **Last Updated:** February 16, 2025

