package com.snail.webgame.game.common;


public class DiffMail {
	private int roleId; // 角色集合
	private String attachment; // 附件信息
	private String content; // 邮件内容
	private String title; // 邮件标题
	private String reserve;

	public DiffMail(int roleId, String attachment, String content, String title, String reserve) {
		this.roleId = roleId;
		this.attachment = attachment;
		this.content = content;
		this.title = title;
		this.reserve = reserve;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
