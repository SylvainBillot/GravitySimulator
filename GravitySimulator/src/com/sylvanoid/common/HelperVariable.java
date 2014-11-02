package com.sylvanoid.common;

public abstract class HelperVariable {
	/*
	 * Constante universelle de gravitation : 6,67.10-11 N.m2.Kg-2
	 */
	public static final double GRAVITY = 6.67384 * Math.pow(10, -11);
	public static final long MINSLEEPTIME = 10;
	
	public static boolean manageImpact = true;
	public static TypeOfUnivers typeOfUnivers = TypeOfUnivers.Random;
	public static boolean stepByStep = false;
	public static boolean traceCourbe = false;
	public static double scala = 1;
	public static double timeFactor = 1;
	public static double probFusion = 1; // 0 = 100% choc  1 = 100% fusion
	public static double typeOfImpact = 0.2; // 1=collision elastique 0=collision parfaitement inélastique
	public static boolean timeFactorChangeX10 = false;
	public static boolean timeFactorChangeDiv10 = false;
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
