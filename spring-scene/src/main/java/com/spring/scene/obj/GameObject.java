package com.spring.scene.obj;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.scene.action.IMoveAble;
import com.spring.scene.action.IVisibleAble;

public abstract class GameObject {

	private IMoveAble moveAble;
	private IVisibleAble visibleAble;
	
	@Autowired
	public void setMoveAble(IMoveAble moveAble) {
		this.moveAble = moveAble;
	}
	
	@Autowired
	public void setVisibleAble(IVisibleAble visibleAble) {
		this.visibleAble = visibleAble;
	}
}
