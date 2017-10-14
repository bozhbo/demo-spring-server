package test.com.snail.client.web.json;

import java.util.Map;

import com.spring.logic.util.LogicUtil;

public class TestJson {

	public static void main(String[] args) {
		LogicUtil.initJson();
		
		String str = "{\"tt\":185}";
		
		Map<String, Object> map = LogicUtil.fromJson(str, Map.class);
		
		System.out.println(map.get("tt"));
	}
}
