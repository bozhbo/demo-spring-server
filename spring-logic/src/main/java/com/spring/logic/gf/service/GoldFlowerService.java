package com.spring.logic.gf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.spring.logic.gf.GoldFlowerConfig;
import com.spring.logic.gf.info.GoldFlowerInfo;

public class GoldFlowerService {

	public List<GoldFlowerInfo> randomPoker(int count) {
		List<GoldFlowerInfo> list = new ArrayList<>();
		Set<Integer> set = new HashSet<>();
		
		if (count > 6) {
			return list;
		}
		
		Random r = new Random();
		int size = GoldFlowerConfig.allList.size();
		
		while (set.size() < count) {
			set.add(r.nextInt(size));
		}
	
		for (Integer index : set) {
			list.add(GoldFlowerConfig.allList.get(index));
		}
		
		return list;
	}
	
	public void sortPoker(List<GoldFlowerInfo> list) {
		Collections.sort(list, (p1, p2) -> {return  p2.getLevel() - p1.getLevel();});
	}
}
