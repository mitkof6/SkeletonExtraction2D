package primitives;

public class Vertex2D extends Point{

	private Vector2D n1, n2;
	
	public Vertex2D(double x, double y) {
		super(x, y);
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
	
	

}
