package org.sandeep.PaymentService.UPI;

import org.sandeep.PaymentService.Service.PaymentMethod;

public abstract class UPI implements PaymentMethod {
    protected String upiId;
    protected String userName;

    public UPI(String upiId, String userName) {
        this.upiId = upiId;
        this.userName = userName;
    }

    @Override
    public String getDetails() {
        return upiId;  // ✅ Returns UPI ID
    }
}

