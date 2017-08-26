package com.spring.common.pathfinding.astar;

import java.util.ArrayList;
import java.util.Collections;

import com.spring.common.pathfinding.astar.heuristics.ClosestHeuristic;

/**
 * A path finder implementation that uses the AStar heuristic based algorithm
 * to determine a path. 
 * 
 * @author Kevin Glass
 */
public class AStarPathFinder implements PathFinder {
	/** The set of nodes that have been searched through */
	private ArrayList closed = new ArrayList();
	/** The set of nodes that we do not yet consider fully searched */
	private Bintree open;
	
	/** The map being searched */
	private TileBasedMap map;
	/** The maximum depth of search we're willing to accept before giving up */
	private int maxSearchDistance;
	
	/** The complete set of nodes across the map */
	private Node[][] nodes;
	/** True if we allow diaganol movement */
	private boolean allowDiagMovement;
	/** The heuristic we're applying to determine which nodes to search first */
	private AStarHeuristic heuristic;
	
	/**
	 * Create a path finder with the default heuristic - closest to target.
	 * 
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
		this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
	}

	/**
	 * Create a path finder 
	 * 
	 * @param heuristic The heuristic used to determine the search order of the map
	 * @param map The map to be searched
	 * @param maxSearchDistance The maximum depth we'll search before giving up
	 * @param allowDiagMovement True if the search should try diaganol movement
	 */
	public AStarPathFinder(TileBasedMap map, int maxSearchDistance, 
						   boolean allowDiagMovement, AStarHeuristic heuristic) {
		this.heuristic = heuristic;
		this.map = map;
		this.maxSearchDistance = maxSearchDistance;
		this.allowDiagMovement = allowDiagMovement;
		int width = map.getWidthInTiles();
		int height = map.getHeightInTiles();
		this.open = new Bintree(width*height);
		nodes = new Node[width][height];
		for (int x=0;x<width;x++) {
			for (int y=0;y<height;y++) {
				nodes[x][y] = new Node(x,y);
			}
		}
	}
	
	/**
	 * @see PathFinder#findPath(Mover, int, int, int, int)
	 */
	public Path findPath(Mover mover, int sx, int sy, int tx, int ty) {
		
//		for(int i = 0; i < nodes.length; i ++){
//			for(int j = 0; j < nodes[i].length; j++){
//				nodes[i][j].reset();
//			}
//		}
		
		// easy first check, if the destination is blocked, we can't get there
		if (map.blocked(mover, tx, ty)) {
			return null;
		}
		
		// initial state for A*. The closed group is empty. Only the starting
		// tile is in the open list and it's cost is zero, i.e. we're already there
		nodes[sx][sy].cost = 0;
		nodes[sx][sy].depth = 0;
		closed.clear();
		open.clear();
		open.add(nodes[sx][sy]);
		
		nodes[tx][ty].parent = null;
		
		// while we haven't found the goal and haven't exceeded our max search depth
		int maxDepth = 0;
		while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
			// pull out the first node in our open list, this is determined to 
			// be the most likely to be the next step based on our heuristic
			Node current = getFirstInOpen();
			if (current == nodes[tx][ty]) {
				break;
			}
			
			removeFromOpen(current);
			addToClosed(current);
			
			
			// search through all the neighbours of the current node evaluating
			// them as next steps
			for (int x=-1;x<2;x++) {
				for (int y=-1;y<2;y++) {
					// not a neighbour, its the current tile
					if ((x == 0) && (y == 0)) {
						continue;
					}
					
					// if we're not allowing diaganol movement then only 
					// one of x or y can be set
					if (!allowDiagMovement) {
						if ((x != 0) && (y != 0)) {
							continue;
						}
					}
					
					// determine the location of the neighbour and evaluate it
					int xp = x + current.x;
					int yp = y + current.y;
					
					
					if (isValidLocation(mover,sx,sy,xp,yp)) {
						// the cost to get to this node is cost the current plus the movement
						// cost to reach this node. Note that the heursitic value is only used
						// in the sorted open list
						float nextStepCost = current.cost + getMovementCost(mover, current.x, current.y, xp, yp);
						Node neighbour = nodes[xp][yp];
						map.pathFinderVisited(xp, yp);
						
						// if the new cost we've determined for this node is lower than 
						// it has been previously makes sure the node hasn't been discarded. We've
						// determined that there might have been a better path to get to
						// this node so it needs to be re-evaluated
						if (nextStepCost < neighbour.cost) {
							if (inOpenList(neighbour)) {
								removeFromOpen(neighbour);
							}
							if (inClosedList(neighbour)) {
								removeFromClosed(neighbour);
							}
						}
						
						// if the node hasn't already been processed and discarded then
						// reset it's cost to our current cost and add it as a next possible
						// step (i.e. to the open list)
						if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
							neighbour.cost = nextStepCost;
							neighbour.heuristic = getHeuristicCost(mover, xp, yp, tx, ty);
							maxDepth = Math.max(maxDepth, neighbour.setParent(current));
							addToOpen(neighbour);
						}
					}
				}
			}
		}

		
		// since we've got an empty open list or we've run out of search 
		// there was no path. Just return null
//		if (nodes[tx][ty].parent == null) {
//			return null;
//		}
		
		// At this point we've definitely found a path so we can uses the parent
		// references of the nodes to find out way from the target location back
		// to the start recording the nodes on the way.
//		Path path = new Path();
//		Node target = nodes[tx][ty];
//		while (target != nodes[sx][sy]) {
//			path.prependStep(target.x, target.y);
//			target = target.parent;
//		}
//		path.prependStep(sx,sy);
		
		Path path = new Path();
		if(nodes[tx][ty].parent != null){
			Node target = nodes[tx][ty];
			while (target != nodes[sx][sy]) {
				path.prependStep(target.x, target.y);
				target = target.parent;
			}
			path.prependStep(sx,sy);
		}
		else{
			if(open.size() == 0)
				return null;
			Node target = (Node)open.first();
			if(target == nodes[sx][sy])
				return null;
			while (target != nodes[sx][sy]) {
				path.prependStep(target.x, target.y);
				target = target.parent;
			}
			path.prependStep(sx,sy);
		}
		
		// thats it, we have our path 
		return path;
	}

	/**
	 * Get the first element from the open list. This is the next
	 * one to be searched.
	 * 
	 * @return The first element in the open list
	 */
	protected Node getFirstInOpen() {
		return (Node) open.first();
	}
	
	/**
	 * Add a node to the open list
	 * 
	 * @param node The node to be added to the open list
	 */
	protected void addToOpen(Node node) {
		node.inOpen = true;
		open.add(node);
	}
	
	/**
	 * Check if a node is in the open list
	 * 
	 * @param node The node to check for
	 * @return True if the node given is in the open list
	 */
	protected boolean inOpenList(Node node) {
//		return open.contains(node);
		return node.inOpen;
	}
	
	/**
	 * Remove a node from the open list
	 * 
	 * @param node The node to remove from the open list
	 */
	protected void removeFromOpen(Node node) {
		node.inOpen = false;
		open.remove(node);
	}
	
	/**
	 * Add a node to the closed list
	 * 
	 * @param node The node to add to the closed list
	 */
	protected void addToClosed(Node node) {
		node.inClose = true;
		closed.add(node);
	}
	
	/**
	 * Check if the node supplied is in the closed list
	 * 
	 * @param node The node to search for
	 * @return True if the node specified is in the closed list
	 */
	protected boolean inClosedList(Node node) {
//		return closed.contains(node);
		return node.inClose;
	}
	
	/**
	 * Remove a node from the closed list
	 * 
	 * @param node The node to remove from the closed list
	 */
	protected void removeFromClosed(Node node) {
		node.inClose = false;
		closed.remove(node);
	}
	
	/**
	 * Check if a given location is valid for the supplied mover
	 * 
	 * @param mover The mover that would hold a given location
	 * @param sx The starting x coordinate
	 * @param sy The starting y coordinate
	 * @param x The x coordinate of the location to check
	 * @param y The y coordinate of the location to check
	 * @return True if the location is valid for the given mover
	 */
	protected boolean isValidLocation(Mover mover, int sx, int sy, int x, int y) {
		boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
		
		if ((!invalid) && ((sx != x) || (sy != y))) {
			invalid = map.blocked(mover, x, y);
		}
		
		return !invalid;
	}
	
	/**
	 * Get the cost to move through a given location
	 * 
	 * @param mover The entity that is being moved
	 * @param sx The x coordinate of the tile whose cost is being determined
	 * @param sy The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The cost of movement through the given tile
	 */
	public float getMovementCost(Mover mover, int sx, int sy, int tx, int ty) {
		return map.getCost(mover, sx, sy, tx, ty);
	}

	/**
	 * Get the heuristic cost for the given location. This determines in which 
	 * order the locations are processed.
	 * 
	 * @param mover The entity that is being moved
	 * @param x The x coordinate of the tile whose cost is being determined
	 * @param y The y coordiante of the tile whose cost is being determined
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The heuristic cost assigned to the tile
	 */
	public float getHeuristicCost(Mover mover, int x, int y, int tx, int ty) {
		return heuristic.getCost(map, mover, x, y, tx, ty);
	}
	
	/**
	 * 二叉堆
	 * 从形式上看，它从顶点开始，每个节点有两个子节点，每个子节点又各自有自己的两个子节点；
	 * 数值上看，每个节点的两个子节点都比它大或和它相等。
	 *         10
	 *     30      20
	 *  34   38 30    24
	 * 
	 * 在二叉堆里我们要求：
	 * 1.最小的元素在顶端
	 * 2.每个元素都比它的父节点大，或者和父节点相等
	 * 
	 * 只要满足这两个条件，其它的元素怎么排都行。如上面的例子，最小的元素10在最顶端，
	 * 第二小的元素20在10的下面，但是第三小的元素24在20的下面，也就是第三层，更大的
	 * 30反而在第二层。
	 * 这样的堆，我们可以用一个简单的一维数组来存储，如下：
	 * 10 30 20 34 38 30 24
	 * 
	 * 假设一个元素的位置是n(第一个元素的位置为1，而不是通常数组的第一个索引0)，那么
	 * 它的两个子节点分别是 n * 2 和 n * 2 + 1, 父节点是 n/2取整。比如第3个元素(例中是20)
	 * 的两个子节点位置是6和7，父节点位置是1
	 * @author zenggy
	 */
	private class Bintree {
		/** The list of elements */
		private Node[] list;
		private int size;
		
		public Bintree(int length){
			list = new Node[length];
		}
		
		/**
		 * Retrieve the first element from the list
		 *  
		 * @return The first element from the list
		 */
		public Node first() {
			if(size > 0)
				return list[0];
			return null;
		}
		
		/**
		 * Empty the list
		 */
		public void clear() {
			list = new Node[list.length];
			size = 0;
		}
		
		/**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o The element to add
		 */
		public void add(Node o) {
			//首先把要添加的元素加到数组的末尾，然后和它的父节点（位置为当前位置除以2取整，
			//比如第4个元素的父节点位置是2，第7个元素的父节点位置是3）比较，如果新元素比父
			//节点元素小则交换这两个元素，然后再和新位置的父节点比较，直到它的父节点不再比它大，
			//或者已经到达顶端，及第1位置
			
			
			list[size] = o;
			size ++;
			
			//新元素的位置
			int position = size;
			o.position = position;
			//父元素的位置
			int parentIdx = position/2;
			while(position > 1){
				Node parent = list[parentIdx-1];
				if(o.compareTo(parent) == -1){
					list[parentIdx-1] = o;
					list[position-1] = parent;
					
					o.position = parentIdx;
					parent.position = position;
					
					position = parentIdx;
					parentIdx = position/2;
				}
				else{
					break;
				}
			}
			
		}
		
		/**
		 * Remove an element from the list
		 * 
		 * @param o The element to remove
		 */
		public void remove(Node o) {
			//从堆中删除元素和添加元素是个类似的过程，但差不多是反过来的。
			//首先，我们删除位置1的元素，现在它空了，然后，我们取堆的最
			//一个元素，移动到位置1，然后我们比较它和两个子节点，它们分别
			//在位置（当前位置*2）和（当前位置*2+1）。如果它比两个子节点
			//都小，就保持原位。反之，就把它和较低的子节点交换位置，以此类推
			//如果子节点的位置不存在就停止
			Node n = (Node)list[size-1];
			
			list[o.position-1] = n;
			list[size-1] = null;
			size --;
			n.position = o.position;
			int position = n.position;
			int childIdx1 = position*2;
			int childIdx2 = position*2+1;
			while(childIdx1 <= size){
				if(childIdx2 <= size){
					Node child1 = list[childIdx1-1];
					Node child2 = list[childIdx2-1];
					if(n.compareTo(child1) != 1 && n.compareTo(child2) != 1){
						break;
					}
					else {
						if(child1.compareTo(child2) == 1){
							list[childIdx2-1] = n;
							list[position-1] = child2;
							
							n.position = childIdx2;
							child2.position = position;
							
							position = n.position;
							
							childIdx1 = position*2;
							childIdx2 = position*2+1;
						}
						else{
							list[childIdx1-1] = n;
							list[position-1] = child1;
							
							n.position = childIdx1;
							child1.position = position;
							
							position = n.position;
							
							childIdx1 = position*2;
							childIdx2 = position*2+1;
						}
					}
				}
				else{
					Node child1 = list[childIdx1-1];
					if(n.compareTo(child1) != 1){
						break;
					}
					else {
						
						list[childIdx1-1] = n;
						list[position-1] = child1;
							
						n.position = childIdx1;
						child1.position = position;
							
						position = n.position;
							
						childIdx1 = position*2;
						childIdx2 = position*2+1;
						
					}
				}
			}
			
		}
	
		/**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
 		 */
		public int size() {
			return this.size;
		}
		
	}
	
	/**
	 * A simple sorted list
	 *
	 * @author kevin
	 */
	private class SortedList {
		/** The list of elements */
		private ArrayList list = new ArrayList();
		
		/**
		 * Retrieve the first element from the list
		 *  
		 * @return The first element from the list
		 */
		public Object first() {
			return list.get(0);
		}
		
		/**
		 * Empty the list
		 */
		public void clear() {
			list.clear();
		}
		
		/**
		 * Add an element to the list - causes sorting
		 * 
		 * @param o The element to add
		 */
		public void add(Object o) {
			list.add(o);
			Collections.sort(list);
		}
		
		/**
		 * Remove an element from the list
		 * 
		 * @param o The element to remove
		 */
		public void remove(Object o) {
			list.remove(o);
		}
	
		/**
		 * Get the number of elements in the list
		 * 
		 * @return The number of element in the list
 		 */
		public int size() {
			return list.size();
		}
		
		/**
		 * Check if an element is in the list
		 * 
		 * @param o The element to search for
		 * @return True if the element is in the list
		 */
		public boolean contains(Object o) {
			return list.contains(o);
		}
	}
	
	/**
	 * A single node in the search graph
	 */
	private class Node implements Comparable<Node> {
		/** The x coordinate of the node */
		private int x;
		/** The y coordinate of the node */
		private int y;
		/** The path cost for this node */
		private float cost;
		/** The parent of this node, how we reached it in the search */
		private Node parent;
		/** The heuristic cost of this node */
		private float heuristic;
		/** The search depth of this node */
		private int depth;
		private boolean inOpen = false;
		private boolean inClose = false;
		/** 在二叉堆里的位置**/
		private int position;
		/**
		 * Create a new node
		 * 
		 * @param x The x coordinate of the node
		 * @param y The y coordinate of the node
		 */
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		/**
		 * Set the parent of this node
		 * 
		 * @param parent The parent node which lead us to this node
		 * @return The depth we have no reached in searching
		 */
		public int setParent(Node parent) {
			depth = parent.depth + 1;
			this.parent = parent;
			
			return depth;
		}
		
		/**
		 * @see Comparable#compareTo(Object)
		 */
		public int compareTo(Node other) {
//			Node o = (Node) other;
			if(other == null){
				int s = 0;
			}
			float f = heuristic + cost;
			float of = other.heuristic + other.cost;
			
			if (f < of) {
				return -1;
			} else if (f > of) {
				return 1;
			} else {
				return 0;
			}
		}
		
		public void reset() {
			parent = null;
			heuristic = cost = depth = 0;
			inOpen = false;
			inClose = false;
			position = 0;
		}
	}
}
