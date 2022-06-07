package main;

import java.util.Scanner;

import dao.ServiceDao;
import db.DBConnectionMgr;
import service.ChangeStreamImpl;
import service.Service;
import service.SortImpl;
import service.SplitImpl;
import service.UserService;

public class Main {
	public static void main(String[] args) {
		
		/*
		 * [구현해야 될 목록]
		 * 소스코드 간소화 하기
		 * 관리자 로그인 db 구현하기
		 * 
		 * 
		 * 
		 * [구현된 목록]
		 * -사용자-
		 * 
		 * -관리자-
		 * 관리자가 품목 가격 수정시 기존 가격보다 낮게 설정할 경우 그 만큼 모든 회원들에게 보상해주기(높게 설정했을땐 무시)
		 * 관리자가 품목 삭제하면 회원이 로그인할때 해당 항목 삭제되었다고 알림 띄어주기
		 * (삭제한 정보를 db에 저장해놓고 회원이 로그인 할 때마다 db정보를 읽어와서 데이터가 있다면
		 * 뽑아와서 해당 데이터의 정보를 알려주고 해당 정보의 가격만큼 돈 추가해주기)
		 * SQLDataException while문에 있는거 수정하기
		 * 관리자가 구매 가능한 품목 늘릴 수 있게 구현하기
		 * ㄴ 설정 불가능한 product_code 보여주기
		 * 관리자 정렬 구현하기
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		Service service = new UserService(sc, new ServiceDao(DBConnectionMgr.getInstance(), sc), new SplitImpl(sc, new ChangeStreamImpl()), new SortImpl(sc));
		String dot = "■■";
		while(true) {
			System.out.println("[농장 프로그램]");
			System.out.println("1.회원가입\n2. 로그인\n00. 프로그램 종료");
			int choice = sc.nextInt();
			sc.nextLine();
			if(choice == 1) {
				service.signup();
			}else if(choice == 2) {
				service.signin();
			}else if(choice == 00) {
				System.out.println("프로그램 종료중...");
				for(int i = 0; i < 10; i++) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.print(dot);
				}
				break;
			}else {
				System.out.println("?");
			}
			
			System.out.println();
			
		}
		System.out.println("\n프로그램이 종료 되었습니다.");
	}
}