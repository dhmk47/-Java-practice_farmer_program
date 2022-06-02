package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import dto.ProductKind;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SortImpl implements Sort{
	private final Scanner sc;

	@Override
	public void executeSortByPrice(ArrayList<ProductKind> productList) {
		// instanceof 로 전부 확인 후에 다운캐스팅을 사용해야 한다.
		System.out.println("가격이 동일한 경우 이름 오름차순으로 정렬 됩니다.");
		System.out.println("1. 가격 오름차순\n2. 가격 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(choice == 1) {
		productList.sort((o1, o2) -> {
			if(o1.getPrice() == o2.getPrice()) {
//				o1.getName().compareTo(o2.getName());
				for(int i = 0; i < o1.getName().length(); i++) {
					if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
						continue;
						
					}
					return o1.getName().charAt(i) - o2.getName().charAt(i);
					
				}
			}
			return o1.getPrice() - o2.getPrice();
			
		});
		
		productList.forEach(System.out::println);
		
//		Comparator<ProductKind> c = new Comparator<ProductKind>() {
//			
//		@Override
//		public int compare(ProductKind o1, ProductKind o2) {
//			if(o1.getPrice() == o2.getPrice()) {
//				for(int i = 0; i < o1.getName().length(); i++) {
//					if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
//						continue;
//					}
//					return o1.getName().charAt(i) - o2.getName().charAt(i);
//				}
//			}
//			return o1.getPrice() - o2.getPrice();
//			}
//		};
			
		}else if(choice == 2) {
			productList.sort((o1, o2) -> {
				if(o1.getPrice() == o2.getPrice()) {
					for(int i = 0; i < o1.getName().length(); i++) {
						if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
							continue;
							
						}
						return o1.getName().charAt(i) - o2.getName().charAt(i);
						
					}
				}
				return o2.getPrice() - o1.getPrice();
				
			});
			
			productList.forEach(System.out::println);
			
		}else if(choice == 99) {
			return;
			
		}else {
			System.out.println("?");
			
		}
	}

	@Override
	public void executeSortBySeason() {
		
	}

	@Override
	public void executeSortByGrowDay() {
		
	}

	@Override
	public void executeSortByAmount() {
		
	}

	@Override
	public void executeSortByCode() {
		System.out.println("1. 코드 오름차순\n2. 코드 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(choice == 1) {
			
		}else if(choice == 2) {
			
		}else {
			System.out.println("?");
		}
	}

}
