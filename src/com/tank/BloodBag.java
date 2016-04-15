package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;


public class BloodBag {
	int x, y, w, h;
	
	int step = 0; //记录血包移动到第几步
	
	private TankClient tc;
	
	private boolean live = true; //标记 血包是否存在

	//血包定向移动的轨迹坐标
	private int pos[][] = {
							{650, 450}, {665, 450}, {680, 450}, {695, 450}, {710, 450}, {710, 465}, {710, 480}, {710, 495}, {710, 510}, {695, 510}, {680, 510}, {665, 510}, {650, 510}, {650, 495}, {650, 480}
						  };
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] img = {
		tk.getImage(BloodBag.class.getClassLoader().getResource("images/bloodBag.gif"))
	};
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public BloodBag() {
		this.x = pos[0][0];
		this.y = pos[0][1];
		w = h = 15;
	}
	
	public void draw(Graphics g) {
		if(!live) return;
				
		move();
		
		g.drawImage(img[0], x, y, null);
	}

	private void move() {
		step ++;
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h); 
	}
	
}
