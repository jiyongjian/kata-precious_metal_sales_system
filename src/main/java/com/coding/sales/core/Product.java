package com.coding.sales.core;

import java.math.BigDecimal;

/**
 * 产品信息
 * @author Administrator
 */
public class Product {
	private String productNo;
	private String productName;
	private BigDecimal  price;
	private String  discout;//支持的券
	
	
	public Product(String productNo, String productName, BigDecimal price,
			String discout) {
		super();
		this.productNo = productNo;
		this.productName = productName;
		this.price = price;
		this.discout = discout;
	}
	public String getDiscout() {
		return discout;
	}
	public void setDiscout(String discout) {
		this.discout = discout;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
