package drawit;

public class FastRoundedPolygonContainsTestStrategy implements RoundedPolygonContainsTestStrategy {
	
	/**
	 * Returns {@code true} iff point is contained by polygon according to the fast method.
	 * 
	 * @inspects | polygon
	 * 
	 * @pre Argument {@code polygon} is not {@code null}.
     *    | polygon != null
     * @pre Argument {@code point} is not {@code null}.
     *    | point != null
     *    
	 * @post The result is {@code true} iff point is contained by the polygon's bounding box.
	 *    | result == polygon.getBoundingBox().contains(point)
	 */
	public boolean contains(RoundedPolygon polygon, IntPoint point) {
		return polygon.getBoundingBox().contains(point);
	}
}
