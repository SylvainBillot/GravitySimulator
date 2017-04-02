package com.sylvanoid.joblib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
	private Vector3d gPointBefore = new Vector3d(0, 0, 0);

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d speed = new Vector3d(0, 0, 0);

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement

	private Vector3d min = new Vector3d(0, 0, 0);
	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d max = new Vector3d(0, 0, 0);

	@XmlElement
	private double k = 0;

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	private Vector3d p = new Vector3d(0, 0, 0);

	@XmlTransient
	private Matter maxMassElement = null;

	@XmlTransient
	private Univers father;

	@XmlTransient
	private ConcurrentHashMap<String, MatterPair> collisionPairs = new ConcurrentHashMap<String, MatterPair>();

	@XmlTransient
	private List<Matter> MatterMatterList = new ArrayList<Matter>();

	@XmlTransient
	private boolean virtual = false;

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
			createRandomStaticUnivers();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomRotateUnivers) {
			createRandomRotateUnivers();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.Planetary) {
			createPlanetary();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.PlanetaryRandom) {
			createPlanetaryRandom();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.PlanetariesGenesis) {
			createPlanetaryGenesisRandom();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomRotateUniverCircular) {
			createRandomRotateUniversCircular();
		}
		if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomExpensionUnivers) {
			createCubicUnivers();
		}
		computeMassLimitsCentroidSpeed(true);
	}

	public Univers(Univers father, Vector3d min, Vector3d max, boolean virtual) {
		this.father = father;
		this.parameters = father.parameters;
		this.listMatter = new ArrayList<Matter>();
		this.collisionPairs = father.getCollisionPairs();
		this.min = new Vector3d(min);
		this.max = new Vector3d(max);
		this.virtual = virtual;
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
		long startTimeCycle = System.currentTimeMillis();
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
		List<Matter> nanToRemove = new ArrayList<Matter>();
		for (Matter m : listMatter) {
			if (Double.isNaN(m.getPoint().getX()) || Double.isNaN(m.getPoint().getY())
					|| Double.isNaN(m.getPoint().getZ()) || Double.isNaN(m.getSpeed().getX())
					|| Double.isNaN(m.getSpeed().getY()) || Double.isNaN(m.getSpeed().getZ())) {
				nanToRemove.add(m);
			}
			mass += m.getMass();
			if (m.isDark()) {
				darkMass += m.getMass();
			} else {
				visibleMass += m.getMass();
			}
			if (withLimit && !nanToRemove.contains(m)) {
				if (!firstTime) {
					min.x = min.x > m.getPoint().getX() ? m.getPoint().getX() : min.x;
					max.x = max.x < m.getPoint().getX() ? m.getPoint().getX() : max.x;
					min.y = min.y > m.getPoint().getX() ? m.getPoint().getX() : min.y;
					max.y = max.y < m.getPoint().getX() ? m.getPoint().getX() : max.y;
					min.z = min.z > m.getPoint().getX() ? m.getPoint().getX() : min.z;
					max.z = max.z < m.getPoint().getX() ? m.getPoint().getX() : max.z;
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
			if (!nanToRemove.contains(m)) {
				tmpGx += (m.getPoint().getX() * m.getMass());
				tmpGy += (m.getPoint().getY() * m.getMass());
				tmpGz += (m.getPoint().getZ() * m.getMass());
				speed.add(m.getSpeed());
				k += m.getK();
				p.add(m.getP());
			}
		}
		listMatter.removeAll(nanToRemove);
		volumicMass = visibleMass / ((max.x - min.x) * (max.y - min.y) * (max.z - min.z));
		density = listMatter.size() / ((max.x - min.x) * (max.y - min.y) * (max.z - min.z));
		gPointBefore = new Vector3d(gPoint);
		gPoint = new Vector3d(tmpGx / mass, tmpGy / mass, tmpGz / mass);

		parameters
				.setLimitComputeTime(parameters.getLimitComputeTime() + (System.currentTimeMillis() - startTimeCycle));
	}

	@SuppressWarnings("unchecked")
	public void process(BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
		if (!parameters.isFrozen()) {
			if (!parameters.isPlayData()) {
				parameters.setLimitComputeTime(0);
				parameters.setNumOfCompute(0);
				parameters.setNumOfAccelCompute(0);
				long startTimeCycle = System.currentTimeMillis();
				computeMassLimitsCentroidSpeed(true);
				parameters.setLimitComputeTime(System.currentTimeMillis() - startTimeCycle);
				long startTimeBH = System.currentTimeMillis();

				/*
				 * Needed to disable acceleration with collision particle
				 */
				if (parameters.isManageImpact() && parameters.getTypeOfImpact() != TypeOfImpact.Viscosity) {
					computeBarnesHutCollision();
				}

				// Compute accelerations
				if (parameters.isBarnesHut()) {
					computeBarnesHutGravity();
				} else {
					computeNNGravity();
				}

				// Change Speed
				changeSpeed();

				if (parameters.isManageImpact()) {
					computeBarnesHutCollision();
					speedsAfterImpact(parameters.getTypeOfImpact());
				}

				move();

				if (parameters.isExpansionUnivers()) {
					expansionUnivers();
				}

				if (parameters.isManageImpact() && parameters.getTypeOfImpact() == TypeOfImpact.Viscosity) {
					computeBarnesHutCollision();
					doubleDensityRelaxation();
				}

				parameters.setTimeFactor(parameters.getTimeFactor() * parameters.getTimeMultiplicator());

				parameters.setBarnesHuttComputeTime(System.currentTimeMillis() - startTimeBH);
				moveEnd(bufferedWriter);
				parameters.setCycleComputeTime(System.currentTimeMillis() - startTimeCycle);
				parameters.setKlength(k);
				parameters.setPlength(p.length());
			} else {
				parameters.setNumOfCompute(-9999);
				parameters.setNumOfAccelCompute(-9999);
				long startTimeCycle = System.currentTimeMillis();
				try {
					String s = bufferedReader.readLine();
					if (s != null) {
						listMatter = new ArrayList<Matter>((List<Matter>) HelperTools.fromString(s));
					} else {
						parameters.setPlayData(false);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				computeMassLimitsCentroidSpeed(true);
				parameters.setCycleComputeTime(System.currentTimeMillis() - startTimeCycle);
			}
			parameters.setElapsedTime(parameters.getElapsedTime() + parameters.getTimeFactor());
		}
	}

	public boolean sameCoordonate() {
		for (Matter m : listMatter) {
			for (Matter m1 : listMatter) {
				if (m.getPoint().x != m1.getPoint().x || m.getPoint().y != m1.getPoint().y
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

	public void resetGpoint() {
		computeMassLimitsCentroidSpeed(false);
		gPointBefore = new Vector3d(gPoint);
	}

	private void changeSpeed() {
		for (Matter m : listMatter) {
			if (m.getTypeOfObject() != TypeOfObject.Virtual) {
				m.changeSpeed();
			}
		}
	}

	private void move() {
		long startTimeMove = System.currentTimeMillis();

		// Correction of gPoint derive because barnesHut is not perfect
		Vector3d gSpeed = new Vector3d();
		if (parameters.getTypeOfUnivers() != TypeOfUnivers.RandomExpensionUnivers) {
			Vector3d gDelta = new Vector3d(gPoint);
			gDelta.sub(gPointBefore);
			gSpeed = new Vector3d(gDelta.x / parameters.getTimeFactor(), gDelta.y / parameters.getTimeFactor(),
					gDelta.z / parameters.getTimeFactor());
		}

		for (Matter m : listMatter) {
			if (maxMassElement == null || maxMassElement.getMass() < m.getMass()) {
				maxMassElement = m;
			}
			m.move();
			m.getSpeed().sub(gSpeed);
			
			if (parameters.getTypeOfUnivers() == TypeOfUnivers.RandomExpensionUnivers
					&& m.getTypeOfObject() != TypeOfObject.Virtual) {

				double x = m.getPoint().getX();
				double y = m.getPoint().getY();
				double z = m.getPoint().getZ();

				while (x > parameters.getNebulaRadius()) {
					x -= parameters.getNebulaRadius() * 2;
				}
				while (x < -parameters.getNebulaRadius()) {
					x += parameters.getNebulaRadius() * 2;
				}

				while (y > parameters.getNebulaRadius()) {
					y -= parameters.getNebulaRadius() * 2;
				}
				while (y < -parameters.getNebulaRadius()) {
					y += parameters.getNebulaRadius() * 2;
				}

				while (z > parameters.getNebulaRadius()) {
					z -= parameters.getNebulaRadius() * 2;
				}
				while (z < -parameters.getNebulaRadius()) {
					z += parameters.getNebulaRadius() * 2;
				}

				m.setPoint(new Vector3d(x, y, z));
			}
			/**/
		}
		parameters.setMoveComputeTime(System.currentTimeMillis() - startTimeMove);
	}

	private int computeBarnesHutCollision() {
		for (Matter m : listMatter) {
			m.getFusionWith().clear();
		}
		collisionPairs.clear();
		BarnesHutCollision barnesHutCollision = new BarnesHutCollision(this);
		if (parameters.isParallelization()) {
			ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
			return (int) pool.invoke(barnesHutCollision);
		} else {
			return (int) barnesHutCollision.compute();
		}
	}

	private void computeBarnesHutGravity() {
		BarnesHutGravity barnesHutGravity = new BarnesHutGravity(this);
		if (parameters.isParallelization()) {
			ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
			pool.invoke(barnesHutGravity);
		} else {
			barnesHutGravity.compute();
		}
	}

	private void computeNNGravity() {
		for (Matter m1 : listMatter) {
			for (Matter m2 : listMatter) {
				if (m1 != m2) {
					if (m1.getTypeOfObject() != TypeOfObject.Virtual
							&& (!parameters.isStaticDarkMatter() || !m1.isDark())) {
						parameters.setNumOfAccelCompute(parameters.getNumOfAccelCompute() + 1);
						double attraction = HelperNewton.attraction(m1, m2, parameters);
						m1.getAccel().add(HelperVector.acceleration(m1.getPoint(), m2.getPoint(), attraction));
					}
				}
			}
		}
	}

	private void recusiveImpact() {
		// recursive impact
		for (Matter m : listMatter) {
			m.setFusionWith(Matter.fusionWithRecursiveAdd(m, m, new ArrayList<Matter>()));
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
			for (MatterPair mp : collisionPairs.values()) {
				mp.impact(0.0);
			}
			break;
		case HardImpact:
			for (MatterPair mp : collisionPairs.values()) {
				mp.impact(1.0);
			}
			break;
		case Viscosity:
			for (MatterPair mp : collisionPairs.values()) {
				mp.applyViscosity();
			}
			changeSpeed();
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
				bufferedWriter.write(HelperTools.toString((Serializable) listMatter));
				bufferedWriter.newLine();
			} catch (IOException e) {
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

	private void doubleDensityRelaxation() {
		double k = 0;
		double kn = 0;
		double p0 = 0;
		for (Matter m : listMatter) {
			double p = 0;
			double pn = 0;
			double P = 0;
			double Pn = 0;
			double pre = 0;
			if ((!parameters.isStaticDarkMatter() || m.getTypeOfObject() != TypeOfObject.Dark)
					&& m.getFusionWith().size() > 0) {
				// compute density and near-density
				for (Matter m1 : m.getFusionWith()) {
					Vector3d relativeSpeed = new Vector3d(m.getSpeed());
					relativeSpeed.sub(m1.getSpeed());
					Vector3d radialSpeed = new Vector3d(m1.getPoint());
					radialSpeed.sub(m.getPoint());
					radialSpeed.normalize();
					double u = relativeSpeed.dot(radialSpeed);
					if (u > 0) {
						double q = HelperNewton.distance(m, m1)
								/ (parameters.getCollisionDistanceRatio() * (m.getRayon() + m1.getRayon()));
						p += net.jafama.FastMath.pow2(1 - q);
						pn += net.jafama.FastMath.pow3(1 - q);
					} else {
						// ???
					}
				}
				// compute pressure and near-pressure
				k = m.globalViscoElasticity();
				kn = m.globalViscoElasticityNear();
				p0 = m.globalPressureZero();
				P = k * (p - p0);
				Pn = kn * pn;
				Vector3d dm = new Vector3d(0, 0, 0);
				m.setPresure(0); // try
				for (Matter m1 : m.getFusionWith()) {
					Vector3d relativeSpeed = new Vector3d(m.getSpeed());
					relativeSpeed.sub(m1.getSpeed());
					Vector3d radialSpeed = new Vector3d(m1.getPoint());
					radialSpeed.sub(m.getPoint());
					radialSpeed.normalize();
					double u = relativeSpeed.dot(radialSpeed);
					if (u > 0) {
						double q = HelperNewton.distance(m, m1)
								/ (parameters.getCollisionDistanceRatio() * (m.getRayon() + m1.getRayon()));
						Vector3d rij = new Vector3d(m1.getPoint());
						rij.sub(m.getPoint());
						rij.normalize();

						Vector3d rijm1 = new Vector3d(rij);
						Vector3d rijm2 = new Vector3d(rij);
						pre = (P * (1 - q) + Pn * net.jafama.FastMath.pow2(1 - q));
						double delta = parameters.getTimeFactor() * pre;
						rijm1.scale(m1.getMass() * delta / (m.getMass() + m1.getMass()));
						rijm2.scale(m.getMass() * delta / (m.getMass() + m1.getMass()));

						m1.getSpeed().add(rijm2);
						dm.add(rijm1);

						/* Try to get pseudo presure */
						m.setPresure(m.getPresure() + pre);
					}

				}
				m.getSpeed().sub(dm);
			}
		}
	}

	private void expansionUnivers() {
		parameters.setNebulaRadius(parameters.getNebulaRadius()
				+ parameters.getNebulaRadius() * HelperVariable.H0ms * parameters.getTimeFactor());
		for (Matter m : listMatter) {
			if (m.getTypeOfObject() != TypeOfObject.Virtual) {
				m.expansionUnivers();
			}

		}
	}

	/**
	 *
	 * @param origine
	 * @param initialSpeed
	 * @param axisOfRing
	 * @param radiusMin
	 * @param radiusMax
	 * @param ratio
	 * @param homogeneousDistributionPow
	 * @param gasHomogeneousDistributionPow
	 * @return
	 */
	private List<Matter> createUnivers(boolean spherical, Vector3d origine, Vector3d initialSpeed, Vector3d axisOfRing,
			double radiusMin, double radiusMax, Vector3d ratio, double homogeneousDistributionPow,
			double gasHomogeneousDistributionPow, double matterViscosity, double gasViscosity,
			double initialViscoElasticity, double initialViscoElasticityNear, double initialPressureZero) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		miniListMatter.addAll(createUniversMain(spherical, origine, initialSpeed, axisOfRing, radiusMin, radiusMax,
				ratio, parameters.getNumberOfObjects(), parameters.getMassObjectMin(), parameters.getMassObjectMax(),
				parameters.getDensity(), new Vector3d(0, 0, 0), homogeneousDistributionPow, TypeOfObject.Matter,
				matterViscosity, initialViscoElasticity, initialViscoElasticityNear, initialPressureZero));

		miniListMatter.addAll(createUniversMain(spherical, origine, initialSpeed, axisOfRing, radiusMin, radiusMax,
				ratio, parameters.getNumOfLowMassParticule(), 0, parameters.getLowMassParticuleMass(),
				parameters.getLowMassDensity(), new Vector3d(0.06, 0.05, 0.05), gasHomogeneousDistributionPow,
				TypeOfObject.Gas, gasViscosity, initialViscoElasticity, initialViscoElasticityNear,
				initialPressureZero));

		return miniListMatter;
	}

	/**
	 *
	 * @param origine
	 * @param initialSpeed
	 * @param axisOfRing
	 * @param radiusMin
	 * @param radiusMax
	 * @param ratio
	 * @param numberOfObjects
	 * @param minMass
	 * @param maxMass
	 * @param density
	 * @param defaultColor
	 * @param homogeneousDistributionPow
	 * @param typeOfObject
	 * @param initialViscosity
	 * @return
	 */
	private List<Matter> createUniversMain(boolean spherical, Vector3d origine, Vector3d initialSpeed,
			Vector3d axisOfRing, double radiusMin, double radiusMax, Vector3d ratio, int numberOfObjects,
			double minMass, double maxMass, double density, Vector3d defaultColor, double homogeneousDistributionPow,
			TypeOfObject typeOfObject, double initialViscosity, double initialViscoElasticity,
			double initialViscoElasticityNear, double initialPressureZero) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < numberOfObjects; cpt++) {
			Matter m;
			double x = 1;
			double y = 1;
			double z = 1;
			if (spherical) {
				boolean IsNotOK = true;
				while (IsNotOK) {

					double d = net.jafama.FastMath.pow(net.jafama.FastMath.random(), homogeneousDistributionPow);

					double r = radiusMin + (radiusMax - radiusMin) * net.jafama.FastMath.pow(d, 1d / 3d);

					double s = 2 * (net.jafama.FastMath.random() - 0.5);
					double alpha = 2 * net.jafama.FastMath.PI * (net.jafama.FastMath.random() - 0.5);
					double c = r * net.jafama.FastMath.sqrt(1 - net.jafama.FastMath.pow2(s));
					x = c * net.jafama.FastMath.cos(alpha);
					y = c * net.jafama.FastMath.sin(alpha);
					z = r * s;

					IsNotOK = false;
					if (axisOfRing.x != 0) {
						IsNotOK = IsNotOK || (net.jafama.FastMath.pow2(y)
								+ net.jafama.FastMath.pow2(z) < net.jafama.FastMath.pow2(radiusMin));
					}
					if (axisOfRing.y != 0) {
						IsNotOK = IsNotOK || (net.jafama.FastMath.pow2(x)
								+ net.jafama.FastMath.pow2(z) < net.jafama.FastMath.pow2(radiusMin));
					}
					if (axisOfRing.z != 0) {
						IsNotOK = IsNotOK || (net.jafama.FastMath.pow2(x)
								+ net.jafama.FastMath.pow2(y) < net.jafama.FastMath.pow2(radiusMin));
					}
				}
			} else { // Is Cubic
				/*
				 * double c = net.jafama.FastMath.pow(numberOfObjects, 1.0 /
				 * 3.0);
				 * 
				 * x = (int) (cpt % c); y = ((int) (cpt / c) % c); z = ((int)
				 * (cpt / (c * c)) % c);
				 * 
				 * // double dist = radiusMax * 2.0 / (c - 1); double dist =
				 * radiusMax * 2.0 / c; x = x * dist - radiusMax + dist / 2; y =
				 * y * dist - radiusMax + dist / 2; z = z * dist - radiusMax +
				 * dist / 2;
				 */

				x = radiusMin + (radiusMax - radiusMin) * 2 * (net.jafama.FastMath.random() - 0.5);
				y = radiusMin + (radiusMax - radiusMin) * 2 * (net.jafama.FastMath.random() - 0.5);
				z = radiusMin + (radiusMax - radiusMin) * 2 * (net.jafama.FastMath.random() - 0.5);

			}

			Vector3d color = defaultColor;
			if (defaultColor.equals(new Vector3d(0, 0, 0))) {
				double alea = net.jafama.FastMath.random();
				color = new Vector3d(0.45 + net.jafama.FastMath.random() * 0.05,
						0.45 + net.jafama.FastMath.random() * 0.05, 0.45 + net.jafama.FastMath.random() * 0.05);
				if (alea > 0.80) {
					color.set(new Vector3d(0.55 + net.jafama.FastMath.random() * 0.05,
							0.45 + net.jafama.FastMath.random() * 0.05, 0.45 + net.jafama.FastMath.random() * 0.05));
				}
				if (alea > 0.90) {
					color.set(new Vector3d(0.45 + net.jafama.FastMath.random() * 0.05,
							0.45 + net.jafama.FastMath.random() * 0.05, 0.55 + net.jafama.FastMath.random() * 0.05));
				}
			}
			m = new Matter(parameters,
					new Vector3d(origine.x + x * ratio.x, origine.y + y * ratio.y, origine.z + z * ratio.z),
					minMass + net.jafama.FastMath.random() * (maxMass - minMass)
							+ 1E-100 * net.jafama.FastMath.random(),
					new Vector3d(0, 0, 0), color, density, typeOfObject, initialViscosity, initialViscoElasticity,
					initialViscoElasticityNear, initialPressureZero);
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

	private void createRandomStaticUnivers() {
		createUnivers(true, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), 0,
				parameters.getNebulaRadius(), new Vector3d(1, 1, 1), parameters.getMatterDistribution(),
				parameters.getGasDistribution(), parameters.getMatterViscosity(), parameters.getGasViscosity(),
				parameters.getViscoElasticity(), parameters.getViscoElasticityNear(), parameters.getPressureZero());
	}

	private void createRandomRotateUnivers() {
		createUnivers(true, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01, parameters.getNebulaRadius(), new Vector3d(1, 1, 0.25),
				parameters.getMatterDistribution(), parameters.getGasDistribution(), parameters.getMatterViscosity(),
				parameters.getGasViscosity(), parameters.getViscoElasticity(), parameters.getViscoElasticityNear(),
				parameters.getPressureZero());

		createUniversMain(true, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01, parameters.getNebulaRadius(), parameters.getDarkMatterXYZRatio(),
				parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule(),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule()),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule()),
				parameters.getDarkMatterDensity(), new Vector3d(0.01, 0.01, 0.01),
				parameters.getDarkMatterDistribution(), TypeOfObject.Dark, parameters.getDarkMatterViscosity(),
				parameters.getViscoElasticity(), parameters.getViscoElasticityNear(), parameters.getPressureZero());

		TreeMap<Double, Double> innerMassTreeMap = new TreeMap<Double, Double>();
		for (Matter m : listMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(gPoint));
			if (innerMassTreeMap.get(distance) == null) {
				innerMassTreeMap.put(distance, m.getMass());
			} else {
				innerMassTreeMap.put(distance, m.getMass() + innerMassTreeMap.get(distance));
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
				double distance = new Point3d(m.getPoint()).distance(new Point3d(gPoint));
				m.orbitalEllipticSpeed(innerMassTreeMapCumul.get(distance), this.getGPoint(), new Vector3d(0, 0, 1),
						parameters.getNbARms());
			}
		}
	}

	private void createRandomRotateUniversCircular() {
		createRandomRotateUniversCircular(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1), new Vector3d(1, 1, 0.25), 0.01,
				0.01);
	}

	private void createRandomRotateUniversCircular(Vector3d origin, Vector3d axisOfRing, Vector3d ratio,
			double nebulaRadiusminRatioMatter, double nebulaRadiusminRatioDarkMatter) {
		List<Matter> miniListMatter = new ArrayList<Matter>();
		miniListMatter.addAll(createUnivers(true, origin, new Vector3d(0, 0, 0), axisOfRing,
				parameters.getNebulaRadius() * nebulaRadiusminRatioMatter, parameters.getNebulaRadius(), ratio,
				parameters.getMatterDistribution(), parameters.getGasDistribution(), parameters.getMatterViscosity(),
				parameters.getGasViscosity(), parameters.getViscoElasticity(), parameters.getViscoElasticityNear(),
				parameters.getPressureZero()));

		miniListMatter.addAll(createUniversMain(true, origin, new Vector3d(0, 0, 0), axisOfRing,
				parameters.getNebulaRadius() * nebulaRadiusminRatioDarkMatter, parameters.getNebulaRadius(),
				parameters.getDarkMatterXYZRatio(),
				parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule(),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule()),
				parameters.getDarkMatterMass()
						/ (parameters.getNumberOfObjects() + parameters.getNumOfLowMassParticule()),
				parameters.getDarkMatterDensity(), new Vector3d(0.01, 0.01, 0.01),
				parameters.getDarkMatterDistribution(), TypeOfObject.Dark, parameters.getDarkMatterViscosity(),
				parameters.getViscoElasticity(), parameters.getViscoElasticityNear(), parameters.getPressureZero()));

		TreeMap<Double, Double> innerMassTreeMap = new TreeMap<Double, Double>();
		for (Matter m : miniListMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(origin));
			if (innerMassTreeMap.get(distance) == null) {
				innerMassTreeMap.put(distance, m.getMass());
			} else {
				innerMassTreeMap.put(distance, m.getMass() + innerMassTreeMap.get(distance));
			}
		}

		TreeMap<Double, Double> innerMassTreeMapCumul = new TreeMap<Double, Double>();
		double innerMass = 0;
		for (Map.Entry<Double, Double> entry : innerMassTreeMap.entrySet()) {
			innerMass += entry.getValue();
			innerMassTreeMapCumul.put(entry.getKey(), innerMass);
		}

		for (Matter m : miniListMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(origin));
			m.orbitalCircularSpeed(origin, distance, innerMassTreeMapCumul.get(distance), axisOfRing);
		}
	}

	private void createCubicUnivers() {
		createUnivers(false, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 1),
				parameters.getNebulaRadius() * 0.01, parameters.getNebulaRadius()*0.2, new Vector3d(1, 1, 1),
				parameters.getMatterDistribution(), parameters.getGasDistribution(), parameters.getMatterViscosity(),
				parameters.getGasViscosity(), parameters.getViscoElasticity(), parameters.getViscoElasticityNear(),
				parameters.getPressureZero());

		/* Inition explosion */
		for (Matter m : listMatter) {
			m.getSpeed().add(HelperVector.acceleration(m.getPoint(), new Vector3d(), -HelperVariable.H0ms * m.getPoint().length()*10));
		}
		
		double coef = 2;
		MatterMatterList = new ArrayList<Matter>();
		for (Matter m : listMatter) {
			MatterMatterList.add(new Matter(parameters,
					new Vector3d(m.getPoint().getX() - parameters.getNebulaRadius() * coef, m.getPoint().getY(),
							m.getPoint().getZ()),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "W"));
			MatterMatterList.add(new Matter(parameters,
					new Vector3d(+m.getPoint().getX() + parameters.getNebulaRadius() * coef, +m.getPoint().getY(),
							+m.getPoint().getZ()),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "E"));

			MatterMatterList.add(new Matter(parameters,
					new Vector3d(m.getPoint().getX(), m.getPoint().getY() + parameters.getNebulaRadius() * coef,
							m.getPoint().getZ()),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "T"));
			MatterMatterList.add(new Matter(parameters,
					new Vector3d(m.getPoint().getX(), m.getPoint().getY() - parameters.getNebulaRadius() * coef,
							m.getPoint().getZ()),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "B"));

			MatterMatterList.add(new Matter(parameters,
					new Vector3d(m.getPoint().getX(), m.getPoint().getY(),
							m.getPoint().getZ() + parameters.getNebulaRadius() * coef),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "S"));
			MatterMatterList.add(new Matter(parameters,
					new Vector3d(m.getPoint().getX(), m.getPoint().getY(),
							m.getPoint().getZ() - parameters.getNebulaRadius() * coef),
					m.getMass(), new Vector3d(m.getSpeed()), m.getColor(), m.getDensity(), TypeOfObject.Virtual,
					m.getDensity(), m.getViscoElasticity(), m.getViscoElasticityNear(), m.getPresure(), m, "N"));
		}
		listMatter.addAll(MatterMatterList);
	}

	private void createPlanetary() {
		Matter sun = new Matter(parameters,
				new Vector3d(net.jafama.FastMath.random(), net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				HelperVariable.M + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 1, 0.5), 1408,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		sun.setName("Sun");
		listMatter.add(sun);

		Matter mercure = new Matter(parameters,
				new Vector3d(0.38709893 * HelperVariable.UA + net.jafama.FastMath.random(),
						net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				330.2E21 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 5427,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		mercure.setName("Mercure");
		listMatter.add(mercure);

		Matter venus = new Matter(parameters,
				new Vector3d(0.723332 * HelperVariable.UA + net.jafama.FastMath.random(), net.jafama.FastMath.random(),
						net.jafama.FastMath.random()),
				5.972E24 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 1, 0.8), 5.204E3,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		venus.setName("Venus");
		listMatter.add(venus);

		Matter earth = new Matter(parameters,
				new Vector3d(1 * HelperVariable.UA + net.jafama.FastMath.random(), net.jafama.FastMath.random(),
						net.jafama.FastMath.random()),
				4.8685E24 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(0.7, 0.7, 1), 5.52E3,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		earth.setName("Earth");
		listMatter.add(earth);

		Matter mars = new Matter(parameters,
				new Vector3d(1.52366231 * HelperVariable.UA + net.jafama.FastMath.random(),
						net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				641.85E21 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 0.7, 0.7), 3933.5,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		mars.setName("Mars");
		listMatter.add(mars);

		Matter jupiter = new Matter(parameters,
				new Vector3d(5.20336301 * HelperVariable.UA + net.jafama.FastMath.random(),
						net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				1.8986E27 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 0.8, 0.8), 1326,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		jupiter.setName("Jupiter");
		listMatter.add(jupiter);

		Matter saturn = new Matter(parameters,
				new Vector3d(9.53707032 * HelperVariable.UA + net.jafama.FastMath.random(),
						net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				568.46E24 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(0.9, 0.9, 0.9), 687.3,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		saturn.setName("Saturn");
		listMatter.add(saturn);

		Matter moon = new Matter(parameters,
				new Vector3d((1 + 0.00257) * HelperVariable.UA + net.jafama.FastMath.random(),
						net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				7.3477E22 + net.jafama.FastMath.random(), new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), 3.3464E3,
				TypeOfObject.Matter, parameters.getMatterViscosity(), parameters.getViscoElasticity(),
				parameters.getViscoElasticityNear(), parameters.getPressureZero());
		moon.setName("Moon");
		listMatter.add(moon);

		for (Matter m : listMatter) {
			if (m != sun) {
				m.orbitalCircularSpeed(sun, new Vector3d(0, 1, 0));
			}
		}

		mercure.setPoint(
				HelperVector.rotate(mercure.getPoint(), new Vector3d(0, 0, 1), 7 * net.jafama.FastMath.PI / 180));
		venus.setPoint(HelperVector.rotate(venus.getPoint(), new Vector3d(0, 0, 1), 3 * net.jafama.FastMath.PI / 180));
		mars.setPoint(HelperVector.rotate(mars.getPoint(), new Vector3d(0, 0, 1), 1.8 * net.jafama.FastMath.PI / 180));
		jupiter.setPoint(
				HelperVector.rotate(jupiter.getPoint(), new Vector3d(0, 0, 1), 1.3 * net.jafama.FastMath.PI / 180));
		saturn.setPoint(
				HelperVector.rotate(saturn.getPoint(), new Vector3d(0, 0, 1), 2.48 * net.jafama.FastMath.PI / 180));
		moon.orbitalCircularSpeed(earth, new Vector3d(0, 1, 0));
	}

	private void createPlanetaryRandom() {
		createPlanetaryRandom(parameters.getMatterRendererExtender(), new Vector3d(1, 1, 0.25));
	}

	private void createPlanetaryGenesisRandom() {
		createPlanetaryRandom(parameters.getMatterRendererExtender() * 10, new Vector3d(1, 1, 0.05));
	}

	private void createPlanetaryRandom(double minimalDistanceCentralStarRadiusRatio, Vector3d ratioxyz) {
		Matter m1 = new Matter(parameters,
				new Vector3d(net.jafama.FastMath.random(), net.jafama.FastMath.random(), net.jafama.FastMath.random()),
				parameters.getDarkMatterMass(), new Vector3d(0, 0, 0), new Vector3d(1, 1, 1),
				parameters.getDarkMatterDensity(), TypeOfObject.Matter, parameters.getMatterViscosity(),
				parameters.getViscoElasticity(), parameters.getViscoElasticityNear(), parameters.getPressureZero());
		createUnivers(true, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(0, 0, 1),
				m1.getRayon() * minimalDistanceCentralStarRadiusRatio, parameters.getNebulaRadius(), ratioxyz,
				parameters.getMatterDistribution(), parameters.getGasDistribution(), parameters.getMatterViscosity(),
				parameters.getGasViscosity(), parameters.getViscoElasticity(), parameters.getViscoElasticityNear(),
				parameters.getPressureZero());
		listMatter.add(m1);
		mass += m1.getMass();
		visibleMass += m1.getMass();

		resetGpoint();
		TreeMap<Double, Double> innerMassTreeMap = new TreeMap<Double, Double>();
		for (Matter m : listMatter) {
			double distance = new Point3d(m.getPoint()).distance(new Point3d(getGPoint()));
			if (innerMassTreeMap.get(distance) == null) {
				innerMassTreeMap.put(distance, m.getMass());
			} else {
				innerMassTreeMap.put(distance, m.getMass() + innerMassTreeMap.get(distance));
			}
		}

		TreeMap<Double, Double> innerMassTreeMapCumul = new TreeMap<Double, Double>();
		double innerMass = 0;
		for (Map.Entry<Double, Double> entry : innerMassTreeMap.entrySet()) {
			innerMass += entry.getValue();
			innerMassTreeMapCumul.put(entry.getKey(), innerMass);
		}

		for (Matter m : listMatter) {
			if (m != m1) {
				double distance = new Point3d(m.getPoint()).distance(new Point3d(getGPoint()));
				m.orbitalCircularSpeed(getGPoint(), distance, innerMassTreeMapCumul.get(distance),
						new Vector3d(0, 0, 1));
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

	public Vector3d getGPointBefore() {
		return gPointBefore;
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

	public ConcurrentHashMap<String, MatterPair> getCollisionPairs() {
		return collisionPairs;
	}

	public void setCollisionPairs(ConcurrentHashMap<String, MatterPair> collisionPairs) {
		this.collisionPairs = collisionPairs;
	}

	/**
	 * @return the virtual
	 */
	public boolean isVirtual() {
		return virtual;
	}

	/**
	 * @param virtual
	 *            the virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

}
