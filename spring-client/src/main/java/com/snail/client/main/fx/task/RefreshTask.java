package com.snail.client.main.fx.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.ISceneParam;
import com.snail.client.main.fx.scene.param.SceneParam;

import javafx.concurrent.Task;

public class RefreshTask extends Task<ISceneParam>{
	
	LinkedBlockingQueue<ISceneParam> queue = new LinkedBlockingQueue<>();

	@Override
	protected ISceneParam call() throws Exception {
		return null;
	}

	@Override
	public void run() {
		while (true) {
			try {
				ISceneParam sceneParam = queue.poll(5000, TimeUnit.MILLISECONDS);
				
				if (sceneParam != null) {
					if (sceneParam instanceof SceneParam) {
						ClientControl.sceneControl.forward("scene", (SceneParam)sceneParam);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
