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
		Vector3d diffLookAt = new Vector3d(guiProgram.getParameters()
				.getLookAt());
		;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_PAGE_UP:
			guiProgram.getParameters().getLookAt().scale(1.05);
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			guiProgram.getParameters().getLookAt().scale(1 / 1.05);
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_LEFT:
			guiProgram.getParameters().setLookAt(
					HelperVector.rotate(guiProgram.getParameters().getLookAt(),
							new Vector3d(0, 1, 0), -theta));
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_RIGHT:
			guiProgram.getParameters().setLookAt(
					HelperVector.rotate(guiProgram.getParameters().getLookAt(),
							new Vector3d(0, 1, 0), theta));
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_UP:
			guiProgram.getParameters().setLookAt(
					HelperVector.rotate(guiProgram.getParameters().getLookAt(),
							new Vector3d(1, 0, 0), -theta));
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_DOWN:
			guiProgram.getParameters().setLookAt(
					HelperVector.rotate(guiProgram.getParameters().getLookAt(),
							new Vector3d(1, 0, 0), theta));
			diffLookAt.sub(guiProgram.getParameters().getLookAt());
			guiProgram.getParameters().getEyes().add(diffLookAt);
			break;
		case KeyEvent.VK_HOME:
			guiProgram.getParameters().setEyes(new Vector3d(0, 0, 900));
			guiProgram.getParameters().setLookAt(new Vector3d(0.001, 0, -900));
			break;
		}

		switch (e.getKeyChar()) {
		case '+':
			guiProgram.getParameters().setTimeFactor(
					guiProgram.getParameters().getTimeFactor() * 1.01);
			break;
		case '-':
			guiProgram.getParameters().setTimeFactor(
					guiProgram.getParameters().getTimeFactor() / 1.01);
			break;

		}
	}

}
