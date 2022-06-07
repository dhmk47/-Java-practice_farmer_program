package service;

import java.util.ArrayList;

import dto.Product;

public interface Sort {
	public void executeSortByPrice(ArrayList<Product> productList);
	public void executeSortBySeason(ArrayList<Product> productList);
	public void executeSortByGrowDay(ArrayList<Product> productList);
	public void executeSortByAmount(ArrayList<Product> productList);
	public void executeSortByCode(ArrayList<Product> productList);
	public void executeSortByName(ArrayList<Product> productList);
}