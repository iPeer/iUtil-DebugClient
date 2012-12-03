package com.simple.sjge.engine;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.simple.sjge.gui.Gui;
import com.simple.sjge.gui.GuiConsole;

public class iMouseWheelListener implements MouseWheelListener {
	
	protected Engine engine;

	public iMouseWheelListener(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Gui gui = engine.currentGui;
		if (gui != null)
			gui.mouseWheelScrolled(e.getWheelRotation());
//		if (gui instanceof GuiConsole)
//			Engine.guiConsole.historyOffset += e.getWheelRotation();
	}

}
