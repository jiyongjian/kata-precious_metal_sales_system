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

/**
 * 订单处理
 * @author Administrator
 */
public class OrderCalculate {

	private Map productMap = new HashMap<String, Product>();
	private Map memberMap = new HashMap<String, Member>();

	public void init() {
		productMap.put("001001", new Product("001001", "世园会五十国钱币册", "册", new BigDecimal("998.00")));
		productMap.put("001002", new Product("001002", "2019北京世园会纪念银章大全40g", "盒", new BigDecimal("1380.00"), "9折券"));
		productMap.put("003001", new Product("003001", "招财进宝", "条", new BigDecimal("1580.00")));
		productMap.put("003002", new Product("003002", "水晶之恋", "条", new BigDecimal("980.00"), null, "4,5"));
		productMap.put("002002", new Product("002002", "中国经典钱币套装", "套", new BigDecimal("998.00"), null, "2,3"));
		productMap.put("002001", new Product("002001", "守扩之羽比翼双飞4.8g", "条", new BigDecimal("1080.00"), "95折券", "4,5"));
		productMap.put("002003", new Product("002003", "中国银象棋12g", "套", new BigDecimal("698.00"), "9折券", "1,2,3"));

		memberMap.put("6236609999", new Member("6236609999", "马丁", 9860));
		memberMap.put("6630009999", new Member("6630009999", "王立", 48860));
		memberMap.put("8230009999", new Member("8230009999", "李想", 98860));
		memberMap.put("9230009999", new Member("9230009999", "张三", 198860));
	}

	public OrderRepresentation getOrderRepresentation(OrderCommand orderCommand) throws ParseException {

		DateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String orderId = orderCommand.getOrderId();
		Date createTime = formate.parse(orderCommand.getCreateTime());
		String memberNo = orderCommand.getMemberId();
		String memberName = "";
		String oldMemberType = "";
		String newMemberType = "";
		int memberPointsIncreased = 0;
		int memberPoints = 0;
		List<OrderItemRepresentation> orderItems = new ArrayList<OrderItemRepresentation>();
		BigDecimal totalPrice = new BigDecimal(0);
		List<DiscountItemRepresentation> discounts = new ArrayList<DiscountItemRepresentation>();
		BigDecimal totalDiscountPrice = new BigDecimal(0);
		BigDecimal receivables = new BigDecimal(0);
		List<PaymentRepresentation> payments = new ArrayList<PaymentRepresentation>();
		List<String> discountCards = new ArrayList<String>();

		Map members = memberMap;
		Map products = productMap;

		List<PaymentCommand> paymentCommands = orderCommand.getPayments();
		for (int i = 0; i < paymentCommands.size(); i++) {
			PaymentCommand paymentCommand = paymentCommands.get(i);
			PaymentRepresentation paymentRepresentation = new PaymentRepresentation(paymentCommand.getType(), paymentCommand.getAmount());
			payments.add(paymentRepresentation);
		}

		discountCards = orderCommand.getDiscounts();

		Member member = (Member) members.get(orderCommand.getMemberId());
		memberName = member.getMemberName();
		oldMemberType = member.getMemberType();
		List<OrderItemCommand> orderItems2 = orderCommand.getItems();
        //遍历订单列表，计算总金额及优惠信息
		for (int i = 0; i < orderItems2.size(); i++) {
			OrderItemCommand orderIterm = orderItems2.get(i);
			Product prod = (Product) products.get(orderIterm.getProduct());
			// 计算单个产品总价
			// 总价=单价*数量
			BigDecimal prodTotAmount = prod.getPrice().multiply(orderIterm.getAmount());

			List<BigDecimal> amoutList = new ArrayList<BigDecimal>();
			amoutList.add(getDisCount(prodTotAmount, prod, orderCommand.getDiscounts().get(0)));
			amoutList.add(getFullDisCount(prod, orderIterm.getAmount(), prodTotAmount));
			BigDecimal prodTotAmountDis = getMinDisAmout(amoutList);
			totalPrice = totalPrice.add(prodTotAmount);
			receivables = receivables.add(prodTotAmountDis);

			orderItems.add(new OrderItemRepresentation(prod.getProductNo(), prod.getProductName(), prod.getPrice(), orderIterm.getAmount(),prodTotAmount));

			totalDiscountPrice = totalDiscountPrice.add(prodTotAmount.subtract(prodTotAmountDis));
			if (prodTotAmount.compareTo(prodTotAmountDis) > 0) {
				DiscountItemRepresentation discountItemRepresentation = new DiscountItemRepresentation(prod.getProductNo(), prod.getProductName(),
						prodTotAmount.subtract(prodTotAmountDis));
				discounts.add(discountItemRepresentation);
			}

		}

		memberPointsIncreased = getOrderPoints(member, receivables);
		memberPoints = member.getMemberPoints() + memberPointsIncreased;//累计积分
		member.setMemberPoints(memberPoints);
		newMemberType = member.getMemberType();//获取用户新的卡等级

		OrderRepresentation orderRepresentation = new OrderRepresentation(orderId, createTime, memberNo, memberName, oldMemberType, newMemberType, memberPointsIncreased,
				memberPoints, orderItems, totalPrice, discounts, totalDiscountPrice, receivables, payments, discountCards);

		return orderRepresentation;
	}

	// 计算折扣金额
	private BigDecimal getDisCount(BigDecimal prodTotAmount, Product prod, String discounts) {
		// 打折金额=总价*折扣
		BigDecimal prodDisTotAmount = prodTotAmount;
		if (prod.getDiscout() != null && !"".equals(prod.getDiscout())) {
			if (prod.getDiscout().equals(discounts)) {
				if ("9折券".equals(prod.getDiscout())) {
					prodDisTotAmount = prodTotAmount.multiply(new BigDecimal(0.9));
				} else if ("9折券".equals(prod.getDiscout())) {
					prodDisTotAmount = prodTotAmount.multiply(new BigDecimal(0.95));
				}
			}
		}
		return prodDisTotAmount;
	}

	// 获取单个产品在满减规则下的产品总价
	private BigDecimal getFullDisCount(Product product, BigDecimal count, BigDecimal prodTotAmount) {
		String fullDiscount = product.getFullDiscount();
		BigDecimal prodDisTotAmount2 = prodTotAmount;
		BigDecimal prodDisTotAmount3 = prodTotAmount;
		if (fullDiscount != null) {
			if (fullDiscount.contains("1") || fullDiscount.contains("2") || fullDiscount.contains("3") || fullDiscount.contains("4") || fullDiscount.contains("5")) {
				// 优惠方案
				if (fullDiscount.contains("1") || fullDiscount.contains("2") || fullDiscount.contains("3")) {
					if (prodTotAmount.compareTo(new BigDecimal("3000")) > 0 && fullDiscount.contains("3")) {
						prodDisTotAmount2 = prodTotAmount.subtract(new BigDecimal("350"));
					} else if (prodTotAmount.compareTo(new BigDecimal("2000")) > 0 && fullDiscount.contains("2")) {
						prodDisTotAmount2 = prodTotAmount.subtract(new BigDecimal("30"));
					} else if (prodTotAmount.compareTo(new BigDecimal("1000")) > 0 && fullDiscount.contains("1")) {
						prodDisTotAmount2 = prodTotAmount.subtract(new BigDecimal("10"));
					}
				}
				if (fullDiscount.contains("4") || fullDiscount.contains("5")) {
					if (fullDiscount.contains("4") && fullDiscount.contains("5")) {
						if (count.compareTo(new BigDecimal(3)) > 0) {
							prodDisTotAmount3 = product.getPrice().multiply(count.subtract(new BigDecimal(1)));
						} else if (count.compareTo(new BigDecimal(3)) == 0) {
							prodDisTotAmount3 = product.getPrice().multiply(count.subtract(new BigDecimal(0.5)));
						}
					} else if (fullDiscount.contains("4")) {
						if (count.compareTo(new BigDecimal(2)) > 0) {
							prodDisTotAmount3 = product.getPrice().multiply(count.subtract(new BigDecimal(0.5)));
						}
					} else if (fullDiscount.contains("5")) {
						if (count.compareTo(new BigDecimal(3)) > 0) {
							prodDisTotAmount3 = product.getPrice().multiply(count.subtract(new BigDecimal(1)));
						}
					}
				}
			}
		}
		return prodDisTotAmount2.compareTo(prodDisTotAmount3) > 0 ? prodDisTotAmount3 : prodDisTotAmount2;
	}

	// 获取两种优惠方案的最小金额，即为优惠后的总价
	private BigDecimal getMinDisAmout(List<BigDecimal> amountList) {
		// list从小到大排序
		BigDecimal amt = amountList.get(0);
		for (int i = 1; i < amountList.size(); i++) {
			BigDecimal amt2 = amountList.get(i);
			if (amt2.compareTo(amt) > 0) {

			} else {
				amt = amt2;
			}
		}
		return amt;
	}

	/* 根据金额获取用户新增积分 */
	public int getOrderPoints(Member member, BigDecimal totAmount) {
		int incresPoint = 0;
		int point = member.getMemberPoints();
		if (point < 10000) {
			incresPoint = totAmount.setScale(0, BigDecimal.ROUND_DOWN).intValue();
		} else if (point < 50000) {
			incresPoint = totAmount.multiply(new BigDecimal("1.5")).setScale(0, BigDecimal.ROUND_DOWN).intValue();
		} else if (point < 100000) {
			incresPoint = totAmount.multiply(new BigDecimal("1.8")).setScale(0, BigDecimal.ROUND_DOWN).intValue();
		} else {
			incresPoint = totAmount.multiply(new BigDecimal("2")).setScale(0, BigDecimal.ROUND_DOWN).intValue();
		}
		return incresPoint;
	}

	public Map getProductMap() {
		return productMap;
	}

	public void setProductMap(Map productMap) {
		this.productMap = productMap;
	}

	public Map getMemberMap() {
		return memberMap;
	}

	public void setMemberMap(Map memberMap) {
		this.memberMap = memberMap;
	}
}
