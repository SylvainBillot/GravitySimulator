package com.sylvanoid.common;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sylvanoid.joblib.Matter;
import com.sylvanoid.joblib.Parameters;
import com.sylvanoid.joblib.Univers;

public class HelperNewton {

	public static double distance(Matter m, Univers u) {
		return new Point3d(m.getPoint()).distance(new Point3d(u.getGPoint()));
	}

	public static double distance(Matter m1, Matter m2) {
		return new Point3d(m1.getPoint()).distance(new Point3d(m2.getPoint()));
	}

	public static double distance(Vector3d p, Matter m) {
		return new Point3d(p).distance(new Point3d(m.getPoint()));
	}

	public static double attraction(Matter m, Univers u, Parameters parameters) {
		return parameters.getTimeFactor() * HelperVariable.G
				* (((u.getMass()) / net.jafama.FastMath.pow2(distance(m, u))));
	}

	public static double attraction(Matter m1, Matter m2, Parameters parameters) {
		return parameters.getTimeFactor()
				* HelperVariable.G
				* (((m2.getMass()) / net.jafama.FastMath.pow2(distance(m1, m2))));
	}

	public static double attraction(Vector3d p, Matter m, Parameters parameters) {
		return parameters.getTimeFactor() * HelperVariable.G
				* (((m.getMass()) / net.jafama.FastMath.pow2(distance(p, m))));
	}
}
