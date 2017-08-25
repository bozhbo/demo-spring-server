package org.epilot.ccf.config;

import java.util.Map;

import org.epilot.ccf.common.ClassResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统读取资源配置文件
 * @author tangjie
 *
 */
public class Resource {
	
	 private static final Logger log =LoggerFactory.getLogger("ccf");
	public Resource()
	
	{
		
	}
	/**
	 * 通过配置文件读取资源文件获取相关值
	 * @param name 配置文件中的 key
	 * @param param	 参数
	 */
	public  static String getMessage(String name,String param)
	{
		Map mapResource = ConfigInit.getInstance().getMessageResource(name);
		String result =null;
		if(mapResource!=null)
		{
			result = (String) mapResource.get(param);
		}
		else
		{
			if(log.isWarnEnabled())
			{
				log.warn("can not find text:properties="+name+",param="+param);
			}
			
		}
		return result;	
	}
	/**
	 * 通过配置文件获得类信息
	 * @param name
	 * @return
	 */
	public  ClassResources getClassResources(String name)
	{
		Map<String, ClassResources> map= Config.getInstance().getClassResource();
		ClassResources c = null;
		if(map!=null)
		{
			c=(ClassResources) map.get(name);
		}
	
		return c;	
	}
}
