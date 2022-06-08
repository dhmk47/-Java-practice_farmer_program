package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dto.MyProduct;
import dto.Product;
import dto.ProductKind;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SplitImpl implements Split{
	private final Scanner sc;
	private final ChangeStream changeStreamImpl;

	@Override
	public void splitBySomething(ArrayList<? extends Product> myProductList) {
		
		if(myProductList.get(0) instanceof ProductKind) {
			ArrayList<ProductKind> productList = myProductList.stream().map(o -> (ProductKind) o).collect(Collectors.toCollection(ArrayList::new));
			Stream<ProductKind> myProductStream = changeStreamImpl.changeStream(productList);
			
			System.out.println("1. 계졀별 분할\n2. 가격별 분할");
			System.out.print("입력: ");
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				Map<String, List<ProductKind>> mapList = myProductStream.collect(Collectors.groupingBy(s -> s.getSeason()));
				
				System.out.print("보고 싶은 계절 입력(봄, 여름, 가을, 겨울, 전부): ");
				String season = sc.nextLine();
				if(season.equals("봄")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
					
				}else if(season.equals("여름")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("가을")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("겨울")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("전부")) {
					for(String key : mapList.keySet()) {
						System.out.println("[" + key + "]");
						mapList.get(key).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(key).forEach(System.out::println);
						System.out.println();
						
					}
						
				}else {
					System.out.println("?");
				}
			}else if(choice == 2) {
				System.out.println("얼마 이상으로 분할하시겠습니까?");
				System.out.print("입력: ");
				int price = sc.nextInt();
				sc.nextLine();
				
				Map<Boolean, List<ProductKind>> mapList = myProductStream.collect(Collectors.partitioningBy(s -> s.getPrice() > price - 1));
				
				System.out.println("[" + price + "원 이상]\n");
				mapList.get(true).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
				mapList.get(true).forEach(System.out::println);
				
				System.out.println();
				
				System.out.println("[" + price + "원 미만]\n");
				mapList.get(false).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
				mapList.get(false).forEach(System.out::println);
				
			}else {
				System.out.println("해당 명령어는 존재하지않는 명령어입니다.");
			}
			
			
		}else {
			ArrayList<MyProduct> productList = myProductList.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new));
			Stream<MyProduct> myProductStream = changeStreamImpl.changeStream(productList);
			
			System.out.println("1. 계졀별 분할\n2. 가격별 분할\n3. 갯수별 분할");
			System.out.print("입력: ");
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				Map<String, List<MyProduct>> mapList = myProductStream.collect(Collectors.groupingBy(s -> s.getSeason()));
				
				System.out.print("보고 싶은 계절 입력(봄, 여름, 가을, 겨울, 전부): ");
				String season = sc.nextLine();
				if(season.equals("봄")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
					
				}else if(season.equals("여름")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("가을")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("겨울")) {
					try {
						mapList.get(season);
						System.out.println("[" + season + "]");
						mapList.get(season).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(season).forEach(System.out::println);
						System.out.println();
					} catch (NullPointerException e) {
						System.out.println(season + "의 상품은 현재 보유하고 있지 않습니다.");
					}
						
				}else if(season.equals("전부")) {
					for(String key : mapList.keySet()) {
						System.out.println("[" + key + "]");
						mapList.get(key).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
						mapList.get(key).forEach(System.out::println);
						System.out.println();
						
					}
						
				}else {
					System.out.println("?");
				}
			}else if(choice == 2) {
				System.out.println("얼마 이상으로 분할하시겠습니까?");
				System.out.print("입력: ");
				int price = sc.nextInt();
				sc.nextLine();
				
				Map<Boolean, List<MyProduct>> mapList = myProductStream.collect(Collectors.partitioningBy(s -> s.getPrice() > price - 1));
				
				System.out.println("[" + price + "원 이상]\n");
				mapList.get(true).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
				mapList.get(true).forEach(System.out::println);
				
				System.out.println();
				
				System.out.println("[" + price + "원 미만]\n");
				mapList.get(false).sort((o1, o2) -> o1.getPrice() - o2.getPrice());
				mapList.get(false).forEach(System.out::println);
				
			}else if(choice == 3) {
				System.out.println("몇개 이상으로 분할하시겠습니까?");
				System.out.print("입력: ");
				int amount = sc.nextInt();
				sc.nextLine();
				
				Map<Boolean, List<MyProduct>> mapList = myProductStream.collect(Collectors.partitioningBy(s -> s.getAmount() > amount - 1));
				
				System.out.println("[" + amount + "개 이상]\n");
				mapList.get(true).sort((o1, o2) -> o1.getAmount() - o2.getAmount());
				mapList.get(true).forEach(System.out::println);
				
				System.out.println();
				
				System.out.println("[" + amount + "개 미만]\n");
				mapList.get(false).sort((o1, o2) -> o1.getAmount() - o2.getAmount());
				mapList.get(false).forEach(System.out::println);
			}else {
				System.out.println("해당 명령어는 존재하지않는 명령어입니다.");
			}
		}
		
	}
}