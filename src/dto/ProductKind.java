package dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductKind extends Product {
	private final int product_code;
	private final String name;
	private final int price;
	private final String season;
	private final int grow_day;
	
}