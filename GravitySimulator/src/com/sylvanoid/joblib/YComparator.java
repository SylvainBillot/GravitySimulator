package com.sylvanoid.joblib;
import java.util.Comparator;
public class YComparator implements Comparator<Matter> {
	@Override
	public int compare(Matter arg0, Matter arg1) {
		// TODO Auto-generated method stub
		return arg0.getY()>arg1.getY()?1:-1;
	}
}
