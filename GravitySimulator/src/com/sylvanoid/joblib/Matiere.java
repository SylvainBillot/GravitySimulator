package com.sylvanoid.joblib;

import com.sylvanoid.common.HelperVariable;

public class Matiere implements Comparable<Matiere> {
	private double x;
	private double y;
	private double masse;
	private double vitessex;
	private double vitessey;
	private double densite;
	private double rayon;

	@Override
	public int compareTo(Matiere o) {
		// TODO Auto-generated method stub
		if (masse < o.getMasse()) {
			return 1;
		}
		if (masse > o.getMasse()) {
			return -1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "m:" + masse + " x:" + x + " y:" + y + " vx:" + vitessex
				+ " vy:" + vitessey;
	}


	public Matiere() {

	}

	public Matiere(double x, double y) {
		this.x=x;
		this.y=y;
	}

	
	
	public Matiere(double x, double y, double masse, double vitessex,
			double vitessey, double densite) {
		this.x = x;
		this.y = y;
		this.masse = masse;
		this.vitessex = vitessex;
		this.vitessey = vitessey;
		this.densite = densite;
		this.rayon = Math.pow(masse, (double) 1 / (double) 3) / densite;
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

	public double getMasse() {
		return masse;
	}

	public void setMasse(double masse) {
		rayon = Math.pow(masse, (double) 1 / (double) 3) / densite;
		this.masse = masse;
	}

	public double getVitessex() {
		return vitessex;
	}

	public void setVitessex(double vitessex) {
		this.vitessex = vitessex;
	}

	public double getVitessey() {
		return vitessey;
	}

	public void setVitessey(double vitessey) {
		this.vitessey = vitessey;
	}

	public double getDensite() {
		return densite;
	}

	public void setDensite(double densite) {
		this.densite = densite;
	}

	public double getRayon() {
		return rayon;
	}

	public double getXplusV() {
		return x + vitessex;
	}

	public double getYplusV() {
		return y + vitessey;
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

	public void fusion(Matiere m2) {
		if (this != m2) {
			x = (x * masse + m2.getX() * m2.getMasse())
					/ (masse + m2.getMasse());
			y = (y * masse + m2.getY() * m2.getMasse())
					/ (masse + m2.getMasse());
			vitessex = (vitessex * masse + m2.getVitessex() * m2.getMasse())
					/ (masse + m2.getMasse());
			vitessey = (vitessey * masse + m2.getVitessey() * m2.getMasse())
					/ (masse + m2.getMasse());
			densite = (densite * masse + m2.getDensite() * m2.getMasse())
					/ (masse + m2.getMasse());
			masse += m2.getMasse();
			rayon = Math.pow(masse, (double) 1 / (double) 3) / densite;
		}
	}

	public void impact(Matiere m2) {
		if (this != m2) {
			double Cr = HelperVariable.typeOfImpact;
			double v1x = (Cr * m2.getMasse() * (m2.getVitessex() - vitessex)
					+ masse * vitessex + m2.getMasse() * m2.getVitessex())
					/ (masse + m2.getMasse());
			double v1y = (Cr * m2.getMasse() * (m2.getVitessey() - vitessey)
					+ masse * vitessey + m2.getMasse() * m2.getVitessey())
					/ (masse + m2.getMasse());

			double v2x = (Cr * masse * (vitessex - m2.getVitessex())
					+ m2.getMasse() * m2.getVitessex() + masse * vitessex)
					/ (m2.getMasse() + masse);
			double v2y = (Cr * masse * (vitessey - m2.getVitessey())
					+ m2.getMasse() * m2.getVitessey() + masse * vitessey)
					/ (m2.getMasse() + masse);

			vitessex = v1x;
			vitessey = v1y;
			m2.setVitessex(v2x);
			m2.setVitessey(v2y);
		}
	}

	public boolean collision(Matiere m2) {
		if (this == m2) {
			return false;
		}
		double rMax = rayon > m2.getRayon() ? rayon : m2.getRayon();
		double distance = Math.pow(
				Math.pow(x - m2.getX(), 2) + Math.pow(y - m2.getY(), 2), 0.5);
		return distance < rMax;
	}
}
