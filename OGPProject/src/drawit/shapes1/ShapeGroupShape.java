package drawit.shapes1;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.shapegroups1.Extent;
import drawit.shapegroups1.ShapeGroup;

/**
 * Each instance of this class stores a reference to a ShapeGroup object. 
 * We define this object's shape coordinate system as its shape group's outer coordinate system.
 * 
 * @immutable
 */
public class ShapeGroupShape implements Shape {
	
	ShapeGroup group;

	/**
	 * Initializes this object to store the given ShapeGroup reference.
	 */
	public ShapeGroupShape(ShapeGroup group) {
		this.group = group;
	}
	
	/**
	 * Returns the ShapeGroup reference stored by this object.
	 */
	public ShapeGroup getShapeGroup() {
		return group;
	}
	
	/**
	 * Returns whether this shape group's extent contains the given point, expressed in shape coordinates.
	 */
	public boolean contains(IntPoint p) {
		return group.getExtent().contains(p);
	}
	
	/**
	 * Returns this shape group's drawing commands.
	 */
	public String getDrawingCommands() {
		return group.getDrawingCommands();
	}
	
	/**
	 * Returns this shape group's parent, or null if it has no parent.
	 */
	public ShapeGroup getParent() {
		return group.getParentGroup();
	}
	
	/**
	 * Returns one control point for this shape group's upper-left corner, 
	 * and one control point for its lower-right corner. If, after calling this method, 
	 * a client mutates the shape group graph referenced by this object, 
	 * it shall no longer call any methods on the returned ControlPoint objects. 
	 * That is, any mutation of the shape group graph referenced by this object invalidates 
	 * the ControlPoint objects returned by any preceding createControlPoints call. 
	 * This is true even if the mutation occurred through the returned ControlPoint objects themselves. 
	 * For example, after calling move on one of the returned ControlPoint objects, 
	 * a client is no longer allowed to call getLocation or remove on any of the returned ControlPoint objects, 
	 * and after calling remove on one of the returned ControlPoint objects, 
	 * a client is no longer allowed to call getLocation or move on any of the returned ControlPoint objects. 
	 * There is one exception: a client can perform any number of consecutive move calls on the same ControlPoint object.
	 */
	public ControlPoint[] createControlPoints() {		
		Extent ext = group.getExtent();
		
		return new ControlPoint[] {		
			new ControlPoint() { //TODO: Deze extent nog converteren naar het shape coordinate system?
				public IntPoint getLocation() {
					return ext.getTopLeft(); //TODO: Checken of deze toShapeCoordinates wel mag hier
				}
				public void move(IntVector delta) {
					group.setExtent(Extent.ofLeftTopRightBottom(ext.getLeft() + delta.getX(),
							ext.getTop() + delta.getY(),
							ext.getRight(),
							ext.getBottom()));
				}
				public void remove() {
					throw new IllegalArgumentException("Can't remove the ControlPoint of a ShapeGroup");
				}
			},
			new ControlPoint() { //TODO: Deze extent nog converteren naar het shape coordinate system?
				public IntPoint getLocation() {
					return ext.getBottomRight(); //TODO: Checken of deze toShapeCoordinates wel mag hier
				}
				public void move(IntVector delta) {
					group.setExtent(Extent.ofLeftTopRightBottom(ext.getLeft(),
							ext.getTop(),
							ext.getRight() + delta.getX(),
							ext.getBottom() + delta.getY()));
				}
				public void remove() {
					throw new IllegalArgumentException("Can't remove the ControlPoint of a ShapeGroup");
				}
			}
		};
	}
	
	//TODO: Wat als de extent van de parent group verandert?
	
	/**
	 * Given the coordinates of a point in the global coordinate system, 
	 * returns the coordinates of the point in the shape coordinate system.
	 */
	public IntPoint toShapeCoordinates(IntPoint p) {
		if (group.getParentGroup() == null)
		{
			return p;
		}
		
		return group.getParentGroup().toInnerCoordinates(p);
	}
	
	/**
	 * Given the coordinates of a point in the shape coordinate system, 
	 * returns the coordinates of the point in the global coordinate system.
	 */
	public IntPoint toGlobalCoordinates(IntPoint p) {
		if (group.getParentGroup() == null)
		{
			return p;
		}
		
		return group.getParentGroup().toGlobalCoordinates(p);		
	}
	
}
