package com.sylvanoid.common;

import java.nio.DoubleBuffer;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
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

	public static DoubleBuffer matrix16(double angle, Point3d ptr) {
		// double x = ptr.x;
		// double y = ptr.y;
		// double z = ptr.z;
		double a = angle;
		double[] matrix = new double[16];
		matrix[0] = Math.cos(a);
		matrix[1] = 0;
		matrix[2] = -Math.sin(a);
		matrix[3] = 0;

		matrix[4] = 0;
		matrix[5] = 1;
		matrix[6] = 0;
		matrix[7] = 0;

		matrix[8] = Math.sin(a);
		matrix[9] = 0;
		matrix[10] = Math.cos(a);
		matrix[11] = 0;

		matrix[12] = 0;
		matrix[13] = 0;
		matrix[14] = 0;
		matrix[15] = 1;

		return DoubleBuffer.wrap(matrix);
	}

}
