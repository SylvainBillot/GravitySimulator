package com.sylvanoid.joblib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ForkJoinPool;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperNewton;
import com.sylvanoid.common.HelperTools;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfImpact;
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
	private double density;
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
	private double k = 0;
	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d p = new Vector3d(0, 0, 0);

	private Matter maxMassElement = null;

	private Univers father;

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
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomRotateUniverCircular) {
			createRandomRotateUniversCircular();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomStaticSphericalUnivers) {
			createRandomStaticSphericalUnivers();
		}
		computeMassLimitsCentroidSpeed(true);
	}

	public Univers(Univers father, Vector3d min, Vector3d max) {
		this.father = father;
		this.parameters = father.parameters;
		this.listMatter = new ArrayList<Matter>();
		this.min = new Vector3d(min);
		this.max = new Vector3d(max);
	}

	public Univers(Univers father) {
		this.father = father;
		this.parameters = father.parameters;
		this.listMatter = new ArrayList<Matter>(father.getListMatter());
		this.mass = father.mass;
		this.gPoint = new Vector3d(father.gPoint);
	}

	public Vector3d gravityAtThisPoint(Vector3d p) {
		Vector3d accel = new Vector3d();
		for (Matter m : listMatter) {
			double attraction = HelperNewton.attraction(p, m, parameters);
			accel.add(HelperVector.acceleration(p, m.getPoint(), attraction));
		}
		return accel;
	}

	public int adjustMassAndCentroid(List<Matter> minus) {
		double tmpGx = gPoint.x * mass;
		double tmpGy = gPoint.y * mass;
		double tmpGz = gPoint.z * mass;
		int cpt = 0;
		for (Matter m : minus) {
			if (listMatter.contains(m)) {
				cpt++;
				mass -= m.getMass();
				tmpGx -= (m.getPoint().getX() * m.getMass());
				tmpGy -= (m.getPoint().getY() * m.getMass());
				tmpGz -= (m.getPoint().getZ() * m.getMass());
			}
		}
		gPoint = new Vector3d(tmpGx / mass, tmpGy / mass, tmpGz / mass);
		return listMatter.size() - cpt;
	}

	public void computeMassLimitsCentroidSpeed(boolean withLimit) {
		boolean firstTime = true;
		volumicMass = 0;
		density = 0;
		speed = new Vector3d(0, 0, 0);
		k = 0;
		p = new Vector3d(0, 0, 0);
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
			k += m.getK();
			p.add(m.getP());
		}
		volumicMass = visibleMass
				/ ((max.x - min.x) * (max.y - min.y) * (max.z - min.z));
		density = listMatter.size()
				/ ((max.x - min.x) * (max.y - min.y) * (max.z - min.z));
		gPoint = new Vector3d(tmpGx / mass, tmpGy / mass, tmpGz / mass);
	}

	@SuppressWarnings("unchecked")
	public void process(BufferedWriter bufferedWriter,
			BufferedReader bufferedReader) {
		if (!parameters.isPlayData()) {
			parameters.setNumOfCompute(0);
			parameters.setNumOfAccelCompute(0);
			long startTimeCycle = System.currentTimeMillis();
			computeMassLimitsCentroidSpeed(true);
			parameters.setLimitComputeTime(System.currentTimeMillis()
					- startTimeCycle);
			long startTimeBH = System.currentTimeMillis();

			/*
			 * Needed to disable acceleration with collision particle
			 */
			if (parameters.isManageImpact()) {
				computeBarnesHutCollision();
			}

			// Compute accelerations
			computeBarnesHutGravity();
			// Change Speed
			changeSpeed();

			if (parameters.isAppliViscosity()) {
				computeBarnesHutNeighbors();
				applyNeighborsFriction();
			}

			if (parameters.isManageImpact()) {
				computeBarnesHutCollision();
				speedsAfterImpact(parameters.getTypeOfImpact());
			}

			move();
			
			parameters.setBarnesHuttComputeTime(System.currentTimeMillis()
					- startTimeBH);
			moveEnd(bufferedWriter);
			parameters.setCycleComputeTime(System.currentTimeMillis()
					- startTimeCycle);
			parameters.setKlength(k);
			parameters.setPlength(p.length());
		} else {
			parameters.setNumOfCompute(-9999);
			parameters.setNumOfAccelCompute(-9999);
			long startTimeCycle = System.currentTimeMillis();
			try {
				String s = bufferedReader.readLine();
				if (s != null) {
					listMatter = new ArrayList<Matter>(
							(List<Matter>) HelperTools.fromString(s));
				} else {
					parameters.setPlayData(false);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

	public boolean containtSameTypeAs(Matter m) {
		boolean valReturn = false;
		for (Matter m1 : listMatter) {
			if (m1.getTypeOfObject() == m.getTypeOfObject()) {
				return true;
			}
		}
		return valReturn;
	}

	public List<Matter> listOfSameTypeAs(Matter m) {
		List<Matter> valReturn = new ArrayList<Matter>();
		for (Matter m1 : listMatter) {
			if (m1.getTypeOfObject() == m.getTypeOfObject()) {
				valReturn.add(m1);
			}
		}
		return valReturn;
	}

	private void changeSpeed() {
		for (Matter m : listMatter) {
			m.changeSpeed();
		}
	}

	private void move() {
		long startTimeMove = System.currentTimeMillis();
		for (Matter m : listMatter) {
			if (maxMassElement == null
					|| maxMassElement.getMass() < m.getMass()) {
				maxMassElement = m;
			}
			m.move();
		}
		parameters.setMoveComputeTime(System.currentTimeMillis()
				- startTimeMove);
	}

	private void computeBarnesHutNeighbors() {
		for (Matter m : listMatter) {
			m.getNeighbors().clear();
		}
		BarnesHutNeighbors barnesHutNeighbors = new BarnesHutNeighbors(this);
		if (parameters.isParallelization()) {
			ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime()
					.availableProcessors());
			pool.invoke(barnesHutNeighbors);
		} else {
			barnesHutNeighbors.compute();
		}
	}

	private int computeBarnesHutCollision() {
		for (Matter m : listMatter) {
			m.getFusionWith().clear();
		}
		BarnesHutCollision barnesHutCollision = new BarnesHutCollision(this);
		if (parameters.isParallelization()) {
			ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime()
					.availableProcessors());
			return (int) pool.invoke(barnesHutCollision);
		} else {
			return (int) barnesHutCollision.compute();
		}
	}

	private void computeBarnesHutGravity() {
		BarnesHutGravity barnesHutGravity = new BarnesHutGravity(this);
		if (parameters.isParallelization()) {
			ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime()
					.availableProcessors());
			pool.invoke(barnesHutGravity);
		} else {
			barnesHutGravity.compute();
		}
	}

	private void recusiveImpact() {
		// recursive impact
		for (Matter m : listMatter) {
			m.setFusionWith(Matter.fusionWithRecursiveAdd(m, m,
					new ArrayList<Matter>()));
		}
	}

	private void applyNeighborsFriction() {
		for (Matter m : listMatter) {
			if (m.getNeighbors().size() != 0) {
				m.neighborsFriction();
				// m.applyVicosityNeighbors();
			}
		}
		for (Matter m : listMatter) {
			if (m.getNeighbors().size() != 0) {
				m.changeSpeed();
			}
		}
	}

	private void speedsAfterImpact(TypeOfImpact typeOfImpact) {
		switch (typeOfImpact) {
		case Fusion:
			recusiveImpact();
			List<Matter> listMatterBis = new ArrayList<Matter>(listMatter);
			for (Matter m : listMatterBis) {
				if (m.getFusionWith().size() != 0) {
					m.fusion(listMatter);
				}
			}
			break;
		case SoftImpact:
			for (Matter m : listMatter) {
				if (m.getFusionWith().size() != 0) {
					m.softImpact();
				}
			}
			for (Matter m : listMatter) {
				if (m.getFusionWith().size() != 0) {
					m.setSpeed(m.getSpeedAdjusted());
				}
			}
			break;
		case HardImpact:
			for (Matter m : listMatter) {
				if (m.getFusionWith().size() != 0) {
					m.hardImpact();
				}
			}
			for (Matter m : listMatter) {
				if (m.getFusionWith().size() != 0) {
					m.setSpeed(m.getSpeedAdjusted());
				}
			}
			break;
		case Viscosity:
			for (Matter m : listMatter) {
				if (m.getFusionWith().size() != 0) {
					m.applyViscosity();
				}
				if (m.getFusionWith().size() != 0) {
					m.setSpeed(m.getSpeedAdjusted());
				}
			}
			break;
		case NoAcell:
			break;
		default:
		}
	}

	@SuppressWarnings("unused")
	private void adjustSpeedFromPositions() {
		for (Matter m : listMatter) {
			m.adjustSpeedFromPositions();
		}
	}

	private void moveEnd(BufferedWriter bufferedWriter) {
		if (parameters.isExportData()) {
			try {
				bufferedWriter.write(HelperTools
						.toString((Serializable) listMatter));
				bufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	private boolean haveImpact() {
		if (computeBarnesHutCollision() > 0) {
			return true;
		}
		return false;
	}

	private List<Matter> createUvivers(Vector3d origine, Vector3d initialSpeed,
			Vector3d axisOfRing, double radiusMin, double radiusMax,
			Vector3d ratio, double homogeneousDistributionPow,
			double gasHomogeneousDistributionPow) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		miniListMatter.addAll(createUniversMain(origine, initialSpeed,
				axisOfRing, radiusMin, radiusMax, ratio,
				parameters.getNumberOfObjects(), parameters.getMassObjectMin(),
				parameters.getMassObjectMax(), parameters.getDensity(),
				new Vector3d(0, 0, 0), homogeneousDistributionPow,
				TypeOfObject.Matter));

		miniListMatter.addAll(createUniversMain(origine, initialSpeed,
				axisOfRing, radiusMin, radiusMax, ratio,
				parameters.getNumOfLowMassParticule(), 0,
				parameters.getLowMassParticuleMass(),
				parameters.getLowMassDensity(), new Vector3d(0.06, 0.05, 0.05),
				gasHomogeneousDistributionPow, TypeOfObject.Gas));

		return miniListMatter;
	}

	private List<Matter> createUniversMain(Vector3d origine,
			Vector3d initialSpeed, Vector3d axisOfRing, double radiusMin,
			double radiusMax, Vector3d ratio, int numberOfObjects,
			double minMass, double maxMass, double density,
			Vector3d defaultColor, double homogeneousDistributionPow,
			TypeOfObject typeOfObject) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < numberOfObjects; cpt++) {
			Matter m;
			double x = 1;
			double y = 1;
			double z = 1;
			boolean IsNotOK = true;
			while (IsNotOK) {

				double d = net.jafama.FastMath.pow(
						net.jafama.FastMath.random(),
						homogeneousDistributionPow);

				double r = radiusMin + (radiusMax - radiusMin)
						* net.jafama.FastMath.pow(d, 1d / 3d);

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
					0), color, density, typeOfObject);
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
				new Vector3d(1, 1, 1), parameters.getMatterDistribution(),
				parameters.getGasDistribution());
	}

	private void createRandomRotateUnivers() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());

		createUniversMain(
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius()
						* parameters.getDarkMatterNubulaFactor(),
				new Vector3d(1, 1, 1),
				parameters.getNumberOfObjects()
						+ parameters.getNumOfLowMassParticule(),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterDensity(), new Vector3d(0.01, 0.01,
						0.01), parameters.getDarkMatterDistribution(),
				TypeOfObject.Dark);

		TreeMap<Double, Double> innerMassTreeMap = new TreeMap<Double, Double>();
		for (Matter m : listMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(
					gPoint));
			if (innerMassTreeMap.get(distance) == null) {
				innerMassTreeMap.put(distance, m.getMass());
			} else {
				innerMassTreeMap.put(distance,
						m.getMass() + innerMassTreeMap.get(distance));
			}
		}

		TreeMap<Double, Double> innerMassTreeMapCumul = new TreeMap<Double, Double>();
		double innerMass = 0;
		for (Map.Entry<Double, Double> entry : innerMassTreeMap.entrySet()) {
			innerMass += entry.getValue();
			innerMassTreeMapCumul.put(entry.getKey(), innerMass);
		}

		for (Matter m : listMatter) {
			if (!parameters.isStaticDarkMatter() || !m.isDark()) {
				double distance = new Point3d(m.getPoint())
						.distance(new Point3d(gPoint));
				m.orbitalEllipticSpeed(innerMassTreeMapCumul.get(distance),
						this.getGPoint(), new Vector3d(0, 0, 1),
						parameters.getNbARms());
			}
		}
		/*
		 * for (Matter m : listMatter) { if (m != m1) {
		 * m.orbitalEllipticSpeed(m1, new Vector3d(0, 0, 1),
		 * parameters.getNbARms()); } }
		 */
	}

	private void createRandomRotateUniversCircular() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());

		createUniversMain(
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius()
						* parameters.getDarkMatterNubulaFactor(),
				new Vector3d(1, 1, 1),
				parameters.getNumberOfObjects()
						+ parameters.getNumOfLowMassParticule(),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterDensity(), new Vector3d(0.01, 0.01,
						0.01), parameters.getDarkMatterDistribution(),
				TypeOfObject.Dark);

		TreeMap<Double, Double> innerMassTreeMap = new TreeMap<Double, Double>();
		for (Matter m : listMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(
					gPoint));
			if (innerMassTreeMap.get(distance) == null) {
				innerMassTreeMap.put(distance, m.getMass());
			} else {
				innerMassTreeMap.put(distance,
						m.getMass() + innerMassTreeMap.get(distance));
			}
		}

		TreeMap<Double, Double> innerMassTreeMapCumul = new TreeMap<Double, Double>();
		double innerMass = 0;
		for (Map.Entry<Double, Double> entry : innerMassTreeMap.entrySet()) {
			innerMass += entry.getValue();
			innerMassTreeMapCumul.put(entry.getKey(), innerMass);
		}

		for (Matter m : listMatter) {
			if (!parameters.isStaticDarkMatter() || !m.isDark()) {
				double distance = new Point3d(m.getPoint())
						.distance(new Point3d(gPoint));
				m.orbitalCircularSpeed(this, distance, innerMassTreeMapCumul
						.get(distance), new Vector3d(0, 0, 1));
			}
		}

	}

	private void createRandomStaticSphericalUnivers() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 1),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());

		createUniversMain(
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01,
				parameters.getNebulaRadius()
						* parameters.getDarkMatterNubulaFactor(),
				new Vector3d(1, 1, 1),
				parameters.getNumberOfObjects()
						+ parameters.getNumOfLowMassParticule(),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters
								.getNumOfLowMassParticule()),
				parameters.getDarkMatterDensity(), new Vector3d(0.01, 0.01,
						0.01), parameters.getDarkMatterDistribution(),
				TypeOfObject.Dark);
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
				parameters.getDarkMatterDensity(), TypeOfObject.Dark);
		listMatter.add(m1);
		mass += m1.getMass();
		darkMass += m1.getMass();

		Matter m2 = new Matter(parameters, dbg1, parameters.getDarkMatterMass()
				/ 2 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(0.25, 0.25, 0.25),
				parameters.getDarkMatterDensity(), TypeOfObject.Dark);
		listMatter.add(m2);
		mass += m2.getMass();
		darkMass += m2.getMass();

		m1.orbitalCircularSpeed(m2, new Vector3d(0, 1, 0));
		m2.orbitalCircularSpeed(m1, new Vector3d(0, 1, 0));

		List<Matter> subu01 = createUvivers(m1.getPoint(), m1.getSpeed(),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());

		List<Matter> subu02 = createUvivers(m2.getPoint(), m2.getSpeed(),
				new Vector3d(1, 0, 0), parameters.getNebulaRadius() * 0.1,
				parameters.getNebulaRadius(), new Vector3d(1, 0.25, 1),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());

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
				new Vector3d(1, 1, 0.5), 1408, TypeOfObject.Matter);
		sun.setName("Sun");
		listMatter.add(sun);

		Matter mercure = new Matter(parameters, new Vector3d(0.38709893
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				330.2E21 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 0.8, 0.8), 5427, TypeOfObject.Matter);
		mercure.setName("Mercure");
		listMatter.add(mercure);

		Matter venus = new Matter(parameters, new Vector3d(0.723332
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				5.972E24 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0),
				new Vector3d(1, 1, 0.8), 5.204E3, TypeOfObject.Matter);
		venus.setName("Venus");
		listMatter.add(venus);

		Matter earth = new Matter(parameters, new Vector3d(1
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				4.8685E24 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.7, 0.7, 1), 5.52E3,
				TypeOfObject.Matter);
		earth.setName("Earth");
		listMatter.add(earth);

		Matter mars = new Matter(parameters, new Vector3d(1.52366231
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				641.85E21 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.7, 0.7), 3933.5,
				TypeOfObject.Matter);
		mars.setName("Mars");
		listMatter.add(mars);

		Matter jupiter = new Matter(parameters, new Vector3d(5.20336301
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				1.8986E27 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 1326,
				TypeOfObject.Matter);
		jupiter.setName("Jupiter");
		listMatter.add(jupiter);

		Matter saturn = new Matter(parameters, new Vector3d(9.53707032
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				568.46E24 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(0.9, 0.9, 0.9), 687.3,
				TypeOfObject.Matter);
		saturn.setName("Saturn");
		listMatter.add(saturn);

		Matter moon = new Matter(parameters, new Vector3d((1 + 0.00257)
				* HelperVariable.UA + net.jafama.FastMath.random(),
				net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				7.3477E22 + net.jafama.FastMath.random(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), 3.3464E3,
				TypeOfObject.Matter);
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
				parameters.getNebulaRadius(), new Vector3d(1, 0.1, 1),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());
		Matter m1 = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(),
				net.jafama.FastMath.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), TypeOfObject.Matter);
		listMatter.add(m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();
		for (Matter m : listMatter) {
			if (m != m1) {
				m.orbitalCircularSpeed(m1, new Vector3d(0, 1, 0));
			}
		}
	}

	private void createPlanetariesGenesis() {
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() / 2 * 0.9,
				parameters.getNebulaRadius() / 2, new Vector3d(1, 1, 0.05),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());
		createUvivers(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0),
				new Vector3d(0, 0, 1), parameters.getNebulaRadius() * 0.9,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 0.05),
				parameters.getMatterDistribution(),
				parameters.getGasDistribution());
		Matter m1 = new Matter(parameters, new Vector3d(
				net.jafama.FastMath.random(), net.jafama.FastMath.random(),
				net.jafama.FastMath.random()), parameters.getDarkMatterMass(),
				new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), TypeOfObject.Matter);
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

	public double getDensity() {
		return density;
	}

	public Univers getFather() {
		return father;
	}

	public double getK() {
		return k;
	}

	public Vector3d getP() {
		return p;
	}

}
