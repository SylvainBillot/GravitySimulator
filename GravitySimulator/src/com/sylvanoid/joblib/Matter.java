package com.sylvanoid.joblib;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement(name = "matter")
public class Matter implements Comparable<Matter> {
	private Parameters parameters;
	private String name;
	private TypeOfObject typeOfObject = TypeOfObject.Star;
	private double mass;
	private Vector3d pointBefore = new Vector3d(0, 0, 0);
	private Vector3d point = new Vector3d(0, 0, 0);
	private Vector3d angles = new Vector3d(0, 0, 0);
	private Vector3d speed = new Vector3d(0, 0, 0);
	private Vector3d color = new Vector3d(1, 1, 1);
	private double density;
	private double rayon;

	@Override
	public int compareTo(Matter o) {
		// TODO Auto-generated method stub
		if (mass < o.getMass()) {
			return 1;
		} else if (mass > o.getMass()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return name + " mass:" + mass + " x:" + point.x + " y:" + point.y
				+ " z:" + point.z + " vx:" + speed.x + " vy:" + speed.y
				+ " vz:" + speed.z;
	}

	public Matter() {

	}

	/* for subMap parameters only */
	public Matter(Vector3d point) {
		this.setPoint(point);
	}
	
	public Matter(Parameters parameters, Vector3d point, double mass,
			Vector3d speed, double density, boolean isDark) {
		this.parameters = parameters;
		this.setPoint(point);
		this.mass = mass;
		this.speed = speed;
		this.density = density;
		if (isDark) {
			typeOfObject = TypeOfObject.Dark;
		}
		this.name = "id: " + this.hashCode();
		this.rayon = Math.pow(3 * (mass / density) / (4 * Math.PI), (double) 1
				/ (double) 3);
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public TypeOfObject getTypeOfObject() {
		return typeOfObject;
	}

	public void setTypeOfObject(TypeOfObject typeOfObject) {
		this.typeOfObject = typeOfObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getPoint() {
		return point;
	}

	public void setPoint(Vector3d point) {
		this.point = point;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getPointBefore() {
		return pointBefore;
	}

	public void setPointBefore(Vector3d pointBefore) {
		this.pointBefore = pointBefore;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getAngles() {
		return angles;
	}

	public void setAngles(Vector3d angles) {
		this.angles = angles;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getColor() {
		return color;
	}

	public void setColor(Vector3d color) {
		this.color = color;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getSpeed() {
		return speed;
	}

	public void setSpeed(Vector3d speed) {
		this.speed = speed;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		rayon = Math.pow(3 * (mass / density) / (4 * Math.PI), (double) 1
				/ (double) 3);
		this.mass = mass;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public boolean isDark() {
		return typeOfObject == TypeOfObject.Dark;
	}

	public double getRayon() {
		return rayon;
	}

	public Vector3d getPlusV() {
		return new Vector3d(point.x + speed.x * parameters.getTimeFactor(),
				point.y + speed.y * parameters.getTimeFactor(), point.z
						+ speed.z * parameters.getTimeFactor());
	}

	public Vector3d getMinusV() {
		return new Vector3d(point.x - speed.x * parameters.getTimeFactor(),
				point.y - speed.y * parameters.getTimeFactor(), point.z
						- speed.z * parameters.getTimeFactor());
	}

	public Vector3d maxWithR() {
		return new Vector3d((point.x > getPlusV().x) ? (point.x + rayon)
				: (getPlusV().x + rayon),
				(point.y > getPlusV().y) ? (point.y + rayon)
						: (getPlusV().y + rayon),
				(point.z > getPlusV().z) ? (point.z + rayon)
						: (getPlusV().z + rayon));
	}

	public Vector3d minWithR() {
		return new Vector3d((point.x > getPlusV().x) ? (getPlusV().x - rayon)
				: (point.x - rayon),
				(point.y > getPlusV().y) ? (getPlusV().y - rayon)
						: (point.y - rayon),
				(point.z > getPlusV().z) ? (getPlusV().z - rayon)
						: (point.z - rayon));
	}

	public Vector3d max() {
		return new Vector3d((point.x > getPlusV().x) ? (point.x)
				: (getPlusV().x), (point.y > getPlusV().y) ? (point.y)
				: (getPlusV().y), (point.z > getPlusV().z) ? (point.z)
				: (getPlusV().z));
	}

	public Vector3d min() {
		return new Vector3d((point.x > getPlusV().x) ? (getPlusV().x)
				: (point.x), (point.y > getPlusV().y) ? (getPlusV().y)
				: (point.y), (point.z > getPlusV().z) ? (getPlusV().z)
				: (point.z));
	}

	public void move() {
		pointBefore = new Vector3d(point);
		// Relativity effet
		/*
		 * double gamma = Math.pow( 1 - speed.length() /
		 * Math.pow(HelperVariable.C, 2), 0.5); speed = new Vector3d(speed.x *
		 * gamma, speed.y * gamma, speed.z * gamma);
		 */
		// End of
		point = getPlusV();
	}

	public void fusion(Matter m) {
		point = new Vector3d((point.x * mass + m.getPoint().x * m.getMass())
				/ (mass + m.getMass()), (point.y * mass + m.getPoint().y
				* m.getMass())
				/ (mass + m.getMass()), (point.z * mass + m.getPoint().z
				* m.getMass())
				/ (mass + m.getMass()));
		speed = new Vector3d((speed.x * mass + m.getSpeed().x * m.getMass())
				/ (mass + m.getMass()), (speed.y * mass + m.getSpeed().y
				* m.getMass())
				/ (mass + m.getMass()), (speed.z * mass + m.getSpeed().z
				* m.getMass())
				/ (mass + m.getMass()));
		density = (density * mass + m.getDensity() * m.getMass())
				/ (mass + m.getMass());
		mass += m.getMass();
		rayon = Math.pow(3 * (mass / density) / (4 * Math.PI), (double) 1
				/ (double) 3);
	}

	public void impact(Matter m) {
		double Cr = parameters.getTypeOfImpact();
		double v1x = (Cr * m.getMass() * (m.getSpeed().x - speed.x) + mass
				* speed.x + m.getMass() * m.getSpeed().x)
				/ (mass + m.getMass());
		double v1y = (Cr * m.getMass() * (m.getSpeed().y - speed.y) + mass
				* speed.y + m.getMass() * m.getSpeed().y)
				/ (mass + m.getMass());
		double v1z = (Cr * m.getMass() * (m.getSpeed().z - speed.z) + mass
				* speed.z + m.getMass() * m.getSpeed().z)
				/ (mass + m.getMass());

		double v2x = (Cr * mass * (speed.x - m.getSpeed().x) + m.getMass()
				* m.getSpeed().x + mass * speed.x)
				/ (m.getMass() + mass);
		double v2y = (Cr * mass * (speed.y - m.getSpeed().y) + m.getMass()
				* m.getSpeed().y + mass * speed.y)
				/ (m.getMass() + mass);
		double v2z = (Cr * mass * (speed.z - m.getSpeed().z) + m.getMass()
				* m.getSpeed().z + mass * speed.z)
				/ (m.getMass() + mass);

		speed = new Vector3d(v1x, v1y, v1z);
		m.setSpeed(new Vector3d(v2x, v2y, v2z));
	}

	public boolean collision(Matter m) {
		if (new Point3d(point).distance(new Point3d(m.getPoint())) < rayon
				+ m.rayon
				|| new Point3d(getPlusV()).distance(new Point3d(m.getPlusV())) < rayon
						+ m.rayon) {
			return true;
		} else {
			if ((Math.max(max().x, m.max().x) - Math.min(min().x, m.min().x) <= (max().x
					- min().x + m.max().x - m.min().x))
					&& (Math.max(max().y, m.max().y)
							- Math.min(min().y, m.min().y) <= (max().y
							- min().y + m.max().y - m.min().y))
					&& (Math.max(max().z, m.max().z)
							- Math.min(min().z, m.min().z) <= (max().z
							- min().z + m.max().z - m.min().z))) {
				return true;
			}
		}
		return false;
	}

	public Vector3d orbitalSpeed(Matter m, Vector3d axis) {
		double orbitalSpeedValue = Math.pow(
				HelperVariable.G
						* Math.pow(m.getMass(), 2)
						/ ((mass + m.getMass()) * new Point3d(point)
								.distance(new Point3d(m.getPoint()))), 0.5);

		Vector3d accel = HelperVector.acceleration(point, m.getPoint(),
				orbitalSpeedValue);

		if (axis.x != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, 0, Math.signum(axis.x) * Math.PI / 2),
					Math.PI / 2);
		}
		if (axis.y != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, Math.signum(axis.y) * Math.PI / 2, 0),
					Math.signum(axis.y) * Math.PI / 2);
		}
		if (axis.z != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, 0, Math.signum(axis.z) * Math.PI / 2),
					Math.signum(axis.z) * Math.PI / 2);
		}

		return accel;
	}

	public Vector3d orbitalSpeed(Univers u, Vector3d axis) {
		u.computeCentroidOfUnivers();
		double orbitalSpeedValue = Math.pow(
				HelperVariable.G
						* Math.pow(u.getMass(), 2)
						/ ((mass + u.getMass()) * new Point3d(point)
								.distance(new Point3d(u.getGPoint()))), 0.5);

		Vector3d accel = HelperVector.acceleration(point, u.getGPoint(),
				orbitalSpeedValue);

		if (axis.x != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, 0, Math.signum(axis.x) * Math.PI / 2),
					Math.PI / 2);
		}
		if (axis.y != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, Math.signum(axis.y) * Math.PI / 2, 0),
					Math.signum(axis.y) * Math.PI / 2);
		}
		if (axis.z != 0) {
			accel = HelperVector.rotate(accel,
					new Vector3d(0, 0, Math.signum(axis.z) * Math.PI / 2),
					Math.signum(axis.z) * Math.PI / 2);
		}


		return accel;
	}
}
