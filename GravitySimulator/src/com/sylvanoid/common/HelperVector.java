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

	public static double[] angles(Vector3d obj1, Vector3d obj2) {
		double values[] = new double[2];
		double x = obj2.x - obj1.x;
		double y = obj2.y - obj1.y;
		double z = obj2.z - obj1.z;
		values[0] = Math.atan2(y, x);
		values[1] = Math.atan2(Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5),
				z);
		return values;
	}

	public static Vector3d acceleration(Vector3d obj1, Vector3d obj2, double attraction) {
		double angles[] = new double[2];
		angles = HelperVector.angles(obj1, obj2);
		return new Vector3d(attraction * Math.cos(angles[0]) * Math.sin(angles[1]),
				attraction * Math.sin(angles[0]) * Math.sin(angles[1]), attraction
						* Math.cos(angles[1]));
	}

	public static Vector3d addDouble(Vector3d vector, double toAdd){
		vector.x *= toAdd;
		vector.y *= toAdd;
		vector.z *= toAdd;
		return vector;
	}
	
	public static DoubleBuffer make3DTransformMatrix(Vector3d angles) {
		double[] matrix = new double[16];
		double x = angles.x;
		double y = angles.y;
		double z = angles.z;
		double cosx = Math.cos(x);
		double cosy = Math.cos(y);
		double cosz = Math.cos(z);
		double sinx = Math.sin(x);
		double siny = Math.sin(y);
		double sinz = Math.sin(z);

		matrix[0] = cosy * cosz;
		matrix[1] = -cosx * sinz + sinx * siny * cosz;
		matrix[2] = sinx * sinz + cosx * siny * cosz;
		matrix[3] = 0;

		matrix[4] = cosy * sinz;
		matrix[5] = cosx * cosz + sinx * siny * sinz;
		matrix[6] = -sinx * cosz + cosx * siny * sinz;
		matrix[7] = 0;

		matrix[8] = -siny;
		matrix[9] = sinx * cosy;
		matrix[10] = cosx * cosy;
		matrix[11] = 0;

		matrix[12] = 0;
		matrix[13] = 0;
		matrix[14] = 0;
		matrix[15] = 1;

		return DoubleBuffer.wrap(matrix);
	}

}
