package com.sylvanoid.joblib;
import java.util.Comparator;
public class YComparator implements Comparator<Matiere> {
	@Override
	public int compare(Matiere arg0, Matiere arg1) {
		// TODO Auto-generated method stub
		return arg0.getY()>arg1.getY()?1:-1;
	}
}
