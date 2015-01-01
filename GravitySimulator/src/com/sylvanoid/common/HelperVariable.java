package com.sylvanoid.common;

public abstract class HelperVariable {
	public static final double G = 6.67234E-11; // N.m2 kg-1 s-2
	public static final double C = 299792458; // m s-1
	public static final double M = 1.9884E30; // kg
	public static final double PC = 3.08567758E16; // m
	public static final double UA = 1.49597870E11; // m
	public static final double ONEDAY = 60 * 60 * 24; // s
	public static final double MINIMALSTARMASS = 0.07 * HelperVariable.M; // kg
	public static final double MAXIMALSTARMASS = 300 * HelperVariable.M; // kg
	public static final double H0 = (67.8 *1E3) /( HelperVariable.PC * 1E6); // m s-1

}
