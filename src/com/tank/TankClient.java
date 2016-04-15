package com.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ������������̹����Ϸ��������
 * @author yangzhen
 *
 */

/*
 *  "�ҷ�̹�˺�Ѫ��"����live��Ϊfalse����draw()�в������ǣ����ö��󻹴��ڣ�������ײ���ʱif������Ҫ����"live(���� live == true)"
 *   (ֻ��û�����������ö��󻹴���)
 *  "�з�̹�ˣ��ӵ��ͱ�ը"����live��Ϊfalse�����ö��󻹴��ڣ�������ײ����"tanks.remove(����)"����ʱ�ö���Ͳ�������
 */
public class TankClient extends Frame {
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount")); //��ȡ�����ļ��е�ֵ
	
	private int pass = 1; //�ڼ���
	private int number = initTankCount; //ÿ��̹�˵�����
	
	Tank myTank = new Tank(50, 200, true, Direction.STOP, this);
	
	//ǽ�ĺ��Ҫ�����ӵ���̹�ˣ���speed�������߳�ÿ��һ���ƶ��ľ��룩�������ӵ���̹�ˣ����ܻᴩ��ǽ����Ϊ��ײ������߳�ÿ�ػ�һ�βż��һ�ε�
	Wall w1 = new Wall(150, 200, 40, 150, this), 
		 w2 = new Wall(350, 200, 148, 40, this),
		 w3 = new Wall(428, 390, 68, 40, this);
	
	List<Missile> missiles = new ArrayList<Missile> ();
	List<Explode> explodes = new ArrayList<Explode> ();
	List<Tank> tanks = new ArrayList<Tank> ();
	
	BloodBag bb = new BloodBag();
	
	Random r = new Random();
	
	Image offScreenImage = null;
	
	public void paint(Graphics g) {	
		
		if(tanks.size() <= 0) {
			number = number + pass;
			for(int i=0; i<number; i++) {
				tanks.add(new Tank(r.nextInt(GAME_WIDTH - 24) + 24, r.nextInt(GAME_HEIGHT - 3) + 3, false, Direction.D, this));
			}
			pass ++;
		}
		
		myTank.draw(g);
		myTank.eat(bb);
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithWall(w3);
		
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		
		bb.draw(g);
		
		for(int i=0; i<missiles.size(); i++) {
			Missile m = missiles.get(i);
            m.hitTanks(tanks); //"��ײ���"����ʱʱ�̿̽��м�⣨�磺д��paint()�����У�
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.hitWall(w3);
			if(!m.isLive()) missiles.remove(m); //������ڵ��Ƴ�,ʹList������Ԫ�ز�����̫��
			else m.draw(g); //û����ļ���������
		}
		
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithWall(w3);
			t.collidesWithTanks(tanks);
			//��ֹ̹��������ɵ�ǽ��������̹�����ɵ�һ��
			if(t.collidesWithWall(w1) || t.collidesWithWall(w2) || t.collidesWithWall(w3) || t.collidesWithTanks(tanks) && t.getX() == t.getInitX() && t.getY() == t.getInitY()) {
				tanks.remove(t);
				tanks.add(new Tank(r.nextInt(GAME_WIDTH - 24) + 24, r.nextInt(GAME_HEIGHT - 3) + 3, false, Direction.D, this));
			} else {
				t.draw(g);
			}
		}
		
		g.setColor(Color.GREEN);
		g.drawString("game      pass: " + pass, 10, 50);
		g.drawString("tanks    count: " + tanks.size(), 10, 70);
		g.drawString("missiles count: " + missiles.size(), 10, 90);
		g.drawString("explodes count: " + explodes.size(), 10, 110);		
		g.drawString("tanks    blood: " + myTank.getBlood(), 10, 130);
	}
	
	//˫����
	public void update(Graphics g) { //��ʾ��Ļ�Ļ���
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics(); //������Ļ�Ļ���
		Color c = gOffScreen.getColor(); //�ػ��󱳾���ˢ�³ɿհ׵���ɫ
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen); //����������Ļ�Ļ��ʻ���ͼƬ
		g.drawImage(offScreenImage, 0, 0, null); //����ʾ��Ļ�ϻ���ͼƬ
	}

	public void lauchFrame() {
		
		//��ӻ���̹��
		for(int i=0; i<initTankCount; i++) {
			tanks.add(new Tank(r.nextInt(GAME_WIDTH - 24) + 24, r.nextInt(GAME_HEIGHT - 3) + 3, false, Direction.D, this));
		}
		
		this.setLocation(300, 100);
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
		new Thread(new PaintThread()).start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.lauchFrame();
	}
	
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {
		
		//����"����"�󴥷����¼���Ȼ����ø÷���
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e); //������װ����
		}
		
		//����"�ɿ�"�󴥷����¼���Ȼ����ø÷���
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
	
}
