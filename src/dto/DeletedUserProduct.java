package dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DeletedUserProduct extends Product{
	private final int product_code;
	private final String name;
	private final int amount;
	private final int usercode;
	private final int purchase_price;
	

}
