package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

import dao.ServiceDao;
import dto.DeletedUserProduct;
import dto.MyProduct;
import dto.Product;
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
	private final Sort sort;
	
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
			AdminService adminService = new AdminService(sc, serviceDao, new SortImpl(sc));
			adminService.displayAdminMenu();
			
		}else {
			
			System.out.print("비밀번호: ");
			String password = sc.nextLine();
			HashMap<String, User> userMap = serviceDao.signinUser(username, password);
			
			if(userMap == null) {
				return;
			}
			displayMenu(userMap);
		}
		
	}

//	@Override
	private void displayMenu(HashMap<String, User> userMap) {
		int myUsercode = ((UserMst) userMap.get("um")).getUsercode();
		int totalCompensationMoney = 0;
		
		ArrayList<DeletedUserProduct> deletedMyProductList = serviceDao.checkDeletedUserProduct(myUsercode);
		
		if(!deletedMyProductList.isEmpty()) {
			
			for(DeletedUserProduct deletedMyProduct : deletedMyProductList) {
				int compensationMoney = deletedMyProduct.getPurchase_price() * deletedMyProduct.getAmount();
				totalCompensationMoney += compensationMoney;
				
				System.out.println(deletedMyProduct.getName() + " 품목이 삭제되어 " + compensationMoney + "원이 보상되었습니다.");
			}
			
			userMap = getMyInfo(userMap);
			int myMoney = ((UserDtl) userMap.get("ud")).getMoney();
			
			if(serviceDao.updateMyMoney(totalCompensationMoney + myMoney, myUsercode) != 0) {
				System.out.println("총 보상 금액: " + totalCompensationMoney);
				
				if(serviceDao.deleteUserProductData(myUsercode) == 0) {
					System.out.println("유저 삭제 품목 데이터 삭제오류");
					
				}
				
			} else {
				System.out.println("보상 오류");
				
			}
			
		}
			
		
		
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
					ArrayList<Product> myProduct = getMyProduct(((UserMst)userMap.get("um")).getUsercode());
					
					if(myProduct == null) {
						return;
					}
					
					System.out.println("1. 정렬로 보기\n2. 분할로 보기");
					int choiceMenu = sc.nextInt();
					sc.nextLine();
					
					if(choiceMenu == 1) {
						System.out.println("1. 가격순 정렬\n2. 계절순 정렬\n3. 개수 정렬\n4. 이름순 정렬\n5. 코드순 정렬");
						
						int choiceSortMenu = sc.nextInt();
						sc.nextLine();
						
						if(choiceSortMenu == 1) {
							sort.executeSortByPrice(myProduct);
						}else if(choiceSortMenu == 2) {
							sort.executeSortBySeason(myProduct);
						}else if(choiceSortMenu == 3) {
							sort.executeSortByAmount(myProduct.stream().map(o -> (MyProduct) o).collect(Collectors.toCollection(ArrayList::new)));
						}else if(choiceSortMenu == 4) {
							sort.executeSortByName(myProduct);
						}else if(choiceSortMenu == 5) {
							sort.executeSortByCode(myProduct);
						}else {
							System.out.println("존재하지 않는 명령어입니다.");
						}
						
					}else if(choiceMenu == 2) {
						split.splitBySomething(myProduct);
						
					}else {
						System.out.println("해당 명령어는 존재하지 않는 명령어 입니다.");
					}
					
					
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
				
				selectProduct = serviceDao.getMyProductByProductName(name, userMst.getUsercode());
				
				// 회원이 기존에 가지고 있는 품목을 추가로 구매한다면 금액설정을 할 필요 없습니다.
				if(selectProduct != null) {
					productPrice = selectProduct.getPrice();
					
				}else {	// 회원이 가지고 있지 않은 품목을 구매한다면
					System.out.print("금액을 입력하세요: ");
					productPrice = sc.nextInt();
					sc.nextLine();
					haveProduct = false;
					
				}
						
					// 불필요한 코드?
//					if(serviceDao.checkIfAlreadyHaveProduct(userMst.getUsercode(), name)) {
//						productPrice = selectProduct.getPrice();
//						
//					}
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
		
		MyProduct myProduct = serviceDao.getMyProductByProductName(sellProductName, usercode);
		
		if(myProduct != null) {
			if(myProduct.getAmount() == 0) {
				System.out.println("재고가 0개입니다.");
				return;
				
			}
			System.out.println("현재 개수/가격: " + myProduct.getAmount() + "/" + myProduct.getPrice());
			System.out.print("몇 개를 판매 하시겠습니까?");
			int sellAmount = sc.nextInt();
			sc.nextLine();
			
			if(myProduct.getAmount() < sellAmount) {
				System.out.println("현재 가지고 있는 갯수를 초과했습니다.");
				return;
				
			}else {
				int updateAmount = myProduct.getAmount() - sellAmount;
				result = serviceDao.sellMyProduct(usercode, updateAmount, sellProductName);
				
				if(result != 0) {
					updateMyMoney(myProduct.getPrice(), sellAmount, (UserMst)userMap.get("um"), ((UserDtl)userMap.get("ud")).getMoney());
					
				}
				
			}
			
		}else {
			System.out.println("해당 품목은 보유하고 있지 않습니다.");
		}
		
	}
	

	// 품목 종류 확인하기
	private void showeProductKind() {
		ArrayList<ProductKind> productList = serviceDao.checkPurchaseableProduct();
		
		if(productList.isEmpty()) {
			System.out.println("현재 구매 가능한 품목이 없습니다. 잠시만 기다려주세요");
			return;
		}
		
		productList.forEach(i -> System.out.println(i));
		
		System.out.println("계속하시려면 엔터키 입력");
		sc.nextLine();
	}
	
	
	// 가지고 있는 품목 가져오기
	private ArrayList<Product> getMyProduct(int usercode) {
		ArrayList<Product> productList = serviceDao.checkmyProduct(usercode);
		
		if(productList.isEmpty()) {
			System.out.println("현재 가지고 있는 품목이 없습니다.");
			return null;
		}
		
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
		
		MyProduct selectProduct = serviceDao.getMyProductByProductName(name, userMst.getUsercode());
		
		// 내가 가지고 있는 품목이 있다면 그 가격으로 재배해서 품목에 주가하기
		if(selectProduct != null){
			price = selectProduct.getPrice();
			
		}
			
		// 없다면 금액 설정
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
	
}