package com.sylvanoid.joblib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

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
	private double volumicMass;
	@XmlElement
	private List<Matter> listMatter;
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

	private Matter maxMassElement = null;

	@Override
	public String toString() {
		return ("m:" + mass + " gx:" + gPoint.y + " gy:" + gPoint.y + " gz:" + gPoint.z);
	}

	public Univers() {

	}

	public Univers(Parameters parameters) {
		this.parameters = parameters;
		listMatter = new ArrayList<Matter>();
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
		computeMassLimitsCentroidSpeed(true);
	}

	public Univers(Univers father, Vector3d min, Vector3d max) {
		this.parameters = father.parameters;
		listMatter = new ArrayList<Matter>();
		this.min = new Vector3d(min);
		this.max = new Vector3d(max);
	}

	public void computeMassLimitsCentroidSpeed(boolean withLimit) {
		boolean firstTime = true;
		volumicMass = 0;
		speed = new Vector3d(0, 0, 0);
		mass = 0;
		visibleMass = 0;
		darkMass = 0;
		double tmpGx = 0;
		double tmpGy = 0;
		double tmpGz = 0;
		for (Matter m : listMatter) {
			mass += m.getMass();
			if (m.isDark()) {
				darkMass += m.getMass();
			} else {
				visibleMass += m.getMass();
			}
			if (withLimit) {
				if (!firstTime) {
					min.x = min.x > m.getPoint().getX() ? m.getPoint().getX()
							: min.x;
					max.x = max.x < m.getPoint().getX() ? m.getPoint().getX()
							: max.x;
					min.y = min.y > m.getPoint().getX() ? m.getPoint().getX()
							: min.y;
					max.y = max.y < m.getPoint().getX() ? m.getPoint().getX()
							: max.y;
					min.z = min.z > m.getPoint().getX() ? m.getPoint().getX()
							: min.z;
					max.z = max.z < m.getPoint().getX() ? m.getPoint().getX()
							: max.z;
				} else {
					min.x = m.getPoint().getX();
					max.x = m.getPoint().getX();
					min.y = m.getPoint().getY();
					max.y = m.getPoint().getY();
					min.z = m.getPoint().getZ();
					max.z = m.getPoint().getZ();
					firstTime = false;
				}
			}
			tmpGx += (m.getPoint().getX() * m.getMass());
			tmpGy += (m.getPoint().getY() * m.getMass());
			tmpGz += (m.getPoint().getZ() * m.getMass());
			speed.add(m.getSpeed());
		}
		volumicMass = visibleMass
				/ ((max.x - min.x) * (max.y - min.y) * (max.z - min.z));
		gPoint = new Vector3d(tmpGx / mass, tmpGy / mass, tmpGz / mass);
	}

	@SuppressWarnings("unchecked")
	public void process(ObjectOutputStream objectOutputStream,
			ObjectInputStream objectInputStream) {
		if (!parameters.isPlayData()) {
			parameters.setNumOfCompute(0);
			parameters.setNumOfAccelCompute(0);
			long startTimeCycle = System.currentTimeMillis();
			computeMassLimitsCentroidSpeed(true);
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
			move(objectOutputStream);
			parameters.setMoveComputeTime(System.currentTimeMillis()
					- startTimeMove);
			parameters.setCycleComputeTime(System.currentTimeMillis()
					- startTimeCycle);
		} else {
			parameters.setNumOfCompute(-9999);
			parameters.setNumOfAccelCompute(-9999);
			long startTimeCycle = System.currentTimeMillis();
			try {
				listMatter = new ArrayList<Matter>(
						(List<Matter>) objectInputStream.readObject());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				parameters.setPlayData(false);
			}
			computeMassLimitsCentroidSpeed(true);
			parameters.setCycleComputeTime(System.currentTimeMillis()
					- startTimeCycle);
		}
	}

	public boolean sameCoordonate() {
		for (Matter m : listMatter) {
			for (Matter m1 : listMatter) {
				if (m.getPoint().x != m1.getPoint().x
						|| m.getPoint().y != m1.getPoint().y
						|| m.getPoint().z != m1.getPoint().z) {
					return false;
				}
			}
		}
		return true;
	}

	private void move(ObjectOutputStream objectOutputStream) {
		List<Matter> listMatterBis = new ArrayList<Matter>(listMatter);
		if (parameters.isManageImpact()) {
			for (Matter m : listMatterBis) {
				if (m.getFusionWith().size() > 0) {
					if (parameters.isFusion()) {
						if (listMatter.contains(m)) {
							m.fusion(listMatter);
						}
					} else {
						m.impact();
					}
					m.getFusionWith().clear();
				}
			}
		}
		for (Matter m : listMatter) {
			if (maxMassElement == null
					|| maxMassElement.getMass() < m.getMass()) {
				maxMassElement = m;
			}
			m.move();
		}
		if (parameters.isExportData()) {
			try {
				objectOutputStream.writeObject(listMatter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private List<Matter> createUvivers(Vector3d origine, Vector3d initialSpeed,
			Vector3d axisOfRing, double radiusMin, double radiusMax,
			Vector3d ratio) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		miniListMatter.addAll(createUviversMain(origine, initialSpeed,
				axisOfRing, radiusMin, radiusMax, ratio,
				parameters.getNumberOfObjects(), parameters.getMassObjectMin(),
				parameters.getMassObjectMax(), parameters.getDensity(),
				new Vector3d(0, 0, 0)));

		miniListMatter
				.addAll(createUviversMain(origine, initialSpeed, axisOfRing,
						radiusMin, radiusMax, ratio, parameters
								.getNumOfLowMassParticule(), 0, parameters
								.getLowMassParticuleMass(), parameters
								.getLowMassDensity(), new Vector3d(0.06, 0.05,
								0.05)));

		return miniListMatter;
	}

	private List<Matter> createUviversMain(Vector3d origine,
			Vector3d initialSpeed, Vector3d axisOfRing, double radiusMin,
			double radiusMax, Vector3d ratio, int numberOfObjects,
			double minMass, double maxMass, double density,
			Vector3d defaultColor) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < numberOfObjects; cpt++) {
			Matter m;
			double x = 1;
			double y = 1;
			double z = 1;
			boolean IsNotOK = true;
			while (IsNotOK) {

				double r = radiusMin
						+ (radiusMax - radiusMin)
						* net.jafama.FastMath.pow(net.jafama.FastMath.random(),
								1d / 3d);
				double s = 2 * (net.jafama.FastMath.random() - 0.5);
				double alpha = 2 * net.jafama.FastMath.PI
						* (net.jafama.FastMath.random() - 0.5);
				double c = r
						* net.jafama.FastMath.sqrt(1 - net.jafama.FastMath
								.pow2(s));
				x = c * net.jafama.FastMath.cos(alpha);
				y = c * net.jafama.FastMath.sin(alpha);
				z = r * s;

				IsNotOK = false;
				if (axisOfRing.x != 0) {
					IsNotOK = IsNotOK
							|| (net.jafama.FastMath.pow2(y)
									+ net.jafama.FastMath.pow2(z) < net.jafama.FastMath
										.pow2(radiusMin));
				}
				if (axisOfRing.y != 0) {
					IsNotOK = IsNotOK
							|| (net.jafama.FastMath.pow2(x)
									+ net.jafama.FastMath.pow2(z) < net.jafama.FastMath
										.pow2(radiusMin));
				}
				if (axisOfRing.z != 0) {
					IsNotOK = IsNotOK
							|| (net.jafama.FastMath.pow2(x)
									+ net.jafama.FastMath.pow2(y) < net.jafama.FastMath
										.pow2(radiusMin));
				}
			}

			Vector3d color = defaultColor;
			if (defaultColor.equals(new Vector3d(0, 0, 0))) {
				double alea = net.jafama.FastMath.random();
				color = new Vector3d(
						0.45 + net.jafama.FastMath.random() * 0.05,
						0.45 + net.jafama.FastMath.random() * 0.05,
						0.45 + net.jafama.FastMath.random() * 0.05);
				if (alea > 0.80) {
					color.set(new Vector3d(
							0.55 + net.jafama.FastMath.random() * 0.05,
							0.45 + net.jafama.FastMath.random() * 0.05,
							0.45 + net.jafama.FastMath.random() * 0.05));
				}
				if (alea > 0.90) {
					color.set(new Vector3d(
							0.45 + net.jafama.FastMath.random() * 0.05,
							0.45 + net.jafama.FastMath.random() * 0.05,
							0.55 + net.jafama.FastMath.random() * 0.05));
				}
			}
			m = new Matter(parameters, new Vector3d(origine.x + x * ratio.x,
					origine.y + y * ratio.y, origine.z + z * ratio.z), minMass
					+ net.jafama.FastMath.random() * (maxMass - minMass)
					+ 1E-100 * net.jafama.FastMath.random(), new Vector3d(0, 0,
					0), color, density, false);
			miniListMatter.add(m);
			miniMass += m.getMass();
		}

		for (Matter m : miniListMatter) {
			m.setSpeed(new Vector3d(initialSpeed));
		}

		listMatter.addAll(miniListMatter);
		mass += miniMass;

		return miniListMatter;
	}

	private void createRandomStaticUvivers() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 0), 0, parameters.getNebulaRadius(),
				new Vector3d(1, 1, 1));
	}

	private void createRandomRotateUnivers() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25));

		Matter m1 = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(), 0),
				parameters.getDarkMatterMass(), new Vector3d(0, 0, 0),
				new Vector3d(0.25, 0.25, 0.25),
				parameters.getDarkMatterDensity(), true);
		listMatter.add(m1);
		mass += m1.getMass();
		darkMass += m1.getMass();
		for (Matter m : listMatter) {
			if (m != m1) {
				m.orbitalEllipticSpeed(m1, new Vector3d(0, 0, 1),
						parameters.getNbARms());
			}
		}
	}

	private void createGalaxiesCollision() {
		Vector3d dbg1 = new Vector3d(
				parameters.getDemiDistanceBetweenGalaxies());
		Vector3d dbg2 = new Vector3d(
				parameters.getDemiDistanceBetweenGalaxies());
		dbg2.negate();

		Matter m1 = new Matter(parameters, dbg2, parameters.getDarkMatterMass()
				/ 1.1 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(0.25, 0.25, 0.25),
				parameters.getDarkMatterDensity(), true);
		listMatter.add(m1);
		mass += m1.getMass();
		darkMass += m1.getMass();

		Matter m2 = new Matter(parameters, dbg1, parameters.getDarkMatterMass()
				/ 2 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(0.25, 0.25, 0.25),
				parameters.getDarkMatterDensity(), true);
		listMatter.add(m2);
		mass += m2.getMass();
		darkMass += m2.getMass();

		m1.orbitalCircularSpeed(m2, new Vector3d(0, 1, 0));
		m2.orbitalCircularSpeed(m1, new Vector3d(0, 1, 0));

		List<Matter> subu01 = createUvivers(m1.getPoint(), m1.getSpeed(),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25));

		List<Matter> subu02 = createUvivers(m2.getPoint(), m2.getSpeed(),
				new Vector3d(1, 0, 0), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 0.25, 1));

		for (Matter m : subu01) {
			if (m != m1) {
				m.orbitalCircularSpeed(m1, new Vector3d(0, 0, 1));
			}
		}

		for (Matter m : subu02) {
			if (m != m2) {
				m.orbitalCircularSpeed(m2, new Vector3d(0, 1, 0));
			}
		}
	}

	private void createPlanetary() {
		Matter sun = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(),
				net.jafama.FastMath.random()), HelperVariable.M
				+ net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 1, 0.5), 1408, false);
		sun.setName("Sun");
		listMatter.add(sun);

		Matter mercure = new Matter(parameters, new Vector3d(0.38709893
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				330.2E21 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 0.8, 0.8), 5427, false);
		mercure.setName("Mercure");
		listMatter.add(mercure);

		Matter venus = new Matter(parameters, new Vector3d(0.723332
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				5.972E24 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 1, 0.8), 5.204E3, false);
		venus.setName("Venus");
		listMatter.add(venus);

		Matter earth = new Matter(parameters, new Vector3d(1
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				4.8685E24 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.7, 0.7, 1), 5.52E3, false);
		earth.setName("Earth");
		listMatter.add(earth);

		Matter mars = new Matter(parameters, new Vector3d(1.52366231
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				641.85E21 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.7, 0.7), 3933.5, false);
		mars.setName("Mars");
		listMatter.add(mars);

		Matter jupiter = new Matter(parameters, new Vector3d(5.20336301
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				1.8986E27 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 1326, false);
		jupiter.setName("Jupiter");
		listMatter.add(jupiter);

		Matter saturn = new Matter(parameters, new Vector3d(9.53707032
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				568.46E24 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.9, 0.9, 0.9), 687.3,
				false);
		saturn.setName("Saturn");
		listMatter.add(saturn);

		Matter moon = new Matter(parameters, new Vector3d((1 + 0.00257)
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				7.3477E22 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), 3.3464E3, false);
		moon.setName("Moon");
		listMatter.add(moon);

		for (Matter m : listMatter) {
			if (m != sun) {
				m.orbitalCircularSpeed(sun, new Vector3d(0, 1, 0));
			}
		}

		mercure.setPoint(HelperVector.rotate(mercure.getPoint(), new Vector3d(
				0, 0, 1), 7 * net.jafama.FastMath.PI / 180));
		venus.setPoint(HelperVector.rotate(venus.getPoint(), new Vector3d(0, 0,
				1), 3 * net.jafama.FastMath.PI / 180));
		mars.setPoint(HelperVector.rotate(mars.getPoint(),
				new Vector3d(0, 0, 1), 1.8 * net.jafama.FastMath.PI / 180));
		jupiter.setPoint(HelperVector.rotate(jupiter.getPoint(), new Vector3d(
				0, 0, 1), 1.3 * net.jafama.FastMath.PI / 180));
		saturn.setPoint(HelperVector.rotate(saturn.getPoint(), new Vector3d(0,
				0, 1), 2.48 * net.jafama.FastMath.PI / 180));
		moon.orbitalCircularSpeed(earth, new Vector3d(0, 1, 0));
	}

	private void createPlanetaryRandom() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 1, 0), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 0.1, 1));
		Matter m1 = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(),
				net.jafama.FastMath.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), false);
		listMatter.add(m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();
		for (Matter m : listMatter) {
			if (m != m1) {
				m.orbitalCircularSpeed(m1, new Vector3d(0, 1, 0));
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
		Matter m1 = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(),
				net.jafama.FastMath.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), false);
		listMatter.add(m1);
		for (Matter m : listMatter) {
			if (m != m1) {
				m.orbitalCircularSpeed(m1, new Vector3d(0, 0, 1));
			}
		}
	}

	public Parameters getParameters() {
		return parameters;
	}

	public List<Matter> getListMatter() {
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

	public Matter getMaxMassElement() {
		return maxMassElement;
	}

	public double getVisibleMass() {
		return visibleMass;
	}

	public double getDarkMass() {
		return darkMass;
	}

	public double getVolumicMass() {
		return volumicMass;
	}
}
