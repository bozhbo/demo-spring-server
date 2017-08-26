package com.snail.client.main.net.filter;

import java.util.Random;

/**
 * 支持的加密算法
 * @author leiqiang
 * @date   2014-8-1
 */
public class Arithmetic {
	
	public static final Arithmetic NO_OP = new Arithmetic(0);
	public static final Arithmetic DES = new Arithmetic(1);
	//public static final Arithmetic BlowFish = new Arithmetic(2);
	public static final Arithmetic DES3 = new Arithmetic(3);
	public static final Arithmetic AES = new Arithmetic(4);
 
	public static final Arithmetic RANDOM = new Arithmetic(99);
	private static Arithmetic[] ra = new Arithmetic[]{DES, /*DES3,*/ AES};
	private int value;
	public Arithmetic(int value)
	{
		this.value = value;
	}
	public int getValue()
	{
		return value;
	}
	/**
	 * 获取一个随机算法
	 * @return
	 */
	public static Arithmetic randomArithmetic()
	{
		return ra[new Random().nextInt(ra.length)];
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+value;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof Arithmetic)
		{
			Arithmetic a = (Arithmetic)obj;
			if(this.value == a.value)
			{
				return true;
			}
		}
		return false;
	}
}
