package test.com.spring.world.common;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.util.LogicUtil;

public class TestJsonResp {

	public static void main(String[] args) {
		LogicUtil.initJson();
		
		CommonObj co = new CommonObj();
		
		PkgInfo pk1 = new PkgInfo();
		PkgInfo pk2 = new PkgInfo();
		
		pk1.setD(11);
		pk2.setD(22);
		
		pk1.setE(33);
		pk2.setE(44);
		
		List<PkgInfo> list = new ArrayList<>();
		list.add(pk1);
		list.add(pk2);
		
		co.setPkgSize(2);
		co.setList(list);
		
		long start = System.currentTimeMillis();
		
		for (int i = 0; i < 10000; i++) {
			LogicUtil.tojson(co);
		}
		//System.out.println(LogicUtil.tojson(co));
		
		System.out.println(System.currentTimeMillis() - start);
	}
}

class CommonObj {
	private int a = 1;
	private int b = 3;
	
	private int pkgSize;
	private List<PkgInfo> list;
	public int getA() {
		return a;
	}
	public void setA(int a) {
		this.a = a;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public int getPkgSize() {
		return pkgSize;
	}
	public void setPkgSize(int pkgSize) {
		this.pkgSize = pkgSize;
	}
	public List<PkgInfo> getList() {
		return list;
	}
	public void setList(List<PkgInfo> list) {
		this.list = list;
	}
	
	
	
	
}

class PkgInfo {
	private int d = 34;
	private int e = 23;
	
	public int getD() {
		return d;
	}
	public void setD(int d) {
		this.d = d;
	}
	public int getE() {
		return e;
	}
	public void setE(int e) {
		this.e = e;
	}
	
	
}

