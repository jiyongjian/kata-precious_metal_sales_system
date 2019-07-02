package com.coding.sales.core;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.coding.sales.FileUtils;
import com.coding.sales.OrderApp;


public class OrderCalculateTest {

	@Test
    public void should_orderincres_puka() {
		OrderCalculate orderCalculate = new OrderCalculate();
		orderCalculate.init();
		Member member = (Member) orderCalculate.getMemberMap().get("6236609999");
        assertEquals(100, orderCalculate.getOrderPoints(member,new BigDecimal("100.5")));
    }
	
	@Test
    public void should_orderincres_gold() {
		OrderCalculate orderCalculate = new OrderCalculate();
		orderCalculate.init();
		Member member = (Member) orderCalculate.getMemberMap().get("6630009999");
        assertEquals(150, orderCalculate.getOrderPoints(member,new BigDecimal("100.5")));
    }
}
