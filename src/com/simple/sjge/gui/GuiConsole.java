package com.simple.sjge.gui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.engine.Keyboard;
import com.simple.sjge.engine.console.Console;
import com.simple.sjge.engine.console.ConsoleMessage;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gfx.Screen;
import com.simple.sjge.gui.controls.GuiTextField;

public class GuiConsole extends Gui {

	protected Engine engine;
	private static final ArrayList<String> commandHistory = new ArrayList<String>();
	public final ArrayList<ConsoleMessage> messageHistory = new ArrayList<ConsoleMessage>();
	private static String text = "";
	public Console console;
	private static int listPos = 0;
	public int historyOffset = 0;
	GuiTextField consoleInput;

	@SuppressWarnings("unchecked")
	public GuiConsole(Engine engine) {
		this.engine = engine;
		this.console = new Console(engine, this);
		controls.clear();
		controls.add(consoleInput = new GuiTextField(2, ((engine.getHeight() + 100) / 2) - 22, engine.getWidth() - 4, 20, ""));
		consoleInput.isFocused = true;
	}

	public void render() {
		try {
			Graphics2D g = Engine.getGraphicsInstance();
			Screen screen = this.engine.screenInstance();
			g.setColor(Colour.DARK_GRAY);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.fillRect(0, -10, engine.getWidth(), (engine.getHeight() + 100) / 2);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			//g.setColor(Colour.WHITE);
			//g.drawLine(0, 275, engine.getWidth(), 275);
			//g.drawString(text+(((caretTick / 12) % 2 == 0) ? "|" : ""), 2, 289);
			int yPos = ((engine.getHeight() + 100) / 2 - 22) - 5;
			int x = messageHistory.size() - 1;
			if (historyOffset > -1 && messageHistory.size() > 17 && historyOffset < messageHistory.size())
				x = historyOffset;
			if (messageHistory.size() == 0)
				historyOffset = 0;
			if (historyOffset > messageHistory.size())
				historyOffset = messageHistory.size();
			if (historyOffset <= -1)
				historyOffset = x = 0;
			for (; x >= 0; x--) {
				ConsoleMessage cm = messageHistory.get(x);
				//g.setColor(cm.colour());
				screen.drawString(cm.text(), 2, yPos);
				yPos -= 16;
			}
			consoleInput.render();
		}
		catch (IndexOutOfBoundsException e) { }
	}

	public void tick() {
		if (!consoleInput.isFocused)
			consoleInput.isFocused = true;
		consoleInput.tick();
	}

	@SuppressWarnings("unused")
	public void keyTyped(KeyEvent e) {
		int i = e.getKeyCode();
		char c = e.getKeyChar();
		if (Arrays.asList(Keyboard.VK_ESCAPE, Keyboard.VK_BACK_QUOTE, Keyboard.VK_DEAD_TILDE).contains(i)) {
			engine.setGui(null);
			return;
		}
		if (i == Keyboard.VK_BACK_SPACE && text.length() > 0)
			setText(text.substring(0, text.length() - 1));
		else if(i == Keyboard.VK_UP) {
			if (listPos - 1 >= 0) {
				listPos--;
				setText(commandHistory.get(listPos), true);
			}
		}
		else if (i == Keyboard.VK_DOWN) {
			if (listPos + 1 <= commandHistory.size() - 1) {
				listPos++;
				setText(commandHistory.get(listPos), true);
			}
			else {
				listPos = commandHistory.size();
				setText("", true);
			}
		}
		else if (i == Keyboard.VK_ENTER) {
			if (commandHistory.size() == 20)
				commandHistory.remove(commandHistory.size() - 1);
			commandHistory.add(getText());
			console.processText(getText());
			setText("", true);
			listPos = commandHistory.size();
		}
		else
			if (consoleInput.isFocused)
				consoleInput.keyTyped(e);
		super.keyTyped(e);
	}

	private void setText(String s) {
		consoleInput.setText(s);
	}

	private void setText(String s, boolean flag) {
		consoleInput.setText(s, flag);
	}
	
	public void mouseWheelScrolled(int i) {
		historyOffset += i;
	}

	private String getText() {
		return consoleInput.text;
	}

	public boolean pausesGame() {
		return true;
	}
	
	public void setConsole(Console c) {
		this.console = c;
	}

}
