package com.snail.webgame.game.common;

import java.sql.Timestamp;

public class RoleOutLog 
{
		private String serial;
		private Timestamp logoutTime;
		public String getSerial() {
			return serial;
		}
		public void setSerial(String serial) {
			this.serial = serial;
		}
		public Timestamp getLogoutTime() {
			return logoutTime;
		}
		public void setLogoutTime(Timestamp logoutTime) {
			this.logoutTime = logoutTime;
		}
		
	
}
