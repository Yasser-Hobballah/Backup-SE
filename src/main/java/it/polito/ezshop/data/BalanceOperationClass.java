package it.polito.ezshop.data;

import java.time.LocalDate;

public class BalanceOperationClass implements BalanceOperation {
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;

    public BalanceOperationClass() {
    }

    public BalanceOperationClass(int balanceId, LocalDate date, double money, String type) {
        this.balanceId = balanceId;
        this.date = date ;
        this.money = money;
        this.type = type;
    }

    @Override
    public int getBalanceId() {
        return balanceId;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setBalanceId(int balanceId) {
        
            if (!(balanceId < 0)) {
                this.balanceId = balanceId;
            }
        

    }

    @Override
    public void setDate(LocalDate date) {
    
        this.date = date;
    
    }

    @Override
    public void setMoney(double money) {
   

        this.money=money;


    }

    @Override
    public void setType(String type) {
   
        if (!(type == null)) {
            if (!(type.isEmpty() ) && ( type.compareTo("CREDIT") == 0 || type.compareTo("DEBIT") == 0  || type.compareTo("SALE") == 0  || type.compareTo("ORDER") == 0   || type.compareTo("RETURN") == 0  ) ) {
                this.type=type;
            }
        }

    }

}
