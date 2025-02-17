package org.sandeep.PaymentService.Wallet;

import org.sandeep.PaymentService.Service.PaymentMethod;

public abstract class Wallet implements PaymentMethod {
    protected String walletId;
    protected String userName;

    public Wallet(String walletId, String userName) {
        this.walletId = walletId;
        this.userName = userName;
    }


    @Override
    public String getDetails() {
        return walletId;  // ✅ Returns Wallet ID
    }
}

