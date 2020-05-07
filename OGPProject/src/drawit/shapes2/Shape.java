package drawit.shapes2;

import drawit.IntPoint;
import drawit.shapegroups2.ShapeGroup;
import drawit.shapes2.ControlPoint;


/**
 * Interface that generalizes classes RoundedPolygonShape and ShapeGroupShape.
 */
public interface Shape {
	
	 ShapeGroup getParent();

	 boolean contains(IntPoint p);
	 
	 String getDrawingCommands();
	 
	 /**
	  * Given the coordinates of a point in the global coordinate system, 
	  * returns the coordinates of the point in the shape coordinate system.
	  */
	 IntPoint toShapeCoordinates(IntPoint p);
	 
	 /**
	  * Given the coordinates of a point in the shape coordinate system, 
	  * returns the coordinates of the point in the global coordinate system.
	  */
	 IntPoint toGlobalCoordinates(IntPoint p);
	 
	 ControlPoint[] createControlPoints();	 
}
