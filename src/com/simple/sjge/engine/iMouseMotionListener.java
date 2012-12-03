package com.simple.sjge.engine;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.simple.sjge.gui.Gui;
import com.simple.sjge.gui.controls.GuiButton;
import com.simple.sjge.gui.controls.GuiListSelectableBox;
import com.simple.sjge.gui.controls.GuiTextField;
import com.simple.sjge.level.Level;

public class iMouseMotionListener implements MouseMotionListener {

	protected Engine engine;
	
	public static int lastX, lastY;
	
	public iMouseMotionListener(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Gui gui = engine.currentGui;
		if ((gui == null && engine.level != null)) { 
			Level l = engine.level;
			int x = l.xOffset;
			x += (e.getX() - lastX);
			int y = l.yOffset;
			y += (e.getY() - lastY);
			lastX = e.getX();
			lastY = e.getY();
			l.setOffset(x, y);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void mouseMoved(MouseEvent e) {
		int i = e.getX();
		int j = e.getY();
		Engine.Mouse = new Point(i, j);
		Gui gui = engine.currentGui;
		Level level = engine.getLevel();
		if (gui != null) { 
			ArrayList controls = gui.controls;
			for (int l = 0; l < controls.size(); l++) {
				Gui control = (Gui)controls.get(l);
				if (control instanceof GuiButton) {
					GuiButton button = (GuiButton)control;
					if (button.enabled && i > button.x && i < (button.x + button.w) && j > button.y && j < (button.y + button.h)) {
						button.isMouseOver = true;
					}
					else {
						button.isMouseOver = false;
					}
				}
				else if (control instanceof GuiTextField) {
					
				}
				else if (control instanceof GuiListSelectableBox) {
					GuiListSelectableBox a = (GuiListSelectableBox)control;
					if (a.mousePressed(i, j)) {
						a.isMouseOver = true;
						a.checkForMouseOnList(i, j);
					}
					else {
						a.clearHighlighted();
						a.isMouseOver = false;
					}
				}
			}
		}
//		else if (gui == null && !level.entities.isEmpty()) {
//			
//		}
	}

}
