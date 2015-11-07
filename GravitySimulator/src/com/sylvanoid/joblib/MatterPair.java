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

	public void applyViscosity() {
		double theta = (m1.getViscosity() * m1.getMass() + m2.getViscosity()
				* m2.getMass())
				/ (m1.getMass() + m2.getMass());
		double beta = 0;
		double ratio = 1 - distanceByradius();
		Vector3d relativeSpeed = new Vector3d(m1.getSpeed());
		relativeSpeed.sub(m2.getSpeed());

		Vector3d radialSpeed = new Vector3d(m2.getPoint());
		radialSpeed.sub(m1.getPoint());
		radialSpeed.normalize();
		double u = relativeSpeed.dot(radialSpeed);
		if (u > 0) {
			double delta = ratio
					* (theta * u + beta * net.jafama.FastMath.pow2(u));

			Vector3d radialSpeedM1 = new Vector3d(radialSpeed);
			radialSpeedM1.scale(delta * m2.getMass()
					/ (m1.getMass() + m2.getMass()));

			Vector3d radialSpeedM2 = new Vector3d(radialSpeed);
			radialSpeedM2.scale(delta * m1.getViscosity() * m1.getMass()
					/ (m1.getMass() + m2.getMass()));

			m1.getSpeed().sub(radialSpeedM1);
			m2.getSpeed().add(radialSpeedM2);
		}
	}

	private double distanceByradius() {
		return HelperNewton.distance(m1, m2)
				/ (m1.getParameters().getCollisionDistanceRatio() * (m1
						.getRayon() + m2.getRayon()));
	}

}
