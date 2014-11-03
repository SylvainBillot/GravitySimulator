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
	private double mass;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	@Override
	public String toString() {
		return ("m:" + mass + " gx:" + gy + " gy:" + gy);
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
		mass = 0;
		for (Matter m : father.getListMatiere().values()) {
			if (m.getX() >= minX && m.getX() <= maxX && m.getY() >= minY
					&& m.getY() <= maxY) {
				listMatter.put(m, m);
				mass += m.getMass();
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
		gx = tmpGx / getMass();
		gy = tmpGy / getMass();
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
			mass = 0;
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
							// not a good idea
							//if (distance < m.getRayon()) {
								//distance = m.getRayon();
							//}

							double attraction = HelperVariable.GRAVITY
									* (((m.getMass() * uvoisin.getMass()) / Math
											.pow(distance, 2)));

							double attractionDeUvoisinSurM = attraction
									* uvoisin.getMass()
									/ (m.getMass() + uvoisin.getMass());

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
					mass += m.getMass();
				}
			}
		}
	}

	public void manageImpact() {
		// On recheche les collisions
		List<Matter[]> toTreat = new ArrayList<Matter[]>();
		HashMap<Matter, String> treated = new HashMap<Matter, String>();
		TreeMap<Matter, Matter> sortX = new TreeMap<Matter, Matter>(
				new XComparator());
		sortX.putAll(listMatter);
		for (Matter m1 : listMatter.values()) {
			if (treated.get(m1) == null) {
				treated.put(m1, "");
				SortedMap<Matter, Matter> selectX = sortX.subMap(
						new Matter(m1.minX(), 0), true,
						new Matter(m1.maxX(), 0), true);
				TreeMap<Matter, Matter> sortY = new TreeMap<Matter, Matter>(
						new YComparator());
				sortY.putAll(selectX);
				SortedMap<Matter, Matter> selectY = sortY.subMap(new Matter(0,
						m1.minY()), true, new Matter(0, m1.maxY()), true);
				for (Matter m2 : selectY.values()) {
					if (treated.get(m2) == null) {
						treated.put(m2, "");
						Matter element[] = new Matter[2];
						element[0] = m1;
						element[1] = m2;
						toTreat.add(element);
					}
				}
			}
		}
		HashMap<Matter, String> toDelete = new HashMap<Matter, String>();
		for (Matter[] element : toTreat) {
			if (HelperVariable.probFusion < Math.random()) {
				element[0].impact(element[1]);
			} else {
				element[0].fusion(element[1]);
				listMatter.remove(element[1]);
			}
		}

		mass = 0;
		for (Matter m : listMatter.values()) {
			if (m.getMass() == 0) {
				toDelete.put(m, "");
			}
			mass += m.getMass();
		}

	}

	public void move() {
		if (HelperVariable.manageImpact) {
			double oldMass = mass;
			manageImpact();
			if (Math.abs(mass - oldMass) > 1) {
				System.out.println("wtf :" + (int) mass + " - " + (int) oldMass
						+ " = " + (int) (mass - oldMass));
			}
		}

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
			double cmaxX = maxMass.getX();
			double cmaxY = maxMass.getY();
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

	public double getMass() {
		return mass;
	}

	private void createRandomUvivers() {
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
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
			mass += m.getMass();
		}
	}

	private void createRandomGalaxy() {
		HelperVariable.probFusion = 1;
		HelperVariable.dentityMin = 50;
		HelperVariable.densityMax = 50;
		HelperVariable.nebulaRadius = 200;
		HelperVariable.massObjectMin = 1E4;
		HelperVariable.massObjectMax = 1E4+1;
		double delta = Math.PI * 2 / 360/20;
		createRandomUvivers();
		for (Matter m : listMatter.values()) {
			double distance = Math.pow(
					Math.pow(m.getX(), 2) + Math.pow(m.getY(), 2), 0.5);
			double angle = Math.atan2(m.getY(), m.getX());
			m.setSpeedX(m.getX() - Math.cos(angle + delta) * distance);
			m.setSpeedY(m.getY() - Math.sin(angle + delta) * distance);
		}
	}

}
