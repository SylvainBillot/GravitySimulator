package com.sylvanoid.joblib;
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
				guiProgram.getUnivers().resetAcceleration();
				guiProgram.getUnivers().computeLimits();
				guiProgram.getUnivers().compute();
				guiProgram.getUnivers().move();
				guiProgram.getGraphicZone().repaint();
				long executionTime = start - System.currentTimeMillis();
				if (executionTime < HelperVariable.MINSLEEPTIME) {
					Thread.sleep(HelperVariable.MINSLEEPTIME - executionTime);
				}
				
				executionTime = start - System.currentTimeMillis();
				guiProgram.getStatus().setText("Number of object: "
								+ guiProgram.getUnivers().getListMatiere()
										.size() + " cps: "
								+ (1000 / -executionTime));
				
				if(HelperVariable.stepByStep){
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block

		}
	}
}
