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
import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement
public class Univers {
	@XmlElement
	private Parameters parameters;
	@XmlElement
	private double mass;
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
	}

	public Univers(Univers father, Vector3d min, Vector3d max) {
		this.parameters = father.parameters;
		this.min = min;
		this.max = max;
		listMatter = new TreeMap<Matter, Matter>();
		mass = 0;
		for (Matter m : father.getListMatiere().values()) {
			if (m.getPoint().getX() >= min.x && m.getPoint().getX() <= max.x
					&& m.getPoint().getY() >= min.y
					&& m.getPoint().getY() <= max.y
					&& m.getPoint().getZ() >= min.z
					&& m.getPoint().getZ() <= max.z) {
				listMatter.put(m, m);
				mass += m.getMass();
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
		for (Matter m : getListMatiere().values()) {
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
			for (Univers u : subUnivers) {
				u.compute();
				for (Univers uvoisin : subUnivers) {
					if (u != uvoisin && uvoisin.getListMatiere().size() > 0) {
						uvoisin.computeCentroidOfUnivers();
						TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
								new MassComparator());
						sortByMass.putAll(u.listMatter);
						for (Matter m : sortByMass.values()) {
							double distance = new Point3d(m.getPoint())
									.distance(new Point3d(uvoisin.getGPoint()));
							double attraction = parameters.getTimeFactor()
									* HelperVariable.GRAVITY
									* (((uvoisin.getMass()) / Math.pow(
											distance, 2)));
							double theta = Math
									.atan2(uvoisin.getGPoint().y
											- m.getPoint().getY(),
											uvoisin.getGPoint().x
													- m.getPoint().getX());
							double phi = Math.atan2(Math.pow(
									Math.pow(uvoisin.getGPoint().x
											- m.getPoint().getX(), 2)
											+ Math.pow(uvoisin.getGPoint().y
													- m.getPoint().getY(), 2),
									0.5), (uvoisin.getGPoint().z - m.getPoint()
									.getZ()));

							m.getA().add(
									new Vector3d(attraction * Math.cos(theta)
											* Math.sin(phi), attraction
											* Math.sin(theta) * Math.sin(phi),
											attraction * Math.cos(phi)));
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
				if (HelperVariable.fusion) {
					treatNeighborFusion(m1, sortX, treatedFusion, toTreat);
				} else {
					treatNeighborImpact(m1, sortX, toTreat);
				}
			} else {
				if (HelperVariable.fusion) {
					treatNeighborFusion(m1, sortXDark, treatedFusion, toTreat);
				} else {
					treatNeighborImpact(m1, sortXDark, toTreat);
				}
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
				m.getPoint().setZ(m.getPoint().getZ() - gPoint.z);
			}
		}

		// Centre l'ecran
		if (HelperVariable.centerOnScreen) {
			computeLimits();
			for (Matter m : listMatter.values()) {
				m.getPoint().setX(m.getPoint().getX() - (max.x + min.x) / 2);
				m.getPoint().setY(m.getPoint().getY() - (max.y + min.y) / 2);
				m.getPoint().setZ(m.getPoint().getZ() - (max.z + min.z) / 2);
			}
		}

		// Centre sur le plus massif
		if (HelperVariable.centerOnMassMax) {
			TreeMap<Matter, Matter> sortByMass = new TreeMap<Matter, Matter>(
					new MassComparator());
			sortByMass.putAll(listMatter);
			Matter maxMass = sortByMass.firstEntry().getValue();
			double cmaxX = maxMass.getPoint().getX();
			double cmaxY = maxMass.getPoint().getY();
			double cmaxZ = maxMass.getPoint().getZ();

			for (Matter m : sortByMass.values()) {
				m.getPoint().setX(m.getPoint().getX() - cmaxX);
				m.getPoint().setY(m.getPoint().getY() - cmaxY);
				m.getPoint().setZ(m.getPoint().getZ() - cmaxZ);
			}
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

	public TreeMap<Matter, Matter> getListMatiere() {
		return listMatter;
	}

	public Vector3d getGPoint() {
		return gPoint;
	}

	public double getMass() {
		return mass;
	}

	private TreeMap<Matter, Matter> createUvivers(Vector3d origine,
			double delta, double speedTrans, double radiusMin,
			double radiusMax, double ratiox, double ratioy, double ratioz) {
		TreeMap<Matter, Matter> miniListMatter = new TreeMap<Matter, Matter>();
		double miniMass = 0;
		for (int cpt = 0; cpt < HelperVariable.numberOfObjects; cpt++) {
			Matter m;
			boolean cont = false;
			do {
				double theta = Math.random() * Math.PI * 2;
				double phi = Math.random() * Math.PI * 2;
				Random random = new Random();
				double r = random.nextDouble();
				m = new Matter(
						parameters,
						new Vector3d(
								origine.x
										+ Math.cos(theta)
										* Math.sin(phi)
										* (r * ratiox * (radiusMax - radiusMin) + radiusMin),
								origine.y
										+ Math.sin(theta)
										* Math.sin(phi)
										* (r * ratioy * (radiusMax - radiusMin) + radiusMin),
								origine.z
										+ Math.cos(phi)
										* (r * ratioz * (radiusMax - radiusMin) + radiusMin)),
						HelperVariable.massObjectMin
								+ Math.random()
								* (HelperVariable.massObjectMax - HelperVariable.massObjectMin)
								+ Math.random(),
						new Vector3d(0, 0, 0),
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

			for (Matter mbis : miniListMatter.values()) {
				double alea = Math.random();
				mbis.setColor(new Vector3d(0.90 + Math.random() / 4,
						0.90 + Math.random() / 4, 0.90 + Math.random() / 4));
				if (alea > 0.80) {
					mbis.setColor(new Vector3d(0.90 + Math.random() * 0.1,
							0.4 + Math.random() * 0.1,
							0.4 + Math.random() * 0.1));
				}
				if (alea > 0.90) {
					mbis.setColor(new Vector3d(0.4 + Math.random() * 0.10,
							0.4 + Math.random() * 0.10,
							0.9 + Math.random() * 0.1));
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
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				HelperVariable.nebulaRadius, 1, 1, 1);
	}

	private void createRandomRotateUnivers() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				HelperVariable.nebulaRadius, 0.25, 1, 0.25);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), 0), HelperVariable.darkMatterMass, new Vector3d(
				0, 0, 0), HelperVariable.darkMatterDensity, true);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}
	}

	private void createGalaxiesCollision() {
		double transSpeed = 0.3;
		TreeMap<Matter, Matter> subu01 = createUvivers(new Vector3d(-400, -100,
				-50), 0, transSpeed, 0, HelperVariable.nebulaRadius, 0.25, 1,
				0.25);
		TreeMap<Matter, Matter> subu02 = createUvivers(new Vector3d(400, 100,
				50), 0, -transSpeed, 0, HelperVariable.nebulaRadius / 2, 1,
				0.15, 0.15);

		Matter m1 = new Matter(parameters, new Vector3d(-400 + Math.random(),
				-100 + Math.random(), -50 + Math.random()),
				HelperVariable.darkMatterMass + Math.random(), new Vector3d(
						transSpeed, 0, 0), HelperVariable.darkMatterDensity,
				true);
		listMatter.put(m1, m1);
		mass += m1.getMass();

		Matter m2 = new Matter(parameters, new Vector3d(400 + Math.random(),
				100 + Math.random(), 50 + Math.random()),
				HelperVariable.darkMatterMass + Math.random(), new Vector3d(
						-transSpeed, 0, 0), HelperVariable.darkMatterDensity,
				true);
		listMatter.put(m2, m2);
		mass += m2.getMass();

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
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), 1E10 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m1, m1);
		Matter m2 = new Matter(parameters, new Vector3d(50 + Math.random(),
				10 + Math.random(), Math.random()), 1E4 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m2, m2);
		Matter m3 = new Matter(parameters, new Vector3d(-320 + Math.random(),
				30 + Math.random(), Math.random()), 1E9 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m3, m3);
		Matter m4 = new Matter(parameters, new Vector3d(-100 + Math.random(),
				-10 + Math.random(), Math.random()), 1E2 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m4, m4);
		Matter m5 = new Matter(parameters, new Vector3d(90 + Math.random(), -20
				+ Math.random(), Math.random()), 1E3 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m5, m5);
		Matter m6 = new Matter(parameters, new Vector3d(-20 + Math.random(),
				-10 + Math.random(), Math.random()), 1E2 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		listMatter.put(m6, m6);
		Matter m7 = new Matter(parameters, new Vector3d(-330 + Math.random(),
				30 + Math.random(), Math.random()), 1E2 + Math.random(),
				new Vector3d(0, 0, 0), 300, false);
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
			mass += m.getMass();
		}
		m7.getSpeed().add(m7.orbitalSpeed(m3, new Vector3d(0, 1, 0)));
		listMatter.put(m7, m7);

	}

	private void createPlanetaryRandom() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				HelperVariable.nebulaRadius, 0.25, 1, 1);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), HelperVariable.darkMatterMass,
				new Vector3d(0, 0, 0), HelperVariable.darkMatterDensity, false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}
	}

	private void createPlanetariesGenesis() {
		createUvivers(new Vector3d(0, 0, 0), 0, 0, 0,
				HelperVariable.nebulaRadius, 1, 1, 0.25);
		Matter m1 = new Matter(parameters, new Vector3d(Math.random(),
				Math.random(), Math.random()), HelperVariable.darkMatterMass,
				new Vector3d(0, 0, 0), HelperVariable.darkMatterDensity, false);
		listMatter.put(m1, m1);
		mass += m1.getMass();
		for (Matter m : listMatter.values()) {
			if (m != m1) {
				m.setSpeed(m.orbitalSpeed(m1, new Vector3d(0, 0, 1)));
			}
		}
	}

	private void createDoubleStars() {
		Matter m1 = new Matter(parameters, new Vector3d(Math.random() - 50,
				Math.random() - 90, Math.random()), 5E9 + 1E10 + Math.random(),
				new Vector3d(0, 0, 0), HelperVariable.dentityMin, false);
		m1.setColor(new Vector3d(1, 0.7, 0.7));
		listMatter.put(m1, m1);
		mass += m1.getMass();

		Matter m2 = new Matter(parameters, new Vector3d(Math.random() + 50,
				Math.random() + 90, Math.random()), 1E10 + Math.random(),
				new Vector3d(0, 0, 0), HelperVariable.dentityMin, false);
		m2.setColor(new Vector3d(0.8, 0.8, 1));
		listMatter.put(m2, m2);
		mass += m2.getMass();
		m1.getSpeed().add(m1.orbitalSpeed(m2, new Vector3d(0, 0, 1)));
		m2.getSpeed().add(m2.orbitalSpeed(m1, new Vector3d(0, 0, 1)));

	}
}
