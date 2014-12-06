package com.sylvanoid.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class InputHandler extends KeyAdapter {

    public InputHandler(GUIProgram guiProgram) {
    	
    }
    
    public void keyPressed(KeyEvent e) {
        // Set flags
    	processKeyEvent(e, true);
    }
    
    private void processKeyEvent(KeyEvent e, boolean pressed) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                
                break;
            case KeyEvent.VK_RIGHT:
                
                break;
            case KeyEvent.VK_UP:
                
                break;
            case KeyEvent.VK_DOWN:
                
                break;
        }
    }
}
