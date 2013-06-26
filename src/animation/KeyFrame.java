package animation;

public class KeyFrame {

	private double x, y, a, l;
	private int time;
	
	public KeyFrame(int time, double x, double y, double a, double l){
		this.time = time;
		this.x = x;
		this.y = y;
		this.a = a;
		this.l = l;
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

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getL() {
		return l;
	}

	public void setL(double l) {
		this.l = l;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	
}
