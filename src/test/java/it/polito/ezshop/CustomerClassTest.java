package it.polito.ezshop;



import static org.junit.Assert.*;
import org.junit.Test;
import it.polito.ezshop.data.CustomerClass;


public class CustomerClassTest {
    
    	
	@Test
	public void testInvalidCustomerName(){
		
		String expectedname = "";
		CustomerClass c = new CustomerClass();
		c.setCustomerName(expectedname); 
        String name = c.getCustomerName();
		assertNotEquals(expectedname,  name );
	}
	
	@Test
	public void testValidCustomerName(){

		String expectedname = "Mario";
		CustomerClass c = new CustomerClass();
		c.setCustomerName(expectedname);
		String name = c.getCustomerName();
		assertEquals(expectedname , name);
	}

	@Test
	public void testNullCustomerName(){
		
		String expectedname = "Mario";
		CustomerClass c = new CustomerClass();
		c.setCustomerName(expectedname);
		String name = c.getCustomerName();
		assertEquals(expectedname , name);
		
		
		expectedname = "Mario" ;
		c.setCustomerName(null);
		name = c.getCustomerName();
		assertEquals(expectedname , name);
	
	}


	
	@Test
	public void testValidCustomerID(){
		
		Integer expectedid = 1;
		CustomerClass c = new CustomerClass();
		c.setId(expectedid); 
        Integer id = c.getId();
		assertEquals(expectedid,  id );
	}
	@Test
	public void testInvalidCustomerID(){	
				
		Integer expectedid = 0;
		CustomerClass c = new CustomerClass();
		c.setId(expectedid); 
        Integer id = c.getId();
		assertNotEquals(expectedid,  id );
	}

	@Test
	public void testNullidCustomerID(){
		
		Integer expectedid = 1;
		CustomerClass c = new CustomerClass();
		c.setId(expectedid); 
        Integer id = c.getId();
		assertEquals(expectedid,  id );

		expectedid = 1 ;
		c.setId(null); 
        id = c.getId();
		assertEquals(expectedid,  id );
	
	}



	

}
