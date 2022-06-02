package service;

import java.util.ArrayList;

import dto.ProductKind;

public interface Sort {
	public void executeSortByPrice(ArrayList<ProductKind> productList);
	public void executeSortBySeason();
	public void executeSortByGrowDay();
	public void executeSortByAmount();
	public void executeSortByCode();
}