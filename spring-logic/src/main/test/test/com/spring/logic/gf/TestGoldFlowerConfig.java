package test.com.spring.logic.gf;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.spring.logic.gf.GoldFlowerConfig;
import com.spring.logic.gf.enums.PokerTypeEnum;
import com.spring.logic.gf.info.GoldFlowerInfo;


public class TestGoldFlowerConfig {

	@Test
	public void testConfig() {
		GoldFlowerConfig.init();
		
//		List<GoldFlowerInfo> list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_BOOM);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
//		
//		System.out.println("=============");
//		
//		list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_GREAT_GF);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
//		
//		System.out.println("=============");
//		
//		list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_GF);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
//		
//		System.out.println("+++++++++++++++");
//		
//		list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_STRAIGHT);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
//		
//		System.out.println("=============");
		
//		List<GoldFlowerInfo>  list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_PAIRS);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
		
//		List<GoldFlowerInfo>  list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_ALONE);
//		int i = 0;
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//			
//			if (i++ > 100) {
//				break;
//			}
//		}
		
//		List<GoldFlowerInfo>  list = GoldFlowerConfig.map.get(PokerTypeEnum.POKER_TYPE_MIN);
//		
//		for (GoldFlowerInfo goldFlowerInfo : list) {
//			System.out.println(goldFlowerInfo.toString());
//		}
	}
}
