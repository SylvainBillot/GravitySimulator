package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
		if (typeOfUnivers == TypeOfUnivers.Random) {
			createRandomUvivers();
		}
		if (typeOfUnivers == TypeOfUnivers.GalaxyWithBackHole) {
			createRandomGalaxyWithBlackHole();
		}
		if (typeOfUnivers == TypeOfUnivers.Galaxy) {
			createRandomGalaxy();
		}
		if (typeOfUnivers == TypeOfUnivers.Planetary) {
			createPlanetary();
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

	public void resetAcceleration() {
		for (Matter m : listMatter.values()) {
			m.setaX(0);
			m.setaY(0);
		}
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

							double attraction = HelperVariable.GRAVITY
									* (((m.getMass() * uvoisin.getMass()) / Math
											.pow(distance, 2)));

							double attractionDeUvoisinSurM = attraction
									* uvoisin.getMass()
									/ (m.getMass() + uvoisin.getMass());

							double angle = Math.atan2(
									uvoisin.getGy() - m.getY(), uvoisin.getGx()
											- m.getX());

							m.setaX(m.getaX() + attractionDeUvoisinSurM
									* Math.cos(angle)
									* HelperVariable.timeFactor);
							m.setaY(m.getaY() + attractionDeUvoisinSurM
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

	private void darkMatterSimulation(double darkMatterX, double darkMatterY,
			double darkMatterMass) {
		for (Matter m : listMatter.values()) {
			double distance = Math.pow(Math.pow(m.getX() - darkMatterX, 2)
					+ Math.pow(m.getY() - darkMatterY, 2), 0.5);

			double attraction = HelperVariable.GRAVITY
					* (((m.getMass() * darkMatterMass) / Math.pow(distance, 2)));

			double attractionDeUvoisinSurM = attraction * darkMatterMass
					/ (m.getMass() + darkMatterMass);

			double angle = Math.atan2(darkMatterY - m.getY(),
					darkMatterY - m.getX());

			m.setaY(m.getaX() + attractionDeUvoisinSurM * Math.cos(angle)
					* HelperVariable.timeFactor);
			m.setaY(m.getaY() + attractionDeUvoisinSurM * Math.sin(angle)
					* HelperVariable.timeFactor);
		}
	}

	private void treatNeighbor(Matter m1, TreeMap<Matter, Matter> sortX,
			HashMap<Matter, String> treated, List<Matter[]> toTreat) {
		if (treated.get(m1) == null) {
			treated.put(m1, "");
			SortedMap<Matter, Matter> selectX = sortX.subMap(
					new Matter(m1.minX(), 0), true, new Matter(m1.maxX(), 0),
					false);
			TreeMap<Matter, Matter> sortY = new TreeMap<Matter, Matter>(
					new YComparator());
			sortY.putAll(selectX);
			SortedMap<Matter, Matter> selectY = sortY.subMap(
					new Matter(0, m1.minY()), true, new Matter(0, m1.maxY()),
					false);
			for (Matter m2 : selectY.values()) {
				if (treated.get(m2) == null) {
					// treatNeighbor(m2, sortX, treated, toTreat);
					treated.put(m2, "");
					Matter element[] = new Matter[2];
					element[0] = m1;
					element[1] = m2;
					toTreat.add(element);
				}
			}
		}
	}

	public void manageImpact() {
		// On recheche les collisions
		LinkedList<Matter[]> toTreat = new LinkedList<Matter[]>();
		HashMap<Matter, String> treated = new HashMap<Matter, String>();
		TreeMap<Matter, Matter> sortX = new TreeMap<Matter, Matter>(
				new XComparator());
		sortX.putAll(listMatter);
		for (Matter m1 : listMatter.values()) {
			treatNeighbor(m1, sortX, treated, toTreat);
		}

		for (int cpt = 0; cpt < toTreat.size(); cpt++) {
			Matter[] element = toTreat.get(cpt);
			if (HelperVariable.probFusion < Math.random()) {
				element[0].impact(element[1]);
			} else {
				element[0].fusion(element[1]);
				listMatter.remove(element[1]);
			}
		}

		mass = 0;
		for (Matter m : listMatter.values()) {
			mass += m.getMass();
		}

	}

	public void move() {
		if (HelperVariable.darkMatterSimulation) {
			darkMatterSimulation(0, 0, mass * 10);
		}

		for (Matter m : listMatter.values()) {
			m.move();
		}

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
	private void createRandomUvivers(){
		HelperVariable.numberOfObjects = 1000;
		HelperVariable.probFusion = 1;
		HelperVariable.dentityMin = 30;
		HelperVariable.densityMax = 30;
		HelperVariable.nebulaRadius = 600;
		HelperVariable.massObjectMin = 1E4;
		HelperVariable.massObjectMax = 1E5 + 1;	
		
		createUvivers(0, 0, 0, 0);
	}
	
	private void createUvivers(double ox, double oy, double delta,
			double speedTrans) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < HelperVariable.numberOfObjects; cpt++) {
			double angle = Math.random() * Math.PI * 2;
			double r = Math.random();
			Matter m = new Matter(
					ox + Math.cos(angle) * r * HelperVariable.nebulaRadius,
					oy + Math.sin(angle) * r * HelperVariable.nebulaRadius,
					HelperVariable.massObjectMin
							+ Math.random()
							* (HelperVariable.massObjectMax - HelperVariable.massObjectMin),
					0,
					0,
					HelperVariable.dentityMin
							+ Math.random()
							* (HelperVariable.densityMax - HelperVariable.dentityMin));
			miniListMatter.put(m, m);
			miniMass += m.getMass();
		}

		for (Matter m : miniListMatter.values()) {
			double distance = Math.pow(
					Math.pow(m.getX() - ox, 2) + Math.pow(m.getY() - oy, 2),
					0.5);
			double angle = Math.atan2(m.getY() - oy, m.getX() - ox);
			m.setSpeedX(m.getX() - ox - Math.cos(angle - delta) * distance
					+ speedTrans);
			m.setSpeedY(m.getY() - oy - Math.sin(angle - delta) * distance);
		}

		listMatter.putAll(miniListMatter);
		mass += miniMass;

	}

	private void createRandomGalaxyWithBlackHole() {
		HelperVariable.numberOfObjects = 500;
		HelperVariable.probFusion = 1;
		HelperVariable.dentityMin = 30;
		HelperVariable.densityMax = 30;
		HelperVariable.nebulaRadius = 300;
		HelperVariable.massObjectMin = 1E3;
		HelperVariable.massObjectMax = 1E3 + 1;
		createUvivers(0, 0, 0, 0);
		Matter m1 = new Matter(Math.random(), -Math.random(), mass * 10, 0, 0,
				500);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		computeCentroidOfUnivers();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double distance = Math
						.pow(Math.pow(m.getX() - gx, 2)
								+ Math.pow(m.getY() - gy, 2), 0.5);
				double a = Math.atan2(m.getY() - gy, m.getX() - gx);
				double speed = Math.pow(mass * HelperVariable.GRAVITY
						/ distance, 0.5);
				m.setSpeedX(10*speed * Math.cos(a + Math.PI / 2));
				m.setSpeedY(10*speed * Math.sin(a + Math.PI / 2));
			}
		}

	}

	private void createRandomGalaxy() {
		HelperVariable.scala = 1;
		HelperVariable.numberOfObjects = 500;
		HelperVariable.probFusion = 1;
		HelperVariable.dentityMin = 30;
		HelperVariable.densityMax = 30;
		HelperVariable.nebulaRadius = 300;
		HelperVariable.massObjectMin = 1E4;
		HelperVariable.massObjectMax = 1E4 + 1;
		double delta = Math.PI * 2 / 360;
		double speedTrans = 0.1;
		createUvivers(-500, -100, delta / 20, speedTrans);
		createUvivers(500, 100, -delta / 20, -speedTrans);

		Matter m1 = new Matter(-500 + Math.random(), -100 + Math.random(),
				1E6 + Math.random(), speedTrans, 0, 30);
		listMatter.put(m1, m1);
		mass += m1.getMass();

		Matter m2 = new Matter(500 + Math.random(), 100 + Math.random(),
				1E6 + Math.random(), -speedTrans, 0, 30);
		listMatter.put(m2, m2);
		mass += m2.getMass();
	}

	private void createPlanetary() {
		HelperVariable.scala = 1;
		Matter m1 = new Matter(Math.random(), Math.random(),
				1E10 + Math.random(), 0, 0, 300);
		listMatter.put(m1, m1);
		Matter m2 = new Matter(200, Math.random(), 1E2 + Math.random(), 0, 0.6,
				2);
		listMatter.put(m2, m2);
		Matter m3 = new Matter(-300, Math.random(), 1E3 + Math.random(), 0,
				-1.5, 2);
		listMatter.put(m3, m3);
		Matter m4 = new Matter(-100, Math.random(), 100 + Math.random(), 0,
				-0.8, 2);
		listMatter.put(m4, m4);
		Matter m5 = new Matter(Math.random(), -400, 100 + Math.random(), 0.4,
				0, 2);
		listMatter.put(m5, m5);
		Matter m6 = new Matter(Math.random(), -30, 50 + Math.random(), 1.1, 0,
				2);
		listMatter.put(m6, m6);

		for (Matter m : listMatter.values()) {
			mass += m.getMass();
		}
	}

}
