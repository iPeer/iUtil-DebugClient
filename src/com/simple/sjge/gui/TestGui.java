package com.simple.sjge.gui;

import java.awt.event.KeyEvent;

import com.simple.sjge.engine.Keyboard;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gui.controls.GuiButton;
import com.simple.sjge.gui.controls.GuiListSelectableBox;
import com.simple.sjge.gui.controls.GuiTextField;
import com.simple.sjge.util.Debug;

public class TestGui extends Gui {
	
	private GuiTextField uneditable;
	private GuiButton transparentDynamic, transparent100, transparent75, transparent50, transparent25;
	boolean inc = false;
	private GuiListSelectableBox guiListBox;

	@SuppressWarnings("unchecked")
	public TestGui() {
		title = "Test GUI!";
		controls.clear();
		controls.add(transparent100 = new GuiButton(0, 60, 85, 100, 20, "100%"));
		controls.add(new GuiTextField(60, 35, 200, 20, "Editable Text Field"));
		controls.add(uneditable = new GuiTextField(60, 60, 200, 20, "Uneditable Text Field"));
		uneditable.isEnabled = false;
		controls.add(transparent75 = new GuiButton(1, 165, 85, 100, 20, "75%"));
		transparent75.setOpacity(0.75f);
		transparent75.setColours(Colour.GREY.getRGB(), 0x999999);
		controls.add(transparent50 = new GuiButton(2, 270, 85, 100, 20, "50%"));
		transparent50.setColours(transparent75.getColours());
		transparent50.setOpacity(0.50f);
		controls.add(transparent25 = new GuiButton(3, 375, 85, 100, 20, "25%"));
		transparent25.setColours(transparent75.getColours());
		transparent25.setOpacity(0.25f);
		transparent100.setColours(transparent75.getColours());
		controls.add(transparentDynamic = new GuiButton(4, 480, 85, 100, 20, "100-0%"));
		transparentDynamic.setColours(transparent75.getColours());
		String[] a = {"Test1", "Test2", "Test3", "test4", "test5", "test6", "test7"};
		controls.add(guiListBox = new GuiListSelectableBox(60, 110, a));
	}
	
	public void tick() {
		if (!inc)
			transparentDynamic.setOpacity(transparentDynamic.getOpacity() - 0.03f);
		else
			transparentDynamic.setOpacity(transparentDynamic.getOpacity() + 0.03f);
		if (transparentDynamic.getOpacity() == 1.0f || transparentDynamic.getOpacity() <= 0.03f)
			inc = !inc;
		super.tick();
	}
	
	public void actionPerformed(GuiButton b) {
			Debug.p(b.id+": left");
	}
	public void actionPerformedMiddle(GuiButton b) {
			Debug.p(b.id+": middle");
	}
	
	public void actionPerformed(GuiButton b, int mouseButton) {
		if (mouseButton == 1)
			Debug.p(b.id+": left");
		else if (mouseButton == 2)
			Debug.p(b.id+": middle");
		else if (mouseButton == 3)
			Debug.p(b.id+": right");
		else
			Debug.p(b.id+": unknown button");
	}
	
	@Deprecated
	public void actionPerformedRight(GuiButton b) { // [Roxy] Handles right clicks for this GUI.
			Debug.p(b.id+": right");
	}
	
	@SuppressWarnings("unused")
	public void keyTyped(KeyEvent e) {
		int i = e.getKeyCode();
		char c = e.getKeyChar();
		if (i == Keyboard.VK_ESCAPE)
			engine.setGui(null);
		for (Object d : controls) {
			if (d instanceof GuiTextField)
				if (((GuiTextField)d).isFocused)
					((GuiTextField)d).keyTyped(e);
		}
		super.keyTyped(e);
	}
	
	public void render() {
		renderBackground();
		drawStringWithShadow(title, 30);
		super.render();
	}
	
	public void mouseWheelScrolled(int i) {
		if (guiListBox.isMouseOver())
			guiListBox.mouseWheelScrolled(i);
	}
	
	public void listClicked(GuiListSelectableBox b, int x1, int y1) {
		System.err.println(b.selectedName+", "+b.selected);
	}
	
	public boolean pausesGame() {
		return true;
	}
	
}
