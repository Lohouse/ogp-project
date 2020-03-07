package drawit;

/**
 * An immutable abstraction for a point in the two-dimensional plane with int coordinates.
 * 
 * @immutable
 */
public class IntPoint {
	
	private final int x;
	private final int y;
	
	/**
	 * Initializes this point with the given coordinates.
	 * 
	 * @mutates | this
	 * 
	 * @post This IntPoints's X coordinate is equal to the given x.
	 *    | getX() == x
	 * @post This IntPoints's Y coordinate is equal to the given y.
	 *    | getY() == y
	 */
	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns true iff the open line segment ab intersects the open line segment cd.
	 * 
	 * @inspects | a, b, c, d
	 * 
	 * @pre Argument {@code a} is not {@code null}.
     *    | a != null
     * @pre Argument {@code b} is not {@code null}.
     *    | b != null
     * @pre Argument {@code c} is not {@code null}.
     *    | c != null
     * @pre Argument {@code d} is not {@code null}.
     *    | d != null
     * @pre The line segments have at most one point in common.
     * 	  | !(((a.isOnLineSegment(c, d) || b.isOnLineSegment(c, d)) && areCollinear(a, b, c, d)) || ((c.isOnLineSegment(a, b) || d.isOnLineSegment(a, b)) && areCollinear(a, b, c, d)))
     *    
     * @post The result is {@code true} if the line segment through a and b intersects the line segment through c and d
	 */
	public static boolean lineSegmentsIntersect(IntPoint a, IntPoint b, IntPoint c, IntPoint d) {
		IntVector ab = new IntVector(a.getX() - b.getX(), a.getY() - b.getY());
		IntVector ac = new IntVector(a.getX() - c.getX(), a.getY() - c.getY());
		IntVector ad = new IntVector(a.getX() - d.getX(), a.getY() - d.getY());
		int val1 = (int) (Math.signum(ac.crossProduct(ab)) * (Math.signum(ad.crossProduct(ab))));
		
		IntVector cd = new IntVector(c.getX() - d.getX(), c.getY() - d.getY());
		IntVector ca = new IntVector(c.getX() - a.getX(), c.getY() - a.getY());
		IntVector cb = new IntVector(c.getX() - b.getX(), c.getY() - b.getY());
		int val2 = (int) (Math.signum(ca.crossProduct(cd)) * (Math.signum(cb.crossProduct(cd))));
		
		if(val1 < 0 && val2 < 0) {
			return true;
		}
		return false;
	}	
	
	/**
	 * Returns true if this point is on open line segment bc. An open line segment does not include its endpoints.
	 * 
	 * @inspects | this, a, b, c, d
	 * 
	 * @pre Argument {@code b} is not {@code null}.
     *    | b != null
	 * @pre Argument {@code c} is not {@code null}.
     *    | c != null
     *    
	 * @post The result is {@code true} when this point is on the line segment through b and c.
	 *    | (!areCollinear(b, this, b, c) ? false : ((0 < dotProduct(b, this, b, c)) && (dotProduct(b, this, b, c) < dotProduct(b, c, b, c)) ? true : false)) //TODO: this isn't correct
	 */
	public boolean isOnLineSegment(IntPoint b, IntPoint c) {
		IntVector ba = new IntVector(b.getX() - x, b.getY() - y);
		IntVector bc = new IntVector(b.getX() - c.getX(), b.getY() - c.getY());
		
		if(!ba.isCollinearWith(bc)) {
			return false;
		} else if(0 < ba.dotProduct(bc) && ba.dotProduct(bc) < bc.dotProduct(bc)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the vector between IntPoints a and b is collinear with the vector between c and d.
	 * 
	 * @inspects | a, b, c, d
	 * 
	 * @pre Argument {@code a} is not {@code null}.
     *    | a != null
	 * @pre Argument {@code b} is not {@code null}.
     *    | b != null
	 * @pre Argument {@code c} is not {@code null}.
     *    | c != null
	 * @pre Argument {@code d} is not {@code null}.
     *    | d != null
     *    
	 * @post The result is {@code true} when the vector between IntPoints a and b is collinear with the vector between c and d.
	 */
	public static boolean areCollinear(IntPoint a, IntPoint b, IntPoint c, IntPoint d) {
		IntVector ab = new IntVector(a.getX() - b.getX(), a.getY() - b.getY());
		IntVector cd = new IntVector(c.getX() - d.getX(), c.getY() - d.getY());
		
		return ab.isCollinearWith(cd);
	}
	
	/**
	 * Returns the dot product of the vector between a and b, and the vector between c and d.
	 * 
	 * @inspects | a, b, c, d
	 * 
	 * @pre Argument {@code a} is not {@code null}.
     *    | a != null
	 * @pre Argument {@code b} is not {@code null}.
     *    | b != null
	 * @pre Argument {@code c} is not {@code null}.
     *    | c != null
	 * @pre Argument {@code d} is not {@code null}.
     *    | d != null
     *    
	 * @post The result is the dot product of the vector between a and b, and the vector between c and d.
	 */
	public static long dotProduct(IntPoint a, IntPoint b, IntPoint c, IntPoint d) {
		IntVector ab = new IntVector(a.getX() - b.getX(), a.getY() - b.getY());
		IntVector cd = new IntVector(c.getX() - d.getX(), c.getY() - d.getY());
		
		return ab.dotProduct(cd);
	}
	
	
	/**
	 * Returns true if this point has the same coordinates as the given point; returns false otherwise.
	 * 
	 * @creates | result
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code other} is not {@code null}.
     *    | other != null
	 * 
	 * @post The result is {@code true} when both coordinates of this point match those of the given point.
	 *    | result ==  ((this.getX() == other.getX() && this.getY() == other.getY()) ? true: false)
	 */
	public boolean equals(IntPoint other) {
		if(x == other.getX() && y == other.getY()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns an IntPoint object representing the point obtained by displacing this point by the given vector.
	 * 
	 * @creates | result
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code vector} is not {@code null}.
     *    | vector != null
	 * 
	 * @post The result is a vector with its x-coordinate being the sum of the x-coordinates of both points
	 *    | result.getX() == this.getX() + vector.getX()
	 * @post The result is a vector with its y-coordinate being the sum of the y-coordinates of both points
	 *    | result.getY() == this.getY() + vector.getY()
	 */
	public IntPoint plus(IntVector vector) {
		return new IntPoint(x + vector.getX(), y + vector.getY());		
	}
	
	/**
	 * Returns an IntVector object representing the displacement from other to this.
	 * 
	 * @creates | result
	 * @inspects | this, other
	 * 
	 * @pre Argument {@code other} is not {@code null}.
     *    | other != null
	 * 
	 * @post The result is a vector with its x-coordinate being the subtraction of the x-coordinate of the given point from that of this point.
	 *    | result.getX() == this.getX() - other.getX()
	 * @post The result is a vector with its y-coordinate being the subtraction of the y-coordinate of the given point from that of this point.
	 *    | result.getY() == this.getY() - other.getY()
	 */
	public IntVector minus(IntPoint other) {
		return new IntVector(x - other.getX(), y - other.getY());
	}	
	
	/**
	 * Returns a DoublePoint object that represents the same 2D point represented by this IntPoint object.
	 * 
	 * @creates | result
	 * @inspects | this
	 * 
	 * @post The x-coordinate of the result is the same as this x-coordinate
	 *    | result.getX() == this.getX()
	 * @post The y-coordinate of the result is the same as this y-coordinate
	 *    | result.getY() == this.getY()
	 */
	public DoublePoint asDoublePoint() {
		return new DoublePoint((double) x, (double) y);
	}
	
	/**
	 * Returns this point's X coordinate.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns this point's Y coordinate.
	 */
	public int getY() {
		return y;
	}
}
