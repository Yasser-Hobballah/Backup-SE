package it.polito.ezshop.data;

public class CustomerClass implements Customer {

    private String Name;
    private Integer id;
    private LoyaltyCard card;

    public CustomerClass() {
    }

    public CustomerClass(String name, Integer id, String cardNumber, Integer points) {
        this.Name = name;
        this.id = id;
        this.card = new LoyaltyCard(cardNumber, points);
    }


    @java.lang.Override
    public String getCustomerName() {
        return this.Name;
    }

    @java.lang.Override
    public void setCustomerName(String customerName) {

        if (!(customerName == null)) {
            if (!(customerName.isEmpty())) {
                this.Name = customerName;
            }
        }
    }

    @java.lang.Override
    public String getCustomerCard() {
        return card.getCardNumber();
    }

    // also create a new Card
    @java.lang.Override
    public void setCustomerCard(String customerCard) {

        if (!(customerCard == null)) {
            if (customerCard.length() == 10) {
                card = new LoyaltyCard(customerCard, 0);
            }
        }
    }

    @java.lang.Override
    public Integer getId() {
        return this.id;
    }

    @java.lang.Override
    public void setId(Integer id) {

        if (!(id == null)) {
            if (!(id <= 0)) {
                this.id = id;
            }
        }
    }

    @java.lang.Override
    public Integer getPoints() {
        return card.getPoints();
    }

    @java.lang.Override
    public void setPoints(Integer points) {
        card.setPoints(points);
    }
}
