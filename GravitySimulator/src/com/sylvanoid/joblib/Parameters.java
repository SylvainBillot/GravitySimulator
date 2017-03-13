package com.sylvanoid.joblib;

import java.io.Serializable;

import javax.vecmath.Vector3d;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.TypeOfImpact;
import com.sylvanoid.common.TypeOfUnivers;
import com.sylvanoid.common.Vector3dAdapter;

@XmlRootElement
public class Parameters implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean frozen = false;
	private TypeOfUnivers typeOfUnivers = TypeOfUnivers.Planetary;
	private double elapsedTime = 0;
	private double timeFactor = 6000;
	private double darkMatterMass = 1E12;
	private double darkMatterDensity = 1E10;
	private boolean manageImpact = false;
	private boolean exportToVideo = false;
	private TypeOfImpact typeOfImpact = TypeOfImpact.Fusion;
	private double density = 100;
	private int numberOfObjects = 1000;
	private double nebulaRadius = HelperVariable.UA * 10;
	private double massObjectMin = 10000;
	private double massObjectMax = 100000;
	private Vector3d eyes = new Vector3d(0, 0, 900);
	private Vector3d lookAt = new Vector3d(0.001, 0, -900);
	private boolean followMaxMass = false;
	private boolean followCentroid = false;
	private boolean permanentRotationy = false;
	private boolean viewSimplePointOnly = false;
	private double scala = 1E-9;
	private boolean showAxis = true;
	private boolean showgrid = false;
	private boolean showInfo = true;
	private boolean showMatter = true;
	private boolean showGas = true;
	private boolean showDarkMatter = false;
	private Matter objectToFollow = null;
	private boolean showTrace = false;
	private int numOfCompute = 0;
	private int numOfAccelCompute = 0;
	private double cycleComputeTime = 0;
	private double limitComputeTime = 0;
	private double moveComputeTime = 0;
	private double barnesHuttComputeTime = 0;
	private double klength = 0;
	private double plength = 0;
	private double ellipseRatio = 0.75;
	private double ellipseShiftRatio = 3;
	private double negligeableMass = 0;
	private int numOfLowMassParticule = 0;
	private double lowMassParticuleMass = 1;
	private double lowMassDensity = 1;
	private int nbARms = 3;
	private boolean exportData = false;
	private boolean playData = false;
	private boolean parallelization = false;
	private boolean barnesHut = true;
	private boolean staticDarkMatter = true;
	private double matterDistribution = 5;
	private double gasDistribution = 1;
	private double darkMatterDistribution = 5;
	private Vector3d darkMatterXYZRatio = new Vector3d(5, 5, 5);
	private double matterViscosity = 1;
	private double gasViscosity = 1;
	private double darkMatterViscosity = 1;
	private double collisionDistanceRatio = 1;
	private double viscoElasticity = 1;
	private double viscoElasticityNear = 1;
	private double pressureZero = 0;
	private boolean recoverFrictionEnegy = false;
	private double recoverFrictionEnergyRatio = 0.5;
	private boolean expansionUnivers = false;
	private double timeMultiplicator = 1.000;
	private double matterRendererExtender = 10;
	private double gasRendererExtender = 10;
	private double darkMatterRendererExtender = 20;
	private boolean viscousDarkMatter = false;
	private boolean infiniteUnivers = false;
	private int videoPicBy = 1;

	public Parameters() {

	}

	/**
	 * @return the frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * @param frozen
	 *            the frozen to set
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
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

	public TypeOfImpact getTypeOfImpact() {
		return typeOfImpact;
	}

	public void setTypeOfImpact(TypeOfImpact typeOfImpact) {
		this.typeOfImpact = typeOfImpact;
	}

	public boolean isExportToVideo() {
		return exportToVideo;
	}

	public void setExportToVideo(boolean exportToVideo) {
		this.exportToVideo = exportToVideo;
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

	public boolean isFollowMaxMass() {
		return followMaxMass;
	}

	public void setFollowMaxMass(boolean followMaxMass) {
		this.followMaxMass = followMaxMass;
	}

	public boolean isFollowCentroid() {
		return followCentroid;
	}

	public void setFollowCentroid(boolean followCentroid) {
		this.followCentroid = followCentroid;
	}

	public boolean isPermanentRotationy() {
		return permanentRotationy;
	}

	public void setPermanentRotationy(boolean permanentRotationy) {
		this.permanentRotationy = permanentRotationy;
	}

	/**
	 * @return the viewSimplePointOnly
	 */
	public boolean isViewSimplePointOnly() {
		return viewSimplePointOnly;
	}

	/**
	 * @param viewSimplePointOnly
	 *            the viewSimplePointOnly to set
	 */
	public void setViewSimplePointOnly(boolean viewSimplePointOnly) {
		this.viewSimplePointOnly = viewSimplePointOnly;
	}

	public double getScala() {
		return scala;
	}

	public void setScala(double scala) {
		this.scala = scala;
	}

	public boolean isShowAxis() {
		return showAxis;
	}

	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
	}

	public boolean isShowgrid() {
		return showgrid;
	}

	public void setShowgrid(boolean showgrid) {
		this.showgrid = showgrid;
	}

	public boolean isShowInfo() {
		return showInfo;
	}

	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	public boolean isShowMatter() {
		return showMatter;
	}

	public void setShowMatter(boolean showMatter) {
		this.showMatter = showMatter;
	}

	public boolean isShowGas() {
		return showGas;
	}

	public void setShowGas(boolean showGas) {
		this.showGas = showGas;
	}

	public boolean isShowDarkMatter() {
		return showDarkMatter;
	}

	public void setShowDarkMatter(boolean showDarkMatter) {
		this.showDarkMatter = showDarkMatter;
	}

	public Matter getObjectToFollow() {
		return objectToFollow;
	}

	public void setObjectToFollow(Matter objectToFollow) {
		this.objectToFollow = objectToFollow;
	}

	public boolean isShowTrace() {
		return showTrace;
	}

	public void setShowTrace(boolean showTrace) {
		this.showTrace = showTrace;
	}

	public int getNumOfCompute() {
		return numOfCompute;
	}

	public void setNumOfCompute(int numOfCompute) {
		this.numOfCompute = numOfCompute;
	}

	public int getNumOfAccelCompute() {
		return numOfAccelCompute;
	}

	public void setNumOfAccelCompute(int numOfAccelCompute) {
		this.numOfAccelCompute = numOfAccelCompute;
	}

	public double getNegligeableMass() {
		return negligeableMass;
	}

	public void setNegligeableMass(double negligeableMass) {
		this.negligeableMass = negligeableMass;
	}

	public double getCycleComputeTime() {
		return cycleComputeTime;
	}

	public void setCycleComputeTime(double cycleComputeTime) {
		this.cycleComputeTime = cycleComputeTime;
	}

	public double getLimitComputeTime() {
		return limitComputeTime;
	}

	public void setLimitComputeTime(double limitComputeTime) {
		this.limitComputeTime = limitComputeTime;
	}

	public double getMoveComputeTime() {
		return moveComputeTime;
	}

	public void setMoveComputeTime(double moveComputeTime) {
		this.moveComputeTime = moveComputeTime;
	}

	public double getBarnesHuttComputeTime() {
		return barnesHuttComputeTime;
	}

	public void setBarnesHuttComputeTime(double barnesHuttComputeTime) {
		this.barnesHuttComputeTime = barnesHuttComputeTime;
	}

	public double getKlength() {
		return klength;
	}

	public void setKlength(double klength) {
		this.klength = klength;
	}

	public double getPlength() {
		return plength;
	}

	public void setPlength(double plength) {
		this.plength = plength;
	}

	public int getNumOfLowMassParticule() {
		return numOfLowMassParticule;
	}

	public void setNumOfLowMassParticule(int numOfLowMassParticule) {
		this.numOfLowMassParticule = numOfLowMassParticule;
	}

	public double getLowMassParticuleMass() {
		return lowMassParticuleMass;
	}

	public void setLowMassParticuleMass(double lowMassParticuleMass) {
		this.lowMassParticuleMass = lowMassParticuleMass;
	}

	public double getLowMassDensity() {
		return lowMassDensity;
	}

	public void setLowMassDensity(double lowMassDensity) {
		this.lowMassDensity = lowMassDensity;
	}

	public double getEllipseShiftRatio() {
		return ellipseShiftRatio;
	}

	public void setEllipseShiftRatio(double ellipseShiftRatio) {
		this.ellipseShiftRatio = ellipseShiftRatio;
	}

	public double getEllipseRatio() {
		return ellipseRatio;
	}

	public void setEllipseRatio(double ellipseRatio) {
		this.ellipseRatio = ellipseRatio;
	}

	public int getNbARms() {
		return nbARms;
	}

	public void setNbARms(int nbARms) {
		this.nbARms = nbARms;
	}

	public boolean isExportData() {
		return exportData;
	}

	public void setExportData(boolean exportData) {
		this.exportData = exportData;
	}

	public boolean isPlayData() {
		return playData;
	}

	public void setPlayData(boolean playData) {
		this.playData = playData;
	}

	public boolean isParallelization() {
		return parallelization;
	}

	public void setParallelization(boolean parallelization) {
		this.parallelization = parallelization;
	}

	/**
	 * @return the barnesHut
	 */
	public boolean isBarnesHut() {
		return barnesHut;
	}

	/**
	 * @param barnesHut
	 *            the barnesHut to set
	 */
	public void setBarnesHut(boolean barnesHut) {
		this.barnesHut = barnesHut;
	}

	public boolean isStaticDarkMatter() {
		return staticDarkMatter;
	}

	public void setStaticDarkMatter(boolean staticDarkMatter) {
		this.staticDarkMatter = staticDarkMatter;
	}

	public double getMatterDistribution() {
		return matterDistribution;
	}

	public void setMatterDistribution(double matterDistribution) {
		this.matterDistribution = matterDistribution;
	}

	public double getGasDistribution() {
		return gasDistribution;
	}

	public void setGasDistribution(double gasDistribution) {
		this.gasDistribution = gasDistribution;
	}

	public double getDarkMatterDistribution() {
		return darkMatterDistribution;
	}

	public void setDarkMatterDistribution(double darkMatterDistribution) {
		this.darkMatterDistribution = darkMatterDistribution;
	}

	public double getMatterViscosity() {
		return matterViscosity;
	}

	public void setMatterViscosity(double matterViscosity) {
		this.matterViscosity = matterViscosity;
	}

	public double getGasViscosity() {
		return gasViscosity;
	}

	public void setGasViscosity(double gasViscosity) {
		this.gasViscosity = gasViscosity;
	}

	public double getDarkMatterViscosity() {
		return darkMatterViscosity;
	}

	public void setDarkMatterViscosity(double darkMatterViscosity) {
		this.darkMatterViscosity = darkMatterViscosity;
	}

	public double getCollisionDistanceRatio() {
		return collisionDistanceRatio;
	}

	public void setCollisionDistanceRatio(double collisionDistanceRatio) {
		this.collisionDistanceRatio = collisionDistanceRatio;
	}

	public double getViscoElasticity() {
		return viscoElasticity;
	}

	public void setViscoElasticity(double viscoElasticity) {
		this.viscoElasticity = viscoElasticity;
	}

	public double getViscoElasticityNear() {
		return viscoElasticityNear;
	}

	public void setViscoElasticityNear(double viscoElasticityNear) {
		this.viscoElasticityNear = viscoElasticityNear;
	}

	public double getPressureZero() {
		return pressureZero;
	}

	public void setPressureZero(double pressureZero) {
		this.pressureZero = pressureZero;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getDarkMatterXYZRatio() {
		return darkMatterXYZRatio;
	}

	public void setDarkMatterXYZRatio(Vector3d darkMatterXYZRatio) {
		this.darkMatterXYZRatio = darkMatterXYZRatio;
	}

	public boolean isRecoverFrictionEnegy() {
		return recoverFrictionEnegy;
	}

	public void setRecoverFrictionEnegy(boolean recoverFrictionEnegy) {
		this.recoverFrictionEnegy = recoverFrictionEnegy;
	}

	public double getRecoverFrictionEnergyRatio() {
		return recoverFrictionEnergyRatio;
	}

	public void setRecoverFrictionEnergyRatio(double recoverFrictionEnergyRatio) {
		this.recoverFrictionEnergyRatio = recoverFrictionEnergyRatio;
	}

	/**
	 * @return the expansionUnivers
	 */
	public boolean isExpansionUnivers() {
		return expansionUnivers;
	}

	/**
	 * @param expansionUnivers
	 *            the expansionUnivers to set
	 */
	public void setExpansionUnivers(boolean expansionUnivers) {
		this.expansionUnivers = expansionUnivers;
	}

	/**
	 * @return the timeMultiplicator
	 */
	public double getTimeMultiplicator() {
		return timeMultiplicator;
	}

	/**
	 * @param timeMultiplicator
	 *            the timeMultiplicator to set
	 */
	public void setTimeMultiplicator(double timeMultiplicator) {
		this.timeMultiplicator = timeMultiplicator;
	}

	/**
	 * @return the matterRendererExtender
	 */
	public double getMatterRendererExtender() {
		return matterRendererExtender;
	}

	/**
	 * @param matterRendererExtender
	 *            the matterRendererExtender to set
	 */
	public void setMatterRendererExtender(double matterRendererExtender) {
		this.matterRendererExtender = matterRendererExtender;
	}

	/**
	 * @return the gasRendererExtender
	 */
	public double getGasRendererExtender() {
		return gasRendererExtender;
	}

	/**
	 * @param gasRendererExtender
	 *            the gasRendererExtender to set
	 */
	public void setGasRendererExtender(double gasRendererExtender) {
		this.gasRendererExtender = gasRendererExtender;
	}

	/**
	 * @return the darkMatterRendererExtender
	 */
	public double getDarkMatterRendererExtender() {
		return darkMatterRendererExtender;
	}

	/**
	 * @param darkMatterRendererExtender
	 *            the darkMatterRendererExtender to set
	 */
	public void setDarkMatterRendererExtender(double darkMatterRendererExtender) {
		this.darkMatterRendererExtender = darkMatterRendererExtender;
	}

	/**
	 * @return the visousDarkMatter
	 */
	public boolean isViscousDarkMatter() {
		return viscousDarkMatter;
	}

	/**
	 * @param visousDarkMatter
	 *            the visousDarkMatter to set
	 */
	public void setViscousDarkMatter(boolean visousDarkMatter) {
		this.viscousDarkMatter = visousDarkMatter;
	}

	/**
	 * @return the infiniteUnivers
	 */
	public boolean isInfiniteUnivers() {
		return infiniteUnivers;
	}

	/**
	 * @param infiniteUnivers
	 *            the infiniteUnivers to set
	 */
	public void setInfiniteUnivers(boolean infiniteUnivers) {
		this.infiniteUnivers = infiniteUnivers;
	}

	/**
	 * @return the videoPicBy
	 */
	public int getVideoPicBy() {
		return videoPicBy;
	}

	/**
	 * @param videoPicBy
	 *            the videoPicBy to set
	 */
	public void setVideoPicBy(int videoPicBy) {
		this.videoPicBy = videoPicBy;
	}

}
