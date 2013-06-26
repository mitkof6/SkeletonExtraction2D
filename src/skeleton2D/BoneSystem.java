package skeleton2D;

import java.util.ArrayList;
import java.util.Stack;

import primitives.Point;
import primitives.Segment;
import primitives.Vector2D;

/**
 * This class generate the hierarchy bone system
 * 
 * @author Jim Stanev
 */
public class BoneSystem {

	private ArrayList<Segment> segments;
	private Bone root;
	private Stack<Bone> bonesStack;
	private static int name = 1;
	
	/**
	 * Constructor
	 * 
	 * @param root the root point
	 * @param bones an array list of the bone segments
	 */
	public BoneSystem(Point root, ArrayList<Segment> bones){
		this.segments = new ArrayList<>();
		for(Segment s: bones){
			this.segments.add(new Segment(s.getLeft(), s.getRight()));
		}
		
		bonesStack = new Stack<>();
		this.root = new Bone(root.getX(), root.getY(), 0, 0, name, null);
		name++;
	}
	
	/**
	 * Generates the tree system. If the bones are not connected correct
	 * it may stuck in inf loop
	 */
	public void generateBoneSystem(){
		Bone current = root;
		Stack<Segment> toRemove = new Stack<>();
		while(segments.size()!=0){
			for(Segment s : segments){
				if(s.getLeft().equals(current.getInitialPosition())){
					makeBone(current, s.getRight());
					toRemove.add(s);
				}else if(s.getRight().equals(current.getInitialPosition())){
					makeBone(current, s.getLeft());
					toRemove.push(s);
				}
			}
			while(!toRemove.empty()){
				segments.remove(toRemove.pop());
			}
			if(!bonesStack.isEmpty()) current = bonesStack.pop();
			
		}
		
	}
	
	/**
	 * Makes a bone and calculates its relative parameters
	 * 
	 * @param parent the parent
	 * @param p the initial point
	 */
	private void makeBone(Bone parent, Point p){
		double a;
		double l;
		
		l = parent.getInitialPosition().dist(p);
		
		//a = (new Line(parent.getJoint(), p).getAngle()) - parent.getA();
		if(parent.getParent()!=null){
			Vector2D v1 = new Vector2D(parent.getParent().getInitialPosition(),
					parent.getInitialPosition());
			Vector2D v2 = new Vector2D(parent.getInitialPosition(), p);
			a = v1.angleBetween(v2);
			int sign = v1.cross(v2)< 0 ? -1 : 1;
			a = a*sign;
		}else{
			//a = (new Line(parent.getJoint(), p).getAngle()) - parent.getA();
			Vector2D v1 = new Vector2D(new Point(0,0), new Point(100, 0));
			Vector2D v2 = new Vector2D(parent.getInitialPosition(), p);
			a = v1.angleBetween(v2);
			int sign = v1.cross(v2)< 0 ? -1 : 1;
			a = a*sign;
		}
		
		
		
		//child
		Bone child = new Bone(p.getX(), p.getY(), a, l, name++, parent);
		bonesStack.add(child);
		
		//parent
		parent.addChild(child);

	}

	/**
	 * Gets the root of the bone system
	 * 
	 * @return the root
	 */
	public Bone getRoot(){
		return this.root;
	}
}
