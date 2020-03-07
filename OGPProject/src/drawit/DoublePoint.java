package drawit;

public class DoublePoint {
	
	private final double x;
	private final double y;
	
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public DoublePoint plus(DoubleVector other) {
		return new DoublePoint(x + other.getX(), y + other.getY());	
	}
	
	public DoubleVector minus(DoublePoint other) {
		return new DoubleVector(x - other.getX(), y - other.getY());	
	}	
	
	public IntPoint round() {
		return new IntPoint((int) Math.round(x), (int) Math.round(y));
	}
	
	public double getX() {
		return x;	
	}
	
	public double getY() {
		return y;
	}
}
