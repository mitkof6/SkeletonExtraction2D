package animation;

/**
 * Represents a key frame entity
 * 
 * @author Jim Stanev
 */
public class KeyFrame {

	private double x, y, angle, length;
	private int time;
	
	/**
	 * Constructor
	 * 
	 * @param time the time
	 * @param x coordinate
	 * @param y coordinate
	 * @param angle the angle in this key frame
	 * @param length the length in this key frame
	 */
	public KeyFrame(int time, double x, double y, double angle, double length){
		this.time = time;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.length = length;
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

	public double getAngle() {
		return angle;
	}

	public void setAngle(double a) {
		this.angle = a;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double l) {
		this.length = l;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
