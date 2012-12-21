package com.ipeer.iutil.debug.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.simple.sjge.engine.Engine;

public class Client implements Runnable {

	private boolean IS_RUNNING = false;
	private int port = 4444;
	private String host = "127.0.0.1";
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Thread thread;
	private File log;
	private Engine engine;
	public volatile long sent = 0;
	public volatile long rec = 0;
	private int clientVersion = 1;

	//	public static void main(String[] args) {
	//		Client b = new Client();
	//		b.start();
	//	}

	public Client() {
		this(Engine.getInstance());
	}

	public Client(Engine engine) {
		this(engine, "auron.co.uk", 4444);
	}
	
	public Client(String string, int i) {
		this(Engine.getInstance(), string, i);
	}

	public Client(Engine engine, String string, int i) {
		this.engine = engine;
		this.host = string;
		this.port = i;
	}
	

	public void start() {
		IS_RUNNING = true;
		(thread = new Thread(this, "iUtil Remote Client")).start();
	}

	public void run() {
		GuiClient gui = (GuiClient)engine.getGui();
		int x = 1;
		for (; gui == null; x++) {
			if (x == 1000)
				throw new RuntimeException("Took too long to initialize GUI object.");
		}
		
		for (x = 1; Engine.getGraphicsInstance() == null; x++) {
			if (x == 10000)
				throw new RuntimeException("Took too long to initialize graphics object.");
		}
		System.err.println("Took "+x+" cycle(s) to initialize graphics object.");
		try {
			gui.addTextToHistory("Attempting to connect to "+host+"/"+port);
			socket = new Socket(host, port);
			out = new PrintWriter(this.socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			gui.addTextToHistory("\247aConnected successfully.");
		}
		catch (UnknownHostException | ConnectException e) {
			if (e instanceof UnknownHostException)
				gui.addTextToHistory("\247cCan't reach server at "+host+"/"+port+": Unknown host: "+e.getMessage());
			else
				gui.addTextToHistory("\247cCan't reach server at "+host+"/"+port+": "+e.getMessage());
			e.printStackTrace();
			return;
		}
//		catch (NullPointerException e) { 
//			e.printStackTrace();
//		}
		catch (Exception e) {
			gui.addTextToHistory("\247cUnable to connect to "+host+"/"+port+": "+e.getMessage());
			e.printStackTrace();
			return;
		}
		try {
			String input, output;
			while ((input = in.readLine()) != null) {
				rec += input.getBytes().length;
				if (!input.equals("null")) {
					if (engine == null)
						System.err.println("[SRV] "+input);
					else {
						if (input.equals("SENDCLIENTVERSION"))
							sendToServer("CLIENTVERSION "+clientVersion);
						else
							((GuiClient)engine.getGui()).addTextToHistory("\2474[SRV]\247z "+input.replaceAll("Â\247", "\247"));
					}
						//char a = 'Â';
					//System.err.println(Character.toString(a));
				}
			}
			in.close();
			out.close();
			socket.close();
		}
		catch (SocketException e) {
			gui.addTextToHistory("\247cLost connection to server: "+e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void sendToServer(String text) {
		if (in == null) {
			GuiClient a = (GuiClient)engine.getGui();
			try {
				a.addTextToHistory("\247eAttempting to recreate input stream...");
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (Exception e) {
				a.addTextToHistory("\247cCouldn't create input stream.");
				a.addTextToHistory("\t"+e.toString()+": "+(e.getMessage() != null ? e.getMessage() : ""));
				for (StackTraceElement ste : e.getStackTrace())
					a.addTextToHistory("\t\t"+ste.toString());
			}
			
		}
		if (out == null) {
			GuiClient a = (GuiClient)engine.getGui();
			try {
				a.addTextToHistory("\247eAttempting to recreate output stream...");
				out = new PrintWriter(this.socket.getOutputStream(), true);
			} catch (Exception e) {
				a.addTextToHistory("\247cCouldn't create output stream.");
				a.addTextToHistory("\t"+e.toString()+": "+(e.getMessage() != null ? e.getMessage() : ""));
				for (StackTraceElement ste : e.getStackTrace())
					a.addTextToHistory("\t\t"+ste.toString());
			}
		}
		sent += text.getBytes().length;
		out.println(text);
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

}
