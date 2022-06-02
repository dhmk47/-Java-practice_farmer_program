package service;

import java.util.ArrayList;
import java.util.stream.Stream;

public interface ChangeStream {
	public <T> Stream<T> changeStream(ArrayList<T> myProductList);
}