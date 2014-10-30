package com.sylvanoid.joblib;
import java.util.Comparator;
public class XComparator implements Comparator<Matter> {
	@Override
	public int compare(Matter arg0, Matter arg1) {
		// TODO Auto-generated method stub
		return arg0.getX()>arg1.getX()?1:-1;
	}
}
