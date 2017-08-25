package com.snail.webgame.game.protocal.countryfight.querycity;

import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.countryfight.common.CityRe;

/**
 * 查询所有城市信息
 * 
 * @author xiasd
 *
 */
public class QueryCityInfoResp extends MessageBody{
	private List<CityRe> list;
	private int count;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.countryfight.common.CityRe", "count");
		ps.add("count", 0);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<CityRe> getList() {
		return list;
	}

	public void setList(List<CityRe> list) {
		this.list = list;
	}
	
}
