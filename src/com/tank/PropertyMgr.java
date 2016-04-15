package com.tank;

/*��ȡ"�����ļ�"����*/
import java.io.IOException;
import java.util.Properties; //��properties�������ļ����в����İ�

/*
 *  "Singletonģʽ(��:����ģʽ)"����Ч��(����"��Ҫ���õ���"�ľ�̬�����þ�̬���������Ǿ�̬������Ȼ����һ��ͬ���ľ�̬����"��װ"��)
 *   //Ϊ�˱���ÿ�ε���getProperty()��Ҫ�ȴ���һ�����󣬵���ģʽ�����ͨ����������
 *   //��Ҫ�½���folder��folder���½���file���ļ���д�ɡ�tank.PROPERTIES�����������ļ���
 */
public class PropertyMgr {
	static Properties props = new Properties(); //��Ҫ�õ������"��̬����"(������"static{}��static����"�иö���û����)
	
	static {
		try {
			//"load()"�Ǵ�tank.properties�У��������У���ȡ�����б�����Ԫ�ضԣ�
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private PropertyMgr() {} //�����췽������ɡ�˽�еġ������ⲿ����ʵ�����ö��󣨷�ֹ�ⲿ�ö�����(��������)�����Լ���static������
	
	public static String getProperty(String key) { //ʵ�ֶ�Properties���е�getProperty()�����ķ�װ
		return props.getProperty(key); 
	}
	/*
	 *  �����Կ�����дgetProperty()������public static String getProperty()��װ
	 */
}
