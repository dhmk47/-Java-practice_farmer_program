package main;

import java.util.Scanner;

import dao.ServiceDao;
import db.DBConnectionMgr;
import service.Service;
import service.ServiceImpl;

public class TestMain {
	public static void main(String[] args) {
		Service service = new ServiceImpl(new Scanner(System.in), new ServiceDao(DBConnectionMgr.getInstance(), new Scanner(System.in)));
		service.signin();
	}
}