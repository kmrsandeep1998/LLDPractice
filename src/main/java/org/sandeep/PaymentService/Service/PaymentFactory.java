package org.sandeep.PaymentService.Service;

import org.sandeep.PaymentService.Card.CreditCard;
import org.sandeep.PaymentService.Card.DebitCard;
import org.sandeep.PaymentService.Enum.PaymentType;
import org.sandeep.PaymentService.UPI.GooglePay;
import org.sandeep.PaymentService.Wallet.PaytmWallet;

public class PaymentFactory {
    public static PaymentMethod createPaymentMethod(PaymentType type, String details, String userName) {
        String[] data = details.split(":");

        return switch (type) {
            case CREDIT_CARD -> new CreditCard(data[1], userName);
            case DEBIT_CARD -> new DebitCard(data[1], userName);
            case UPI -> new GooglePay(data[1], userName);  // Default to Google Pay (or change dynamically)
            case WALLET -> new PaytmWallet(data[2], userName); // Default to Paytm Wallet
            default -> throw new IllegalArgumentException("Unknown Payment Type");
        };
    }
}

