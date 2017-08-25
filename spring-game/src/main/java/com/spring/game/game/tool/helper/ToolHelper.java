package com.snail.webgame.game.tool.helper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.configdb.ConfigXmlService;
import com.snail.webgame.game.dao.ConfigXmlDAO;
import com.snail.webgame.game.info.ToolBoxInfo;
import com.snail.webgame.game.info.ToolOpActivityInfo;
import com.snail.webgame.game.info.ToolOpActivityRewardInfo;
import com.snail.webgame.game.protocal.mail.service.MailService;
import com.snail.webgame.game.tool.pojo.DeleteVo;
import com.snail.webgame.game.tool.pojo.HeroVo;
import com.snail.webgame.game.tool.pojo.MailInfo;
import com.snail.webgame.game.tool.pojo.PostVo;
import com.snail.webgame.game.tool.pojo.RoleVo;
import com.snail.webgame.game.tool.pojo.SkillVo;
import com.snail.webgame.game.xml.info.RecruitItemXMLInfo;

/**
 * 极效相关static 方法
 * 
 * @author caowl
 * 
 */
public class ToolHelper {

	private static final Logger logger = LoggerFactory.getLogger(ToolHelper.class);
	
	/**
	 * 解析mailXML
	 * <h3>Mail XML example</h3>
	 * <pre>
	 *  &lt;Mail&gt;
	 *    &lt;Topic&gt;主题&lt;/Topic&gt;
	 *    &lt;Content&gt;内容&lt;/Content&gt;
	 *    &lt;Receiver IsAll="1"&gt;接收人用英文逗号分隔的接收人列表&lt;/Receiver&gt;
	 *    &lt;Items&gt;
	 *      &lt;Item ID="1" Count="1" Level="0" &gt;&lt;/Item&gt;
	 *      &lt;Item ID="2" Count="2" Level="0" &gt;&lt;/Item&gt;
	 *      &lt;Item ID="3" Count="3" Level="0" &gt;&lt;/Item&gt;
	 *    &lt;/Items&gt;
	 *  &lt;/Mail&gt;
	 *  </pre>
	 * @param mailTitle
	 * @param mailXml
	 */
	public static MailInfo parseMail(String mailXml) {
		MailInfo mail = null;
		Document doc = XMLUtil4DOM.string2Document(mailXml);
		if (doc != null) {
			Element rootEle = doc.getRootElement();
			if (rootEle != null) {
				String topic = rootEle.element("Topic").getData().toString();
				if (topic != null) {
					String mailContent = rootEle.element("Content").getData().toString();
					Element receiveEle = rootEle.element("Receiver");
					byte receiveType = NumberUtils.toByte(receiveEle.attributeValue("isAll"));
					String receiverStr = rootEle.elementText("Receiver");

					List<?> eleList = rootEle.element("Items").elements("Item");
					String attachment = getAttachment(eleList);
					
					int money = Integer.valueOf(rootEle.element("Money").getData().toString());
					int gold = Integer.valueOf(rootEle.element("Gold").getData().toString());
					int courage = Integer.valueOf(rootEle.element("Courage").getData().toString());
					int justice = Integer.valueOf(rootEle.element("Justice").getData().toString());
//					int guildcurrency = Integer.valueOf(rootEle.element("Guildcurrency").getData().toString());
					int kuafumoney = Integer.valueOf(rootEle.element("Kuafumoney").getData().toString());
					int exploit = Integer.valueOf(rootEle.element("Exploit").getData().toString());
					
					StringBuffer sourceBuff = new StringBuffer();
					if(money>0) {
						sourceBuff.append("money");
						sourceBuff.append(",");
						sourceBuff.append(money);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
					if(gold>0) {
						sourceBuff.append("gold");
						sourceBuff.append(",");
						sourceBuff.append(gold);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
					if(courage>0) {
						sourceBuff.append("courage");
						sourceBuff.append(",");
						sourceBuff.append(courage);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
					if(justice>0) {
						sourceBuff.append("justice");
						sourceBuff.append(",");
						sourceBuff.append(justice);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
//					if(guildcurrency>0) {
//						sourceBuff.append("guildcurrency");
//						sourceBuff.append(",");
//						sourceBuff.append(guildcurrency);
//						sourceBuff.append(",");
//						sourceBuff.append(0);
//						sourceBuff.append(",");
//						sourceBuff.append(0);
//						sourceBuff.append(";");
//					}
					if(kuafumoney>0) {
						sourceBuff.append("kuafumoney");
						sourceBuff.append(",");
						sourceBuff.append(kuafumoney);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
					if(exploit>0) {
						sourceBuff.append("exploit");
						sourceBuff.append(",");
						sourceBuff.append(exploit);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(",");
						sourceBuff.append(0);
						sourceBuff.append(";");
					}
					if(sourceBuff.length()>0)
					{
						attachment = attachment+";"+sourceBuff.toString();
					}
					else
					{
						attachment = attachment+sourceBuff.toString();
					}
					
					
					mail = new MailInfo(topic, mailContent, receiverStr, receiveType, attachment);
					
					
				}
			}
		}
		return mail;
	}
	
	
	/**
	 * 拼接附件
	 * @param elements
	 * @return 格式   道具编号,数量,品质,领取标识;360001,10,0,0;
	 */
	public static String getAttachment(List<?> elements) {
		StringBuffer attachment = new StringBuffer();
		if (elements != null && elements.size() > 0) {
			Element elemt = null;
			for (int i = 0; i < elements.size(); i++) {
				elemt = (Element) elements.get(i);
				if (elemt != null) {
					attachment.append(elemt.attributeValue("ID"));
					attachment.append(",");
					attachment.append(elemt.attributeValue("Count"));
					attachment.append(",");
					attachment.append(elemt.attributeValue("Level"));
					attachment.append(",0");
					if (i < elements.size() -1) {
						attachment.append(";");
					}
				}
			}
		}
		return attachment.toString();
	}
	
	/**
	 * 发送邮件
	 * @param mail
	 * @return
	 */
	public static boolean sendMail(MailInfo mail) {
		if (mail != null) {
			if (mail.getSendType() == 1) { //发送给全部玩家
				MailService.pushMailPrize(null, mail.getAttachment(), mail.getTopic(), mail.getContent(), "");
			} else if (mail.getSendType() == 0) {
				if (StringUtils.isNotBlank(mail.getReceiver()) && !"".equalsIgnoreCase(mail.getReceiver())) {
					MailService.pushMailPrize(mail.getReceiver(), mail.getAttachment(), 
							mail.getTopic(), mail.getContent(), "");
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 解析 公告Xml
	 * @param postXml
	 * @return
	 */
	public static PostVo parsePost(String postXml) {
		PostVo post = null;
		Document doc = XMLUtil4DOM.string2Document(postXml);
		if (doc != null) {
			Element rootEle = doc.getRootElement();
			if (rootEle != null) {
				String content = rootEle.element("Content").getData().toString();
				post = new PostVo(content);
			}
		}
		return post;
	}

	/**
	 * 解析修改角色信息XML
	 * @param string
	 * @return
	 */
	public static RoleVo parseRoleDataXml(String roleDataXml) {
		RoleVo roleVo = null;
		Document doc = XMLUtil4DOM.string2Document(roleDataXml);
		if (doc != null) {
			Element rootEle = doc.getRootElement();
			if (rootEle != null) {
				roleVo = new RoleVo();
				String roleName = rootEle.element("RoleName").getData().toString();
				byte roleRace = Byte.parseByte(rootEle.element("RoleRace").getData().toString());
				long money = Long.parseLong(rootEle.element("Silver").getData().toString());
				long coin = Long.parseLong(rootEle.element("Gold").getData().toString());
				short sp = Short.parseShort(rootEle.element("Sp").getData().toString());
				long courage = Long.parseLong(rootEle.element("Courage").getData().toString());
				long justice = Long.parseLong(rootEle.element("Justice").getData().toString());
				int kuafuMoney = Integer.parseInt(rootEle.element("KuafuMoney").getData().toString());
				int exploit = Integer.parseInt(rootEle.element("Exploit").getData().toString());
				
				if (rootEle.element("Guide") != null && 
						rootEle.element("Guide").getData() != null && !"".equals(rootEle.element("Guide").getData().toString())) {
					int guide = Integer.parseInt(rootEle.element("Guide").getData().toString());
					roleVo.setGuide(guide);
				}
				
				int rankShow = Integer.parseInt(rootEle.element("RankShow").getData().toString());
				int isAdvert = Integer.parseInt(rootEle.element("IsAdvert").getData().toString());
				String chgName = rootEle.element("ChgName").getData().toString();
				
				int equip = Integer.parseInt(rootEle.element("EquipMoney").getData().toString());
				int equipStrengeh = Integer.parseInt(rootEle.element("EquipStrengeh").getData().toString());
				int starMoney = Integer.parseInt(rootEle.element("StarMoney").getData().toString());
				
				String allTitles =  rootEle.element("AllTitles").getData().toString();
				
				roleVo.setRoleName(roleName);
				roleVo.setRoleRace(roleRace);
				roleVo.setMoney(money);
				roleVo.setCoin(coin);
				roleVo.setSp(sp);
				roleVo.setCourage(courage);
				roleVo.setJustice(justice);
				roleVo.setKuafuMoney(kuafuMoney);
				roleVo.setExploit(exploit);
				roleVo.setRankShow(rankShow);
				roleVo.setIsAdvert(isAdvert);
				roleVo.setChgName(chgName);
				
				roleVo.setEquip(equip);
				roleVo.setEquipStrengeh(equipStrengeh);
				roleVo.setStarMoney(starMoney);
				
				roleVo.setAllTitles(allTitles);
				
				if (rootEle.element("ModifyHeros") != null) {
					List<?> elements = rootEle.element("ModifyHeros").elements("Hero");
					if (elements != null && elements.size() > 0) {
						Element elemt = null;
						HeroVo heroVo = null;
						for (int i = 0; i < elements.size(); i++) {
							elemt = (Element) elements.get(i);
							if (elemt != null) {
								heroVo = new HeroVo();
								heroVo.setHeroId(Integer.parseInt(elemt.attributeValue("ID")));
								heroVo.setHeroLevel(Integer.parseInt(elemt.attributeValue("Level")));
								heroVo.setHeroExp(Integer.parseInt(elemt.attributeValue("Exp")));
								roleVo.getHeroList().add(heroVo);
							}
						}
					}
				}
				
				if (rootEle.element("ModifySkill") != null) {
					List<?> skills = rootEle.element("ModifySkill").elements("Skill");
					if (skills != null && skills.size() > 0) {
						Element elemt = null;
						SkillVo skillVo = null;
						for (int i = 0; i < skills.size(); i++) {
							elemt = (Element) skills.get(i);
							if (elemt != null) {
								skillVo = new SkillVo();
								skillVo.setSkillNo(Integer.parseInt(elemt.attributeValue("No")));
								skillVo.setSkillLv(Integer.parseInt(elemt.attributeValue("Level")));
								
								roleVo.getSkillList().add(skillVo);
							}
						}
					}
				}
				
				if (rootEle.element("DeleteItem") != null) {
					List<?> items = rootEle.element("DeleteItem").elements("Item");
					if (items != null && items.size() > 0) {
						Element elemt = null;
						DeleteVo deleteVo = null;
						for (int i = 0; i < items.size(); i++) {
							elemt = (Element) items.get(i);
							if (elemt != null) {
								deleteVo = new DeleteVo();
								deleteVo.setId(Integer.parseInt(elemt.attributeValue("ID")));
								deleteVo.setNum(Integer.parseInt(elemt.attributeValue("Count")));
								
								roleVo.getItemList().add(deleteVo);
							}
						}
					}
				}
				
				if (rootEle.element("DeleteEquip") != null) {
					List<?> equips = rootEle.element("DeleteEquip").elements("Equip");
					if (equips != null && equips.size() > 0) {
						Element elemt = null;
						DeleteVo deleteVo = null;
						for (int i = 0; i < equips.size(); i++) {
							elemt = (Element) equips.get(i);
							if (elemt != null) {
								deleteVo = new DeleteVo();
								deleteVo.setId(Integer.parseInt(elemt.attributeValue("ID")));
								deleteVo.setNum(Integer.parseInt(elemt.attributeValue("Count")));
								
								roleVo.getEquipList().add(deleteVo);
							}
						}
					}
				}
			}
		}
		return roleVo;
	}
	
	/**
	 * 新增或者修改XML
	 * @param xmlName
	 * @param idStr
	 * @param xmlStr
	 * @return
	 */
	public static boolean createOrUpdateXml(String xmlName, String idStr, String xmlStr) {
		boolean flag = false;
		//验证传来的XML字符串是否正确
		Document doc = XMLUtil4DOM.string2Document(xmlStr);
		if(doc != null) {
			Element e = doc.getRootElement();
			if(e != null && StringUtils.isNotBlank(e.attributeValue("Name"))) {
				//更新该记录若返回值为0，则说明该记录不存在
				ConfigXmlDAO configXmlDAO= ConfigXmlDAO.getInstance();
				int resultValue = configXmlDAO.modifyConfigDAO(idStr, xmlStr);
				if(resultValue >= 1) {
					flag = true;
				} else if (resultValue == 0){
					//插入新节点记录 
					flag = configXmlDAO.insertConfigDAO(xmlName, idStr, e.attributeValue("Name"), xmlStr);
				}
				// flag为true则执行成功
				if(flag) {
					logger.info("success");
					try {
						return ConfigXmlService.modifyConfigCache(true, idStr, xmlStr,true); //即时修改缓存
					} catch (Exception e2) {
						logger.error("createOrUpdateXml error", e);
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 解析运营礼包xml
	 * 
	 * @param toolBoxXml
	 * @return 
	 */
	public static ToolBoxInfo parseToolBoxXml(String toolBoxXml) {
		Document doc = XMLUtil4DOM.string2Document(toolBoxXml);
		if (doc != null) {
			Element rootEle = doc.getRootElement();
			if (rootEle != null) {
				ToolBoxInfo toolBox = new ToolBoxInfo();
				
				int boxType = Integer.valueOf(rootEle.element("BoxType").getText());
				toolBox.setBoxType(boxType);
				
				if (rootEle.element("ShowId") != null 
						&& rootEle.element("ShowId").getText() != null && !"".equals(rootEle.element("ShowId").getText())) {
					toolBox.setGuid(Integer.valueOf(rootEle.element("ShowId").getText()));
				}
				
				if (rootEle.element("ChargeNo").getText() != null && !"".equals(rootEle.element("ChargeNo").getText())) {
					toolBox.setChargeNo(Integer.valueOf(rootEle.element("ChargeNo").getText()));
				}
				
				if (rootEle.element("Id").getText() != null && !"".equals(rootEle.element("Id").getText())) {
					toolBox.setId(Integer.valueOf(rootEle.element("Id").getText()));
				}
				
				if (rootEle.element("BoxName").getText() != null && !"".equals(rootEle.element("BoxName").getText())) {
					toolBox.setBoxName(rootEle.element("BoxName").getText());
				}
				
				if (rootEle.element("CostPrice").getText() != null && !"".equals(rootEle.element("CostPrice").getText())) {
					toolBox.setCostPrice(Integer.valueOf(rootEle.element("CostPrice").getText()));
				}
				
				if (rootEle.element("ItemSaleNum").getText() != null && !"".equals(rootEle.element("ItemSaleNum").getText())) {
					toolBox.setItemSaleNum(Integer.valueOf(rootEle.element("ItemSaleNum").getText()));
				}
				
				if (rootEle.element("BoxVersion").getText() != null && !"".equals(rootEle.element("BoxVersion").getText())) {
					toolBox.setBoxVersion(Integer.valueOf(rootEle.element("BoxVersion").getText()));
				}
				
				if (rootEle.element("BoxQuality").getText() != null && !"".equals(rootEle.element("BoxQuality").getText())) {
					toolBox.setBoxQuality(Integer.valueOf(rootEle.element("BoxQuality").getText()));
				}
				
				if (rootEle.element("Items").getText() != null && !"".equals(rootEle.element("Items").getText())) {
					toolBox.setItemStr(rootEle.element("Items").getText());
				}
				
				try {
					Timestamp startTime = DateUtil.parseStr2Time(rootEle.element("StartTime").getText());
					Timestamp endTime = DateUtil.parseStr2Time(rootEle.element("EndTime").getText());
					if (startTime == null || endTime == null || endTime.getTime() <= startTime.getTime()) {
						return null;
					}
					
					toolBox.setStartTime(startTime);
					toolBox.setEndTime(endTime);
				} catch (Exception e) {
					logger.info("", e);
					return null;
				}
				
				return toolBox;
			}
		}
		
		return null;
	}

	/**
	 * 解析运营时限活动xml
	 * 
	 * @param toolOpActXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ToolOpActivityInfo parseToolOpActXml(String toolOpActXml) {

		Document doc = XMLUtil4DOM.string2Document(toolOpActXml);
		if (doc != null) {
			Element rootEle = doc.getRootElement();
			if (rootEle != null) {
				ToolOpActivityInfo toolOpActInfo = new ToolOpActivityInfo();
				
				int actType = Integer.valueOf(rootEle.element("type").getText());
				toolOpActInfo.setActType(actType);
				
				// 该字段传的是guid
				if (rootEle.element("id").getText() != null && !"".equals(rootEle.element("id").getText())) {
					toolOpActInfo.setActNo(rootEle.element("id").getText());
				}
				
				if (rootEle.element("showid") != null 
						&& rootEle.element("showid").getText() != null && !"".equals(rootEle.element("showid").getText())) {
					toolOpActInfo.setShowId(Integer.valueOf(rootEle.element("showid").getText()));
				}
				
				if (rootEle.element("name").getText() != null && !"".equals(rootEle.element("name").getText())) {
					toolOpActInfo.setActName(rootEle.element("name").getText());
				}
				
				if (rootEle.element("introduce").getText() != null && !"".equals(rootEle.element("introduce").getText())) {
					toolOpActInfo.setActIntroduce(rootEle.element("introduce").getText());
				}
				
				if (rootEle.element("state").getText() != null && !"".equals(rootEle.element("state").getText())) {
					toolOpActInfo.setActState(Integer.valueOf(rootEle.element("state").getText()));
				}
				
				if (rootEle.element("version").getText() != null && !"".equals(rootEle.element("version").getText())) {
					toolOpActInfo.setActVersion(Integer.valueOf(rootEle.element("version").getText()));
				}
				
				if (rootEle.element("opentimes") != null) {
					Element subEle = rootEle.element("opentimes").element("opentime");
					if (subEle != null) {
						
						try {
							Timestamp startTime = DateUtil.parseStr2Time(subEle.element("startdate").getText());
							Timestamp endTime = DateUtil.parseStr2Time(subEle.element("enddate").getText());
							if (startTime == null || endTime == null || endTime.getTime() <= startTime.getTime()) {
								return null;
							}
							
							toolOpActInfo.setStartTime(startTime);
							toolOpActInfo.setEndTime(endTime);
						} catch (Exception e) {
							logger.info("", e);
							return null;
						}
					}
				}
				
				if (rootEle.element("main") != null) {
					// 武将id：概率（1000）：抽多少次必送（10）: 抽奖价格（100）
					String[] tempArr = rootEle.element("main").getText().split(":");
					if (tempArr.length != 4) {
						return null;
					}
					
					String lotHeroStr = tempArr[0] + ":" + tempArr[1];
					toolOpActInfo.setLotHeroStr(lotHeroStr);
					toolOpActInfo.setLotHitNum(Integer.valueOf(tempArr[2]));
					toolOpActInfo.setLotPrice(Integer.valueOf(tempArr[3]));
					
					toolOpActInfo.setActHeroNo(Integer.valueOf(tempArr[0]));
					
					RecruitItemXMLInfo xmlInfo = new RecruitItemXMLInfo();
					xmlInfo.setItemNo(tempArr[0]);
					xmlInfo.setNum(1);
					xmlInfo.setRand(Integer.valueOf(tempArr[1]));
					
					toolOpActInfo.getList().add(xmlInfo);
				}
				
				// 解析奖励
				if (actType == ToolOpActivityInfo.OP_ACT_TYPE_0 || actType == ToolOpActivityInfo.OP_ACT_TYPE_3) {
					if (rootEle.element("rewards") != null) {
						
						Map<Integer, ToolOpActivityRewardInfo> rewardMap = new HashMap<Integer, ToolOpActivityRewardInfo>();
						toolOpActInfo.setRewardMap(rewardMap);
						
						ToolOpActivityRewardInfo rewardXMlInfo = null;
						
						List<Element> subEles = rootEle.element("rewards").elements("reward");
						if (subEles != null && !subEles.isEmpty()) {
							for (Element rewEle : subEles) {
								rewardXMlInfo = new ToolOpActivityRewardInfo();
								rewardXMlInfo.setRewardNo(Integer.valueOf(rewEle.element("id").getText()));
								rewardMap.put(rewardXMlInfo.getRewardNo(), rewardXMlInfo);
								
								rewardXMlInfo.setRewardName(rewEle.element("name").getText());
								
								if (rewEle.element("rewardconditions") != null) {
									Element subEle = rewEle.element("rewardconditions").element("condition");
									if (subEle != null) {
										String condStr = subEle.element("type").getText();
										int goalVal = Integer.valueOf(subEle.element("valuefrom").getText());
										
										rewardXMlInfo.setGoalVal(goalVal);
										rewardXMlInfo.setRewardCond(condStr);
										
										if (rewardXMlInfo.isSpecCond()) {
											String dateStr = null;
											if (subEle.element("date") != null && !"".equals(subEle.element("date").getText())) {
												dateStr = subEle.element("date").getText();
											}
											
											if (dateStr == null) {
												return null;
											}
											
											rewardXMlInfo.setDateStr(dateStr);
											
											condStr = condStr + AbstractConditionCheck.SPLIT_2 + goalVal + AbstractConditionCheck.SPLIT_2 + dateStr;
										} else {
											condStr = condStr + AbstractConditionCheck.SPLIT_2 + goalVal;
										}
										
										try {
											rewardXMlInfo.getRewardConds().addAll(AbstractConditionCheck.generateConds("", condStr));
										} catch (Exception e) {
											logger.info("parse cond error, condStr : " + condStr);
											e.printStackTrace();
											return null;
										}
									}
								}
								
								if (rewEle.element("rewarditems") == null || "".equals(rewEle.element("rewarditems").getText())) {
									// 领取类活动必须配置奖励
									return null;
								}
								
								String rewardStr = rewEle.element("rewarditems").getText();
								rewardXMlInfo.setRewardItems(rewardStr);
								rewardXMlInfo.setMultiple(Byte.parseByte(rewEle.element("multiple").getText()));
								
								if (actType == ToolOpActivityInfo.OP_ACT_TYPE_3) {
									toolOpActInfo.setLotRewardStr(rewardStr);
									
									String[] tempArr = rewardStr.split(";");
									for (String str : tempArr) {
										String[] subArr = str.split(":");
										if (subArr.length != 3) {
											return null;
										}
										
										// 2:36000004:10;2:36000005:10
										RecruitItemXMLInfo xmlInfo = new RecruitItemXMLInfo();
										xmlInfo.setItemNo(subArr[1]);
										xmlInfo.setNum(Integer.valueOf(subArr[0]));
										xmlInfo.setRand(Integer.valueOf(subArr[2]));
										
										toolOpActInfo.getList().add(xmlInfo);
									}
								}
							}
						}
					}
				}
				
				return toolOpActInfo;
			}
		}
		
		return null;
	}
	
}
