package com.snail.webgame.game.protocal.equip.enchant;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipEnchantReq extends MessageBody {
	private int heroId; // 英雄Id (0-未被装备)
	private int equipId; // 装备Id
	private String enchantItems; // 附魔材料(itemNo,itemNum;itemNo,itemNum;)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.add("equipId", 0);
		ps.addString("enchantItems", "flashCode", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public String getEnchantItems() {
		return enchantItems;
	}

	public void setEnchantItems(String enchantItems) {
		this.enchantItems = enchantItems;
	}
}
