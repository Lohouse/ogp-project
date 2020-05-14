package drawit;

public class PreciseRoundedPolygonContainsTestStrategy implements RoundedPolygonContainsTestStrategy {
	
	/**
	 * Returns {@code true} iff point is contained by polygon according to the precise method.
	 * 
	 * @inspects | polygon
	 * 
	 * @post The result is {@code true} iff point is contained by the polygon's vertices.
	 *    | result == polygon.contains(point)
	 */
	public boolean contains(RoundedPolygon polygon, IntPoint point) {
		if (polygon == null) {
			throw new IllegalArgumentException("polygon is null");
		}
		if (point == null) {
			throw new IllegalArgumentException("point is null");
		}
		
		return polygon.contains(point);
	}
	
}
