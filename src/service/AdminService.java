package service;

import java.util.ArrayList;
import java.util.Scanner;

import dao.ServiceDao;
import dto.ProductInfo;
import dto.ProductKind;
import dto.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminService {
	private final Scanner sc;
	private final ServiceDao serviceDao;
	
	public void displayAdminMenu() {
		ArrayList<ProductKind> productList = null;
		
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
				new SortImpl(sc).executeSortByPrice(productList);
			}else if(choice == 5) {
				modifyProductInfo();
			}else if(choice == 99) {
				break;
			}else {
				System.out.println("?");
			}
			
			System.out.println();
			
		}
	}
	
	private void showAllUserInfo(ArrayList<User> userList) {
		
		userList.forEach(System.out::println);
//		Stream<User> userStream = userList.stream();
//		userStream.forEach(System.out::println);
		
	}
	
	private void showAllProductKind(ArrayList<ProductKind> productList) {
		
//		productList.forEach(i -> System.out.println(i));
		productList.forEach(System.out::println);
		
//		Consumer<ArrayList<ProductKind>> c = t -> t.forEach(System.out::println);
//		c.accept(productList);
		
	}
	
	private void addProductKind() {
		int productCode = 0;
		String name = null;
		
		System.out.print("추가하실 품목의 코드를 입력하세요: ");
		productCode = sc.nextInt();
		sc.nextLine();
		System.out.print("추가하실 품목의 이름을 입력하세요: ");
		name = sc.nextLine();
		
		ArrayList<ProductKind> productList = serviceDao.getAllProductKindInfo();
		
		for(ProductKind product : productList) {
			if(product.getProduct_code() == productCode) {
				System.out.println("현재 같은 코드의 품목이 존재합니다.");
				return;
				
			}else if(product.getName().equals(name)) {
					System.out.println("현재 같은 이름의 품목이 존재합니다.");
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
			if(serviceDao.updateProductInfo(condition, product, isString) != 0) { // 업데이트 성공
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
}