package com.spring.common.pathfinding.astar;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.spring.common.info.MapPoint;

public class GameMap implements TileBasedMap {
	private String mapId;
	private int WIDTH = 0;

	private int HEIGHT = 0;

	private byte[][] terrain = null;
	
	private float[][] elevation = null;//高度信息

	private String[][] units = null;

	private boolean[][] visited = null;

	private MapPoint[][] mapPoints = null;
	private float gridTitleX;//一格所占的世界X坐标
	private float gridTitleY;//一格所占的世界Y坐标
	
	public GameMap(String mapId, byte[][] terrain,int width,int height) {
		this.mapId = mapId;
		this.terrain = terrain;
		this.units = new String[width][height];
		this.visited = new boolean[width][height];
		this.WIDTH = width;
		this.HEIGHT = height;
	}
	
	public GameMap(String mapId, byte[][] terrain,String[][] units,boolean[][] visited,int width,int height) {
		this.mapId = mapId;
		this.terrain = terrain;
		this.units = units;
		this.visited = visited;
		this.WIDTH = width;
		this.HEIGHT = height;
	}
	public GameMap(String mapId, byte[][] terrain,float[][] elevation,String[][] units,boolean[][] visited,int width,int height) {
		this.mapId = mapId;
		this.terrain = terrain;
		this.elevation = elevation;
		this.units = units;
		this.visited = visited;
		
		this.WIDTH = width;
		this.HEIGHT = height;
	}
	public GameMap(String mapId, byte[][] terrain,float[][] elevation,String[][] units,boolean[][] visited,int width,int height, MapPoint[][] mapPoints) {
		this.mapId = mapId;
		this.terrain = terrain;
		this.elevation = elevation;
		this.units = units;
		this.visited = visited;
		this.mapPoints = mapPoints;
		
		this.WIDTH = width;
		this.HEIGHT = height;
	}



	public float getGridTitleX() {
		return gridTitleX;
	}




	public String getMapId() {
		return mapId;
	}



	public void setGridTitleX(float gridTitleX) {
		this.gridTitleX = gridTitleX;
	}



	public float getGridTitleY() {
		return gridTitleY;
	}

	public float getElevation(int x, int y) {
		try{
			return elevation[x][y];
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.print("x:"+x+"y:"+y+"length:"+elevation.length+"width:"+elevation[0].length+"\n");
		}
		return 0;
	}
	
	public void setElevation(int x, int y, float e) {
		elevation[x][y] = e;
	}
	
 	public void setUnit(int x, int y, String unit,int gridNum) {
 		
 		List<Point> points = getGridPoint(x, y, gridNum);
 		for(Point p : points){
 			units[p.x][p.y] = unit;
 		}
	}
 	public void setUnit(int x, int y, String unit){
 		units[x][y] = unit;
 	}

	public void setGridTitleY(float gridTitleY) {
		this.gridTitleY = gridTitleY;
	}



	public void clearVisited() {
		for (int x = 0; x < getWidthInTiles(); x++) {
			for (int y = 0; y < getHeightInTiles(); y++) {
				visited[x][y] = false;
			}
		}
	}

	public boolean visited(int x, int y) {
		return visited[x][y];
	}

	public int getTerrain(int x, int y) {
		return terrain[x][y];
	}

	/**
	 * 根据所占格子数量，得到所占格子坐标
	 * @param x
	 * @param y
	 * @param gridNum
	 * @return
	 */
	public List<Point> getGridPoint(int x, int y, int gridNum){
		List<Point> result = new ArrayList<Point>();
		int v = (int)Math.sqrt(gridNum);
		if(gridNum==1)
		{
			result.add(new Point(x, y));
		}
		else if(gridNum % 2 == 0)
		{
			for(int i=0;i<v;i++)
			{
				for(int j=0;j<v;j++)
				{
					if(x+i<getWidthInTiles()&&y+j<getHeightInTiles())
					{
						result.add(new Point(x+i, y+j));
					}
				}
			}
		 
		}
		else if(gridNum % 2 == 1)
		{
			for(int i=-(v/2);i<=(v/2);i++)
			{
				for(int j=-(v/2);j<=(v/2);j++)
				{
					if(x+i>=0&&x+i<getWidthInTiles()&&y+j>=0&&y+j<getHeightInTiles())
					{
						result.add(new Point(x+i, y+j));
					}
				}
			}
		}
		else{
			result.add(new Point(x, y));
		}
		return result;
	}
	public List<Byte> getTerrain(int x, int y, int gridNum) {
		List<Byte> result = new ArrayList<Byte>();
		List<Point> points = getGridPoint(x, y, gridNum);
		for(Point p : points){
			result.add(terrain[p.x][p.y]);
		}
		return result;
	}
	
	public void setTerrain(int x, int y, byte unit) {
		terrain[x][y] = unit;
	}

	public String getUnit(int x, int y){
		return units[x][y];
	}
	public List<String> getUnit(int x, int y, int gridNum) {
		List<String> result = new ArrayList<String>();
		List<Point> points = getGridPoint(x, y, gridNum);
		for(Point p : points){
			String unit = units[p.x][p.y];
			if(unit != null)
				result.add(unit);
		}
		return result;
	}

	/**
	 * 设置地图上玩家
	 * @param x
	 * @param y
	 * @param roleId
	 */
	public void setRole(int x, int y, long roleId){
		MapPoint result = mapPoints[x][y];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y] = result;
		}
		if(!result.getRoleIds().contains(roleId))
			result.getRoleIds().add(roleId);
	}
	
	/**
	 * 删除地图上玩家
	 * @param x
	 * @param y
	 * @param roleId
	 */
	public void delRole(int x, int y, long roleId){
		MapPoint result = mapPoints[x][y];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y] = result;
		}
		result.getRoleIds().remove(roleId);
	}
	
	/**
	 * 设置地图上NPC
	 * @param x
	 * @param y
	 * @param npcId
	 */
	public void setNPC(int x, int y, long npcId){
		MapPoint result = mapPoints[x][y];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y] = result;
		}
		if(!result.getNpcIds().contains(npcId))
			result.getNpcIds().add(npcId);
	}
	
	/**
	 * 删除地图上NPC
	 * @param x
	 * @param y
	 * @param npcId
	 */
	public void delNPC(int x, int y, long npcId){
		MapPoint result = mapPoints[x][y];
		if (result == null) {
			result = new MapPoint();
			mapPoints[x][y] = result;
		}
		result.getNpcIds().remove(npcId);
	}

	public void setMapPoint(int x, int y, MapPoint mapPoint) {
		mapPoints[x][y] = mapPoint;
	}

	public List<MapPoint> getMapPointArea(int startX, int endX, int startY,
			int endY) {
		List<MapPoint> result = new ArrayList<MapPoint>();
		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;
		if (endX >= WIDTH)
			endX = WIDTH-1;
		if (endY >= HEIGHT)
			endY = HEIGHT-1;

		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				MapPoint mapPoint = mapPoints[x][y];
				if (mapPoint != null) {
					result.add(mapPoint);
				}
			}
		}
		return result;
	}

	public byte[][] getTerrain() {
		return terrain;
	}

	public boolean blocked(Mover mover, int x, int y) {

		String type = null;
		boolean moveFlag = false;
		int gridNum = 1;
		List<String> teamType = null;
		if (mover == null) {
			return true;
		}
		else {
			UnitMover unitMover = (UnitMover) mover;
			type = unitMover.getType();
			moveFlag = unitMover.isMoveFlag();
			gridNum = unitMover.getGridNum();
			teamType = unitMover.getTeamType();
		}

		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
			return true;
		}

		if (moveFlag) {
			return false;
		}

		List<Point> points = getGridPoint(x, y, gridNum);
		for(Point p : points){
			if (terrain[p.x][p.y] == 1 || terrain[p.x][p.y] == 9) {
				return true;
			}
			String gridStr1 = getUnit(p.x, p.y);

			if (gridStr1 != null && !gridStr1.equals(type) && !teamType.contains(gridStr1)) {
				return true;
			}
		}
		return false;
	}

	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		if (sx == tx || sy == ty)
			return 10;
		else
			return 14;
	}

	public int getHeightInTiles() {
		return HEIGHT;
	}

	public int getWidthInTiles() {

		return WIDTH;
	}

	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}

//	/**
//	 * 获得该点附近其它角色(不包括自己)
//	 * 
//	 * @param 自己
//	 * @param radiiX
//	 *            X轴半径
//	 * @param radiiY
//	 *            Y轴半径
//	 * @return
//	 */
//	public List<Long> getAreaOtherRole(AIInfo aiInfo, int radiiX, int radiiY) {
//		int x = (int) aiInfo.getCurrX();
//		int y = (int) aiInfo.getCurrY();
//		List<Long> roleIds = getAreaAllRole(x, y, radiiX, radiiY);
//		if(aiInfo instanceof RoleInfo)
//			roleIds.remove(aiInfo.getId());
//		return roleIds;
//	}
//	
//	/**
//	 * 检查附近玩家和NPC变化
//	 * @param aiInfo
//	 * @param radiiX
//	 *            X轴半径
//	 * @param radiiY
//	 *            Y轴半径
//	 */
//	public ViewChangeInfo checkAreaChange(AIInfo aiInfo, int radiiX, int radiiY){
//		ViewChangeInfo result = new ViewChangeInfo();
//		
//		//新附近的玩家
//		List<Long> newRoleIds = new ArrayList<Long>();
//		//新附近的NPC
//		List<Long> newNpcIds = new ArrayList<Long>();
//		
//		// 老附近的玩家
//		List<Long> oldRoleIds = aiInfo.getOtherRoleIds();
//		
//		// 附近新出现的玩家
//		List<Long> addRoleIds = new ArrayList<Long>();
//		// 附近消失的玩家
//		List<Long> delRoleIds = new ArrayList<Long>();
//		delRoleIds.addAll(oldRoleIds);
//		
//		
//		// 老附近的NPC
//		List<Long> oldNPCIds = aiInfo.getNpcIds();
//		
//		// 附近新出现的NPC
//		List<Long> addNPCIds = new ArrayList<Long>();
//		// 附近消失的NPC
//		List<Long> delNPCIds = new ArrayList<Long>();
//		delNPCIds.addAll(oldNPCIds);
//		
//		List<MapPoint> mapPoints = getMapPointArea((int)aiInfo.getCurrX() - radiiX, (int)aiInfo.getCurrX() + radiiX, (int)aiInfo.getCurrY()
//				- radiiY, (int)aiInfo.getCurrY() + radiiY);
//		if (mapPoints != null && !mapPoints.isEmpty()) {
//			for (MapPoint mapPoint : mapPoints) {
//				if (mapPoint != null) {
//					List<Long> tmpRoles = mapPoint.getRoleIds();
//					newRoleIds.addAll(tmpRoles);
//					for(long roleId : tmpRoles){
//						//排除自己
//						if(aiInfo instanceof RoleInfo && aiInfo.getId() == roleId){
//							newRoleIds.remove(roleId);
//							continue;
//						}
//						//附近新出现的玩家
//						if (!oldRoleIds.contains(roleId)) {
//							addRoleIds.add(roleId);
//						}
//						//附近消失的玩家
//						if(delRoleIds.contains(roleId)){
//							delRoleIds.remove(roleId);
//						}
//					}
//					if(aiInfo instanceof RoleInfo){
//						List<Long> tmpNPCs = mapPoint.getNpcIds();
//						newNpcIds.addAll(tmpNPCs);
//						for(long npcId : tmpNPCs){
//							//附近新出现的NPC
//							if(!oldNPCIds.contains(npcId)){
//								addNPCIds.add(npcId);
//							}
//							//附近消失的NPC
//							if(delNPCIds.contains(npcId)){
//								delNPCIds.remove(npcId);
//							}
//						}
//					}
//				}
//			}
//		}
//				
//		result.setNewRoleIds(newRoleIds);
//		result.setNewNpcIds(newNpcIds);
//		result.setAddRoleIds(addRoleIds);
//		result.setDelRoleIds(delRoleIds);
//		result.setAddNPCIds(addNPCIds);
//		result.setDelNPCIds(delNPCIds);
//		
//		return result;
//	}

	/**
	 * 获得该点附近所有角色
	 * 
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 * @param radiiX
	 *            X轴半径
	 * @param radiiY
	 *            Y轴半径
	 * @return
	 */
	public List<Long> getAreaAllRole(int x, int y, int radiiX, int radiiY) {
		List<MapPoint> mapPoints = getMapPointArea(x - radiiX, x + radiiX, y
				- radiiY, y + radiiY);
		List<Long> roleIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					roleIds.addAll(mapPoint.getRoleIds());
				}
			}
		}
		return roleIds;
	}
	/**
	 * 获取玩家角色所在坐标附近的npc信息
	 * 
	 * @param currX
	 * @param currY
	 * @param refreshRadiiX
	 * @param refreshRadiiY
	 * @return
	 * @author wangxf
	 * @date 2012-7-30
	 */
	public List<Long> getRoundNpcInfo(int x, int y, int radiiX, int radiiY) {
		List<MapPoint> mapPoints = getMapPointArea(x - radiiX, x + radiiX, y
				- radiiY, y + radiiY);
		List<Long> npcIds = new ArrayList<Long>();
		if (mapPoints != null && !mapPoints.isEmpty()) {
			for (MapPoint mapPoint : mapPoints) {
				if (mapPoint != null) {
					npcIds.addAll(mapPoint.getNpcIds());
				}
			}
		}
		return npcIds;
	}

	/**
	 * 判断两节点之间是否存在障碍物
	 * 
	 */
	public boolean hasBarrier(int startX, int startY, int endX, int endY, Mover mover) {
		if (startX == endX && startY == endY)
			return false;

		Point point1 = new Point(startX, startY);
		Point point2 = new Point(endX, endY);
		// 根据起点终点间横纵向距离的大小来判断遍历方向
		int distX = Math.abs(endX - startX);
		int distY = Math.abs(endY - startY);

		/** 遍历方向，为true则为横向遍历，否则为纵向遍历 */
		boolean loopDirection = distX > distY ? true : false;

		/** 循环递增量 */
		double i;

		/** 循环起始值 */
		int loopStart;

		/** 循环终结值 */
		int loopEnd;

		// 起终点连线所经过的节点
		List<Point> passedNodeList;

		// 为了运算方便，以下运算全部假设格子尺寸为1，格子坐标就等于它们的行、列号
		if (loopDirection) {

			loopStart = Math.min(startX, endX);
			loopEnd = Math.max(startX, endX);

			// 开始横向遍历起点与终点间的节点看是否存在障碍(不可移动点)
			for (i = loopStart; i <= loopEnd; i++) {
				// 由于线段方程是根据终起点中心点连线算出的，所以对于起始点来说需要根据其中心点
				// 位置来算，而对于其他点则根据左上角来算
				if (i == loopStart)
					i += 0.5;

				// 根据X得到直线上的Y值
				double yPos = getLineFunc(point1 , point2, 0, i);
				if(yPos == -1)
					return true;
				// 检查经过的节点是否有障碍物，若有返回true
				passedNodeList = getNodesUnderPoint(i, yPos, null);

				for (Point passedNode : passedNodeList) {
					if (blocked(mover, passedNode.x,
							passedNode.y))
						return true;
				}

				if (i == loopStart + 0.5)
					i -= 0.5;
			}
		}
		else {
			loopStart = Math.min(startY, endY);
			loopEnd = Math.max(startY, endY);

			// 开始纵向遍历起点与终点间的节点看是否存在障碍(不可移动点)
			for (i = loopStart; i <= loopEnd; i++) {
				if (i == loopStart)
					i += 0.5;

				// 根据Y得到直线上的X值
				double xPos = getLineFunc(point1, point2, 1, i);
				if(xPos == -1)
					return true;

				// 检查经过的节点是否有障碍物，若有返回true
				passedNodeList = getNodesUnderPoint(xPos, i, null);

				for (Point passedNode : passedNodeList) {
					if (blocked(mover, passedNode.x,
							passedNode.y))
						return true;
				}

				if (i == loopStart + 0.5)
					i -= 0.5;
			}
		}
		
		return false;
	}

	/**
	 * 得到一个点下的所有节点
	 * 
	 * @param xPos
	 *            点的横向位置
	 * @param yPos
	 *            点的纵向位置
	 * @param exception
	 *            例外格，若其值不为空，则在得到一个点下的所有节点后会排除这些例外格
	 * @return 共享此点的所有节点
	 * 
	 */
	public List<Point> getNodesUnderPoint(double xPos, double yPos,
			List<Point> exception) {
		List<Point> result = new ArrayList<Point>();
		boolean xIsInt = xPos % 1 == 0;
		boolean yIsInt = yPos % 1 == 0;

		// 点由四节点共享情况
		if (xIsInt && yIsInt) {
			result.add(new Point((int)(xPos - 1), (int)(yPos - 1)));
			result.add(new Point((int)xPos, (int)(yPos - 1)));
			result.add(new Point((int)(xPos - 1), (int)yPos));
			result.add(new Point((int)xPos, (int)yPos));
		}
		// 点由2节点共享情况
		// 点落在两节点左右临边上
		else if (xIsInt && !yIsInt) {
			result.add(new Point((int)(xPos - 1), (int)yPos));
			result.add(new Point((int)xPos, (int)yPos));
		}
		// 点落在两节点上下临边上
		else if (!xIsInt && yIsInt) {
			result.add(new Point((int)xPos, (int)(yPos - 1)));
			result.add(new Point((int)xPos, (int)yPos));
		}
		// 点由一节点独享情况
		else {
			result.add(new Point((int)xPos, (int)yPos));
		}

		// 在返回结果前检查结果中是否包含例外点，若包含则排除掉
		if (exception != null && exception.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				if (exception.contains(result.get(i))) {
					result.remove(i);
					i--;
				}
			}
		}

		return result;
	}

	/**
	 * 根据两点确定这两点连线的二元一次方程 y = ax + b或者 x = ay + b
	 * 
	 * @param ponit1
	 * @param point2
	 * @param type
	 *            指定返回函数的形式。为0则根据x值得到y，为1则根据y得到x
	 * 
	 * @return 由参数中两点确定的直线的二元一次函数
	 */
	public double getLineFunc(Point ponit1, Point point2, int type, double xy) {

		// 先考虑两点在一条垂直于坐标轴直线的情况，此时直线方程为 y = a 或者 x = a 的形式
		if (ponit1.x == point2.x) {
			if (type == 0) {
				//两点所确定直线垂直于y轴，不能根据x值得到y值
				return -1;
			}
			else if (type == 1) {
				return ponit1.x;

			}
		}
		else if (ponit1.y == point2.y) {
			if (type == 0) {

				return ponit1.y;

			}
			else if (type == 1) {
				//两点所确定直线垂直于y轴，不能根据x值得到y值
				return -1;
			}
		}

		// 当两点确定直线不垂直于坐标轴时直线方程设为 y = ax + b
		double a;

		// 根据
		// y1 = ax1 + b
		// y2 = ax2 + b
		// 上下两式相减消去b, 得到 a = ( y1 - y2 ) / ( x1 - x2 )
		a = (double)(ponit1.y- point2.y) / (double)(ponit1.x - point2.x);

		double b;

		// 将a的值代入任一方程式即可得到b
		b = ponit1.y+0.5 - a * (ponit1.x+0.5);

		// 把a,b值代入即可得到结果函数
		if (type == 0) {

			return a * xy + b;

		}
		else if (type == 1) {
			return (xy - b) / a;
		}

		return -1;

	}
	/**
	 * 得到附近的四个方向上的空闲点
	 * @param currX
	 * @param currY
	 * @param num 需要坐标点数量,等于result.length
	 * @return 不包括参数currX,currY坐标
	 */
	public ArrayList<Point> getAreaFourPoint(int currX, int currY, int num, ArrayList<Point> result){
		if(currX<0||currX>=WIDTH||currY<0||currY>=HEIGHT)
		{
			return result;
		}
		for (int x=-1;x<2;x+=1) {
			for (int y=-1;y<2;y+=1) {
				if ((x == 0) || (y == 0)) {
					continue;
				}
					
				int xp = x + currX;
				int yp = y + currY;
				
				if(xp == currX && yp == currY)
					continue;
					
				if(!blocked(new UnitMover(null,0, false), xp, yp)){
					if(result.size() < num ){
						Point p = new Point(xp, yp);
						if(!result.contains(p))
							result.add(p);
					}
					else{
						return result;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 得到附近的八个方向上的空闲点
	 * @param currX
	 * @param currY
	 * @param k 搜索圈数
	 * @param space 搜索点之间的空隙
	 * @return 不包括参数currX,currY坐标
	 */
	public ArrayList<Point> getAreaEightPoint(int currX, int currY, int k, Mover mover, int space, ArrayList<Point> result){
		if(currX<0||currX>=WIDTH||currY<0||currY>=HEIGHT)
		{
			return result;
		}
		int startX = k*(-space-1);
		int startY = k*(-space-1);
		int endX = k*(space+1);
		int endY =  k*(space+1);
		int xSpan = space+1;
		int ySpan =  space+1;
		for (int x = startX; x <= endX; x += xSpan){
			for (int y = startY; y <= endY; y += ySpan){
				if ((x == 0) && (y == 0)) {
					continue;
				}
				if(Math.abs(x) != endX && Math.abs(y) != endY){
					continue;
				}
				int xp = x + currX;
				int yp = y + currY;
				
				if(xp == currX && yp == currY)
					continue;
					
				if(!blocked(mover, xp, yp)){
					Point p = new Point(xp, yp);
					if(!result.contains(p))
						result.add(p);
				}
			}
		}
		return result;
	}
	
	/**
	 * 寻找附近指定数量的空闲点
	 * @param currX
	 * @param currY
	 * @param num 需要坐标点数量,包括currX,currY
	 * @return 包括参数x,y坐标
	 */
	public ArrayList<Point> findAreaEmptyPoint(int currX, int currY, List<String> units, int num, int maxGridNum){
		int sX = currX;
		int sY = currY;
		boolean isBlock = false;//阻挡
		ArrayList<Point> result = new ArrayList<Point>();
		UnitMover mover = new UnitMover(null, units, maxGridNum, false);
		//如果该点无阻挡
		if(!blocked(new UnitMover(null, maxGridNum, false), currX, currY)){
			
			result.add(new Point(currX, currY));
		}
		else{
			isBlock = true;
			if(getUnit(currX, currY) != null){
				return result;
			}
		}
		int len = 1;
		while(result.size() < num){
			if(isBlock){
				getAreaEightPoint(currX, currY, len, mover, 0, result);
			}
			else{
				int space = (int)Math.sqrt(maxGridNum);
				getAreaEightPoint(currX, currY, len, mover, space, result);
			}
			if(result.size() > num){
				Collections.sort(result, new LatelyComparator(sX, sY));
				for(int i = result.size()-1; i >= num; i --){
					result.remove(i);
				}
			}
			len ++;
			if(len >= 100)
				break;
		}
		return result;
	}
	/**
	 * 寻找附近指定数量的空闲点
	 * @param currX
	 * @param currY
	 * @param num 需要坐标点数量,包括currX,currY
	 * @return 包括参数x,y坐标
	 */
	public ArrayList<Point> findAreaEmptyPoint(int currX, int currY, int num, int maxGridNum){
		int sX = currX;
		int sY = currY;
		boolean isBlock = false;//阻挡
		ArrayList<Point> result = new ArrayList<Point>();
		UnitMover mover = new UnitMover(null, maxGridNum, false);
		//如果该点无阻挡
		if(!blocked(new UnitMover(null, maxGridNum, false), currX, currY)){
			
			result.add(new Point(currX, currY));
		}
		else{
			isBlock = true;
		}
		int len = 1;
		while(result.size() < num){
			if(isBlock){
				getAreaEightPoint(currX, currY, len, mover, 0, result);
			}
			else{
				int space = (int)Math.sqrt(maxGridNum);
				getAreaEightPoint(currX, currY, len, mover, space, result);
			}
			if(result.size() > num){
				Collections.sort(result, new LatelyComparator(sX, sY));
				for(int i = result.size()-1; i >= num; i --){
					result.remove(i);
				}
			}
			len ++;
			if(len >= 100)
				break;
		}
		return result;
	}
	
	/**
	 * 格子坐标转换成世界坐标
	 * @param x
	 * @param z
	 * @return
	 */
	public Step gridToWorld(float x, float y){
		Step step = new Step();
		step.setX(x*this.gridTitleX+this.gridTitleX/2f);
		step.setY(y*this.gridTitleY+this.gridTitleY/2f);
		return step;
	}
	
	/**
	 * 世界坐标转换成格子坐标
	 * @param x
	 * @param y
	 * @return
	 */
	public Point worldToGrid(float x, float y){
		Point p = new Point();
		p.x = (int)(x/this.gridTitleX);
		p.y = (int)(y/this.gridTitleY);
		return p;
	}
	
	/**
	 * 弗洛伊德路径平滑处理
	 * 
	 * @return
	 */
	public void floyd(Path path, Mover mover) {
		if (path == null)
			return;
		ArrayList<Step> smoothPaths = new ArrayList<Step>();
		smoothPaths.addAll(path.getSteps());
		int len = smoothPaths.size();
		if (len > 2) {
			Point vector = new Point(0, 0);
			Point tempVector = new Point(0, 0);

			// 遍历路径数组中全部路径节点，合并在同一直线上的路径节点
			// 假设有1，2，3三点，若2与1的横、纵坐标差值分别与3与2的横、纵坐标差值相等则
			// 判断此三点共线，此时可以删除中间点2
			floydVector(vector, smoothPaths.get(len - 1), smoothPaths.get(len - 2));
			for (int i = smoothPaths.size() - 3; i >= 0; i--) {
				floydVector(tempVector, smoothPaths.get(i + 1), smoothPaths.get(i));
				if (vector.x == tempVector.x && vector.y == tempVector.y) {
					smoothPaths.remove(i + 1);
				}
				else {
					vector.x = tempVector.x;
					vector.y = tempVector.y;
				}
			}
		}

		// 合并共线节点后进行第二步，消除拐点操作。算法流程如下：
		// 如果一个路径由1-10十个节点组成，那么由节点10从1开始检查
		// 节点间是否存在障碍物，若它们之前不存在障碍物，则直接合并
		// 此两路径节点间所有节点
		len = smoothPaths.size();
		for (int i = len - 1; i >= 0; i--) {
			for (int j = 0; j <= i - 2; j++) {
				Step step1 = smoothPaths.get(i);
				Step step2 = smoothPaths.get(j);
				if (!hasBarrier((int) step1.getX(), (int) step1.getY(), (int) step2.getX(), (int) step2.getY(),
						mover)) {
					for (int k = i - 1; k > j; k--) {
						smoothPaths.remove(k);
					}
					i = j;
					len = smoothPaths.size();
					break;
				}
			}
		}
		path.setSmoothPaths(smoothPaths);
	}
	
	protected void floydVector(Point target, Step n1, Step n2) {
		target.x = (int) (n1.getX() - n2.getX());
		target.y = (int) (n1.getY() - n2.getY());
	}
}
