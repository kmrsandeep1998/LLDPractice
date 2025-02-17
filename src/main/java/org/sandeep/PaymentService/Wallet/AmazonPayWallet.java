package org.sandeep.PaymentService.Wallet;

public class AmazonPayWallet extends Wallet {
    public AmazonPayWallet(String walletId, String userName) {
        super(walletId, userName);
    }

    @Override
    public void pay() {
        System.out.println("Making payment via Amazon Pay Wallet: " + walletId);
    }
}
