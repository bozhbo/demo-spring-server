package org.epilot.ccf.mapping;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.epilot.ccf.codec.MethodObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public  class MappingMethodCache {
	private static final Logger logger=LoggerFactory.getLogger("logs");
	private final static ConcurrentHashMap<String, Future<MethodObject>> mapField =new ConcurrentHashMap<String, Future<MethodObject>>();
	private final static MappingMethodCache cache =new MappingMethodCache();
	private MappingMethodCache()
	{
		
	}
	public  synchronized static MappingMethodCache getInstance()
	{
		return cache;
	}

/**
 * 缓存对象方法及方法类型
 * @param object
 * @param key
 * @param it
 * @return
 */

	public   MethodObject getSetMethod(final Object object,final String key ,final Iterator it)
	{
		while(true)
		{
		Future<MethodObject> future = mapField.get(key);
		if(future ==null)
		{
			Callable<MethodObject> eval = new Callable<MethodObject>()
			{
				public MethodObject call() throws InterruptedException
				{
					Method []method = null;
					try {
						method = object.getClass().getMethods();
					
					} catch (Exception e) {
						e.printStackTrace();
					} 
					HashMap<String,Method> map = new HashMap<String,Method>();
					if(method ==null||method.length==0)
					{
						return null;
					}
					
					for(int k=0;k<method.length;k++)
					{
						map.put(method[k].getName().toUpperCase(), method[k]);
						map.put(method[k].getName().toUpperCase(), method[k]);
					}
					MethodObject methodObject = new MethodObject();
					
					LinkedHashMap setList  = new LinkedHashMap();
					LinkedHashMap getList  = new LinkedHashMap();
					ArrayList<String> setListType  = new ArrayList<String>();
					ArrayList<String> getListType  = new ArrayList<String>();
					ArrayList<String> setListCode  = new ArrayList<String>();
					ArrayList<String> getListCode  = new ArrayList<String>();
					while (it.hasNext()) {
						String name = (String) it.next();
						name = name.toUpperCase();
						Object obj = it.next();
						String endian = "0";
						try{
						if(obj instanceof Integer)
						{
							endian = String.valueOf( obj);
							setList.put(name,map.get("SET"+name));
							setListType.add(map.get("SET"+name).getParameterTypes()[0].toString());
							getList.put(name,map.get("GET"+name));
							getListType.add(map.get("GET"+name).getReturnType().toString());
							setListCode.add(endian);
							getListCode.add(endian);
						}
						else
						{
							setList.put(name,map.get("SET"+name));
							getList.put(name,map.get("GET"+name));
							if(obj.equals("class-"))//自定义类对象
							{
								Method methodObj = map.get("GET"+name);
								setListType.add("class-"+methodObj.getReturnType().getName());
								getListType.add("class-"+methodObj.getReturnType().getName());
								setListCode.add("0");
								getListCode.add("0");
							}
							else if(obj.equals("classArray-"))//自定义类对象数组
							{
								
								String className = (String) it.next();
								String numName = (String) it.next();
								setListType.add("classArray-"+className+"-"+numName.toUpperCase());
								getListType.add("classArray-"+className+"-"+numName.toUpperCase());
								setListCode.add("0");
								getListCode.add("0");
							}
							else
							{
								String HandleClassName = (String)obj;//AbstractStringHandle
								if(HandleClassName!=null&&HandleClassName.trim().length()>0)
								{
									setListType.add("String-"+HandleClassName);
									getListType.add("String-"+HandleClassName);
								}
								else
								{
									setListType.add(map.get("SET"+name).getParameterTypes()[0].toString());
									getListType.add(map.get("GET"+name).getReturnType().toString());
								}
								endian =String.valueOf( it.next());
								setListCode.add(endian);
								getListCode.add(endian);
							}
						}
						}
						catch(Exception e)
						{
							if(logger.isErrorEnabled())
							{
								logger.error(object.getClass().getName()+" class's method"+name+" is config error!");
							}
						 
						}
					}
					methodObject.setSetMethod(setList);
					methodObject.setGetMethod(getList);
					methodObject.setSetMethodCode(setListCode.toArray(new String[0]));
					methodObject.setGetMethodCode(getListCode.toArray(new String[0]));
					methodObject.setSetMethodType(setListType.toArray(new String[0]));
					methodObject.setGetMethodType(getListType.toArray(new String[0]));
					return methodObject;
				}
			};
			FutureTask<MethodObject> futuretask =new FutureTask<MethodObject>(eval);
			future = mapField.putIfAbsent(key, futuretask);
			if(future==null)
			{
				future= futuretask;
				futuretask.run();
			}
		}
			try {
				return future.get();
			} catch (Exception e) {
				mapField.remove(key,future);
				e.printStackTrace();
				
		}
	  }
	}
}
