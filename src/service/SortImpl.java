package service;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

import dto.MyProduct;
import dto.Product;
import dto.ProductKind;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SortImpl implements Sort{
	private final Scanner sc;

	@Override
	public void executeSortByPrice(ArrayList<? extends Product> productList) {
		
		// instanceof 로 전부 확인 후에 다운캐스팅을 사용해야 한다.
		System.out.println("가격이 동일한 경우 이름 오름차순으로 정렬 됩니다.");
		System.out.println("1. 가격 오름차순\n2. 가격 내림차순");
		
		if(productList.get(0) instanceof MyProduct) {
			ArrayList<MyProduct> myProductList = productList.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new));
			
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> {
					if(o1.getPrice() == o2.getPrice()) {
						int o1Size = o1.getName().length();
						int o2Size = o2.getName().length();
						int size = o1Size > o2Size ? o1Size : o2Size;
						for(int i = 0; i < size; i++) {
							if(i == o1Size || i == o2Size) {
								return o1Size - o2Size;
							}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
								continue;
								
							}
							return o1.getName().charAt(i) - o2.getName().charAt(i);
							
						}
					}
					return o1.getPrice() - o2.getPrice();
					
				});
			
				myProductList.forEach(System.out::println);
			
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> {
					if(o1.getPrice() == o2.getPrice()) {
						int o1Size = o1.getName().length();
						int o2Size = o2.getName().length();
						int size = o1Size > o2Size ? o1Size : o2Size;
						for(int i = 0; i < size; i++) {
							if(i == o1Size || i == o2Size) {
								return o1Size - o2Size;
							}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
								continue;
								
							}
							return o2.getName().charAt(i) - o1.getName().charAt(i);
							
						}
					}
					return o2.getPrice() - o1.getPrice();
					
				});
				
				myProductList.forEach(System.out::println);
				
			}else if(choice == 99) {
				return;
				
			}else {
				System.out.println("?");
				
			}
		}else {
			ArrayList<ProductKind> myProductList = productList.stream().map(o -> (ProductKind) o).collect(Collectors.toCollection(ArrayList::new));
			
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> {
					if(o1.getPrice() == o2.getPrice()) {
						int o1Size = o1.getName().length();
						int o2Size = o2.getName().length();
						int size = o1Size > o2Size ? o1Size : o2Size;
						for(int i = 0; i < size; i++) {
							if(i == o1Size || i == o2Size) {
								return o1Size - o2Size;
							}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
								continue;
								
							}
							return o1.getName().charAt(i) - o2.getName().charAt(i);
							
						}
					}
					return o1.getPrice() - o2.getPrice();
					
				});
			
				myProductList.forEach(System.out::println);
			
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> {
					if(o1.getPrice() == o2.getPrice()) {
						int o1Size = o1.getName().length();
						int o2Size = o2.getName().length();
						int size = o1Size > o2Size ? o1Size : o2Size;
						for(int i = 0; i < size; i++) {
							if(i == o1Size || i == o2Size) {
								return o1Size - o2Size;
							}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
								continue;
								
							}
							return o2.getName().charAt(i) - o1.getName().charAt(i);
							
						}
					}
					return o2.getPrice() - o1.getPrice();
					
				});
				
				myProductList.forEach(System.out::println);
				
			}else if(choice == 99) {
				return;
				
			}else {
				System.out.println("?");
				
			}
		}
		
		
	}

	@Override
	public void executeSortBySeason(ArrayList<? extends Product> productList) {
		System.out.println("1. 계절 오름차순\n2. 계절 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(productList.get(0) instanceof MyProduct) {
			ArrayList<MyProduct> myProductList = productList.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new));
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> o1.getSeason().charAt(0) - o2.getSeason().charAt(0));
				
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> o2.getSeason().charAt(0) - o1.getSeason().charAt(0));
				
			}else {
				System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
		}else {
			ArrayList<ProductKind> myProductList = productList.stream().map(o -> (ProductKind) o).collect(Collectors.toCollection(ArrayList::new));
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> o1.getSeason().charAt(0) - o2.getSeason().charAt(0));
				
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> o2.getSeason().charAt(0) - o1.getSeason().charAt(0));
				
			}else {
				System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
		}
		
	}

	@Override
	public void executeSortByGrowDay(ArrayList<ProductKind> productList) {
		System.out.println("1. 성장 날짜 오름차순\n2. 성장 날짜 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(choice == 1) {
			productList.sort((o1, o2) -> o1.getGrow_day() - o2.getGrow_day());
			
		}else if(choice == 2) {
			productList.sort((o1, o2) -> o2.getGrow_day() - o1.getGrow_day());
			
		}else {
			System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
			return;
		}
		
		productList.forEach(System.out::println);
	
	}

	@Override
	public void executeSortByAmount(ArrayList<MyProduct> productList) {
		System.out.println("1. 개수 오름차순\n2. 개수 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
			
		if(choice == 1) {
			productList.sort((o1, o2) -> o1.getAmount() - o2.getAmount());
			
		}else if(choice == 2) {
			productList.sort((o1, o2) -> o2.getAmount() - o1.getAmount());
			
		}else {
			System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
			return;
		}
		
		productList.forEach(System.out::println);
	}

	@Override
	public void executeSortByCode(ArrayList<? extends Product> productList) {
		System.out.println("1. 코드 오름차순\n2. 코드 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(productList.get(0) instanceof MyProduct) {
			ArrayList<MyProduct> myProductList = productList.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new));
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> o1.getProduct_code() - o2.getProduct_code());
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> o2.getProduct_code() - o1.getProduct_code());
			}else {
				System.out.println("해당 명령어는 존재하지않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
			
		}else {
			ArrayList<ProductKind> myProductList = productList.stream().map(o -> (ProductKind) o).collect(Collectors.toCollection(ArrayList::new));
		
			if(choice == 1) {
				myProductList.sort((o1, o2) -> o1.getProduct_code() - o2.getProduct_code());
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> o2.getProduct_code() - o1.getProduct_code());
			}else {
				System.out.println("해당 명령어는 존재하지않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
		}
		
	}

	@Override
	public void executeSortByName(ArrayList<? extends Product> productList) {
		System.out.println("1. 이름 오름차순\n2. 이름 내림차순");
		
		int choice = sc.nextInt();
		sc.nextLine();
		
		if(productList.get(0) instanceof MyProduct) {
			ArrayList<MyProduct> myProductList = productList.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new));
			
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> {
					int o1Size = o1.getName().length();
					int o2Size = o2.getName().length();
					int size = o1Size > o2Size ? o1Size : o2Size;		// 비교해서 긴 문자열 기준으로 반복횟수 결정
					for(int i = 0; i < size; i++) {
						if(i == o1Size || i == o2Size) {				// 반복중에 짧은 문자열을 초과하지 않기 위한 조건문
							return o1Size - o2Size;						// 문자열 짧은 순으로 정렬
						}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
							continue;
						}
						return o1.getName().charAt(i) - o2.getName().charAt(i);
					}
					return 0;
				});
				
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> {
					int o1Size = o1.getName().length();
					int o2Size = o2.getName().length();
					int size = o1Size > o2Size ? o1Size : o2Size;
					for(int i = 0; i < size; i++) {
						if(i == o1Size || i == o2Size) {
							return o1Size - o2Size;
						}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
							continue;
						}
						return o2.getName().charAt(i) - o1.getName().charAt(i);
					}
					return 0;
				});
				
			}else {
				System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
		}else {
			ArrayList<ProductKind> myProductList = productList.stream().map(o -> (ProductKind) o).collect(Collectors.toCollection(ArrayList::new));
			
			if(choice == 1) {
				myProductList.sort((o1, o2) -> {
					int o1Size = o1.getName().length();
					int o2Size = o2.getName().length();
					int size = o1Size > o2Size ? o1Size : o2Size;		// 비교해서 긴 문자열 기준으로 반복횟수 결정
					for(int i = 0; i < size; i++) {
						if(i == o1Size || i == o2Size) {				// 반복중에 짧은 문자열을 초과하지 않기 위한 조건문
							return o1Size - o2Size;						// 문자열 짧은 순으로 정렬
						}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
							continue;
						}
						return o1.getName().charAt(i) - o2.getName().charAt(i);
					}
					return 0;
				});
				
			}else if(choice == 2) {
				myProductList.sort((o1, o2) -> {
					int o1Size = o1.getName().length();
					int o2Size = o2.getName().length();
					int size = o1Size > o2Size ? o1Size : o2Size;
					for(int i = 0; i < size; i++) {
						if(i == o1Size || i == o2Size) {
							return o1Size - o2Size;
						}else if(o1.getName().charAt(i) == o2.getName().charAt(i)) {
							continue;
						}
						return o2.getName().charAt(i) - o1.getName().charAt(i);
					}
					return 0;
				});
				
			}else {
				System.out.println("해당 명령어는 존재하지 않는 명령어입니다.");
				return;
			}
			
			myProductList.forEach(System.out::println);
		}
		
	}
}