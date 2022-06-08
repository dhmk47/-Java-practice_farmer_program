package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class MyProduct extends Product {
	private final int product_code;
	private final String name;
	private final int price;
	private final int amount;
	private final String season;
	private final int usercode;
	private final int purchase_price;
	
	@Override
	public String toString() {
		return "MyProduct [name=" + getName() + ", price=" + getPrice() + ", amount=" + amount + ", season=" + getSeason()
				+ ", purchase_price=" + purchase_price + "]";
	}
}