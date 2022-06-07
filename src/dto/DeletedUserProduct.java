package dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeletedUserProduct extends Product{
	private int product_code;
	private String name;
	private int amount;
	private int usercode;
	private int purchase_price;

}
