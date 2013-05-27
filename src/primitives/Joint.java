package primitives;

public class Joint {

	private double x, y, z;
	private Joint parent;
	
	/**
	 * 2D constructor.
	 * 
	 * @param x
	 * @param y
	 * @param p the parent coordinates, if root give null
	 */
	public Joint(double x, double y, Point p){
		this.x = x;
		this.y = y;
		if(p!=null){
			this.parent = new Joint(p.getX(), p.getY(), null);
		}
	}
	
	public boolean equals(Joint j){
		return j.getX()==this.x && j.getY()==this.y;
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

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Joint getParent() {
		return parent;
	}

	public void setParent(Joint parent) {
		this.parent = parent;
	}
	
	
	
}
