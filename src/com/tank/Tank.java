package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * static{获取图片}静态加载时，"tankImages[0].getWidth()"可能获取的值为"-1"。(因为此时图片可能还没从硬盘加载到内存上，所以获取不到图片的值)
*/
public class Tank {
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 35;
	public static final int HEIGHT = 37;
	
	public static final int BLOOD = 100;
	
	TankClient tc; //声明对象
	
	private int x, y;
 
	private int oldX, oldY; //移动后，坦克上一步的x，y坐标
	private int initX, initY; //最初的x,y

	private int blood = BLOOD; //坦克生命值
	private BloodBar bb = new BloodBar();
	
	Random r = new Random(); //随机生成数
	
	private boolean live = true; //标记坦克是否被打中
	private boolean good; //标记坦克好坏
	private boolean bL=false, bU=false, bR=false, bD=false; //标记键盘敲了哪些方向键
	//enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}; //可以通过类名Tank调用
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	
	int step = r.nextInt(12) + 3; //坦克每移动step步，就改变方向
	
	private static Toolkit tk = Toolkit.getDefaultToolkit(); //获取默认的工具包
	private static Image[] tankImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image> ();
	
	//“{}”是方便在函数外也能写赋值语句和调用语句（如：System.out.println();）
	static {
		tankImages = new Image[] { //"Tank.class"等价于"this.getClass()"(但是static{}中对象还没生成，没this)
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
		};
		
		//方便能清楚的知道Image[0]是哪张图片
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);
	}
				
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getInitX() {
		return oldX;
	}

	public int getInitY() {
		return oldY;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	public boolean isGood() {
		return good;
	}
	
	public int getBlood() {
		return blood;
	}

	public void setBlood(int blood) {
		this.blood = blood;
	}

	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.initX = this.oldX = x; //oldX的初始化
		this.initY = this.oldY = y; //oldY的初始化
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, Direction dir, TankClient tc) { //通过构造方法给声明的对象初始化(赋值)
		this(x, y, good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g) {
		move();
		
		if(!live) { //坦克死了，就从容器中移除
			if(!good) {
				tc.tanks.remove(this);
			}
			return;
		}
		
		if(good) bb.draw(g);
		
		switch(ptDir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
	}

	void move() {
		
		this.oldX = x; //把每次移动前的x赋给oldX
		this.oldY = y; //把每次移动前的x赋给oldY
		
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
		case STOP:
			break;
		}
		
		//让炮筒方向随坦克方向改变
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		//不让坦克出界
		if(x < 1) x = 1;
		if(y < 24) y = 24;
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH - 3) x = TankClient.GAME_WIDTH - Tank.WIDTH - 3;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT - 3) y = TankClient.GAME_HEIGHT - Tank.HEIGHT - 3;
		
		//让敌方坦克每移动随机生成的step步后改变移动方向，继续随机生成一个step移动
		if(!good) {
			Direction dirs[] = Direction.values(); //将enum转化为数组，enum不能通过下标取元素
			if(step == 0) {
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			
			step --;
			
			if(r.nextInt(40) >= 38) this.fire(); //避免每画一次图机器坦克就发一颗子弹
		}
	}
	
	private void stay() { //让坦克每次撞墙后都回到上一步的位置（防止坦克每次碰撞检测时自己都在墙上，然后一直STOP）
		this.x = oldX;
		this.y = oldY;
	}
	
	//键盘"按下设"为 true，"松开"设为 false
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_F2:
			if(!this.live) {
				this.live = true;
				this.blood = BLOOD;
			}
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_LEFT :
			bL = true;
			break;
		case KeyEvent.VK_UP :
			bU = true;
			break;
		case KeyEvent.VK_RIGHT :
			bR = true;
			break;
		case KeyEvent.VK_DOWN :
			bD = true;
			break;
		}
		locateDirection();
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key) {
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_B:
			superFire();
			break;
		case KeyEvent.VK_LEFT :
			bL = false;
			break;
		case KeyEvent.VK_UP :
			bU = false;
			break;
		case KeyEvent.VK_RIGHT :
			bR = false;
			break;
		case KeyEvent.VK_DOWN :
			bD = false;
			break;
		}
		locateDirection();
	}
	
	//判断坦克当前朝的方向
	void locateDirection() {
		if(bL && !bU && !bR && !bD) dir = Direction.L;
		else if(bL && bU && !bR && !bD) dir = Direction.LU;
		else if(!bL && bU && !bR && !bD) dir = Direction.U;
		else if(!bL && bU && bR && !bD) dir = Direction.RU;
		else if(!bL && !bU && bR && !bD) dir = Direction.R;
		else if(!bL && !bU && bR && bD) dir = Direction.RD;
		else if(!bL && !bU && !bR && bD) dir = Direction.D;
		else if(bL && !bU && !bR && bD) dir = Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
	}
	
	//延炮筒发射炮弹
	public Missile fire() {
		if(!live) return null; //坦克死了，就不能再发子弹
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, ptDir, this.tc); //传当前坦克的好坏good给该坦克子弹的好坏
		tc.missiles.add(m);
		return m;
	}
	
	//可以任意方向发射炮弹
	public Missile fire(Direction dir) {
		if(!live) return null; //坦克死了，就不能再发子弹
		int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
		int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
		Missile m = new Missile(x, y, good, dir, this.tc); //传当前坦克的好坏good给该坦克子弹的好坏
		tc.missiles.add(m);
		return m;
	}
	
	//发射超级炮弹
	public void superFire() {
		Direction dirs[] = Direction.values();
		for(int i=0; i<8; i++) {
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	/**
	 * 
	 * @param w 被撞的墙
	 * @return 撞上返回true、没撞上返回false
	 */
	//坦克撞墙处理
	public boolean collidesWithWall(Wall w) {
		if(this.getRect().intersects(w.getRect()) && this.live) {
			this.stay(); 
			return true;
		}
		return false;
	}
	
	//坦克之间相撞处理
	public boolean collidesWithTanks(java.util.List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this != t) { //防止坦克自己和自己碰撞检测
				if(this.getRect().intersects(t.getRect()) && this.live && t.isLive()) {
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	//内部BloodBar类
	private class BloodBar {
		public void draw(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-12, WIDTH, 6); //画空心矩形
			int w = WIDTH * blood/100;
			g.fillRect(x, y-12, w, 6); //画实心矩形
			g.setColor(c);
		}
	}
	
	public boolean eat(BloodBag bb) {
		if(this.live && bb.isLive() && this.blood != BLOOD && this.getRect().intersects(bb.getRect())) {
			this.blood = BLOOD;
			bb.setLive(false);
			return true;
		}
		return false;
	}

}
