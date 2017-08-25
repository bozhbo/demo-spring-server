package com.snail.webgame.game.common;

import java.util.HashMap;

public enum ActionType {
	
	action5(5, "用户体力定时回复"),
	action6(6, "gm命令"),
	action7(7, "领取邮件附件"),
	action10(10, "银子购买"),
	action13(13, "体力购买"),
	action14(14, "国家任务-在大地图攻击玩家"),
	action15(15, "角色名修改"),
	action17(17, "拜访贤者"),
	action18(18, "在某个NPC驻足"),
	action19(19, "大地图攻击NPC开始"),
	action27(27, "大地图攻击NPC结束"),
	action20(20, "领取功能开启获得道具"),
	action21(21, "战斗强制退出"),
	action22(22, "用户精力定时回复"),
	action23(23, "精力购买"),
	action24(24, "物品合成获得"),
	action25(25, "用户技能点定时回复"),
	action26(26, "技能点购买"),
	action28(28, "大地图攻击镜像开始"),
	action29(29, "大地图攻击镜像结束"),
	action30(30, "刺探世界地图城市"),
	action31(31, "每日签到奖励"),
	action32(32, "重置未签到天数"),
	action33(33, "7日签到奖励"),
	action34(34, "等级礼包"),
	action41(41, "背包物品出售"),
	action42(42, "背包道具使用"),
	action57(57, "兵法升级"),
	action58(58, "兵法升级使用金子提升概率"),
	action66(66, "银子单抽"),
	action67(67, "银子十连抽"),
	action68(68, "装备单抽"),
	action69(69, "装备十连抽"),
	action71(71, "抽卡"),
	action72(72, "副将升级"),
	action73(73, "副将升星"),
	action74(74, "副将星石合成武将"),
	action75(75, "副将技能升级"),
	action76(76, "副将觉醒提升"),
	action77(77, "主角装备强化"),
	action78(78, "副将装备合成"),
	action79(79, "副将穿装备"),
	action81(81, "装备宝石镶嵌(弃用)"),
	action82(82, "宝石合成(弃用)"),
	action83(83, "宝石摘除(弃用)"),
	action84(84, "主角穿脱装备"),
	action85(85, "武将布阵"),
	action86(86, "装备熔炼"),
	action87(87, "主角装备精炼"),
	action88(88, "主角使用道具"),
	action89(89, "装备重铸"),
	action90(90, "角色技能升级"),
	action91(91, "武将羁绊位置布阵开启"),
	action92(92, "装备附魔"),
	
	action101(101, "副本开始"),
	action102(102, "领取副本宝箱"),
	action103(103, "普通副本结束"),
	action104(104, "普通副本扫荡"),
	action105(105, "重置副本次数"),
	action106(106, "英雄副本扫荡"),
	action107(107, "英雄副本结束"),
	action108(108, "关卡防守开始"),
	action109(109, "关卡防守结束"),
	action110(110, "关卡对攻开始"),
	action111(111, "关卡对攻结束"),
	action141(141, "竞技场战斗开始"),
	action142(142, "竞技场战斗结束"),
	action143(143, "购买竞技场战斗次数"),
	action144(144, "重置 cd 时间"),
	action181(181, "领取任务奖励"),
	action182(182, "任务检测"),
	action183(183, "任务一键完成"),
	
	action221(221, "手动刷新商店物品信息"),
	action222(222, "购买商店物品信息"),
	action223(223, "使用道具刷新商店"),
	
	action281(281, "重置宝石活动"),
	action282(282, "购买宝石重置次数"),
	action283(283, "扫荡"),
	action284(284, "宝石活动战斗开始"),
	action285(285, "宝石活动战斗结束奖励"),
	action286(286, "领取活动关卡奖励"),
	
	action301(301, "重置宝物活动"),
	action302(302, "购买宝物重置次数"),
	action303(303, "宝物活动战斗开始"),
	action304(304, "宝物活动战斗结束"),
	action305(305, "领取活动关卡奖励"),
	action306(306, "复活武将继续战斗"),
	action307(307, "扫荡宝物活动"),
	
	action325(325, "经验活动战斗开始"),
	action321(321, "经验活动战斗结束"),
	action322(322, "银币活动战斗结束奖励"),
	action323(323, "购买经验活动次数"),
	action324(324, "购买银币活动次数"),
	action326(326, "银币活动战斗开始"),
	action351(351, "PVP竞技场奖励领取"),
	action352(352, "PVP竞技场战斗结束发放"),
	action353(353, "PVP竞技场战斗开始"),
	action354(354, "PVP竞技场战斗结束"),
	action355(355, "PVP竞技场战斗扣除"),
	action356(356, "世界地图一对一开始"),
	action357(357, "世界地图一对一结束"),
	
	
	action358(358, "世界地图劫镖"),
	action359(359, "宝石升级"),
	action361(361, "开启安全模式"),
	action362(362, "你争我夺"),
	action363(363, "宝石碎片合成"),
	action364(364, "狭路相逢开始"),
	action365(365, "狭路相逢结束"),
	action367(367, "世界BOSS战斗开始"),
	action369(369, "世界BOSS战斗结束"),
	action368(368, "刷新世界BOSS战斗"),
	action370(370, "兵来将挡开始"),
	action371(371, "兵来将挡结束"),
	action377(377, "兑换黄金"),
	action378(378, "计费购买道具"),
	action379(379, "镖车刷新"),
	action380(380, "押镖奖励"),
	action381(381, "护镖奖励"),
	action382(382, "PVP对攻战开始"),
	action383(383, "PVP对攻战结束"),
	action384(384, "创建公会"),
	action386(386, "购买黑市商城使用权限"),
	action387(387, "购买异域商城使用权限"),
	action388(388, "劫镖开始"),
	action389(389, "劫镖结束"),
	
	action390(390, "VIP购买橙色镖车"),
	action400(400, "领取首充奖励"),
	action401(401, "领取在线礼包奖励"),
	action402(402, "领取手机绑定奖励"),
	action403(403, "领取首次微信分享奖励"),
	action404(404, "领取每日微信分享奖励"),
	action405(405, "领取七日活动奖励"),
	action406(406, "购买七日活动半价礼包"),
	action407(407, "领取精彩活动奖励"),
	action408(408, "购买投资计划"),
	action409(409, "领取时限活动奖励"),
	action410(410, "限时武将抽奖"),
	action411(411, "金币消费"),
	action412(412, "累计充值时限活动检测"),
	
	action431(431, "充值购买金子"),
	action432(432, "充值购买会员卡"),
	action433(433, "充值购买礼包"),
	action434(434, "领取vip等级礼包"),
	action435(435, "金子购买促销礼包"),
	action436(436, "购买vip特权礼包"),
	
	action437(437, "武将单抽"),
	action438(438, "武将十连抽"),
	
	action439(439, "充值购买月卡"),
	action440(440, "充值月卡升季卡"),
	action441(441, "充值月卡升年卡"),
	action442(442, "充值购买季卡"),
	action443(443, "充值季卡升年卡"),
	action444(444, "充值购买年卡"),
	action445(445, "充值购买福利卡"),
	
	action446(446, "领取好友赠送精力"),
	action447(447, "公会建设"),
	action448(448, "角色升级"),
	
	action451(451, "大地图开矿战斗开始"),
	action452(452, "大地图开矿战斗结束"),
	action453(453, "大地图抢矿战斗开始"),
	action454(454, "大地图抢矿战斗结束"),
	action455(455, "领取矿采集奖励"),
	action456(456, "放弃矿占领"),
	action457(457, "购买抢夺次数"),
	
	action470(470, "膜拜大R"),
	action471(471, "赠送好友精力"),
	action472(472, "星石分解"),
	
	action473(473, "扫荡流寇古墓"),
	action474(474, "公会科技升级"),
	action475(475, "公会扩容"),
	action476(476, "公会科技个人相关属性升级"),
	action477(477, "一键领取好友赠送的精力"),
	action478(478, "击杀世界BOSS"),
	action479(479, "开服礼包"),
	
	action481(481, "领取时装套装奖励"),
	
	action482(482, "召唤坐骑"),
	action483(483, "坐骑强化"),
	action484(484, "坐骑升阶"),
	action485(485, "召回佣兵"),
	action486(486, "雇佣佣兵"),
	
	action487(487, "PVP3V3战斗开始"),
	action488(488, "PVP3V3战斗结束"),
	action489(489, "组队副本战斗开始"),
	action490(490, "组队副本战斗结束"),
	action491(491, "坐骑传承"),
	
	action501(501, "4,5星武将特殊处理"),
	action502(502, "攻城略地雇佣兵上阵"),
	action503(503, "退出公会"),
	action504(504, "踢出公会"),
	
	action505(505, "派出佣兵");
	
	private int type;
	private String desc;

	private static HashMap<Integer, ActionType> typeMap = new HashMap<Integer, ActionType>();

	static {
		ActionType[] values = ActionType.values();
		for (ActionType value : values) {
			typeMap.put(value.getType(), value);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private ActionType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static HashMap<Integer,ActionType> getActionMap()
	{
		return typeMap;
	}
	/**
	 * 解析
	 * 
	 * @param attrTypeValue
	 * @return
	 */
	public static ActionType attrParseType(int attrTypeValue) {
		return typeMap.get(attrTypeValue);
	}
}
