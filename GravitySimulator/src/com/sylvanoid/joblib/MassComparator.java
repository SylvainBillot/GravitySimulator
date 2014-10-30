package com.sylvanoid.joblib;
import java.util.Comparator;
public class MassComparator implements Comparator<Matter> {
	@Override
	public int compare(Matter arg0, Matter arg1) {
		// TODO Auto-generated method stub
		return arg0.getMasse()>arg1.getMasse()?-1:1;
	}
}
