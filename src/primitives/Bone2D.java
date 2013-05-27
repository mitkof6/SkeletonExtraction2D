package primitives;

import java.util.ArrayList;

public class Bone2D {
	
	private double x, y, a, l;	//coordinates x, y
								//a angle in radius
								//l length of the bone
	private ArrayList<Bone2D> child;
	private Bone2D parent;
	
	public Bone2D(double x, double y, double a, double l, Bone2D parent){
		this.x = x;
		this.y = y;
		this.a = a;
		this.l = l;
		this.parent = parent;
		
		child = new ArrayList<>();
	}
	
	public void addChild(Bone2D child){
		this.child.add(child);
	}
	
	public Point getPoint(){
		return new Point(x, y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getA() {
		return a;
	}

	public double getL() {
		return l;
	}

	public ArrayList<Bone2D> getChild() {
		return child;
	}

	public Bone2D getParent() {
		return parent;
	}
	
	
}
