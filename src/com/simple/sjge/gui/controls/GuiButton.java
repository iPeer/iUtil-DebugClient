package com.simple.sjge.gui.controls;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gui.Gui;

public class GuiButton extends Gui {

	private int NORMAL_COLOUR = 0xff0000;
	private int HOVER_COLOUR = 0x0000ff;
	private float TRANSPARENCY = 1.0f;
	public String text;
	public int x, y, w, h, id;
	public boolean enabled = true;
	public boolean isMouseOver = false;

	public GuiButton(int id, int x, int y, String text) {
		this(id, x, y, 200, 30, text);
	}

	public GuiButton(int id, int x, int y, int w, int h, String text) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
	}

	public void tick() {
		super.tick();
	}

	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		Colour c = new Colour(NORMAL_COLOUR);
		if (isMouseOver())
			c = new Colour(HOVER_COLOUR);
		g.setColor(c);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.TRANSPARENCY));
		g.fill3DRect(this.x, this.y, this.w, this.h, enabled);
		g.setColor(Colour.BLACK);
		FontMetrics fm = g.getFontMetrics();
		int a = fm.stringWidth(this.text);
		//g.drawString(this.text, this.x + (this.w - a) / 2, this.y + 9);
		drawStringWithShadow(this.text, (this.x + (this.w - a) / 2), (this.y + 4 + this.h / 2), fm.getFont().getSize());
		if (this.TRANSPARENCY != 1.0f)
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
	}

	public boolean mousePressed(int x, int y) {
		return enabled && x > this.x && x < (this.x + this.w) && y < (this.y + this.h) && y > this.y;
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}
	
	public void setOpacity(float f) {
		this.TRANSPARENCY = f;
	}
	
	@Deprecated
	public void setTransparency(float f) {
		setOpacity(f);
	}
	
	public void setColours(Colour NORMAL, Colour HOVER) {
		setColours(NORMAL.getRGB(), HOVER.getRGB());
	}
	
	public void setColours(Color NORMAL, Color HOVER) {
		setColours(NORMAL.getRGB(), HOVER.getRGB());
	}
	
	public void setColours(int NORMAL, int HOVER) {
		this.NORMAL_COLOUR = NORMAL;
		this.HOVER_COLOUR = HOVER;
	}
	
	public void setColours(int[] colours) {
		this.NORMAL_COLOUR = colours[0];
		this.HOVER_COLOUR = colours[1];
	}
	
	public int[] getColours() {
		int[] a = {this.NORMAL_COLOUR, this.HOVER_COLOUR};
		return a;
	}

	public float getOpacity() {
		return this.TRANSPARENCY;
	}
	
	@Deprecated
	public float getTransparency() {
		return getOpacity();
	}

}
