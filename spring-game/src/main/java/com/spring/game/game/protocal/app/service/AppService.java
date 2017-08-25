package com.snail.webgame.game.protocal.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.RoleChargeMap;
import com.snail.webgame.game.charge.order.RoleChargeService;
import com.snail.webgame.game.charge.text.util.TextUtil;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.RoleChargeErrorDao;
import com.snail.webgame.game.info.RoleChargeInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.log.RoleChargeLog;
import com.snail.webgame.game.protocal.app.AppStoreRechargeReq;
import com.snail.webgame.game.protocal.app.AppStoreRechargeResp;
import com.snail.webgame.game.protocal.app.common.EChargeState;
import com.snail.webgame.game.xml.cache.PayXMLInfoMap;
import com.snail.webgame.game.xml.info.PayXMLInfo;

public class AppService {
	private static final Logger logger = LoggerFactory.getLogger("logs");

	public static final String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
	public static final String _CONNECT_TIMEOUT = "com.sun.xml.ws.connect.timeout";
	
	public static final String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
	public static final String _REQUEST_TIMEOUT = "com.sun.xml.ws.request.timeout";

	/**
	 * 处理APP充值
	 * 
	 * @param roleInfo
	 * @param req
	 * @param resp 
	 */
	public static void doAppCharge(RoleInfo roleInfo,AppStoreRechargeReq req, AppStoreRechargeResp resp){
		String sCardType = req.getPid();// 卡类型 900
		PayXMLInfo ios = PayXMLInfoMap.getPidPayXMLInfo(sCardType);
		
		if (ios == null) {
			logger.error("APP rechare error sCardType is not found, sCardType = " + sCardType);
			return ;
		}
		String totalValue = (ios.getMoneyCost() * req.getAmount()) + "";// 总值(人民币数)
		String sUserName = roleInfo.getAccount(); // 总价=卡类型配置价格*数量

		String sAccountTypeID = GameConfig.getInstance().getsAccountTypeId();// 账户类型(0-中心,1-分区)
		String sAreaID = Integer.toString(GameConfig.getInstance().getGameServerId());// 充入分区ＩＤ（如果有赠送道具，也赠入此分区ＩＤ）
		String sImprestAccountIP = roleInfo.getRoleLoadInfo() == null ? "" : roleInfo.getLoginIp();// 充值人ＩＰ
		boolean send = false;
		long seqId = 0;
		
		try {
			String jsonData = req.getTransactionData().replaceAll("\r\n", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll("\f", "").replaceAll("\b", "");
			// 先插入极效异常表，后期改状态
			seqId = RoleChargeErrorDao.insertChargeError(0, "0", req.getTransactionData(), sCardType, 
					req.getAmount(), ios.getMoneyCost() * req.getAmount(), sUserName, Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID), 
					sAreaID, roleInfo.getLoginIp(), "", roleInfo.getId(), roleInfo.getAccount(), "", EChargeState.PREPARE_SEND_TO_TRANSIT_SERVER.getValue(), -918918);
			
			// 先存储
			RoleChargeService.saveChargeOrderInfo(roleInfo, req.getTransactionIdentifier(), ios.getNo(), seqId, EChargeState.PREPARE_SEND_TO_TRANSIT_SERVER);
			GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), req.getTransactionIdentifier(), ios.getNo(), RoleChargeLog.ROLE_CHARGE_EVENT_4);
			
			if(seqId > 0){
				send = TextUtil.sendMessage("AppleCharge", jsonData, sCardType, sUserName, sImprestAccountIP, 
						req.getAmount(), Integer.valueOf(totalValue), Integer.valueOf(sAccountTypeID), Integer.valueOf(sAreaID),"" , String.valueOf(seqId));
			} else {
				logger.error("APP rechare error insert failed : " + 0 + "," + 0 + "," + req.getTransactionData() + "," + sCardType + "," + req.getAmount() + "," + (ios.getMoneyCost() * req.getAmount()) + "," + sUserName + "," + Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID) + "," + sAreaID + "," + roleInfo.getLoginIp() + "," + "" + "," + roleInfo.getId() + "," + roleInfo.getAccount() + "," + "" + "," + 1);
			}
		} catch (Exception e) {
			logger.error("APP rechare error Exception", e);
			
			if(!RoleChargeErrorDao.updateRecordStatus(seqId, EChargeState.EXCEPTION_SEND_TO_TRANSIT_SERVER.getValue(), -918917)){
				logger.error("RoleChargeErrorDao.updateRecordStatus error1");
			} else {
				RoleChargeInfo roleChargeInfo = RoleChargeMap.fetchRoleChargeInfoBySeqId(seqId);
				
				if(roleChargeInfo != null){
					roleChargeInfo.setState(EChargeState.EXCEPTION_SEND_TO_TRANSIT_SERVER);
				}
			}
			return;
		}

		if (!send) {
			logger.error("APP rechare error: " + 0
					+ "," + 0 + "," + req.getTransactionData() + "," + sCardType + "," + req.getAmount() 
					+ "," + (ios.getMoneyCost() * req.getAmount()) + "," + sUserName
					+ "," + Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID) 
					+ "," + sAreaID + "," + roleInfo.getLoginIp() + "," + "" 
					+ "," + roleInfo.getId() + "," + roleInfo.getAccount() + "," + "" + "," + 0);

			if(!RoleChargeErrorDao.updateRecordStatus(seqId, EChargeState.FAILED_SEND_TO_TRANSIT_SERVER.getValue(), -918916)){
				logger.error("RoleChargeErrorDao.updateRecordStatus error2");
			} else {
				RoleChargeInfo roleChargeInfo = RoleChargeMap.fetchRoleChargeInfoBySeqId(seqId);
				
				if(roleChargeInfo != null){
					roleChargeInfo.setState(EChargeState.FAILED_SEND_TO_TRANSIT_SERVER);
				}
			}
			GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), req.getTransactionIdentifier(), ios.getNo(), RoleChargeLog.ROLE_CHARGE_EVENT_6);
		} else {
			if(resp != null){
				resp.setResult(1);
			}
			
			if(!RoleChargeErrorDao.updateRecordStatus(seqId, EChargeState.HAS_SEND_TO_TRANSIT_SERVER.getValue(), -918915)){
				logger.error("RoleChargeErrorDao.updateRecordStatus error2");
			} else {
				RoleChargeInfo roleChargeInfo = RoleChargeMap.fetchRoleChargeInfoBySeqId(seqId);
				
				if(roleChargeInfo != null){
					roleChargeInfo.setState(EChargeState.HAS_SEND_TO_TRANSIT_SERVER);
				}
			}
			
			GameLogService.insertRoleChargeLog(roleInfo.getAccount(), roleInfo.getRoleName(), req.getTransactionIdentifier(), ios.getNo(), RoleChargeLog.ROLE_CHARGE_EVENT_5);
		}
	}
	
//	public static void main(String[] args) {
//		TextUtil.startAppChargeThread("10.101.7.102", 3002, null);
//		
//		Document doc = XMLUtil4DOM.file2Dom(GameConfig.class.getResourceAsStream("/config/system-config.xml"));
//		
//		Element rootEle = null;
//		if (doc != null) {
//			rootEle = doc.getRootElement();
//			String log4jPaht = rootEle.elementTextTrim("log4j-path");
//			if (log4jPaht != null && log4jPaht.length() > 0 && log4jPaht.endsWith("log4j.properties")) {
//				InputStream is = GameConfig.class.getResourceAsStream(log4jPaht);
//				Properties properties = new Properties();
//				try {
//					properties.load(is);
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally {
//					if (is != null) {
//						try {
//							is.close();
//						} catch (IOException e) {
//						}
//					}
//				}
//				PropertyConfigurator.configure(properties);
//			}
//		}
//			
//		try {
//			TimeUnit.SECONDS.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		TextUtil.sendMessage("AppleCharge","{\"signature\" = \"AjCt0UpklnOC65zDDyLpHsk0MUNJe/c3IKSRFRyNld93Gt/Jrt7fCBsxRvO3qtpfmxlAkVW0TtRSMJApBoHZRxfchT9mr1McxmPVT5tFTya+HrPv5iOGy6J62wUOyKYZ8n32nj4CMlTVCxJrqJDuCWNHNQeAc2VlQ0mKoGrzH5V4AAADVzCCA1MwggI7oAMCAQICCBup4+PAhm/LMA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTE0MDYwNzAwMDIyMVoXDTE2MDUxODE4MzEzMFowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMmTEuLgjimLwRJxy1oEf0esUNDVEIe6wDsnnal14hNBt1v195X6n93YO7gi3orPSux9D554SkMp+Sayg84lTc362UtmYLpWnb34nqyGx9KBVTy5OGV4ljE1OwC+oTnRM+QLRCmeNxMbPZhS47T+eZtDEhVB9usk3+JM2Cogfwo7AgMBAAGjcjBwMB0GA1UdDgQWBBSJaEeNuq9Df6ZfN68Fe+I2u22ssDAMBgNVHRMBAf8EAjAAMB8GA1UdIwQYMBaAFDYd6OKdgtIBGLUyaw7XQwuRWEM6MA4GA1UdDwEB/wQEAwIHgDAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAeaJV2U51rxfcqAAe5C2/fEW8KUl4iO4lMuta7N6XzP1pZIz1NkkCtIIweyNj5URYHK+HjRKSU9RLguNl0nkfxqObiMckwRudKSq69NInrZyCD66R4K77nb9lMTABSSYlsKt8oNtlhgR/1kjSSRQcHktsDcSiQGKMdkSlp4AyXf7vnHPBe4yCwYV2PpSN04kboiJ3pBlxsGwV/ZlL26M2ueYHKYCuXhdqFwxVgm52h3oeJOOt/vY4EcQq7eqHm6m03Z9b7PRzYM2KGXHDmOMk7vDpeMVlLDPSGYz1+U3sDxJzebSpbaJmT7imzUKfggEY7xxf4czfH0yj5wNzSGTOvQ==\";\"purchase-info\" = \"ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDE1LTA4LTI4IDAyOjU4OjU3IEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJInVuaXF1ZS1pZGVudGlmaWVyIiA9ICI0OWQzNTg2NDkyMDcyM2U0ZGFlYWY4NjY5NTMzZGE5OGI5ZTI0Mjc4IjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDE2OTU5NTc2NSI7CgkiYnZycyIgPSAiMC4xLjUiOwoJInRyYW5zYWN0aW9uLWlkIiA9ICIxMDAwMDAwMTY5NTk1NzY1IjsKCSJxdWFudGl0eSIgPSAiMSI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZS1tcyIgPSAiMTQ0MDc1NTkzNzgxMiI7CgkidW5pcXVlLXZlbmRvci1pZGVudGlmaWVyIiA9ICI4Qjg0MDNDQS0wNzdGLTQ0QUYtOEZDNy1BMzI3RjM5MThCNjMiOwoJInByb2R1Y3QtaWQiID0gImNvbS5zbmFpbGdhbWVzLmd5Yy5wcm9kdWN0X3BvaW50LjMyOCI7CgkiaXRlbS1pZCIgPSAiMTAwNjUyMjg2NCI7CgkiYmlkIiA9ICJjb20uc25haWxnYW1lcy5neWMuYXBwc3RvcmUiOwoJInB1cmNoYXNlLWRhdGUtbXMiID0gIjE0NDA3NTU5Mzc4MTIiOwoJInB1cmNoYXNlLWRhdGUiID0gIjIwMTUtMDgtMjggMDk6NTg6NTcgRXRjL0dNVCI7CgkicHVyY2hhc2UtZGF0ZS1wc3QiID0gIjIwMTUtMDgtMjggMDI6NTg6NTcgQW1lcmljYS9Mb3NfQW5nZWxlcyI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZSIgPSAiMjAxNS0wOC0yOCAwOTo1ODo1NyBFdGMvR01UIjsKfQ==\";\"environment\" = \"Sandbox\";\"pod\" = \"100\";\"signing-status\" = \"0\";}","com.snailgames.gyc.product_point.328","776E7465737479616C6937383934","172.17.36.160",1,328,1,7400005,"","123");
//	}
	
//	public static void main(String[] args) {
//		GameConfig.getInstance();
//		String sAgentId = "216";// 平台ID
//		String sAgentPwd = "123456789";// 平台密码
//		String sCardType = "1";// 卡类型 900
//		String amcount = "1";// 数量
//
//		String totalValue = 1 + "";// 总值(人民币数)
//		String sUserName = ""; // 总价=卡类型配置价格*数量
//
//		String sAccountTypeID = "1";// 账户类型(0-中心,1-分区)
//		String sAreaID = Integer.toString(7400001);// 充入分区ＩＤ（如果有赠送道具，也赠入此分区ＩＤ）
//		String sImprestAccountIP = "202.108.23.50";// 充值人ＩＰ
//		String seed = "123456";// SEED
//
//		String transactionData = "asdfasdf";
//		
//		String sVerifyStr = EncodeUtil.md5_32(
//				sAgentId + sAgentPwd + transactionData + sCardType + amcount + totalValue + sUserName + sAccountTypeID + sAreaID + sImprestAccountIP + seed).toUpperCase();// 校验串，前面参数依次串接再加上ＳＥＥＤ，最后ＭＤ５，再转大写．
//		String result = "";
//		
//		try {
//			ImprestServiceForJunWang service = new ImprestServiceForJunWang();
//			ImprestServiceForJunWangPortType portType = service.getImprestServiceForJunWangHttpPort();
//			
//			Map<String, Object> ctx = ((BindingProvider)portType).getRequestContext();
//			ctx.put(CONNECT_TIMEOUT, 30 * 1000);
//			ctx.put(_CONNECT_TIMEOUT, 30 * 1000);
//			ctx.put(REQUEST_TIMEOUT, 30 * 1000);
//			ctx.put(_REQUEST_TIMEOUT, 30 * 1000);
//			
//			result = portType.completeImprestAmount(sAgentId, sAgentPwd, transactionData, sCardType, amcount, totalValue, sUserName, sAccountTypeID, sAreaID, sImprestAccountIP,
//					sVerifyStr);
//		} catch (Exception e) {
//			logger.error("APP rechare error Exception", e);
//			boolean flag = RoleChargeErrorDao.insertChargeError(Integer.parseInt(sAgentId == null ? "0" : sAgentId), sAgentPwd, transactionData, sCardType, 1, 1, sUserName, Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID), sAreaID, "202.108.23.50", sVerifyStr, 123, "123", result, 1, 0);
//			
//			if (!flag) {
//				logger.error("APP rechare error insert failed : " + Integer.parseInt(sAgentId == null ? "0" : sAgentId) + "," + sAgentPwd + "," + transactionData + "," + sCardType + "," + 1 + "," + (1) + "," + sUserName + "," + Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID) + "," + sAreaID + "," + "172.19.60.39" + "," + sVerifyStr + "," + 1 + "," + "xxx" + "," + result + "," + 1);
//			}
//			return;
//		}
//
//		logger.info("completeImprestAmount = " + result);
//
//		RechargeVO rechargeVo = RechargeVOUtil.getRechargeVO(result);
//		if (!rechargeVo.isSuccess()) {
//			int n_error_code;
//			
//			try {
//				n_error_code = Integer.valueOf(rechargeVo.getResult());
//			} catch (Exception e) {
//				n_error_code = 0;
//			}
//			AppChargeFailedException appChargeFailedException = new AppChargeFailedException();
//			appChargeFailedException.setFailedResult(result);
//			appChargeFailedException.setnErrorCode(n_error_code);
//			
//			logger.error("APP rechare error result is not 1" + result);
//			
//			boolean flag = RoleChargeErrorDao.insertChargeError(Integer.parseInt(sAgentId == null ? "0" : sAgentId), sAgentPwd,
//					"123", sCardType, 1, 
//					1, sUserName, Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID), 
//					sAreaID, "10.101.1.13", sVerifyStr, 1, "123", 
//					result, 0, n_error_code);
//		
//			if (!flag) {
//				logger.error("APP rechare error insert failed : " + Integer.parseInt(sAgentId == null ? "0" : sAgentId) 
//						+ "," + sAgentPwd + "," + 1 + "," + sCardType + "," + 1
//						+ "," + (1) + "," + sUserName
//						+ "," + Integer.parseInt(sAccountTypeID == null ? "0" : sAccountTypeID) 
//						+ "," + sAreaID + "," + "10.101.1.13" + "," + sVerifyStr 
//						+ "," + 123 + "," + 123 + "," + result + "," + 0);
//			}
//		}
//	}
}
