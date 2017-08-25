package com.snail.webgame.engine.common.pathfinding.mesh;
import com.snail.webgame.engine.common.info.Point2D;

public class AlgorithmUtil {  

    public static void main(String[] args) {  
        AlgorithmUtil.GetIntersection(new Point2D(1, 2), new Point2D(1, 2),  
                new Point2D(1, 2), new Point2D(1, 2));  
        AlgorithmUtil.GetIntersection(new Point2D(1, 2), new Point2D(1, 2),  
                new Point2D(1, 4), new Point2D(1, 4));  
        AlgorithmUtil.GetIntersection(new Point2D(100, 1), new Point2D(100, 100),  
                new Point2D(100, 101), new Point2D(100, 400));  
        AlgorithmUtil.GetIntersection(new Point2D(5, 5), new Point2D(100, 100),  
                new Point2D(100, 5), new Point2D(5, 100));  
    }  

    /** 
     * 判断两条线是否相交	 a 线段1起点坐标 	 b 线段1终点坐标 		c 线段2起点坐标 	d 线段2终点坐标	 intersection 相交点坐标 
     * reutrn 交点
     */  

    public static Point2D GetIntersection(Point2D a, Point2D b, Point2D c, Point2D d) {  
    	Point2D intersection = new Point2D(0, 0);  

        if (Math.abs(b.z - a.z) + Math.abs(b.x - a.x) + Math.abs(d.z - c.z)  
                + Math.abs(d.x - c.x) == 0) {  
            if ((c.x - a.x) + (c.z - a.z) == 0) {  
                //System.out.println("ABCD是同一个点！");  
            } else {  
                //System.out.println("AB是一个点，CD是一个点，且AC不同！");  
            }  
            return null;  
        }  

        if (Math.abs(b.z - a.z) + Math.abs(b.x - a.x) == 0) {  
            if ((a.x - d.x) * (c.z - d.z) - (a.z - d.z) * (c.x - d.x) == 0) {  
               // System.out.println("A、B是一个点，且在CD线段上！");  
            } else {  
               // System.out.println("A、B是一个点，且不在CD线段上！");  
            }  
            return null;  
        }  
        if (Math.abs(d.z - c.z) + Math.abs(d.x - c.x) == 0) {  
            if ((d.x - b.x) * (a.z - b.z) - (d.z - b.z) * (a.x - b.x) == 0) {  
                //System.out.println("C、D是一个点，且在AB线段上！");  
            } else {  
                //System.out.println("C、D是一个点，且不在AB线段上！");  
            }  
            return null;  
        }  

        if ((b.z - a.z) * (c.x - d.x) - (b.x - a.x) * (c.z - d.z) == 0) {  
           // System.out.println("线段平行，无交点！");  
            return null;  
        }  

        intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.z - a.z) -   
                c.x * (b.x - a.x) * (c.z - d.z) + a.x * (b.z - a.z) * (c.x - d.x)) /   
                ((b.z - a.z) * (c.x - d.x) - (b.x - a.x) * (c.z - d.z));  
        intersection.z = ((b.z - a.z) * (c.z - d.z) * (c.x - a.x) - c.z  
                * (b.z - a.z) * (c.x - d.x) + a.z * (b.x - a.x) * (c.z - d.z))  
                / ((b.x - a.x) * (c.z - d.z) - (b.z - a.z) * (c.x - d.x));  

        if ((intersection.x - a.x) * (intersection.x - b.x) <= 0  
                && (intersection.x - c.x) * (intersection.x - d.x) <= 0  
                && (intersection.z - a.z) * (intersection.z - b.z) <= 0  
                && (intersection.z - c.z) * (intersection.z - d.z) <= 0) {  
              
            //System.out.println("线段相交于点(" + intersection.x + "," + intersection.z + ")！");  
            return intersection; // '相交  
        } else {  
            //System.out.println("线段相交于虚交点(" + intersection.x + "," + intersection.z + ")！");  
            return null; // '相交但不在线段上  
        }  
    }  
}  