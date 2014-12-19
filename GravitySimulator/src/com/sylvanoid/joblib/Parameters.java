package com.sylvanoid.joblib;

import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement
public class Parameters {
	private TypeOfUnivers typeOfUnivers = TypeOfUnivers.Planetary;
	private double elapsedTime = 0;
	private double timeFactor = 5;
	private double darkMatterMass = 1E12;
	private double darkMatterDensity = 1E10;
	private boolean manageImpact = false;
	private boolean exportToVideo = false;
	private boolean fusion = true;
	private double typeOfImpact = 1;
	private double density = 30;
	private int numberOfObjects = 1000;
	private double nebulaRadius = 600;
	private double massObjectMin = 10000;
	private double massObjectMax = 100000;
	private Vector3d eyes = new Vector3d(0, 0, 900);
	private Vector3d lookAt = new Vector3d(0.001, 0, -900);

	public Parameters() {

	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public double getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(double timeFactor) {
		this.timeFactor = timeFactor;
	}

	public double getDarkMatterDensity() {
		return darkMatterDensity;
	}

	public void setDarkMatterDensity(double darkMatterDensity) {
		this.darkMatterDensity = darkMatterDensity;
	}

	public double getDarkMatterMass() {
		return darkMatterMass;
	}

	public void setDarkMatterMass(double darkMatterMass) {
		this.darkMatterMass = darkMatterMass;
	}

	public boolean isManageImpact() {
		return manageImpact;
	}

	public void setManageImpact(boolean manageImpact) {
		this.manageImpact = manageImpact;
	}

	public TypeOfUnivers getTypeOfUnivers() {
		return typeOfUnivers;
	}

	public void setTypeOfUnivers(TypeOfUnivers typeOfUnivers) {
		this.typeOfUnivers = typeOfUnivers;
	}

	public boolean isFusion() {
		return fusion;
	}

	public void setFusion(boolean fusion) {
		this.fusion = fusion;
	}

	public boolean isExportToVideo() {
		return exportToVideo;
	}

	public void setExportToVideo(boolean exportToVideo) {
		this.exportToVideo = exportToVideo;
	}

	public double getTypeOfImpact() {
		return typeOfImpact;
	}

	public void setTypeOfImpact(double typeOfImpact) {
		this.typeOfImpact = typeOfImpact;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		this.density = density;
	}

	public int getNumberOfObjects() {
		return numberOfObjects;
	}

	public void setNumberOfObjects(int numberOfObjects) {
		this.numberOfObjects = numberOfObjects;
	}

	public double getNebulaRadius() {
		return nebulaRadius;
	}

	public void setNebulaRadius(double nebulaRadius) {
		this.nebulaRadius = nebulaRadius;
	}

	public double getMassObjectMin() {
		return massObjectMin;
	}

	public void setMassObjectMin(double massObjectMin) {
		this.massObjectMin = massObjectMin;
	}

	public double getMassObjectMax() {
		return massObjectMax;
	}

	public void setMassObjectMax(double massObjectMax) {
		this.massObjectMax = massObjectMax;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getEyes() {
		return eyes;
	}

	public void setEyes(Vector3d eyes) {
		this.eyes = eyes;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getLookAt() {
		return lookAt;
	}

	public void setLookAt(Vector3d lookAt) {
		this.lookAt = lookAt;
	}


}
