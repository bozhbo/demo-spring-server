package com.spring.common.util;

import java.text.DecimalFormat;

public class DataFormat {


	public static String getFormatFloat(float f)
	{
		DecimalFormat   df   =   new   DecimalFormat("#0.0");   
		
		String str = df.format(f);
 
		return str;
	}
 
 
	public static String getDouble1Scale(double d)
	{
		double d1 = new Double(new DecimalFormat("#0.0").format(d));
		
		return String.valueOf(d1);
	}
	
	public static String getDouble2Scale(double d)
	{
		double d1 = new Double(new DecimalFormat("#0.00").format(d));
		
		return String.valueOf(d1);
	}
	
	
	
	public static String getDouble3Scale(double d)
	{
		double d1 = new Double(new DecimalFormat("#0.000").format(d));
		
		return String.valueOf(d1);
	}
	
	public static String getDouble4Scale(double d)
	{
		double d1 = new Double(new DecimalFormat("#0.0000").format(d));
		
		return String.valueOf(d1);
	}
	
	public static double double3Scale(double d)
	{
		return new Double(new DecimalFormat("#0.000").format(d));
	}
	
	public static double double1Scale(double d)
	{
		return new Double(new DecimalFormat("#0.0").format(d));
	}
	
	public static float float2Scale(float f)
	{
		return new Float(new DecimalFormat("#0.00").format(f));
	}
}
