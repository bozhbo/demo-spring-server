package com.snail.webgame.engine.common.to;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 实体超类
 * @author zenggy
 *
 */
public abstract class BaseTO implements Cloneable{
	private List<String> updateFields = new ArrayList<String>();
	/**
	 * 即时更新
	 */
	public static final byte ONLINE = 0;
	/**
	 * 定时更新
	 */
	public static final byte LAZY = 1;
	/**
	 * 主键
	 */
	protected int id;
	
	/**
	 * 保存SQL语句ID，供定时存储时用
	 */
	private String sqlKey;
	
	
	public abstract byte getSaveMode();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSqlKey() {
		return sqlKey;
	}

	public void setSqlKey(String sqlKey) {
		this.sqlKey = sqlKey;
	}

	/**
	 * 修改需要自动同步字段的值
	 * @param field 字段名称
	 * @param value 值
	 */
	public void set(String field, Object value){
		char firstChar = field.charAt(0);
		field = field.replaceFirst(String.valueOf(firstChar), String.valueOf((char)(firstChar - 32)));
		if(!updateFields.contains(field))
			updateFields.add(field);
		
		try {
			Method[] methods = this.getClass().getMethods();
			if(methods != null && methods.length > 0){
				for(Method method : methods){
					if(method.getName().equals("set" + field)){
						method.setAccessible(true);
						method.invoke(this, new Object[]{value});
						break;
					}
					
				}
			}
				
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	public void cleanUpdateFields(){
		updateFields.clear();
	}
	
	public List<String> getUpdateFields(){
		return this.updateFields;
	}

	@Override
	public Object clone(){
		BaseTO o = null;
		
		try {
			o = (BaseTO)super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	/**
	 * 复制数据到另一个对象
	 * @param target
	 */
	public void copyTo(Object target){
		if(target == null)
			return;
		
		Method[] methods = target.getClass().getMethods();
		if(methods != null && methods.length > 0){
			for(Method method : methods){
				String methodName = method.getName();
				if(methodName.startsWith("set")){
					try {
						Method getMethod = this.getClass().getMethod("get" + methodName.substring(3, methodName.length()),null);
						if(getMethod != null){
							Object result = getMethod.invoke(this, new Object[]{});
							if(getMethod.getReturnType() == method.getGenericParameterTypes()[0]){
								method.invoke(target, new Object[]{result});
							}
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
}
