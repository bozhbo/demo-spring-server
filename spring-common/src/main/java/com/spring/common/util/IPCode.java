package com.spring.common.util;

public class IPCode {

	public static int IPtoInt(String ip)
	{  	
		int res=0;
		if(ip!=null&&ip.length()>0)
		{
		  String[] aip=ip.split("[.]");
		  if(aip!=null&&aip.length==4)
		  {
			  for ( int i=0;i<4;i++)
			  {
				  res = res+Integer.parseInt(aip[i]);
				  if(i<3)
				  res = res<<8;				  
			  }
		  }
		}
		  return res;
	}
	public static String intToIP(int longIP)
    {
         StringBuffer sb=new StringBuffer("");
         sb.append(String.valueOf(longIP>>>24));
         sb.append(".");      
         sb.append(String.valueOf((longIP&0x00FFFFFF)>>>16)); 
         sb.append(".");
         sb.append(String.valueOf((longIP&0x0000FFFF)>>>8));
         sb.append(".");
         sb.append(String.valueOf(longIP&0x000000FF));
         return sb.toString(); 
    } 
}
