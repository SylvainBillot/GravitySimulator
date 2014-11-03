package com.sylvanoid.joblib;

import com.sylvanoid.common.HelperVariable;

public class Matter implements Comparable<Matter> {
	private double x;
	private double y;
	private double mass;
	private double speedX;
	private double speedY;
	private double density;
	private double rayon;
	
	@Override
	public int compareTo(Matter o) {
		// TODO Auto-generated method stub
		if (mass < o.getMass()) {
			return 1;
		}
		if (mass > o.getMass()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "m:" + mass + " x:" + x + " y:" + y + " vx:" + speedX + " vy:"
				+ speedY;
	}

	public Matter() {

	}

	public Matter(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Matter(double x, double y, double mass){
		this.x = x;
		this.y = y;
		this.mass = mass;
	}
	
	public Matter(double x, double y, double mass, double speedX,
			double speedY, double density) {
		this.x = x;
		this.y = y;
		this.mass = mass;
		this.speedX = speedX;
		this.speedY = speedY;
		this.density = density;
		this.rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
		this.mass = mass;
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

	public double getRayon() {
		return rayon;
	}

	public double getXplusV() {
		return x + speedX;
	}

	public double getYplusV() {
		return y + speedY;
	}

	public double maxX() {
		return (x > getXplusV()) ? (x + rayon) : (getXplusV() + rayon);
	}

	public double maxY() {
		return (y > getYplusV()) ? (y + rayon) : (getYplusV() + rayon);
	}

	public double minX() {
		return (x > getXplusV()) ? (getXplusV() - rayon) : (x - rayon);
	}

	public double minY() {
		return (y > getYplusV()) ? (getYplusV() - rayon) : (y - rayon);
	}

	public void move() {
		x = getXplusV();
		y = getYplusV();
	}

	public void fusion(Matter m2) {
		x = (x * mass + m2.getX() * m2.getMass()) / (mass + m2.getMass());
		y = (y * mass + m2.getY() * m2.getMass()) / (mass + m2.getMass());
		speedX = (speedX * mass + m2.getSpeedX() * m2.getMass())
				/ (mass + m2.getMass());
		speedY = (speedY * mass + m2.getSpeedY() * m2.getMass())
				/ (mass + m2.getMass());
		density = (density * mass + m2.getDensity() * m2.getMass())
				/ (mass + m2.getMass());
		mass += m2.getMass();
		rayon = Math.pow(mass, (double) 1 / (double) 3) / density;
	}

	public void impact(Matter m2) {
		double Cr = HelperVariable.typeOfImpact;
		double v1x = (Cr * m2.getMass() * (m2.getSpeedX() - speedX) + mass
				* speedX + m2.getMass() * m2.getSpeedX())
				/ (mass + m2.getMass());
		double v1y = (Cr * m2.getMass() * (m2.getSpeedY() - speedY) + mass
				* speedY + m2.getMass() * m2.getSpeedY())
				/ (mass + m2.getMass());
/*
		double v2x = (Cr * mass * (speedX - m2.getSpeedX()) + m2.getMass()
				* m2.getSpeedX() + mass * speedX)
				/ (m2.getMass() + mass);
		double v2y = (Cr * mass * (speedY - m2.getSpeedY()) + m2.getMass()
				* m2.getSpeedY() + mass * speedY)
				/ (m2.getMass() + mass);
*/
		speedX = v1x;
		speedY = v1y;
//		m2.setSpeedX(v2x);
//		m2.setSpeedY(v2y);
	}

	public boolean collision(Matter m2) {
		if (this == m2) {
			return false;
		}
		double rMax = rayon > m2.getRayon() ? rayon : m2.getRayon();
		double distance = Math.pow(
				Math.pow(x - m2.getX(), 2) + Math.pow(y - m2.getY(), 2), 0.5);
		return distance < rMax;
	}
}
