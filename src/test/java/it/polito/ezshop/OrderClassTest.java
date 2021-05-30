package it.polito.ezshop;


import it.polito.ezshop.data.OrderClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OrderClassTest {


	@Test
	public void testValidProductCode() {

		String expectedPC = "yasser";
		OrderClass o = new OrderClass();
		o.setProductCode(expectedPC);
		String name = o.getProductCode();
		assertEquals(expectedPC, name);
	}

	@Test
	public void testInvalidProductCode() {

		String expectedPC = "";
		OrderClass o = new OrderClass();
		o.setProductCode(expectedPC);
		String name = o.getProductCode();
		assertNotEquals(expectedPC, name);
	}

	@Test
	public void testNullProductCode() {

		String expectedPC = "Mario";
		OrderClass o = new OrderClass();
		o.setProductCode(expectedPC);
		String name = o.getProductCode();
		assertEquals(expectedPC, name);


		expectedPC = "Mario";
		o.setProductCode(null);
		name = o.getProductCode();
		assertEquals(expectedPC, name);

	}


	@Test
	public void testValidUserID() {

		Integer expectedid = 1;
		OrderClass o = new OrderClass();
		o.setOrderId(expectedid);
		Integer id = o.getOrderId();
		assertEquals(expectedid, id);
	}

	@Test
	public void testInvalidUserID() {

		Integer expectedid = 0;
		OrderClass o = new OrderClass();
		o.setOrderId(expectedid);
		Integer id = o.getOrderId();
		assertNotEquals(expectedid, id);
	}

	@Test
	public void testNullUserID() {

		Integer expectedid = 1;
		OrderClass o = new OrderClass();
		o.setOrderId(expectedid);
		Integer id = o.getOrderId();

		expectedid = 1;
		o.setOrderId(null);
		id = o.getOrderId();
		assertEquals(expectedid, id);
	}

}
