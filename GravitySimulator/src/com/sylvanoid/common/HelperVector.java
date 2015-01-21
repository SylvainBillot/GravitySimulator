package com.sylvanoid.common;

import java.nio.DoubleBuffer;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public abstract class HelperVector {
	public static Vector3d rotate(Vector3d vector,Vector3d centroid, Vector3d axis, double angle) {
		Vector3d newVector = new Vector3d(vector);
		newVector.sub(centroid);
		Matrix3d rotate = new Matrix3d();
		rotate.set(new AxisAngle4d(axis, angle));
		Vector3d result = new Vector3d();
		rotate.transform(newVector, result);
		result.add(centroid);
		return result;
	}

	public static Vector3d rotate(Vector3d vector, Vector3d axis, double angle) {
		Matrix3d rotate = new Matrix3d();
		rotate.set(new AxisAngle4d(axis, angle));
		Vector3d result = new Vector3d();
		rotate.transform(vector, result);
		return result;
	}
	
	public static Vector3d rThetaPhi(Vector3d obj1, Vector3d obj2) {
		Vector3d tmpVect = new Vector3d(obj2);
		tmpVect.sub(obj1);
		return new Vector3d(tmpVect.length(), Math.atan2(tmpVect.y, tmpVect.x),
				Math.atan2(Math.pow(
						Math.pow(tmpVect.x, 2) + Math.pow(tmpVect.y, 2), 0.5),
						tmpVect.z));
	}

	public static Vector3d polToCoord(double radius, double theta, double phi) {
		return new Vector3d(radius * Math.cos(theta) * Math.sin(phi), radius
				* Math.sin(theta) * Math.sin(phi), radius * Math.cos(phi));
	}

	public static Vector3d acceleration(Vector3d obj1, Vector3d obj2,
			double attraction) {
		Vector3d tmpVect = rThetaPhi(obj1, obj2); 
		return polToCoord(attraction, tmpVect.y, tmpVect.z);
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
