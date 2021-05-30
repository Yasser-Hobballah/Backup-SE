package it.polito.ezshop;



import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.data.EZShop;

public class UsersTests { // For This Tests to run, We Supposed that "Yasser" Password="123" "Administrator" EXIST
    

    @Test
	public void resetTest() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		
        EZShop EZ = new EZShop();
        
		EZ.reset(); // THIS TEST DELETES ALL FILE
        
        String username = "Yasser",password="123",role="Administrator";   
		
        EZ.createUser( username,  password, role);
        username = "Simone";
        password="123";
        role="Cashier";
        EZ.createUser( username,  password, role);
        username = "Giorgio";
        password="123";
        role="ShopManager";
        EZ.createUser( username,  password, role);
    }


    @Test
	public void CreateUserTest() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        Integer id =0;
		String username = "yasser",password="123",role="Administrator";   
		id=EZ.createUser( username,  password, role);
        EZ.deleteUser(id);
    }
	
    @Test
	public void CreateUserTest2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		
        EZShop EZ = new EZShop();

		String username = "",password="123",role="Administrator";

		assertThrows(InvalidUsernameException.class, () -> { EZ.createUser(username,  password, role);} );
	
    }


    @Test
	public void CreateUserTest3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		
        EZShop EZ = new EZShop();

		String username = "TEST",password="",role="Administrator";

		assertThrows(InvalidPasswordException.class, () -> { EZ.createUser(username,  password, role);} );
	
    }


    @Test
	public void CreateUserTest4() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
		
        EZShop EZ = new EZShop();

		String username = "TEST",password="123",role="NotValidRole";

		assertThrows(InvalidRoleException.class, () -> { EZ.createUser(username,  password, role);} );
	
    }

    @Test
	public void CreateUserTest5() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String actual = "";
		String username = "yasser",password="123",role="Cashier";
        Integer id=0;
        id = EZ.createUser( username,  password, role);
        actual = Integer.toString( (EZ.createUser( username,  password, role)));

        assertEquals( "-1" , actual);
        EZ.deleteUser(id);
    }


	@Test
	public void deleteUserTest() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{

		EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        String username = "test",password="123",role="Cashier";
        Integer id=0;
        id = EZ.createUser( username,  password, role);
		EZ.deleteUser(id);
	
    }

    @Test
	public void deleteUserTest2() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException{

		EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        Integer id = -1;
		assertThrows(InvalidUserIdException.class, () -> { EZ.deleteUser(id);} ); 
	
    }


    @Test
	public void updateUserRightsTest() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
		String role2="Cashier";
        String username = "yasser",password="123",role="ShopManager";
        Integer id=0;
        id = EZ.createUser( username,  password, role);
        
		assertTrue(EZ.updateUserRights(id , role2));
        EZ.deleteUser(id);
    }

    @Test
	public void updateUserRightsTest2() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
		String role="NOTVALIDROLE";
        String username = "test2",password="123",role2="ShopManager";
        final Integer id=EZ.createUser( username,  password, role2);

        assertThrows(InvalidRoleException.class, () -> { EZ.updateUserRights(id , role);} ); 
        EZ.deleteUser(id);
    }

    @Test
	public void updateUserRightsTest3() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        String username = "test2",password="123",role2="Cashier";
        final Integer id=EZ.createUser( username,  password, role2);
        EZ.login("test2", "123"); // in order to manage UnAuthorized Access and fill the user Class
		String role="Cashier";
        
        assertThrows(UnauthorizedException.class, () -> { EZ.updateUserRights(id , role);} ); 
       EZ.logout();
       EZ.login("Yasser", "123");
        EZ.deleteUser(id);
    }

    @Test
	public void updateUserRightsTest4() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
		String role="Cashier";
        
		
        assertThrows(InvalidUserIdException.class, () -> { EZ.updateUserRights(-1 , role);} ); 
    }

    @Test
	public void updateUserRightsTest5() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException, UnauthorizedException{
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
		String role="Cashier";
        
		assertFalse(EZ.updateUserRights(100000 , role));
	
    }

    

    @Test
	public void getAllUsersTest() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertNotEquals(null, EZ.getAllUsers());
    }


    @Test
	public void getUserTest() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertNotEquals(null, EZ.getUser(2) );
        
    }



    @Test
	public void getUserTest2() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class

        assertThrows(InvalidUserIdException.class, () -> { EZ.getUser(-1 );} ); 
        
    }

    @Test
	public void loginTest() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();

        assertNotEquals(null, EZ.login("Yasser", "123") );
        
    }

    @Test
	public void loginTest2() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();

        assertEquals(null, EZ.login("Yasser2", "123") );
        
    }

    @Test
	public void loginTest3() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();

        assertEquals(null, EZ.login("Yasser", "1234") );
        
    }

    @Test
	public void logoutTest() throws InvalidUsernameException, InvalidPasswordException, InvalidUserIdException, InvalidRoleException, UnauthorizedException  {
		
        EZShop EZ = new EZShop();
        EZ.login("Yasser", "123"); // in order to manage UnAuthorized Access and fill the user Class
        assertTrue(EZ.logout());
        
    }



}
