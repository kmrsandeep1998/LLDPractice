package org.sandeep.PaymentService.Card;

import org.sandeep.PaymentService.Service.PaymentMethod;

public abstract class Card implements PaymentMethod {
    protected String cardNo;
    protected String userName;

    public Card(String cardNo, String userName) {
        this.cardNo = cardNo;
        this.userName = userName;
    }

    public String getCardNo() {
        return cardNo;
    }

    @Override
    public String getDetails() {
        return cardNo;  // ✅ Returns the card number
    }
}
