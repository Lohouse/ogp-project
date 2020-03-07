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
	 * 
	 * @mutates | this
	 * 
	 * @post This IntVector's X coordinate is equal to the given x.
	 *    | getX() == x
	 * @post This IntVector's Y coordinate is equal to the given y.
	 *    | getY() == y
	 */
	public IntVector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the cross product of this vector and the given vector.
	 * 
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code other} is not {@code null}.
     *    | other != null
     * 
	 * @post 
	 *      The result is a long with a value equal to the product of the X coordinate of
	 *      this vector and the Y coordinate of the other vector, subtracted by the
	 *      product of the Y vector of this vector and the X coordinate of the other vector.
	 *    | result == (long) getX() * other.getY() - (long) getY() * other.getX()
	 */
	public long crossProduct(IntVector other) {
		return (long) x * other.getY() - (long) y * other.getX();
	}
	
	/**
	 * Returns the dot product of this vector and the given vector.
	 * 
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code other} is not {@code null}.
     *    | other != null
	 * 
	 * @post 
	 *      The result is a long with a value equal to the sum of the product of the
	 *      X coordinate of both vectors and the product of the Y coordinate of both vectors.
	 *    | result == (long) getX() * other.getX() + (long) getY() * other.getY()
	 */
	public long dotProduct(IntVector other) {
		return (long) x * other.x + (long) y * other.y;
	}
	
	/**
	 * Returns whether this vector is collinear with the given vector.
	 * 
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code other} is not {@code null}.
     *    | other != null
	 * 
	 * @post The result is {@code true} iff the cross product of the vectors equals zero.
	 *    | result == (crossProduct(other) == 0)
	 */
	public boolean isCollinearWith(IntVector other) {
		return crossProduct(other) == 0;
	}
	
	/**
	 * Returns a DoubleVector object that represents the same vector represented by this IntVector object.
	 * 
	 * @creates | result
	 * @inspects | this
	 * 
	 * @post The result is a {@code DoubleVector} object.
	 * @post The result's X coordinate will have the same value as this vector's X coordinate.
	 * @post The result's Y coordinate will have the same value as this vector's Y coordinate.
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
