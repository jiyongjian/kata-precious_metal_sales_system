package com.coding.sales.core;

/**
 * 产品信息
 * @author Administrator
 */
public class Product {
	private String productNo;
	private String productName;
	private String  price;
	private String  discout;//支持的券
	
	
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
