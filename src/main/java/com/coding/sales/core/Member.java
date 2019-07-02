package com.coding.sales.core;
/**
 * 会员信息
 * @author Administrator
 */
public class Member {
	private String memberNo;
	private String memberName;
	private String memberType;
//	private String cardNo;
	private int memberPoints;
	
	public Member() {		
	}
	public Member(String memberNo, String memberName, int memberPoints) {
		super();
		this.memberNo = memberNo;
		this.memberName = memberName;
		this.memberPoints = memberPoints;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	

	public String getMemberType() {
		if(memberPoints<10000){
			return "普卡";
		}else if(memberPoints<50000){
			return "金卡";
		}else if(memberPoints<50000){
			return "白金卡";
		}else{
			return "钻石卡";
		}
	}

	public int getMemberPoints() {
		return memberPoints;
	}
	public void setMemberPoints(int memberPoints) {
		this.memberPoints = memberPoints;
	}
	
	
	
}
