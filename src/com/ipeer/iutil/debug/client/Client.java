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

//	public static void main(String[] args) {
//		Client b = new Client();
//		b.start();
//	}
	
	public Client() {
		this.engine = Engine.getInstance();
	}
	
	public Client(Engine engine) {
		this.engine = engine;
	}

	public void start() {
		IS_RUNNING = true;
		(thread = new Thread(this, "iUtil Remote Client")).start();
	}

	public void run() {
		GuiClient gui = (GuiClient)engine.getGui();
		try {
			gui.addTextToHistory("Attempting to connect to "+host+"/"+port);
			this.socket = new Socket(host, port);
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
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
		catch (NullPointerException e) { 
			run();
		}
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
					else
						((GuiClient)engine.getGui()).addTextToHistory("\2474[SRV]\247z "+input);
				}
			}
			System.err.println("Disconnected.");
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
//		catch (NullPointerException e) {
//			e.printStackTrace();
//		}
	}

	public void sendToServer(String text) {
		sent += text.getBytes().length;
		out.println(text);
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

}
