package com.simple.sjge.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.simple.sjge.engine.Engine;

public class Screen {
	
	public int width, height;
	protected Engine engine;
	private Graphics2D graphics;

	public Screen(int width, int height, Engine engine) {
		this.width = width;
		this.height = height;
		this.engine = engine;
	}
	
	public void render() {
		
		graphics.setColor(Colour.BLACK);
		graphics.fillRect(0, 0, this.width, this.height);
		
	}
	
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public void tick() {
		
	}

	public void drawRect(int x, int y, int w, int h, Colour c) {
		graphics.setColor(c);
		graphics.drawRect(x, y, w, h);
	}
	
	public void drawRect(int x, int y, int w, int h) {
		drawRect(x, y, w, h, Colour.WHITE);
	}

	public void setGraphics(Graphics2D drawGraphics) {
		this.graphics = drawGraphics;
	}

	public void drawString(String string, int x, int y) {
		drawString(string, x, y, Colour.WHITE);	
	}
	
	public void drawString(String string, int x, int y, Colour c) {
		FontRenderer fr = engine.fontRenderer;
		graphics.setColor(c);
		fr.render(string, x, y);
	}
	
	public static BufferedImage loadImage(String path, int width, int height) throws IOException {
		return loadImage(path, width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public static BufferedImage loadImage(String path, int width, int height, int type) throws IOException {
		BufferedImage i = new BufferedImage(width, height, type);
		i = ImageIO.read(new File(path));
		return i;
	}
	
	public void render(BufferedImage image, int screenX, int screenY, int width, int height, int sourceX, int sourceY, int sourceW, int sourceH) {
		Graphics2D g = Engine.getGraphicsInstance();
		g.drawImage(image, screenX, screenY, width, height, sourceX, sourceY, sourceW, sourceH, null);
	}
	
	public void renderSprite(BufferedImage sprite, int x, int y) {
		Graphics2D g = Engine.getGraphicsInstance();
		g.drawImage(sprite, x, y, null);
	}

	public void renderSprite(BufferedImage sprite, int x, int y, int entityW, int entityH) {
		Graphics2D g = Engine.getGraphicsInstance();
		g.drawImage(sprite, x, y, entityW + 1, entityH + 1, null);
	}

	public Graphics2D getGraphics() {
		return graphics;
	}
	
	
}
