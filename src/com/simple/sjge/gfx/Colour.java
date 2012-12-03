package com.simple.sjge.gfx;

import java.awt.Color;
import java.awt.color.ColorSpace;

public class Colour extends Color {

	private static final long serialVersionUID = 4776646632985288080L;
	
	public static final Colour WHITE = new Colour(255, 255, 255);
	public static final Colour BLACK = new Colour(0, 0, 0);
	public static final Colour RED = new Colour(255, 0, 0);
	public static final Colour GREEN = new Colour(0, 255, 0);
	public static final Colour BLUE = new Colour(0, 0, 255);
	public static final Colour YELLOW = new Colour(Color.YELLOW.getRGB());
	public static final Colour GREY = new Colour(Color.GRAY.getRGB());
	public static final Colour LIGHT_GREY = new Colour(Color.LIGHT_GRAY.getRGB());
	public static final Colour DARK_GREY = new Colour(Color.DARK_GRAY.getRGB());

	public Colour(int arg0) {
		super(arg0);
	}

	public Colour(int arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public Colour(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public Colour(float arg0, float arg1, float arg2) {
		super(arg0, arg1, arg2);
	}

	public Colour(ColorSpace arg0, float[] arg1, float arg2) {
		super(arg0, arg1, arg2);
	}

	public Colour(int arg0, int arg1, int arg2, int arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public Colour(float arg0, float arg1, float arg2, float arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
