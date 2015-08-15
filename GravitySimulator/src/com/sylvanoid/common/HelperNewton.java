package com.sylvanoid.common;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Parameters;
import com.sylvanoid.joblib.Univers;

public class HelperNewton {

	public static double distance(Matter m, Univers u) {
		return distance(new Point3d(m.getPoint()), new Point3d(u.getGPoint()));
	}

	public static double distance(Matter m1, Matter m2) {
		return distance(new Point3d(m1.getPoint()), new Point3d(m2.getPoint()));
	}

	public static double distance(Vector3d p, Matter m) {
		return distance(new Point3d(p), new Point3d(m.getPoint()));
	}

	public static double distance(Vector3d v1, Vector3d v2) {
		return distance(new Point3d(v1), new Point3d(v2));
	}

	public static double attraction(Matter m, Univers u, Parameters parameters) {
		return attraction(new Point3d(m.getPoint()),
				new Point3d(u.getGPoint()), u.getMass(), parameters);
	}

	public static double attraction(Matter m1, Matter m2, Parameters parameters) {
		return attraction(new Point3d(m1.getPoint()),
				new Point3d(m2.getPoint()), m2.getMass(), parameters);
	}
	
	public static double attraction(Vector3d p, Matter m, Parameters parameters) {
		return attraction(new Point3d(p), new Point3d(m.getPoint()),
				m.getMass(), parameters);
	}

	public static double attractionBefore(Matter m1, Matter m2, Parameters parameters) {
		return attraction(new Point3d(m1.getPointBefore()),
				new Point3d(m2.getPointBefore()), m2.getMass(), parameters);
	}	
	
	public static Vector3d medianPoint(Matter m, Matter m1) {
		double mpx = (m.getPoint().x * m1.getRayon() + m1.getPoint().x * m.getRayon())
				/ (m.getRayon() + m1.getRayon());
		double mpy = (m.getPoint().y * m1.getRayon() + m1.getPoint().y * m.getRayon())
				/ (m.getRayon() + m1.getRayon());
		double mpz = (m.getPoint().z * m1.getRayon() + m1.getPoint().z * m.getRayon())
				/ (m.getRayon() + m1.getRayon());

		return new Vector3d(mpx,mpy,mpz);
	}

	private static double distance(Point3d p1, Point3d p2) {
		return p1.distance(p2);
	}

	private static double attraction(Point3d p1, Point3d p2, double mass,
			Parameters parameters) {
		return parameters.getTimeFactor() * HelperVariable.G
				* ((mass / net.jafama.FastMath.pow2(distance(p1, p2))));
	}

}
