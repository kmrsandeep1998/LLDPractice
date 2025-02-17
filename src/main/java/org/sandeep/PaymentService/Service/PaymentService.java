package org.sandeep.PaymentService.Service;

import org.sandeep.PaymentService.Card.CreditCard;
import org.sandeep.PaymentService.Card.DebitCard;
import org.sandeep.PaymentService.Enum.PaymentType;
import org.sandeep.PaymentService.UPI.GooglePay;
import org.sandeep.PaymentService.UPI.PhonePe;
import org.sandeep.PaymentService.Wallet.AmazonPayWallet;
import org.sandeep.PaymentService.Wallet.PaytmWallet;

import java.sql.*;

public class PaymentService {
    private static PaymentService instance;
    private Connection connection;

    // Singleton Constructor
    private PaymentService() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdatabase", "root", "AdminSandeep");
            createTableIfNotExists();
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed!", e);
        }
    }

    public static synchronized PaymentService getInstance() {
        if (instance == null) {
            instance = new PaymentService();
        }
        return instance;
    }

    // Ensures Table Exists
    private void createTableIfNotExists() {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS payment_methods (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) UNIQUE NOT NULL, " +
                    "type ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI') NOT NULL, " +
                    "details TEXT NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(String name, String email) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (name, email) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=VALUES(name)"
            );
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPaymentMethod(String name, PaymentMethod pm, String email) {
        try {
            PreparedStatement userStmt = connection.prepareStatement(
                    "SELECT user_id FROM users WHERE email = ?"
            );
            userStmt.setString(1, email);
            ResultSet userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                throw new IllegalArgumentException("User not found: " + email);
            }
            int userId = userRs.getInt("user_id");

            // ✅ Correctly map Java class names to ENUM values
            String type = getString(pm);

            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO payment_methods (user_id, name, type, details) " +
                            "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE type=VALUES(type), details=VALUES(details)"
            );
            stmt.setInt(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, type);  // ✅ Now using correct ENUM value
            stmt.setString(4, pm.getDetails());
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getString(PaymentMethod pm) {
        String type;
        if (pm instanceof CreditCard) {
            type = "CREDIT_CARD";
        } else if (pm instanceof DebitCard) {
            type = "DEBIT_CARD";
        } else if (pm instanceof PhonePe || pm instanceof GooglePay) {
            type = "UPI";
        } else if (pm instanceof PaytmWallet || pm instanceof AmazonPayWallet) {
            type = "WALLET";
        } else {
            throw new IllegalArgumentException("Unknown payment method type: " + pm.getClass().getSimpleName());
        }
        return type;
    }

    private PaymentMethod createPaymentMethod(PaymentType type, String details, String userName) {
        switch (type) {
            case CREDIT_CARD:
                return new CreditCard(details, userName);
            case DEBIT_CARD:
                return new DebitCard(details, userName);
            case UPI:
                if (details.contains("@okhdfcbank")) {
                    return new GooglePay(details, userName);
                } else if (details.contains("@ybl")) {
                    return new PhonePe(details, userName);
                }
                throw new IllegalArgumentException("Unknown UPI Provider");
            case WALLET:
                if (details.contains("paytm")) {
                    return new PaytmWallet(details, userName);
                } else if (details.contains("amazon")) {
                    return new AmazonPayWallet(details, userName);
                }
                throw new IllegalArgumentException("Unknown Wallet Provider");
            default:
                throw new IllegalArgumentException("Unknown Payment Type");
        }
    }

    // Retrieve and Make Payment
    public void makePayment(String name, double amount) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT id, type, details FROM payment_methods WHERE name = ?"
            );
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int paymentMethodId = rs.getInt("id");
                PaymentType type = PaymentType.valueOf(rs.getString("type"));
                String details = rs.getString("details");

                // Use Factory Method to create the correct instance
                PaymentMethod pm = createPaymentMethod(type, details, name);

                // Execute payment
                pm.pay();

                // Log the transaction in DB
                PreparedStatement logStmt = connection.prepareStatement(
                        "INSERT INTO transactions (payment_method_id, amount, status) VALUES (?, ?, ?)"
                );
                logStmt.setInt(1, paymentMethodId);
                logStmt.setDouble(2, amount);
                logStmt.setString(3, "SUCCESS");
                logStmt.executeUpdate();
            } else {
                throw new IllegalArgumentException("Payment method not found: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
