package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement
public class Univers {
	@XmlElement
	private Parameters parameters;
	@XmlElement
	private double mass;
	@XmlElement
	private double visibleMass;
	@XmlElement
	private double darkMass;
	@XmlElement
	private TreeMap<Matter, Matter> listMatter;
	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d gPoint = new Vector3d(0, 0, 0);
	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d min = new Vector3d(0, 0, 0);
	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d max = new Vector3d(0, 0, 0);

	@Override
	public String toString() {
		return ("m:" + mass + " gx:" + gPoint.y + " gy:" + gPoint.y + " gz:" + gPoint.z);
	}

	public Univers() {

	}

	public Univers(Parameters parameters) {
		this.parameters = parameters;
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
		visibleMass = 0;
		darkMass = 0;
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.Random) {
			createRandomStaticUvivers();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomRotateUnivers) {
			createRandomRotateUnivers();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.GalaxiesCollision) {
			createGalaxiesCollision();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.Planetary) {
			createPlanetary();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.PlanetaryRandom) {
			createPlanetaryRandom();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.PlanetariesGenesis) {
			createPlanetariesGenesis();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.DoubleStars) {
			createDoubleStars();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomInitialExpension) {
			createRandomStaticUvivers();
		}
	}

	public Univers(Univers father, Vector3d min, Vector3d max) {
		this.parameters = father.parameters;
		this.min = min;
		this.max = max;
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
		visibleMass = 0;
		darkMass = 0;
		for (Matter m : father.getListMatter().values()) {
			if (m.getPoint().getX() >= min.x && m.getPoint().getX() <= max.x
					&& m.getPoint().getY() >= min.y
					&& m.getPoint().getY() <= max.y
					&& m.getPoint().getZ() >= min.z
					&& m.getPoint().getZ() <= max.z) {
				listMatter.put(m, m);
				mass += m.getMass();
				if (m.isDark()) {
					darkMass += m.getMass();
				} else {
					visibleMass += m.getMass();
				}
			}
		}
	}

	public void computeLimits() {
		boolean firstTime = true;
		for (Matter o : listMatter.values()) {
			if (!firstTime) {
				if (min.x > o.getPoint().getX()) {
					min.x = o.getPoint().getX();
				}
				if (max.x < o.getPoint().getX()) {
					max.x = o.getPoint().getX();
				}
				if (min.y > o.getPoint().getY()) {
					min.y = o.getPoint().getY();
				}
				if (max.y < o.getPoint().getY()) {
					max.y = o.getPoint().getY();
				}
				if (min.z > o.getPoint().getZ()) {
					min.z = o.getPoint().getZ();
				}
				if (max.z < o.getPoint().getZ()) {
					max.z = o.getPoint().getZ();
				}
			} else {
				min.x = o.getPoint().getX();
				max.x = o.getPoint().getX();
				min.y = o.getPoint().getY();
				max.y = o.getPoint().getY();
				min.z = o.getPoint().getZ();
				max.z = o.getPoint().getZ();
				firstTime = false;
			}
		}
	}

	public void computeCentroidOfUnivers() {
		double tmpGx = 0;
		double tmpGy = 0;
		double tmpGz = 0;
		for (Matter m : getListMatter().values()) {
			tmpGx += (m.getPoint().getX() * m.getMass());
			tmpGy += (m.getPoint().getY() * m.getMass());
			tmpGz += (m.getPoint().getZ() * m.getMass());
		}
		gPoint = new Vector3d(tmpGx / getMass(), tmpGy / getMass(), tmpGz
				/ getMass());
	}

	public void resetAcceleration() {
		for (Matter m : listMatter.values()) {
			m.setA(new Vector3d(0, 0, 0));
		}
	}

	public void compute() {

		if (listMatter.size() > 1) {
			// Découpage en 4 et compute de chacun
			double cx = min.x + (max.x - min.x) / 2;
			double cy = min.y + (max.z - min.y) / 2;
			double cz = min.z + (max.z - min.z) / 2;
			List<Univers> subUnivers = new ArrayList<Univers>();
			Univers suba = new Univers(this, new Vector3d(min.x, min.y, min.z),
					new Vector3d(cx, cy, cz));
			Univers subb = new Univers(this, new Vector3d(cx, min.y, min.z),
					new Vector3d(max.x, cy, cz));
			Univers subc = new Univers(this, new Vector3d(cx, cy, min.z),
					new Vector3d(max.x, max.y, cz));
			Univers subd = new Univers(this, new Vector3d(min.x, cy, min.z),
					new Vector3d(cx, max.y, cz));

			Univers sube = new Univers(this, new Vector3d(min.x, min.y, cz),
					new Vector3d(cx, cy, max.z));
			Univers subf = new Univers(this, new Vector3d(cx, min.y, cz),
					new Vector3d(max.x, cy, max.z));
			Univers subg = new Univers(this, new Vector3d(cx, cy, cz),
					new Vector3d(max.x, max.y, max.z));
			Univers subh = new Univers(this, new Vector3d(min.x, cy, cz),
					new Vector3d(cx, max.y, max.z));

			subUnivers.add(suba);
			subUnivers.add(subb);
			subUnivers.add(subc);
			subUnivers.add(subd);

			subUnivers.add(sube);
			subUnivers.add(subf);
			subUnivers.add(subg);
			subUnivers.add(subh);

			listMatter = new TreeMap<Matter, Matter>();
			mass = 0;
			visibleMass = 0;
			darkMass = 0;
			for (Univers u : subUnivers) {
				u.compute();
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin && uvoisin.getListMatter().size() > 0) {
						uvoisin.computeCentroidOfUnivers();
						TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
								new MassComparator());
						sortByMass.putAll(u.listMatter);
						for (Matter m : sortByMass.values()) {
							double distance = new Point3d(m.getPoint())
									.distance(new Point3d(uvoisin.getGPoint()));
							double attraction = parameters.getTimeFactor()
									* HelperVariable.G
									* (((uvoisin.getMass()) / Math.pow(
											distance, 2)));
							m.getA().add(
									HelperVector.acceleration(m.getPoint(),
											uvoisin.getGPoint(), attraction));
						}
					}
				}
				for (Matter m : u.getListMatter().values()) {
					listMatter.put(m, m);
					mass += m.getMass();
					if (m.isDark()) {
						darkMass += m.getMass();
					} else {
						visibleMass += m.getMass();
					}
				}
			}
		}
	}

	private void treatNeighborFusion(Matter m1, TreeMap<Double, Matter> sortX,
			HashMap<Matter, String> treated, List<Matter[]> toTreat) {
		if (treated.get(m1) == null) {
			treated.put(m1, "");
			SortedMap<Double, Matter> selectX = sortX.subMap(m1.minWithR().x,
					true, m1.maxWithR().x, false);
			TreeMap<Double, Matter> sortY = new TreeMap<Double, Matter>();
			for (Matter m : selectX.values()) {
				sortY.put(m.minWithR().y, m);
				sortY.put(m.getPoint().y, m);
				sortY.put(m.maxWithR().y, m);
			}
			SortedMap<Double, Matter> selectY = sortY.subMap(m1.minWithR().y,
					true, m1.maxWithR().y, false);
			TreeMap<Double, Matter> sortZ = new TreeMap<Double, Matter>();
			for (Matter m : selectY.values()) {
				sortZ.put(m.minWithR().z, m);
				sortZ.put(m.getPoint().z, m);
				sortZ.put(m.maxWithR().z, m);
			}
			SortedMap<Double, Matter> selectZ = sortZ.subMap(m1.minWithR().z,
					true, m1.maxWithR().z, false);

			for (Matter m2 : selectZ.values()) {
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
		SortedMap<Double, Matter> selectX = sortX.subMap(m1.minWithR().x, true,
				m1.maxWithR().x, false);
		TreeMap<Double, Matter> sortY = new TreeMap<Double, Matter>();
		for (Matter m : selectX.values()) {
			sortY.put(m.minWithR().y, m);
			sortY.put(m.getPoint().y, m);
			sortY.put(m.maxWithR().y, m);
		}
		SortedMap<Double, Matter> selectY = sortY.subMap(m1.minWithR().y, true,
				m1.maxWithR().y, false);

		TreeMap<Double, Matter> sortZ = new TreeMap<Double, Matter>();
		for (Matter m : selectY.values()) {
			sortZ.put(m.minWithR().z, m);
			sortZ.put(m.getPoint().z, m);
			sortZ.put(m.maxWithR().z, m);
		}
		SortedMap<Double, Matter> selectZ = sortZ.subMap(m1.minWithR().z, true,
				m1.maxWithR().z, false);

		for (Matter m2 : selectZ.values()) {
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
		TreeMap<Double, Matter> sortXDark = new TreeMap<Double, Matter>();
		for (Matter m : listMatter.values()) {
			if (!m.isDark()) {
				sortX.put(m.minWithR().x, m);
				sortX.put(m.getPoint().x, m);
				sortX.put(m.maxWithR().x, m);
			} else {
				sortXDark.put(m.minWithR().x, m);
				sortXDark.put(m.getPoint().x, m);
				sortXDark.put(m.maxWithR().x, m);
			}
		}

		for (Matter m1 : listMatter.values()) {
			if (!m1.isDark()) {
				if (parameters.isFusion()) {
					treatNeighborFusion(m1, sortX, treatedFusion, toTreat);
				} else {
					treatNeighborImpact(m1, sortX, toTreat);
				}
			} else {
				if (parameters.isFusion()) {
					treatNeighborFusion(m1, sortXDark, treatedFusion, toTreat);
				} else {
					treatNeighborImpact(m1, sortXDark, toTreat);
				}
			}
		}
		for (int cpt = 0; cpt < toTreat.size(); cpt++) {
			Matter[] element = toTreat.get(cpt);
			if (parameters.isFusion()) {
				element[0].fusion(element[1]);
				listMatter.remove(element[1]);
			} else {
				element[0].impact(element[1]);
			}
		}
		mass = 0;
		visibleMass = 0;
		darkMass = 0;
		for (Matter m : listMatter.values()) {
			mass += m.getMass();
			if (m.isDark()) {
				darkMass += m.getMass();
			} else {
				visibleMass += m.getMass();
			}
		}
	}

	public void move() {

		if (parameters.isManageImpact()) {
			double oldMass = mass;
			manageImpact();
			if (Math.abs(mass - oldMass) > 1) {
				System.out.println("wtf :" + mass + " - " + oldMass + " = "
						+ (mass - oldMass));
			}
		}

		for (Matter m : listMatter.values()) {
			double exp = 1 + parameters.getTimeFactor()
					* parameters.getExpensionOfUnivers();
			m.getPoint().x *= exp;
			m.getPoint().y *= exp;
			m.getPoint().z *= exp;

			m.move();
		}
	}

	public void process() {
		resetAcceleration();
		computeLimits();
		compute();
		move();
	}

	public Parameters getParameters() {
		return parameters;
	}

	public TreeMap<Matter, Matter> getListMatter() {
		return listMatter;
	}

	public Vector3d getGPoint() {
		return gPoint;
	}

	public double getMass() {
		return mass;
	}

	public double getVisibleMass() {
		return visibleMass;
	}

	public double getDarkMass() {
		return darkMass;
	}

	private TreeMap<Matter, Matter> createUvivers(Vector3d origine,
			double delta, double speedTrans, double radiusMin,
			double radiusMax, double ratiox, double ratioy, double ratioz) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		Random random = new Random();
		double miniMass = 0;
		for (int cpt = 0; cpt < parameters.getNumberOfObjects(); cpt++) {
			Matter m;
			boolean cont = false;
			do {
				double x = 1;
				double y = 1;
				double z = 1;
				while (x * x + y * y + z * z > 1) {
					x = 2 * (random.nextDouble() - 0.5);
					y = 2 * (random.nextDouble() - 0.5);
					z = 2 * (random.nextDouble() - 0.5);
				}

				m = new Matter(
						parameters,
						new Vector3d(
								origine.x
										+ (x * ratiox * (radiusMax - radiusMin) + radiusMin),
								origine.y
										+ (y * ratioy * (radiusMax - radiusMin) + radiusMin),
								origine.z
										+ (z * ratioz * (radiusMax - radiusMin) + radiusMin)),
						parameters.getMassObjectMin()
								+ random.nextDouble()
								* (parameters.getMassObjectMax() - parameters
										.getMassObjectMin())
								+ random.nextDouble(), new Vector3d(0, 0, 0),
						parameters.getDensity(), false);
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

			for (Matter mbis : miniListMatter.values()) {
				double alea = random.nextDouble();
				mbis.setColor(new Vector3d(0.80 + random.nextDouble() * 0.20,
						0.80 + random.nextDouble() * 0.20, 0.80 + random
								.nextDouble() * 0.20));
				if (alea > 0.80) {
					mbis.setColor(new Vector3d(
							0.90 + random.nextDouble() * 0.1, 0.4 + random
									.nextDouble() * 0.2, 0.4 + random
									.nextDouble() * 0.2));
				}
				if (alea > 0.90) {
					mbis.setColor(new Vector3d(
							0.4 + random.nextDouble() * 0.20, 0.4 + random
									.nextDouble() * 0.20, 0.9 + random
									.nextDouble() * 0.10));
				}
			}

		}

		for (Matter m : miniListMatter.values()) {
			double distance = Math
					.pow(Math.pow(m.getPoint().getX() - origine.x, 2)
							+ Math.pow(m.getPoint().getY() - origine.y, 2), 0.5);
			double angle = Math.atan2(m.getPoint().getY() - origine.y, m
					.getPoint().getX() - origine.x);
			m.setSpeed(new Vector3d(m.getPoint().getX() - origine.x
					- Math.cos(angle - delta) * distance + speedTrans, m
					.getPoint().getY()
					- origine.y
					- Math.sin(angle - delta)
					* distance, 0));
		}

		listMatter.putAll(miniListMatter);
		mass += miniMass;

		return miniListMatter;
	}

	private void createRandomStaticUvivers() {
		TreeMap<Matter, Matter> subu01 = createUvivers(new Vector3d(0, 0, 0),
				0, 0, 0, parameters.getNebulaRadius(), 1, 1, 1);
		listMatter.putAll(subu01);
	}

	private void createRandomRotateUnivers() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				parameters.getNebulaRadius(), 0.25, 1, 0.25);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), 0), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), parameters.getDarkMatterDensity(), true);
		m1.setColor(new Vector3d(0.25, 0.25, 0.25));
		listMatter.put(m1, m1);
		mass += m1.getMass();
		darkMass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}
	}

	private void createGalaxiesCollision() {
		double transSpeed = 0.3;
		TreeMap<Matter, Matter> subu01 = createUvivers(new Vector3d(-400, -100,
				-50), 0, transSpeed, 0, parameters.getNebulaRadius(), 0.25, 1,
				0.25);
		TreeMap<Matter, Matter> subu02 = createUvivers(new Vector3d(400, 100,
				50), 0, -transSpeed, 0, parameters.getNebulaRadius() / 2, 1,
				0.15, 0.15);

		Matter m1 = new Matter(parameters, new Vector3d(-400 + Math.random(),
				-100 + Math.random(), -50 + Math.random()),
				parameters.getDarkMatterMass() + Math.random(), new Vector3d(
						transSpeed, 0, 0), parameters.getDarkMatterDensity(),
				true);
		m1.setColor(new Vector3d(0.25, 0.25, 0.25));
		listMatter.put(m1, m1);
		mass += m1.getMass();
		darkMass += m1.getMass();

		Matter m2 = new Matter(parameters, new Vector3d(400 + Math.random(),
				100 + Math.random(), 50 + Math.random()),
				parameters.getDarkMatterMass() + Math.random(), new Vector3d(
						-transSpeed, 0, 0), parameters.getDarkMatterDensity(),
				true);
		m2.setColor(new Vector3d(0.25, 0.25, 0.25));
		listMatter.put(m2, m2);
		mass += m2.getMass();
		darkMass += m2.getMass();

		for (Matter m : subu01.values()) {
			if (m != m1) {
				m.getSpeed().add(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}

		for (Matter m : subu02.values()) {
			if (m != m2) {
				m.getSpeed().add(m.orbitalSpeed(m2, new Vector3d(0, 1, 0)));
			}
		}
		listMatter.putAll(subu01);
		listMatter.putAll(subu02);
	}

	private void createPlanetary() {
		Matter sun = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), 1.9891E30 + Math.random(),
				new Vector3d(0, 0, 0), 1408, false);
		sun.setColor(new Vector3d(1, 1, 0.5));
		sun.setName("Sun");
		listMatter.put(sun, sun);

		Matter mercure = new Matter(parameters, new Vector3d(0.38709893
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 330.2E21 + Math.random(),
				new Vector3d(0, 0, 0), 5427, false);
		mercure.setColor(new Vector3d(1, 0.8, 0.8));
		mercure.setName("Mercure");
		listMatter.put(mercure, mercure);

		Matter venus = new Matter(parameters, new Vector3d(0.723332
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 5.972E24 + Math.random(),
				new Vector3d(0, 0, 0), 5.204E3, false);
		venus.setColor(new Vector3d(1, 1, 0.8));
		venus.setName("Venus");
		listMatter.put(venus, venus);

		Matter earth = new Matter(parameters, new Vector3d(1
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 4.8685E24 + Math.random(),
				new Vector3d(0, 0, 0), 5.52E3, false);
		earth.setColor(new Vector3d(0.7, 0.7, 1));
		earth.setName("Earth");
		listMatter.put(earth, earth);

		Matter mars = new Matter(parameters, new Vector3d(1.52366231
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 641.85E21 + Math.random(),
				new Vector3d(0, 0, 0), 3933.5, false);
		mars.setColor(new Vector3d(1, 0.7, 0.7));
		mars.setName("Mars");
		listMatter.put(mars, mars);

		Matter jupiter = new Matter(parameters, new Vector3d(5.20336301
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 1.8986E27 + Math.random(),
				new Vector3d(0, 0, 0), 1326, false);
		jupiter.setColor(new Vector3d(1, 0.8, 0.8));
		jupiter.setName("Jupiter");
		listMatter.put(jupiter, jupiter);

		Matter saturn = new Matter(parameters, new Vector3d(9.53707032
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 568.46E24 + Math.random(),
				new Vector3d(0, 0, 0), 687.3, false);
		saturn.setColor(new Vector3d(0.9, 0.9, 0.9));
		saturn.setName("Saturn");
		listMatter.put(saturn, saturn);

		Matter moon = new Matter(parameters, new Vector3d((1 + 0.00257)
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 7.3477E22 + Math.random(),
				new Vector3d(0, 0, 0), 3.3464E3, false);
		moon.setColor(new Vector3d(1, 1, 1));
		moon.setName("Moon");
		listMatter.put(moon, moon);

		for (Matter m : listMatter.values()) {
			if (m != sun) {
				m.setSpeed(m.orbitalSpeed(sun, new Vector3d(0, 1, 0)));
				m.setTypeOfObject(TypeOfObject.Planetary);
			}
			mass += m.getMass();
			visibleMass += m.getMass();
		}

		moon.getSpeed().add(moon.orbitalSpeed(earth, new Vector3d(0, 1, 0)));
	}

	private void createPlanetaryRandom() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				parameters.getNebulaRadius(), 1, 0.1, 1);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), parameters.getDarkMatterDensity(), false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 1, 0)));
				m.setTypeOfObject(TypeOfObject.Planetary);
			}
		}
	}

	private void createPlanetariesGenesis() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				parameters.getNebulaRadius(), 1, 1, 0.1);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), parameters.getDarkMatterDensity(), false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
				m.setTypeOfObject(TypeOfObject.Planetary);
			}
		}
	}

	private void createDoubleStars() {
		Matter m1 = new Matter(parameters, new Vector3d(Math.random() - 50,
				Math.random() - 90, Math.random()), 5E9 + 1E10 + Math.random(),
				new Vector3d(0, 0, 0), parameters.getDensity(), false);
		m1.setColor(new Vector3d(1, 0.7, 0.7));
		listMatter.put(m1, m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();

		Matter m2 = new Matter(parameters, new Vector3d(Math.random() + 50,
				Math.random() + 90, Math.random()), 1E10 + Math.random(),
				new Vector3d(0, 0, 0), parameters.getDensity(), false);
		m2.setColor(new Vector3d(0.8, 0.8, 1));
		listMatter.put(m2, m2);
		mass += m2.getMass();
		visibleMass += m1.getMass();
		m1.getSpeed().add(m1.orbitalSpeed(m2, new Vector3d(0, 0, 1)));
		m2.getSpeed().add(m2.orbitalSpeed(m1, new Vector3d(0, 0, 1)));

	}
}
