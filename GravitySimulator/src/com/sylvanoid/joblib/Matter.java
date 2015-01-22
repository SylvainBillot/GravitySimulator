package com.sylvanoid.joblib;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;
import com.sylvanoid.common.TypeOfObject;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement(name = "matter")
public class Matter {
	private Parameters parameters;
	private String name;
	private TypeOfObject typeOfObject = TypeOfObject.Star;
	private double mass;
	private Vector3d pointBefore = new Vector3d(0, 0, 0);
	private Vector3d point = new Vector3d(0, 0, 0);
	private Vector3d angles = new Vector3d(0, 0, 0);
	private Vector3d speed = new Vector3d(0, 0, 0);
	private Vector3d color = new Vector3d(1, 1, 1);
	private double density;
	private double rayon;
	private List<Matter> fusionWith = new ArrayList<Matter>();

	private Vector3d impactSpeed = new Vector3d(0, 0, 0);

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
		mass = Math.random();
	}

	public Matter(Parameters parameters, Vector3d point, double mass,
			Vector3d speed, Vector3d color, double density, boolean isDark) {
		this.parameters = parameters;
		this.setPoint(point);
		this.mass = mass;
		this.speed = speed;
		this.color = color;
		this.density = density;
		if (isDark) {
			typeOfObject = TypeOfObject.Dark;
		}
		this.name = "id: " + this.hashCode();
		this.rayon = Math.pow(3 * (mass / density) / (4 * Math.PI), (double) 1
				/ (double) 3);
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

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getSpeed() {
		return speed;
	}

	public void setSpeed(Vector3d speed) {
		this.speed = speed;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		rayon = Math.pow(3 * (mass / density) / (4 * Math.PI), (double) 1
				/ (double) 3);
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
		if (!impactSpeed.equals(new Vector3d(0, 0, 0))) {
			speed = new Vector3d(impactSpeed);
			impactSpeed = new Vector3d(0, 0, 0);
		}
		point = getPlusV();
	}

	public void fusion(List<Matter> listMatter) {
		Vector3d newPoint = new Vector3d(point);
		Vector3d newSpeed = new Vector3d(speed);
		Vector3d newColor = new Vector3d(color);
		double newDensity = density;
		double newMass = mass;
		boolean newIsDark = isDark();
		for (Matter m : fusionWith) {
			if (listMatter.contains(m)) {
				if (m.mass > newMass) {
					newColor = new Vector3d(m.getColor());
				}
				newPoint = new Vector3d((newPoint.x * newMass + m.getPoint().x
						* m.getMass())
						/ (newMass + m.getMass()),
						(newPoint.y * newMass + m.getPoint().y * m.getMass())
								/ (newMass + m.getMass()), (newPoint.z
								* newMass + m.getPoint().z * m.getMass())
								/ (newMass + m.getMass()));
				newSpeed = new Vector3d((newSpeed.x * newMass + m.getSpeed().x
						* m.getMass())
						/ (newMass + m.getMass()),
						(newSpeed.y * newMass + m.getSpeed().y * m.getMass())
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
				newColor, newDensity, newIsDark));
	}

	public void elastic(double k) {
		for (Matter m : fusionWith) {
			double distance = new Point3d(point).distance(new Point3d(m
					.getPoint()));
			double dx = distance - rayon + m.getRayon();
			double elasticForceAccel = dx * k * parameters.getTimeFactor();
			speed.add(HelperVector.acceleration(point, m.getPoint(),
					elasticForceAccel));
		}
	}

	public void impact() {
		System.out.println(fusionWith.size());
		for (Matter m : fusionWith) {
			System.out.println(hashCode() + "->" + m.hashCode());
			double Cr = parameters.getTypeOfImpact();
			double v1x = (Cr * m.getMass() * (m.getSpeed().x - speed.x) + mass
					* speed.x + m.getMass() * m.getSpeed().x)
					/ (mass + m.getMass());
			double v1y = (Cr * m.getMass() * (m.getSpeed().y - speed.y) + mass
					* speed.y + m.getMass() * m.getSpeed().y)
					/ (mass + m.getMass());
			double v1z = (Cr * m.getMass() * (m.getSpeed().z - speed.z) + mass
					* speed.z + m.getMass() * m.getSpeed().z)
					/ (mass + m.getMass());
			impactSpeed.add(new Vector3d(v1x, v1y, v1z));
		}
	}

	public Vector3d orbitalCircularSpeed(Matter m, Vector3d axis) {
		double distance = new Point3d(point)
				.distance(new Point3d(m.getPoint()));
		double orbitalSpeedValue = Math.pow(
				HelperVariable.G * Math.pow(m.getMass(), 2)
						/ ((mass + m.getMass()) * distance), 0.5);

		Vector3d accel = HelperVector.acceleration(point, m.getPoint(),
				orbitalSpeedValue);

		accel = axis.x != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
				Math.signum(axis.x) * Math.PI / 2), Math.PI / 2) : accel;
		accel = axis.y != 0 ? HelperVector.rotate(accel,
				new Vector3d(0, Math.signum(axis.y) * Math.PI / 2, 0),
				Math.signum(axis.y) * Math.PI / 2) : accel;
		accel = axis.z != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
				Math.signum(axis.z) * Math.PI / 2), Math.signum(axis.z)
				* Math.PI / 2) : accel;

		return accel;
	}

	public Vector3d orbitalEllipticSpeed(Matter m, Vector3d axis) {
		double distance = new Point3d(point)
				.distance(new Point3d(m.getPoint()));

		double d = 0;
		d = axis.x != 0 ? Math.abs(point.y - m.getPoint().y) : d;
		d = axis.y != 0 ? Math.abs(point.z - m.getPoint().z) : d;
		d = axis.z != 0 ? Math.abs(point.x - m.getPoint().x) : d;
		
		double a = distance * parameters.getEllipseRatio()
				+ (1 - parameters.getEllipseRatio()) * d;

		double orbitalSpeedValue = Math.pow(HelperVariable.G * m.getMass()
				* ((2 / distance) - (1 / a)), 0.5);

		Vector3d accel = HelperVector.acceleration(point, m.getPoint(),
				orbitalSpeedValue);

		double angle = parameters.getEllipseShiftRatio() * Math.PI * distance
				/ parameters.getNebulaRadius();
		point = HelperVector.rotate(point, m.getPoint(), axis, angle);
		accel = HelperVector.rotate(accel, axis, angle);

		accel = axis.x != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
				Math.signum(axis.x) * Math.PI / 2), Math.PI / 2) : accel;
		accel = axis.y != 0 ? HelperVector.rotate(accel,
				new Vector3d(0, Math.signum(axis.y) * Math.PI / 2, 0),
				Math.signum(axis.y) * Math.PI / 2) : accel;
		accel = axis.z != 0 ? HelperVector.rotate(accel, new Vector3d(0, 0,
				Math.signum(axis.z) * Math.PI / 2), Math.signum(axis.z)
				* Math.PI / 2) : accel;

		return accel;
	}
}
