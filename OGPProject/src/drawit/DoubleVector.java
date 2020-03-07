package drawit;

public class DoubleVector {
	
	private final double x;
	private final double y;
	
	public DoubleVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double crossProduct(DoubleVector other) {
		return x * other.getY() - y * other.getX();
	}
	
	public double dotProduct(DoubleVector other) {
		return x * other.getX() + y * other.getY();
	}
	
	public DoubleVector plus(DoubleVector other) {
		return new DoubleVector(x + other.getX(), y + other.getY());
	}
	
	public DoubleVector scale(double d) {
		return new DoubleVector(d * x, d * y);
	}
	
	public double asAngle() {
		return Math.atan2(y, x);
	}
	
	public double getSize() {
		return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
	}
	
	public double getX() {
		return x;		
	}
	
	public double getY() {
		return y;
	}
}
