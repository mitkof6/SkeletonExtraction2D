package primitives;

import primitives.Point;

public class Vector2D {

	public double i;

	public double j;

	public Vector2D() {
	}

	public Vector2D(double di, double dj) {
		i = di;
		j = dj;
	}

	public Vector2D(Vector2D Other) {
		i = Other.i;
		j = Other.j;
	}

	public Vector2D(Point PointFrom, Point PointTo) {
		i = PointTo.getX() - PointFrom.getX();
		j = PointTo.getY() - PointFrom.getY();
	}

	public Vector2D(Point Point) {
		i = Point.getX();
		j = Point.getY();
	}

	public void set(double di, double dj) {
		i = di;
		j = dj;
	}

	public void set(Point PointFrom, Point PointTo) {
		i = PointTo.getX() - PointFrom.getX();
		j = PointTo.getY() - PointFrom.getY();
	}

	public void set(Vector2D Other) {
		i = Other.i;
		j = Other.j;
	}

	public void reverse() {
		i = -i;
		j = -j;
	}

	public void turnRight() {
		double tempi = i;
		i = j;
		j = -tempi;
	}

	public void turnLeft() {
		double tempi = i;
		i = -j;
		j = tempi;
	}

	public void turnLeft(double dAng) {
		double temp = i;

		i = Math.cos(dAng) * i - Math.sin(dAng) * j;
		j = Math.sin(dAng) * temp + Math.cos(dAng) * j;
	}

	public double getLength() {
		return Math.sqrt(i * i + j * j);
	}

	public void setLength(double dDistance) {
		normalize();
		i = i * dDistance;
		j = j * dDistance;
	}

	public void normalize() {
		double dDistance = getLength();
		if (dDistance == 0)
			return;
		i = i / dDistance;
		j = j / dDistance;
	}

	public static Vector2D add(Vector2D V1, Vector2D V2) {
		Vector2D V3 = new Vector2D(V1.i + V2.i, V1.j + V2.j);
		return V3;
	}

	public static Vector2D minus(Vector2D V1, Vector2D V2) {
		Vector2D V3 = new Vector2D(V1.i - V2.i, V1.j - V2.j);
		return V3;
	}

	public void multiply(double dFactor) {
		i *= dFactor;
		j *= dFactor;
	}

	public double dot(Vector2D Other) {
		return i * Other.i + j * Other.j;
	}

	public double cross(Vector2D Other) {
		return i * Other.i - j * Other.j;
	}

	public void set(Point Other) {
		i = Other.getX();
		j = Other.getY();
	}

	public boolean vectorEqualTo(Vector2D Other) {
		boolean biClose;
		boolean bjClose;

		if (i == 0)
			biClose = Other.i == 0;
		else
			biClose = Math.abs((Other.i - i) / i) < 0.00001;
		if (j == 0)
			bjClose = Other.j == 0;
		else
			bjClose = Math.abs((Other.j - j) / j) < 0.00001;

		return (biClose && bjClose);
	}

	public double angleBetween(Vector2D Other) {
		double dDot = this.dot(Other);
		dDot = dDot / (this.getLength() * Other.getLength());
		// System.out.println(dDot+"-> "+Math.acos(dDot));
		if (dDot > 1.0) {
			dDot = 1.0;
		} else if (dDot < -1.0) {
			dDot = -1.0;
		}
		return Math.acos(dDot);
	}

	public Point getPoint() {
		return new Point(i, j);
	}

	public String toString() {
		return this.i + "i " + this.j + "j";
	}
}
