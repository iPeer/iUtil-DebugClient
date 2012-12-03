package com.simple.sjge.gui.controls;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gui.Gui;

@SuppressWarnings("unused")
public class GuiListSelectableBox extends Gui {
	//TODO: Finish!
	private int min = 0;
	private int max = 1;
	private String[] options;
	private List<GuiListBoxItem> items = new ArrayList<GuiListBoxItem>();
	private boolean enabled = true;
	private int renderFrom = 0;
	public boolean isMouseOver = false;
	private int yStep = 17;

	private Colour BOX_COLOUR = Colour.GREY;
	private Colour DISABLED_COLOUR = Colour.LIGHT_GREY;
	private Colour TEXT_COLOUR = Colour.WHITE;
	
	public String selectedName = "";
	public int selected = -1;

	private int x, y, w, h;

	public GuiListSelectableBox(int x, int y, int w, int h) {
		this(x, y, w, h, null);
	}

	public GuiListSelectableBox(int x, int y, String[] options) {
		this(x, y, 150, 90, options);		
	}

	public GuiListSelectableBox(int x, int y, int w, int h, String[] options) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.max = options.length;
		this.options = options;
		setOptions(options);
	}

	public void tick() {
		for (GuiListBoxItem a : items)
			a.tick();
	}

	public void render() {
		Graphics2D g = Engine.getGraphicsInstance();
		g.setColor(BOX_COLOUR);
		g.drawRect(x, y, w, h);
		if (!enabled) {
			g.setColor(DISABLED_COLOUR);
			g.fillRect(x + 1, y + 1, w - 2, h - 2);
		}
		int x1 = this.x + 2;
		int y1 = (this.y - yStep) + 3;
		for (int x = renderFrom; x < this.items.size(); x++) {
			GuiListBoxItem a = items.get(x);
			if (((y1 + yStep) + a.h) < (this.y + this.h))
				a.render(x1, y1+=yStep);
		}
		//		for (GuiListBoxSlot a : this.items)
		//			a.render();
		if (Engine.DEBUG_ENABLED) {
			g.setColor(TEXT_COLOUR);
			g.drawString("L: "+this.options.length+"/"+this.items.size(), this.x, this.y + this.h + 12);
		}
		super.render();
	}

	public void setOpts(String[] newOpts) {
		setOptions(newOpts);
	}

	public void setOptions(String[] newOpts) {
		this.max = newOpts.length;
		if (this.selected > this.max)
			this.selected = 0;
		this.options = newOpts;
		this.items.clear();
		int ypos = (this.y - 17) + 3;
		for (String a : this.options) {
			this.items.add(new GuiListBoxItem(this.x + 2, ypos+=17, this.w - 4, 14, a));
		}
	}


	public boolean mousePressed(int x, int y) {
		return enabled && x > this.x && x < (this.x + this.w) && y < (this.y + this.h) && y > this.y;
	}

	public void setOpts(List<String> newOpts) {
		setOptions(newOpts);
	}

	public void setOptions(List<String> newOpts) {
		String[] a = new String[newOpts.size()];
		newOpts.toArray(a);
		setOptions(a);
	}

	public void addOpt(String opt) {
		addOption(opt);
	}

	public void addOpt(List<String> opts) {
		addOption(opts);
	}

	public void addOption(String opt) {
		List<String> a = new ArrayList<String>();
		for (String b : this.options)
			a.add(b);
		a.add(opt);
		String[] c = new String[a.size()];
		a.toArray(c);
		setOptions(c);

	}

	public void addOption(List<String> opts) {
		for (String a : opts)
			addOption(a);

	}

	public void addOptions(List<String> opts) {
		addOption(opts);
	}

	public void checkForMouseOnList(int i, int j) {
		for (int l = 0; l < items.size(); l++) {
			GuiListBoxItem b = items.get(l);
			if (b.mousePressed(i, (j + (yStep * renderFrom))))
				b.isMouseOver = true;
			else
				b.isMouseOver = false;
		}
	}

	public boolean isMouseOver() {
		return isMouseOver;
	}

	public void mouseWheelScrolled(int i) {
		if (isMouseOver()) {
			renderFrom += i;
			if (renderFrom < 0)
				renderFrom = 0;
			if (renderFrom > items.size() - 1)
				renderFrom = items.size() - 1;
		}
	}

	public void clearHighlighted() {
		for (GuiListBoxItem a : items)
			a.isMouseOver = false;
	}
	
	public void clearSelected() {
		this.selected = -1;
		this.selectedName = "";
		for (GuiListBoxItem a : items)
			a.deselect();
	}

	public void setItemColours(Colour BOX, Colour HIGHLIGHT, Colour TEXT) {
		for (GuiListBoxItem a : items) {
			a.setTextColour(TEXT);
			a.setHighlightColour(HIGHLIGHT);
			a.setBoxColour(BOX);
		}
	}

	public void setDisabledColour(Colour c) {
		this.DISABLED_COLOUR = c;
	}

	public void setBoxColour(Colour c) {
		this.BOX_COLOUR = c;
	}

	public void setTextColour(Colour c) { // Only used when debug is enabled
		this.TEXT_COLOUR = c;
	}

	public void checkForClicksOnItems(int x, int y) {
		clearSelected();
		for (int l = 0; l < items.size(); l++) {
			GuiListBoxItem b = items.get(l);
			if (b.mousePressed(x, (y + (yStep * renderFrom)))) {
				this.selected = l;
				this.selectedName = b.text;
				b.select();
			}
		}
	}

}
