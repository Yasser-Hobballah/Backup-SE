package it.polito.ezshop;
import static org.junit.Assert.*;


import java.util.List;
import org.junit.Test;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.data.EZShop;

import it.polito.ezshop.data.ProductType;


public class ProductTypeTests {
    EZShop shop = new EZShop();
    // ------------------------------------------ //
    // -------------- PRODUCT TYPE -------------- //
    // ------------------------------------------ //

    @Test
    public void createProductTypeTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertTrue(EZ.createProductType("test1110", "08987456789123", 1.0, "note") > 0);
        int p = EZ.getProductTypeByBarCode("08987456789123").getId();
        EZ.deleteProductType(p);
    }

    @Test
    public void getProductTypeByBarcodeTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test33111", "08444456333123", 1.0, "note3");


        assertNull(EZ.getProductTypeByBarCode("0811111111123"));

        ProductType prod = EZ.getProductTypeByBarCode("08444456333123");
        assertEquals("test33111", prod.getProductDescription());
        assertEquals("note3", prod.getNote());

        ProductType prod2 = EZ.getProductByBarCode("08444456333123");
        assertEquals("test33111", prod.getProductDescription());
        assertEquals("note3", prod.getNote());

        EZ.deleteProductType(p);
    }


    @Test
    public void updateProductTypeTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test1111", "08987456333123", 1.0, "note");

        EZ.updateProduct(p, "test1112", "08987456313123", 2.0, "note2");

        ProductType prod = EZ.getProductTypeByBarCode("08987456313123");

        assertEquals("test1112", prod.getProductDescription());
        assertEquals("note2", prod.getNote());

        EZ.deleteProductType(p);
    }

    @Test
    public void getAllProductsTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test1122", "08987456322323", 1.0, "note");
        int p2 = EZ.createProductType("test1222", "08987226322323", 1.0, "note");

        List<ProductType> prod = EZ.getAllProductTypes();
        assertEquals("test1122", prod.get(p-1).getProductDescription());
        assertEquals("test1222", prod.get(p2-1).getProductDescription());

        EZ.deleteProductType(p);
        EZ.deleteProductType(p2);

    }

    @Test
    public void getProductTypesByDescriptionTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test11122", "08741456322323", 1.0, "note");
        int p2 = EZ.createProductType("test111222", "08741226322323", 1.0, "note");

        List<ProductType> prod = EZ.getProductTypesByDescription("test1");

        for (ProductType pr : prod) {
            assertTrue(pr.getProductDescription().startsWith("test1"));
        }

        EZ.deleteProductType(p);
        EZ.deleteProductType(p2);

    }

    @Test
    public void deleteProductTypeTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test2111", "01127456333123", 1.0, "note");

        assertFalse(EZ.deleteProductType(p+15236));
        assertTrue(EZ.deleteProductType(p));

    }

    @Test
    public void updateQuantityTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test214411", "01127457743123", 1.0, "note");

        assertFalse(EZ.updateQuantity(p, 100000));
        assertFalse(EZ.updateQuantity(p, -100000));
        EZ.updatePosition(p, "155-121-117");
        assertTrue(EZ.updateQuantity(p, 100000));

        EZ.logout();
        EZ.login("Simone", "123"); // in order to manage UnAuthorized Access and fill the user Class
        //internal
        assertTrue(EZ.updateQuantityinternal(p, 100000));
        EZ.logout();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        EZ.deleteProductType(p);
    }

    @Test
    public void updatePositionTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidLocationException {

        it.polito.ezshop.data.EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        int p = EZ.createProductType("test277711", "01127797744623", 1.0, "note");

        assertFalse(EZ.updatePosition(p+78544, "155-221-117"));
        assertTrue(EZ.updatePosition(p, "155-221-117"));

        int p2 = EZ.createProductType("test215411", "01111437743123", 1.0, "note");
        //assertFalse(EZ.updatePosition(p2, "155-221-117"));

        EZ.deleteProductType(p);
        EZ.deleteProductType(p2);

    }
}


