package com.snail.webgame.engine.game;

import java.nio.ByteOrder;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.snail.webgame.engine.component.room.protocol.code.RequestDecode;
import com.snail.webgame.engine.component.room.protocol.code.RequestEncoder;
import com.snail.webgame.engine.component.room.protocol.filter.ProtocolCodecFilter;
import com.snail.webgame.engine.component.room.protocol.handler.ServerHandle;
import com.snail.webgame.engine.component.room.protocol.info.RoomFilterInfo;
import com.snail.webgame.engine.component.room.protocol.server.RoomServer;
import com.snail.webgame.engine.game.base.init.GameConfig;
import com.snail.webgame.engine.game.base.spring.BaseBeanFactory;
import com.snail.webgame.engine.game.dao.BaseSuperDao;
import com.snail.webgame.engine.game.dao.SuperDaoRequest;
import com.snail.webgame.engine.game.enums.SqlType;
import com.snail.webgame.engine.net.filter.MessageCodecFilter;
import com.snail.webgame.engine.net.handler.SessionStateHandlerImpl;

/**
 * 
 * 类介绍:游戏启动类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class GameServer {
	public static void main(String[] args) {
		GameConfig.init();
		
		RoomServer server = new RoomServer();
		
		List<RoomFilterInfo> ioFilterList = new ArrayList<RoomFilterInfo>();
		ioFilterList.add(new RoomFilterInfo("codec", new ProtocolCodecFilter(new RequestEncoder(), new RequestDecode())));
		ioFilterList.add(new RoomFilterInfo("messagecodec", new MessageCodecFilter("GameServer", ByteOrder.BIG_ENDIAN)));
		
		server.start(GameConfig.serverIp, GameConfig.serverPort, GameConfig.socketThreads - 1, new ServerHandle(new SessionStateHandlerImpl(), GameConfig.getProcessorThreadPool().getThreadPool()), false, ioFilterList);
		
//		ServerHandle handle = new ServerHandle(null, ServerHandleThreadPool.getInstance().getThreadPool());
//		
//		BaseMessage message = new BaseMessage();
//		
//		GameMessageHead head = new GameMessageHead();
//		head.setMsgType(0xA005);
//		head.setUserState(1);
//		
//		GamePrimitiveReq req = new GamePrimitiveReq();
//		req.setMsgType(0xA005);
//		try {
//			Field field = req.getClass().getDeclaredField("objs");
//			field.setAccessible(true);
//			
//			Object[] objs = new Object[2 + 1];
//			objs[1] = "bob";
//			objs[2] = "123456";
//			field.set(req, objs);
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		
//		message.setiMessageHead(head);
//		message.setiMessageBody(req);
//		// handle.messageReceived(null, message);
//		
//		test();
	}
	
	private static void test() {
		BaseSuperDao dao = BaseBeanFactory.getBeanByClass(BaseSuperDao.class);
		List<Object[]> list = new ArrayList<Object[]>();
		list.add(new Object[]{"bob"});
		list.add(new Object[]{"wmf"});
		list.add(new Object[]{"kkk"});
		list.add(new Object[]{"ddd"});
		list.add(new Object[]{"fff"});
		
		SuperDaoRequest superDaoRequest1 = new SuperDaoRequest(SqlType.INSERT_FORKEY_BATCH, dao.getSqlMap().get("role_name_insert").toString(), list, new int[] {Types.VARCHAR}, "Id");
		SuperDaoRequest superDaoRequest2 = new SuperDaoRequest(SqlType.UPDATE, dao.getSqlMap().get("role_step_update_star").toString(), new Object[] {10, 199}, new int[] {Types.TINYINT, Types.INTEGER});
		SuperDaoRequest superDaoRequest3 = new SuperDaoRequest(SqlType.INSERT_FORKEY_BATCH, dao.getSqlMap().get("role_name_insert").toString(), list, new int[] {Types.VARCHAR}, "Id");
		
		try {
			dao.execute(superDaoRequest1, superDaoRequest2, superDaoRequest3);
			
			System.out.println(Arrays.toString(superDaoRequest1.getResults()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
