package main;

import java.util.Scanner;

import dao.ServiceDao;
import db.DBConnectionMgr;
import service.ChangeStreamImpl;
import service.Service;
import service.UserService;
import service.SplitImpl;

public class Main {
	public static void main(String[] args) {
		
		/*
		 * [구현해야 될 목록]
		 * 관리자 목록 만들고
		 * 관리자가 구매 가능한 품목 늘릴 수 있게 구현하기
		 * 
		 */
		Scanner sc = new Scanner(System.in);
		Service service = new UserService(sc, new ServiceDao(DBConnectionMgr.getInstance(), sc), new SplitImpl(sc, new ChangeStreamImpl()));
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