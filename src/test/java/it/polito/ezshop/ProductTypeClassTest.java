package it.polito.ezshop;


import it.polito.ezshop.data.ProductTypeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ProductTypeClassTest {


	@Test
	public void testValidProductCode() {

		String expectedPC = "yasser";
		ProductTypeClass u = new ProductTypeClass();
		u.setBarCode(expectedPC);
		String name = u.getBarCode();
		assertEquals(expectedPC, name);
	}

	@Test
	public void testInvalidProductCode() {

		String expectedPC = "";
		ProductTypeClass u = new ProductTypeClass();
		u.setBarCode(expectedPC);
		String name = u.getBarCode();
		assertNotEquals(expectedPC, name);
	}

	@Test
	public void testNullProductCode() {

		String expectedPC = "Mario";
		ProductTypeClass c = new ProductTypeClass();
		c.setBarCode(expectedPC);
		String name = c.getBarCode();
		assertEquals(expectedPC, name);


		expectedPC = "Mario";
		c.setBarCode(null);
		name = c.getBarCode();
		assertEquals(expectedPC, name);

	}


	@Test
	public void testValidPTID() {

		Integer expectedid = 1;
		ProductTypeClass c = new ProductTypeClass();
		c.setId(expectedid);
		Integer id = c.getId();
		assertEquals(expectedid, id);
	}

	@Test
	public void testInvalidPTID() {

		Integer expectedid = 0;
		ProductTypeClass c = new ProductTypeClass();
		c.setId(expectedid);
		Integer id = c.getId();
		assertNotEquals(expectedid, id);
	}

	@Test
	public void testNullPTID() {

		Integer expectedid = 1;
		ProductTypeClass c = new ProductTypeClass();
		c.setId(expectedid);
		Integer id = c.getId();

		expectedid = 1;
		c.setId(null);
		id = c.getId();
		assertEquals(expectedid, id);

	}

}
