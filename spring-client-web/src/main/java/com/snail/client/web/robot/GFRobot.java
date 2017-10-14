package com.snail.client.web.robot;

public class GFRobot {

	public void start() {
		RobotThread rt = new RobotThread(0, 10);
		rt.start();
	}
}
