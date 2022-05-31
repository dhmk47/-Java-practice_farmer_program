package dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@Getter
public class ProductInfo {
	private final ArrayList<String> productInfo;
	public static ProductInfo instance;
	
	private ProductInfo() {
		productInfo = new ArrayList<String>();
		productInfo.add("product_code");
		productInfo.add("name");
		productInfo.add("price");
		productInfo.add("season");
		productInfo.add("grow_day");
	}
	
	public static ProductInfo getInstance() {
		if(instance == null) {
			instance = new ProductInfo();
		}
		return instance;
	}
}
