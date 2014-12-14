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
		case KeyEvent.VK_NUMPAD0:
			guiProgram.setEyes(new Vector3d(0, 0, 900));
			break;
		case KeyEvent.VK_NUMPAD1:
			guiProgram.setEyes(new Vector3d(0.0000000001, 900, 0));
			break;
		case KeyEvent.VK_NUMPAD2:
			guiProgram.setEyes(new Vector3d(900, 0, 0));
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
