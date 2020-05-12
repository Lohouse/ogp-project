package drawit;

public interface RoundedPolygonContainsTestStrategy {
	
	/**
	 * Returns {@code true} iff point is contained by polygon.
	 * 
	 * @inspects | polygon
	 * 
	 * @post The result is {@code true} iff point is contained by the polygon's bounding box. //TODO: Fast will not strengthen this condition, is this OK?
	 *    //TODO: | result == polygon.getBoundingBox().contains(point) geeft rare eclipse builder foutmelding
	 */
	boolean contains(RoundedPolygon polygon, IntPoint point);
	
}
