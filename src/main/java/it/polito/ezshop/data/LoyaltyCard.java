package it.polito.ezshop.data;

public class LoyaltyCard {

    private String cardNumber;
    private Integer points;

    public LoyaltyCard(){}
    //constructor
    public LoyaltyCard(String cardNumber, Integer points) {

        if (points != null) {
            if (points >= 0) {
                this.points = points;
            } else {
                this.points = 0;
            }
        } else {
            this.points = 0;
        }
        if (!(cardNumber == null)) {
            if (cardNumber.length() == 10) {
                this.cardNumber=cardNumber;
            } else {
                this.cardNumber = "";
            }
        } else {
            this.cardNumber = "";
        }
        
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        if (points != null) {
            if (points >= 0) {
                this.points = points;
            }
        }
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        if (!(cardNumber == null)) {
            if (cardNumber.length() == 10) {
                this.cardNumber=cardNumber;
            }
        }
    }


}
