package drawit.shapes1;

import java.util.ArrayList;
import java.util.List;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.RoundedPolygon;
import drawit.shapegroups1.ShapeGroup;

/**
 * Each instance of this class stores a reference to a RoundedPolygon object and, 
 * optionally, a reference to a ShapeGroup object that contains it. 
 * We define this object's shape coordinate system as the referenced ShapeGroup object's inner coordinate system, 
 * if the stored ShapeGroup reference is non-null, or the global coordinate system otherwise. 
 * We interpret the polygon's vertex coordinates as being expressed in this object's shape coordinate system.
 * 
 * @immutable
 */
public class RoundedPolygonShape implements Shape {
	
	RoundedPolygon polygon;
	ShapeGroup parent;
	
	/**
	 * Initializes this object to store the given ShapeGroup reference (or null) and the given RoundedPolygon reference.
	 */
	public RoundedPolygonShape(ShapeGroup parent, RoundedPolygon polygon) {
		this.polygon = polygon;
		this.parent = parent;
	}
	
	/**
	 * Returns the RoundedPolygon reference stored by this object.
	 */
	public RoundedPolygon getPolygon() {
		return polygon;
	}
	
	/**
	 * Returns whether this polygon contains this point, given in shape coordinates.
	 */
	public boolean contains(IntPoint p) {
		return polygon.contains(p);
	}
	
	/**
	 * Returns this polygon's drawing commands.
	 */
	public String getDrawingCommands() {
		return polygon.getDrawingCommands();
	}
	
	/**
	 * Returns the ShapeGroup reference stored by this object.
	 */
	public ShapeGroup getParent() {
		return parent;
	}
	
	/**
	 * Returns one control point for each of this polygon's vertices. 
	 * If, after calling this method, a client mutates either the polygon or the shape group graph referenced by this object, 
	 * it shall no longer call any methods on the returned ControlPoint objects. 
	 * That is, any mutation of the polygon or the shape group graph referenced by this object 
	 * invalidates the ControlPoint objects returned by any preceding createControlPoints call. 
	 * This is true even if the mutation occurred through the returned ControlPoint objects themselves. 
	 * For example, after calling move on one of the returned ControlPoint objects, 
	 * a client is no longer allowed to call getLocation or remove on any of the returned ControlPoint objects, 
	 * and after calling remove on one of the returned ControlPoint objects, 
	 * a client is no longer allowed to call getLocation or move on any of the returned ControlPoint objects. 
	 * There is one exception: a client can perform any number of consecutive move calls on the same ControlPoint object.
	 */
	public ControlPoint[] createControlPoints() {
		List<ControlPoint> result = new ArrayList<ControlPoint>();
		IntPoint[] vertices = polygon.getVertices();
		for (int i = 0 ; i < vertices.length ; i++) {
			final int j = i;	// Is dit wel een goede methode?
			result.add(new ControlPoint() {				
				public IntPoint getLocation() {
					return toShapeCoordinates(vertices[j]); //TODO: Checken of deze toShapeCoordinates wel mag hier
				}
				public void move(IntVector delta) {
					IntPoint newVertex = vertices[j].plus(delta);
					polygon.update(j, newVertex);
				}
				public void remove() {
					polygon.remove(j);
				}
			});
		}
		return (ControlPoint[]) result.toArray();
	}
	
	/**
	 * Given the coordinates of a point in the global coordinate system, 
	 * returns the coordinates of the point in the shape coordinate system.
	 */
	public IntPoint toShapeCoordinates(IntPoint p) {
		if(parent == null) {
			return p;
		} else {
			return parent.toInnerCoordinates(p);
		}
	}
	
	/**
	 * Given the coordinates of a point in the shape coordinate system, 
	 * returns the coordinates of the point in the global coordinate system.
	 */
	public IntPoint toGlobalCoordinates(IntPoint p) {
		if(parent == null) {
			return p;
		} else {
			return parent.toGlobalCoordinates(p);
		}
	}
}
