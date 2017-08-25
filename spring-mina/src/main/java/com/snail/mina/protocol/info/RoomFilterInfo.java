package com.snail.mina.protocol.info;

import org.apache.mina.common.IoFilter;

public class RoomFilterInfo {

	private String name;
	private IoFilter ioFilter;
	
	public RoomFilterInfo(String name, IoFilter ioFilter) {
		this.name = name;
		this.ioFilter = ioFilter;
	}

	public String getName() {
		return name;
	}

	public IoFilter getIoFilter() {
		return ioFilter;
	}
	
	
}
