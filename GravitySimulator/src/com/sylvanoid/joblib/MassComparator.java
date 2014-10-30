package com.sylvanoid.joblib;
import java.util.Comparator;
public class MassComparator implements Comparator<Matiere> {
	@Override
	public int compare(Matiere arg0, Matiere arg1) {
		// TODO Auto-generated method stub
		return arg0.getMasse()>arg1.getMasse()?-1:1;
	}
}
