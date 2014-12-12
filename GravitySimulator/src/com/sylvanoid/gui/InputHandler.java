package com.sylvanoid.gui;

import com.sylvanoid.common.HelperVariable;
import com.sylvanoid.common.HelperVector;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.vecmath.Vector3d;

public class InputHandler extends KeyAdapter {
	private GUIProgram guiProgram;

	private double theta = Math.PI / 180;

	public InputHandler(GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
	}

	public void keyPressed(KeyEvent e) {
		// Set flags
		processKeyEvent(e, true);
	}

	private void processKeyEvent(KeyEvent e, boolean pressed) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_PAGE_UP:
			guiProgram.getEyes().scale(1.01);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			guiProgram.getEyes().scale(1 / 1.01);
			break;
		case KeyEvent.VK_LEFT:
			guiProgram.getEyes().set(
					HelperVector.rotate(guiProgram.getEyes(), new Vector3d(0,
							1, 0), -theta));
			break;
		case KeyEvent.VK_RIGHT:
			guiProgram.getEyes().set(
					HelperVector.rotate(guiProgram.getEyes(), new Vector3d(0,
							1, 0), theta));
			break;
		case KeyEvent.VK_UP:
			guiProgram.getEyes().set(
					HelperVector.rotate(guiProgram.getEyes(), new Vector3d(1,
							0, 0), -theta));
			break;
		case KeyEvent.VK_DOWN:
			guiProgram.getEyes().set(
					HelperVector.rotate(guiProgram.getEyes(), new Vector3d(1,
							0, 0), theta));
			break;
		case KeyEvent.VK_HOME:
			guiProgram.setEyes(new Vector3d(0, 0, 900));
			break;

		case KeyEvent.VK_NUMPAD1:
			HelperVariable.timeFactor *= 1.1;
			break;

		case KeyEvent.VK_NUMPAD2:
			HelperVariable.timeFactor *= 1 / 1.1;
			break;

		}
	}

}
