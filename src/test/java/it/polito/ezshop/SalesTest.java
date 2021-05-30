package it.polito.ezshop;

import static org.junit.Assert.*;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.*;
import org.junit.Test;

public class SalesTest {

    @Test
    public void StartSaleTransactionTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Simone", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertTrue(EZ.startSaleTransaction() > 0);
    }

    @Test
    public void applyDiscountRateToSaleTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidDiscountRateException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Simone", "123"); // in order to manage UnAuthorized Access and fill the user Class
        int id = EZ.startSaleTransaction();

        assertTrue(EZ.applyDiscountRateToSale(id, 0.5));
        assertFalse(EZ.applyDiscountRateToSale(id+100, 0.5));
        assertThrows(InvalidDiscountRateException.class, () -> {
            EZ.applyDiscountRateToSale(id, -1);
        });

    }

    @Test
    public void addProductToSaleTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test00", "08123456789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "599-751-111");
            EZ.updateQuantity(p, 100000);
        }
        //valid
        assertTrue(EZ.addProductToSale(id, "08123456789123", 3));
        //id not found
        assertFalse(EZ.addProductToSale(id+75015, "08123456789123", 3));
        //product not found
        assertFalse(EZ.addProductToSale(id, "00001111110000", 3));
        //amount greater then available (3 already added)
        assertFalse(EZ.addProductToSale(id, "08123456789123", 100000));

    }
    @Test
    public void deleteProductFromSaleTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test0001", "08123456789003", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "799-751-777");
            EZ.updateQuantity(p, 100000);
        }

        EZ.addProductToSale(id, "08123456789003", 3);

        //ID error
        assertFalse(EZ.deleteProductFromSale(id+5000, "08123456789003", 2));
        //productCode error
        assertFalse(EZ.deleteProductFromSale(id, "88123456789003", 2));
        //Amount error
        assertFalse(EZ.deleteProductFromSale(id, "08123456789003", 900));
        //valid
        assertTrue(EZ.deleteProductFromSale(id, "08123456789003", 2));

    }

    @Test
    public void receiveCashPaymentTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("test", "0123456789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "555-111-111");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0123456789123", 3);
        EZ.endSaleTransaction(id);
        assertTrue(EZ.receiveCashPayment(id, 5) >= 0);

        //NOT ENOUGH CASH
        int p2 = EZ.createProductType("test2", "0223456789123", 1.0, "note");
        int id2 = EZ.startSaleTransaction();
        if (p2>0) {
            EZ.updatePosition(p2, "112-121-111");
            EZ.updateQuantity(p2, 10000);
        }
        EZ.addProductToSale(id2, "0223456789123", 3);
        EZ.endSaleTransaction(id2);
        assertThrows(InvalidPaymentException.class, () -> {
            EZ.receiveCashPayment(id2, -1);
        });
        assertTrue(EZ.receiveCashPayment(id2, 1) < 0);


    }

    @Test
    public void endSaleTransactionTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("test88", "0153486789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "555-151-101");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0153486789123", 3);

        //transaction does not exists
        assertFalse(EZ.endSaleTransaction(id+1700));

        //transaction closed
        assertTrue(EZ.endSaleTransaction(id));

        //transaction already closed
        assertFalse(EZ.endSaleTransaction(id));


        //just to rerun the test
        EZ.deleteSaleTransaction(id);

    }

    @Test
    public void receiveCreditCardPaymentTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException, InvalidCreditCardException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("test3", "0153456789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "555-151-111");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0153456789123", 3);
        EZ.endSaleTransaction(id);
        //INVALID CREDIT CARD NUMBER
        assertThrows(InvalidCreditCardException.class, () -> {
            EZ.receiveCreditCardPayment(id, "4485370086510892");
        });
        //CREDIT CARD WITH NO MONEY
        assertFalse(EZ.receiveCreditCardPayment(id, "4716258050958645"));
        //CREDIT CARD WITH SUFFICIENT MONEY
        assertTrue(EZ.receiveCreditCardPayment(id, "4485370086510891"));

    }

    @Test
    public void deleteSaleTransactionTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test98", "0153486119123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "523-151-101");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0153486119123", 3);

        EZ.endSaleTransaction(id);

        //transaction not found
        assertFalse(EZ.deleteSaleTransaction(id+45000));

        //deleted
        assertTrue(EZ.deleteSaleTransaction(id));

        int id2 = EZ.startSaleTransaction();
        EZ.addProductToSale(id2, "0153486119123", 3);
        EZ.endSaleTransaction(id2);
        EZ.receiveCashPayment(id2, 150);

        //transaction payed
        assertFalse(EZ.deleteSaleTransaction(id2));

    }

    @Test
    public void applyDiscountRateToProductTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidTransactionIdException, InvalidDiscountRateException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test5", "0123956089123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "555-121-117");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0123956089123", 3);
        //INVALID DISCOUNT RATE
        assertThrows(InvalidDiscountRateException.class, () -> {
            EZ.applyDiscountRateToProduct(id, "0123956089123", -1);
        });
        //INVALID ID
        assertFalse(EZ.applyDiscountRateToProduct(id+10000, "0123956089123", 0.5 ));

        //INVALID BARCODE
        assertFalse(EZ.applyDiscountRateToProduct(id, "0173956089123", 0.5 ));

        //VALID TRANSACTION
        assertTrue(EZ.applyDiscountRateToProduct(id, "0123956089123", 0.5 ));

    }

    @Test
    public void computePointForSaleTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        int id = EZ.startSaleTransaction();
        int p = EZ.createProductType("test6", "01239560891231", 8.0, "note");
        if (p > 0) {
            EZ.updatePosition(p, "155-121-117");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "01239560891231", 5);

        //40 euro = 4 points
        assertEquals(4, EZ.computePointsForSale(id));

    }

    @Test
    public void getSaleTransactionTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("test1010", "0153486117533", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "503-151-444");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0153486117533", 3);

        EZ.endSaleTransaction(id);

        SaleTransaction s = EZ.getSaleTransaction(id);
        //same ID
        assertEquals((int) s.getTicketNumber(), id);
        //same price
        assertEquals((double) s.getPrice(), 3.0, 0.01);
        //same discount rate
        assertEquals((double) s.getDiscountRate(), 0.0, 0.01);
        //check for 1 entry
        assertEquals(s.getEntries().size(), 1);
        //if is the right product
        assertEquals(s.getEntries().get(0).getProductDescription(), "test1010");
        //if is the right amount
        assertEquals(s.getEntries().get(0).getAmount(), 3);

    }

}
