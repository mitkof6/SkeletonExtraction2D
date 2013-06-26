package skeleton2D;

import java.util.ArrayList;

import mst.Edge;
import mst.EdgeWeightedGraph;
import mst.KruskalMST;

import primitives.Line;
import primitives.Point;
import primitives.Segment;
import primitives.SegmentsIntersect;
import primitives.Vector2D;
import primitives.Vertex;


/**
 * This algorithm is based on the paper Skeleton Extraction of 3D Objects with
 * Visible Repulsive Force by Fu-Che Wu, Wan-Chun Ma, Ping-Chou Liou, Rung-Huei
 * Liang and Ming Ouhyoung. Some changes are made for better results. This is
 * the 2D implementation.
 * 
 * @author Jim Stanev
 * @version 1.0
 */
public class Skeleton2D {

	private ArrayList<Point> joints, root;
	private ArrayList<Segment> bones;
	private int sampling, pushingFactor, iteration, distanceTolerance, rootTolerance;
	private double step;
	
	//prevent infinity constant
	private static final double CINF = 0.0000001;
	
	/**
	 * Constructor
	 * 
	 * @param step the step of moving toward local minimum
	 * @param sampling the sampling interval
	 * @param pushingFactor the pushing factor
	 * @param iteration the iteration index of vrf
	 * @param distanceTolerance the distance tolerance for clustering
	 * @param rootTolerance the root tolerance
	 */
	public Skeleton2D(double step, int sampling, int pushingFactor, int iteration,
			int distanceTolerance, int rootTolerance){
		
		this.step = step;
		this.sampling = sampling;
		this.pushingFactor = pushingFactor;
		this.iteration = iteration;
		this.distanceTolerance = distanceTolerance;
		this.rootTolerance = rootTolerance;

		joints = new ArrayList<Point>();
		bones = new ArrayList<Segment>();
		root = new ArrayList<Point>();
	}
	
	/**
	 * Computes the visible set of a given point, with sampling rays and
	 * intersection points
	 * 
	 * @param x the point to define
	 * @param edges edges of the polygon
	 * @return the visible set
	 */
	private ArrayList<Point> visibleSet(Point x, ArrayList<Segment> edges){
		
		ArrayList<Point> visibleSet = new ArrayList<Point>();
		ArrayList<Point> list = new ArrayList<Point>();
		Segment ray;
		Point p;
		
		for(int i = 0;i<36;i++){
			p = new Point(Integer.MAX_VALUE, 0);
			p = rotatePoint(p, i*10);
			ray = new Segment(x, p);
			
			for(Segment s : edges){
				
				if(SegmentsIntersect.two(ray, s)){
					
					Point intersection = Line.intersection(new Line(ray),
							new Line(s));
					//visibleSet.add(intersection);
					list.add(intersection);
					//break;
				}
				
			}
			/*
			//find minimum
			Point minimumPoint = list.get(0);
			for(int j = 1;j<list.size();j++){
				if(x.dist(minimumPoint)>x.dist(list.get(j))){
					minimumPoint = list.get(j);
				}
			}
			*/
			//find minimum
			Point minimumPoint = findMinimum(x, list);
			
			visibleSet.add(minimumPoint);
			list.clear();
		}
		return visibleSet;
	}
	
	/**
	 * VRF(x)~ = Σf(||vi-x||2)*(vi-x)~
	 * 
	 * @param x interior of surface S
	 * @param visibleSet the visible set
	 * @return the visible repulsive force
	 */
	private Vector2D vrf(Point x, ArrayList<Point> visibleSet){
		
		Double scalar;
		Vector2D result = new Vector2D(0, 0);
		Vector2D vector;
		
		for(Point v: visibleSet){
			scalar = f(norm2(v, x));
			vector = Vector2D.minus(new Vector2D(v), new Vector2D(x));
			vector.multiply(scalar);
			result = Vector2D.add(result, vector);
		}
		
		return result;
	}
	
	/**
	 * f(x) = 1/(x^2+c). The constant is added not to get inf. 
	 * 
	 * @param x input
	 * @return result
	 */
	private double f(double x){
    	return 1/(Math.pow(x, 2)+CINF);
    }
	
	/**
	 * Norm ||p1-p2||2
	 * 
	 * @param p1 point1
	 * @param p2 point2
	 * @return norm2
	 */
	private double norm2(Point p1, Point p2){
    	return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2)+
    			Math.pow(p1.getY()-p2.getY(), 2));
    }
	
	/**
	 * Finds the normal vector
	 * 
	 * @param vector input vector
	 * @return the normal vector
	 */
	private Vector2D normalize(Vector2D vector){
		
		double dDistance = vector.getLength();
		
        if (dDistance == 0) 
            return null;
        
        return new Vector2D(vector.i/dDistance, vector.j/dDistance);
	}
        
	/**
	 * Rotate point to left with respect to origin
	 * 
	 * @param point point to rotate
	 * @param dAngle angle in degree
	 * @return the new position of the point
	 */
	private Point rotatePoint(Point point, double dAngle){
		
		Vector2D vector = new Vector2D(point);
        vector.turnLeft(Math.toRadians(dAngle));
        
        return new Point(vector.i, vector.j);
	}
	
	/**
	 * Locates the local minimum of a seed point using the vrf(x), termination
	 * condition |vrf(x+1)|>|vrf(x)| (not working so iteration inserted) so 
	 * iteration factor inserted.
	 * 
	 * @param seed the initial position
	 * @param edges the edges of the polygon
	 * @return a set of continues points
	 */
	private ArrayList<Point> localeMinimum(Point seed, ArrayList<Segment> edges){
		
		ArrayList<Point> result = new ArrayList<Point>();
		Vector2D vrft, vrft_1, norm;
		Point seed_1;
		
		//visible repulsive force t
		vrft = vrf(seed, visibleSet(seed, edges));
		int i = 0;
		while(i<iteration){
			
			norm = normalize(vrft);
			norm.multiply(step);
			seed_1 = Vector2D.minus(new Vector2D(seed), norm).getPoint();
			
			//visible repulse force t+1
			vrft_1 = vrf(seed_1, visibleSet(seed_1, edges));
			
			/*
			//break condition
			if(vrft_1.getLength()>vrft.getLength()){
				break;
			}
			*/
			
			result.add(seed_1);
			seed = seed_1;
			vrft = vrft_1;
			i++;
		}
		
		return result;
	}
	
	/**
	 * Pushes the point interior to the surface S
	 * 
	 * @param p the initial point
	 * @param edges the edges of the polygon
	 * @return the new position of p
	 */
	private Point pushInside(Vertex p, ArrayList<Segment> edges){
		
		Vector2D n;
		
		//get normal vector
		n = Vector2D.add(p.getN1(), p.getN2());
		n.normalize();
		
		//multiply by pushing factor
		n.multiply(pushingFactor);
		Vector2D temp = new Vector2D(p.getPosition());
		
		return Vector2D.add(temp, n).getPoint();
	}
	
	
	/**
	 * Run the algorithm to generate bones and joints
	 * 
	 * @param vertices the vertexes of the polygon
	 * @param edges the edges of the polygon
	 */
	public void getVDS(ArrayList<Vertex> vertices, ArrayList<Segment> edges){
		
		ArrayList<Point> points;
		
		for(Vertex v : vertices){
			//get points
			points = localeMinimum(pushInside(v, edges), edges);
			//cluster
			cluster(points);
			
		}
		
		//join
		skeletonChainUnion();
	}
	
	/**
	 * Filter the points and make clear skeleton, using sampling and nearest 
	 * point search
	 * 
	 * @param points a list of all the point to local minimum
	 */
	private void cluster(ArrayList<Point> points){
		
		Point previous, nearest;
		int count = 0;
		previous = new Point(points.get(0).getX(), points.get(0).getY());
		
		for(int i = sampling;i<points.size();i=i+sampling){
			nearest = nearest(points.get(i));
			
			if(nearest.equals(points.get(i))){
				
				addPointTo(previous, joints);
				addPointTo(new Segment(previous, nearest),bones);
				previous = new Point(nearest.getX(), nearest.getY());
				count++;
			}else{
				
				if(!previous.equals(points.get(0))){
					
					addPointTo(previous, joints);
					addPointTo(new Segment(previous, nearest),bones);
					if(count>rootTolerance){
						addPointTo(nearest, root);
					}
				}
				return;
			}
		}
	}
	
	/**
	 * Adds the point if not in list
	 * @param <T>
	 * 
	 * @param p point to add
	 * @param pointList list to check
	 */
	private <T> void addPointTo(T p, ArrayList<T> pointList){
		
		for(T g: pointList){
			if(g.equals(p)){
				return;
				//break;
			}
		}
		pointList.add(p);
		
	}
	
	/**
	 * Makes skeleton chain union using closes point algorithm
	 * 
	 */
	private void skeletonChainUnion(){
		
		EdgeWeightedGraph G = new EdgeWeightedGraph(root.size());
		Point[] arrayRoot = new Point[root.size()];
		root.toArray(arrayRoot);
		
		for(int i = 0;i<arrayRoot.length;i++){
			for(int j = 0;j<arrayRoot.length;j++){
				if(arrayRoot[i].equals(arrayRoot[j])) continue;
				
				G.addEdge(new Edge(i, j, arrayRoot[i].dist(arrayRoot[j])));
			}
		}
		
		KruskalMST mst = new KruskalMST(G);
		
		for(Edge e: mst.edges()){
			addPointTo(new Segment(arrayRoot[e.v()], arrayRoot[e.w()]), bones);
		}
		
		//System.out.println(root.get(0).getX()+" "+root.get(0).getY());
		
	}
	
	/**
	 * Find the nearest point given a tolerance
	 * 
	 * @param p point
	 * @return the nearest point if exist, else the input point
	 */
	private Point nearest(Point p){
		
		ArrayList<Point> candidates = new ArrayList<>();
		
		for(Point point : joints){
			if(point.dist(p)<distanceTolerance){
				candidates.add(point);
			}
		}
		
		if(candidates.size()!=0){
			return findMinimum(p, candidates);
		}else{
			return p;
		}
	}
	
	/**
	 * Finds the closest point in the list
	 * 
	 * @param p the initial point
	 * @param list the list to search
	 * @return closest point
	 */
	private Point findMinimum(Point p, ArrayList<Point> list){
		Point minPoint;
		
		//find minimum
		minPoint = list.get(0);
		for(int i = 1;i<list.size();i++){
			if(p.dist(minPoint)>p.dist(list.get(i))){
				if(p.equals(list.get(i))) continue;
				minPoint = list.get(i);
			}
		}
		
		return minPoint;
	}

	/**
	 * Joints getter
	 * 
	 * @return the joints
	 */
	public ArrayList<Point> getJoints() {
		return joints;
	}

	/**
	 * Get root points
	 * 
	 * @return root point list
	 */
	public ArrayList<Point> getRoot() {
		return root;
	}
	
	/**
	 * Bones getter
	 * 
	 * @return the bones
	 */
	public ArrayList<Segment> getBones() {
		return bones;
	}
	
}
