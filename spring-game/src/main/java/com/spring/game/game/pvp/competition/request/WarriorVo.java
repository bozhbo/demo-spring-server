package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RideInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.info.RideXMLInfo.LvUpInfo;

/**
 * 
 * @author qiuhd
 * @since  2014年11月10日
 * @version V1.0.0
 */
public class WarriorVo extends BaseRoomReq {

	private int heroId;//数据库主键
    private int heroNo;// 将领编号
    private byte group;//英雄分组
    private int star;//英雄星星数
    private int heroLevel;//英雄等级
    private int heroQuality;//英雄品质
    private byte main;//是否主战英雄：0.不是;1.是 
    private byte skillControl;//副将技能能否被主将控制：0:不可以，1.可以
    private byte deployStatus;
    private byte emiliTtype;//兵种类型
    private int emilitLevel;//兵种等级
    private int hp;// 血量
    private int soldierHP;
    private int ad;//普通攻击基础属性
	private int attack;// 物理攻击
	private int magicAttack;// 法术攻击
	private int  crit;// 暴击
	private int  critAvo;// 抗暴
	private int  skillCrit;// 技能暴击
	private int  skillCritAvo;// 技能抗暴
	private int ignorAttackAvo;// 物理穿透
	private int ignorMagicAvo;// 法术穿透
	private int  miss;// 闪避
	private int  hit;// 命中
	private float moveSpeed;// 移动速度(m/s)
	private float cutCD;// 技能冷却缩短比例
	private float hpBack;// 生命吸取
	private float  cureUp;// 治疗效果提高
	private float attackSpeed;// 攻击速度(次/s)
	private int attackDef;// 物理防御
	private int magicDef;// 法术防御
	private int attackRange;// 攻击距离
	private int radius;// 攻击范围
	private float lookRange;// 视野
	private int bulletSpeed;// 弹道速度
	private float critMore;// 暴伤加成
	private float critLess;// 暴伤减免
	private float breakSoldierDef;// 破防率
	private int reduceDamage;//减少伤害
	private float immunityDamage;//免伤
	
    private byte skillCount; // 技能列表
    private List<SkillInfoVo> skillList;// 主动技能列表
    private String equipNos;// AVATER装备编号
    
	private int rideNo;// 坐骑编号
	private int rideHp;// 血量上限
	private int rideCurrHp;// 当前血量
	private int rideLv;// 等级
	private float rideSpeed;// 速度
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.heroId = getInt(buffer, order);
	    this.heroNo = getInt(buffer, order);
	    this.group = getByte(buffer, order);
	    this.star = getInt(buffer, order);
	    this.heroLevel = getInt(buffer, order);
	    this.heroQuality = getInt(buffer, order);
	    this.main = getByte(buffer, order);
	    this.skillControl = getByte(buffer, order);
	    this.deployStatus = getByte(buffer, order);
	    this.emiliTtype = getByte(buffer, order);
	    this.emilitLevel = getInt(buffer, order);
	    this.hp = getInt(buffer, order);
	    this.soldierHP = getInt(buffer, order);
	    this.ad = getInt(buffer, order);
		this.attack = getInt(buffer, order);
		this.magicAttack = getInt(buffer, order);
		this.crit = getInt(buffer, order);
		this.critAvo = getInt(buffer, order);
		this.skillCrit = getInt(buffer, order);
		this.skillCritAvo = getInt(buffer, order);
		this.ignorAttackAvo = getInt(buffer, order);
		this.ignorMagicAvo = getInt(buffer, order);
		this.miss = getInt(buffer, order);
		this.hit = getInt(buffer, order);
		this.moveSpeed = getFloat(buffer, order);
		this.cutCD = getFloat(buffer, order);
		this.hpBack = getFloat(buffer, order);
		this.cureUp = getFloat(buffer, order);
		this.attackSpeed = getFloat(buffer, order);
		this.attackDef = getInt(buffer, order);
		this.magicDef = getInt(buffer, order);
		this.attackRange = getInt(buffer, order);
		this.radius = getInt(buffer, order);
		this.lookRange = getInt(buffer, order);
		this.bulletSpeed = getInt(buffer, order);
		this.critMore = getFloat(buffer, order);
		this.critLess = getFloat(buffer, order);
		this.breakSoldierDef = getFloat(buffer, order);
		this.reduceDamage = getInt(buffer, order);
		this.immunityDamage = getFloat(buffer, order);
		this.skillCount = getByte(buffer, order);
		this.skillList = new ArrayList<SkillInfoVo>(this.skillCount);
		for (int index = 0; index < skillCount; index++) {
			SkillInfoVo vo = new SkillInfoVo();
			vo.bytes2Req(buffer, order);
			this.skillList.add(vo);
		}
		this.equipNos = getString(buffer, order);
		
		this.rideNo = getInt(buffer, order);
		this.rideHp = getInt(buffer, order);
		this.rideCurrHp = getInt(buffer, order);
		this.rideLv = getInt(buffer, order);
		this.rideSpeed = getFloat(buffer, order);
	}

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.heroId);
		setInt(buffer, order, this.heroNo);
		setByte(buffer, order, this.group);
		setInt(buffer, order, this.star);
		setInt(buffer, order, this.heroLevel);
		setInt(buffer, order, this.heroQuality);
		setByte(buffer, order, this.main);
		setByte(buffer, order, this.skillControl);
		setByte(buffer, order, this.deployStatus);
		setByte(buffer, order, this.emiliTtype);
		setInt(buffer, order, this.emilitLevel);
		setInt(buffer, order, this.hp);
		setInt(buffer, order, this.soldierHP);
		setInt(buffer, order, this.ad);
		setInt(buffer, order, this.attack);
		setInt(buffer, order, this.magicAttack);
		setInt(buffer, order, this.crit);
		setInt(buffer, order, this.critAvo);
		setInt(buffer, order, this.skillCrit);
		setInt(buffer, order, this.skillCritAvo);
		setInt(buffer, order, this.ignorAttackAvo);
		setInt(buffer, order, this.ignorMagicAvo);
		setInt(buffer, order, this.miss);
		setInt(buffer, order, this.hit);
		setFloat(buffer, order, this.moveSpeed);
		setFloat(buffer, order, this.cutCD);
		setFloat(buffer, order, this.hpBack);
		setFloat(buffer, order, this.cureUp);
		setFloat(buffer, order, this.attackSpeed);
		setInt(buffer, order, this.attackDef);
		setInt(buffer, order, this.magicDef);
		setInt(buffer, order, this.attackRange);
		setInt(buffer, order, this.radius);
		setFloat(buffer, order, this.lookRange);
		setInt(buffer, order, this.bulletSpeed);
		setFloat(buffer, order, this.critMore);
		setFloat(buffer, order, this.critLess);
		setFloat(buffer, order, this.breakSoldierDef);
		setInt(buffer, order, this.reduceDamage);
		setFloat(buffer, order, this.immunityDamage);
	    setByte(buffer, order, this.skillCount);
	    if (this.skillList != null && this.skillList.size() > 0) {
	    	for (SkillInfoVo vo : this.skillList) {
	    		vo.resp2Bytes(buffer, order);
	    	}
	    }
	    setString(buffer, order, this.equipNos);
	    
	    setInt(buffer, order, this.rideNo);
	    setInt(buffer, order, this.rideHp);
	    setInt(buffer, order, this.rideCurrHp);
	    setInt(buffer, order, this.rideLv);
	    setFloat(buffer, order, this.rideSpeed);
	}
	
	public int getHeroNo() {
		return heroNo;
	}

	public void setHeroNo(int heroNo) {
		this.heroNo = heroNo;
	}

	public byte getMain() {
		return main;
	}

	public void setMain(byte main) {
		this.main = main;
	}

	public byte getEmiliTtype() {
		return emiliTtype;
	}

	public void setEmiliTtype(byte emiliTtype) {
		this.emiliTtype = emiliTtype;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getCritAvo() {
		return critAvo;
	}

	public void setCritAvo(int critAvo) {
		this.critAvo = critAvo;
	}

	public int getSkillCrit() {
		return skillCrit;
	}

	public void setSkillCrit(int skillCrit) {
		this.skillCrit = skillCrit;
	}

	public int getSkillCritAvo() {
		return skillCritAvo;
	}

	public void setSkillCritAvo(int skillCritAvo) {
		this.skillCritAvo = skillCritAvo;
	}

	public int getIgnorAttackAvo() {
		return ignorAttackAvo;
	}

	public void setIgnorAttackAvo(int ignorAttackAvo) {
		this.ignorAttackAvo = ignorAttackAvo;
	}

	public int getIgnorMagicAvo() {
		return ignorMagicAvo;
	}

	public void setIgnorMagicAvo(int ignorMagicAvo) {
		this.ignorMagicAvo = ignorMagicAvo;
	}

	public int getMiss() {
		return miss;
	}

	public void setMiss(int miss) {
		this.miss = miss;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getCutCD() {
		return cutCD;
	}

	public void setCutCD(float cutCD) {
		this.cutCD = cutCD;
	}

	public float getHpBack() {
		return hpBack;
	}

	public void setHpBack(float hpBack) {
		this.hpBack = hpBack;
	}


	public float getCureUp() {
		return cureUp;
	}

	public void setCureUp(float cureUp) {
		this.cureUp = cureUp;
	}

	public float getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public int getAttackDef() {
		return attackDef;
	}

	public void setAttackDef(int attackDef) {
		this.attackDef = attackDef;
	}

	public int getMagicDef() {
		return magicDef;
	}

	public void setMagicDef(int magicDef) {
		this.magicDef = magicDef;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public void setAttackRange(int attackRange) {
		this.attackRange = attackRange;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public float getLookRange() {
		return lookRange;
	}

	public void setLookRange(float lookRange) {
		this.lookRange = lookRange;
	}

	public int getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public byte getSkillCount() {
		return skillCount;
	}

	public void setSkillCount(byte skillCount) {
		this.skillCount = skillCount;
	}

	public List<SkillInfoVo> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SkillInfoVo> skillList) {
		this.skillList = skillList;
	}
	
	public byte getSkillControl() {
		return skillControl;
	}

	public void setSkillControl(byte skillControl) {
		this.skillControl = skillControl;
	}
	
	public byte getDeployStatus() {
		return deployStatus;
	}

	public void setDeployStatus(byte deployStatus) {
		this.deployStatus = deployStatus;
	}
	
	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}
	
	public int getEmilitLevel() {
		return emilitLevel;
	}

	public void setEmilitLevel(int emilitLevel) {
		this.emilitLevel = emilitLevel;
	}
	
	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getHeroQuality() {
		return heroQuality;
	}

	public void setHeroQuality(int heroQuality) {
		this.heroQuality = heroQuality;
	}
	

	public byte getGroup() {
		return group;
	}

	public void setGroup(byte group) {
		this.group = group;
	}

	public int getAd() {
		return ad;
	}

	public void setAd(int ad) {
		this.ad = ad;
	}
	
	public int getSoldierHP() {
		return soldierHP;
	}

	public void setSoldierHP(int soldierHP) {
		this.soldierHP = soldierHP;
	}
	
	public float getCritMore() {
		return critMore;
	}

	public void setCritMore(float critMore) {
		this.critMore = critMore;
	}

	public float getCritLess() {
		return critLess;
	}

	public void setCritLess(float critLess) {
		this.critLess = critLess;
	}

	public float getBreakSoldierDef() {
		return breakSoldierDef;
	}

	public void setBreakSoldierDef(float breakSoldierDef) {
		this.breakSoldierDef = breakSoldierDef;
	}

	public String getEquipNos() {
		return equipNos;
	}

	public void setEquipNos(String equipNos) {
		this.equipNos = equipNos;
	}

	public int getReduceDamage() {
		return reduceDamage;
	}

	public void setReduceDamage(int reduceDamage) {
		this.reduceDamage = reduceDamage;
	}

	public float getImmunityDamage() {
		return immunityDamage;
	}

	public void setImmunityDamage(float immunityDamage) {
		this.immunityDamage = immunityDamage;
	}

	@Override
	public String toString() {
		return "WarriorVo [heroNo=" + heroNo + ", main=" + main
				+ ", skillControl=" + skillControl + ", emiliTtype="
				+ emiliTtype + ", hp=" + hp + ", attack=" + attack
				+ ", magicAttack=" + magicAttack + ", crit=" + crit
				+ ", critAvo=" + critAvo + ", skillCrit=" + skillCrit
				+ ", skillCritAvo=" + skillCritAvo + ", ignorAttackAvo="
				+ ignorAttackAvo + ", ignorMagicAvo=" + ignorMagicAvo
				+ ", miss=" + miss + ", hit=" + hit + ", moveSpeed="
				+ moveSpeed + ", cutCD=" + cutCD + ", hpBack=" + hpBack
				+ ", cureUp=" + cureUp + ", attackSpeed=" + attackSpeed
				+ ", attackDef=" + attackDef + ", magicDef=" + magicDef
				+ ", attackRange=" + attackRange + ", radius=" + radius
				+ ", lookRange=" + lookRange + ", bulletSpeed=" + bulletSpeed
				+ ", skillCount=" + skillCount + ", skillList=" + skillList
				+ ", equipNos="+ this.equipNos +"]";
	}

	/**
	 * 
	 * @param heroInfo			英雄信心
	 * @param heroType          英雄类型
	 * @return
	 */
	public static WarriorVo transformFromHero(HeroInfo heroInfo,int heroType,int group,
			Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate) {
		HeroPropertyInfo totalPropertyInfo = HeroProService.getHeroTotalProperty(heroInfo,mainRate,otherRate);
		WarriorVo vo = new WarriorVo();
		vo.heroId = (int) heroInfo.getId();
		vo.heroNo = heroInfo.getHeroNo();
		vo.main = (byte) heroInfo.getDeployStatus();
		vo.emiliTtype = (byte) heroType;
		//vo.star = heroInfo.getStar();
		vo.heroLevel = heroInfo.getHeroLevel();
		vo.heroQuality = heroInfo.getQuality();
		vo.setGroup((byte)group);
		if(totalPropertyInfo != null)
		{
			vo.ad = totalPropertyInfo.getAd();
			vo.soldierHP = totalPropertyInfo.getSoldierHp();
			vo.hp = totalPropertyInfo.getHp();
			vo.attack = totalPropertyInfo.getAttack();
			vo.magicAttack = totalPropertyInfo.getMagicAttack();
			vo.crit = (int) totalPropertyInfo.getCrit();
			vo.critAvo = (int) totalPropertyInfo.getCritAvo();
			vo.skillCrit = (int) totalPropertyInfo.getSkillCrit();
			vo.skillCritAvo = (int) totalPropertyInfo.getSkillCritAvo();
			vo.ignorAttackAvo = totalPropertyInfo.getIgnorAttackAvo();
			vo.ignorMagicAvo = totalPropertyInfo.getIgnorMagicAvo();
			vo.miss = (int) totalPropertyInfo.getMiss();
			vo.hit = (int) totalPropertyInfo.getHit();
			vo.moveSpeed = totalPropertyInfo.getMoveSpeed();
			vo.cutCD = totalPropertyInfo.getCutCD();
			vo.hpBack = totalPropertyInfo.getHpBack();
			vo.cureUp = totalPropertyInfo.getCureUp();
			vo.attackSpeed = totalPropertyInfo.getAttackSpeed();
			vo.attackDef = totalPropertyInfo.getAttackDef();
			vo.magicDef = totalPropertyInfo.getMagicDef();
			vo.attackRange = totalPropertyInfo.getAttackRange();
			vo.radius = totalPropertyInfo.getRadius();
			vo.lookRange = totalPropertyInfo.getLookRange();
			vo.bulletSpeed = totalPropertyInfo.getBulletSpeed();
			vo.critMore = totalPropertyInfo.getCritMore();
			vo.critLess = totalPropertyInfo.getCritLess();
			vo.breakSoldierDef = totalPropertyInfo.getBreakSoldierDef();
			vo.setReduceDamage(totalPropertyInfo.getReduceDamage());
			vo.setImmunityDamage(totalPropertyInfo.getImmunityDamage());
		}
		
		vo.deployStatus = heroInfo.getDeployStatus();
		if(heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN){
			vo.equipNos = SceneService1.getHeroEquipNoforAvater(heroInfo);
		}
		
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(heroInfo.getRoleId());
		if (roleInfo != null && heroInfo.getDeployStatus() == 1) {
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			
			if(mainHero != null){
				// 坐骑
				RideInfo rideInfo = roleInfo.getRideInfo();
				
				if(rideInfo != null){
					LvUpInfo lvUpInfo = rideInfo.getLvUpInfo();
					
					if(lvUpInfo != null){
						vo.setRideHp(rideInfo.getCurrHP());
						vo.setRideLv(lvUpInfo.getLv());
						vo.setRideSpeed(lvUpInfo.getMoveSpeed());
					}
					vo.setRideNo(rideInfo.getRideNo());
					vo.setRideCurrHp(rideInfo.getCurrHP());
				}
			}
		}

			
//	    //TODO For test
//	    List<SkillInfoVo> skillInfoVoList = new ArrayList<SkillInfoVo>(vo.skillCount);
// 	    HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(vo.getHeroNo());
// 	    for (int skillNo : heroXMLInfo.getSkillMap().keySet()) {
// 	    	skillInfoVoList.add(new SkillInfoVo(skillNo,1));
// 	    }
// 	    vo.skillCount = (byte)heroXMLInfo.getSkillMap().size();
// 		vo.skillList = skillInfoVoList;
	    return vo;
	}

	public int getRideNo() {
		return rideNo;
	}

	public void setRideNo(int rideNo) {
		this.rideNo = rideNo;
	}

	public int getRideHp() {
		return rideHp;
	}

	public void setRideHp(int rideHp) {
		this.rideHp = rideHp;
	}

	public int getRideCurrHp() {
		return rideCurrHp;
	}

	public void setRideCurrHp(int rideCurrHp) {
		this.rideCurrHp = rideCurrHp;
	}

	public int getRideLv() {
		return rideLv;
	}

	public void setRideLv(int rideLv) {
		this.rideLv = rideLv;
	}

	public float getRideSpeed() {
		return rideSpeed;
	}

	public void setRideSpeed(float rideSpeed) {
		this.rideSpeed = rideSpeed;
	}
}
