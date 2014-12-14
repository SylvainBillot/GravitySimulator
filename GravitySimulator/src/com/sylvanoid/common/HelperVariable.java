package com.sylvanoid.common;

public abstract class HelperVariable {
	/*
	 * Constante universelle de gravitation : 6,67.10-11 N.m2.Kg-2 C : 299 792
	 * 458 m/s
	 */
	public static final double GRAVITY = 6.67234 * Math.pow(10, -11);
	public static final double C = 299792458;
	public static final long MINSLEEPTIME = 10;

	//public static double density = 30;
	public static int numberOfObjects = 1000;
	public static double nebulaRadius = 600;
	public static double massObjectMin = 10000;
	public static double massObjectMax = 100000;
}
