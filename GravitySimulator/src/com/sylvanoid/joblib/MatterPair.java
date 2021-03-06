package com.sylvanoid.joblib;

import java.util.Objects;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;
import com.sylvanoid.common.HelperVector;

public class MatterPair implements Comparable<MatterPair> {
    private Matter m1;
    private Matter m2;
    private Parameters parameters;

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
	    isEqual = (m1 == o.getM1() && m2 == o.getM2()) || (m1 == o.getM2() && m2 == o.getM1());
	}
	return isEqual;
    }

    @Override
    public int hashCode() {
	return Objects.hash(m1, m2);
    }

    public MatterPair() {

    }

    public MatterPair(Matter m1, Matter m2, Parameters parameters) {
	this.m1 = m1;
	this.m2 = m2;
	this.parameters = parameters;
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

	disableAcceleration();

	// Vector3d relativeSpeed = new Vector3d(m1.getSpeed());
	// relativeSpeed.sub(m2.getSpeed());

	// Vector3d radialSpeed = new Vector3d(m2.getPoint());
	// radialSpeed.sub(m1.getPoint());
	// radialSpeed.normalize();
	// double u = relativeSpeed.dot(radialSpeed);
	// if (u > 0) {
	Vector3d newSpeed1 = m1.speedAfterImpactWith(m2, Cr);
	Vector3d newSpeed2 = m2.speedAfterImpactWith(m1, Cr);
	m1.setSpeed(newSpeed1);
	m2.setSpeed(newSpeed2);
	// } else {
	// ???
	// }
    }

    public void applyViscosity() {

	double theta = (m1.getViscosity() * m1.getMass() + m2.getViscosity() * m2.getMass())
		/ (m1.getMass() + m2.getMass());
	double beta = 0;

	Vector3d relativeSpeed = new Vector3d(m1.getSpeed());
	relativeSpeed.sub(m2.getSpeed());

	Vector3d radialSpeed = new Vector3d(m2.getPoint());
	radialSpeed.sub(m1.getPoint());
	radialSpeed.normalize();
	double u = relativeSpeed.dot(radialSpeed);
	// if (u > 0) {
	/*
	 * double delta = parameters.getTimeFactor() (1 - distanceByradius())
	 * (theta * u + beta * net.jafama.FastMath.pow2(u));
	 */

	double delta = (1 - distanceByradius()) * (theta * u + beta * net.jafama.FastMath.pow2(u));

	Vector3d radialSpeedM1 = new Vector3d(radialSpeed);
	radialSpeedM1.scale(delta * m2.getMass() / (m1.getMass() + m2.getMass()));
	Vector3d radialSpeedM2 = new Vector3d(radialSpeed);
	radialSpeedM2.scale(delta * m1.getMass() / (m1.getMass() + m2.getMass()));

	m1.getAccel().sub(radialSpeedM1);
	m2.getAccel().add(radialSpeedM2);
	// } else {
	// ???
	// }

    }

    public void moveForAvoidCollision() {
	double q = HelperNewton.distance(m1, m2)
		/ (parameters.getCollisionDistanceRatio() * (m1.getRadius() + m2.getRadius()));
	if (q < 1) {
	    double mass = m1.getMass() + m2.getMass();
	    Vector3d centroid = new Vector3d((m1.getPoint().x * m1.getMass() + m2.getPoint().x * m2.getMass()) / mass,
		    (m1.getPoint().y * m1.getMass() + m2.getPoint().y * m2.getMass()) / mass,
		    (m1.getPoint().z * m1.getMass() + m2.getPoint().z * m2.getMass()) / mass);
	    m1.getPoint().sub(centroid);
	    m2.getPoint().sub(centroid);

	    double nl1 = m1.getPoint().length() / (q / 1.01);
	    double nl2 = m2.getPoint().length() / (q / 1.01);

	    m1.getPoint().normalize();
	    m2.getPoint().normalize();
	    m1.getPoint().scale(nl1);
	    m2.getPoint().scale(nl2);

	    m1.getPoint().add(centroid);
	    m2.getPoint().add(centroid);
	}
    }

    public double distance() {
	return HelperNewton.distance(m1, m2);
    }

    private double distanceByradius() {
	return HelperNewton.distance(m1, m2)
		/ (parameters.getCollisionDistanceRatio() * (m1.getRadius() + m2.getRadius()));
    }

    private void disableAcceleration() {
	double attraction = HelperNewton.attraction(m1, m2, parameters);
	m1.getSpeed().sub(HelperVector.acceleration(m1.getPoint(), m2.getPoint(), attraction));
	attraction = HelperNewton.attraction(m2, m1, parameters);
	m2.getSpeed().sub(HelperVector.acceleration(m2.getPoint(), m1.getPoint(), attraction));
	;
    }
}
