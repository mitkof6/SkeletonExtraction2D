package primitives;

import java.util.ArrayList;

import animation.SkinBoneBinding;

public class Vertex{

	private Vector2D n1, n2;
	private ArrayList<SkinBoneBinding> attached = new ArrayList<>();
	private double x, y;
	
	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D getN1() {
		return n1;
	}

	public void setN1(Vector2D n1) {
		this.n1 = n1;
	}

	public Vector2D getN2() {
		return n2;
	}

	public void setN2(Vector2D n2) {
		this.n2 = n2;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Point getPosition(){
		return new Point(this.x, this.y);
	}

	public ArrayList<SkinBoneBinding> getAttached() {
		return attached;
	}
	
	public void addBoneSkinBinding(SkinBoneBinding binding){
		this.attached.add(binding);
	}
	
	
	
}
