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
public class ServiceImpl implements Service{
	private final Scanner sc;
	private final ServiceDao serviceDao;
	
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
			displayAdminMenu();
			
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
					userMap = showMyInfo(userMap);
					sellProduct(userMap);
					
				}else if(select == 2) {
					userMap = showMyInfo(userMap);
					ArrayList<MyProduct> myProduct = getMyProduct((UserMst)userMap.get("um"));
					showMyProduct(myProduct);
					
				}else if(select == 3) {
					
					
				}else {
					System.out.println("?");
					
				}
				
			}else if(choice == 3) {
				growProduct((UserMst)userMap.get("um"));
				
			}else if(choice == 4) {
				userMap = showMyInfo(userMap);
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
	
//	@Override
	private HashMap<String, User> showMyInfo(HashMap<String, User> userMap) {
		UserMst userMst = (UserMst)userMap.get("um");
		userMap = serviceDao.showMyInfo(userMst.getUsercode());
		
		return userMap;
	}
	
//	@Override
	private void purchaseProduct(HashMap<String, User> userMap) {
		System.out.print("구매할 상품을 입력하세요: ");
		String name = sc.nextLine();
		ProductKind produceKind = serviceDao.checkPurchaseProduct(name);
		MyProduct selectProduct = null;
		int productPrice = 0;
		boolean haveProduct = true;
		
		if(produceKind != null) {
			int price = produceKind.getPrice();
			userMap = showMyInfo(userMap);
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
				ArrayList<MyProduct> myProductList = getMyProduct(userMst);
				
				for(MyProduct myProduct : myProductList) {
					if(myProduct.getName().equals(name)) {
						selectProduct = myProduct;
						
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
				
				int result = serviceDao.purchaseProduct(produceKind, amount, userMst, name, selectProduct, productPrice, haveProduct);
				
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
	
//	@Override
	private int payMoney(int price, UserMst userMst, int myMoney) {
		
		return serviceDao.payMyMoney(price, userMst.getUsercode(), myMoney);
	}

//	@Override
	private void sellProduct(HashMap<String, User> userMap) {
		System.out.print("판매할 상품을 입력하세요: ");
		String sellProductName = sc.nextLine();
		ArrayList<MyProduct> myProductList = getMyProduct((UserMst)userMap.get("um"));
		int usercode = ((UserMst)userMap.get("um")).getUsercode();
		int result = 0;
		
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
	
	// 현재 내가 가지고 있는 품목들 보기
	private void showMyProduct(ArrayList<MyProduct> myProductList) {
		
		myProductList.forEach(i -> System.out.println(i));
		
		System.out.println("계속하시려면 엔터키 입력");
		sc.nextLine();
	}

	private void showeProductKind() {
		ArrayList<ProductKind> productList = serviceDao.checkPurchaseableProduct();
		
		productList.forEach(i -> System.out.println(i));
		
		System.out.println("계속하시려면 엔터키 입력");
		sc.nextLine();
	}
	
	private ArrayList<MyProduct> getMyProduct(UserMst userMst) {
		ArrayList<MyProduct> productList = serviceDao.checkmyProduct(userMst.getUsercode());
		
		return productList;
	}
	
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
		
		ArrayList<MyProduct> myProductList = getMyProduct(userMst);
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
	
	private void updateMyMoney(int price, int amount, UserMst userMst, int myMoney) {
		int income = price * amount;
		
		serviceDao.updateMyMoney(income + myMoney, userMst.getUsercode());
		
		System.out.println(income + "원을 벌었습니다.");
	}
	
	private void displayAdminMenu() {
		ArrayList<ProductKind> productList = null;
		
		while(true) {
			System.out.println("[관리자]");
			System.out.println("1. 현재 가입된 유저 정보 보기\n2. 현재 구매 가능한 품목 보기\n3. 구매 가능 품목 추가\n99 로그아웃\n4. 정렬보기");
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
				
			}else if(choice == 99) {
				break;
			}else if(choice == 4) {
				productList = serviceDao.getAllProductKindInfo();
				new SortImpl(sc).executeSortByPrice(productList);
			}else {
				System.out.println("?");
			}
			
			System.out.println();
			
		}
	}
	
	private void showAllUserInfo(ArrayList<User> userList) {
		
//		userList.forEach(i -> System.out.println(i));
		Stream<User> userStream = userList.stream();
		userStream.forEach(System.out::println);
		
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
	private void conditionFindMenu() {
		String condition = null;
		
		ProductInfo productInfo = ProductInfo.getInstance();
		ArrayList<String> productInfoList = productInfo.getProductInfo();
		
		System.out.println("원하는 정보를 입력하세요.");
		condition = sc.nextLine();
		
		
		if(productInfoList.contains(condition)) {
			
		}else {
			System.out.println("해당 정보는 변경이 불가능 합니다.");
		}
	}
}
