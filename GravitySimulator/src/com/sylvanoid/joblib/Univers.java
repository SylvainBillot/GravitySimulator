package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.vecmath.Point2d;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.gui.GUIProgram;

public class Univers {
	private GUIProgram guiProgram;
	private TreeMap<Matter, Matter> listMatter;
	private Point2d gPoint = new Point2d(0, 0);
	private double mass;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	@Override
	public String toString() {
		return ("m:" + mass + " gx:" + gPoint.y + " gy:" + gPoint.y);
	}

	public Univers(TypeOfUnivers typeOfUnivers, GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
		if (typeOfUnivers == TypeOfUnivers.Random) {
			createRandomStaticUvivers();
		}
		if (typeOfUnivers == TypeOfUnivers.RandomRotateUnivers) {
			createRandomRotateUnivers();
		}
		if (typeOfUnivers == TypeOfUnivers.GalaxiesCollision) {
			createGalaxiesCollision();
		}
		if (typeOfUnivers == TypeOfUnivers.Planetary) {
			createPlanetary();
		}
		if (typeOfUnivers == TypeOfUnivers.PlanetaryRandom) {
			createPlanetaryRandom();
		}
		if (typeOfUnivers == TypeOfUnivers.PlanetariesGenesis) {
			createPlanetariesGenesis();
		}
		if (typeOfUnivers == TypeOfUnivers.DoubleStars) {
			createDoubleStars();
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
			if (m.getPoint().getX() >= minX && m.getPoint().getX() <= maxX
					&& m.getPoint().getY() >= minY
					&& m.getPoint().getY() <= maxY) {
				listMatter.put(m, m);
				mass += m.getMass();
			}
		}
	}

	public void computeLimits() {
		boolean firstTime = true;
		for (Matter o : listMatter.values()) {
			if (!firstTime) {
				if (minX > o.getPoint().getX()) {
					minX = o.getPoint().getX();
				}
				if (maxX < o.getPoint().getX()) {
					maxX = o.getPoint().getX();
				}
				if (minY > o.getPoint().getY()) {
					minY = o.getPoint().getY();
				}
				if (maxY < o.getPoint().getY()) {
					maxY = o.getPoint().getY();
				}
			} else {
				minX = o.getPoint().getX();
				maxX = o.getPoint().getX();
				minY = o.getPoint().getY();
				maxY = o.getPoint().getY();
				firstTime = false;
			}
		}
	}

	public void computeCentroidOfUnivers() {
		double tmpGx = 0;
		double tmpGy = 0;
		for (Matter m : getListMatiere().values()) {
			tmpGx += (m.getPoint().getX() * m.getMass());
			tmpGy += (m.getPoint().getY() * m.getMass());
		}
		gPoint.x = tmpGx / getMass();
		gPoint.y = tmpGy / getMass();
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
							double distance = m.getPoint().distance(
									uvoisin.getGPoint());
							double attraction = HelperVariable.timeFactor
									* HelperVariable.GRAVITY
									* (((uvoisin.getMass()) / Math.pow(
											distance, 2)));

							double angle = Math
									.atan2(uvoisin.getGPoint().y
											- m.getPoint().getY(),
											uvoisin.getGPoint().x
													- m.getPoint().getX());

							m.setaX(m.getaX() + attraction * Math.cos(angle));
							m.setaY(m.getaY() + attraction * Math.sin(angle));
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

	private void treatNeighborFusion(Matter m1, TreeMap<Double, Matter> sortX,
			HashMap<Matter, String> treated, List<Matter[]> toTreat) {
		if (treated.get(m1) == null) {
			treated.put(m1, "");
			SortedMap<Double, Matter> selectX = sortX.subMap(m1.minX(), true,
					m1.maxX(), false);
			TreeMap<Double, Matter> sortY = new TreeMap<Double, Matter>();
			for (Matter m : selectX.values()) {
				for (double delta = m.minY(); delta <= m.maxY(); delta += m
						.getRayon()) {
					sortY.put(delta, m);
				}
				sortY.put(m.maxY(), m);
			}
			SortedMap<Double, Matter> selectY = sortY.subMap(m1.minY(), true,
					m1.maxY(), false);
			for (Matter m2 : selectY.values()) {
				if (treated.get(m2) == null && m1.collision(m2)) {
					treated.put(m2, "");
					Matter element[] = new Matter[2];
					element[0] = m1;
					element[1] = m2;
					toTreat.add(element);
				}
			}
		}
	}

	private void treatNeighborImpact(Matter m1, TreeMap<Double, Matter> sortX,
			List<Matter[]> toTreat) {
		SortedMap<Double, Matter> selectX = sortX.subMap(m1.minX(), true,
				m1.maxX(), false);
		TreeMap<Double, Matter> sortY = new TreeMap<Double, Matter>();
		for (Matter m : selectX.values()) {
			for (double delta = m.minY(); delta <= m.maxY(); delta += m
					.getRayon()) {
				sortY.put(delta, m);
			}
			sortY.put(m.maxY(), m);
		}
		SortedMap<Double, Matter> selectY = sortY.subMap(m1.minY(), true,
				m1.maxY(), false);
		for (Matter m2 : selectY.values()) {
			if (m1 != m2 && m1.collision(m2)) {
				Matter element[] = new Matter[2];
				element[0] = m1;
				element[1] = m2;
				boolean toadd = true;
				for (Matter e[] : toTreat) {
					if (e[0] == m1 && e[1] == m2 && e[0] == m2 && e[1] == m1) {
						toadd = false;
						break;
					}
				}
				if (toadd) {
					toTreat.add(element);
				}
			}
		}
	}

	public void manageImpact() {
		// On recheche les collisions
		LinkedList<Matter[]> toTreat = new LinkedList<Matter[]>();
		HashMap<Matter, String> treatedFusion = new HashMap<Matter, String>();
		TreeMap<Double, Matter> sortX = new TreeMap<Double, Matter>();
		for (Matter m : listMatter.values()) {
			for (double delta = m.minX(); delta <= m.maxX(); delta += m
					.getRayon()) {
				sortX.put(delta, m);
			}
			sortX.put(m.maxX(), m);
		}

		for (Matter m1 : listMatter.values()) {
			if (HelperVariable.fusion) {
				treatNeighborFusion(m1, sortX, treatedFusion, toTreat);
			} else {
				treatNeighborImpact(m1, sortX, toTreat);
			}
		}
		for (int cpt = 0; cpt < toTreat.size(); cpt++) {
			Matter[] element = toTreat.get(cpt);
			if (HelperVariable.fusion) {
				element[0].fusion(element[1]);
				listMatter.remove(element[1]);
			} else {
				element[0].impact(element[1]);
			}
		}
		mass = 0;
		for (Matter m : listMatter.values()) {
			mass += m.getMass();
		}
	}

	public void move() {

		if (HelperVariable.manageImpact) {
			double oldMass = mass;
			manageImpact();
			if (Math.abs(mass - oldMass) > 1) {
				System.out.println("wtf :" + mass + " - " + oldMass + " = "
						+ (mass - oldMass));
			}
		}

		for (Matter m : listMatter.values()) {
			m.move();
		}

		// Barycentre au centre de l'écran
		computeCentroidOfUnivers();
		if (HelperVariable.centerOnCentroid) {
			for (Matter m : listMatter.values()) {
				m.getPoint().setX(m.getPoint().getX() - gPoint.x);
				m.getPoint().setY(m.getPoint().getY() - gPoint.y);
			}
		}

		// Centre l'ecran
		if (HelperVariable.centerOnScreen) {
			computeLimits();
			for (Matter m : listMatter.values()) {
				m.getPoint().setX(m.getPoint().getX() - (maxX + minX) / 2);
				m.getPoint().setY(m.getPoint().getY() - (maxY + minY) / 2);
			}
			HelperVariable.scala = 0.75 * ((double) guiProgram.getSize().height / (maxY - minY));
		}

		// Centre sur le plus massif
		if (HelperVariable.centerOnMassMax) {
			TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
					new MassComparator());
			sortByMass.putAll(listMatter);
			Matter maxMass = sortByMass.firstEntry().getValue();
			double cmaxX = maxMass.getPoint().getX();
			double cmaxY = maxMass.getPoint().getY();
			for (Matter m : sortByMass.values()) {
				m.getPoint().setX(m.getPoint().getX() - cmaxX);
				m.getPoint().setY(m.getPoint().getY() - cmaxY);
			}
		}
	}

	public void process() {
		resetAcceleration();
		computeLimits();
		compute();
		move();
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

	public Point2d getGPoint() {
		return gPoint;
	}

	public double getMass() {
		return mass;
	}

	private TreeMap<Matter, Matter> createUvivers(double ox, double oy,
			double delta, double speedTrans, double radiusMin,
			double radiusMax, double ratioxy) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < HelperVariable.numberOfObjects; cpt++) {
			Matter m;
			boolean cont = false;
			do {
				double angle = Math.random() * Math.PI * 2;
				double r = Math.log10(Math.random());
				m = new Matter(
						new Point2d(
								ox
										+ Math.cos(angle)
										* (r * ratioxy
												* (radiusMax - radiusMin) + radiusMin),
								oy
										+ Math.sin(angle)
										* (r * (radiusMax - radiusMin) + radiusMin)),
						HelperVariable.massObjectMin
								+ Math.random()
								* (HelperVariable.massObjectMax - HelperVariable.massObjectMin)
								+ Math.random(),
						0,
						0,
						HelperVariable.dentityMin
								+ Math.random()
								* (HelperVariable.densityMax - HelperVariable.dentityMin),
						false);
				cont = false;
				for (Matter m2 : miniListMatter.values()) {
					if (m.collision(m2)) {
						cont = true;
						break;
					}
				}
			} while (cont);
			miniListMatter.put(m, m);
			miniMass += m.getMass();
		}

		for (Matter m : miniListMatter.values()) {
			double distance = Math.pow(Math.pow(m.getPoint().getX() - ox, 2)
					+ Math.pow(m.getPoint().getY() - oy, 2), 0.5);
			double angle = Math.atan2(m.getPoint().getY() - oy, m.getPoint()
					.getX() - ox);
			m.setSpeedX(m.getPoint().getX() - ox - Math.cos(angle - delta)
					* distance + speedTrans);
			m.setSpeedY(m.getPoint().getY() - oy - Math.sin(angle - delta)
					* distance);
		}

		listMatter.putAll(miniListMatter);
		mass += miniMass;

		return miniListMatter;
	}

	private void createRandomStaticUvivers() {
		createUvivers(0, 0, 0, 0, 0, HelperVariable.nebulaRadius, 1);
	}

	private void createRandomRotateUnivers() {
		createUvivers(0, 0, 0, 0, 0, HelperVariable.nebulaRadius, 0.25);
		Matter m1 = new Matter(new Point2d(Math.random(), Math.random()),
				HelperVariable.darkMatterMass, 0, 0,
				HelperVariable.darkMatterDensity, true);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double a = Math.atan2(m.getPoint().getY()
						- m1.getPoint().getY(), m.getPoint().getX()
						- m1.getPoint().getX());
				m.setSpeedX(m.orbitalSpeed(m1) * Math.cos(a + Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));
			}
		}
	}

	private void createGalaxiesCollision() {
		double transSpeed = 0.3;
		TreeMap<Matter, Matter> subu01 = createUvivers(-400, -100, 0, 0, 0,
				HelperVariable.nebulaRadius, 0.25);
		TreeMap<Matter, Matter> subu02 = createUvivers(400, 100, 0, 0, 0,
				HelperVariable.nebulaRadius, 0.25);

		Matter m1 = new Matter(new Point2d(-400 + Math.random(), -100
				+ Math.random()),
				HelperVariable.darkMatterMass + Math.random(), transSpeed, 0,
				HelperVariable.darkMatterDensity, true);
		listMatter.put(m1, m1);
		mass += m1.getMass();

		Matter m2 = new Matter(new Point2d(400 + Math.random(),
				100 + Math.random()), HelperVariable.darkMatterMass
				+ Math.random(), -transSpeed, 0,
				HelperVariable.darkMatterDensity, true);
		listMatter.put(m2, m2);
		mass += m2.getMass();

		for (Matter m : subu01.values()) {
			if (m != m1) {
				double a = Math.atan2(m.getPoint().getY()
						- m1.getPoint().getY(), m.getPoint().getX()
						- m1.getPoint().getX());
				m.setSpeedX(transSpeed + m.orbitalSpeed(m1)
						* Math.cos(a + Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));
			}
		}

		for (Matter m : subu02.values()) {
			if (m != m2) {
				double a = Math.atan2(m.getPoint().getY()
						- m2.getPoint().getY(), m.getPoint().getX()
						- m2.getPoint().getX());
				m.setSpeedX(-transSpeed + m.orbitalSpeed(m2)
						* Math.cos(a - Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m2) * Math.sin(a - Math.PI / 2));
			}
		}
		listMatter.putAll(subu01);
		listMatter.putAll(subu02);
	}

	private void createPlanetary() {
		Matter m1 = new Matter(new Point2d(Math.random(), Math.random()),
				1E10 + Math.random(), 0, 0, 300, false);
		listMatter.put(m1, m1);
		Matter m2 = new Matter(new Point2d(50 + Math.random(),
				10 + Math.random()), 1E4 + Math.random(), 0, 0, 300, false);
		listMatter.put(m2, m2);
		Matter m3 = new Matter(new Point2d(-320 + Math.random(),
				30 + Math.random()), 1E9 + Math.random(), 0, 0, 300, false);
		listMatter.put(m3, m3);
		Matter m4 = new Matter(new Point2d(-100 + Math.random(), -10
				+ Math.random()), 1E2 + Math.random(), 0, 0, 300, false);
		listMatter.put(m4, m4);
		Matter m5 = new Matter(new Point2d(90 + Math.random(), -20
				+ Math.random()), 1E3 + Math.random(), 0, 0, 300, false);
		listMatter.put(m5, m5);
		Matter m6 = new Matter(new Point2d(-20 + Math.random(), -10
				+ Math.random()), 1E2 + Math.random(), 0, 0, 300, false);
		listMatter.put(m6, m6);
		Matter m7 = new Matter(new Point2d(-312 + Math.random(),
				30 + Math.random()), 1E2 + Math.random(), 0, 0, 300, false);
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double a = Math.atan2(m.getPoint().getY()
						- m1.getPoint().getY(), m.getPoint().getX()
						- m1.getPoint().getX());
				m.setSpeedX(m.orbitalSpeed(m1) * Math.cos(a + Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));
			}
			mass += m.getMass();
		}
		double a = Math.atan2(m7.getPoint().getY() - m3.getPoint().getY(), m7
				.getPoint().getX() - m3.getPoint().getX());
		m7.setSpeedX(m3.getSpeedX() + m7.orbitalSpeed(m3)
				* Math.cos(a + Math.PI / 2));
		m7.setSpeedY(m3.getSpeedY() + m7.orbitalSpeed(m3)
				* Math.sin(a + Math.PI / 2));
		listMatter.put(m7, m7);

	}

	private void createPlanetaryRandom() {
		createUvivers(0, 0, 0, 0, 0, HelperVariable.nebulaRadius, 1);
		Matter m1 = new Matter(new Point2d(Math.random(), Math.random()),
				HelperVariable.darkMatterMass, 0, 0,
				HelperVariable.darkMatterDensity, false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double a = Math.atan2(m.getPoint().getY()
						- m1.getPoint().getY(), m.getPoint().getX()
						- m1.getPoint().getX());
				m.setSpeedX(m.orbitalSpeed(m1) * Math.cos(a + Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));
			}
		}
	}

	private void createPlanetariesGenesis() {
		createUvivers(0, 0, 0, 0, 250, 200, 1);
		Matter m1 = new Matter(new Point2d(Math.random(), Math.random()),
				HelperVariable.darkMatterMass, 0, 0,
				HelperVariable.darkMatterDensity, false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double a = Math.atan2(m.getPoint().getY()
						- m1.getPoint().getY(), m.getPoint().getX()
						- m1.getPoint().getX());
				m.setSpeedX(m.orbitalSpeed(m1) * Math.cos(a + Math.PI / 2));
				m.setSpeedY(m.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));
			}
		}
	}

	private void createDoubleStars() {
		Matter m1 = new Matter(new Point2d(Math.random() - 50,
				Math.random() - 90), 5E9 + 1E10 + Math.random(), 0, 0,
				HelperVariable.dentityMin, false);
		listMatter.put(m1, m1);
		mass += m1.getMass();

		Matter m2 = new Matter(new Point2d(Math.random() + 50,
				Math.random() + 90), 1E10 + Math.random(), 0, 0,
				HelperVariable.dentityMin, false);
		listMatter.put(m2, m2);
		mass += m2.getMass();

		double a = Math.atan2(m2.getPoint().getY() - m1.getPoint().getY(), m2
				.getPoint().getX() - m1.getPoint().getX());
		m1.setSpeedX(m2.orbitalSpeed(m1) * Math.cos(a + Math.PI / 2));
		m1.setSpeedY(m2.orbitalSpeed(m1) * Math.sin(a + Math.PI / 2));

		a = Math.atan2(m1.getPoint().getY() - m2.getPoint().getY(), m1
				.getPoint().getX() - m2.getPoint().getX());
		m2.setSpeedX(m1.orbitalSpeed(m2) * Math.cos(a + Math.PI / 2));
		m2.setSpeedY(m1.orbitalSpeed(m2) * Math.sin(a + Math.PI / 2));

	}

}
