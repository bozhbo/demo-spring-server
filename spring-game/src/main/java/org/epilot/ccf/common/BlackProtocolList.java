package org.epilot.ccf.common;

import java.util.HashSet;
import java.util.Set;

import org.epilot.ccf.core.protocol.Message;

public class BlackProtocolList {

	public static Set<Integer> set = new HashSet<Integer>();
	
	public static void addBlackProtocol(int protocolId) {
		set.add(protocolId);
	}
	
	public static void removeBlackProtocol(int protocolId) {
		set.remove(protocolId);
	}
	
	public static void clearBlackProtocol() {
		set.clear();
	}
	
	public static boolean contain(int protocolId) {
		return set.contains(protocolId);
	}
}
