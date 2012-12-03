package com.simple.sjge.gui.controls;

import java.awt.AlphaComposite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gui.Gui;

public class GuiListBoxItem extends Gui {
	
	public int x, y, w, h;
	public String text;
	private boolean selected = false;
	private boolean enabled = true;
	private float opacity = 1.0f;
	private boolean up = false;
	
	private Colour BOX_COLOUR = Colour.GREY;
	private Colour HIGHLIGHT_COLOUR = Colour.LIGHT_GREY;
	private Colour TEXT_COLOUR = Colour.WHITE;
	
	public boolean isMouseOver = false;

	public GuiListBoxItem(int x, int y, String text) {
		FontMetrics a = Engine.getGraphicsInstance().getFontMetrics();
		Rectangle2D b = a.getStringBounds(text, Engine.getGraphicsInstance());
		this.x = x;
		this.y = y;
		this.w = (int)b.getWidth();
		this.h = (int)b.getHeight();
		this.text = text;
		
	}

	
	public GuiListBoxItem(int x, int y, int w, int h, String text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}
	
	public void select() {
		this.selected = true;
	}
	
	public void deselect() {
		this.selected = false;
	}
	
	public void tick() {
		if (!isMouseOver() && opacity < 1.0f)
			opacity = 1.0f;
		if (isMouseOver()) {
			if (!up)
				opacity -= 0.03f;
			else
				opacity += 0.03f;
			if (opacity > 1.0f) {
				opacity = 1.0f;
				up = false;
			}
			else if (opacity < 0.0f) {
				opacity = 0.0f;
				up = true;
			}
		}
		
	}
	
	public void render(int x, int y) {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(BOX_COLOUR);
		g.drawRect(x, y, w, h);
		if (isMouseOver()) {
			g.setColor(HIGHLIGHT_COLOUR);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
			g.fillRect(x + 1, y + 1, w - 1, h - 1);
			if (opacity < 1.0f)
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		}
		g.setColor(TEXT_COLOUR);
		g.drawString((isSelected() ? "> " : "")+this.text,  x + 3,  y + 12);
	}
	
	public boolean isMouseOver() {
		return isMouseOver;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public boolean mousePressed(int x, int y) {
		return enabled && x > this.x && x < (this.x + this.w) && y < (this.y + this.h) && y > this.y;
	}
	
	public void mouseClicked(int x, int y) {
		if (new Rectangle(this.x, this.y, this.w, this.h).contains(x, y))
			select();
		else
			deselect();
	}

	public void setTextColour(Colour c) {
		this.TEXT_COLOUR = c;
	}
	
	public void setBoxColour(Colour c) {
		this.BOX_COLOUR = c;
	}
	
	public void setHighlightColour(Colour c) {
		this.HIGHLIGHT_COLOUR = c;
	}
	
}
