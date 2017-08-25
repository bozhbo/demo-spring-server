package com.snail.webgame.game.xml.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snail.webgame.game.common.util.RandomUtil;

public class SceneXMLInfo {
	
	private int no;
	private float bornPointX;//玩家在场景中的初始位置
	private float bornPointY;//玩家在场景中的初始位置
	private float bornPointZ;//玩家在场景中的初始位置
	
	private float disaperPointX;//玩家在场景中的初始位置
	private float disaperPointY;//玩家在场景中的初始位置
	private float disaperPointZ;//玩家在场景中的初始位置
	
	private float jinchengPointX;//玩家进城位置
	private float jinchengPointY;
	private float jinchengPointZ;
	
	private HashMap<Integer,SceneNPCXML> sceneNPCMap;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public float getBornPointX() {
		return bornPointX;
	}
	public void setBornPointX(float bornPointX) {
		this.bornPointX = bornPointX;
	}
	public float getBornPointY() {
		return bornPointY;
	}
	public void setBornPointY(float bornPointY) {
		this.bornPointY = bornPointY;
	}
	public float getBornPointZ() {
		return bornPointZ;
	}
	public void setBornPointZ(float bornPointZ) {
		this.bornPointZ = bornPointZ;
	}
	public float getDisaperPointX() {
		return disaperPointX;
	}
	public void setDisaperPointX(float disaperPointX) {
		this.disaperPointX = disaperPointX;
	}
	public float getDisaperPointY() {
		return disaperPointY;
	}
	public void setDisaperPointY(float disaperPointY) {
		this.disaperPointY = disaperPointY;
	}
	public float getDisaperPointZ() {
		return disaperPointZ;
	}
	public void setDisaperPointZ(float disaperPointZ) {
		this.disaperPointZ = disaperPointZ;
	}
	public float getJinchengPointX() {
		return jinchengPointX;
	}
	public void setJinchengPointX(float jinchengPointX) {
		this.jinchengPointX = jinchengPointX;
	}
	public float getJinchengPointY() {
		return jinchengPointY;
	}
	public void setJinchengPointY(float jinchengPointY) {
		this.jinchengPointY = jinchengPointY;
	}
	public float getJinchengPointZ() {
		return jinchengPointZ;
	}
	public void setJinchengPointZ(float jinchengPointZ) {
		this.jinchengPointZ = jinchengPointZ;
	}
	public HashMap<Integer, SceneNPCXML> getSceneNPCMap() {
		return sceneNPCMap;
	}
	public void setSceneNPCMap(HashMap<Integer, SceneNPCXML> sceneNPCMap) {
		this.sceneNPCMap = sceneNPCMap;
	}
	
	
	public class SceneNPCXML{
		private int npcNo;
		private float pointX;
		private float pointY;
		private float pointZ;
		private float npcFace;
		public int getNpcNo() {
			return npcNo;
		}
		public void setNpcNo(int npcNo) {
			this.npcNo = npcNo;
		}
		public float getPointX() {
			return pointX;
		}
		public void setPointX(float pointX) {
			this.pointX = pointX;
		}
		public float getPointY() {
			return pointY;
		}
		public void setPointY(float pointY) {
			this.pointY = pointY;
		}
		public float getPointZ() {
			return pointZ;
		}
		public void setPointZ(float pointZ) {
			this.pointZ = pointZ;
		}
		public float getNpcFace() {
			return npcFace;
		}
		public void setNpcFace(float npcFace) {
			this.npcFace = npcFace;
		}
	}
	
	public class MapCityXML{
		private int no;
		private float pointX;//大地图初始坐标
		private float pointZ;
		private int race;
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public float getPointX() {
			return pointX;
		}
		public void setPointX(float pointX) {
			this.pointX = pointX;
		}
		public float getPointZ() {
			return pointZ;
		}
		public void setPointZ(float pointZ) {
			this.pointZ = pointZ;
		}
		public int getRace() {
			return race;
		}
		public void setRace(int race) {
			this.race = race;
		}
		
	}
	
	public class MapCityXMLNPC{
		private int no;
		private int type;//1-永久存在 2-定时刷新
		private int battleType;// 1-到达即开始NPC战斗  2-到达即开始镜像战斗  3-到达即通知完成任务 4-到达后开始倒计时（秒）
		private int timeDown;//大地图某NPC驻足
		private String gwName;
		private int costEng;//消耗的精力
		private String prize;
		private int level;// 挑战等级限制
		private List<RecruitItemXMLInfo> gwItems = new ArrayList<RecruitItemXMLInfo>();
		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getBattleType() {
			return battleType;
		}
		public void setBattleType(int battleType) {
			this.battleType = battleType;
		}
		public int getTimeDown() {
			return timeDown;
		}
		public void setTimeDown(int timeDown) {
			this.timeDown = timeDown;
		}
		public String getGwName() {
			return gwName;
		}
		public void setGwName(String gwName) {
			this.gwName = gwName;
		}
		public int getCostEng() {
			return costEng;
		}
		public void setCostEng(int costEng) {
			this.costEng = costEng;
		}
		public String getPrize() {
			return prize;
		}
		public void setPrize(String prize) {
			this.prize = prize;
		}
		
		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public List<RecruitItemXMLInfo> getGwItems() {
			return gwItems;
		}
		
		public String getGw() {
			return getGw(gwItems);
		}
		
		public String getGw(List<RecruitItemXMLInfo> gwItems) {
			if (gwItems != null && gwItems.size() > 0) {
				int sumRand = 0;
				for (RecruitItemXMLInfo item : gwItems) {
					item.setMinRand(sumRand);
					item.setMaxRand(sumRand + item.getRand());
					sumRand = item.getMaxRand();
				}
				int rand = RandomUtil.getRandom(0, sumRand);
				for (RecruitItemXMLInfo item : gwItems) {
					if(rand == 0){
						return item.getItemNo();
					}
					if (item.getMinRand() < rand && rand <= item.getMaxRand()) {
						return item.getItemNo();
					}
				}
			}
			return "";
		}
		
	}

}
