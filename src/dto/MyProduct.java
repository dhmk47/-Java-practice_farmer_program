package dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class MyProduct extends Product {
	private int product_code;
	private String name;
	private int price;
	private int amount;
	private String season;
	private int usercode;
	private int purchase_price;
	
	@Override
	public String toString() {
		return "MyProduct [name=" + name + ", price=" + price + ", amount=" + amount + ", season=" + season
				+ ", purchase_price=" + purchase_price + "]";
	}
}