package skeleton2D;

import java.util.ArrayList;
import java.util.Stack;

import primitives.Bone2D;
import primitives.Line;
import primitives.Point;
import primitives.Segment;

public class BoneSystem {

	private ArrayList<Segment> segments;
	private Bone2D rootBS;
	private Stack<Bone2D> bonesStack;
	
	public BoneSystem(Point root, ArrayList<Segment> segments){
		this.segments = new ArrayList<>();
		for(Segment s: segments){
			this.segments.add(new Segment(s.getLeft(), s.getRight()));
		}
		
		bonesStack = new Stack<>();
		rootBS = new Bone2D(root.getX(), root.getY(), 0, 0, null);
	}
	
	public void generateBoneSystem(){
		Bone2D current = rootBS;
		Stack<Segment> toRemove = new Stack<>();
		while(segments.size()!=0){
			for(Segment s : segments){
				if(s.getLeft().equals(current.getPoint())){
					makeBone(current, s.getRight());
					toRemove.add(s);
				}else if(s.getRight().equals(current.getPoint())){
					makeBone(current, s.getLeft());
					toRemove.push(s);
				}
			}
			while(!toRemove.empty()){
				segments.remove(toRemove.pop());
			}
			current = bonesStack.pop();
		}
		
	}
	
	private void makeBone(Bone2D parent, Point p){
		double a;
		double l;
		
		l = parent.getPoint().dist(p);
		
		a = new Line(parent.getPoint(), p).getAngle();
		
		//child
		Bone2D child = new Bone2D(p.getX(), p.getY(), a, l, parent);
		bonesStack.add(child);
		
		//parent
		parent.addChild(child);

	}
	
	public Bone2D getRoot(){
		return this.rootBS;
	}
}
