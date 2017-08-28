package test.com.spring.logic.gf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.spring.logic.gf.service.GoldFlowerService;

public class TestRandomPerformance {
	
	public static Set<Integer> set = new HashSet<>();
	public static Set<Integer> set1 = new HashSet<>();

	public static void main(String[] args) {
		for (int i = 0; i < 52; i++) {
			set.add(i + 1);
			set1.add(i + 1);
		}
		
		GoldFlowerService service = new GoldFlowerService();
		
		long l = System.currentTimeMillis();
		
		for (int i = 0; i < 10000; i++) {
			List<Integer> list = service.randomPoker(6);
			
			System.out.println(Arrays.toString(list.toArray()));
		}
		
		System.out.println(System.currentTimeMillis() - l);
		
		if (set1.size() > 0) {
			System.out.println("++++++++++++");
		}
	}
	
	public static void test() {
		//Random r = new Random();
		//int a = r.nextInt(100000);
		int b = (int)(Math.random() * 52) + 1;
		//System.out.println(b);
	}
}
