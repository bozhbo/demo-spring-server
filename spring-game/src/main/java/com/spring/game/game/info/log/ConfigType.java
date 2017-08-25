package com.snail.webgame.game.info.log;

/**
 * 日志 gameConfigType 枚举类型值
 * @author zenggang
 *
 */
public enum ConfigType {

	gameAction(1, "玩家行为标识"), // s_event_id
	task(2, "任务编号"), // task_log s_task_id
	prop(3, "道具编号"), // item_log s_item_id
	hero(4, "武将编号"), // item_log s_item_id
	equip(5, "装备编号"), // item_log s_item_id
	weapan(6, "神兵编号"), // item_log s_item_id
	skill(7, "技能编号"), // role_upgrade_log -s_skill_id
	fightType(8, "战斗类型标识"), // instance_log -s_instancetypeid
	mine(9, "矿类型"),
	condition(10, "条件类型"),
	teamChallenge(11, "组队副本"),
	payment(12, "礼包");


	private int type;
	private String name;

	private ConfigType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
