package com.coding.sales.core;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.input.PaymentCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderItemRepresentation;
import com.coding.sales.output.OrderRepresentation;
import com.coding.sales.output.PaymentRepresentation;

public class OrderCalculate {
	
	private Map productMap = new HashMap<String , Product>();
	private Map memberMap = new HashMap<String , Member>();
	
	public void init(){
		productMap.put("", new Product("001001", "世园会五十国钱币册", "册",new BigDecimal("998.00")));
		productMap.put("", new Product("001002", "2019北京世园会纪念银章大全40g","盒", new BigDecimal("1380.00"),"9折券"));
		productMap.put("", new Product("003001", "招财进宝","条", new BigDecimal("1580.00")));
		productMap.put("", new Product("003002", "水晶之恋","条" ,new BigDecimal("980.00"),null,"4,5"));
		productMap.put("", new Product("002002", "中国经典钱币套装","套", new BigDecimal("998.00"),null,"2,3"));
		productMap.put("", new Product("002001", "守扩之羽比翼双飞4.8g","条", new BigDecimal("1080.00"),"95折券","4,5"));
		productMap.put("", new Product("002003", "中国银象棋12g","套", new BigDecimal("698.00"),"9折券","1,2,3"));
		
		memberMap.put("6236609999", new Member("6236609999", "马丁", "6236609999", 9860));
		memberMap.put("6630009999", new Member("6630009999", "王立", "6630009999", 48860));
		memberMap.put("8230009999", new Member("8230009999", "李想", "8230009999", 98860));
		memberMap.put("9230009999", new Member("9230009999", "张三", "9230009999", 198860));		
	}

	public OrderRepresentation calculate(OrderCommand orderCommand) throws ParseException {
		
		
		DateFormat formate = new SimpleDateFormat();
		String orderId= orderCommand.getOrderId();
		Date createTime = formate.parse(orderCommand.getCreateTime());
		String memberNo= orderCommand.getMemberId();
		String memberName = "";
		String oldMemberType = "";
		String newMemberType = "";
		int memberPointsIncreased =0 ;
		int memberPoints =0 ;
	    List<OrderItemRepresentation> orderItems = new ArrayList<OrderItemRepresentation>();
		BigDecimal totalPrice = new BigDecimal(0) ;
		List<DiscountItemRepresentation> discounts =new ArrayList<DiscountItemRepresentation>();
		BigDecimal totalDiscountPrice = new BigDecimal(0)  ;
		BigDecimal receivables  = new BigDecimal(0);
		List<PaymentRepresentation>  payments = new ArrayList<PaymentRepresentation>();
		List<String> discountCards = new ArrayList<String>();
		

		
//		Map members =getMemberMaps();
		Map members =memberMap;
		Map products =productMap;

		
		List<PaymentCommand> paymentCommands= orderCommand.getPayments();
		for(int i=0 ;i<paymentCommands.size();i++){
			PaymentCommand paymentCommand  = paymentCommands.get(i);
			PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(), paymentCommand.getAmount());
			payments.add(paymentRepresentation);
		}
		
		discountCards = orderCommand.getDiscounts();
		
		
		
		
			Member member = (Member) members.get(members.get(orderCommand.getMemberId()));
			memberName =member.getMemberName();	
			oldMemberType = member.getOldMemberType();
		List<OrderItemCommand> orderItems2 = orderCommand.getItems();

		// 计算单个产品总价
		for (int i = 0; i < orderItems2.size(); i++) {
			OrderItemCommand orderIterm = orderItems2.get(i);


			Product prod = (Product) products.get(orderIterm.getProduct());

			// 总价=单价*数量
			BigDecimal prodTotAmount = prod.getPrice().multiply(orderIterm.getAmount());

			//打折金额=总价*折扣
			BigDecimal prodDisTotAmount = new BigDecimal(0);
			if (prod.getDiscout() != null&& !"".equals(prod.getDiscout())) {
				if (prod.getDiscout().equals(orderCommand.getDiscounts().get(0))) {
					if ("9折券".equals(prod.getDiscout())) {
						 prodDisTotAmount = prodTotAmount.multiply(new BigDecimal(0.9));
					} else if ("9折券".equals(prod.getDiscout())) {
						 prodDisTotAmount = prodTotAmount.multiply(new BigDecimal(0.95));
					}
				}
			}
			
			
			String youhui=prod.getFullDiscount();
			
			BigDecimal prodDisTotAmount2  = new BigDecimal(0);
			BigDecimal prodDisTotAmount3  = new BigDecimal(0);
			
			
			if(youhui.contains("1")||youhui.contains("2")||youhui.contains("3")||youhui.contains("4")||youhui.contains("5")){
				
				//优惠方案
				if(youhui.contains("1")||youhui.contains("2")||youhui.contains("3")){
					
					if(prodTotAmount.compareTo(new BigDecimal("1000"))>0 && youhui.contains("1")){
						prodDisTotAmount2 = prodDisTotAmount.subtract(new BigDecimal("10"));
					}
					if(prodTotAmount.compareTo(new BigDecimal("2000"))>0 && youhui.contains("2")){
						prodDisTotAmount2 = prodDisTotAmount.subtract(new BigDecimal("30"));
					}
					if(prodTotAmount.compareTo(new BigDecimal("3000"))>0 && youhui.contains("3")){
						prodDisTotAmount2 = prodDisTotAmount.subtract(new BigDecimal("350"));
					}
					
					if(youhui.contains("4")||youhui.contains("5")){
						
						if(youhui.contains("4")&&youhui.contains("5")){
							
							if (orderIterm.getAmount().compareTo(new BigDecimal(3)) > 0) {
								 prodDisTotAmount3 = prod.getPrice().multiply(orderIterm.getAmount().subtract(new BigDecimal(1)));
							}else if(orderIterm.getAmount().compareTo(new BigDecimal(3))==0){
								 prodDisTotAmount3 = prod.getPrice().multiply(orderIterm.getAmount().subtract(new BigDecimal(0.5)));
							}
						}else if (youhui.contains("4")){
							if(orderIterm.getAmount().compareTo(new BigDecimal(2)) > 0){
								 prodDisTotAmount3 = prod.getPrice().multiply(orderIterm.getAmount().subtract(new BigDecimal(0.5)));
							}
						}else if(youhui.contains("5")){
							if(orderIterm.getAmount().compareTo(new BigDecimal(3)) > 0){
								 prodDisTotAmount3 = prod.getPrice().multiply(orderIterm.getAmount().subtract(new BigDecimal(1)));
							}
						}
					}
				}
			}
			
			List<BigDecimal> amoutList = new ArrayList<BigDecimal>();
			amoutList.add(prodDisTotAmount);
			amoutList.add(prodDisTotAmount2);
			amoutList.add(prodDisTotAmount3);
			BigDecimal prodTotAmountDis =  getMinDisAmout(amoutList);
			totalPrice = totalPrice.add(prodTotAmount);
			receivables = receivables.add(prodTotAmountDis);
			OrderItemRepresentation orderItemRepresentation = new OrderItemRepresentation(
					prod.getProductNo(), prod.getProductName(),
					prod.getPrice(), prodTotAmount, prodTotAmountDis);
			orderItems.add(orderItemRepresentation);	
			
			DiscountItemRepresentation discountItemRepresentation = new DiscountItemRepresentation(
					prod.getProductNo(), prod.getProductName(),
					prodTotAmount.subtract(prodTotAmountDis));
			discounts.add(discountItemRepresentation);
			
			
		

		}

		memberPointsIncreased = getOrderPoints(member,totalDiscountPrice);
		
		OrderRepresentation orderRepresentation  = new OrderRepresentation(
				orderId, 
				createTime, 
				memberNo,
				memberName, 
				oldMemberType,
				newMemberType, 
				memberPointsIncreased,
				memberPoints, 
				orderItems, 
				totalPrice, 
				discounts, 
				totalDiscountPrice, 
				receivables, 
				payments, 
				discountCards);
		
		return orderRepresentation;
	}
	
	
	
	private BigDecimal getMinDisAmout(List<BigDecimal> amountList){

		//list从小到大排序
		
		
		return amountList.get(0);
		
	}
	
	private  int getOrderPoints(Member member,BigDecimal totAmount){
		
		return 0;
	}

}
