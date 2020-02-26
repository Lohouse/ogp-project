package drawit;

/**
 * An immutable abstraction for a point in the two-dimensional plane with double coordinates.
 */
public class DoublePoint {
	
	private double x;
	private double y;
	
	/**
	 * Initializes this point with the given coordinates.
	 */
	public DoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns an DoublePoint object representing the point obtained by displacing this point by the given vector.
	 * 
	 * @post The result is a vector with its x-coordinate being the sum of the x-coordinates of both points
	 *    | result.getX() == this.getX() + other.getX()
	 * @post The result is a vector with its y-coordinate being the sum of the y-coordinates of both points
	 *    | result.getY() == this.getY() + other.getY()
	 */
	public DoublePoint plus(DoubleVector other) {
		return new DoublePoint(x + other.getX(), y + other.getY());	
	}
	
	/**
	 * Returns an DoubleVector object representing the displacement from other to this.
	 * 
	 * @post The result is a vector with its x-coordinate being the subtraction of the x-coordinate of the given point from that of this point.
	 *    | result.getX() == this.getX() - other.getX()
	 * @post The result is a vector with its y-coordinate being the subtraction of the y-coordinate of the given point from that of this point.
	 *    | result.getY() == this.getY() - other.getY()
	 */
	public DoubleVector minus(DoublePoint other) {
		return new DoubleVector(x - other.getX(), y - other.getY());	
	}	
	
	/**
	 * Returns an IntPoint object whose coordinates are obtained by rounding the coordinates of this point to the nearest integer.
	 * 
	 * @post The result is an IntPoint whose coordinates are rounded to the nearest integer.
	 *    | result == new IntPoint((int) Math.round(this.getX()), (int) Math.round(this.getY()))
	 */
	public IntPoint round() {
		return new IntPoint((int) Math.round(x), (int) Math.round(y));
	}
	
	/**
	 * Returns this point's X coordinate.
	 */
	public double getX() {
		return x;	
	}
	
	/**
	 * Returns this point's Y coordinate.
	 */
	public double getY() {
		return y;
	}
}
