package skeleton2D;

import java.util.ArrayList;

import primitives.Point;

import animation.KeyFrame;

import Jama.Matrix;


public class Bone {
	
	private double x, y, a, l, initAngle;
	private double angleOffst, lengthOffset;
	private int name;
	private ArrayList<Bone> child;
	private Bone parent;
	private ArrayList<KeyFrame> keyFrames;
	private Matrix ablolute;
	
	public Bone(double x, double y, double a, double l, int name, Bone parent){
		this.x = x;
		this.y = y;
		this.a = a;
		this.initAngle = a;
		this.l = l;
		this.name = name;
		this.parent = parent;
		
		child = new ArrayList<>();
		keyFrames = new ArrayList<>();
	}
	
	public void addChild(Bone child){
		this.child.add(child);
	}
	
	public void addKeyFrames(KeyFrame keyFrames){
		this.keyFrames.add(keyFrames);
	}
	
	public ArrayList<KeyFrame> getKeyFrame(){
		return this.keyFrames;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setA(double a) {
		this.a = a;
	}

	public void setL(double l) {
		this.l = l;
	}

	public void setName(int name) {
		this.name = name;
	}

	public void setChild(ArrayList<Bone> child) {
		this.child = child;
	}

	public void setParent(Bone parent) {
		this.parent = parent;
	}

	public Point getInitialPosition(){
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

	public int getName(){
		return this.name;
	}
	
	public void setInitialAngle(){
		this.a = initAngle;
	}
	
	public ArrayList<Bone> getChild() {
		return child;
	}

	public Bone getParent() {
		return parent;
	}

	public double getAngleOffst() {
		return angleOffst;
	}

	public void setAngleOffst(double angleOffst) {
		this.angleOffst = angleOffst;
	}

	public double getLengthOffset() {
		return lengthOffset;
	}

	public void setLengthOffset(double lengthOffset) {
		this.lengthOffset = lengthOffset;
	}

	public void setAbsoluteMatrix(double[] m){
		double[][] array = {{m[0], m[4], m[8], m[12]},
							{m[1], m[5], m[9], m[13]},
							{m[2], m[6], m[10], m[14]},
							{m[3], m[7], m[11], m[15]}};
		
		this.ablolute = new Matrix(array);
		//System.out.println(Arrays.deepToString(ablolute.getArray()));
	}
	
	public Matrix getAbsoluteMatrix(){
		return this.ablolute;
	}
	
	public void setAbsoluteMatrix(Matrix m){
		this.ablolute = m;
	}
	
	public Point getAbsolutePosition(){
		return new Point(ablolute.get(0, 3), ablolute.get(1, 3));
	}

	
}
