package com.spring.common.util;

import java.awt.Point;
import java.util.Random;

import com.spring.common.pathfinding.astar.Step;




public class CommUtil {

	private static double tan60  = 0.5773502691896257d;
	/**
	 * 随机抽取数值，范围是(0～maxNum-1）
	 * @param maxNum
	 * @return
	 */
	public static int getRandomData(int maxNum)
	{
		int random = -1;
		
		if(maxNum<=0)
		{
			
		}
		else
		{
			random =  new Random().nextInt(maxNum);
		}
		
		 
		return random;
	}
	
	
	/**
	 * 在一定范围内获得指定不重复的数字(取值范围不能大于100)
	 * @param getNum 获得数字
	 * @param maxNum 指定范围
	 * @return
	 */
	public static int[]  getRandomData(int getNum,int maxNum)
	{
		if(getNum<=0||maxNum<=0)
		{
			return null;
		}
		
		
		int a[]=new int[getNum];
		int b[]=new int[maxNum];
		long time = System.currentTimeMillis();
		Random r=new Random();
	
		if(getNum>maxNum)
		{
			return null;
		}
		for(int i=0;i<getNum;i++)
		{
		   a[i]=r.nextInt(maxNum);
		   if(b[a[i]]>0)
		   {
		     i--;
		   }
		   else
		   {
		     b[a[i]]=1;
		   }
		   if(System.currentTimeMillis()-time>1000)//1秒超时
		   {
			   return null;
		   }
		}
		return a;
			
	}
	
	
	
	
	/**
	 * 判断是否能取道机率 只能精确到百万分之一
	 * @param d 小于1大于 0 的值
	 * @return
	 */
	public static boolean isGetRank(double d)
	{
		if(d<0)
		{
			return false;
		}
		if(d>=1)
		{
			return true;
		}
	   
 
		int rank = (int) (d*Math.pow(10,6));
 
		int value = getRandomData((int) Math.pow(10,6));
		
		if(rank>value)
		{
			return true;
		}
		
		return false;
		
	}
	/**
	 * 计算以base为底value的对数
	 * @param value
	 * @param base
	 * @return -1-未错误
	 */
	public static double log(double value, double base)
	{
		if(base != 1)
		{
			return Math.log(value)/Math.log(base);
		}
		else
		{
			return -1;
		}
		
	}
	/**
	 * 向上取整
	 * @param value 小于最大Integer值
	 * @return
	 */
	public static int upInt(double value)
	{
		if(value > 0)
		{
			if(value > (int)value)
			{
				return (int)value + 1;
			}
			return (int)value;
		}
		else
		{
			return (int)value;
		}
	}
	
	public static double getRandomData(double minNum,double maxNum,int decimalDigits)
	{
	 
		double a =  minNum * Math.pow(10d, decimalDigits);
		double b = maxNum * Math.pow(10d, decimalDigits);
		
		int c = (int) (b-a);
		int k = getRandomData(c);

		double  d = a + k;
 
		d = d*1d/ Math.pow(10d, decimalDigits);

		return d;
	}
	
	
	 public static int byteArrayToInt(byte[] b) {
	        int value = 0;
	        for (int i = 0; i < b.length; i++) {
	            int shift = (b.length - 1 - i) * 8;
	            value += (b[i] & 0xFF) << shift;
	        }
	        return value;
	    } 
	 
	 public static byte[] int2bytes(int n) {
		 byte[] b = new byte[4];
		 for(int i = 0;i < 4;i++){
		 b[i] = (byte)(n >> (24 - i * 8)); 
		 }
		 return b;
	}
	public  byte[] shortToBytes(short sNum){
		    byte[] bytesRet = new byte[2];
		    bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
		    bytesRet[1] = (byte) (sNum & 0xFF);
		    return bytesRet;
	} 
	public static short bytesToShort(byte[] bytesShort) {
	    short sRet = 0;
	    sRet += (bytesShort[0] & 0xFF) << 8;
	    sRet += bytesShort[1] & 0xFF;
	    return sRet;
	}
	public static int getDirectX (int x,int y,int length)
	{
		return (int) ((x-y/2d)+(y&1)/2d+(length/2d-1d));	 
	}
	public static int getDirectY (int x,int y)
	{
		return (int) ((y+(y&1))/2+x);
	}
	
	
	public static int getLogicY(int x,int y,int length)
	{
		return (int)(y-x+(length/2d-1d));
	}
	public static int getLogicX(int x,int logicY,int length)
	{
		return (int)(x - (( logicY & 1 ) + (length/2d-1d) ) + logicY / 2d + ( logicY & 1 ) / 2d);	
	}
	
	public static Point LogicToStage(int x,int y,int TileWidth,int TileHeigth) 
	{
		
		int x1 = x * TileWidth + ( y & 1) * ( TileWidth / 2 );
		int y1 = y * TileHeigth / 2;
		
		Point  point= new Point(x1,y1);
		
		return point;
		
	}
	
	public static Point StageToLogic(Point stage,int TileWidth,int TileHeigth)
	{
		Point logic = new Point();
		
		Point possibleValue_1 ;
		Point possibleValue_2 ;
	 
		//加入X的半格除法结果为偶数，那么X可确定
		int littleX = (int)(stage.x / (TileWidth / 2));
		int littleY = (int)(stage.y / (TileHeigth / 2));
		if ((littleX & 1) != 0)
		{
			logic.x = (littleX - 1) / 2;
			//再来检测littleY
			if ((littleY & 1) != 0)
			{
				possibleValue_1 = new Point(logic.x, littleY - 1);
				possibleValue_2 = new Point(logic.x, littleY);
				if (!SelectLogic(stage, 1,TileWidth,TileHeigth))
				{
					return possibleValue_1;
				}
				else
				{
					return possibleValue_2;
				}
			}
			else
			{
				possibleValue_1 = new Point(logic.x, littleY - 1);
				possibleValue_2 = new Point(logic.x, littleY);
				if (!SelectLogic(stage, 2,TileWidth,TileHeigth))
				{
					return possibleValue_1;
				}
				else
				{
					return possibleValue_2;
				}
			}
		}
		else
		{
			if ((littleY & 1) != 0)
			{
				possibleValue_1 = new Point((int)((littleX + 1) / 2), littleY - 1);
				possibleValue_2 = new Point((int)((littleX + 1) / 2) - 1, littleY);
				if (!SelectLogic(stage, 4,TileWidth,TileHeigth))
				{
					return possibleValue_1;
				}
				else
				{
					return possibleValue_2;
				}
			}
			else
			{
				possibleValue_1 = new Point((int)((littleX + 1) / 2), littleY);
				possibleValue_2 = new Point((int)((littleX + 1) / 2) - 1, littleY - 1);
				if (SelectLogic(stage, 3,TileWidth,TileHeigth))
				{
					return possibleValue_1;
				}
				else
				{
					return possibleValue_2;
				}
			}
		}
	 
	}
	
	private static boolean SelectLogic(Point stage, int postion ,int TileWidth,int TileHeigth)
	{
		double x  = stage.x % (TileWidth / 2);
		double y  = stage.y % (TileHeigth / 2);
		//直线方程为y = Xtan60
		if (postion == 2 || postion == 4)
		{
			if (y > x * tan60)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		if (postion == 1 || postion == 3)
		{
			if (y > x * tan60 * -1 + TileHeigth / 2)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 两点之间长度
	 * @param step1
	 * @param step2
	 * @return
	 */
	public static float getDistance(Step step1, Step step2){
		double result = Math.sqrt((step2.getX() - step1.getX()) * (step2.getX() - step1.getX()) + 
				(step2.getY() - step1.getY()) * (step2.getY() - step1.getY()));
		
		return (float)result;
	}
	
	/**
	 * 两点之间长度
	 * @return
	 */
	public static float getDistance(float x, float y, float x1, float y1){
		double result = Math.sqrt((x1 - x) * (x1 - x) + 
				(y1 - y) * (y1 - y));
		
		return (float)result;
	}
	public static void main(String args[])
	{
		System.out.println(isGetRank(0.00000026));
//	 
//		System.out.println(StageToLogic(new Point(1280,1024),100,50));
//		
		
//		int a [] = getRandomData(3,10);
//		
//		System.out.println(a.length);
		 
		
	}
}
