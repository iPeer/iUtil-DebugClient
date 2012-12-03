package com.simple.sjge.engine;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class iComponentListener implements ComponentListener {
	
	protected Engine engine;

	public iComponentListener(Engine engine) {
		this.engine = engine;
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {


	}

	@Override
	public void componentMoved(ComponentEvent arg0) {


	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {


	}

}
