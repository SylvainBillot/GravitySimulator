package com.sylvanoid.joblib;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperVariable;

public class Matter implements Comparable<Matter> {
	private Point3d point = new Point3d(0, 0, 0);
	private double mass;
	private Vector3d a = new Vector3d(0, 0, 0);
	private Vector3d speed = new Vector3d(0, 0, 0);
	private double density;
	private boolean isDark;
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
		return "m:" + mass + " x:" + point.x + " y:" + point.y + " vx:"
				+ speed.x + " vy:" + speed.y;
	}

	public Matter(Point3d point, double mass, Vector3d speed, double density,
			boolean isDark) {
		this.setPoint(point);
		this.mass = mass;
		this.speed = speed;
		this.density = density;
		this.isDark = isDark;
		this.rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public Point3d getPoint() {
		return point;
	}

	public void setPoint(Point3d point) {
		this.point = point;
	}

	public Vector3d getA() {
		return a;
	}

	public void setA(Vector3d a) {
		this.a = a;
	}

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
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
		this.mass = mass;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public boolean isDark() {
		return isDark;
	}

	public void setDark(boolean isDark) {
		this.isDark = isDark;
	}

	public double getRayon() {
		return rayon;
	}

	public Point3d getPlusV() {
		return new Point3d(point.x + speed.x * HelperVariable.timeFactor,
				point.y + speed.y * HelperVariable.timeFactor, point.z
						+ speed.z * HelperVariable.timeFactor);
	}

	public Point3d getMinusV() {
		return new Point3d(point.x - speed.x * HelperVariable.timeFactor,
				point.y - speed.y * HelperVariable.timeFactor, point.z
						- speed.z * HelperVariable.timeFactor);
	}

	public Point3d maxWithR() {
		return new Point3d((point.x > getPlusV().x) ? (point.x + rayon)
				: (getPlusV().x + rayon),
				(point.y > getPlusV().y) ? (point.y + rayon)
						: (getPlusV().y + rayon),
				(point.z > getPlusV().z) ? (point.z + rayon)
						: (getPlusV().z + rayon));
	}

	public Point3d minWithR() {
		return new Point3d((point.x > getPlusV().x) ? (getPlusV().x - rayon)
				: (point.x - rayon),
				(point.y > getPlusV().y) ? (getPlusV().y - rayon)
						: (point.y - rayon),
				(point.z > getPlusV().z) ? (getPlusV().z - rayon)
						: (point.z - rayon));
	}

	public Point3d max() {
		return new Point3d((point.x > getPlusV().x) ? (point.x)
				: (getPlusV().x), (point.y > getPlusV().y) ? (point.y)
				: (getPlusV().y), (point.z > getPlusV().z) ? (point.z)
				: (getPlusV().z));
	}

	public Point3d min() {
		return new Point3d((point.x > getPlusV().x) ? (getPlusV().x)
				: (point.x), (point.y > getPlusV().y) ? (getPlusV().y)
				: (point.y), (point.z > getPlusV().z) ? (getPlusV().z)
				: (point.z));
	}

	public void move() {
		speed.add(a);
		// Relativity effet
		double gamma = Math.pow(
				1 - speed.length() / Math.pow(HelperVariable.C, 2), 0.5);
		speed = new Vector3d(speed.x * gamma, speed.y * gamma, speed.z * gamma);
		// End of
		point = getPlusV();
	}

	public void fusion(Matter m) {
		point = new Point3d(
				(point.x * mass + m.getPoint().getX() * m.getMass())
						/ (mass + m.getMass()), (point.y * mass + m.getPoint()
						.getY() * m.getMass())
						/ (mass + m.getMass()), (point.z * mass + m.getPoint()
						.getZ() * m.getMass())
						/ (mass + m.getMass()));
		speed = new Vector3d((speed.x * mass + m.getSpeed().x * m.getMass())
				/ (mass + m.getMass()), (speed.y * mass + m.getSpeed().y
				* m.getMass())
				/ (mass + m.getMass()), (speed.z * mass + m.getSpeed().z
				* m.getMass())
				/ (mass + m.getMass()));
		a = new Vector3d((a.x * mass + m.getA().x * m.getMass())
				/ (mass + m.getMass()), (a.y * mass + m.getA().y * m.getMass())
				/ (mass + m.getMass()), (a.z * mass + m.getA().z * m.getMass())
				/ (mass + m.getMass()));
		density = (density * mass + m.getDensity() * m.getMass())
				/ (mass + m.getMass());
		mass += m.getMass();
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public void impact(Matter m) {
		double Cr = HelperVariable.typeOfImpact;
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
		setA(new Vector3d(0, 0, 0));
		m.setA(new Vector3d(0, 0, 0));
	}

	public boolean collision(Matter m) {
		if (point.distance(m.getPoint()) < rayon + m.rayon
				|| getPlusV().distance(m.getPlusV()) < rayon + m.rayon) {
			return true;
		} else {
			if ((Math.max(max().x, m.max().x) - Math.min(min().x, m.min().x) <= (max().x
					- min().x + m.max().x - m.min().x))
					&& (Math.max(max().y, m.max().y)
							- Math.min(min().y, m.min().y) <= (max().y - min().y
							+ m.max().y - m.min().y))
					&& (Math.max(max().z, m.max().z)
							- Math.min(min().z, m.min().z) <= (max().z - min().z
							+ m.max().z - m.min().z))) {
				return true;
			} else {
				return false;
			}

		}
	}

	public double orbitalSpeed(Matter m) {
		double orbitalSpeed = Math
				.pow(HelperVariable.GRAVITY * Math.pow(m.getMass(), 2)
						/ ((mass + m.getMass()) * point.distance(m.getPoint())),
						0.5);
		return orbitalSpeed;
	}

}
