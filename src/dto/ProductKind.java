package dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@RequiredArgsConstructor
public class ProductKind extends Product {
	private final int product_code;
	private final String name;
	private final int price;
	private final String season;
	private final int grow_day;
	
}