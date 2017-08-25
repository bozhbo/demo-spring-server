package com.snail.webgame.game.protocal.recruit.service;

import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.xml.info.RecruitKindXMLInfo;


public class ChestService {
	
	/**
	 * 获取抽卡细分类型
	 * @param kind
	 * @return
	 */
	public static int getChestNo(RecruitKindXMLInfo kind)
	{
		if(kind.getNo() == 1)
		{
			return ActionType.action66.getType();
		}
		else if(kind.getNo() == 2)
		{
			return ActionType.action67.getType();
		}
		else if(kind.getNo() == 3)
		{
			return ActionType.action68.getType();
		}
		else if(kind.getNo() == 4)
		{
			return ActionType.action69.getType();
		}else if(kind.getNo() == 7)
		{
			return ActionType.action437.getType();
		}else if(kind.getNo() == 8)
		{
			return ActionType.action438.getType();
		}
		
		return 0;
	}
}
