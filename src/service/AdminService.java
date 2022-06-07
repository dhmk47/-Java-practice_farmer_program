package service;

import java.util.ArrayList;
import java.util.Scanner;

import dao.ServiceDao;
import dto.Product;
import dto.ProductInfo;
import dto.ProductKind;
import dto.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminService {
	private final Scanner sc;
	private final ServiceDao serviceDao;
	private final Sort sort;
	
	public void displayAdminMenu() {
		ArrayList<Product> productList = null;
//		ArrayList<ProductKind> productList = null;
		
		while(true) {
			System.out.println("[관리자]");
			System.out.println("1. 현재 가입된 유저 정보 보기\n2. 현재 구매 가능한 품목 보기\n3. 구매 가능 품목 추가"
					+ "\n4. 정렬보기\n5. 상품 정보 수정\n99 로그아웃");
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				ArrayList<User> userList = serviceDao.getAllUserInfo();
				
				showAllUserInfo(userList);
				
			}else if(choice == 2) {
				productList = serviceDao.getAllProductKindInfo();
				
				showAllProductKind(productList);
				
			}else if(choice == 3) {
				addProductKind();
				
			}else if(choice == 4) {
				productList = serviceDao.getAllProductKindInfo();
				
				if(productList.isEmpty()) {
					System.out.println("현재 등록된 품목이 존재하지 않습니다.");
					return;
				}
				
				System.out.println("1. 가격순 정렬\n2. 계절순 정렬\n3. 성장 날짜순 정렬\n4. 이름순 정렬\n5. 코드순 정렬");
				int select = sc.nextInt();
				
				if(select == 1) {
					sort.executeSortByPrice(productList);
				}else if(select == 2) {
					sort.executeSortBySeason(productList);
				}else if(select == 3) {
					sort.executeSortByGrowDay(productList);
				}else if(select == 4) {
					sort.executeSortByName(productList);
				}else if(select == 5) {
					sort.executeSortByCode(productList);
				}else {
					System.out.println("존재하지 않는 명령어입니다.");
				}
				
			}else if(choice == 5) {
				System.out.println("1. 상품 정보 수정\n2. 상품 삭제하기\n3. 뒤로가기");
				int select = sc.nextInt();
				sc.nextLine();
				
				if(select == 1) {
					modifyProductInfo();
					
				}else if(select == 2) {
					removeProduct();
					
				}else if(select == 3) {
					
				}else {
					System.out.println("존재하지 않는 명령어입니다.");
				}
				
			}else if(choice == 99) {
				break;
			}else {
				System.out.println("?");
			}
			
			System.out.println();
			
		}
	}
	
	private void showAllUserInfo(ArrayList<User> userList) {
		
		if(userList.isEmpty()) {
			System.out.println("현재 가입된 회원이 없습니다.");
		}
		
		userList.forEach(System.out::println);
		
	}
	
	private void showAllProductKind(ArrayList<Product> productList) {
		
		if(productList.isEmpty()) {
			System.out.println("현재 등록된 품목이 존재하지 않습니다.");
			return;
		}
		
		productList.forEach(System.out::println);
		
	}
	
	private void addProductKind() {
		int productCode = 0;
		String name = null;
		
		ArrayList<Product> productList = serviceDao.getAllProductKindInfo();
		
		if(!productList.isEmpty()) {
			System.out.println("이미 등록되어 있는 상품 코드 : 이름");
			productList.forEach(o -> System.out.println(o.getProduct_code() + " : " + o.getName()));
			
		}
		
		
		System.out.print("추가하실 품목의 코드를 입력하세요: ");
		productCode = sc.nextInt();
		sc.nextLine();
		
		System.out.print("추가하실 품목의 이름을 입력하세요: ");
		name = sc.nextLine();
		
		for(Product product : productList) {
			if(product.getProduct_code() == productCode) {
				System.out.println("해당 코드는 이미 존재하므로 등록할 수 없습니다.");
				return;
				
			}else if(product.getName().equals(name)) {
				System.out.println("해당 이름은 이미 존재하므로 등록할 수 없습니다.");
				return;
					
			}
			
		}
		if(serviceDao.addProductKind(productCode, name) != 0) {
			System.out.println(name + "(이)라는 상품을 코드 " + productCode + "으로 등록 되었습니다.");
			
		}else {
			System.out.println("등록 오류!");
		}
		
	}
	
	// 원하는 조건으로 정보를 읽는 메뉴
	private void conditionFindMenu(String product) {
		String condition = null;
		boolean isString = false;
		
		ProductInfo productInfo = ProductInfo.getInstance();
		ArrayList<String> productInfoList = productInfo.getProductInfo();
		
		System.out.println("product_code, name, price, season, grow_day");
		System.out.print("변경을 원하는 정보를 입력하세요: ");
		condition = sc.nextLine();
		
		if(condition.equals("name") || condition.equals("season")) {
			isString = true;
			
		}
		
		if(productInfoList.contains(condition)) {
			
			int result = serviceDao.updateProductInfo(condition, product, isString);
			
			if(result != 0) {
				System.out.println(product + "의 " + condition + " 정보가 변경 되었습니다.");
				
			}else {
				System.out.println("변경오류");
				
			}
		}else {
			System.out.println("해당 정보는 변경이 불가능 합니다.");
			
		}
	}
	
	private void modifyProductInfo() {
		String product = null;
		
		System.out.print("변경을 원하는 품목의 이름을 입력하세요: ");
		product = sc.nextLine();
		
		
		if(serviceDao.checkProductKind(product) != 0) {
			conditionFindMenu(product);
			
		}else {
			System.out.println(product + "(이)라는 품목은 존재 하지 않습니다.");
			
		}
	}
	
	private void removeProduct() {
		System.out.print("삭제하실 상품의 이름을 입력하세요: ");
		String productName = sc.nextLine();
		if(serviceDao.checkProductKind(productName) != 0) {
			serviceDao.deleteProduct(productName);
			
		}else {
			System.out.println("해당 상품은 존재하지 않습니다.");
			
		}
	}
}