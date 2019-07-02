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
	private String fullDiscount;//满减状态（可支持多种，逗号分隔）
	private String unit;//单位（条，个等）
	
	
	public Product() {		
	}
	
	public Product(String productNo, String productName,String unit, BigDecimal price,
			String discout,String fullDiscount) {
		super();
		this.productNo = productNo;
		this.productName = productName;
		this.price = price;
		this.discout = discout;
		this.fullDiscount = fullDiscount;
		this.unit = unit;
	}
	
	public Product(String productNo, String productName,String unit, BigDecimal price
			) {
		super();
		this.productNo = productNo;
		this.productName = productName;
		this.price = price;
		this.unit = unit;
	}
	
	public Product(String productNo, String productName,String unit, BigDecimal price,
			String discout) {
		super();
		this.productNo = productNo;
		this.productName = productName;
		this.price = price;
		this.discout = discout;
		this.unit = unit;
	}
	
	public String getFullDiscount() {
		return fullDiscount;
	}
	public void setFullDiscount(String fullDiscount) {
		this.fullDiscount = fullDiscount;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
