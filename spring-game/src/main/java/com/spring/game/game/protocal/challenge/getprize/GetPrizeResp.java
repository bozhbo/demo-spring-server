package com.snail.webgame.game.protocal.challenge.getprize;

import java.util.*;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.challenge.queryBattleDetail.ChapterDetailRe;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;

public class GetPrizeResp extends MessageBody {

	private int result;
	private int num; //道具数量 list.size()
	private List<BattlePrize> list = new ArrayList<BattlePrize>(); //奖励道具
	
	private ChapterDetailRe chapterDetailRe = new ChapterDetailRe(); //已开启宝箱

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("num", 0);
		ps.addObjectArray("list", "com.snail.webgame.game.protocal.challenge.getprize.PrizeRe", "num");
		
		ps.addObject("chapterDetailRe");
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public List<BattlePrize> getList() {
		return list;
	}

	public void setList(List<BattlePrize> list) {
		this.list = list;
	}

	public ChapterDetailRe getChapterDetailRe() {
		return chapterDetailRe;
	}

	public void setChapterDetailRe(ChapterDetailRe chapterDetailRe) {
		this.chapterDetailRe = chapterDetailRe;
	}
	
}
