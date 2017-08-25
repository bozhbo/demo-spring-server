package com.snail.webgame.engine.gate.util;

import com.snail.webgame.engine.common.util.CodeUtil;

public class CryptoTool {

	
	public static String getCryptoType(String encryptCode, int encryptType,int type)
	{
		switch (type)
		{
			case 0:
				 return getCryptoType(encryptCode,encryptType
						 ,"jsi2",new int[]{13, 11 ,24 ,18 ,3, 26 ,15, 19},new int[]{31, 12, 21, 30, 5 ,17, 27 ,7}
				 				,new int[]{4, 8 ,14, 23, 1, 25, 28, 20});
			case 1:
				return getCryptoType(encryptCode,encryptType
						 ,"ksov",new int[]{13, 11, 28, 18 ,3, 8 ,23 ,7},new int[]{31 ,26, 24 ,19,5,17, 27,30}
				 				,new int[]{4, 12, 14 ,15 ,1, 25, 21 ,20});
			case 2:
				return getCryptoType(encryptCode,encryptType
						 ,"j93k",new int[]{13, 11, 28 ,18 ,1, 8, 27 ,30 },new int[]{31, 3, 24,19,5,17, 20,7}
				 				,new int[]{4, 26 ,14, 15 ,12, 25, 21 ,23});
			case 3:
				return getCryptoType(encryptCode,encryptType
						 ,"b03s",new int[]{26, 11 ,14, 18, 20 ,8 ,5 ,30},new int[]{31 ,3 ,24 ,19 ,12,17 ,27,7}
				 				,new int[]{4, 13, 28 ,15 ,1 ,25, 21 ,23});
			case 4:
				return getCryptoType(encryptCode,encryptType
						 ,"oo2s",new int[]{26 ,13, 28 ,1 ,12, 17, 5 ,27},new int[]{31, 3, 24 ,19, 20 ,8 ,30,7}
				 				,new int[]{4 ,11, 14, 15, 18 ,25, 21, 23});
			case 5:
				return getCryptoType(encryptCode,encryptType
						 ,"blso",new int[]{26, 28 ,3, 1 ,18 ,8, 5, 27},new int[]{31, 11 ,24, 19, 20 ,17,30,7}
				 				,new int[]{4 ,13, 14, 12, 15 ,25, 21, 23});
			case 6:
				return getCryptoType(encryptCode,encryptType
						 ,"8s02",new int[]{26, 11, 3, 1 ,4, 31, 5 ,23},new int[]{8 ,28, 24 ,19, 15, 17, 30,7}
				 				,new int[]{20, 13, 14 ,12, 18 ,25 ,21, 27});
			case 7:
				return getCryptoType(encryptCode,encryptType
						 ,"kol2",new int[]{26 ,11 ,3 ,19 ,4 ,31, 5, 27},new int[]{ 8, 28, 24, 12, 15 ,17,30,7}
				 				,new int[]{20, 13, 14 ,23 ,18 ,25, 21, 1});
			case 8:
				return getCryptoType(encryptCode,encryptType
						 ,"lvo2",new int[]{31, 11, 3, 1, 12 ,8, 5, 27},new int[]{7, 25 ,24, 19, 20 ,17 ,30, 26}
				 				,new int[]{20, 13 ,14, 15, 18 ,28, 21, 23});
			case 9:
				return getCryptoType(encryptCode,encryptType
						 ,"kl2s",new int[]{18, 11, 14, 1, 15 ,8 ,19 ,26},new int[]{31, 25, 24 ,5 ,20 ,17, 3, 27}
				 				,new int[]{20, 13, 30 ,12, 7, 28 ,21, 23});
			default :
				return null;
		}
	}
	
	
	
	private static String getCryptoType(String encryptCode, int encryptType,String strKey,int[] index1,int[]  index2,int[]  index3 )
	{
		encryptCode = encryptCode.substring(0,encryptCode.length()-4)+strKey;
		
		String str = CodeUtil.Md5(encryptCode.toUpperCase());
		
		
		char[] c = str.toCharArray();
		String str1 = ""+c[index1[0]]+c[index1[1]]+c[index1[2]]+c[index1[3]]+c[index1[4]]+c[index1[5]]+c[index1[6]]+c[index1[7]];
		String str2 = ""+c[index2[0]]+c[index2[1]]+c[index2[2]]+c[index2[3]]+c[index2[4]]+c[index2[5]]+c[index2[6]]+c[index2[7]];
		String str3 = ""+c[index3[0]]+c[index3[1]]+c[index3[2]]+c[index3[3]]+c[index3[4]]+c[index3[5]]+c[index3[6]]+c[index3[7]];
		if(encryptType==1||encryptType==2)
		{
			encryptCode = str1;
		}
		else if(encryptType==3)
		{
			encryptCode = str1+str2+str3;
		}
		else if(encryptType==4)
		{
			encryptCode = str1+str2;
		}
		
		
		return encryptCode.toUpperCase();
	}
	
	
	
}
