package com.sylvanoid.common;

public abstract class HelperVariable {
	/*
	 * Constante universelle de gravitation : 6,67.10-11 N.m2.Kg-2 C : 299 792
	 * 458 m/s
	 */
	public static final double GRAVITY = 6.67234 * Math.pow(10, -11);
	public static final double C = 299792458;
	public static final long MINSLEEPTIME = 10;

	//public static TypeOfUnivers typeOfUnivers = TypeOfUnivers.Planetary;
	//public static double timeFactor = 5;
	//public static double darkMatterMass = 1E12;
	public static double darkMatterDensity = 1E10;
	public static boolean manageImpact = false;
	public static boolean exportToVideo = false;
	public static boolean fusion = true;
	// 1=collision elastique 0=collision parfaitement inélastique
	public static double typeOfImpact = 1;
	public static boolean centerOnCentroid = false;
	public static boolean centerOnMassMax = false;
	public static boolean centerOnScreen = false;
	public static double dentityMin = 30;
	public static double densityMax = 30;
	public static int numberOfObjects = 1000;
	public static double nebulaRadius = 600;
	public static double massObjectMin = 10000;
	public static double massObjectMax = 100000;
}
