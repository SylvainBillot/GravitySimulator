package com.sylvanoid.joblib;

import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Vector3d;

import com.sylvanoid.common.HelperNewton;

public class Solid {

    private ConcurrentHashMap<String, Matter> listMatter = new ConcurrentHashMap<>();

    public Solid() {

    }

    public Solid(Matter m) {
	listMatter.put(Integer.toString(m.hashCode()), m);
	m.setMySolid(this);
    }

    public Solid(Matter m1, Matter m2) {
	listMatter.put(Integer.toString(m1.hashCode()), m1);
	listMatter.put(Integer.toString(m2.hashCode()), m2);
	m1.setMySolid(this);
	m2.setMySolid(this);
    }

    public void add(Matter m) {
	listMatter.put(Integer.toString(m.hashCode()), m);
	m.setMySolid(this);
    }

    public void transfertAll(Matter m) {
	for (Matter m1 : m.getMySolid().getListMatter().values()) {
	    listMatter.put(Integer.toString(m1.hashCode()), m1);
	}
	for (Matter m1 : m.getMySolid().getListMatter().values()) {
	    m1.setMySolid(this);
	}
    }

    public void removeAll(Matter m) {
	m.getMySolid().getListMatter().clear();
	m.setMySolid(null);
    }

    public boolean contains(Matter m) {
	return listMatter.contains(m);
    }

    public void changeSpeed() {
	Vector3d newSpeed = new Vector3d(0, 0, 0);
	Vector3d centroid = new Vector3d(0, 0, 0);

	double newMass = 0;
	double newDensity = 0;
	double newViscosity = 0;
	for (Matter m : listMatter.values()) {
	    newSpeed.x += m.getSpeed().x * m.getMass();
	    newSpeed.y += m.getSpeed().y * m.getMass();
	    newSpeed.z += m.getSpeed().z * m.getMass();
	    centroid.x += m.getPoint().x * m.getMass();
	    centroid.y += m.getPoint().y * m.getMass();
	    centroid.z += m.getPoint().z * m.getMass();
	    newMass += m.getMass();
	    newDensity += m.getDensity() * m.getMass();
	    newViscosity += m.getViscosity() * m.getMass();
	}
	newSpeed.scale(1 / newMass);
	centroid.scale(1 / newMass);
	newDensity /= newMass;
	newViscosity /= newMass;
	double newRadius = net.jafama.FastMath.pow(3.0 * (newMass / newDensity) / (4 * net.jafama.FastMath.PI),
		(double) 1.0 / (double) 3.0);

	for (Matter m : listMatter.values()) {
	    // delete speed to centroid

	    Vector3d relativeSpeed = new Vector3d(m.getSpeed());
	    relativeSpeed.sub(new Vector3d(newSpeed));
	    Vector3d radialSpeed = new Vector3d(m.getPoint());
	    radialSpeed.sub(centroid);
	    radialSpeed.normalize();
	    double u = relativeSpeed.dot(radialSpeed);

	    double p = HelperNewton.distance(centroid, m)
		    / ((newRadius + m.getRayon()) * m.getParameters().getCollisionDistanceRatio());

	    radialSpeed.scale((1 - p) * u * newViscosity);
	    m.getAccel().sub(radialSpeed);
	    m.getLossspeed().add(radialSpeed);

/*	    
	    // pseudo presure
	    
	    double p = HelperNewton.distance(centroid, m)
		    / ((newRadius + m.getRayon()) * m.getParameters().getCollisionDistanceRatio());
	    double delta = m.getParameters().getTimeFactor() * m.getViscoElasticity()
		    * (1 - net.jafama.FastMath.pow(p, 1.0 / 2.0)* m.getViscoElasticityNear());

	    radialSpeed.normalize();
	    radialSpeed.scale(delta);
	    m.getAccel().sub(radialSpeed);

*/  
/*	    
	    // move to border
	    m.getPoint().sub(centroid);
	    m.getPoint().normalize();
	    m.getPoint().scale(newRadius*m.getParameters().getCollisionDistanceRatio());
	    m.getPoint().add(centroid);
*/
	}
    }

    /**
     * @return the listMatter
     */
    public ConcurrentHashMap<String, Matter> getListMatter() {
	return listMatter;
    }

    /**
     * @param listMatter
     *            the listMatter to set
     */
    public void setListMatter(ConcurrentHashMap<String, Matter> listMatter) {
	this.listMatter = listMatter;
    }
}
