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
	private static TypeOfUnivers typeOfUnivers = TypeOfUnivers.Planetary;
	private static double elapsedTime = 0;
	private static double timeFactor = 6000;
	private static double darkMatterMass = 1E12;
	private static double darkMatterDensity = 1E10;
	private static boolean manageImpact = false;
	private static boolean exportToVideo = false;
	private static TypeOfImpact typeOfImpact = TypeOfImpact.Fusion;
	private static double density = 100;
	private static int numberOfObjects = 1000;
	private static double nebulaRadius = HelperVariable.UA * 10;
	private static double massObjectMin = 10000;
	private static double massObjectMax = 100000;
	private static Vector3d eyes = new Vector3d(0, 0, 900);
	private static Vector3d lookAt = new Vector3d(0.001, 0, -900);
	private static boolean followMaxMass = false;
	private static boolean followCentroid = false;
	private static boolean permanentRotationy = false;
	private static double scala = 1E-9;
	private static boolean showAxis = true;
	private static boolean showgrid = false;
	private static boolean showInfo = true;
	private static boolean showMatter = true;
	private static boolean showGas = true;
	private static boolean showDarkMatter = false;
	private static Matter objectToFollow = null;
	private static boolean showTrace = false;
	private static int numOfCompute = 0;
	private static int numOfAccelCompute = 0;
	private static double cycleComputeTime = 0;
	private static double limitComputeTime = 0;
	private static double moveComputeTime = 0;
	private static double barnesHuttComputeTime = 0;
	private static double klength = 0;
	private static double plength = 0;
	private static double ellipseRatio = 0.75;
	private static double ellipseShiftRatio = 3;
	private static double negligeableMass = 0;
	private static int numOfLowMassParticule = 0;
	private static double lowMassParticuleMass = 1;
	private static double lowMassDensity = 1;
	private static Vector3d demiDistanceBetweenGalaxies = new Vector3d(
			200000 * HelperVariable.PC, 0, 0);
	private static int nbARms = 3;
	private static boolean exportData = false;
	private static boolean playData = false;
	private static boolean parallelization = false;
	private static boolean staticDarkMatter = true;
	private static double matterDistribution = 5;
	private static double gasDistribution = 1;
	private static double darkMatterDistribution = 5;
	private static double darkMatterNubulaFactor = 5;
	private static Vector3d darkMatterXYZRatio = new Vector3d(1,1,1);

	private static double matterViscosity = 1;
	private static double gasViscosity = 1;
	private static double darkMatterViscosity = 0;

	private static double collisionDistanceRatio = 1;

	private static double viscoElasticity = 1;
	private static double viscoElasticityNear = 1;
	private static double pressureZero = 0;

	private static boolean recoverFrictionEnegy = false;
	private static double recoverFrictionEnergyRatio = 0.5;

	private static boolean expansionUnivers = true;
	private static double timeMultiplicator = 1.000;


	private static double matterRendererExtender = 3;
	private static double gasRendererExtender = 3;
	private static double darkMatterRendererExtender = 5;

	public Parameters() {

	}

	public double getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(double elapsedTime) {
		Parameters.elapsedTime = elapsedTime;
	}

	public double getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(double timeFactor) {
		Parameters.timeFactor = timeFactor;
	}

	public double getDarkMatterDensity() {
		return darkMatterDensity;
	}

	public void setDarkMatterDensity(double darkMatterDensity) {
		Parameters.darkMatterDensity = darkMatterDensity;
	}

	public double getDarkMatterMass() {
		return darkMatterMass;
	}

	public void setDarkMatterMass(double darkMatterMass) {
		Parameters.darkMatterMass = darkMatterMass;
	}

	public boolean isManageImpact() {
		return manageImpact;
	}

	public void setManageImpact(boolean manageImpact) {
		Parameters.manageImpact = manageImpact;
	}

	public TypeOfUnivers getTypeOfUnivers() {
		return typeOfUnivers;
	}

	public void setTypeOfUnivers(TypeOfUnivers typeOfUnivers) {
		Parameters.typeOfUnivers = typeOfUnivers;
	}

	public TypeOfImpact getTypeOfImpact() {
		return typeOfImpact;
	}

	public void setTypeOfImpact(TypeOfImpact typeOfImpact) {
		Parameters.typeOfImpact = typeOfImpact;
	}

	public boolean isExportToVideo() {
		return exportToVideo;
	}

	public void setExportToVideo(boolean exportToVideo) {
		Parameters.exportToVideo = exportToVideo;
	}

	public double getDensity() {
		return density;
	}

	public void setDensity(double density) {
		Parameters.density = density;
	}

	public int getNumberOfObjects() {
		return numberOfObjects;
	}

	public void setNumberOfObjects(int numberOfObjects) {
		Parameters.numberOfObjects = numberOfObjects;
	}

	public double getNebulaRadius() {
		return nebulaRadius;
	}

	public void setNebulaRadius(double nebulaRadius) {
		Parameters.nebulaRadius = nebulaRadius;
	}

	public double getMassObjectMin() {
		return massObjectMin;
	}

	public void setMassObjectMin(double massObjectMin) {
		Parameters.massObjectMin = massObjectMin;
	}

	public double getMassObjectMax() {
		return massObjectMax;
	}

	public void setMassObjectMax(double massObjectMax) {
		Parameters.massObjectMax = massObjectMax;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getEyes() {
		return eyes;
	}

	public void setEyes(Vector3d eyes) {
		Parameters.eyes = eyes;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getLookAt() {
		return lookAt;
	}

	public void setLookAt(Vector3d lookAt) {
		Parameters.lookAt = lookAt;
	}

	public boolean isFollowMaxMass() {
		return followMaxMass;
	}

	public void setFollowMaxMass(boolean followMaxMass) {
		Parameters.followMaxMass = followMaxMass;
	}

	public boolean isFollowCentroid() {
		return followCentroid;
	}

	public void setFollowCentroid(boolean followCentroid) {
		Parameters.followCentroid = followCentroid;
	}

	public boolean isPermanentRotationy() {
		return permanentRotationy;
	}

	public void setPermanentRotationy(boolean permanentRotationy) {
		Parameters.permanentRotationy = permanentRotationy;
	}

	public double getScala() {
		return scala;
	}

	public void setScala(double scala) {
		Parameters.scala = scala;
	}

	public boolean isShowAxis() {
		return showAxis;
	}

	public void setShowAxis(boolean showAxis) {
		Parameters.showAxis = showAxis;
	}

	public boolean isShowgrid() {
		return showgrid;
	}

	public void setShowgrid(boolean showgrid) {
		Parameters.showgrid = showgrid;
	}

	public boolean isShowInfo() {
		return showInfo;
	}

	public void setShowInfo(boolean showInfo) {
		Parameters.showInfo = showInfo;
	}

	public boolean isShowMatter() {
		return showMatter;
	}

	public void setShowMatter(boolean showMatter) {
		Parameters.showMatter = showMatter;
	}

	public boolean isShowGas() {
		return showGas;
	}

	public void setShowGas(boolean showGas) {
		Parameters.showGas = showGas;
	}

	public boolean isShowDarkMatter() {
		return showDarkMatter;
	}

	public void setShowDarkMatter(boolean showDarkMatter) {
		Parameters.showDarkMatter = showDarkMatter;
	}

	public Matter getObjectToFollow() {
		return objectToFollow;
	}

	public void setObjectToFollow(Matter objectToFollow) {
		Parameters.objectToFollow = objectToFollow;
	}

	public boolean isShowTrace() {
		return showTrace;
	}

	public void setShowTrace(boolean showTrace) {
		Parameters.showTrace = showTrace;
	}

	public int getNumOfCompute() {
		return numOfCompute;
	}

	public void setNumOfCompute(int numOfCompute) {
		Parameters.numOfCompute = numOfCompute;
	}

	public int getNumOfAccelCompute() {
		return numOfAccelCompute;
	}

	public void setNumOfAccelCompute(int numOfAccelCompute) {
		Parameters.numOfAccelCompute = numOfAccelCompute;
	}

	public double getNegligeableMass() {
		return negligeableMass;
	}

	public void setNegligeableMass(double negligeableMass) {
		Parameters.negligeableMass = negligeableMass;
	}

	public double getCycleComputeTime() {
		return cycleComputeTime;
	}

	public void setCycleComputeTime(double cycleComputeTime) {
		Parameters.cycleComputeTime = cycleComputeTime;
	}

	public double getLimitComputeTime() {
		return limitComputeTime;
	}

	public void setLimitComputeTime(double limitComputeTime) {
		Parameters.limitComputeTime = limitComputeTime;
	}

	public double getMoveComputeTime() {
		return moveComputeTime;
	}

	public void setMoveComputeTime(double moveComputeTime) {
		Parameters.moveComputeTime = moveComputeTime;
	}

	public double getBarnesHuttComputeTime() {
		return barnesHuttComputeTime;
	}

	public void setBarnesHuttComputeTime(double barnesHuttComputeTime) {
		Parameters.barnesHuttComputeTime = barnesHuttComputeTime;
	}

	public double getKlength() {
		return klength;
	}

	public void setKlength(double klength) {
		Parameters.klength = klength;
	}

	public double getPlength() {
		return plength;
	}

	public void setPlength(double plength) {
		Parameters.plength = plength;
	}

	public int getNumOfLowMassParticule() {
		return numOfLowMassParticule;
	}

	public void setNumOfLowMassParticule(int numOfLowMassParticule) {
		Parameters.numOfLowMassParticule = numOfLowMassParticule;
	}

	public double getLowMassParticuleMass() {
		return lowMassParticuleMass;
	}

	public void setLowMassParticuleMass(double lowMassParticuleMass) {
		Parameters.lowMassParticuleMass = lowMassParticuleMass;
	}

	public double getLowMassDensity() {
		return lowMassDensity;
	}

	public void setLowMassDensity(double lowMassDensity) {
		Parameters.lowMassDensity = lowMassDensity;
	}

	public double getEllipseShiftRatio() {
		return ellipseShiftRatio;
	}

	public void setEllipseShiftRatio(double ellipseShiftRatio) {
		Parameters.ellipseShiftRatio = ellipseShiftRatio;
	}

	public double getEllipseRatio() {
		return ellipseRatio;
	}

	public void setEllipseRatio(double ellipseRatio) {
		Parameters.ellipseRatio = ellipseRatio;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getDemiDistanceBetweenGalaxies() {
		return demiDistanceBetweenGalaxies;
	}

	public void setDemiDistanceBetweenGalaxies(
			Vector3d demiDistanceBetweenGalaxies) {
		Parameters.demiDistanceBetweenGalaxies = demiDistanceBetweenGalaxies;
	}

	public int getNbARms() {
		return nbARms;
	}

	public void setNbARms(int nbARms) {
		Parameters.nbARms = nbARms;
	}

	public boolean isExportData() {
		return exportData;
	}

	public void setExportData(boolean exportData) {
		Parameters.exportData = exportData;
	}

	public boolean isPlayData() {
		return playData;
	}

	public void setPlayData(boolean playData) {
		Parameters.playData = playData;
	}

	public boolean isParallelization() {
		return parallelization;
	}

	public void setParallelization(boolean parallelization) {
		Parameters.parallelization = parallelization;
	}

	public boolean isStaticDarkMatter() {
		return staticDarkMatter;
	}

	public void setStaticDarkMatter(boolean staticDarkMatter) {
		Parameters.staticDarkMatter = staticDarkMatter;
	}

	public double getMatterDistribution() {
		return matterDistribution;
	}

	public void setMatterDistribution(double matterDistribution) {
		Parameters.matterDistribution = matterDistribution;
	}

	public double getGasDistribution() {
		return gasDistribution;
	}

	public void setGasDistribution(double gasDistribution) {
		Parameters.gasDistribution = gasDistribution;
	}

	public double getDarkMatterDistribution() {
		return darkMatterDistribution;
	}

	public void setDarkMatterDistribution(double darkMatterDistribution) {
		Parameters.darkMatterDistribution = darkMatterDistribution;
	}

	public double getDarkMatterNubulaFactor() {
		return darkMatterNubulaFactor;
	}

	public void setDarkMatterNubulaFactor(double darkMatterNubulaFactor) {
		Parameters.darkMatterNubulaFactor = darkMatterNubulaFactor;
	}

	public double getMatterViscosity() {
		return matterViscosity;
	}

	public void setMatterViscosity(double matterViscosity) {
		Parameters.matterViscosity = matterViscosity;
	}

	public double getGasViscosity() {
		return gasViscosity;
	}

	public void setGasViscosity(double gasViscosity) {
		Parameters.gasViscosity = gasViscosity;
	}

	public double getDarkMatterViscosity() {
		return darkMatterViscosity;
	}

	public void setDarkMatterViscosity(double darkMatterViscosity) {
		Parameters.darkMatterViscosity = darkMatterViscosity;
	}

	public double getCollisionDistanceRatio() {
		return collisionDistanceRatio;
	}

	public void setCollisionDistanceRatio(double collisionDistanceRatio) {
		Parameters.collisionDistanceRatio = collisionDistanceRatio;
	}

	public double getViscoElasticity() {
		return viscoElasticity;
	}

	public void setViscoElasticity(double viscoElasticity) {
		Parameters.viscoElasticity = viscoElasticity;
	}

	public double getViscoElasticityNear() {
		return viscoElasticityNear;
	}

	public void setViscoElasticityNear(double viscoElasticityNear) {
		Parameters.viscoElasticityNear = viscoElasticityNear;
	}

	public double getPressureZero() {
		return pressureZero;
	}

	public void setPressureZero(double pressureZero) {
		Parameters.pressureZero = pressureZero;
	}

	@XmlJavaTypeAdapter(Vector3dAdapter.class)
	@XmlElement
	public Vector3d getDarkMatterXYZRatio() {
		return darkMatterXYZRatio;
	}

	public void setDarkMatterXYZRatio(Vector3d darkMatterXYZRatio) {
		Parameters.darkMatterXYZRatio = darkMatterXYZRatio;
	}

	public boolean isRecoverFrictionEnegy() {
		return recoverFrictionEnegy;
	}

	public void setRecoverFrictionEnegy(boolean recoverFrictionEnegy) {
		Parameters.recoverFrictionEnegy = recoverFrictionEnegy;
	}

	public double getRecoverFrictionEnergyRatio() {
		return recoverFrictionEnergyRatio;
	}

	public void setRecoverFrictionEnergyRatio(double recoverFrictionEnergyRatio) {
		Parameters.recoverFrictionEnergyRatio = recoverFrictionEnergyRatio;
	}

	/**
	 * @return the expansionUnivers
	 */
	public boolean isExpansionUnivers() {
		return expansionUnivers;
	}

	/**
	 * @param expansionUnivers the expansionUnivers to set
	 */
	public void setExpansionUnivers(boolean expansionUnivers) {
		Parameters.expansionUnivers = expansionUnivers;
	}

	/**
	 * @return the timeMultiplicator
	 */
	public double getTimeMultiplicator() {
		return timeMultiplicator;
	}

	/**
	 * @param timeMultiplicator the timeMultiplicator to set
	 */
	public void setTimeMultiplicator(double timeMultiplicator) {
		Parameters.timeMultiplicator = timeMultiplicator;
	}

	/**
	 * @return the matterRendererExtender
	 */
	public double getMatterRendererExtender() {
		return matterRendererExtender;
	}

	/**
	 * @param matterRendererExtender the matterRendererExtender to set
	 */
	public void setMatterRendererExtender(double matterRendererExtender) {
		Parameters.matterRendererExtender = matterRendererExtender;
	}

	/**
	 * @return the gasRendererExtender
	 */
	public double getGasRendererExtender() {
		return gasRendererExtender;
	}

	/**
	 * @param gasRendererExtender the gasRendererExtender to set
	 */
	public void setGasRendererExtender(double gasRendererExtender) {
		Parameters.gasRendererExtender = gasRendererExtender;
	}

	/**
	 * @return the darkMatterRendererExtender
	 */
	public double getDarkMatterRendererExtender() {
		return darkMatterRendererExtender;
	}

	/**
	 * @param darkMatterRendererExtender the darkMatterRendererExtender to set
	 */
	public void setDarkMatterRendererExtender(double darkMatterRendererExtender) {
		Parameters.darkMatterRendererExtender = darkMatterRendererExtender;
	}

}
