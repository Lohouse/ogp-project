package drawit.shapes1;

import java.util.ArrayList;
import java.util.List;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.shapegroups1.ShapeGroup;

/**
 * Each instance of this class stores a reference to a ShapeGroup object. 
 * We define this object's shape coordinate system as its shape group's outer coordinate system.
 * 
 * @immutable
 */
public class ShapeGroupShape {
	
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
		//TODO
		
		throw new RuntimeException("not implemented");
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
		class PointControl implements ControlPoint {
			IntPoint p;
			
			public PointControl(IntPoint p) {
				this.p = p;
			}	
			public IntPoint getLocation() {
				return p; //Juiste coordinate system?
			}
			public void move(IntVector delta) {
				p.plus(delta);
			}
			public void remove() {
				// Niet van toepassing?
			}
		}
		

		//TODO
		
		throw new RuntimeException("not implemented");
	}
	
	/**
	 * Given the coordinates of a point in the global coordinate system, 
	 * returns the coordinates of the point in the shape coordinate system.
	 */
	public IntPoint toShapeCoordinates(IntPoint p) {
		//TODO
		
		throw new RuntimeException("not implemented");		
	}
	
	/**
	 * Given the coordinates of a point in the shape coordinate system, 
	 * returns the coordinates of the point in the global coordinate system.
	 */
	public IntPoint toGlobalCoordinates(IntPoint p) {
		//TODO
		
		throw new RuntimeException("not implemented");		
	}
	
}
