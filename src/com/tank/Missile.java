package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Missile {
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 5;
	public static final int HEIGHT = 5;
	
	int x, y;
	Direction dir;
	
	TankClient tc;
	
	private boolean live = true; //标记当前炮弹是否出界
	private boolean good; //标记炮弹好坏
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] missileImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image> ();
	
	static {
		missileImages = new Image[] {
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/MyMissileU.gif"))
		};
		
		imgs.put("D", missileImages[0]);
		imgs.put("L", missileImages[1]);
		imgs.put("LD", missileImages[2]);
		imgs.put("LU", missileImages[3]);
		imgs.put("R", missileImages[4]);
		imgs.put("RD", missileImages[5]);
		imgs.put("RU", missileImages[6]);
		imgs.put("U", missileImages[7]);
		imgs.put("MyD", missileImages[8]);
		imgs.put("MyL", missileImages[9]);
		imgs.put("MyLD", missileImages[10]);
		imgs.put("MyLU", missileImages[11]);
		imgs.put("MyR", missileImages[12]);
		imgs.put("MyRD", missileImages[13]);
		imgs.put("MyRU", missileImages[14]);
		imgs.put("MyU", missileImages[15]);	
	}
	
	public boolean isLive() {
		return live;
	}

	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, boolean good, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		move();
		
		switch(dir) {
		case L:
			if(good) g.drawImage(imgs.get("MyL"), x, y, null);
			else g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			if(good) g.drawImage(imgs.get("MyLU"), x, y, null);
			else g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			if(good) g.drawImage(imgs.get("MyU"), x, y, null);
			else g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			if(good) g.drawImage(imgs.get("MyRU"), x, y, null);
			else g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			if(good) g.drawImage(imgs.get("MyR"), x, y, null);
			else g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			if(good) g.drawImage(imgs.get("MyRD"), x, y, null);
			else g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			if(good) g.drawImage(imgs.get("MyD"), x, y, null);
			else g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			if(good) g.drawImage(imgs.get("MyLD"), x, y, null);
			else g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
	}

	//让打出的炮弹不断前进 
	private void move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		//判断当前炮弹是否出界
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT) {
			live = false;
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t) {
		if(t.isLive() && this.live && this.good != t.isGood() && this.getRect().intersects(t.getRect())) {
			if(t.isGood()) { //我方坦克被打中五下后死掉
				t.setBlood(t.getBlood() - 20);
				if(t.getBlood() <= 0) t.setLive(false);
			}else { //敌方坦克被打中就死
				t.setLive(false);
			}
			
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hitWall(Wall w) {
		if(this.live && this.getRect().intersects(w.getRect())) {
			this.live = false;
			return true;
		}
		return false;
	}
	
}
	