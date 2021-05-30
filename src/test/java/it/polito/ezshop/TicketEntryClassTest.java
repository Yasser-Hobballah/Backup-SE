package it.polito.ezshop;

import it.polito.ezshop.data.TicketEntryClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TicketEntryClassTest {

    @Test
    public void testInvalidAmount(){
        int expectedAmount = 5;
        TicketEntryClass t = new TicketEntryClass();
        t.setAmount(expectedAmount);
        int amount = t.getAmount();
        assertEquals(expectedAmount,  amount);
        expectedAmount = -1;
        t.setAmount(expectedAmount);
        amount = t.getAmount();
        assertNotEquals(expectedAmount, amount);
    }

    @Test
    public void testInvalidDiscountRate(){
        double expectedDiscount = 0.5;
        TicketEntryClass t = new TicketEntryClass();
        t.setDiscountRate(expectedDiscount);
        double discount = t.getDiscountRate();
        assertEquals(expectedDiscount,  discount, 0.01);
        expectedDiscount = -1;
        t.setDiscountRate(expectedDiscount);
        discount = t.getAmount();
        assertNotEquals(expectedDiscount, discount);
        expectedDiscount = 1.1;
        t.setDiscountRate(expectedDiscount);
        discount = t.getAmount();
        assertNotEquals(expectedDiscount, discount);
    }
}
