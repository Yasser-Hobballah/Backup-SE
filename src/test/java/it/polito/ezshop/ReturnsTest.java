package it.polito.ezshop;

import static org.junit.Assert.*;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.*;
import org.junit.Test;

public class ReturnsTest {

    @Test
    public void startReturnTransactionTest () throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("testRet1", "9764319764310", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "777-155-311");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "9764319764310", 3);
        EZ.endSaleTransaction(id);

        //transaction not payed
        assertEquals(-1, (int) EZ.startReturnTransaction(id));

        EZ.receiveCashPayment(id, 5);

        //transaction payed
        assertNotEquals(-1, (int) EZ.startReturnTransaction(id));
    }

    @Test
    public void returnProductTest () throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("testRet3", "9000919764310", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "737-215-375");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "9000919764310", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);
        int retId = (int) EZ.startReturnTransaction(id);

        //transaction does not exists
        assertFalse(EZ.returnProduct(retId+50046, "9000919764310", 1));
        //productCode does not exists
        assertFalse(EZ.returnProduct(retId, "1111110111110", 1));

        int p2 = EZ.createProductType("testRet3.1", "9010919764310", 1.0, "note");
        //productCode not in transaction
        assertFalse(EZ.returnProduct(retId, "9010919764310", 1));

        //return success
        assertTrue(EZ.returnProduct(retId, "9000919764310", 2));

    }

    @Test
    public void endReturnTransactionTest () throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("testRet4", "9764944474310", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p > 0) {
            EZ.updatePosition(p, "734-115-311");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "9764944474310", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);
        int retId = (int) EZ.startReturnTransaction(id);
        EZ.returnProduct(retId, "9764944474310", 2);

        //Id not found
        assertFalse(EZ.endReturnTransaction(retId+77741, false));

        //rollback
        assertTrue(EZ.endReturnTransaction(retId, false));
        assertNull(EZ.getReturnTransaction(retId));

        //committed
        int retId2 = (int) EZ.startReturnTransaction(id);
        EZ.returnProduct(retId2, "9764944474310", 2);
        assertTrue(EZ.endReturnTransaction(retId2, true));
        assertTrue(EZ.getReturnTransaction(retId2).getTicketNumber() > 0);

    }

    @Test
    public void returnCashPaymentTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH TO HAVE A VALID TRANSACTION
        int p = EZ.createProductType("testRet5", "0123956789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "555-411-217");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0123956789123", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);

        //start return
        int retId = EZ.startReturnTransaction(id);
        EZ.returnProduct(retId, "0123956789123", 2);

        //before end
        assertEquals(-1, EZ.returnCashPayment(retId), 0.0);

        EZ.endReturnTransaction(retId, true);

        //does not exist
        assertEquals(-1, EZ.returnCashPayment(retId+75666), 0.0);

        //valid
        assertEquals(2.0, EZ.returnCashPayment(retId), 0.0);

    }

    @Test
    public void returnCreditCardPaymentTest() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException, InvalidCreditCardException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH TO HAVE A VALID TRANSACTION
        int p = EZ.createProductType("testRet5", "0123956789123", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "555-411-217");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "0123956789123", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);

        //start return
        int retId = EZ.startReturnTransaction(id);
        EZ.returnProduct(retId, "0123956789123", 2);

        //before end
        assertEquals(-1, EZ.returnCreditCardPayment(retId, "4485370086510891"), 0.0);

        EZ.endReturnTransaction(retId, true);

        //does not exist
        assertEquals(-1, EZ.returnCreditCardPayment(retId+75496, "4485370086510891"), 0.0);

        //valid
        assertEquals(2.0, EZ.returnCreditCardPayment(retId, "4485370086510891"), 0.0);
    }

    @Test
    public void getReturnTransactionTest () throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("testRet200", "9764919764310", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "737-255-311");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "9764919764310", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);
        int retId = (int) EZ.startReturnTransaction(id);
        EZ.returnProduct(retId, "9764919764310", 2);
        EZ.endReturnTransaction(retId, true);

        SaleTransaction r = EZ.getReturnTransaction(retId);

        //same ID
        assertEquals((int) r.getTicketNumber(), retId);
        //same price
        assertEquals((double) r.getPrice(), 2.0, 0.01);
        //same discount rate
        assertEquals((double) r.getDiscountRate(), 0.0, 0.01);
        //check for 1 entry
        assertEquals(r.getEntries().size(), 1);
        //if is the right product
        assertEquals(r.getEntries().get(0).getProductDescription(), "testRet200");
        //if is the right amount
        assertEquals(r.getEntries().get(0).getAmount(), 2);
    }


    @Test
    public void deleteReturnTransactionTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException {
        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        //SUFFICIENT CASH
        int p = EZ.createProductType("testRet5500", "97123419764310", 1.0, "note");
        int id = EZ.startSaleTransaction();
        if (p>0) {
            EZ.updatePosition(p, "037-215-001");
            EZ.updateQuantity(p, 100000);
        }
        EZ.addProductToSale(id, "97123419764310", 3);
        EZ.endSaleTransaction(id);
        EZ.receiveCashPayment(id, 5);
        int retId = (int) EZ.startReturnTransaction(id);
        EZ.returnProduct(retId, "97123419764310", 2);
        EZ.endReturnTransaction(retId, true);

        //not found
        assertFalse(EZ.deleteReturnTransaction(retId+754623));

        //valid
        assertTrue(EZ.deleteReturnTransaction(retId));

    }



}
