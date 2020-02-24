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
	
	public DoubleVector plus(DoubleVector other) {
		throw new RuntimeException("Not yet implemented");		
	}
	
	public DoubleVector scale(double d) {
		throw new RuntimeException("Not yet implemented");
	}
	
	public double asAngle() {
		throw new RuntimeException("Not yet implemented");		
	}

	public double getSize() {
		throw new RuntimeException("Not yet implemented");		
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
