package it.polito.ezshop.data;



public class TicketEntryClass implements TicketEntry {

    private ProductType product = new ProductTypeClass();
    private int quantity;
    private double discountRate;

    @Override
    public String getBarCode() {
        return product.getBarCode();
    }

    @Override
    public void setBarCode(String barCode) {
        product.setBarCode(barCode);
    }

    @Override
    public String getProductDescription() {
        return product.getProductDescription();
    }

    @Override
    public void setProductDescription(String productDescription) {
        product.setProductDescription(productDescription);
    }

    @Override
    public int getAmount() {
        return this.quantity;
    }

    @Override
    public void setAmount(int amount) {
        if (amount >= 0) {
            this.quantity = amount;
        }
    }

    @Override
    public double getPricePerUnit() {
        return product.getPricePerUnit();
    }

    @Override
    public void setPricePerUnit(double pricePerUnit){
        product.setPricePerUnit( pricePerUnit);
    }

    @Override
    public double getDiscountRate() {
        return this.discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        if (!(discountRate < 0 || discountRate > 1.0)) {
            this.discountRate = discountRate;
        }
    }
}
