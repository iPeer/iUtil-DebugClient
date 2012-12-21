package com.ipeer.iutil.debug.client;

import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.simple.sjge.engine.Engine;
import com.simple.sjge.engine.Keyboard;
import com.simple.sjge.engine.console.Console;
import com.simple.sjge.engine.console.ConsoleMessage;
import com.simple.sjge.gfx.Screen;
import com.simple.sjge.gui.Gui;
import com.simple.sjge.gui.controls.GuiTextField;

public class GuiClient extends Gui {

	protected Engine engine;
	private static final ArrayList<String> commandHistory = new ArrayList<String>();
	public final ArrayList<ConsoleMessage> messageHistory = new ArrayList<ConsoleMessage>();
	private static String text = "";
	public Console console;
	private static int listPos = 0;
	public int historyOffset = 0;
	GuiTextField consoleInput;

	private Client client;


	@SuppressWarnings("unchecked")
	public GuiClient(Engine engine, Client client) {
		this.engine = engine;
		this.client = client;
		controls.clear();
		controls.add(consoleInput = new GuiTextField(2, engine.getHeight() - 22, engine.getWidth() - 6, 20, ""));
		consoleInput.isFocused = true;
	}

	public void render() {
		try {
			Screen screen = this.engine.screenInstance();

			int yPos = (engine.getHeight() - 22) - 5;
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
			if (isCommand(getText())) {
				processCommand(getText());
				setText("", true);
			}
			else {
				addTextToHistory("\247a > "+getText());
				if (getText().equals("TESTSTRING")) {
					GuiClient gui = (GuiClient)engine.getGui();
					gui.addTextToHistory("Really long string that will hopefully \247cgo off the screen allowing me to wrap it on to a new line because I am awesome and I can code like a pro. \247zMaybe the line needs to be longer \247cbecause apparently \247wthat last sentence isn't long enough to make it \247zoverflow \247t\247uoff the screen thus not allowing me to test if it wraps.\247z");
					setText("", true);
					return;
				}
				client.sendToServer(getText());
			}
			setText("", true);
			listPos = commandHistory.size();
		}
		else
			if (consoleInput.isFocused)
				consoleInput.keyTyped(e);
		super.keyTyped(e);
	}

	private boolean isCommand(String text2) {
		return text2.startsWith("/");
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

	public void addTextToHistory(String t) {
		List<String> wrap = wrapLine(t);
		for (String t1 : wrap)
			messageHistory.add(new ConsoleMessage(t1));
		historyOffset = messageHistory.size();
	}

	public List<String> wrapLine(String t) { // To the nearest space text wrapping
		List<String> a = new ArrayList<String>();

		FontMetrics f = Engine.getGraphicsInstance().getFontMetrics();

		int index = 0;
		String[] data = t.split(" ");
		String o = "";
		for (String s : data) {
			o = o+(o.length() > 0 ? " " : "")+s;
			int l = f.stringWidth((o+(index < data.length - 1 ? data[index + 1] : "")).replaceAll("\247[0123456789abcdeftuvwxyz]", ""));
			if (l >= engine.getWidth() - 5 || index >= data.length - 1) {
				a.add(o);
				o = "";
			}
			index++;
		}		
		return a;
	}

	private void processCommand(String text) {
		String[] d = text.split(" ");
		String c = d[0].replaceFirst("/", "");
		String p = "";
		for (int x = 1; x < d.length; x++)
			p = p+(p.length() > 1 ? " " : "")+d[x];

		if (c.equalsIgnoreCase("colours")) {
			String codes = "0123456789abcdeftuvwxyz";
			addTextToHistory("Colours:");
			for (int x = 0; x < codes.length(); x++) {
				if (codes.charAt(x) != 'z')
					addTextToHistory("\t\\247"+codes.charAt(x)+" = \247"+codes.charAt(x)+"Sample Text");
				else
					addTextToHistory("\t\\247z will end all previous formatting codes.");
				if (codes.charAt(x) == 'f')
					addTextToHistory("Other formatting:");
			}
		}
		else if (c.equalsIgnoreCase("logout")) {
			client.sendToServer("LOGMEOUT");
		}
		else if (c.equalsIgnoreCase("login")) {
			client.sendToServer("login "+p);
		}
		else if (c.equalsIgnoreCase("create")) {
			client.sendToServer("create "+p);
		}
		else if (c.equalsIgnoreCase("connect")) {
			String[] data = p.split(" ");
			if (data.length == 0 || p.length() == 0) {
				addTextToHistory("\247cYou must specify a server to connect to!");
				addTextToHistory("\247c/connect \247vserver:port\247z \247cOR /connect \247vserver\247z\247c[ \247vport\247z\247c]");
				addTextToHistory("\247cIf a port is not specified the default (4444) will be used.");
				return;
			}
			else {
				if (client.isConnected()) {
					disconnectFromServer();
				}
				Client a = null;
				if (p.contains(":"))
					data = p.split(":");
				try {
					a = new Client(data[0], Integer.valueOf(data[1]));
				}
				catch (ArrayIndexOutOfBoundsException e) {
					a = new Client(data[0], 4444);
				}
				client = a;
				client.start();
			}
		}
		else if (c.equalsIgnoreCase("exit")) {
			System.exit(0);
		}
	}
	
	public void disconnectFromServer() {
		addTextToHistory("Disconnected.");
		client.sendToServer("DISCONNECT");
	}

}
