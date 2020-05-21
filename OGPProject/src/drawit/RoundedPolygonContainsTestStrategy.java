package drawit;

public interface RoundedPolygonContainsTestStrategy {
	
	/**
	 * Returns {@code true} iff point is contained by polygon.
	 * 
	 * @inspects | polygon
	 * 
	 * @post The result is {@code true} iff point is contained by the polygon's bounding box.
	 *    | result == polygon.getBoundingBox().contains(point)
	 */
	boolean contains(RoundedPolygon polygon, IntPoint point);
	
}
