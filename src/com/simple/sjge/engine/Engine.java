/*
 * Originally written by Chris 'iPeer' Wignall and Roxanne Newman of SiMPLE STUDIOS
 * Feel free to use this engine in your games and modify to your needs.
 * Just don't do evil.
 */

package com.simple.sjge.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import com.ipeer.iutil.debug.client.Client;
import com.ipeer.iutil.debug.client.GuiClient;
import com.simple.sjge.engine.console.Console;
import com.simple.sjge.gfx.Colour;
import com.simple.sjge.gfx.FontRenderer;
import com.simple.sjge.gfx.Screen;
import com.simple.sjge.gui.Gui;
import com.simple.sjge.gui.GuiConsole;
import com.simple.sjge.level.Level;
import com.simple.sjge.util.Debug;

public class Engine extends Canvas implements Runnable {

	private static final long serialVersionUID = 1341257396583356312L;
	protected static String GAME_TITLE = "iUtil Debug Client"/*"SiMPLE Engine 4"*/;
	protected static int BUFFER_LEVEL = 2;
	protected static int HEIGHT_RATIO = 9;
	protected static int WIDTH_RATIO = 16;
	protected static int GAME_WIDTH = 1024;
	protected static int GAME_HEIGHT = GAME_WIDTH * HEIGHT_RATIO / WIDTH_RATIO;
	protected static double TICKS_PER_SECOND = 50.0;
	protected static boolean INITIALISED = false;
	protected static boolean LOADLEVELS = true;

	protected static int FPS_LIMIT = -1;

	protected static Engine engine;
	protected static Frame frame;
	protected static Graphics2D g;
	public KeyboardHandler input;
	public FontRenderer fontRenderer;
	public IKeyHandler keyHandler;

	private int lastTicks, lastFrames;

	protected Screen screen;
	public Level level;
	public GameRenderer gameRenderer;
	public GameTicker gameTicker;

	static boolean GAME_RUNNING = false;
	public static boolean CHECK_COLLISIONS = true;
	public static boolean CHECK_ENTITY_COLLISIONS = true;
	public static boolean ALLOW_OVERDRAGGING = false;
	public static Point Mouse = new Point(0, 0);

	protected Gui currentGui = null;
	private static String pendingGui = "";
	public static GuiConsole guiConsole;

	public static final String allowedCharacters = "abcdefghijklmnopqrstuvwxyz1234567890.+-*/`¬¦!\"£$€%^&*()_{}[]@~'#<>?,/\\;:";
	public static boolean PRINT_FPS = true;
	public static boolean DEBUG_ENABLED = false;
	
	public static long tickTime, renderTime, totalTime;
	
	private boolean graphicsInitialized = false;
	
	public Client client;

	public Engine() {
		guiConsole = new GuiConsole(this);
		this.screen = new Screen(GAME_WIDTH, GAME_HEIGHT, this);
	}

	public static void main(String[] args) {
		if (args.length > 0)
			for (String line : args) {
				if (line.startsWith("-debug="))
					DEBUG_ENABLED = Boolean.parseBoolean(line.split("=")[1]);
				if (line.startsWith("-fpslimit="))
					FPS_LIMIT = Integer.valueOf(line.split("=")[1]);
				if (line.startsWith("-title="))
					setTitle(line.split("=")[1]);
				if (line.startsWith("-print-fps="))
					PRINT_FPS = Boolean.parseBoolean(line.split("=")[1]);
				if (line.equals("-donotloadlevels") || line.equals("-donotsetlevel"))
					LOADLEVELS = false;
				if (line.startsWith("-resx="))
					GAME_WIDTH = Integer.valueOf(line.split("=")[1]);
				if (line.startsWith("-resy="))
					GAME_HEIGHT = Integer.valueOf(line.split("=")[1]);
				if (line.startsWith("-gui=")) {
					pendingGui = line.split("=")[1];
				}
			}
		if (PRINT_FPS)
			PRINT_FPS = false;
		FPS_LIMIT = 34;
		engine = new Engine();
		Debug.p("SiMPLE Engine "+engine.getVersion());
		Debug.p("GT :"+GAME_TITLE+", BL: "+BUFFER_LEVEL+", HR: "+HEIGHT_RATIO+", WR: "+WIDTH_RATIO+", GW: "+GAME_WIDTH+", GH: "+GAME_HEIGHT+", RTPS: "+TICKS_PER_SECOND);
		Debug.p("AC: "+allowedCharacters);
		String fullArgs = "";
		for (String a : args)
			fullArgs = fullArgs+(fullArgs.length() > 0 ? ", ": "")+a;
		Debug.p("ARGS: "+fullArgs);
		Debug.p("");
		frame = new Frame(GAME_TITLE);
		engine.setPreferredSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));
		frame.setLayout(new BorderLayout());
		frame.addWindowListener(new iWindowListener(engine));
		Debug.p("Added Window listener.");
		engine.addMouseListener(new iMouseListener(engine));
		Debug.p("Added Mouse listener.");
		engine.addMouseMotionListener(new iMouseMotionListener(engine));
		Debug.p("Added Mouse Motion listener.");
		engine.addMouseWheelListener(new iMouseWheelListener(engine));
		Debug.p("Added Mouse Wheel listener.");
		engine.addComponentListener(new iComponentListener(engine));
		Debug.p("Added Component listener.");
		frame.add(engine, "Center");
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		engine.requestFocus();
		engine.start();
	}

	public void start() {
		try {
			(new Thread(this, "SE4 Main Thread")).start();
			GAME_RUNNING = true;
		}
		catch (Exception e) {
			Debug.p("Unable to start Engine Thread!");
			System.exit(0);
		}
	}

	public void stop() {
		GAME_RUNNING = false;
	}
	
	public void initAndStart() {
		INITIALISED = true;
		init();
		start();
	}

	public void init() {
		INITIALISED = true;
		fontRenderer = new FontRenderer(this);
		input = new KeyboardHandler(this);
		Debug.p("");
		Debug.p("Screen:");
		Debug.p(screen.toString()+", "+this.toString()+", "+screen.width+", "+screen.height);
		Debug.p("");
//		if (LOADLEVELS)
//			setLevel(new TestLevel(1280, 720, screen));
//		if (!pendingGui.equals("")) {
//			setGui(pendingGui);
//			pendingGui = "";
//		}
		client = new Client(this);
	}

	@SuppressWarnings("unused")
	private void setGui(String gui1) {
		Gui newGui;
		try {
			
			newGui = (Gui)Class.forName(gui1).newInstance();
			pendingGui = gui1;
			setGui(newGui);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			System.err.println("Could not create class for specified GUI: "+gui1);
			e.printStackTrace();
		}
	}

	public void setLevel(Level level) {
		Debug.p("Changing level!");
		if (level == null)
			Debug.p("NULL");
		else
			Debug.p(level.toString()+", "+level.width+", "+level.height);
		this.level = level;
	}

	public void run() {
		int ticks = 0;
		int frames = 0;
		long lastTime = System.nanoTime();
		double processQueue = 0.0;
		double ticksPerLoop = 1000000000 / TICKS_PER_SECOND;
		long lastTick = System.currentTimeMillis();
		if (!INITIALISED)
			init();
		while (GAME_RUNNING) {
			long now1 = System.currentTimeMillis();
			long now = System.nanoTime();
			processQueue += (double)(now - lastTime) / ticksPerLoop;
			lastTime = now;
			while (processQueue >= 1.0) { // Tick
				if (processQueue > TICKS_PER_SECOND)
					processQueue = TICKS_PER_SECOND;
				ticks++;
				tick();
				processQueue--;
			}
			if (FPS_LIMIT > 29) {
				try {
					Thread.sleep(1000 / FPS_LIMIT);
				} 
				catch (InterruptedException e) {
					Debug.e("Unable to enforece idle frame limit");
					e.printStackTrace();
				}
			}
			render();
			frames++;
			if (System.currentTimeMillis() - lastTick > 1000L) {
				if (PRINT_FPS)
					System.out.println(frames+" fps, "+ticks+" ticks");
				lastFrames = frames;
				lastTicks = ticks;
				frames = ticks = 0;
				lastTick = System.currentTimeMillis();
			}
			totalTime = System.currentTimeMillis() - now1;
		}
		System.exit(0);
	}

	public void tick() {	
		long tick = System.currentTimeMillis();
		//input.tick();
		if (keyHandler != null)
			keyHandler.tickKeys();
//		if (Keyboard.isKeyDown(Keyboard.F3) && (System.currentTimeMillis() - input.lastDebugPress) > 200L) {
//			DEBUG_ENABLED = !DEBUG_ENABLED;
//			input.lastDebugPress = System.currentTimeMillis();
//		}
//		if (Keyboard.isKeyDown(Keyboard.console) && (System.currentTimeMillis() - input.lastDebugPress) > 150L && !(currentGui instanceof GuiConsole) && DEBUG_ENABLED) {
//			setGui(guiConsole);
//			input.lastDebugPress = System.currentTimeMillis();
//		}
		screen.tick();
		if (gameTicker != null)
			gameTicker.tick(this);
		if (currentGui != null) {
			currentGui.tick();
			if (currentGui.pausesGame() && level != null)
				return;
		}

		if (level != null)
			level.tick();
		tickTime = System.currentTimeMillis() - tick;
	}

	public void render() { 
		long render = System.currentTimeMillis();
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(BUFFER_LEVEL);
			requestFocus();
			return;
		}

		g = (Graphics2D)bs.getDrawGraphics();
		if (!graphicsInitialized) {
			graphicsInitialized = true;
			onGraphicsCreation();
		}

		// Game rendering

//		g.setColor(Colour.BLACK);
//		g.fillRect(0, 0, GAME_WIDTH,  GAME_HEIGHT);
		
		screen.setGraphics(g);
		screen.render();
		
		if (gameRenderer != null)
			gameRenderer.render(this, g);
		if (level != null)
			level.render();
		if (currentGui != null)
			currentGui.render();
		if (DEBUG_ENABLED)
			drawDebug(g);
		g.dispose();
		bs.show();
		renderTime = System.currentTimeMillis() - render;
	}

	private void onGraphicsCreation() {
		setGui(new GuiClient(this, client));
		client.start();
	}

	public void drawDebug(Graphics2D g) { // [Roxy] Draws debug output to the game screen
		FontMetrics fm = g.getFontMetrics(); // [Roxy] Get the FontMetrics object.
		String fps = lastFrames+" fps, "+lastTicks+" ticks"; // [Roxy] The String for the fps output.
		long usedMem = Runtime.getRuntime().totalMemory();
		long totalMem = Runtime.getRuntime().maxMemory();
		String mem = ", "+(usedMem / 1024 / 1024)+"MB/"+(totalMem / 1024 / 1024)+"MB";
		int a = fm.stringWidth(fps+mem); // [Roxy] an int containing the width of the fps string.
		int b = screen.width - (a + 2); // [Roxy] The x position of where the text should be drawn.
		//g.setColor(Colour.WHITE); // [Roxy] Sets the debug text to always be white.
		screen.drawString(fps+mem, b, 12); // [Roxy] Draw the string on the screen (string, x ,y + 10).
		screen.drawString(screen.toString()+" ["+screen.width+"x"+screen.height+"]", screen.width - (fontRenderer.stringWidth(screen.toString()+" ["+screen.width+"x"+screen.height+"]")), 22, Colour.WHITE);
		if (level != null)
			screen.drawString(level.toString()+" ["+level.width+"x"+level.height+" | "+level.xOffset+", "+level.yOffset+"]", screen.width - (fontRenderer.stringWidth(level.toString()+" ["+level.width+"x"+level.height+" | "+level.xOffset+", "+level.yOffset+"]")), 32, Colour.WHITE);
	}

	public static Engine getInstance() {
		return engine;
	}

	public static Graphics2D getGraphicsInstance() {
		return g;
	}

	public void setGui(Gui gui) {
		this.currentGui = gui;
	}

	@Override
	public int getWidth() {
		return GAME_WIDTH;
	}

	@Override
	public int getHeight() {
		return GAME_HEIGHT;
	}

	public Level getLevel() {
		return this.level;
	}

	public String getVersion() {
		return "4.0.0";
	}

	public void resizeGame(int w, int h) {
		//engine.setPreferredSize(new Dimension(w-10, h-10));
		frame.setSize(new Dimension(w - 10, h - 10));
		frame.setLayout(new BorderLayout());
		engine.setPreferredSize(new Dimension(w - 10, h - 10));
		frame.add(engine, "Center");
		frame.pack();
		screen.setSize(w - 10,  h - 10);
	}

	public Screen screenInstance() {
		return screen;
	}
	
	public static void setTitle(String string) {
		GAME_TITLE = string;
		Debug.p("Window title is now \""+string+"\"");
	}
	
	public static void setFPSLimit(int fps) {
		FPS_LIMIT = fps;
	}
	
	public void setKeyHandler(IKeyHandler k) {
		this.keyHandler = k;
	}
	
	public void setConsole (Console c) {
		guiConsole.console = c;
	}
	
	public void setTicker(GameTicker t) {
		this.gameTicker = t;
	}
	
	public void setRenderer(GameRenderer r) {
		this.gameRenderer = r;
	}
	
	public Gui getGui() {
		return this.currentGui;
	}

}
