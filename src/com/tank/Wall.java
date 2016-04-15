package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;


public class Wall {
	int x, y, w, h;
	TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image> ();
	
	static {
		wallImages = new Image[] {
				tk.getImage(Wall.class.getClassLoader().getResource("images/wallV.gif")),
				tk.getImage(Wall.class.getClassLoader().getResource("images/wallL1.gif")),
				tk.getImage(Wall.class.getClassLoader().getResource("images/wallL2.gif"))
		};
		
		imgs.put("V", wallImages[0]);
		imgs.put("L1", wallImages[1]);
		imgs.put("L2", wallImages[2]);
	}
	
	public Wall(int x, int y, int w, int h, TankClient tc) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		if(this == tc.w1) g.drawImage(imgs.get("V"), x, y, null);
		else if(this == tc.w2) g.drawImage(imgs.get("L1"), x, y, null);
		else g.drawImage(imgs.get("L2"), x, y, null);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
}
