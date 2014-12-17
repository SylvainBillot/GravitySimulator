package com.sylvanoid.gui;

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
		Vector3d lookAt;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_PAGE_UP:
			guiProgram.getParameters().getEyes().scale(1.05);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			guiProgram.getParameters().getEyes().scale(1 / 1.05);
			break;
		case KeyEvent.VK_LEFT:
			guiProgram.getParameters().getEyes().set(
					HelperVector.rotate(guiProgram.getParameters().getEyes(), new Vector3d(0,
							1, 0), -theta));
			break;
		case KeyEvent.VK_RIGHT:
			guiProgram.getParameters().getEyes().set(
					HelperVector.rotate(guiProgram.getParameters().getEyes(), new Vector3d(0,
							1, 0), theta));
			break;
		case KeyEvent.VK_UP:
			guiProgram.getParameters().getEyes().set(
					HelperVector.rotate(guiProgram.getParameters().getEyes(), new Vector3d(1,
							0, 0), -theta));
			break;
		case KeyEvent.VK_DOWN:
			guiProgram.getParameters().getEyes().set(
					HelperVector.rotate(guiProgram.getParameters().getEyes(), new Vector3d(1,
							0, 0), theta));
			break;
		case KeyEvent.VK_HOME:
			lookAt = new Vector3d(guiProgram.getParameters().getCenterOfVision());
			lookAt.add(new Vector3d(0,0,900));
			guiProgram.getParameters().setEyes(lookAt);
			break;
		}

		switch (e.getKeyChar()) {
		case '+':
			guiProgram.getParameters().setTimeFactor(guiProgram.getParameters().getTimeFactor()*1.01);
			break;
		case '-':
			guiProgram.getParameters().setTimeFactor(guiProgram.getParameters().getTimeFactor()/1.01);
			break;

		}
	}

}
