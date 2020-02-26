package drawit;

/**
 * An immutable abstraction for a point in the two-dimensional plane with int coordinates.
 */
public class IntPoint {
	
	private int x;
	private int y;
	
	/**
	 * Initializes this point with the given coordinates.
	 */
	public IntPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns true iff the open line segment ab intersects the open line segment cd.
	 * 
	 * @pre The line segments have at most one point in common.
	 *     | 0 <= x
	 */
	public static boolean lineSegmentsIntersect(IntPoint a, IntPoint b, IntPoint c, IntPoint d) {
		IntVector ab = new IntVector(a.getX() - b.getX(), a.getY() - b.getY());
		IntVector ac = new IntVector(a.getX() - c.getX(), a.getY() - c.getY());
		IntVector ad = new IntVector(a.getX() - d.getX(), a.getY() - d.getY());
		int val = (int) (Math.signum(ac.crossProduct(ab)) * (Math.signum(ad.crossProduct(ab))));
		
		if(val < 0) {
			return true;
		}
		return false;
	}	
	
	/**
	 * Returns true iff this point is on open line segment bc. An open line segment does not include its endpoints.
	 */
	public boolean isOnLineSegment(IntPoint b, IntPoint c) {
		IntVector ba = new IntVector(b.getX() - x, b.getY() - y);
		IntVector bc = new IntVector(b.getX() - c.getX(), b.getY() - c.getY());
		
		if(ba.isCollinearWith(bc)) {
			return false;
		} else if(0 < ba.crossProduct(bc) && ba.crossProduct(bc) < bc.crossProduct(bc) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if this point has the same coordinates as the given point; returns false otherwise.
	 * 
	 * 
	 */
	public boolean equals(IntPoint other) {
		if(x == other.getX() && y == other.getY()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns an IntPoint object representing the point obtained by displacing this point by the given vector.
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
