package com.sylvanoid.joblib;

import java.util.Objects;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;

public class MatterPair implements Comparable<MatterPair> {
	private Matter m1;
	private Matter m2;

	@Override
	public int compareTo(MatterPair o) {
		if (distanceByradius() > o.distanceByradius()) {
			return 1;
		} else if (distanceByradius() < o.distanceByradius()) {
			return -1;
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object object) {
		boolean isEqual = false;
		if (object != null && object instanceof MatterPair) {
			MatterPair o = (MatterPair) object;
			isEqual = (m1 == o.getM1() && m2 == o.getM2())
					|| (m1 == o.getM2() && m2 == o.getM1());
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(m1, m2);
	}

	public MatterPair(Matter m1, Matter m2) {
		this.m1 = m1;
		this.m2 = m2;
	}

	public Matter getM1() {
		return m1;
	}

	public void setM1(Matter m1) {
		this.m1 = m1;
	}

	public Matter getM2() {
		return m2;
	}

	public void setM2(Matter m2) {
		this.m2 = m2;
	}

	public void impact(double Cr) {
		Vector3d newSpeed1 = m1.speedAfterImpactWith(m2, Cr);
		Vector3d newSpeed2 = m2.speedAfterImpactWith(m1, Cr);
		m1.setSpeed(newSpeed1);
		m2.setSpeed(newSpeed2);
	}

	
	
	public double distance() {
		return HelperNewton.distance(m1, m2);
	}

	public double distanceByradius() {
		return HelperNewton.distance(m1, m2) / (m1.getRayon() + m2.getRayon());
	}

}
