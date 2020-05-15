package drawit;

import java.awt.Color;
import java.util.Arrays;
import java.util.stream.IntStream;
import drawit.shapegroups1.Extent;

//TODO: post-condition for bounding box?
/**
 * An instance of this class is a mutable abstraction storing a rounded polygon defined
   by a set of 2D points with integer coordinates and a nonnegative corner radius.
 * 
 * @invar This RoundedPolygon's radius is not negative.
 *    | 0 <= this.getRadius()
 * @invar This RoundedPolygon's vertices are not {@code null}
 *    | this.getVertices() != null && 
 *    | Arrays.stream(this.getVertices()).allMatch(e -> e != null)
 * @invar This RoundedPolygon stores a proper polygon.
 *    | PointArrays.checkDefinesProperPolygon(this.getVertices()) == null
 * @invar This RoundedPolygon's color is not null.
 *    | this.getColor() != null
 * @invar This RoundedPolygon's bounding box is not null.
 * 	  | this.getBoundingBox() != null
 */
public class RoundedPolygon {
	
	/**
	 * @invar | 0 <= radius
	 * @invar | vertices != null
	 * @invar | Arrays.stream(vertices).allMatch(e -> e != null)
	 * @invar | PointArrays.checkDefinesProperPolygon(vertices) == null
	 * @invar | color != null
	 * @invar | box != null
	 * 
	 * @representationObject
	 */
	private IntPoint[] vertices;
	private int radius;
	private Color color;
	private Extent box;
	
	/**
	 * Initializes an unrounded (radius 0) and empty (no initial vertices) polygon.
	 * 
	 * @mutates | this
	 * 
	 * @post This RoundedPolygon initially contains no vertices.
	 *    | this.getVertices().length == 0
	 * @post This RoundedPolygon initially has a radius of 0.
	 *    | this.getRadius() == 0
	 * @post This RoundedPolygon initially has a white color.
	 *    | this.getColor() == Color.WHITE
	 * @post The initial bounding box only contains the origin.
	 *    | this.getBoundingBox().equals(Extent.ofLeftTopRightBottom(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE))
	 */
	public RoundedPolygon() {
		vertices = new IntPoint[]{};
		radius = 0;
		color = Color.WHITE;
		box = Extent.ofLeftTopRightBottom(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE); //TODO: Is this correct? Also check corresponding post-condition
	}
	
	/** 
	 * Returns true if the given point is contained by the (non-rounded) polygon defined by this rounded polygon's vertices.
	 * This method does not take into account this rounded polygon's corner radius; it assumes a corner radius of zero.
	 * A point is contained by a polygon if it coincides with one of its vertices, or if it is on one of its edges,
	 * or if it is in the polygon's interior.
	 * 
	 * @inspects | this
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * 
	 * @post
	 *      The result is {@code true} iff {@code point} coincides with one of this polygon's vertices,
	 *      or if {@code point} is on one of this polygon's edges, or if {@code point} is in this polygon's interior.
	 */
	public boolean contains(IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		
		boolean contains = false;
		for (int i = 0; i < vertices.length; i++) {
			int j = (i + 1) % vertices.length;
			IntPoint p1 = vertices[i];
			IntPoint p2 = vertices[j];
			
			if (point.equals(p1) || point.equals(p2)) {
				return true;
			}
			
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
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (exclusive).
	 *    | !(0 <= index && index < this.getVertices().length)
	 * @throws IllegalArgumentException if the new state of this polygon is not proper.
	 *    | PointArrays.checkDefinesProperPolygon(
	 *    |     PointArrays.update(this.getVertices(), index, point)) != null
	 * 
	 * @post The amount of vertices of this polygon remains unchanged.
	 *    | this.getVertices().length == old(this.getVertices()).length
	 * @post This polygon's vertices remain unchanged at all indexes except at index {@code index}.
	 *    | IntStream.range(0, this.getVertices().length).allMatch(i -> 
	 *    |     i == index || this.getVertices()[i].equals(old(this.getVertices())[i]))
	 * @post This polygon's vertex at index {@code index} is equal to {@code point}.
	 *    | this.getVertices()[index] == point
	 */
	public void update(int index, IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		if (!(0 <= index && index < getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		IntPoint[] newVertices = PointArrays.update(vertices, index, point);
		String properCheck = PointArrays.checkDefinesProperPolygon(newVertices);
		
		if (properCheck != null) {
			throw new IllegalArgumentException("new vertices do not define a proper polygon: " + properCheck);
		}
		
		vertices = newVertices;
		updateBox();
	}
	
	/**
	 * Inserts a given point into the existing vertices at the specified index.
	 * 
	 * @mutates | this
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (inclusive).
	 *    | !(0 <= index && index <= this.getVertices().length)
	 * @throws IllegalArgumentException if the new state of this polygon is not proper.
	 *    | PointArrays.checkDefinesProperPolygon(
	 *    |     PointArrays.insert(this.getVertices(), index, point)) != null
	 * 
	 * @post The amount of vertices of this polygon is increased by 1.
	 *    | this.getVertices().length == old(this.getVertices()).length + 1
	 * @post This polygon's vertices before index {@code index} remain unchanged.
	 *    | IntStream.range(0, index).allMatch(i -> 
	 *    |     this.getVertices()[i].equals(old(this.getVertices())[i]))
	 * @post The vertex of this polygon at {@code index} is equal to {@code point}.
	 *    | this.getVertices()[index].equals(point)
	 * @post This polygon's vertices after index {@code index} have its place within the polygon shifted up by 1 compared to the previous state of this polygon.
	 *    | IntStream.range(index + 1, this.getVertices().length).allMatch(i -> 
	 *    |     this.getVertices()[i].equals(old(this.getVertices())[i - 1]))
	 */
	public void insert(int index, IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		if (!(0 <= index && index <= getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		IntPoint[] newVertices = PointArrays.insert(vertices, index, point);
		String properCheck = PointArrays.checkDefinesProperPolygon(newVertices);
		
		if (properCheck != null) {
			throw new IllegalArgumentException("new vertices do not define a proper polygon: " + properCheck);
		}
		
		vertices = newVertices;
		updateBox();
	}

	/**
	 * Removes the point from vertices at the given index.
	 * 
	 * @mutates | this
	 * 
	 * @throws IllegalArgumentException if argument {@code index} is not between 0 (inclusive)
	 *         and the current amount of vertices in this polygon (exclusive).
	 *    | !(0 <= index && index < this.getVertices().length)
	 * 
	 * @post The amount of vertices of this polygon is decreased by 1.
	 *    | this.getVertices().length == old(this.getVertices()).length - 1
	 * @post This polygon's vertices before index {@code index} remain unchanged.
	 *    | IntStream.range(0, index).allMatch(i -> 
	 *    |     this.getVertices()[i].equals(old(this.getVertices())[i]))
	 * @post This polygon's vertices starting at index {@code index} have its place within the polygon shifted down by 1 compared to the previous state of this polygon.
	 *    | IntStream.range(index, this.getVertices().length).allMatch(i -> 
	 *    |     this.getVertices()[i].equals(old(this.getVertices())[i + 1]))
	 */
	public void remove(int index) {
		if (!(0 <= index && index < getVertices().length)) {
			throw new IllegalArgumentException("invalid index");
		}
		
		IntPoint[] newVertices = PointArrays.remove(vertices, index);
		String properCheck = PointArrays.checkDefinesProperPolygon(newVertices);
		
		if (properCheck != null) {
			throw new IllegalArgumentException("new vertices do not define a proper polygon: " + properCheck);
		}
		
		vertices = newVertices;
		updateBox();
	}
	
	/**
	 * Updates the bounding box to represent the current vertices.
     * 
     * @post The left coordinate of the bounding box is equal to the smallest x-value of the vertices.
     *    | getBoundingBox().getLeft() == Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt()
     * @post The right coordinate of the bounding box is equal to the largest x-value of the vertices, or one greater than
     *       the largest x-value of the vertices only if the largest x-value of the vertices is equal to the smallest x-value of the vertices.
     *    | getBoundingBox().getRight() == (Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt()
     *    |     == Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt() ?
     *    |     Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt() + 1:
     *    |     Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt())
     * @post The top coordinate of the bounding box is equal to the smallest y-value of the vertices.
     *    | getBoundingBox().getTop() == Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt()
     * @post The bottom coordinate of the bounding box is equal to the largest y-value of the vertices, or one greater than
     *       the largest y-value of the vertices only if the largest y-value of the vertices is equal to the smallest y-value of the vertices.
     *    | getBoundingBox().getBottom() == (Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt()
     *    |     == Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt() ?
     *    |     Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt() + 1:
     *    |     Arrays.stream(getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt())
	 */
	public void updateBox() {
		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		
		for(IntPoint vertex : vertices) {
			int x = vertex.getX();
			int y = vertex.getY();
			if(x < left) left = x;
			if(x > right) right = x;
			if(y < top) top = y;
			if(y > bottom) bottom = y;
		}

		if (left == right) right++;
		if (top == bottom) bottom++; //TODO: Is this correct?
		
		box = Extent.ofLeftTopRightBottom(left, top, right, bottom);
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

			DoubleVector baVector = new DoubleVector((a.getX() - b.getX()), a.getY() - b.getY());
			DoubleVector bcVector = new DoubleVector((c.getX() - b.getX()), c.getY() - b.getY());
			DoubleVector baUnitVector = new DoubleVector(baVector.getX() / baVector.getSize(), baVector.getY() / baVector.getSize());
			DoubleVector bcUnitVector = new DoubleVector(bcVector.getX() / bcVector.getSize(),bcVector.getY() / bcVector.getSize());
			DoubleVector bisectorVector = baUnitVector.plus(bcUnitVector);
			DoubleVector bisectorUnitVector = new DoubleVector(bisectorVector.getX() / bisectorVector.getSize(), 
					bisectorVector.getY() / bisectorVector.getSize());
			
			double unitCutOff = Math.abs(baUnitVector.dotProduct(bisectorUnitVector));
			double unitRadius = Math.abs(bisectorUnitVector.crossProduct(baUnitVector));
			double scaleFactor = Math.min(radius / unitRadius, Math.min(baVector.getSize() / 2, bcVector.getSize() / 2) / unitCutOff);
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
			
			commands += " " + baCutPoint.getX() + " " + baCutPoint.getY();			
			if (extentAngle != 0) {
				commands += bc + "arc " + cornerCenterPoint.getX() + " " + cornerCenterPoint.getY() + " " + 
						unitRadius * scaleFactor + " " + startAngle + " " + extentAngle;
			}
			commands += bc + "line " + bcCutPoint.getX() + " " + bcCutPoint.getY();

		}
		
		commands = commands.substring(commands.lastIndexOf(bc) + 1, commands.length()) + commands.substring(0, commands.lastIndexOf(bc));
		commands += bc + "fill " + color.getRed() + " " + color.getGreen() + " " + color.getBlue();
		return commands;
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
	 * Sets this rounded polygon's corner radius to the given value.
	 * 
	 * @mutates | this
	 * 
	 * @throws IllegalArgumentException if argument {@code newRadius} is negative.
	 *    | newRadius < 0
	 * 
	 * @post This polygon's corner radius is equal to the given radius.
	 *    | this.getRadius() == newRadius
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
	 * @mutates | this
	 * @inspects | newVertices
	 * 
	 * @throws IllegalArgumentException if argument {@code newVertices} or one its elements is {@code null}.
	 *    | newVertices == null || 
	 *    | Arrays.stream(newVertices).anyMatch(e -> e == null)
	 * @throws IllegalArgumentException if {@code newVertices} do not define a proper polygon.
	 *    | PointArrays.checkDefinesProperPolygon(newVertices) != null
	 * 
	 * @post This polygon's vertices are equal to the elements of {@code newVertices}.
	 *    | IntStream.range(0, getVertices().length).allMatch(i -> 
	 *    |     this.getVertices()[i].equals(newVertices[i]))
	 */
	public void setVertices(IntPoint[] newVertices) {
		if (newVertices == null || Arrays.stream(newVertices).anyMatch(e -> e == null)) {
			throw new IllegalArgumentException("given vertices or one of its elements is null");
		}
		String polygonError = PointArrays.checkDefinesProperPolygon(newVertices);
		if (polygonError != null) {
			throw new IllegalArgumentException(polygonError);
		}
		
		vertices = newVertices.clone();
		updateBox();
	}
	
	/**
	 * Returns the color of this polygon.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color of this rounded polygon to the given color.
	 * 
	 * @mutates | this
	 * 
	 * @throws IllegalArgumentException if argument {@code color} is {@code null}.
	 *    | color == null
	 * 
	 * @post This polygon's color is equal to {@code color}.
	 *    | this.getColor() == color
	 */
	public void setColor(Color color) {
		if (color == null) {
			throw new IllegalArgumentException("given color is null");
		}
		
		this.color = color;
	}
	
	/**
	 * Returns the bounding box for this polygon.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 */
	public Extent getBoundingBox() {
		return box;
	}
}
