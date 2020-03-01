package drawit;

/**
 * An instance of this class represents a displacement in the two-dimensional plane.
 * 
 * @immutable
 */
public class IntVector {
	
	private final int x;
	private final int y;
	
	/**
	 * Initializes this vector with the given coordinates.
	 */
	public IntVector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the cross product of this vector and the given vector.
	 * 
	 * @post The result is a long with a value equal to the subtraction of the product of the opposite coordinates of each vector.
	 *    | result == (long) this.getX() * other.getY() - (long) this.getY() * other.getX()
	 */
	public long crossProduct(IntVector other) {
		return x * other.getY() - y * other.getX();
	}
	
	/**
	 * Returns the dot product of this vector and the given vector.
	 * 
	 * @post The result is a long with a value equal to the sum of the product of the respective coordinates of each vector.
	 *    | result == (long) this.getX() * other.getX() + (long) this.getY() * other.getY()
	 */
	public long dotProduct(IntVector other) {
		return (long) x * other.x + (long) y * other.y;
	}
	
	/**
	 * Returns whether this vector is collinear with the given vector.
	 * 
	 * @post The result is a boolean value indicating whether the cross product of the vectors is equal to zero.
	 *    | result == (this.crossProduct(other) == 0)
	 */
	public boolean isCollinearWith(IntVector other) {
		return crossProduct(other) == 0;
	}
	
	/**
	 * Returns a DoubleVector object that represents the same vector represented by this IntVector object.
	 * 
	 * @post The result is a vector with its X coordinate being a double with the same value as the X coordinate of the original vector.
	 *    | result.getX() == (double) this.getX()
	 * @post The result is a vector with its Y coordinate being a double with the same value as the Y coordinate of the original vector.
	 *    | result.getY() == (double) this.getY()
	 */
	public DoubleVector asDoubleVector() {
		return new DoubleVector(x, y);
	}
	
	/**
	 * Returns this vector's X coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns this vector's Y coordinate.
	 */
	public int getY() {
		return y;
	}	
}
