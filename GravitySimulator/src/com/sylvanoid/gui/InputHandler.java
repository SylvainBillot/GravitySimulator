package com.sylvanoid.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

public class InputHandler extends KeyAdapter {
	private GUIProgram guiProgram;

	private double theta = Math.PI / 128;

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
			guiProgram.getEyes().scale(1.05);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			guiProgram.getEyes().scale(1 / 1.05);
			break;
		case KeyEvent.VK_LEFT:
			guiProgram.getEyes().set(
					rotate(guiProgram.getEyes(), new Vector3d(0, 1, 0), theta));
			break;
		case KeyEvent.VK_RIGHT:
			guiProgram.getEyes()
					.set(rotate(guiProgram.getEyes(), new Vector3d(0, 1, 0),
							-theta));
			break;
		case KeyEvent.VK_UP:
			guiProgram.getEyes().set(
					rotate(guiProgram.getEyes(), new Vector3d(1, 0, 0), theta));
			break;
		case KeyEvent.VK_DOWN:
			guiProgram.getEyes()
					.set(rotate(guiProgram.getEyes(), new Vector3d(1, 0, 0),
							-theta));
			break;
		}
	}

	private static Vector3d rotate(Vector3d vector, Vector3d axis, double angle) {
		Matrix3d rotate = new Matrix3d();
		rotate.set(new AxisAngle4d(axis, angle));
		Vector3d result = new Vector3d();
		rotate.transform(vector, result);
		return result;
	}
}
