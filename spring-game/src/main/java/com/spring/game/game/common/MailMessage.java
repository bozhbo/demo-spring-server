package com.snail.webgame.game.common;

import java.util.List;

/**
 * 邮件信息
 * 
 * @author xiasd
 * 
 */
public class MailMessage extends TimeMessage {
	private List<Integer> roleIdList; // 角色集合
	private String attachment; // 附件信息
	private String content; // 邮件内容
	private String title; // 邮件标题
	private String reserve;

	public MailMessage(ETimeMessageType type, List<Integer> roleList, String attachment, String content, String title, String reserve) {
		super(type);
		this.roleIdList = roleList;
		this.attachment = attachment;
		this.content = content;
		this.title = title;
		this.reserve = reserve;
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
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

}
