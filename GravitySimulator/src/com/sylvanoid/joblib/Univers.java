package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfUnivers;

public class Univers {
	private TreeMap<Matter, Matter> listMatter;
	private double gx;
	private double gy;
	private double masse;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	@Override
	public String toString() {
		return ("m:" + masse + " gx:" + gy + " gy:" + gy);
	}

	public Univers(TypeOfUnivers typeOfUnivers) {
		if (typeOfUnivers == TypeOfUnivers.Random) {
			createRandomUvivers();
		}
		if (typeOfUnivers == TypeOfUnivers.Galaxy) {
			createRandomGalaxy();
		}
	}

	public Univers(Univers father, double minX, double minY, double maxX,
			double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		listMatter = new TreeMap<Matter, Matter>();
		masse = 0;
		for (Matter m : father.getListMatiere().values()) {
			if (m.getX() >= minX && m.getX() <= maxX && m.getY() >= minY
					&& m.getY() <= maxY) {
				listMatter.put(m, m);
				masse += m.getMass();
			}
		}
	}

	public void computeLimits() {
		boolean firstTime = true;
		for (Matter o : listMatter.values()) {
			if (!firstTime) {
				if (minX > o.getX()) {
					minX = o.getX();
				}
				if (maxX < o.getX()) {
					maxX = o.getX();
				}
				if (minY > o.getY()) {
					minY = o.getY();
				}
				if (maxY < o.getY()) {
					maxY = o.getY();
				}
			} else {
				minX = o.getX();
				maxX = o.getX();
				minY = o.getY();
				maxY = o.getY();
				firstTime = false;
			}
		}
	}

	public void computeCentroidOfUnivers() {
		double tmpGx = 0;
		double tmpGy = 0;
		for (Matter m : getListMatiere().values()) {
			tmpGx += (m.getX() * m.getMass());
			tmpGy += (m.getY() * m.getMass());
		}
		gx = tmpGx / getMasse();
		gy = tmpGy / getMasse();
	}

	public void compute() {
		if (listMatter.size() > 1) {
			// Découpage en 4 et compute de chacun
			double cx = minX + (maxX - minX) / 2;
			double cy = minY + (maxY - minY) / 2;
			List<Univers> subUnivers = new ArrayList<Univers>();
			Univers suba = new Univers(this, minX, minY, cx, cy);
			subUnivers.add(suba);
			Univers subb = new Univers(this, cx, minY, maxX, cy);
			subUnivers.add(subb);
			Univers subc = new Univers(this, cx, cy, maxX, maxY);
			subUnivers.add(subc);
			Univers subd = new Univers(this, minX, cy, cx, maxY);
			subUnivers.add(subd);

			listMatter = new TreeMap<Matter, Matter>();
			masse = 0;
			for (Univers u : subUnivers) {
				u.compute();
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin && uvoisin.getListMatiere().size() > 0) {
						uvoisin.computeCentroidOfUnivers();
						TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
								new MassComparator());
						sortByMass.putAll(u.listMatter);
						for (Matter m : sortByMass.values()) {
							double distance = Math.pow(
									Math.pow(m.getX() - uvoisin.getGx(), 2)
											+ Math.pow(
													m.getY() - uvoisin.getGy(),
													2), 0.5);
							// To avoid aberated acceleration
							if (distance < m.getRayon()) {
								distance = m.getRayon();
							}

							double attraction = HelperVariable.GRAVITY
									* (((m.getMass() * uvoisin.getMasse()) / Math
											.pow(distance, 2)));

							double attractionDeUvoisinSurM = attraction
									* uvoisin.getMasse()
									/ (m.getMass() + uvoisin.getMasse());

							double angle = Math.atan2(
									uvoisin.getGy() - m.getY(), uvoisin.getGx()
											- m.getX());

							m.setSpeedX(m.getSpeedX() + attractionDeUvoisinSurM
									* Math.cos(angle)
									* HelperVariable.timeFactor);
							m.setSpeedY(m.getSpeedY() + attractionDeUvoisinSurM
									* Math.sin(angle)
									* HelperVariable.timeFactor);
						}
					}
				}
				for (Matter m : u.getListMatiere().values()) {
					listMatter.put(m, m);
					masse += m.getMass();
				}
			}
		}
	}

	private Matter origine(HashMap<Matter, Matter> aVirer, Matter m) {
		if (aVirer.get(m) == null) {
			return m;
		} else {
			return origine(aVirer, aVirer.get(m));
		}
	}

	public void manageImpact() {
		// On recheche les collisions
		HashMap<Matter, Matter> aVirer = new HashMap<Matter, Matter>();
		HashMap<Matter, Matter> traite = new HashMap<Matter, Matter>();
		TreeMap<Matter, Matter> sortX = new TreeMap<Matter, Matter>(
				new XComparator());
		sortX.putAll(listMatter);
		TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
				new MassComparator());
		sortByMass.putAll(listMatter);
		for (Matter m1 : sortByMass.values()) {
			SortedMap<Matter, Matter> selectX = sortX.subMap(
					new Matter(m1.minX(), 0), new Matter(m1.maxX(), 0));
			TreeMap<Matter, Matter> sortY = new TreeMap<Matter, Matter>(
					new YComparator());
			sortY.putAll(selectX);
			SortedMap<Matter, Matter> selectY = sortY.subMap(
					new Matter(0, m1.minY()), new Matter(0, m1.maxY()));

			for (Matter m2 : selectY.values()) {
				if (aVirer.get(m2) == null) {
					traite.put(m1, m1);
					// if (traite.get(m2) == null && m1.collision(m2)) {
					if (traite.get(m2) == null) {
						if (HelperVariable.probFusion < Math.random()) {
							m1.impact(m2);
						} else {
							origine(aVirer, m1).fusion(m2);
							aVirer.put(m2, m1);
						}
					}
				}
			}
		}
		for (Matter m : aVirer.keySet()) {
			listMatter.remove(m);
		}
	}

	public void move() {
		manageImpact();

		// Barycentre au centre de l'écran
		computeCentroidOfUnivers();
		if (HelperVariable.centerOnCentroid) {
			for (Matter m : listMatter.values()) {
				m.setX(m.getX() - gx);
				m.setY(m.getY() - gy);
			}
		}

		// Centre l'ecran
		if (HelperVariable.centerOnScreen) {
			computeLimits();
			for (Matter m : listMatter.values()) {
				m.setX(m.getX() - (maxX + minX) / 2);
				m.setY(m.getY() - (maxY + minY) / 2);
			}
		}

		// Centre sur le plus massif
		if (HelperVariable.centerOnMassMax) {
			TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
					new MassComparator());
			sortByMass.putAll(listMatter);
			Matter maxMass = sortByMass.firstEntry().getValue();
			double cmaxX =maxMass.getX();
			double cmaxY =maxMass.getY();
			for (Matter m : sortByMass.values()) {
				m.setX(m.getX() - cmaxX);
				m.setY(m.getY() - cmaxY);
			}
		}

		if (HelperVariable.timeFactorChangeX10) {
			for (Matter m : listMatter.values()) {
				m.setSpeedX(m.getSpeedX() * 2);
				m.setSpeedY(m.getSpeedY() * 2);
			}
			HelperVariable.timeFactorChangeX10 = false;
		}

		if (HelperVariable.timeFactorChangeDiv10) {
			for (Matter m : listMatter.values()) {
				m.setSpeedX(m.getSpeedX() / 2);
				m.setSpeedY(m.getSpeedY() / 2);
			}
			HelperVariable.timeFactorChangeDiv10 = false;
		}

		for (Matter m : listMatter.values()) {
			m.move();
		}
	}

	public TreeMap<Matter, Matter> getListMatiere() {
		return listMatter;
	}

	public double getMinX() {
		return minX;
	}

	public double getMinY() {
		return minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public double getGx() {
		return gx;
	}

	public double getGy() {
		return gy;
	}

	public double getMasse() {
		return masse;
	}

	public void setMasse(double masse) {
		this.masse = masse;
	}

	private void createRandomUvivers() {
		listMatter = new TreeMap<Matter, Matter>();
		masse = 0;
		for (int cpt = 0; cpt < HelperVariable.numberOfObjects; cpt++) {
			double angle = Math.random() * Math.PI * 2;
			Matter m = new Matter(
					Math.cos(angle) * Math.random()
							* HelperVariable.nebulaRadius,
					Math.sin(angle) * Math.random()
							* HelperVariable.nebulaRadius,
					HelperVariable.massObjectMin
							+ Math.random()
							* (HelperVariable.massObjectMax - HelperVariable.massObjectMin),
					0,
					0,
					HelperVariable.dentityMin
							+ Math.random()
							* (HelperVariable.densityMax - HelperVariable.dentityMin));
			listMatter.put(m, m);
			masse += m.getMass();
		}
	}

	private void createRandomGalaxy() {
		createRandomUvivers();
		double delta = Math.PI * 2 / 360 / 20;
		for (Matter m : listMatter.values()) {
			double distance = Math.pow(
					Math.pow(m.getX(), 2) + Math.pow(m.getY(), 2), 0.5);
			double angle = Math.atan2(m.getY(), m.getX());
			m.setSpeedX(m.getX() - Math.cos(angle + delta) * distance);
			m.setSpeedY(m.getY() - Math.sin(angle + delta) * distance);
		}
	}

}
