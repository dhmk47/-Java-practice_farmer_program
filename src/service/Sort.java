package service;

import java.util.ArrayList;

import dto.MyProduct;
import dto.Product;
import dto.ProductKind;

public interface Sort {
	public void executeSortByPrice(ArrayList<? extends Product> productList);
	public void executeSortBySeason(ArrayList<? extends Product> productList);
	public void executeSortByGrowDay(ArrayList<ProductKind> productList);
	public void executeSortByAmount(ArrayList<MyProduct> productList);
	public void executeSortByCode(ArrayList<? extends Product> productList);
	public void executeSortByName(ArrayList<? extends Product> productList);
}