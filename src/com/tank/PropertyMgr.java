package com.tank;

/*获取"配置文件"的类*/
import java.io.IOException;
import java.util.Properties; //对properties的配置文件进行操作的包

/*
 *  "Singleton模式(即:单例模式)"增加效率(定义"需要调用的类"的静态对象，用静态对象调用其非静态方法，然后定义一个同样的静态方法"封装"它)
 *   //为了避免每次调用getProperty()都要先创建一个对象，单例模式后可以通过类名调用
 *   //需要新建个folder，folder下新建个file，文件名写成“tank.PROPERTIES”就是配置文件了
 */
public class PropertyMgr {
	static Properties props = new Properties(); //需要用到的类的"静态对象"(否者在"static{}和static方法"中该对象还没生成)
	
	static {
		try {
			//"load()"是从tank.properties中（即：流中）读取属性列表（键和元素对）
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private PropertyMgr() {} //将构造方法定义成“私有的”，类外部不能实例化该对象（防止外部用对象名(不用类名)调用自己的static方法）
	
	public static String getProperty(String key) { //实现对Properties类中的getProperty()方法的封装
		return props.getProperty(key); 
	}
	/*
	 *  还可以可以重写getProperty()方法用public static String getProperty()封装
	 */
}
