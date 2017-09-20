package com.spring.common.xml;

import java.util.ArrayList;
import java.util.List;

public class CommonRecord {
	private String name;
	private String cols;
	private String maxrows;
	private String save;
	private String visible;
	private String desc;

	private List<CommonColumn> list = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getMaxrows() {
		return maxrows;
	}

	public void setMaxrows(String maxrows) {
		this.maxrows = maxrows;
	}

	public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<CommonColumn> getList() {
		return list;
	}

	public void setList(List<CommonColumn> list) {
		this.list = list;
	}

	public void addCommonColumn(CommonColumn commonColumn) {
		list.add(commonColumn);
	}
}
