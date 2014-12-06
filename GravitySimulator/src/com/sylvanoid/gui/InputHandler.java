package com.sylvanoid.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
	private GUIProgram guiProgram;

	public InputHandler(GUIProgram guiProgram) {
		this.guiProgram = guiProgram;
	}

	public void keyPressed(KeyEvent e) {
		// Set flags
		processKeyEvent(e, true);
	}

	private void processKeyEvent(KeyEvent e, boolean pressed) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			guiProgram.eyesXAdd(10);
			break;
		case KeyEvent.VK_RIGHT:
			guiProgram.eyesXAdd(-10);
			break;
		case KeyEvent.VK_PAGE_UP:
			guiProgram.eyesZAdd(10);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			guiProgram.eyesZAdd(-10);
			break;
		case KeyEvent.VK_UP:
			guiProgram.eyesYAdd(10);
			break;
		case KeyEvent.VK_DOWN:
			guiProgram.eyesYAdd(-10);
			break;
		}
	}
}
