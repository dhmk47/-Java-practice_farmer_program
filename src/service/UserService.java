package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

import dao.ServiceDao;
import dto.MyProduct;
import dto.ProductInfo;
import dto.ProductKind;
import dto.User;
import dto.UserDtl;
import dto.UserMst;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService implements Service{
	private final Scanner sc;
	private final ServiceDao serviceDao;
	private final Split split;
	
	@Override
	public void signup() {
		System.out.println("[회원가입]");
		System.out.print("이름: ");
		String name = sc.nextLine();
		System.out.print("이메일: ");
		String email = sc.nextLine();
		System.out.print("아이디: ");
		String username = sc.nextLine();
		System.out.print("비밀번호: ");
		String password = sc.nextLine();
		
		if(serviceDao.signupUser(email, name, username, password) != 0) {
			System.out.println("회원가입이 정상적으로 이루어졌습니다.");
			
		}else {
			System.out.println("회원가입 오류");
			
		}
		
	}

	@Override
	public void signin() {
		System.out.println("[로그인]");
		System.out.print("아이디: ");
		String username = sc.nextLine();
		
		if(username.equals("9999")) {
			AdminService adminService = new AdminService(sc, serviceDao);
			adminService.displayAdminMenu();
			
		}else {
			
			System.out.print("비밀번호: ");
			String password = sc.nextLine();
			HashMap<String, User> userMap = serviceDao.signinUser(username, password);
			
			displayMenu(userMap);
		}
		
	}

//	@Override
	private void displayMenu(HashMap<String, User> userMap) {
		
		while(true) {
			
			try {
				System.out.println(((UserMst)userMap.get("um")).getName() + "님 환영합니다!");
				
			} catch (NullPointerException e) {
				return;
				
			}
			
			System.out.println("[메뉴]");
			System.out.println("1. 물품구매\n2. 물품판매\n3. 농작물 심기\n4. 정보보기\n99. 로그아웃");
			int choice = sc.nextInt();
			sc.nextLine();
			
			if(choice == 1) {
				
				System.out.println("1. 구매 가능 목록보기\n2. 물품 구매하기\n3. 뒤로가기");
				int select = sc.nextInt();
				sc.nextLine();
				
				if(select == 1) {
					showeProductKind();
					
				}else if(select == 2) {
					purchaseProduct(userMap);
					
				}else if(select == 3) {
					
					
				}else {
					System.out.println("?");
					
				}
				
			}else if(choice == 2) {
				System.out.println("1. 물품판매\n2. 판매 가능한 물품 보기\n3. 뒤로가기");
				int select = sc.nextInt();
				sc.nextLine();
				
				if(select == 1) {
					userMap = getMyInfo(userMap);
					sellProduct(userMap);
					
				}else if(select == 2) {
					userMap = getMyInfo(userMap);
					ArrayList<MyProduct> myProduct = getMyProduct(((UserMst)userMap.get("um")).getUsercode());
					split.splitBySomething(myProduct);
					
				}else if(select == 3) {
					
					
				}else {
					System.out.println("?");
					
				}
				
			}else if(choice == 3) {
				growProduct((UserMst)userMap.get("um"));
				
			}else if(choice == 4) {
				userMap = getMyInfo(userMap);
				userMap.forEach((k, v) -> System.out.println(v));
				System.out.println("계속하시려면 엔터키 입력");
				sc.nextLine();
				
			}else if(choice == 99) {
				System.out.println("로그아웃합니다.");
				break;
				
			}else {
				System.out.println("?");
				
			}
			
		}
		
	}
	
	// 내 정보 보기
//	@Override
	private HashMap<String, User> getMyInfo(HashMap<String, User> userMap) {
		UserMst userMst = (UserMst)userMap.get("um");
		userMap = serviceDao.getUserInfo(userMst.getUsercode());
		
		return userMap;
	}
	
	// 물품 구매하기
//	@Override
	private void purchaseProduct(HashMap<String, User> userMap) {
		System.out.print("구매할 상품을 입력하세요: ");
		String name = sc.nextLine();
		ProductKind productKind = serviceDao.checkPurchaseProduct(name);
		MyProduct selectProduct = null;
		int productPrice = 0;
		boolean haveProduct = true;
		
		if(productKind != null) {
			int price = productKind.getPrice();
			userMap = getMyInfo(userMap);
			UserMst userMst = (UserMst)userMap.get("um");
			UserDtl userDtl = (UserDtl)userMap.get("ud");
			int myMoney = userDtl.getMoney();
			
			System.out.println(name + "의 개당 가격: " + price);
			System.out.print("몇개를 구매하시겠습니까? 현재 보유 금액: " + myMoney);
			System.out.print("입력: ");
			int amount = sc.nextInt();
			sc.nextLine();
			
			price *= amount;
			
			if(myMoney > price) {
				ArrayList<MyProduct> myProductList = getMyProduct(userMst.getUsercode());
				
				for(MyProduct myProduct : myProductList) {
					if(myProduct.getName().equals(name)) {
						selectProduct = myProduct;
						break;
						
					}
					
				}
				
				if(serviceDao.checkIfAlreadyHaveProduct(userMst.getUsercode(), name)) {
					productPrice = selectProduct.getPrice();
					
				}else {
					System.out.print("금액을 입력하세요: ");
					productPrice = sc.nextInt();
					sc.nextLine();
					haveProduct = false;
					
				}
				
				int result = serviceDao.purchaseProduct(productKind, amount, userMst, name, selectProduct, productPrice, haveProduct);
				
				if(result != 0) {
					System.out.println(name + "이라는 상품을 " + amount + "개를 구매했습니다.");
					
				}else {
					System.out.println("구매 오류!");
					
				}
				if(payMoney(price, userMst, myMoney) == 0) {
					System.out.println("금액 지불 오류");
					
				}
			}else {
				int needMoney = price - myMoney;
				System.out.println("금액이 부족합니다.");
				System.out.print("부족 금액: " + needMoney);
				
			}
			
		}else {
			return;
			
		}
		
	}
	
	// 돈 지불하기
//	@Override
	private int payMoney(int price, UserMst userMst, int myMoney) {
		
		return serviceDao.payMyMoney(price, userMst.getUsercode(), myMoney);
	}

	// 물품 판매하기
//	@Override
	private void sellProduct(HashMap<String, User> userMap) {
		System.out.print("판매할 상품을 입력하세요: ");
		String sellProductName = sc.nextLine();
		
		int usercode = ((UserMst)userMap.get("um")).getUsercode();
		int result = 0;
		
//		MyProduct myProduct = serviceDao.getMyProductByProductName(sellProductName, usercode);
		
//		if(myProduct == null) {
//			System.out.println(sellProductName + "은 보유하고 있지 않은 품목입니다.");
//		}
		
		ArrayList<MyProduct> myProductList = getMyProduct(usercode);
		
		for(MyProduct myProduct : myProductList) {
			
			if(myProduct.getName().equals(sellProductName)) {
				
				if(myProduct.getAmount() == 0) {
					System.out.println("재고가 0개입니다.");
					break;
					
				}
				
				System.out.println("현재 개수/가격: " + myProduct.getAmount() + "/" + myProduct.getPrice());
				System.out.print("몇 개를 판매 하시겠습니까?");
				int sellAmount = sc.nextInt();
				sc.nextLine();
				
				if(myProduct.getAmount() < sellAmount) {
					System.out.println("현재 가지고 있는 갯수를 초과했습니다.");
					break;
					
				}else {
					int updateAmount = myProduct.getAmount() - sellAmount;
					result = serviceDao.sellMyProduct(usercode, updateAmount, sellProductName);
					
					if(result != 0) {
						updateMyMoney(myProduct.getPrice(), sellAmount, (UserMst)userMap.get("um"), ((UserDtl)userMap.get("ud")).getMoney());
						
					}
					else if(result == 0) {
						System.out.println("해당 품목은 보유하고 있지 않습니다.");
						
					}
					
					break;
				}
				
			}
			
		}
		
	}
	

	// 품목 종류 확인하기
	private void showeProductKind() {
		ArrayList<ProductKind> productList = serviceDao.checkPurchaseableProduct();
		
		productList.forEach(i -> System.out.println(i));
		
		System.out.println("계속하시려면 엔터키 입력");
		sc.nextLine();
	}
	
	
	// 가지고 있는 품목 가져오기
	private ArrayList<MyProduct> getMyProduct(int usercode) {
		ArrayList<MyProduct> productList = serviceDao.checkmyProduct(usercode);
		
		return productList;
	}
	
	// 재배하기
	private void growProduct(UserMst userMst) {
		int price = 0;
		boolean haveProduct = true;
		System.out.println("[재배 가능한 품목]");
		
		showeProductKind();
		
		System.out.print("재배 하고 싶은 품목을 입력하세요: ");
		String name = sc.nextLine();
		ProductKind productKind = serviceDao.checkPurchaseProduct(name);
		
		if(productKind == null) {
			return;
			
		}
		
		ArrayList<MyProduct> myProductList = getMyProduct(userMst.getUsercode());
		MyProduct selectProduct = null;
		
		for(MyProduct myProduct : myProductList) {
			
			if(myProduct.getName().equals(name)) {
				selectProduct = myProduct;
				price = selectProduct.getPrice();
				break;
				
			}
			
		}
		
		if(price == 0) {
			System.out.print("금액을 입력하세요: ");
			price = sc.nextInt();
			sc.nextLine();
			haveProduct = false;
			
		}
//		int result = serviceDao.purchaseProduct(productKind, 10, userMst, name, selectProduct);
		Thread growProductThread = new Thread(new GrowProductThread(productKind, serviceDao, userMst, selectProduct, price, haveProduct));
		growProductThread.start();
		
	}
	
	
	// 돈 업데이트
	private void updateMyMoney(int price, int amount, UserMst userMst, int myMoney) {
		int income = price * amount;
		
		serviceDao.updateMyMoney(income + myMoney, userMst.getUsercode());
		
		System.out.println(income + "원을 벌었습니다.");
	}
	
//	private void displayAdminMenu() {
//		ArrayList<ProductKind> productList = null;
//		
//		while(true) {
//			System.out.println("[관리자]");
//			System.out.println("1. 현재 가입된 유저 정보 보기\n2. 현재 구매 가능한 품목 보기\n3. 구매 가능 품목 추가"
//					+ "\n4. 정렬보기\n5. 상품 정보 수정\n99 로그아웃");
//			int choice = sc.nextInt();
//			sc.nextLine();
//			
//			if(choice == 1) {
//				ArrayList<User> userList = serviceDao.getAllUserInfo();
//				
//				showAllUserInfo(userList);
//				
//			}else if(choice == 2) {
//				productList = serviceDao.getAllProductKindInfo();
//				
//				showAllProductKind(productList);
//				
//			}else if(choice == 3) {
//				addProductKind();
//				
//			}else if(choice == 4) {
//				productList = serviceDao.getAllProductKindInfo();
//				new SortImpl(sc).executeSortByPrice(productList);
//			}else if(choice == 5) {
//				modifyProductInfo();
//			}else if(choice == 99) {
//				break;
//			}else {
//				System.out.println("?");
//			}
//			
//			System.out.println();
//			
//		}
//	}
//	
//	private void showAllUserInfo(ArrayList<User> userList) {
//		
//		userList.forEach(System.out::println);
////		Stream<User> userStream = userList.stream();
////		userStream.forEach(System.out::println);
//		
//	}
//	
//	private void showAllProductKind(ArrayList<ProductKind> productList) {
//		
////		productList.forEach(i -> System.out.println(i));
//		productList.forEach(System.out::println);
//		
////		Consumer<ArrayList<ProductKind>> c = t -> t.forEach(System.out::println);
////		c.accept(productList);
//		
//	}
//	
//	private void addProductKind() {
//		int productCode = 0;
//		String name = null;
//		
//		System.out.print("추가하실 품목의 코드를 입력하세요: ");
//		productCode = sc.nextInt();
//		sc.nextLine();
//		System.out.print("추가하실 품목의 이름을 입력하세요: ");
//		name = sc.nextLine();
//		
//		ArrayList<ProductKind> productList = serviceDao.getAllProductKindInfo();
//		
//		for(ProductKind product : productList) {
//			if(product.getProduct_code() == productCode) {
//				System.out.println("현재 같은 코드의 품목이 존재합니다.");
//				return;
//				
//			}else if(product.getName().equals(name)) {
//					System.out.println("현재 같은 이름의 품목이 존재합니다.");
//					return;
//					
//			}
//			
//		}
//		if(serviceDao.addProductKind(productCode, name) != 0) {
//			System.out.println(name + "(이)라는 상품을 코드 " + productCode + "으로 등록 되었습니다.");
//			
//		}else {
//			System.out.println("등록 오류!");
//		}
//		
//	}
//	
//	// 원하는 조건으로 정보를 읽는 메뉴
//	private void conditionFindMenu(String product) {
//		String condition = null;
//		boolean isString = false;
//		
//		ProductInfo productInfo = ProductInfo.getInstance();
//		ArrayList<String> productInfoList = productInfo.getProductInfo();
//		
//		System.out.println("product_code, name, price, season, grow_day");
//		System.out.print("변경을 원하는 정보를 입력하세요: ");
//		condition = sc.nextLine();
//		
//		if(condition.equals("name") || condition.equals("season")) {
//			isString = true;
//			
//		}
//		
//		if(productInfoList.contains(condition)) {
//			if(serviceDao.updateProductInfo(condition, product, isString) != 0) { // 업데이트 성공
//				System.out.println(product + "의 " + condition + " 정보가 변경 되었습니다.");
//				
//			}else {
//				System.out.println("변경오류");
//				
//			}
//		}else {
//			System.out.println("해당 정보는 변경이 불가능 합니다.");
//			
//		}
//	}
//	
//	private void modifyProductInfo() {
//		String product = null;
//		
//		System.out.print("변경을 원하는 품목의 이름을 입력하세요: ");
//		product = sc.nextLine();
//		
//		
//		if(serviceDao.checkProductKind(product) != 0) {
//			conditionFindMenu(product);
//			
//		}else {
//			System.out.println(product + "(이)라는 품목은 존재 하지 않습니다.");
//			
//		}
//	}
}
