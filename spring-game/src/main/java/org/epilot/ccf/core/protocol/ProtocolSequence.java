package org.epilot.ccf.core.protocol;


import java.util.Iterator;
import java.util.LinkedList;


/**
 * 
 * 设置协议字段顺序
 *
 */
public  class ProtocolSequence {
	private  LinkedList<Comparable> list =new LinkedList<Comparable>();
	
	/**
	 * 添加一个协议字段
	 * @param name 字段名称 
	 * @param resource 处理字符串编码解码的资源类
	 */
	public void addString(String name,String resource,int endian)
	{
		list.add(name);
		list.add(resource);
		list.add(endian);
	}
	/**
	 * 添加一个协议字段
	 * @param name 字段名称 
	 * @param endian 字节类型 0--bigEndian 1--littleEndian
	 * 
	 */
	public void add(String name,int endian)
	{
		list.add(name);
		list.add(endian);
	}
	/**
	 * 添加一个对象字段
	 * @param name 字段名称 
	 */
	public void addObject(String name)
	{
		list.add(name);
		list.add("class-");
	}
	/**
	 * 添加一个对象数组字段
	 * @param name 字段名称 
	 * @param className list中的对象类型
	 * @param List 大小对应字段
	 */
	public void addObjectArray(String name,String className,String num)
	{
		list.add(name);
		list.add("classArray-");
		list.add(className);
		list.add(num);
	}

	
	public Iterator get()
	{
		return list.iterator();
	}

	public int size()
	{
		return list.size();
	}
}
