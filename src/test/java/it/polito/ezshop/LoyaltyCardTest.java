package it.polito.ezshop;



import static org.junit.Assert.*;
import org.junit.Test;
import it.polito.ezshop.data.LoyaltyCard;


public class LoyaltyCardTest {

    @Test
    public void testInvalidCardNumber(){
        String expectedCardNumber = "123";
        LoyaltyCard c = new LoyaltyCard();
        c.setCardNumber(expectedCardNumber);
        String cardNumber = c.getCardNumber();
        assertNotEquals(expectedCardNumber,  cardNumber );
        expectedCardNumber = "0123456789";
        c.setCardNumber(expectedCardNumber);
        cardNumber = c.getCardNumber();
        assertEquals(expectedCardNumber,  cardNumber );
        expectedCardNumber = null;
        c.setCardNumber(expectedCardNumber);
        cardNumber = c.getCardNumber();
        assertNotEquals(expectedCardNumber,  cardNumber );
    }

    @Test
    public void testInvalidPoints(){
        int expectedPoints = 5;
        LoyaltyCard c = new LoyaltyCard();
        c.setPoints(expectedPoints);
        int points;
        points = c.getPoints();
        assertEquals(expectedPoints,  points);
        expectedPoints = -1;
        c.setPoints(expectedPoints);
        points = c.getPoints();
        assertNotEquals(expectedPoints, points);
    }

    @Test
    public void testInvalidLoyaltyCardConstructor(){
        String expectedCardNumber = "123";
        int expectedPoints = 5;
        LoyaltyCard c = new LoyaltyCard(expectedCardNumber, expectedPoints);
        int points = c.getPoints();
        String cardNumber = c.getCardNumber();
        assertNotEquals(expectedCardNumber,  cardNumber);
        assertEquals(expectedPoints,  points);
    }
}
