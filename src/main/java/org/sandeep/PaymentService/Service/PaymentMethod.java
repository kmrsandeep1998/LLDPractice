package org.sandeep.PaymentService.Service;

public interface PaymentMethod {
    void pay();
    String getDetails(); // Get necessary details for storage
}
