package com.sylvanoid.joblib;

import java.util.TreeMap;
//import java.util.concurrent.ForkJoinPool;

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
	private Vector3d speed = new Vector3d(0, 0, 0);
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
		computeMassLimitsCentroidSpeed();
	}

	public Univers(Univers father) {
		this.parameters = father.parameters;
		listMatter = new TreeMap<Matter, Matter>();
	}

	public void computeMassLimitsCentroidSpeed() {
		boolean firstTime = true;
		speed = new Vector3d(0, 0, 0);
		mass = 0;
		visibleMass = 0;
		darkMass = 0;
		double tmpGx = 0;
		double tmpGy = 0;
		double tmpGz = 0;
		for (Matter m : listMatter.values()) {
			mass += m.getMass();
			if (m.isDark()) {
				darkMass += m.getMass();
			} else {
				visibleMass += m.getMass();
			}
			if (!firstTime) {
				if (min.x > m.getPoint().getX()) {
					min.x = m.getPoint().getX();
				}
				if (max.x < m.getPoint().getX()) {
					max.x = m.getPoint().getX();
				}
				if (min.y > m.getPoint().getY()) {
					min.y = m.getPoint().getY();
				}
				if (max.y < m.getPoint().getY()) {
					max.y = m.getPoint().getY();
				}
				if (min.z > m.getPoint().getZ()) {
					min.z = m.getPoint().getZ();
				}
				if (max.z < m.getPoint().getZ()) {
					max.z = m.getPoint().getZ();
				}
			} else {
				min.x = m.getPoint().getX();
				max.x = m.getPoint().getX();
				min.y = m.getPoint().getY();
				max.y = m.getPoint().getY();
				min.z = m.getPoint().getZ();
				max.z = m.getPoint().getZ();
				firstTime = false;
			}
			tmpGx += (m.getPoint().getX() * m.getMass());
			tmpGy += (m.getPoint().getY() * m.getMass());
			tmpGz += (m.getPoint().getZ() * m.getMass());
			speed.add(m.getSpeed());
		}
		gPoint = new Vector3d(tmpGx / mass, tmpGy / mass, tmpGz / mass);
	}

	public void process() {
		parameters.setNumOfCompute(0);
		parameters.setNumOfAccelCompute(0);
		long startTimeCycle = System.currentTimeMillis();
		computeMassLimitsCentroidSpeed();
		parameters.setLimitComputeTime(System.currentTimeMillis()
				- startTimeCycle);
		long startTimeBH = System.currentTimeMillis();
		BarnesHut barnesHut = new BarnesHut(this);
		barnesHut.compute();
		/*
		 * ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime()
		 * .availableProcessors()); pool.invoke(barnesHut);
		 */
		parameters.setBarnesHuttComputeTime(System.currentTimeMillis()
				- startTimeBH);

		long startTimeMove = System.currentTimeMillis();
		move();
		parameters.setMoveComputeTime(System.currentTimeMillis()
				- startTimeMove);
		parameters.setCycleComputeTime(System.currentTimeMillis()
				- startTimeCycle);

	}

	public boolean sameCoordonate() {
		for (Matter m : listMatter.values()) {
			for (Matter m1 : listMatter.values()) {
				if (m.getPoint().x != m1.getPoint().x
						|| m.getPoint().y != m1.getPoint().y
						|| m.getPoint().z != m1.getPoint().z) {
					return false;
				}
			}
		}
		return true;
	}

	private void move() {
		TreeMap<Matter, Matter> listMatterBis = new TreeMap<Matter, Matter>(
				listMatter);
		if (parameters.isManageImpact()) {
			for (Matter m : listMatterBis.values()) {
				if (m.getFusionWith().size() > 0) {
					if (parameters.isFusion() ) {
						Matter newM = m.fusion(listMatter);
						listMatter.put(newM, newM);
						listMatter.remove(m);
						for(Matter mbis:m.getFusionWith().values()){
							listMatter.remove(mbis);
						}
						// m.elastic(1E-15);
					} else {
						m.impact();
					}
				}
			}
		}

		for (Matter m : listMatter.values()) {
			m.move();
		}
	}

	private TreeMap<Matter, Matter> createUvivers(Vector3d origine,
			Vector3d initialSpeed, Vector3d axisOfRing, double radiusMin,
			double radiusMax, Vector3d ratio) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		miniListMatter.putAll(createUviversMain(origine, initialSpeed,
				axisOfRing, radiusMin, radiusMax, ratio,
				parameters.getNumberOfObjects(), parameters.getMassObjectMin(),
				parameters.getMassObjectMax(), parameters.getDensity(),
				new Vector3d(0, 0, 0)));

		miniListMatter.putAll(createUviversMain(origine, initialSpeed,
				axisOfRing, radiusMin, radiusMax, ratio,
				parameters.getNumOfLowMassParticule(), 0,
				parameters.getLowMassParticuleMass(),
				parameters.getLowMassDensity(), new Vector3d(0.1, 0.1, 0.1)));

		return miniListMatter;
	}

	private TreeMap<Matter, Matter> createUviversMain(Vector3d origine,
			Vector3d initialSpeed, Vector3d axisOfRing, double radiusMin,
			double radiusMax, Vector3d ratio, int numberOfObjects,
			double minMass, double maxMass, double density,
			Vector3d defaultColor) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < numberOfObjects; cpt++) {
			Matter m;
			double x = 1;
			double y = 1;
			double z = 1;
			boolean IsNotOK = true;
			while (IsNotOK) {

				double r = radiusMin + (radiusMax - radiusMin)
						* Math.pow(Math.random(), Math.pow(1, 3));
				double s = 2 * (Math.random() - 0.5);
				double alpha = 2 * Math.PI * (Math.random() - 0.5);
				double c = r * Math.pow(1 - Math.pow(s, 2), 0.5);
				x = c * Math.cos(alpha);
				y = c * Math.sin(alpha);
				z = r * s;

				IsNotOK = false;
				if (axisOfRing.x != 0) {
					IsNotOK = IsNotOK
							|| (Math.pow(y, 2) + Math.pow(z, 2) < Math.pow(
									radiusMin, 2));
				}
				if (axisOfRing.y != 0) {
					IsNotOK = IsNotOK
							|| (Math.pow(x, 2) + Math.pow(z, 2) < Math.pow(
									radiusMin, 2));
				}
				if (axisOfRing.z != 0) {
					IsNotOK = IsNotOK
							|| (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(
									radiusMin, 2));
				}
			}

			Vector3d color = defaultColor;
			if (defaultColor.equals(new Vector3d(0, 0, 0))) {
				double alea = Math.random();
				color = new Vector3d(0.45 + Math.random() * 0.05,
						0.45 + Math.random() * 0.05,
						0.45 + Math.random() * 0.05);
				if (alea > 0.80) {
					color.set(new Vector3d(0.55 + Math.random() * 0.05,
							0.45 + Math.random() * 0.05,
							0.45 + Math.random() * 0.05));
				}
				if (alea > 0.90) {
					color.set(new Vector3d(0.45 + Math.random() * 0.05,
							0.45 + Math.random() * 0.05,
							0.55 + Math.random() * 0.05));
				}
			}
			m = new Matter(parameters, new Vector3d(origine.x + x * ratio.x,
					origine.y + y * ratio.y, origine.z + z * ratio.z), minMass
					+ Math.random() * (maxMass - minMass) + 1E-100
					* Math.random(), new Vector3d(0, 0, 0), color, density,
					false);
			miniListMatter.put(m, m);
			miniMass += m.getMass();
		}

		for (Matter m : miniListMatter.values()) {
			m.setSpeed(new Vector3d(initialSpeed));
		}

		listMatter.putAll(miniListMatter);
		mass += miniMass;

		return miniListMatter;
	}

	private void createRandomStaticUvivers() {
		TreeMap<Matter, Matter> subu01 = createUvivers(new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), 0,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 1));
		listMatter.putAll(subu01);
	}

	private void createRandomRotateUnivers() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25));

		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), 0), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(0.25, 0.25, 0.25),
				parameters.getDarkMatterDensity(), true);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		darkMass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				double distance = new javax.vecmath.Point3d(m1.getPoint())
						.distance(new javax.vecmath.Point3d(m.getPoint()));
				double distancey = Math.abs(m.getPoint().y - m1.getPoint().y);
				double demiaxis = distance * 0.50 + 0.50 * distancey;
				m.setSpeed(m.orbitalEllipticSpeed(m1, demiaxis, new Vector3d(0,
						0, 1)));
				double angle = -Math.PI / 4 * distance
						/ parameters.getNebulaRadius();
				m.setPoint(HelperVector.rotate(m.getPoint(), new Vector3d(0, 0,
						1), angle));
				m.setSpeed(HelperVector.rotate(m.getSpeed(), new Vector3d(0, 0,
						1), angle));
			}
		}
	}

	private void createGalaxiesCollision() {
		double initialSpeedx = 1E5;
		double deltax = 70000;
		double deltay = 25000;
		double deltaz = 25000;
		TreeMap<Matter, Matter> subu01 = createUvivers(new Vector3d(
				-HelperVariable.PC * deltax, -HelperVariable.PC * deltay,
				-HelperVariable.PC * deltaz),
				new Vector3d(initialSpeedx, 0, 0), new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(0.15, 1, 0.15));

		TreeMap<Matter, Matter> subu02 = createUvivers(new Vector3d(
				HelperVariable.PC * deltax, HelperVariable.PC * deltay,
				HelperVariable.PC * deltaz),
				new Vector3d(-initialSpeedx, 0, 0), new Vector3d(1, 0, 0),
				parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius() / 2, new Vector3d(1, 0.15, 0.15));

		Matter m1 = new Matter(parameters, new Vector3d(-HelperVariable.PC
				* deltax + Math.random(), -HelperVariable.PC * deltay
				+ Math.random(), -HelperVariable.PC * deltaz + Math.random()),
				parameters.getDarkMatterMass() / 1.1 + Math.random(),
				new Vector3d(initialSpeedx, 0, 0), new Vector3d(0.25, 0.25,
						0.25), parameters.getDarkMatterDensity(), true);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		darkMass += m1.getMass();

		Matter m2 = new Matter(parameters, new Vector3d(HelperVariable.PC
				* deltax + Math.random(), HelperVariable.PC * deltay
				+ Math.random(), HelperVariable.PC * deltaz + Math.random()),
				parameters.getDarkMatterMass() / 2 + Math.random(),
				new Vector3d(-initialSpeedx, 0, 0), new Vector3d(0.25, 0.25,
						0.25), parameters.getDarkMatterDensity(), true);
		listMatter.put(m2, m2);
		mass += m2.getMass();
		darkMass += m2.getMass();

		for (Matter m : subu01.values()) {
			if (m != m1) {
				Vector3d newSpeed = new Vector3d(m.getSpeed());
				newSpeed.add(m.orbitalCircularSpeed(m1, new Vector3d(0, 0, 1)));
				m.setSpeed(newSpeed);
			}
		}

		for (Matter m : subu02.values()) {
			if (m != m2) {
				Vector3d newSpeed = new Vector3d(m.getSpeed());
				newSpeed.add(m.orbitalCircularSpeed(m2, new Vector3d(0, 1, 0)));
				m.setSpeed(newSpeed);
			}
		}

		listMatter.putAll(subu01);
		listMatter.putAll(subu02);
	}

	private void createPlanetary() {
		Matter sun = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()),
				HelperVariable.M + Math.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 1, 0.5), 1408, false);
		sun.setName("Sun");
		listMatter.put(sun, sun);

		Matter mercure = new Matter(parameters, new Vector3d(0.38709893
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 330.2E21 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 5427, false);
		mercure.setName("Mercure");
		listMatter.put(mercure, mercure);

		Matter venus = new Matter(parameters, new Vector3d(0.723332
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 5.972E24 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 0.8), 5.204E3, false);
		venus.setName("Venus");
		listMatter.put(venus, venus);

		Matter earth = new Matter(parameters, new Vector3d(1
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 4.8685E24 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.7, 0.7, 1), 5.52E3, false);
		earth.setName("Earth");
		listMatter.put(earth, earth);

		Matter mars = new Matter(parameters, new Vector3d(1.52366231
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 641.85E21 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.7, 0.7), 3933.5, false);
		mars.setName("Mars");
		listMatter.put(mars, mars);

		Matter jupiter = new Matter(parameters, new Vector3d(5.20336301
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 1.8986E27 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 1326, false);
		jupiter.setName("Jupiter");
		listMatter.put(jupiter, jupiter);

		Matter saturn = new Matter(parameters, new Vector3d(9.53707032
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 568.46E24 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.9, 0.9, 0.9), 687.3,
				false);
		saturn.setName("Saturn");
		listMatter.put(saturn, saturn);

		Matter moon = new Matter(parameters, new Vector3d((1 + 0.00257)
				* HelperVariable.UA + Math.random(), Math.random(),
				Math.random()), 7.3477E22 + Math.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), 3.3464E3, false);
		moon.setName("Moon");
		listMatter.put(moon, moon);

		for (Matter m : listMatter.values()) {
			if (m != sun) {
				m.setSpeed(m.orbitalCircularSpeed(sun, new Vector3d(0, 1, 0)));
			}
		}

		mercure.setPoint(HelperVector.rotate(mercure.getPoint(), new Vector3d(
				0, 0, 1), 7 * Math.PI / 180));
		venus.setPoint(HelperVector.rotate(venus.getPoint(), new Vector3d(0, 0,
				1), 3 * Math.PI / 180));
		mars.setPoint(HelperVector.rotate(mars.getPoint(),
				new Vector3d(0, 0, 1), 1.8 * Math.PI / 180));
		jupiter.setPoint(HelperVector.rotate(jupiter.getPoint(), new Vector3d(
				0, 0, 1), 1.3 * Math.PI / 180));
		saturn.setPoint(HelperVector.rotate(saturn.getPoint(), new Vector3d(0,
				0, 1), 2.48 * Math.PI / 180));

		Vector3d newSpeed = new Vector3d(moon.getSpeed());
		newSpeed.add(moon.orbitalCircularSpeed(earth, new Vector3d(0, 1, 0)));
		moon.setSpeed(newSpeed);
	}

	private void createPlanetaryRandom() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 1, 0), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 0.1, 1));
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalCircularSpeed(m1, new Vector3d(0, 1, 0)));
				m.setTypeOfObject(TypeOfObject.Planetary);
			}
		}
	}

	private void createPlanetariesGenesis() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() / 2 * 0.9,
				parameters.getNebulaRadius() / 2, new Vector3d(1, 1, 0.05));
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.9,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.05));
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), false);
		listMatter.put(m1, m1);
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalCircularSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}
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

	public Vector3d getSpeed() {
		return speed;
	}

	public Vector3d getMin() {
		return min;
	}

	public Vector3d getMax() {
		return max;
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
}
