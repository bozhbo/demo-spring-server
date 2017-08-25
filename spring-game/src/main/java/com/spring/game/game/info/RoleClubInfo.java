package com.snail.webgame.game.info;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.snail.webgame.engine.common.to.BaseTO;
import com.snail.webgame.game.protocal.countryfight.common.City;

public class RoleClubInfo extends BaseTO {
	private int createRoleId; // 公会创建人ID
	private String clubName; // 公会名称
	private String declaration; // 宣言
	private String description; // 公告
	private int imageId; // 公会头像Id/旗帜
	private int level; // 公会等级
	private int build; // 公会建设度
	private int levelLimit; // 加入公会等级限制
	private int flag; // 是否需要审批 0-不需要 1-需要
	private Timestamp createTime; // 公会创建时间
	private Set<Integer> adminSet = new HashSet<Integer>(); //公会管理成员的角色ID
	private VoiceInfo voiceInfo;
	private Map<Integer, City> cityMap; // 城市
	private int tech; //公会科技等级
	private int gold; //成员贡献的金币，类似经验的使用
	private int extendLv; //公会扩容等级

	public int getCreateRoleId() {
		return createRoleId;
	}

	public void setCreateRoleId(int createRoleId) {
		this.createRoleId = createRoleId;
	}

	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) {
		this.clubName = clubName;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getBuild() {
		return build;
	}

	public void setBuild(int build) {
		this.build = build;
	}

	public int getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(int levelLimit) {
		this.levelLimit = levelLimit;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Set<Integer> getAdminSet() {
		return adminSet;
	}

	public void setAdminSet(Set<Integer> adminSet) {
		this.adminSet = adminSet;
	}
	
	public VoiceInfo getVoiceInfo() {
		return voiceInfo;
	}

	public void setVoiceInfo(VoiceInfo voiceInfo) {
		this.voiceInfo = voiceInfo;
	}

	public Map<Integer, City> getCityMap() {
		return cityMap;
	}

	public void setCityMap(Map<Integer, City> cityMap) {
		this.cityMap = cityMap;
	}

	public int getTech() {
		return tech;
	}

	public void setTech(int tech) {
		this.tech = tech;
	}

	public int getGold() {
		return gold;	
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getExtendLv() {
		return extendLv;
	}

	public void setExtendLv(int extendLv) {
		this.extendLv = extendLv;
	}

	@Override
	public byte getSaveMode() {
		return 0;
	}

}
