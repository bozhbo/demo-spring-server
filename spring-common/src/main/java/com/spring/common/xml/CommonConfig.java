package com.spring.common.xml;

import java.util.ArrayList;
import java.util.List;

public class CommonConfig {
	List<CommonProperty> propList = new ArrayList<>();
	List<CommonRecord> recordList = new ArrayList<>();

	public List<CommonProperty> getPropList() {
		return propList;
	}

	public void setPropList(List<CommonProperty> propList) {
		this.propList = propList;
	}

	public List<CommonRecord> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<CommonRecord> recordList) {
		this.recordList = recordList;
	}

	public void addCommonProperty(CommonProperty commonProperty) {
		propList.add(commonProperty);
	}

	public void addCommonRecord(CommonRecord commonRecord) {
		recordList.add(commonRecord);
	}
}
