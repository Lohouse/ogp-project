package drawit;

/**
 * An instance of this class is a mutable abstraction storing a rounded polygon defined
 * by a set of 2D points with integer coordinates and a nonnegative corner radius.
 */
public class RoundedPolygon {
	
	private int radius;
	private IntPoint[] vertices;
	
	/**
	 * Initializes an unrounded (radius 0) and empty (no intial vertices) polygon.
	 * 
	 * @post This polygon will initially not contain any vertices.
	 *    | this.getVertices().length == 0
	 * @post This polygon will initially have a radius of 0.
	 *    | this.getRadius() == 0
	 */
	public RoundedPolygon() {
		vertices = new IntPoint[]{};
		radius = 0;
	}
	
	/**
	 * Returns true if the given point is contained by the (non-rounded) polygon defined by this rounded polygon's vertices.
	 * This method does not take into account this rounded polygon's corner radius; it assumes a corner radius of zero.
	 * A point is contained by a polygon if it coincides with one of its vertices, or if it is on one of its edges,
	   or if it is in the polygon's interior.
	 */
	public boolean contains(IntPoint point) {
		boolean contains = false;
		for (int i = 0; i < vertices.length; i++) {
			int j = (i + 1) % vertices.length;
			IntPoint p1 = vertices[i];
			IntPoint p2 = vertices[j];
			
			if (p1.getY() == p2.getY()) {
				continue;
			}
			
			int intersectX = p1.getX() + (p2.getX() - p1.getX()) * (point.getY() - p1.getY()) / (p2.getY() - p1.getY());
			if (point.getX() == intersectX) {
				return true;
			}
			
			if ((p1.getY() > point.getY()) != (p2.getY() > point.getY()) && (point.getX() < intersectX)) {
				contains = !contains;
			}
		}
		return contains;
	}
	
	/**
	 * Changes the point at the given index in vertices to the given point.
	 * 
	 * @pre The given index is less than the current amount of vertices in this polygon.
	 *    | index < this.getVertices().length
	 */
	public void update(int index, IntPoint point) {
		vertices[index] = point;
	}
	
	/**
	 * Inserts a given point into the existing vertices at the specified index.
	 * 
	 * @pre The given index is less than or equal to the current amount of vertices in this polygon.
	 *    | index <= this.getVertices().length
	 */
	public void insert(int index, IntPoint point) {
		IntPoint[] newVertices = new IntPoint[vertices.length + 1];
		
		for (int i = 0; i < index; i++) {
			newVertices[i] = vertices[i];
		}
		newVertices[index] = point;
		for (int i = index; i < vertices.length; i++) {
			newVertices[i + 1] = vertices[i];
		}
		
		vertices = newVertices;
	}
	
	/**
	 * Removes the point from vertices at the given index.
	 * 
	 * @pre The given index is less than the current amount of vertices in this polygon.
	 *    | index < this.getVertices().length
	 */
	public void remove(int index) {
		IntPoint[] newVertices = new IntPoint[vertices.length - 1];
		
		for (int i = 0; i < index; i++) {
			newVertices[i] = vertices[i];
		}
		for (int i = index; i < newVertices.length; i++) {
			newVertices[i] = vertices[i + 1];
		}
		
		vertices = newVertices;
	}
	
	/**
	 * Returns a textual representation of a set of drawing commands for drawing this rounded polygon.
     * The returned text consists of a sequence of drawing operators and arguments, separated by spaces. 
       The drawing operators are line and arc. Each argument is a decimal representation of a floating-point number.
     * Operator line takes four arguments: X1 Y1 X2 Y2; it draws a line between (X1, Y1) and (X2, Y2). 
     * Operator arc takes five arguments: X Y R S E. It draws a part of a circle. The circle is defined by its center (X, Y) 
       and its radius R. The part to draw is defined by the start angle A and angle extent E, both in radians. 
       Positive X is angle zero; positive Y is angle Math.PI / 2; negative Y is angle -Math.PI / 2.
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
			DoubleVector bisectorVector = baVector.plus(bcVector);
			DoubleVector baUnitVector = new DoubleVector(baVector.getX() / baVector.getSize(), baVector.getY() / baVector.getSize());
			DoubleVector bisectorUnitVector = new DoubleVector(bisectorVector.getX() / bisectorVector.getSize(), 
					bisectorVector.getY() / bisectorVector.getSize());
			
			double unitCutOff = baUnitVector.dotProduct(bisectorUnitVector);
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
			
			commands += " " + baCutPoint.getX() + " " + baCutPoint.getY() + bc + "arc " + cornerCenterPoint.getX() + " "
					+ cornerCenterPoint.getY() + " " + radius + " " + startAngle + " " + extentAngle + bc + "line " + bcCutPoint.getX() + " " + bcCutPoint.getY();

		}
		return commands.substring(commands.lastIndexOf(bc) + 1, commands.length()) + commands.substring(0, commands.lastIndexOf(bc));
	}
	
	/**
	 * Returns the radius of the corners of this rounded polygon.
	 */
	public int getRadius() {
		return radius;
	}
	
	/**
	 * Sets this rounded polygon's corner radius to the given value.
	 * 
	 * @throws IllegalArgumentException
	 *      The the given radius is negative.
	 *    | newRadius < 0
	 */
	public void setRadius(int newRadius) {
		if (newRadius < 0) {
			throw new IllegalArgumentException("negative radius");
		}
		
		radius = newRadius;
	}
	
	/**
	 * Returns a new array whose elements are the vertices of this rounded polygon.
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
	 */
	public void setVertices(IntPoint[] newVertices) {
		String polygonError = PointArrays.checkDefinesProperPolygon(newVertices);
		if (polygonError != null) {
			throw new IllegalArgumentException(polygonError);
		}
		
		vertices = newVertices.clone();
	}
}
