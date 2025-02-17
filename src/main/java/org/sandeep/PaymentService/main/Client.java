package org.sandeep.PaymentService.main;

import org.sandeep.PaymentService.Card.CreditCard;
import org.sandeep.PaymentService.Card.DebitCard;
import org.sandeep.PaymentService.Service.PaymentService;
import org.sandeep.PaymentService.UPI.GooglePay;
import org.sandeep.PaymentService.UPI.PhonePe;
import org.sandeep.PaymentService.Wallet.PaytmWallet;
import org.sandeep.PaymentService.Wallet.AmazonPayWallet;

public class Client {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        PaymentService ps = PaymentService.getInstance();

        // ✅ Add Users First
        ps.addUser("Sandeep", "sandeep@example.com");
        ps.addUser("John", "john@example.com");

        // ✅ Adding Credit Cards
        ps.addPaymentMethod("sandeepCreditCardId", new CreditCard("1234-5678-9012-3456", "Sandeep"), "sandeep@example.com");
        ps.addPaymentMethod("JohnCredit", new CreditCard("9876-5432-1098-7654", "John"), "john@example.com");

        // ✅ Making payments with Credit Cards (Now includes amount)
        ps.makePayment("JohnCredit", 100.50);
        ps.makePayment("sandeepCreditCardId", 200.00);

        // ✅ Adding Debit Card
        ps.addPaymentMethod("sgDebitCardId", new DebitCard("5678-9012-3456-7890", "Sandeep"), "sandeep@example.com");
        ps.makePayment("sgDebitCardId", 150.75);

        // ✅ Adding UPI Payments
        ps.addPaymentMethod("SgUPIID", new PhonePe("sg@okhdfcbank", "Sandeep"), "sandeep@example.com");
        ps.addPaymentMethod("JohnUPI", new GooglePay("john@upi", "John"), "john@example.com");
        ps.makePayment("SgUPIID", 50.25);
        ps.makePayment("JohnUPI", 75.00);

        // ✅ Adding Wallet Payments
        ps.addPaymentMethod("SandeepPaytmWallet", new PaytmWallet("paytm1234", "Sandeep"), "sandeep@example.com");
        ps.makePayment("SandeepPaytmWallet", 30.00);

        ps.addPaymentMethod("JohnAmazonWallet", new AmazonPayWallet("amazon9876", "John"), "john@example.com");
        ps.makePayment("JohnAmazonWallet", 120.00);
    }
}
