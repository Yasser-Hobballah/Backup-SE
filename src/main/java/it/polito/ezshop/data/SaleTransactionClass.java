package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidDiscountRateException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;

import java.util.List;

public class SaleTransactionClass implements SaleTransaction{

    private Integer transactionID;
    List<TicketEntry> entries;
    double discountRate;
    double price;

    @Override
    public Integer getTicketNumber() {
        return transactionID;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        try {
            if (ticketNumber <= 0 || ticketNumber == null) {    //why is never null?
                throw new InvalidTransactionIdException();
            } else {
                this.transactionID = ticketNumber;
            }
        } catch (InvalidTransactionIdException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public List<TicketEntry> getEntries() {
        return this.entries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public double getDiscountRate() {
        return this.discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate){
        try {
            if (discountRate < 0 || discountRate > 1.0) {
                throw new InvalidDiscountRateException();
            } else {
                this.discountRate = discountRate;
            }
        } catch (InvalidDiscountRateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }
}
