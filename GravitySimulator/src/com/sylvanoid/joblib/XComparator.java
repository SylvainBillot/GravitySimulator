package com.sylvanoid.joblib;
import java.util.Comparator;
public class XComparator implements Comparator<Matter> {
	@Override
	public int compare(Matter arg0, Matter arg1) {
		// TODO Auto-generated method stub
		return arg0.getPoint().x>arg1.getPoint().x?-1:1;
	}
}
