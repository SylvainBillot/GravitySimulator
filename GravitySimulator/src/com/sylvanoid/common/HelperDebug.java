package com.sylvanoid.common;

import java.util.Date;

public class HelperDebug {

	private static int debug = 1;

	public static void info(String msg) {
		if (debug > 0) {
			Date date = new Date();
			System.out.println(date + ": " + msg);
		}
	}
}
