package com.spring.common.pathfinding.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//import org.critterai.math.Vector3;
//import org.critterai.nav.AStarSearch;
//import org.critterai.nav.DistanceHeuristicType;
//import org.critterai.nav.MasterPath;
//import org.critterai.nav.SearchState;
//import org.critterai.nav.TriCell;
//import org.critterai.nav.TriNavMesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.common.info.Point2D;

public class LocationHelper {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	public final static int POOL_SIZE = 5;

//	private TriNavMesh mesh;
//
//	private ExecutorService pool;
//
//	public LocationHelper(TriNavMesh mesh) {
//		this(mesh, POOL_SIZE);
//	}
//
//	public LocationHelper(TriNavMesh mesh, int poolSize) {
//		if (mesh == null) {
//			throw new IllegalArgumentException("mesh can not be null!");
//		}
//		this.mesh = mesh;
//		if (poolSize < 1 || poolSize > 10) {
//			poolSize = POOL_SIZE;
//		}
//		pool = Executors.newFixedThreadPool(poolSize);
//	}
//
//	public Vector3 getClosetPoint(Vector3 v) {
//		try {
//			Vector3 vOut = pool.submit(new ClosestPointJob(v)).get();
//			return vOut;
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public Vector3 getClosetPoint(float a, float b, float c) {
//		return getClosetPoint(new Vector3(a, b, c));
//	}
//
//	public boolean isValidatePoint(Vector3 v) {
//		try {
//			return pool.submit(new ValidatePointJob(v)).get();
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	public boolean isValidatePoint(float a, float b, float c) {
//		return isValidatePoint(new Vector3(a, b, c));
//	}
//
//	/**
//	 * 抛出异常时会返回 null
//	 * 
//	 * @param start
//	 * @param goal
//	 * @return
//	 */
//	public List<Vector3> getPathPoint(Vector3 start, Vector3 goal) {
//		try {
//			Future<MasterPath> futureMasterPath = pool.submit(new SearchPathJob(start, goal));
//			MasterPath masterPath = futureMasterPath.get();
//			if(masterPath == null){
//				if(logger.isWarnEnabled()){
//					logger.warn("LocationHelper getPathPoint error: start=" + start.toString() + ", goal=" + goal.toString());
//				}
//				return null;
//			}
//			List<Vector3> pathPoints = pool.submit(new ParsePathJob(masterPath, start, goal)).get();
//			return pathPoints;
//		}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		catch (ExecutionException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private Future<TriCell> getClosetCell(Vector3 v) {
//		return pool.submit(new ClosetCellJob(v));
//	}
//
//	/**
//	 * 性能原因，参数请优先使用利用{@code #getClosetPoint(Vector3)}查出来的点
//	 * 
//	 * @param start
//	 * @param goal
//	 * @return
//	 */
//	public Future<MasterPath> searchPath(Vector3 start, Vector3 goal) {
//		return pool.submit(new SearchPathJob(start, goal));
//	}
//
//	/**
//	 * 将算法查出来的的路径解析成路点
//	 * 
//	 * @param masterPath
//	 * @param start
//	 * @param goal
//	 * @return
//	 */
//	public Future<List<Vector3>> parsePath(MasterPath masterPath, Vector3 start, Vector3 goal) {
//		return pool.submit(new ParsePathJob(masterPath, start, goal));
//	}
//
//	private final class ClosestPointJob implements Callable<Vector3> {
//
//		private Vector3 v;
//
//		public ClosestPointJob(Vector3 v) {
//			this.v = v;
//		}
//
//		@Override
//		public Vector3 call() throws Exception {
//			synchronized (mesh) {
//				Vector3 p = new Vector3();
//				TriCell cell = mesh.getClosestCell(v.x, v.y, v.z, false, p);
//
//				if (cell == null)
//					return null;
//				else
//					return p;
//			}
//		}
//	}
//
//	private final class ClosetCellJob implements Callable<TriCell> {
//
//		private Vector3 v;
//
//		public ClosetCellJob(Vector3 v) {
//			this.v = v;
//		}
//
//		@Override
//		public TriCell call() throws Exception {
//			synchronized (mesh) {
//				Vector3 p = new Vector3();
//				TriCell cell = null;
//				cell = mesh.getClosestCell(v.x, v.y, v.z, true, p);
//				return cell;
//			}
//
//		}
//	}
//
//	private final class ValidatePointJob implements Callable<Boolean> {
//
//		private Vector3 v;
//
//		public final static float tolerance = 1.5f;
//
//		public ValidatePointJob(Vector3 v) {
//			this.v = v;
//		}
//
//		@Override
//		public Boolean call() throws Exception {
//			synchronized (mesh) {
//				Vector3 p = new Vector3();
//				TriCell cell = mesh.getClosestCell(v.x, v.y, v.z, true, p);
//
//				if (cell == null)
//					return false;
//				else if (v.y < p.y - tolerance || v.y > p.y + tolerance)
//					return false;
//				else
//					return true;
//			}
//		}
//
//	}
//
//	private final class SearchPathJob implements Callable<MasterPath> {
//
//		private Vector3 start;
//		private Vector3 goal;
//
//		public final static float planeTolerance = 0.5f;
//		private final static float offsetScale = 0.5f;
//
//		public SearchPathJob(Vector3 start, Vector3 goal) {
//			this.start = start;
//			this.goal = goal;
//		}
//
//		@Override
//		public MasterPath call() throws Exception {
//			TriCell startCell, goalCell;
//			startCell = getClosetCell(start).get();
//			if (startCell == null) {
//				return null;
//			}
//			goalCell = getClosetCell(goal).get();
//			if (goalCell == null) {
//				return null;
//			}
//
//			// A-star寻路
//			AStarSearch astarSearch = new AStarSearch(DistanceHeuristicType.LONGEST_AXIS);
//			SearchState searchState = astarSearch.initialize(start.x, start.y, start.z, goal.x, goal.y, goal.z,
//					startCell, goalCell);
//			if (searchState != SearchState.INITIALIZED) {
//				return null;
//			}
//
//			searchState = astarSearch.process();
//			while (searchState != SearchState.COMPLETE) { // 找出所有的路径面片
//				if (searchState == SearchState.FAILED) {
//					break;
//				}
//				searchState = astarSearch.process();
//			}
//
//			TriCell[] pathCells = astarSearch.pathCells();
//			if(pathCells == null)
//				return null;
//			MasterPath masterPath = new MasterPath(0, pathCells, planeTolerance, offsetScale);
//
//			return masterPath;
//		}
//	}
//
//	private final class ParsePathJob implements Callable<List<Vector3>> {
//		private final float ADD_HIGHT = 0.1f;
//		private boolean isAdd = false;
//		private final float XZ = 1f;
//		private final float Y = 0.2f;
//
//		private MasterPath masterPath;
//		private Vector3 start;
//		private Vector3 goal;
//
//		public ParsePathJob(MasterPath masterPath, Vector3 start, Vector3 goal) {
//			this.masterPath = masterPath;
//			this.start = start;
//			this.goal = goal;
//		}
//
//		@Override
//		public List<Vector3> call() throws Exception {
//			MasterPath.Path path = masterPath.getPath(goal.x, goal.y, goal.z);
//			TriCell[] outCells = new TriCell[masterPath.size()];
//			masterPath.getRawCopy(outCells);
//			List<Vector3> resultPath = new ArrayList<Vector3>();
//
//			resultPath.add(start);
//			Vector3 v = start;
//			Vector3 vOut = new Vector3();
//			while (path.getTarget(v.x, v.y, v.z, vOut)) {
//				if (v.equals(vOut))
//					break;
//				resultPath.add(vOut);
//				v = vOut;
//				vOut = new Vector3();
//			}
//
//			List<Vector3> rets = new ArrayList<Vector3>();
//			for (int i = 0; i < resultPath.size() - 1; i++) {
////				rets.addAll(insert(path, resultPath.get(i), resultPath.get(i + 1)));
//				Vector3 v1 = resultPath.get(i);
//				Vector3 v2 = resultPath.get(i + 1);
//				rets.add(v1);
//				if(v1.y != v2.y){
//					List<Vector3> highInflexion = highInflexion(path, outCells, v1, v2);
//					if(highInflexion != null && !highInflexion.isEmpty()){
//						rets.addAll(highInflexion);
//					}
//				}
//			}
//			rets.add(resultPath.get(resultPath.size() - 1));
//
//			return rets;
//		}
//		/**
//		 * 求高度拐点
//		 */
//		private List<Vector3> highInflexion(MasterPath.Path path, TriCell[] cells, Vector3 v1, Vector3 v2){
//			List<Vector3> result = new ArrayList<Vector3>();
//			result.add(v1);
//			for(int i = 0; i < cells.length; i ++){
//				TriCell cell = cells[i];
//				Vector3 va = new Vector3();
//				Vector3 vb = new Vector3();
//				Vector3 vc = new Vector3();
//				
//				cell.getVertex(0, va);
//				cell.getVertex(1, vb);
//				cell.getVertex(2, vc);
//				
//				Point2D ab = AlgorithmUtil.GetIntersection(new Point2D(v1.x, v1.z), new Point2D(v2.x, v2.z), new Point2D(va.x, va.z), new Point2D(vb.x, vb.z));
//				Point2D bc = AlgorithmUtil.GetIntersection(new Point2D(v1.x, v1.z), new Point2D(v2.x, v2.z), new Point2D(vb.x, vb.z), new Point2D(vc.x, vc.z));
//				Point2D ca = AlgorithmUtil.GetIntersection(new Point2D(v1.x, v1.z), new Point2D(v2.x, v2.z), new Point2D(vc.x, vc.z), new Point2D(va.x, va.z));
//				
//				if(ab != null){
//					Vector3 tmp = snap(path, new Vector3(ab.x, 0, ab.z));
//					if(tmp != null && !tmp.equals(v1) && !tmp.equals(v2)){
//						result.add(tmp);
//					}
//				}
//				if(bc != null){
//					Vector3 tmp = snap(path, new Vector3(bc.x, 0, bc.z));
//					if(tmp != null && !tmp.equals(v1) && !tmp.equals(v2)){
//						result.add(tmp);
//					}
//				}
//				if(ca != null){
//					Vector3 tmp = snap(path, new Vector3(ca.x, 0, ca.z));
//					if(tmp != null && !tmp.equals(v1) && !tmp.equals(v2)){
//						result.add(tmp);
//					}
//				}
//			}
//			result.add(v2);
//			
//			List<Vector3> ps = new ArrayList<Vector3>();
//			for (int j = 1; j < result.size() - 1; j++) {
//				if (isChangePoint(result.get(j - 1), result.get(j), result.get(j + 1))) {
//					Vector3 v = result.get(j);
//					if (isAdd) {
//						ps.add(new Vector3(v.x, v.y + ADD_HIGHT, v.z));
//					}
//					else {
//						ps.add(v);
//					}
//				}
//			}
//			return ps;
//		}
//		private double distanceS(Vector3 v1, Vector3 v2) {
//			Vector3 v = new Vector3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
//			return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
//		}
//
//		/**
//		 * 返回列表中包含起始节点,不包含终点<br/>
//		 * 仅限于直线上的点
//		 * @param start
//		 * @param end
//		 * @return
//		 */
//		private List<Vector3> insert(MasterPath.Path path, Vector3 start, Vector3 end) {
//			Vector3 direction = new Vector3(end.x - start.x, end.y - start.y, end.z - start.z).normalize();
//
//			final Vector3 step = mutil(direction, XZ);
//			final int dFlag = direction.x >= 0 ? 1 : -1;
//
//			List<Vector3> ret = new ArrayList<Vector3>();
//			Vector3 v = start;
//			while ((getDirectionFlag(v, end) * dFlag >= 0) && distanceS(v, end) >= XZ) {  //必须在一个方向
//				ret.add(v);
//				v = snap(path, addV(v, step));
//				if (v == null) {
//					break;
//				}
//			}
//			ret.add(end);
//			List<Vector3> ps = new ArrayList<Vector3>();
//			ps.add(start);
//			for (int j = 1; j < ret.size() - 1; j++) {
//				if (isChangePoint(ret.get(j - 1), ret.get(j), ret.get(j + 1))) {
//					v = ret.get(j);
//					if (isAdd) {
//						ps.add(new Vector3(v.x, v.y + ADD_HIGHT, v.z));
//					}
//					else {
//						ps.add(v);
//					}
//				}
//			}
//			return ps;
//		}
//
//		/**
//		 * 仅限于直线上的点
//		 * @param v1
//		 * @param v2
//		 * @return
//		 */
//		private int getDirectionFlag(Vector3 v1, Vector3 v2) {
//			return v2.x - v1.x >= 0 ? 1 : -1;
//		}
//
//		private boolean isChangePoint(Vector3 v1, Vector3 v2, Vector3 v3) {
//			return (Math.abs(v1.y - v2.y) <= Y && Math.abs(v2.y - v3.y) >= Y)
//					|| (Math.abs(v1.y - v2.y) >= Y && Math.abs(v2.y - v3.y) <= Y);
//		}
//
//		private Vector3 snap(MasterPath.Path path, Vector3 v) {
//			Vector3 vOut = new Vector3();
//			if (path.forceYToPath(v.x, v.y, v.z, vOut)) {
//				return vOut;
//			}
//			else
//				return null;
//		}
//
//		private Vector3 mutil(Vector3 v, float m) {
//			return new Vector3(v.x * m, v.y * m, v.z * m);
//		}
//
//		private Vector3 addV(Vector3 v1, Vector3 v2) {
//			return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
//		}
//	}
}
