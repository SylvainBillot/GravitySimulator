package com.sylvanoid.common;

import java.util.Date;

public class HelperDebug {

	private static int debug = 0;
	
	public static void info(String msg) {
		if(debug>0) {
			Date date = new Date();
			System.out.println(date + ": " + msg);
		}
	}
}
