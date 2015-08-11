package com.sylvanoid.joblib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperDebug;
import com.sylvanoid.common.HelperNewton;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement(name = "matter")
public class Matter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Parameters parameters;
	private String name;
	private TypeOfObject typeOfObject;
	private double mass;
	private Vector3d pointBefore = new Vector3d(0, 0, 0);
	private Vector3d point = new Vector3d(0, 0, 0);
	private Vector3d angles = new Vector3d(0, 0, 0);
	private Vector3d speedBefore = new Vector3d(0, 0, 0);
	private Vector3d speed = new Vector3d(0, 0, 0);
	private Vector3d accel = new Vector3d(0, 0, 0);
	private Vector3d color = new Vector3d(1, 1, 1);
	private double density;
	private double rayon;
	private List<Matter> fusionWith = new ArrayList<Matter>();
	private List<Matter> neighbors = new ArrayList<Matter>();

	@Override
	public String toString() {
		return name + " mass:" + mass + " x:" + point.x + " y:" + point.y
				+ " z:" + point.z + " vx:" + speed.x + " vy:" + speed.y
				+ " vz:" + speed.z;
	}

	public Matter() {

	}

	/* For comaraison only */
	public Matter(Vector3d point) {
		this.setPoint(point);
		mass = net.jafama.FastMath.random();
	}

	public Matter(Parameters parameters, Vector3d point, double mass,
			Vector3d speed, Vector3d color, double density,
			TypeOfObject typeOfObject) {
		this.parameters = parameters;
		this.setPoint(point);
		this.mass = mass;
		this.speed = speed;
		this.color = color;
		this.density = density;
		this.typeOfObject = typeOfObject;
		this.name = "id: " + this.hashCode();
		this.rayon = net.jafama.FastMath.pow(3 * (mass / density)
				/ (4 * net.jafama.FastMath.PI), (double) 1 / (double) 3);
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public TypeOfObject getTypeOfObject() {
		return typeOfObject;
	}

	public void setTypeOfObject(TypeOfObject typeOfObject) {
		this.typeOfObject = typeOfObject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getPoint() {
		return point;
	}

	public void setPoint(Vector3d point) {
		this.point = point;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getPointBefore() {
		return pointBefore;
	}

	public void setPointBefore(Vector3d pointBefore) {
		this.pointBefore = pointBefore;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getAngles() {
		return angles;
	}

	public void setAngles(Vector3d angles) {
		this.angles = angles;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getColor() {
		return color;
	}

	public void setColor(Vector3d color) {
		this.color = color;
	}

	public Vector3d getSpeedBefore() {
		return speedBefore;
	}

	public void setSpeedBefore(Vector3d speedBefore) {
		this.speedBefore = speedBefore;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getSpeed() {
		return speed;
	}

	public void setSpeed(Vector3d speed) {
		this.speed = speed;
	}

	public Vector3d getAccel() {
		return accel;
	}

	public void setAccel(Vector3d accel) {
		this.accel = accel;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		rayon = net.jafama.FastMath.pow(3 * (mass / density)
				/ (4 * net.jafama.FastMath.PI), (double) 1 / (double) 3);
		this.mass = mass;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public boolean isDark() {
		return typeOfObject == TypeOfObject.Dark;
	}

	public double getRayon() {
		return rayon;
	}

	public List<Matter> getFusionWith() {
		return fusionWith;
	}

	public void setFusionWith(List<Matter> fusionWith) {
		this.fusionWith = fusionWith;
	}

	public List<Matter> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Matter> neighbors) {
		this.neighbors = neighbors;
	}

	public Vector3d getPlusV() {
		return new Vector3d(point.x + speed.x * parameters.getTimeFactor(),
				point.y + speed.y * parameters.getTimeFactor(), point.z
						+ speed.z * parameters.getTimeFactor());
	}

	public Vector3d getMinusV() {
		return new Vector3d(point.x - speed.x * parameters.getTimeFactor(),
				point.y - speed.y * parameters.getTimeFactor(), point.z
						- speed.z * parameters.getTimeFactor());
	}

	public Vector3d maxWithR() {
		return new Vector3d((point.x > getPlusV().x) ? (point.x + rayon)
				: (getPlusV().x + rayon),
				(point.y > getPlusV().y) ? (point.y + rayon)
						: (getPlusV().y + rayon),
				(point.z > getPlusV().z) ? (point.z + rayon)
						: (getPlusV().z + rayon));
	}

	public Vector3d minWithR() {
		return new Vector3d((point.x > getPlusV().x) ? (getPlusV().x - rayon)
				: (point.x - rayon),
				(point.y > getPlusV().y) ? (getPlusV().y - rayon)
						: (point.y - rayon),
				(point.z > getPlusV().z) ? (getPlusV().z - rayon)
						: (point.z - rayon));
	}

	public Vector3d max() {
		return new Vector3d((point.x > getPlusV().x) ? (point.x)
				: (getPlusV().x), (point.y > getPlusV().y) ? (point.y)
				: (getPlusV().y), (point.z > getPlusV().z) ? (point.z)
				: (getPlusV().z));
	}

	public Vector3d min() {
		return new Vector3d((point.x > getPlusV().x) ? (getPlusV().x)
				: (point.x), (point.y > getPlusV().y) ? (getPlusV().y)
				: (point.y), (point.z > getPlusV().z) ? (getPlusV().z)
				: (point.z));
	}

	public void move() {
		pointBefore = new Vector3d(point);
		speedBefore = new Vector3d(speed);
		speed.add(accel);
		point = getPlusV();
		accel = new Vector3d(0, 0, 0);
	}

	public void fusion(List<Matter> listMatter) {
		if (listMatter.contains(this)) {
			Vector3d newPoint = new Vector3d(point);
			Vector3d newSpeed = new Vector3d(speed);
			Vector3d newColor = new Vector3d(color);
			double newDensity = density;
			double newMass = mass;

			for (Matter m : fusionWith) {
				if (listMatter.contains(m)) {
					if (m.mass > newMass) {
						newColor = new Vector3d(m.getColor());
					}
					newPoint = new Vector3d(
							(newPoint.x * newMass + m.getPoint().x
									* m.getMass())
									/ (newMass + m.getMass()), (newPoint.y
									* newMass + m.getPoint().y * m.getMass())
									/ (newMass + m.getMass()), (newPoint.z
									* newMass + m.getPoint().z * m.getMass())
									/ (newMass + m.getMass()));
					newSpeed = new Vector3d(
							(newSpeed.x * newMass + m.getSpeed().x
									* m.getMass())
									/ (newMass + m.getMass()), (newSpeed.y
									* newMass + m.getSpeed().y * m.getMass())
									/ (newMass + m.getMass()), (newSpeed.z
									* newMass + m.getSpeed().z * m.getMass())
									/ (newMass + m.getMass()));
					newDensity = (newDensity * newMass + m.getDensity()
							* m.getMass())
							/ (newMass + m.getMass());
					newMass = newMass + m.getMass();
					listMatter.remove(m);
				}
			}
			listMatter.remove(this);
			listMatter.add(new Matter(parameters, newPoint, newMass, newSpeed,
					newColor, newDensity, typeOfObject));
		}
	}

	public void impact() {
		String msg = fusionWith.size() + " " + name + " - ";
		for (Matter mf : fusionWith) {
			msg += mf.getName() + " ";
		}
		HelperDebug.info(msg);
		double Cr = parameters.getTypeOfImpact();
		for (Matter m : fusionWith) {
			double v1x = (Cr * m.getMass() * (m.getSpeed().x - speed.x) + mass
					* speed.x + m.getMass() * m.getSpeed().x)
					/ (mass + m.getMass());
			double v1y = (Cr * m.getMass() * (m.getSpeed().y - speed.y) + mass
					* speed.y + m.getMass() * m.getSpeed().y)
					/ (mass + m.getMass());
			double v1z = (Cr * m.getMass() * (m.getSpeed().z - speed.z) + mass
					* speed.z + m.getMass() * m.getSpeed().z)
					/ (mass + m.getMass());
			Vector3d tmpSpeed = new Vector3d(0, 0, 0);
			tmpSpeed.add(new Vector3d(v1x, v1y, v1z));
			tmpSpeed.sub(speed);
			accel.add(tmpSpeed);
			/*
			accel.sub(HelperVector.acceleration(point, m.getPoint(),
					HelperNewton.attraction(this, m, parameters)));
					*/
		}

	}

	public void glue() {
		Vector3d newSpeed = new Vector3d(speed);
		double newMass = mass;
		for (Matter m : fusionWith) {
			newSpeed = new Vector3d((newSpeed.x * newMass + m.getSpeed().x
					* m.getMass())
					/ (newMass + m.getMass()),
					(newSpeed.y * newMass + m.getSpeed().y * m.getMass())
							/ (newMass + m.getMass()),
					(newSpeed.z * newMass + m.getSpeed().z * m.getMass())
							/ (newMass + m.getMass()));
			newMass = newMass + m.getMass();
			accel.sub(HelperVector.acceleration(point, m.getPoint(),
					HelperNewton.attraction(this, m, parameters)));
		}
		newSpeed.sub(speed);
		accel.add(newSpeed);
	}

	public static List<Matter> fusionWithRecursiveAdd(Matter m, Matter m1,
			List<Matter> noMore) {
		List<Matter> f = new ArrayList<Matter>();
		if (!noMore.contains(m1)) {
			noMore.add(m1);
			if (!f.contains(m1) && m != m1) {
				f.add(m1);
			}

			for (Matter m2 : m1.getFusionWith()) {
				f.addAll(fusionWithRecursiveAdd(m, m2, noMore));
			}
		}
		return f;
	}

	public void orbitalCircularSpeed(Matter m, Vector3d axis) {
		orbitalCircularSpeed(m.getMass(), m.getPoint(), axis);
	}

	public void orbitalCircularSpeed(Univers u, double distance,
			double innerMass, Vector3d axis) {
		if (!parameters.isStaticDarkMatter() || !isDark()) {
			double orbitalSpeedValue = net.jafama.FastMath
					.sqrt((HelperVariable.G * innerMass) / distance);

			Vector3d accel = HelperVector.acceleration(point, u.getGPoint(),
					orbitalSpeedValue);
			accel = axis.x != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
					net.jafama.FastMath.signum(axis.x) * net.jafama.FastMath.PI
							/ 2), net.jafama.FastMath.PI / 2) : accel;
			accel = axis.y != 0 ? HelperVector.rotate(accel, new Vector3d(0,
					net.jafama.FastMath.signum(axis.y) * net.jafama.FastMath.PI
							/ 2, 0), net.jafama.FastMath.signum(axis.y)
					* net.jafama.FastMath.PI / 2) : accel;
			accel = axis.z != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
					net.jafama.FastMath.signum(axis.z) * net.jafama.FastMath.PI
							/ 2), net.jafama.FastMath.signum(axis.z)
					* net.jafama.FastMath.PI / 2) : accel;
			speed.add(accel);
		}
	}

	private void orbitalCircularSpeed(double totalMass, Vector3d gPoint,
			Vector3d axis) {
		if (!parameters.isStaticDarkMatter() || !isDark()) {
			double distance = new Point3d(point).distance(new Point3d(gPoint));
			double orbitalSpeedValue = net.jafama.FastMath
					.sqrt(HelperVariable.G
							* net.jafama.FastMath.pow2(totalMass)
							/ ((mass + totalMass) * distance));
			Vector3d accel = HelperVector.acceleration(point, gPoint,
					orbitalSpeedValue);
			accel = axis.x != 0 ? HelperVector.rotate(accel, new Vector3d(
					net.jafama.FastMath.signum(axis.x), 0, 0),
					net.jafama.FastMath.PI / 2) : accel;
			accel = axis.y != 0 ? HelperVector.rotate(accel, new Vector3d(0,
					net.jafama.FastMath.signum(axis.y), 0),
					net.jafama.FastMath.signum(axis.y) * net.jafama.FastMath.PI
							/ 2) : accel;
			accel = axis.z != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
					net.jafama.FastMath.signum(axis.z)),
					net.jafama.FastMath.signum(axis.z) * net.jafama.FastMath.PI
							/ 2) : accel;

			speed.add(accel);
		}
	}

	public void orbitalEllipticSpeed(double totalMass, Vector3d gPoint,
			Vector3d axis, int nbArm) {
		if (!parameters.isStaticDarkMatter() || !isDark()) {
			// axis x --> ellipse on y
			// axis y --> ellipse on z
			// axis z --> ellipse on x

			double distance = new Point3d(gPoint).distance(new Point3d(point));
			double p1 = 0;
			double p2 = 0;

			p1 = axis.x != 0 ? point.y : p1;
			p1 = axis.y != 0 ? point.z : p1;
			p1 = axis.z != 0 ? point.x : p1;
			p2 = axis.x != 0 ? gPoint.y : p2;
			p2 = axis.y != 0 ? gPoint.z : p2;
			p2 = axis.z != 0 ? gPoint.x : p2;

			double e = parameters.getEllipseRatio();
			double base = (e * p1 - distance) / e;
			double h = p2 - base;
			double a = e * h / (1 - net.jafama.FastMath.pow2(e));
			double b = e * h
					/ net.jafama.FastMath.sqrt(1 - net.jafama.FastMath.pow2(e));
			double c = net.jafama.FastMath.pow2(e) * h
					/ (1 - net.jafama.FastMath.pow2(e));
			double n = p1 - net.jafama.FastMath.pow2(b) * (p1 - p2 - c)
					/ net.jafama.FastMath.pow2(a);
			Vector3d normalOnAxis = new Vector3d();

			normalOnAxis = axis.x != 0 ? new Vector3d(gPoint.x, n, gPoint.z)
					: normalOnAxis;
			normalOnAxis = axis.y != 0 ? new Vector3d(gPoint.x, gPoint.y, n)
					: normalOnAxis;
			normalOnAxis = axis.z != 0 ? new Vector3d(n, gPoint.y, gPoint.z)
					: normalOnAxis;

			double orbitalSpeed = net.jafama.FastMath.sqrt(HelperVariable.G
					* totalMass * (2 / distance - 1 / a));
			Vector3d accel = HelperVector.acceleration(point, normalOnAxis,
					orbitalSpeed);

			double alea = 2 * ((int) (nbArm * net.jafama.FastMath.random()))
					/ ((double) nbArm);
			double angle = net.jafama.FastMath.PI * alea;
			accel = HelperVector.rotate(accel, gPoint, axis, angle);
			point = HelperVector.rotate(point, gPoint, axis, angle);

			angle = parameters.getEllipseShiftRatio() * net.jafama.FastMath.PI
					* (distance / parameters.getNebulaRadius());
			accel = HelperVector.rotate(accel, gPoint, axis, angle);
			point = HelperVector.rotate(point, gPoint, axis, angle);

			accel = axis.x != 0 ? HelperVector.rotate(accel, new Vector3d(
					net.jafama.FastMath.signum(axis.x), 0, 0),
					net.jafama.FastMath.PI / 2) : accel;
			accel = axis.y != 0 ? HelperVector.rotate(accel, new Vector3d(0,
					net.jafama.FastMath.signum(axis.y), 0),
					net.jafama.FastMath.signum(axis.y) * net.jafama.FastMath.PI
							/ 2) : accel;
			accel = axis.z != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
					net.jafama.FastMath.signum(axis.z)),
					net.jafama.FastMath.signum(axis.z) * net.jafama.FastMath.PI
							/ 2) : accel;

			speed.add(accel);
		}
	}
}
