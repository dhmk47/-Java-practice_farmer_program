package service;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ChangeStreamImpl implements ChangeStream{
	@Override
	public <T> Stream<T> changeStream(ArrayList<T> myProductList) {
		Stream<T> myProductStream = myProductList.stream();
		return myProductStream;
	}
}
