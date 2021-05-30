package it.polito.ezshop;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.exceptions.*;
import org.junit.Test;


import static org.junit.Assert.*;

public class OrderTests {
    EZShop shop = new EZShop();

    // ------------------------------------------ //
    // ----------------- ORDER ------------------ //
    // ------------------------------------------ //

    @Test
    public void testFR4() throws InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidOrderIdException, InvalidLocationException, InvalidProductDescriptionException, InvalidProductIdException {
        Integer id =0 ,id2=0,id4=0;

     
        // ** Common Exceptions - Unauthorized
        assertThrows(UnauthorizedException.class, () -> shop.issueOrder("978020137962", 100, 2));
        assertThrows(UnauthorizedException.class, () -> shop.payOrderFor("978020137962", 100, 2));
        assertThrows(UnauthorizedException.class, () -> shop.payOrder(1));
        assertThrows(UnauthorizedException.class, () -> shop.recordOrderArrival(1));
        assertThrows(UnauthorizedException.class, () -> shop.getAllOrders());
        
        shop.login("Yasser", "123");
     
       
       final Integer id3=shop.createProductType( "apple" , "978020137963", 5, "Nothing" );
        // ** Common Exceptions - Invalid Order Id/Barcode/PricePerUnit,Quantity
        assertThrows(InvalidProductCodeException.class, () -> shop.issueOrder("", 100, 2));
        assertThrows(InvalidPricePerUnitException.class, () -> shop.issueOrder("978020137962", 100, 0));
        assertThrows(InvalidQuantityException.class, () -> shop.issueOrder("978020137962", 0, 2));
        assertThrows(InvalidProductCodeException.class, () -> shop.payOrderFor("", 100, 2));
        assertThrows(InvalidPricePerUnitException.class, () -> shop.payOrderFor("978020137962", 100, 0));
        assertThrows(InvalidQuantityException.class, () -> shop.payOrderFor("978020137962", 0, 2));
        assertThrows(InvalidProductCodeException.class, () -> shop.issueOrder("", 100, 2));
        assertThrows(InvalidPricePerUnitException.class, () -> shop.issueOrder("978020137962", 100, 0));
        assertThrows(InvalidQuantityException.class, () -> shop.issueOrder("978020137962", 0, 2));
        assertThrows(InvalidOrderIdException.class, () -> shop.payOrder(0));
        assertThrows(InvalidOrderIdException.class, () -> shop.recordOrderArrival(0));
        //assertThrows(InvalidLocationException.class, () -> shop.recordOrderArrival(id3));



        id2=shop.createProductType( "banana" , "978020137962", 5, "Nothing" );
        shop.updatePosition(id2, "111-111-111");
        // ** Nominal cases

        // Create Order
        id= shop.issueOrder("978020137962", 100, 1);
        assertTrue( id > 0);
       
        shop.recordBalanceUpdate(5000); // just to add to balance some money so we can pay order
       
        assertTrue(shop.payOrder(id));
     
        
      
        assertTrue( shop.recordOrderArrival(id)) ;


         assertNotNull(shop.getAllOrders()); 

         id4= shop.payOrderFor("978020137962", 100, 1);
         assertTrue( id4 > 0);
        
        shop.deleteProductType(id2);
        shop.deleteProductType(id3);
    }

}
