package it.polito.ezshop;



import static org.junit.Assert.*;
import org.junit.Test;
import it.polito.ezshop.data.BalanceOperationClass;

public class BalanceOperationClassTest {
 
    

    @Test
	public void testValidBalanceID(){
		
		int expectedid = 1;
		BalanceOperationClass b = new BalanceOperationClass();
		b.setBalanceId(expectedid); 
        int id = b.getBalanceId();
		assertEquals(expectedid,  id );
	}
	@Test
	public void testInvalidBalanceID(){	
				
		int expectedid = -1;
		BalanceOperationClass b = new BalanceOperationClass();
		b.setBalanceId(expectedid); 
        int id = b.getBalanceId();
		assertNotEquals(expectedid,  id );
	}




    @Test
	public void testValidType(){
		
		String expectedType = "CREDIT";
		BalanceOperationClass b = new BalanceOperationClass();
		b.setType(expectedType); 
        String name = b.getType();
		assertEquals(expectedType,  name );
	}
	
	@Test
	public void testNotValidType(){

		String expectedType = "buy";
		BalanceOperationClass b = new BalanceOperationClass();
		b.setType("buy"); 
        String name = b.getType();
		assertNotEquals(expectedType,  name );
	}

	@Test
	public void testNullType(){
		
		String expectedType = "CREDIT";
		BalanceOperationClass c = new BalanceOperationClass();
		c.setType(expectedType);
		String name = c.getType();
				
		expectedType = "CREDIT" ;
		c.setType(null);
		name = c.getType();
		assertEquals(expectedType , name);
	
        expectedType = "" ;
		c.setType(expectedType);
		name = c.getType();
		assertNotEquals(expectedType , name);

    }



}
