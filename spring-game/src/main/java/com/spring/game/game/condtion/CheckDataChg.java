package com.snail.webgame.game.condtion;

/**
 * 检测数据变化
 * 
 * @author nijp
 *
 */
public interface CheckDataChg {
	
	/**
	 * 获取特定索引位置的值。
	 * 
	 * @param index
	 * @return
	 */
	public long getSpecValue(int index);

	/**
	 * 更新特定索引位置的值。
	 * 
	 * @param index
	 * @param value
	 */
	public void updateInterValue(int index, long value);
	
	/**
	 * 设置版本中间值变化
	 * 
	 * @param versionChg
	 */
	public void setVersionChg(boolean versionChg);
	
	/**
	 * 设置完成状态
	 */
	public void setFinishState();
}
