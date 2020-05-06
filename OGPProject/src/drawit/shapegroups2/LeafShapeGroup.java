package drawit.shapegroups2;

import java.util.Arrays;

import drawit.IntPoint;
import drawit.RoundedPolygon;

/**
 * Each instance of this class represents a leaf shape group that defines a
 * transformation (i.e. a displacement and/or a horizontal and/or vertical scaling) 
 * of the shapes it contains.
 * A leaf shape group contains a single shape.
 * 
 * @invar The shape is not equal to {@code null}.
 * 	  | getShape() != null
 */
public class LeafShapeGroup extends ShapeGroup {
	
	/**
	 * @invar | shape != null
	 */
	RoundedPolygon shape;
	
	
	//TODO: Add @mutates_properties | this. Currently gives compilation errors when added.
	/**
	 * Initializes this object to represent a leaf shape group that directly contains the given shape.
	 * 
	 * @inspects | shape
	 * 
	 * @throws IllegalArgumentException if argument {@code shape} is null.
	 *    | shape == null
	 *    
	 * @post This extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getExtent().getLeft() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt() &&
	 *    |	getExtent().getTop() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt() && 
	 *    |	getExtent().getRight() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt() &&
	 *    |	getExtent().getBottom() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt()
	 * @post This original extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getOriginalExtent().getLeft() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt() &&
	 *    |	getOriginalExtent().getTop() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt() &&
	 *    |	getOriginalExtent().getRight() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt() &&
	 *    |	getOriginalExtent().getBottom() == Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt()
	 * @post This shape equals the given {@code shape}
	 *    | getShape() == shape
	 * @post This parent shape group equals {@code null}
	 *    | getParentGroup() == null
	 */
	public LeafShapeGroup(RoundedPolygon shape) {
		super();
		
		if (shape == null) {
			throw new IllegalArgumentException("argument shape is null");
		}

		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		for (IntPoint vertex : shape.getVertices()) {
			if (vertex.getX() > right) {
				right = vertex.getX();
			}
			if (vertex.getX() < left) {
				left = vertex.getX();
			}
			if (vertex.getY() < top) {
				top = vertex.getY();
			}
			if (vertex.getY() > bottom) {
				bottom = vertex.getY();
			}
		}
		
		this.extent = this.originalExtent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = shape;
	}
	
	/**
	 * Returns the shape directly contained by this shape group.
	 * 
	 * @basic
	 */
	public RoundedPolygon getShape() {
		return shape;
	}
}
