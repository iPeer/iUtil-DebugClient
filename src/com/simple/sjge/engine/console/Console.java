package com.simple.sjge.engine.console;

import java.util.ArrayList;
import java.util.Random;

import com.simple.sjge.collision.BBox;
import com.simple.sjge.engine.Engine;
import com.simple.sjge.entities.AITestEntity;
import com.simple.sjge.entities.AITestEntity2;
import com.simple.sjge.entities.Entity;
import com.simple.sjge.gui.GuiConsole;
import com.simple.sjge.level.Level;

public class Console {

	protected Engine engine;
	protected GuiConsole gui;

	public Console (Engine engine, GuiConsole gui) {
		this.engine = engine;
		this.gui = gui;
	}

	public Console(Engine engine) {
		this.engine = engine;
	}

	public void setGui(GuiConsole g) {
		this.gui = g;
	}

	public void processText(String t) {
		Level level = engine.level;
		String[] args = t.split(" ");
		if (args[0].equals("setDebug")) {
			if (args.length < 2)
				addConsoleMessage("Must supply a setting: "+args[0]+" true/false");
			else {
				Engine.DEBUG_ENABLED = Boolean.parseBoolean(args[1]);
				addConsoleMessage("Set debug settings to "+args[1]);
			}
		}
		else if (args[0].equals("clear")) {
			gui.historyOffset = 0;
			gui.messageHistory.clear();
		}

		else if (args[0].matches("l(ist)?[eE](ntities)?")) {
			addConsoleMessage(level.entities.size()+" entities");
			for (Entity e : level.entities) {
				addConsoleMessage("\t"+e.getClass().toString().split("@")[0]+" ("+e.getID()+")");
			}
		}
		else if (args[0].matches("l(evel)?d(ata)?")) {
			addConsoleMessage("Level: "+level.toString());
			addConsoleMessage(level.entities.size()+" entities");
			for (Entity e : level.entities)
				addConsoleMessage("\t"+e.getClass().toString().split("@")[0]+" ("+e.getID()+")");
			addConsoleMessage(level.BBoxes.size()+" bounding boxes");
			for (BBox b : level.BBoxes)
				addConsoleMessage("\tX: "+b.x+" Y: "+b.y+" W: "+b.w+" H: "+b.h);
		}
		else if (args[0].matches("c(lear)?b(bounding)?b(ox(es)?)?")) {
			engine.level.BBoxes.clear();
		}
		else if (args[0].equals("qqq")) {
			addConsoleMessage("Bye.");
			System.exit(0);
		}
		else if (args[0].matches("t(oggle)?c(ol)?l(isions)?")) {
			Engine.CHECK_COLLISIONS = !Engine.CHECK_COLLISIONS;
			Engine.CHECK_ENTITY_COLLISIONS = !Engine.CHECK_ENTITY_COLLISIONS;
			level.removeColliding();
			addConsoleMessage("Collisions = "+(Engine.CHECK_COLLISIONS ? "ON" : "OFF"));
			addConsoleMessage("entityCollisions = "+(Engine.CHECK_ENTITY_COLLISIONS ? "ON" : "OFF"));
		}
		else if (args[0].matches("t(oggle)?e(ntity)?c(ol)?l(isions)?")) {
			Engine.CHECK_ENTITY_COLLISIONS = !Engine.CHECK_ENTITY_COLLISIONS;
			level.removeColliding();
			addConsoleMessage("entityCollisions = "+(Engine.CHECK_ENTITY_COLLISIONS ? "ON" : "OFF"));
		}
		else if (args[0].matches("t(oggle)?fpsi(n)?c(onsole)?")) {
			Engine.PRINT_FPS = !Engine.PRINT_FPS;
			addConsoleMessage("FPS will "+(!Engine.PRINT_FPS ? "not " : "")+"be printed to the console.");
		}
		
		else if (args[0].matches("a(llow)?(o(over)?d(ragging)?|p(ast)?l(evel)?d(ragging)?)")) {
			if (args.length < 2 || args[1].equals("")) {
				addConsoleMessage("You must supply an argument: "+args[0]+" yes/true/1/0/false/no");
				return;
			}
			boolean a = Boolean.valueOf(args[1].replaceAll("yes|1", "true").replaceAll("no|0", "false"));
			Engine.ALLOW_OVERDRAGGING = a;
			addConsoleMessage("ALLOW_OVERDRAGGING = "+a);
		}
		
		else if (args[0].matches("(s(et)?)?c(urrent)?s(hield)?")) {
			if (args.length < 2 || args[1].equals("")) {
				addConsoleError("You must specify an entity ID - "+args[0]+" entityID [current]");
				return;
			}
			String eID = args[1];
			for (Entity a : level.entities)
				if (a.getID().split(" ")[0].equals(eID)) {
					try {
						if (args.length < 3)
							addConsoleMessage(eID+".getShield().getCurrent() = "+a.getShield().getCurrent());
						else {
							a.getShield().setCurrent(Integer.valueOf(args[2]));
							addConsoleMessage(eID+".getShield().getCurrent() = "+args[2]);
						}

					}
					catch (NullPointerException e) {
						addConsoleError("That entity doesn't have a shield!");
						return;
					}
				}
		}
		
		else if (args[0].matches("(r(esize)?|s(et)?)((g)?ame)?w(indow)?")) {
			if (args.length < 3) {
				addConsoleMessage("WIDTH = "+engine.getWidth()+" HEIGHT = "+engine.getHeight());
				return;
			}
			int w = Integer.valueOf(args[1]);
			int h = Integer.valueOf(args[2]);
			engine.resizeGame(w, h);
				
		}
		
		else if (args[0].matches("(s(et)?)?s(hield)?c(apacity)?")) {
			if (args.length < 2 || args[1].equals("")) {
				addConsoleError("You must specify an entity ID - "+args[0]+" entityID [capacity]");
				return;
			}
			String eID = args[1];
			for (Entity a : level.entities)
				if (a.getID().split(" ")[0].equals(eID)) {
					try {
						if (args.length < 3)
							addConsoleMessage(eID+".getShield().getCapacity() = "+a.getShield().getCapacity());
						else {
							a.getShield().setCapacity(Integer.valueOf(args[2]));
							addConsoleMessage(eID+".getShield().getCapacity() = "+args[2]);
						}

					}
					catch (NullPointerException e) {
						addConsoleError("That entity doesn't have a shield!");
						return;
					}
				}
		}
		else if (args[0].matches("d(eplete)?e(ntity)?s(hield)?")) {
			if (args.length < 2 || args[1].equals("")) {
				addConsoleError("You must specify an entity ID - "+args[0]+" entityID");
				return;
			}
			String eID = args[1];
			for (Entity a : level.entities)
				if (a.getID().split(" ")[0].equals(eID)) {
					try {
						a.getShield().setCurrent(0);
					}
					catch (NullPointerException e) {
						addConsoleError("That entity doesn't have a shield!");
						return;
					}
				}
		}
		else if (args[0].matches("r(eload)?(c(urrent)?)?l(evel)?")) {
			engine.level.generate();
		}
		else if (args[0].matches("e(ntity)?s(tress)?t(est)?")) {
			if (args.length < 2 || args[1].equals("")) {
				addConsoleError("You must specify a number of entities to stress test with: "+args[0]+" 100");
				return;
			}
			addConsoleMessage("Stress testing with "+args[1]+" entities. Use kae OR killallentities to abort.");
			int x = engine.getMousePosition().x;
			int y = engine.getMousePosition().y;
			int e = Integer.valueOf(args[1]);
			ArrayList<Entity> toAdd = new ArrayList<Entity>();
			for (int x1 = 0; x1 <= e; x1++) {
				int x2 = new Random().nextInt(2);
				if (x2 == 0)
					toAdd.add(new AITestEntity(x + new Random().nextInt(10), y + new Random().nextInt(10), 16, 16, level));
				else
					toAdd.add(new AITestEntity2(x + new Random().nextInt(10), y + new Random().nextInt(10), 16, 16, level));
			}
			level.entities.addAll(toAdd);
			level.removeColliding();
		}
		else if (args[0].matches("k(ill)?a(ll)?e(ntities)?")) {
			level.entities.clear();
			addConsoleMessage("\247aAll entities cleared.");
		}
		else if (args[0].equals("colours")) {
			String codes = "0123456789abcdeftuvwxyz";
			addConsoleMessage("Colours:");
			for (int x = 0; x < codes.length(); x++) {
				if (codes.charAt(x) != 'z')
					addConsoleMessage("\t\\247"+codes.charAt(x)+" = \247"+codes.charAt(x)+"Sample Text");
				else
					addConsoleMessage("\t\\247z will end all previous formatting codes.");
				if (codes.charAt(x) == 'f')
					addConsoleMessage("Other formatting:");
			}
		}
		else if (args[0].equals("historyoffset"))
			addConsoleMessage("historyOffset = "+Engine.guiConsole.historyOffset);
		else if (args[0].startsWith("fontRenderer->")) {
			String command = args[0].split("->")[1];
			if (command.equals("generateColours"))
				engine.fontRenderer.generateColours();
		}
		else 
			addConsoleError("Unknown command '"+args[0]+"'");

	}

	public void addConsoleError(String s) {
		addConsoleMessage("\247c"+s);
	}

	public void addConsoleMessage(String s) {
		//addConsoleMessage("\247a"+s, Colour.GREEN);
		gui.historyOffset = gui.messageHistory.size() + 1;
		gui.messageHistory.add(new ConsoleMessage(s));
	}

}
