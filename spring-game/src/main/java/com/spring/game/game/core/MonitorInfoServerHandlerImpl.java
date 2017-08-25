package com.snail.webgame.game.core;

import com.snail.webgame.engine.component.info.monitor.server.MonitorInfoHandler;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.dao.ConfigXmlDAO;

/**
 * 用于远程监控获取本服信息
 * @author hongfm
 *
 */
public class MonitorInfoServerHandlerImpl implements MonitorInfoHandler {

	private ConfigXmlDAO configXmlDAO = ConfigXmlDAO.getInstance();

	/**
	 * 取得消息信息 返回消息格式<info connect="" onlinenum="" queue="" charge="OK/Break"
	 * gmcc="OK/Break" database="OK/Break" d1xn_pipe="OK/Break" />
	 * connect-最大登陆人数 onlinenum-最大在线人数 queue-队伍数量 charge-计费服务器状态 gmcc服务器状态
	 * database-数据库状态 d1xn_pipe-行为服务器状态 XML中的内容可根据实际情况增减字段
	 * 
	 * @return String XML字符串
	 */
	public String getMessageInfo() {
		int maxonlineNum = RoleInfoMap.getMaxOnlineSize();
		int onlinenum = RoleInfoMap.getOnlineSize();
		int queueNum = 0;
		boolean charge = ChargeGameService.getChargeService()!=null&&ChargeGameService.getChargeService().isChargeOk();
		boolean gmcc = GmccGameService.getService()!=null&&GmccGameService.getService().isGmccOk();
		boolean database = configXmlDAO.testdbConnect();
		boolean d1xn_pipe = false;

		StringBuffer content = new StringBuffer();
		content.append("<info ");
		content.append(" connect=\"" + maxonlineNum + "\"");
		content.append(" online=\"" + onlinenum + "\"");
		content.append(" queue=\"" + queueNum + "\"");
		if (charge) {
			content.append(" charge=\"OK\"");
		} else {
			content.append(" charge=\"Break\"");
		}

		content.append(" db_err=\"OK\"" );
		
		if (gmcc) {
			content.append(" gmcc=\"OK\"");
		} else {
			if(GameValue.GAME_GMCC_FLAG == 1)
			{
				content.append(" gmcc=\"Break\"");
			}
			else
			{
				content.append(" gmcc=\"NOUSE\"");
			}
		}
		if (database) {
			content.append(" database=\"OK\"");
		} else {
			content.append(" database=\"Break\"");
		}
		if (d1xn_pipe) {
			content.append(" d1xn_pipe=\"OK\"");
		} else {
			content.append(" d1xn_pipe=\"Break\"");
		}
		content.append("/>");

		return content.toString();
	}

}
