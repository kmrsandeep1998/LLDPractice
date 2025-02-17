package org.sandeep.PaymentService.UPI;

public class GooglePay extends UPI {
    public GooglePay(String upiId, String userName) {
        super(upiId, userName);
    }

    @Override
    public void pay() {
        System.out.println("Making payment via Google Pay: " + upiId);
    }
}
