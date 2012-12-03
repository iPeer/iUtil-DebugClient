package com.simple.sjge.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.simple.sjge.entities.AITestEntity;
import com.simple.sjge.entities.AITestEntity2;
import com.simple.sjge.entities.AITestEntity3;
import com.simple.sjge.gui.Gui;
import com.simple.sjge.gui.controls.GuiButton;
import com.simple.sjge.gui.controls.GuiListSelectableBox;
import com.simple.sjge.gui.controls.GuiTextField;
import com.simple.sjge.level.TestLevel;

public class iMouseListener implements MouseListener {

	protected Engine engine;

	public iMouseListener(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int i = e.getX();
		int j = e.getY();
		Gui gui = engine.currentGui;
		if (gui == null && engine.getLevel() instanceof TestLevel) {
			if (e.getButton() == 3)
				engine.getLevel().queueEntity(new AITestEntity2(i - engine.getLevel().xOffset, j - engine.getLevel().yOffset, 16, 16, engine.getLevel()));
			else if (e.getButton() == 1)
				engine.getLevel().queueEntity(new AITestEntity(i - engine.getLevel().xOffset, j - engine.getLevel().yOffset, 16, 16, engine.getLevel()));
			else
				engine.getLevel().queueEntity(new AITestEntity3(i - engine.getLevel().xOffset, j - engine.getLevel().yOffset, 16, 16, engine.getLevel()));
		}
		if (gui != null) { 
			@SuppressWarnings("unchecked")
			ArrayList<GuiButton> controls = gui.controls;
			for (int l = 0; l < controls.size(); l++) {
				Object a = controls.get(l);
				if (a instanceof GuiButton) {
					GuiButton button = (GuiButton)a;
					if (button.mousePressed(i, j))
						gui.actionPerformed(button, e.getButton()); // [Roxy] Enables the button to take right click input

				}
				else if (a instanceof GuiTextField) {
					GuiTextField b = (GuiTextField)a;
					b.mousePressed(i, j);
				}
				else if (a instanceof GuiListSelectableBox) {
					GuiListSelectableBox b = (GuiListSelectableBox)a;
					if (b.mousePressed(i, j)) {
						b.checkForClicksOnItems(i, j);
						gui.listClicked(b,  i,  j);
					}
					
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {


	}

	@Override
	public void mouseExited(MouseEvent arg0) {


	}

	@Override
	public void mousePressed(MouseEvent e) {
		Gui gui = engine.currentGui;
		if ((gui == null && engine.level != null)) { 
			iMouseMotionListener.lastX = e.getX();
			iMouseMotionListener.lastY = e.getY();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		Gui gui = engine.currentGui;
		if ((gui == null && engine.level != null)) { 
			iMouseMotionListener.lastX = iMouseMotionListener.lastY = 0;
		}
	}

}
