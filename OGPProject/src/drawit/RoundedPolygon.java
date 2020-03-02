package drawit;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * An instance of this class is a mutable abstraction storing a rounded polygon defined
   by a set of 2D points with integer coordinates and a nonnegative corner radius.
 * 
 * @invar This RoundedPolygon's radius is not negative.
 *    | 0 <= getRadius()
 */
public class RoundedPolygon {
	
	/**
	 * @invar | 0 <= radius
	 * @invar | vertices != null
	 * @invar | Arrays.stream(vertices).allMatch(e -> e != null)
	 * 
	 * @representationObject
	 */
	private IntPoint[] vertices;
	private int radius;
	
	/**
	 * Initializes an unrounded (radius 0) and empty (no intial vertices) polygon.
	 * 
	 * @mutates | this
	 * 
	 * @post This RoundedPolygon will initially not contain any vertices.
	 *    | getVertices().length == 0
	 * @post This RoundedPolygon will initially have a radius of 0.
	 *    | getRadius() == 0
	 */
	public RoundedPolygon() {
		vertices = new IntPoint[]{};
		radius = 0;
	}
	
	/** 
	 * Returns true if the given point is contained by the (non-rounded) polygon defined by this rounded polygon's vertices.
	 * This method does not take into account this rounded polygon's corner radius; it assumes a corner radius of zero.
	 * A point is contained by a polygon if it coincides with one of its vertices, or if it is on one of its edges,
	 * or if it is in the polygon's interior.
	 * 
	 * @inspects | this, other
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
     * @throws IllegalArgumentException if this polygon has not been initialized with any vertices yet.
     *    | getVertices().length == 0
	 * 
	 * @post
	 *      The result is {@code true} iff the given point coincides with one of this polygon's vertices,
	 *      or if the given point is on one of this polygon's edges, or if the given point is in this polygon's interior.
	 */
	public boolean contains(IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		if (getVertices().length == 0) {
			throw new IllegalArgumentException("polygon has not been initialized with any vertices yet");
		}
		
		boolean contains = false;
		for (int i = 0; i < vertices.length; i++) {
			int j = (i + 1) % vertices.length;
			IntPoint p1 = vertices[i];
			IntPoint p2 = vertices[j];
			
			if ((point.getY() > p1.getY()) == point.getY() > p2.getY()) {
				continue;
			}
			
			int intersectX = p1.getX() + (p2.getX() - p1.getX()) * (point.getY() - p1.getY()) / (p2.getY() - p1.getY());
			if (point.getX() == intersectX) {
				return true;
			}
			
			if (point.getX() < intersectX) {
				contains = !contains;
			}
		}
		return contains;
	}
	
	/**
	 * Changes the point at the given index in this polygon's vertices to the given point.
	 * 
	 * @mutates | this
	 * @inspects | point
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (exclusive).
	 *    | !(0 <= index && index < getVertices().length)
	 * 
	 * @post This polygon's number of vertices equals its old number of vertices.
	 *    | getVertices().length == old(getVertices().length)
	 * @post This polygon's vertices remain unchanged at all indexes except at the given index.
	 *    | IntStream.range(0, getVertices().length).allMatch(i -> 
	 *    |     i == index || getVertices()[i].equals(old(getVertices())[i]))
	 * @post This polygon's vertex at the given index is equal to the given point.
	 *    | getVertices()[index] == point
	 */
	public void update(int index, IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		if (!(0 <= index && index < getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		vertices = PointArrays.update(vertices, index, point);
	}
	
	/**
	 * Inserts a given point into the existing vertices at the specified index.
	 * 
	 * @mutates | this
	 * @inspects | point
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (inclusive).
	 *    | !(0 <= index || index <= getVertices().length)
	 * 
	 * @post The given point is inserted into this polygon's vertices at the given index.
	 *    | getVertices() == PointArrays.insert(old(getVertices()), index, point)
	 */
	public void insert(int index, IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		if (!(0 <= index || index <= getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		vertices = PointArrays.insert(vertices, index, point);
	}
	
	/**
	 * Removes the point from vertices at the given index.
	 * 
	 * @mutates | this
	 * @inspects | point
	 * 
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (exclusive).
	 *    | !(0 <= index || index < getVertices().length)
	 * 
	 * @post The vertex at the given index is removed from this polygon's vertices.
	 *    | getVertices() == PointArrays.remove(old(getVertices()), index)
	 */
	public void remove(int index) {
		if (!(0 <= index || index < getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		vertices = PointArrays.remove(vertices, index);
	}
	
	/**
	 * Returns a textual representation of a set of drawing commands for drawing this rounded polygon.
     * The returned text consists of a sequence of drawing operators and arguments, separated by spaces. 
     * The drawing operators are line and arc. Each argument is a decimal representation of a floating-point number.
     * Operator line takes four arguments: X1 Y1 X2 Y2; it draws a line between (X1, Y1) and (X2, Y2). 
     * Operator arc takes five arguments: X Y R S E. It draws a part of a circle. The circle is defined by its center (X, Y) 
     * and its radius R. The part to draw is defined by the start angle A and angle extent E, both in radians. 
     * Positive X is angle zero; positive Y is angle Math.PI / 2; negative Y is angle -Math.PI / 2.
     * 
     * @inspects | this
     * 
     * @post
     *      The result is an empty string if this polygon has less than 3 vertices. If this polygon has 3 or more vertices,
     *      the result is a string detailing the drawing instructions of this polygon using the 'line' and 'arc' drawing operators.
	 */
	public String getDrawingCommands() {
		if (vertices.length < 3) {
			return "";
		}
		
		String bc = "\n";
		String commands = "";
		
		for (int i = 0; i < vertices.length; i++) {
			int j = (i + 1) % vertices.length;
			int k = (i + 2) % vertices.length;
			IntPoint a = vertices[i];
			IntPoint b = vertices[j];
			IntPoint c = vertices[k];
			
			//TODO: Radius == 0
			//TODO: Does not work yet

			DoubleVector baVector = new DoubleVector((a.getX() - b.getX()), a.getY() - b.getY());
			DoubleVector bcVector = new DoubleVector((c.getX() - b.getX()), c.getY() - b.getY());
			DoubleVector bisectorVector = baVector.plus(bcVector);
			DoubleVector baUnitVector = new DoubleVector(baVector.getX() / baVector.getSize(), baVector.getY() / baVector.getSize());
			DoubleVector bisectorUnitVector = new DoubleVector(bisectorVector.getX() / bisectorVector.getSize(), 
					bisectorVector.getY() / bisectorVector.getSize());
			
			double unitCutOff = Math.abs(baUnitVector.dotProduct(bisectorUnitVector));
			double unitRadius = Math.abs(bisectorUnitVector.crossProduct(baUnitVector));
			double scaleFactor = Math.min(radius / unitRadius, Math.min(baVector.getSize() / 2, bcVector.getSize() / 2) / unitCutOff);
			System.out.println("ScaleFactor: " + scaleFactor);
			double cutOff = unitCutOff * scaleFactor;
			
			DoublePoint baCutPoint = new DoublePoint(b.getX() + baVector.getX() / baVector.getSize() * cutOff, 
					b.getY() + baVector.getY() / baVector.getSize() * cutOff);
			DoublePoint bcCutPoint = new DoublePoint(b.getX() + bcVector.getX() / bcVector.getSize() * cutOff, 
					b.getY() + bcVector.getY() / bcVector.getSize() * cutOff);
			DoublePoint cornerCenterPoint = new DoublePoint(b.getX() + bisectorVector.getX() / bisectorVector.getSize() * scaleFactor,
					b.getY() + bisectorVector.getY() / bisectorVector.getSize() * scaleFactor);

			double startAngle = baCutPoint.minus(cornerCenterPoint).asAngle();
			double endAngle = bcCutPoint.minus(cornerCenterPoint).asAngle();
			double extentAngle = endAngle - startAngle;
			if (extentAngle > Math.PI) {
				extentAngle -= Math.PI * 2;
			} else if (extentAngle <= -Math.PI) {
				extentAngle += Math.PI * 2;
			}
			
			commands += " " + baCutPoint.getX() + " " + baCutPoint.getY() + bc + "arc " + cornerCenterPoint.getX() + " "
					+ cornerCenterPoint.getY() + " " + unitRadius * scaleFactor + " " + startAngle + " " + extentAngle
					+ bc + "line " + bcCutPoint.getX() + " " + bcCutPoint.getY();

		}
		String c = commands.substring(commands.lastIndexOf(bc) + 1, commands.length()) + commands.substring(0, commands.lastIndexOf(bc));
		System.out.println("");
		System.out.println(c);
		return c;
	}
	
	/**
	 * Returns the radius of the corners of this rounded polygon.
	 * 
	 * @post The result is not negative.
	 *    | 0 <= result
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * @mutates | this
	 * 
	 * Sets this rounded polygon's corner radius to the given value.
	 * 
	 * @throws IllegalArgumentException
	 *      The given radius is negative.
	 *    | newRadius < 0
	 * 
	 * @post This polygon's corner radius is equal to the given radius.
	 *    | getRadius() == newRadius
	 */
	public void setRadius(int newRadius) {
		if (newRadius < 0) {
			throw new IllegalArgumentException("negative radius");
		}
		
		radius = newRadius;
	}
	
	/**
	 * Returns a new array whose elements are the vertices of this rounded polygon.
	 * 
	 * @creates | result
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 * @post The result's elements are not {@code null}
	 *    | Arrays.stream(result).allMatch(e -> e != null)
	 */
	public IntPoint[] getVertices() {
		return vertices.clone();
	}
	
	/**
	 * Sets the vertices of this rounded polygon to be equal to the elements of the given array.
	 * 
	 * @throws IllegalArgumentException
	 *      The given vertices do not define a proper polygon.
	 *    | PointArrays.checkDefinesProperPolygon(newVertices) != null
	 * 
	 * @post This polygon's vertices are equal to the given vertices.
	 *    | getVertices() == newVertices
	 */
	public void setVertices(IntPoint[] newVertices) {
		String polygonError = PointArrays.checkDefinesProperPolygon(newVertices);
		if (polygonError != null) {
			throw new IllegalArgumentException(polygonError);
		}
		
		vertices = newVertices.clone();
	}
}
