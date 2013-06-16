package skeleton2D;

import java.util.ArrayList;
import java.util.Stack;

import primitives.Bone2D;
import primitives.Line;
import primitives.Point;
import primitives.Segment;
import primitives.Vector2D;

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
				//if(s.getLeft().equals(s.getRight())) continue;
				if(s.getLeft().equals(current.getJoint())){
					makeBone(current, s.getRight());
					toRemove.add(s);
				}else if(s.getRight().equals(current.getJoint())){
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
	
	private void makeBone(Bone2D parent, Point p){
		double a;
		double l;
		
		l = parent.getJoint().dist(p);
		
		//a = (new Line(parent.getJoint(), p).getAngle()) - parent.getA();
		if(parent.getParent()!=null){
			Vector2D v1 = new Vector2D(parent.getParent().getJoint(),
					parent.getJoint());
			Vector2D v2 = new Vector2D(parent.getJoint(), p);
			a = v1.angleBetween(v2);
			int sign = v1.cross(v2)< 0 ? -1 : 1;
			a = a*sign;
		}else{
			//a = (new Line(parent.getJoint(), p).getAngle()) - parent.getA();
			Vector2D v1 = new Vector2D(new Point(0,0), new Point(100, 0));
			Vector2D v2 = new Vector2D(parent.getJoint(), p);
			a = v1.angleBetween(v2);
			int sign = v1.cross(v2)< 0 ? -1 : 1;
			a = a*sign;
		}
		
		
		
		//child
		Bone2D child = new Bone2D(p.getX(), p.getY(), a, l, parent);
		bonesStack.add(child);
		
		//parent
		parent.addChild(child);

	}
	
	public void printBones(Bone2D bone, int level){
		for(int i = 0;i<level;i++){
			System.out.print("#");
		}
		
		//System.out.println(" "+bone.getX()+" "+bone.getX()+" "+bone.getA()+" "+
				//bone.getL());
		System.out.println(" "+0+" "+0+" "+bone.getA()+" "+bone.getL()+" "+Math.toDegrees(bone.getA()));
				
		for(Bone2D child : bone.getChild()){
			printBones(child, level+1);
		}
	}
	public Bone2D getRoot(){
		return this.rootBS;
	}
}
