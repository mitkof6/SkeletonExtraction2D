package primitives;

import java.util.ArrayList;

import primitives.Point;

/**
 * Finds the pair of closest points using different algorithms.
 * 
 */
public class ClosestPair {
    
    /**
     * Finds the pair of closest points using a naive method in O(n^2).
     */
    public static ArrayList<Point> Naive(ArrayList<Point> points) {
        Point p0 = points.get(0);
        Point p1 = points.get(1);
        
        for (int i = 0; i < points.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if(points.get(i).dist(points.get(j)) < p0.dist(p1)) {
                    p0 = points.get(i);
                    p1 = points.get(j);
                }
            } 
        }
        
        ArrayList<Point> resultPair = new ArrayList<Point>();
        resultPair.add(p0);
        resultPair.add(p1);
        
        return resultPair;
    }
}