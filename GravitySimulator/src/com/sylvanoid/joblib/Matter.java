package com.sylvanoid.joblib;

import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import com.sylvanoid.common.HelperVariable;

public class Matter implements Comparable<Matter> {
	private Point2d point = new Point2d(0,0);
	private double mass;
	private Vector2d a = new Vector2d(0,0);
	private Vector2d speed = new Vector2d(0,0);
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

	public Matter(Point2d point, double mass, Vector2d speed, double density,
			boolean isDark) {
		this.setPoint(point);
		this.mass = mass;
		this.speed = speed;
		this.density = density;
		this.isDark = isDark;
		this.rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public Point2d getPoint() {
		return point;
	}

	public void setPoint(Point2d point) {
		this.point = point;
	}

	public Vector2d getA() {
		return a;
	}

	public void setA(Vector2d a) {
		this.a = a;
	}

	public Vector2d getSpeed() {
		return speed;
	}

	public void setSpeed(Vector2d speed) {
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

	public Point2d getPlusV() {
		return new Point2d(point.x + speed.x * HelperVariable.timeFactor,
				point.y + speed.y * HelperVariable.timeFactor);
	}

	public Point2d getMinusV() {
		return new Point2d(point.x - speed.x * HelperVariable.timeFactor,
				point.y - speed.y * HelperVariable.timeFactor);
	}

	public Point2d max() {
		return new Point2d((point.x > getPlusV().x) ? (point.x + rayon)
				: (getPlusV().x + rayon),
				(point.y > getPlusV().y) ? (point.y + rayon)
						: (getPlusV().y + rayon));
	}

	public Point2d min() {
		return new Point2d((point.x > getPlusV().x) ? (getPlusV().x - rayon)
				: (point.x - rayon),
				(point.y > getPlusV().y) ? (getPlusV().y - rayon)
						: (point.y - rayon));
	}

	public void move() {
		speed.add(a);
		// Relativity effet
		double gamma = Math.pow(
				1 - speed.length() / Math.pow(HelperVariable.C, 2), 0.5);
		speed = new Vector2d(speed.x * gamma, speed.y * gamma);
		// End of

		point.add(new Vector2d(speed.x * HelperVariable.timeFactor, speed.y
				* HelperVariable.timeFactor));
	}

	public void fusion(Matter m) {
		point = new Point2d(
				(point.x * mass + m.getPoint().getX() * m.getMass())
						/ (mass + m.getMass()), (point.y * mass + m.getPoint()
						.getY() * m.getMass())
						/ (mass + m.getMass()));
		speed = new Vector2d((speed.x * mass + m.getSpeed().x * m.getMass())
				/ (mass + m.getMass()), (speed.y * mass + m.getSpeed().y
				* m.getMass())
				/ (mass + m.getMass()));
		a = new Vector2d((a.x * mass + m.getA().x * m.getMass())
				/ (mass + m.getMass()), (a.y * mass + m.getA().y * m.getMass())
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

		double v2x = (Cr * mass * (speed.x - m.getSpeed().x) + m.getMass()
				* m.getSpeed().x + mass * speed.x)
				/ (m.getMass() + mass);
		double v2y = (Cr * mass * (speed.y - m.getSpeed().y) + m.getMass()
				* m.getSpeed().y + mass * speed.y)
				/ (m.getMass() + mass);

		speed = new Vector2d(v1x, v1y);
		m.setSpeed(new Vector2d(v2x, v2y));
		setA(new Vector2d(0, 0));
		m.setA(new Vector2d(0, 0));
	}

	public boolean collision(Matter m) {
		if (point.distance(m.getPoint()) < rayon + m.rayon
				|| getPlusV().distance(m.getPlusV()) < rayon + m.rayon) {
			return true;
		} else {
			double a1 = (point.y - getPlusV().y) / (point.x - getPlusV().x);
			double b1 = point.y - a1 * point.x;
			double a2 = (m.getPoint().getY() - m.getPlusV().y)
					/ (m.getPoint().getX() - m.getPlusV().x);
			double b2 = m.getPoint().getY() - a2 * m.getPoint().getX();
			if (a1 == a2) {
				return false;
			} else {
				double xres = (b2 - b1) / (a1 - a2);
				if (xres >= point.x && xres <= m.getPoint().getX()
						|| xres >= m.getPoint().getX() && xres <= point.x) {
					return true;
				} else {
					return false;
				}
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
