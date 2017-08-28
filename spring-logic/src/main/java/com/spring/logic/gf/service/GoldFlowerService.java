package com.spring.logic.gf.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoldFlowerService {
	
	public List<Integer> randomPoker(int count) {
		List<Integer> list = new ArrayList<>(18);
		
		if (count > 6) {
			return list;
		}
		
		int allCount = count * 3;
		
		Set<Integer> chooseSet = new HashSet<>(18);
		
		while (chooseSet.size() < allCount) {
			int value = random(52);
			
			if (chooseSet.add(value)) {
				list.add(value);
			}
		}
		
		return list;
	}
	
	public int random(int v) {
		return (int)(Math.random() * v) + 1;
	}
}
