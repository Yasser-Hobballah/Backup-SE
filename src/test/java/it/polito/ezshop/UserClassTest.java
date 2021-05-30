package it.polito.ezshop;



import static org.junit.Assert.*;
import org.junit.Test;
import it.polito.ezshop.data.UserClass;

public class UserClassTest {
    

    @Test
	public void testValidUserName(){
		
		String expectedname = "yasser";
		UserClass u = new UserClass();
		u.setUsername(expectedname); 
        String name = u.getUsername();
		assertEquals(expectedname,  name );
	}
	
	@Test
	public void testInvalidUserName(){

		String expectedname = "";
		UserClass u = new UserClass();
		u.setUsername(expectedname); 
        String name = u.getUsername();
		assertNotEquals(expectedname,  name );
	}

	@Test
	public void testNullUserName(){
		
		String expectedname = "Mario";
		UserClass c = new UserClass();
		c.setUsername(expectedname);
		String name = c.getUsername();
		assertEquals(expectedname , name);
		
		
		expectedname = "Mario" ;
		c.setUsername(null);
		name = c.getUsername();
		assertEquals(expectedname , name);
	
	}


	
	@Test
	public void testValidUserID(){
		
		Integer expectedid = 1;
		UserClass c = new UserClass();
		c.setId(expectedid); 
        Integer id = c.getId();
		assertEquals(expectedid,  id );
	}
	@Test
	public void testInvalidUserID(){	
				
		Integer expectedid = 0;
		UserClass c = new UserClass();
		c.setId(expectedid); 
        Integer id = c.getId();
		assertNotEquals(expectedid,  id );
	}

	@Test
	public void testNullUserID(){
		
		Integer expectedid = 1;
		UserClass c = new UserClass();
		c.setId(expectedid); 
        Integer id = c.getId();

		expectedid = 1 ;
		c.setId(null); 
        id = c.getId();
		assertEquals(expectedid,  id );
	
	}



    @Test
	public void testValidRole(){
		
		String expectedType = "Cashier";
		UserClass b = new UserClass();
		b.setRole(expectedType); 
        String name = b.getRole();
		assertEquals(expectedType,  name );
	}
	
	@Test
	public void testNotValidRole(){

		String expectedType = "buy";
		UserClass b = new UserClass();
		b.setRole("buy"); 
        String name = b.getRole();
		assertNotEquals(expectedType,  name );
	}

	@Test
	public void testNullRole(){
		
		String expectedType = "Cashier";
		UserClass c = new UserClass();
		c.setRole(expectedType);
		String name = c.getRole();
				
		expectedType = "Cashier" ;
		c.setRole(null);
		name = c.getRole();
		assertEquals(expectedType , name);
	
        expectedType = "" ;
		c.setRole(expectedType);
		name = c.getRole();
		assertNotEquals(expectedType , name);

    }







}
