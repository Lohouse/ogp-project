package drawit;

/**
 * An instance of this class represents a displacement in the two-dimensional plane.
 */
public class DoubleVector {
	
	private double x;
	private double y;
	
	/**
	 * Initializes this vector with the given coordinates.
	 */
	public DoubleVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the cross product of this vector and the given vector.
	 * 
	 * @post The result is equal to the subtraction of the product of the opposite coordinates of each vector.
	 *    | result == this.getX() * other.getY() - this.getY() * other.getX()
	 */
	public double crossProduct(DoubleVector other) {
		return x * other.getY() - y * other.getX();
	}
	
	/**
	 * Returns the dot product of this vector and the given vector.
	 * 
	 * @post The result is equal to the sum of the product of the respective coordinates of each vector.
	 *    | result == this.getX() * other.getX() + this.getY() * other.getY()
	 */
	public double dotProduct(DoubleVector other) {
		return x * other.getX() + y * other.getY();
	}
	
	/**
	 * Returns a DoubleVector object representing the vector obtained by adding the given vector to this vector.
	 */
	public DoubleVector plus(DoubleVector other) {
		return new DoubleVector(x + other.getX(), y + other.getY());
	}
	
	/**
	 * Returns a DoubleVector object whose coordinates are obtained by multiplying this vector's coordinates by the given scale factor.
	 */
	public DoubleVector scale(double d) {
		return new DoubleVector(d * x, d * y);
	}
	
	/**
	 * Returns the angle from positive X to this vector, in radians. The angle from positive X to positive Y is Math.PI / 2;
	 * the angle from positive X to negative Y is -Math.PI / 2.
	 * 
	 * @post The result is equal to the inverse tangent function of the Y coordinate divided by the X coordinate.
	 *    | result == Math.atan2(this.getY(), this.getX())
	 */
	public double asAngle() {
		return Math.atan2(y, x);
	}
	
	/**
	 * Returns the size of this vector.
	 * 
	 * @post The result is equal to the Euclidean distance from the origin to the point in 2D space to which this vector points.
	 *    | result == Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2))
	 */
	public double getSize() {
		return Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
	}
	
	/**
	 * Returns this vector's X coordinate.
	 */
	public double getX() {
		return x;		
	}
	
	/**
	 * Returns this vector's Y coordinate.
	 */
	public double getY() {
		return y;
	}
}
