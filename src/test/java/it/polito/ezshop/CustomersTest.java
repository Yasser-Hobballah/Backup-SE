package it.polito.ezshop;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.data.EZShop;

public class CustomersTest {

    @Test
    public void defineCustomerTest() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String customerName = "yasser";
        Integer id = EZ.defineCustomer(customerName);
        assertTrue(id > 0);
        EZ.deleteCustomer(id);
    }

    @Test
    public void defineCustomerTest2() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String customerName = "";

        assertThrows(InvalidCustomerNameException.class, () -> {
            EZ.defineCustomer(customerName);
        });
    
    }

    @Test
    public void defineCustomerTest3() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String customerName = "Yasser2";
        Integer expected = -1;
        Integer id=EZ.defineCustomer(customerName);

        assertEquals(expected, EZ.defineCustomer(customerName));
        EZ.deleteCustomer(id);
    }

    @Test
    public void deleteCustomerTest() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String customerName = "yasser3";

        Integer id = EZ.defineCustomer(customerName);

        assertTrue(EZ.deleteCustomer(id));
        EZ.deleteCustomer(id);
    }

    @Test
    public void deleteCustomerTest2() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertThrows(InvalidCustomerIdException.class, () -> {
            EZ.deleteCustomer(null);
        });

    }

    @Test
    public void deleteCustomerTest3() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        Integer id = 100000;

        assertFalse(EZ.deleteCustomer(id));

    }

    @Test
    public void getAllCustomersTest() throws InvalidCustomerNameException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertNotNull(EZ.getAllCustomers());

    }

    @Test
    public void getCustomerTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String customerName = "yasser4";

        Integer id = EZ.defineCustomer(customerName);

        assertNotNull(EZ.getCustomer(id));
        EZ.deleteCustomer(id);
    }

    @Test
    public void getCustomerTest2() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertNull(EZ.getCustomer(100000));

    }

    @Test
    public void getCustomerTest3() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertThrows(InvalidCustomerIdException.class, () -> {
            EZ.getCustomer(0);
        });

    }

    @Test
    public void CreateCardTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertEquals(10, EZ.createCard().length());

    }

    @Test
    public void modifyCustomerTest()
            throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException, InvalidCustomerCardException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String newCustomerCard = "1234567891", newCustomerName = "Simone", customerName = "Hello";

        Integer id = EZ.defineCustomer(customerName);

        assertTrue(EZ.modifyCustomer(id, newCustomerName, newCustomerCard));
        EZ.deleteCustomer(id);
    }

    @Test
    public void modifyCustomerTest2()
            throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException, InvalidCustomerCardException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String newCustomerCard = "1234567891", newCustomerName = "Simone2", customerName = "Hello",
                customerName2 = "Hello2";
        Integer id1 = EZ.defineCustomer(customerName);

        EZ.modifyCustomer(id1, newCustomerName, newCustomerCard);
        Integer id2 = EZ.defineCustomer(customerName2);
        assertFalse(EZ.modifyCustomer(id2, newCustomerName, newCustomerCard));
        EZ.deleteCustomer(id1);
        EZ.deleteCustomer(id2);
    }

    @Test
    public void modifyCustomerTest3()
            throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException, InvalidCustomerCardException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String newCustomerCard = "1237891", newCustomerName = "Simone5", customerName = "Hello5";

        Integer id = EZ.defineCustomer(customerName);

        assertThrows(InvalidCustomerCardException.class, () -> {
            EZ.modifyCustomer(id, newCustomerName, newCustomerCard);
        });
        EZ.deleteCustomer(id);
    }



    @Test
    public void attachCardToCustomerTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException, InvalidCustomerCardException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String newCustomerCard = "1234567893", customerName = "Hello9144";
        Integer id = EZ.defineCustomer(customerName);
         assertTrue( EZ.attachCardToCustomer(newCustomerCard, id) );
         EZ.deleteCustomer(id);
    }

    @Test
    public void modifyPointsOnCardTest() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidCustomerIdException, InvalidCustomerCardException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String newCustomerCard = "1234567893", customerName = "Hello91";
        Integer id = EZ.defineCustomer(customerName);
         assertTrue( EZ.attachCardToCustomer(newCustomerCard, id) );
         EZ.deleteCustomer(id);
    }




}
