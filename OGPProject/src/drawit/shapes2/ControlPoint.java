package drawit.shapes2;

import drawit.IntPoint;
import drawit.IntVector;

public interface ControlPoint {
	/**
	 * The location of this control point, in shape coordinates.
	 */
	 IntPoint getLocation();
	 
	 /**
     * Mutate the shape so that this control point's location, expressed in global coordinates, equals its initial location 
     * (i.e. the location at the time of the getControlPoints() call through which this ControlPoint object was obtained) 
     * plus the given vector.
	 */
	 void move(IntVector delta);
	 
	 /**
	 * If this control point corresponds to a polygon vertex, remove the vertex from the polygon's list of vertices. 
	 * Otherwise, throw an UnsupportedOperationException.
     */
	 void remove();
}
