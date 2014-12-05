package com.sylvanoid.joblib;

import java.awt.image.BufferedImage;
import java.io.IOException;
import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.gui.GUIProgram;

public class SimpleThread implements Runnable {

	private GUIProgram guiProgram;

	public SimpleThread(GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
	}

	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				long start = System.currentTimeMillis();
				guiProgram.getUnivers().process();
				guiProgram.getGraphicZone().repaint();

				if (HelperVariable.exportToVideo) {
					BufferedImage img = new BufferedImage(guiProgram
							.getGraphicZone().getWidth(), guiProgram
							.getGraphicZone().getHeight(),
							BufferedImage.TYPE_USHORT_555_RGB);
					guiProgram.getGraphicZone().paint(img.getGraphics());
					try {
						guiProgram.getOut().encodeImage(img);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				long executionTime = start - System.currentTimeMillis();
				if (executionTime < HelperVariable.MINSLEEPTIME) {
					Thread.sleep(HelperVariable.MINSLEEPTIME - executionTime);
				}

				if (HelperVariable.stepByStep) {
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block

		}
	}
}
