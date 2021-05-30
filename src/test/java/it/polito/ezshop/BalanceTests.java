package it.polito.ezshop;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.data.EZShop;

public class BalanceTests {

    @Test
    public void getCreditsAndDebitsTest()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        LocalDate from = LocalDate.of(2021, 5, 22), to = LocalDate.of(2021, 5, 25);
        assertNotEquals(null, EZ.getCreditsAndDebits(from, to));

    }

    @Test
    public void getCreditsAndDebitsTest2()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();

        LocalDate from = LocalDate.of(2021, 5, 22), to = LocalDate.of(2021, 5, 25);

        assertThrows(UnauthorizedException.class, () -> {
            EZ.getCreditsAndDebits(from, to);
        });
    }

    @Test
    public void recordBalanceUpdateTest()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertTrue(EZ.recordBalanceUpdate(50));

    }

    @Test
    public void recordBalanceUpdateTest2()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertFalse(EZ.recordBalanceUpdate(-500000));

    }

    @Test
    public void recordBalanceUpdateTest3()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        EZ.recordBalanceUpdate(500);
        assertTrue(EZ.recordBalanceUpdate(-100));

    }


    @Test
    public void ComputeBalanceTest()
            throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException {

        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        EZ.computeBalance();
        assertNotNull( EZ.computeBalance());
    }



    
}
