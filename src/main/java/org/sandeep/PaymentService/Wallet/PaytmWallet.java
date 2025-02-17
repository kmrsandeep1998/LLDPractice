package org.sandeep.PaymentService.Wallet;

public class PaytmWallet extends Wallet {
    public PaytmWallet(String walletId, String userName) {
        super(walletId, userName);  // ✅ Only pass two arguments
    }

    @Override
    public void pay() {
        System.out.println("Making payment via Paytm Wallet: " + walletId);
    }
}

