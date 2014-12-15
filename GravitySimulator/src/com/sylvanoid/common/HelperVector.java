package com.sylvanoid.common;

import java.nio.DoubleBuffer;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public abstract class HelperVector {
	public static Vector3d rotate(Vector3d vector, Vector3d axis, double angle) {
		Matrix3d rotate = new Matrix3d();
		rotate.set(new AxisAngle4d(axis, angle));
		Vector3d result = new Vector3d();
		rotate.transform(vector, result);
		return result;
	}

	public static double angleTheta(double x, double y) {
		x = x == 0 ? Math.random() / 1E23 : y;
		return Math.atan2(y, x);
	}

	public static double anglePhi(double x, double y, double z) {
		z = z == 0 ? Math.random() / 1E23 : z;
		return Math.atan2(Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5), z);
	}

	/**
	 * 
	 * @param r
	 * @param u
	 * @param l
	 * @param p
	 * @return
	 */
	public static DoubleBuffer matrix16(Vector3d r,Vector3d u,Vector3d l, Vector3d p ) {
		double[] matrix = new double[16];
		matrix[0] = r.x;
		matrix[1] = r.y;
		matrix[2] = r.z;
		matrix[3] = 0;

		matrix[4] = u.x;
		matrix[5] = u.y;
		matrix[6] = u.z;
		matrix[7] = 0;

		matrix[8] = l.x;
		matrix[9] = l.y;
		matrix[10] = l.z;
		matrix[11] = 0;

		matrix[12] = p.x;
		matrix[13] = p.y;
		matrix[14] = p.z;
		matrix[15] = 1;

		return DoubleBuffer.wrap(matrix);
	}

}
