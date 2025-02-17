package org.sandeep.PaymentService.UPI;

public class PhonePe extends UPI {
    public PhonePe(String upiId, String userName) {
        super(upiId, userName);
    }

    @Override
    public void pay() {
        System.out.println("Making payment via PhonePe: " + upiId);
    }
}

