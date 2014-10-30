package com.sylvanoid.joblib;
import java.util.Comparator;
public class XComparator implements Comparator<Matiere> {
	@Override
	public int compare(Matiere arg0, Matiere arg1) {
		// TODO Auto-generated method stub
		return arg0.getX()>arg1.getX()?1:-1;
	}
}
