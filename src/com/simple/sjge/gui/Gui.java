package com.simple.sjge.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gui.controls.GuiButton;
import com.simple.sjge.gui.controls.GuiListSelectableBox;

@SuppressWarnings("rawtypes")
public class Gui {

	protected Engine engine;
	public ArrayList controls;
	public String title;
	
	public Gui() {
		controls = new ArrayList();
		title = "Some GUI";
		this.engine = Engine.getInstance();
	}
	
	public void render() {
		for (int c = 0; c < controls.size(); c++) {
			((Gui)controls.get(c)).render();
		}
	}
	
	public void tick() { 
		for (int c = 0; c < controls.size(); c++) {
			((Gui)controls.get(c)).tick();
		}
	}
	
	public void drawStringWithShadow(String s, int x, int y) {
		drawStringWithShadow(s, x, y, 14);
	}
	
	public void drawStringWithShadow(String s, int y) {
		Graphics2D g = Engine.getGraphicsInstance();
		int x = (engine.getWidth() - g.getFontMetrics().stringWidth(s)) / 2;
		drawStringWithShadow(s, x, y, 14);
	}
	
	public void drawStringWithShadow(String s, int x, int y, Color colour) {
		drawStringWithShadow(s, x, y, 14, colour);
	}
	
	private void drawStringWithShadow(String s, int x, int y, int h, Color colour) {
		Graphics2D g = Engine.getGraphicsInstance();

		Font f = g.getFont();
		g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, h));

		g.setColor(Colour.BLACK);
		g.drawString(s, x+2, y+2);
		g.setColor(colour);
		g.drawString(s, x, y);
		g.setFont(f);
	}

	public void drawStringWithShadow(String s, int x, int y, int h) {
		drawStringWithShadow(s, x, y, h, Colour.WHITE);
	}
	
	public void renderBackground() {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(Colour.BLACK);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
		g.fillRect(0, 0, engine.getWidth(), engine.getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

	@Deprecated
	public void actionPerformed(GuiButton button) { }
	@Deprecated
	public void actionPerformedRight(GuiButton button) { }
	@Deprecated
	public void keyTyped(char keyChar, int keyCode) { }
	@Deprecated
	public void actionPerformedMiddle(GuiButton button) { }

	public boolean pausesGame() {
		return false;
	}
	public void actionPerformed(GuiButton b, int mouseButton) { }
	public void keyTyped(KeyEvent e) { }
	public void mouseWheelScrolled(int wheelRotation) {	}
	public void listClicked(GuiListSelectableBox b, int x, int y) { }
	
}
