package com.sylvanoid.gui;

import com.sylvanoid.common.HelperVector;
import com.sylvanoid.joblib.Parameters;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.vecmath.Vector3d;

public class InputHandler extends KeyAdapter {
	private Parameters parameters;

	private double theta = Math.PI / 180;

	public InputHandler(GUIProgram guiProgram) {
		this.parameters = guiProgram.getParameters();
	}

	public void keyPressed(KeyEvent e) {
		// Set flags
		processKeyEvent(e, true);
	}

	private void processKeyEvent(KeyEvent e, boolean pressed) {
		Vector3d diffLookAt = new Vector3d(parameters.getLookAt());
		;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_PAGE_UP:
			parameters.getLookAt().scale(1.05);
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			parameters.getLookAt().scale(1 / 1.05);
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_LEFT:
			parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(),
					new Vector3d(0, 1, 0), -theta));
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_RIGHT:
			parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(),
					new Vector3d(0, 1, 0), theta));
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_UP:
			parameters.setLookAt(HelperVector.rotate(parameters.getLookAt(),
					new Vector3d(1, 0, 0), -theta));
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_DOWN:
			parameters.setLookAt(
					HelperVector.rotate(parameters.getLookAt(), new Vector3d(1,
							0, 0), theta));
			diffLookAt.sub(parameters.getLookAt());
			parameters.getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_HOME:
			parameters.setEyes(new Vector3d(0, 0, 900));
			parameters.setLookAt(new Vector3d(0.001, 0, -900));
			break;
		}

		switch (e.getKeyChar()) {
		case '+':
			parameters.setTimeFactor(parameters.getTimeFactor() * 1.01);
			break;
		case '-':
			parameters.setTimeFactor(parameters.getTimeFactor() / 1.01);
			break;

		}
	}

}
