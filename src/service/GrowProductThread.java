package service;

import java.util.Random;

import dao.ServiceDao;
import dto.MyProduct;
import dto.ProductKind;
import dto.UserMst;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrowProductThread implements Runnable{
	private final ProductKind productKind;
	private final ServiceDao serviceDao;
	private final UserMst userMst;
	private final MyProduct myProduct;
	private final int price;
	private final boolean haveProduct;
//	private final int result;
	
	@Override
	public synchronized void run() {
		Random random = new Random();
		int grow_day = productKind.getGrow_day();
		String name = productKind.getName();
		int randomAmount = random.nextInt(20) + 1;
//		System.out.println(productKind);
//		System.out.println(randomAmount);
//		System.out.println(userMst);
//		System.out.println(name);
//		System.out.println(myProduct);
		
		
		
		for(int i = 0; i < grow_day; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int result = serviceDao.purchaseProduct(productKind, randomAmount, userMst, name, myProduct, price, haveProduct);
		System.out.println("*************" + name + "재배 완료!*************");
		System.out.println(name + "을 " + randomAmount + "개를 재배했습니다.");
		if(result == 0) {
			System.out.println("재배오류!");
		}
		
	}

}