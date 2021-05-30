package it.polito.ezshop.data;

public class OrderClass implements Order{

    private Integer balanceId;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;
    private Integer orderId;

    public OrderClass() {}
    public OrderClass(Integer balanceId, String productCode, double pricePerUnit, int quantity, String status, Integer orderId) {
        this.balanceId = balanceId;
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.status = status;
        this.orderId = orderId;
    }
    public OrderClass(String productCode, double pricePerUnit, int quantity) {
        this.productCode = productCode;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

    @Override
    public Integer getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        if(balanceId>0)
            this.balanceId = balanceId;
    }

    @Override
    public String getProductCode() {
        return productCode;
    }

    @Override
    public void setProductCode(String productCode){
        if (!(productCode == null)) {
            if (!(productCode.isEmpty())) {
                this.productCode = productCode;
            }
        }
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit){

        if(pricePerUnit>0.0)
            this.pricePerUnit = pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        if(quantity>0)
            this.quantity = quantity;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        if (!(status == null)) {
            if (!(status.isEmpty())) {
                this.status = status;
            }
        }
    }

    @Override
    public Integer getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(Integer orderId) {
        if(orderId != null)
            if (orderId > 0)
                this.orderId = orderId;
    }

}
