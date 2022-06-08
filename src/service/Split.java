package service;

import java.util.ArrayList;

import dto.Product;

public interface Split {
	public void splitBySomething(ArrayList<? extends Product> myProductList);
}