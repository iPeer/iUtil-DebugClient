package com.simple.sjge.gfx;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import com.simple.sjge.engine.Engine;

public class FontRenderer {

	@SuppressWarnings("unused")
	private Engine engine;

	private boolean bold;
	private boolean underline;
	private boolean strike;
	private boolean italics; 
	private boolean end;
	private boolean supers;
	private boolean sub;

	private int[] colours;

	public FontRenderer(Engine engine) {
		this.engine = engine;
		generateColours();
	}

	public void generateColours() {
		colours = new int[32];
		for(int x = 0; x < 32; x++)
		{
			int a = (x >> 3 & 1) * 85;
			int b = (x >> 2 & 1) * 170 + a;
			int c = (x >> 1 & 1) * 170 + a;
			int d = (x >> 0 & 1) * 170 + a;
			if(x == 6)
			{
				a += 85;
			}
			if(x >= 16)
			{
				b /= 4;
				c /= 4;
				d /= 4;
			}
			colours[x] = (b & 0xff) << 16 | (c & 0xff) << 8 | d & 0xff;
		}
	}
	
	public int stringWidth(String s) {
		String text = s.replaceAll("\247[0123456789abcdeftuvwxyz]", "");
		int l = 0;
		for (int x = 0; x < text.length(); x++)
			l += Engine.getGraphicsInstance().getFontMetrics().stringWidth(Character.toString(text.charAt(x)));
		return l;
	}

	public void render(String text, int x, int y, float opacity) {
		text = text.replaceAll("\t", "     ");
		Graphics2D g = Engine.getGraphicsInstance();
		Font oldFont = g.getFont();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
//			if (c == '\t') {
//				text = text.substring(0, i)+"     "+text.substring(i+1);
//				i+=5;
//			}	
			if (c == '\247' && i + 1 < text.length()) {
				int a = "0123456789abcdeftuvwxyz".indexOf(text.toLowerCase().charAt(i + 1));
				if (a < 16) {
					bold = false;
					italics = false;
					underline = false;
					strike = false;
					supers = false;
					sub = false;
					end = false;
					if (a < 0 || a > 15)
						a = 15;
					a = colours[a];
					g.setColor(new Colour((float)(a >> 16) / 255F, (float)(a >> 8 & 0xff) / 255F, (float)(a & 0xff) / 255F));
					if (opacity >= 0.0 && opacity <= 1.0f)
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
				}
				else if (a == 16)
					bold = !bold;
				else if (a == 17)
					italics = !underline;
				else if (a == 18)
					underline = !underline;
				else if (a == 19)
					strike = !strike;
				else if (a == 20)
					supers = !supers;
				else if (a == 21)
					sub = !sub;
				else if (a == 22) {
					bold = underline = italics = strike = supers = sub = false;
					g.setFont(oldFont);
					g.setColor(Colour.WHITE);
				}
				if (bold || underline || strike || italics || supers || sub) {
					if (bold)
						g.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize()));
					else if (italics)
						g.setFont(new Font(oldFont.getName(), Font.ITALIC, oldFont.getSize()));
//					else if (underline)
//						g.setFont(new Font(oldFont.getName(), Font.UNDERLINE, oldFont.getSize()));
//					//else if (strike)
//					//	g.setFont(new Font(oldFont.getName(), TextAttribute.STRIKETHROUGH_ON, oldFont.getSize()));
//					else if (supers)
//						g.setFont(new Font(oldFont.getName(), TextAttribute.SUPERSCRIPT_SUPER, oldFont.getSize()));
//					else if (sub)
//						g.setFont(new Font(oldFont.getName(), TextAttribute.SUPERSCRIPT_SUB, oldFont.getSize()));
					else if (end) {
						bold = underline = italics = strike = supers = sub = false;
						g.setFont(oldFont);
						g.setColor(Colour.WHITE);
					}

				}
			}
			else {
				if (i == 0 || (i > 0 && text.charAt(i - 1) != '\247')) {
					String s = Character.toString(c);
					int si = g.getFontMetrics().stringWidth(s);
					if (underline || strike || supers || sub) {
						AttributedString as = new AttributedString(s);
						if (underline)
							as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
						else if (strike)
							as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
						else if (supers)
							as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER);
						else if (sub)
							as.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
						g.drawString(as.getIterator(), x, y);
					}
					else
						g.drawString(s, x, y);
					x += si;
				}
			}
		}
		reset();
		g.setFont(oldFont);
		g.setColor(Colour.WHITE);
	}

	private void reset() {
		bold = underline = italics = strike = supers = sub = false;
	}

	public void render(String string, int x, int y) {
		render(string, x, y, 1.0f);
	}

}
