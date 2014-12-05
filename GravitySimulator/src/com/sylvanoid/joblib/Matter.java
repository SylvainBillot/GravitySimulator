package com.sylvanoid.joblib;

import javax.vecmath.Point2d;

import com.sylvanoid.common.HelperVariable;

public class Matter implements Comparable<Matter> {
	private Point2d point;
	private double mass;
	private double aX;
	private double aY;
	private double speedX;
	private double speedY;
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
				+ speedX + " vy:" + speedY;
	}

	public Matter(Point2d point, double mass, double speedX, double speedY,
			double density, boolean isDark) {
		this.setPoint(point);
		this.mass = mass;
		this.speedX = speedX;
		this.speedY = speedY;
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

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
		this.mass = mass;
	}

	public double getaX() {
		return aX;
	}

	public void setaX(double aX) {
		this.aX = aX;
	}

	public double getaY() {
		return aY;
	}

	public void setaY(double aY) {
		this.aY = aY;
	}

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
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

	public double getXplusA() {
		return point.x + aX;
	}

	public double getYplusA() {
		return point.y + aY;
	}

	public double getXplusV() {
		return point.x + speedX;
	}

	public double getYplusV() {
		return point.y + speedY;
	}

	public double getXminusV() {
		return point.x - speedX;
	}

	public double getYminusV() {
		return point.y - speedY;
	}

	public double maxX() {
		// return x + rayon;
		return (point.x > getXplusV()) ? (point.x + rayon)
				: (getXplusV() + rayon);

	}

	public double maxY() {
		// return y + rayon;
		return (point.y > getYplusV()) ? (point.y + rayon)
				: (getYplusV() + rayon);
	}

	public double minX() {
		// return x - rayon;
		return (point.x > getXplusV()) ? (getXplusV() - rayon)
				: (point.x - rayon);
	}

	public double minY() {
		// return y - rayon;
		return (point.y > getYplusV()) ? (getYplusV() - rayon)
				: (point.y - rayon);
	}

	public void move() {
		speedX += aX;
		speedY += aY;
		point.x = getXplusV();
		point.y = getYplusV();
	}

	public void fusion(Matter m) {
		point.x = (point.x * mass + m.getPoint().getX() * m.getMass())
				/ (mass + m.getMass());
		point.y = (point.y * mass + m.getPoint().getY() * m.getMass())
				/ (mass + m.getMass());
		speedX = (speedX * mass + m.getSpeedX() * m.getMass())
				/ (mass + m.getMass());
		speedY = (speedY * mass + m.getSpeedY() * m.getMass())
				/ (mass + m.getMass());
		aX = (aX * mass + m.getaX() * m.getMass()) / (mass + m.getMass());
		aY = (aY * mass + m.getaY() * m.getMass()) / (mass + m.getMass());
		density = (density * mass + m.getDensity() * m.getMass())
				/ (mass + m.getMass());
		mass += m.getMass();
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public void impact(Matter m) {
		double Cr = HelperVariable.typeOfImpact;
		double v1x = (Cr * m.getMass() * (m.getSpeedX() - speedX) + mass
				* speedX + m.getMass() * m.getSpeedX())
				/ (mass + m.getMass());
		double v1y = (Cr * m.getMass() * (m.getSpeedY() - speedY) + mass
				* speedY + m.getMass() * m.getSpeedY())
				/ (mass + m.getMass());

		double v2x = (Cr * mass * (speedX - m.getSpeedX()) + m.getMass()
				* m.getSpeedX() + mass * speedX)
				/ (m.getMass() + mass);
		double v2y = (Cr * mass * (speedY - m.getSpeedY()) + m.getMass()
				* m.getSpeedY() + mass * speedY)
				/ (m.getMass() + mass);

		speedX = v1x;
		speedY = v1y;
		m.setSpeedX(v2x);
		m.setSpeedY(v2y);
		aX = 0;
		aY = 0;
		m.setaX(0);
		m.setaY(0);
	}

	public boolean collision(Matter m) {
		double distance = point.distance(m.getPoint());
		double distance2 = Math.pow(Math.pow(getXplusV() - m.getXplusV(), 2)
				+ Math.pow(getYplusV() - m.getYplusV(), 2), 0.5);

		if (distance < rayon + m.rayon || distance2 < rayon + m.rayon) {
			return true;
		} else {
			double a1 = (point.y - getYplusV()) / (point.x - getXplusV());
			double b1 = point.y - a1 * point.x;
			double a2 = (m.getPoint().getY() - m.getYplusV())
					/ (m.getPoint().getX() - m.getXplusV());
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
		double distance = point.distance(m.getPoint());
		double orbitalSpeed = Math.pow(HelperVariable.timeFactor
				* HelperVariable.GRAVITY * Math.pow(m.getMass(), 2)
				/ ((mass + m.getMass()) * distance), 0.5);
		return orbitalSpeed;
	}

}
