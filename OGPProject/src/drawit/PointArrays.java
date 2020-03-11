package drawit;

import java.util.stream.IntStream;

/**
 * Declares a number of methods useful for working with arrays of IntPoint objects.
 */
public class PointArrays {
	
	private PointArrays() {}
	
	/**
	 * Returns null if the given array of points defines a proper polygon; otherwise, returns a string describing why it does not.
	 * 
	 * @inspects | points
	 * 
	 * @pre {@code points} is not {@code null}
	 *    | points != null
	 * 
	 * @post 
	 *      The result is {@code null} if the elements of {@code points} define a proper polygon. 
	 *      The result is a {@code String} if {@code points} contains exactly 2 vertices, or if 2 vertices coincide, 
	 *      or if a vertex lies on an edge, or if 2 edges intersect.
	 *    | points.length == 2 || 
	 *    | IntStream.range(0, points.length).anyMatch(i -> 
	 *    |		IntStream.range(0, points.length).anyMatch(x -> 
	 *    | 		i != x && ((points[i].equals(points[(i + 1) % points.length]) || (i != x && points[i].equals(points[x])) ||
	 *    | 		(i != (x + 1) % points.length && points[i].equals(points[(x + 1) % points.length])) || 
	 *    |			points[x].equals(points[(x + 1) % points.length]) || ((i + 1) % points.length != x && points[(i + 1) % points.length].equals(points[x])) ||
	 *    |			((i + 1) % points.length != (x + 1) % points.length && points[(i + 1) % points.length].equals(points[(x + 1) % points.length]))) ||
	 *    |			(points[i].isOnLineSegment(points[x], points[(x + 1) % points.length]) || points[(i + 1) % points.length].isOnLineSegment(points[x], points[(x + 1) % points.length]) ||
	 *    |			points[x].isOnLineSegment(points[i], points[(i + 1) % points.length]) || points[(x + 1) % points.length].isOnLineSegment(points[i], points[(i + 1) % points.length])) ||
	 *    |			IntPoint.lineSegmentsIntersect(points[i], points[(i + 1) % points.length], points[x], points[(x + 1) % points.length]))
	 *    |     )
	 *    | ) ?
	 *    | 	result instanceof String : 
	 *    | 	result == null
	 */
	public static String checkDefinesProperPolygon(IntPoint[] points) {
		if (points.length == 2) {
			return "2 vertices do not define a proper polygon";
		}
		
		for (int i = 0; i < points.length; i++) {
			int j = (i + 1) % points.length;
			IntPoint p1 = points[i];
			IntPoint p2 = points[j];
			
			for (int x = 0; x < points.length; x++) {
				if (i == x) {
					continue;
				}
				
				int y = (x + 1) % points.length;
				IntPoint pA = points[x];
				IntPoint pB = points[y];				
				
				if (p1.equals(p2)) {
					return "Vertices at index " + i + " and " + j + " coincide: (" + p1.getX() + ", " + p1.getY() + ")";
				}
				if (i != x && p1.equals(pA)) {
					return "Vertices at index " + i + " and " + x + " coincide: (" + p1.getX() + ", " + p1.getY() + ")";					
				}
				if (i != y && p1.equals(pB)) {
					return "Vertices at index " + i + " and " + y + " coincide: (" + p1.getX() + ", " + p1.getY() + ")";					
				}
				if (pA.equals(pB)) {
					return "Vertices at index " + x + " and " + y + " coincide: (" + pA.getX() + ", " + pA.getY() + ")";					
				}
				if (j != x && p2.equals(pA)) {
					return "Vertices at index " + j + " and " + x + " coincide: (" + pA.getX() + ", " + pA.getY() + ")";						
				}
				if (j != y && p2.equals(pB)) {
					return "Vertices at index " + j + " and " + y + " coincide: (" + pB.getX() + ", " + pB.getY() + ")";						
				}
				
				if (p1.isOnLineSegment(pA, pB)) {
					return "Vertex (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " lies on edge from (" + pA.getX() + 
							", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
				}
				if (p2.isOnLineSegment(pA, pB)) {
					return "Vertex (" + p2.getX() + ", " + p2.getY() + ") at index " + j + " lies on edge from (" + pA.getX() + 
							", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
				}
				if (pA.isOnLineSegment(p1, p2)) {
					return "Vertex (" + pA.getX() + ", " + pA.getY() + ") at index " + x + " lies on edge from (" + p1.getX() + 
							", " + p1.getY() + ") at index " + i + " to (" + p2.getX() + ", " + p2.getY() + ") at index " + j;
				}
				if (pB.isOnLineSegment(p1, p2)) {
					return "Vertex (" + pB.getX() + ", " + pB.getY() + ") at index " + y + " lies on edge from (" + p1.getX() + 
							", " + p1.getY() + ") at index " + i + " to (" + p2.getX() + ", " + p2.getY() + ") at index " + j;
				}
				
				if (IntPoint.lineSegmentsIntersect(p1, p2, pA, pB)) {
					return "Edge from (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " to (" + p2.getX() + ", " + p2.getY() + ") at index " + j
							+ " intersects with edge from (" + pA.getX() + ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a new array with the same contents as the given array.
	 * 
	 * @creates | result
	 * @inspects | points
	 * 
	 * @pre {@code points} is not {@code null}.
	 *    | points != null
	 * 
	 * @post The result has the same size as points.
	 *    | result.length == points.length
	 * @post Each element of the result is equal to the corresponding element of {@code points}.
	 *    | IntStream.range(0, points.length).allMatch(i -> 
	 *    |     result[i].equals(points[i]))
	 */
	public static IntPoint[] copy(IntPoint[] points) {
		return points.clone();
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the element at the given index replaced by the given point.
	 * 
	 * @creates | result
	 * @inspects | points
	 * 
	 * @pre {@code points} is not {@code null}.
	 *    | points != null
	 * @pre {@code index} must be greater than or equal to zero, {@code index} must be smaller than the amount of elements in {@code points}.
	 *    | index >= 0 &&
	 *    | index < points.length
	 * 
	 * @post The result has the same size as {@code points}.
	 *    | result.length == points.length
	 * @post Each element of the result, except at index {@code index}, is the same as the corresponding element of {@code points}.
	 *    | IntStream.range(0, result.length).allMatch(i -> 
	 *    |     i == index || result[i].equals(points[i]))
	 * @post The element of the result at index {@code index} is equal to {@code value}.
	 *    | result[index].equals(value)
	 */
	public static IntPoint[] update(IntPoint[] points, int index, IntPoint value) {
		IntPoint[] newArray = copy(points);
		newArray[index] = value;
		return newArray;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the given point inserted at the given index.
	 * 
	 * @creates | result
	 * @inspects | points
	 * 
	 * @pre {@code points} is not {@code null}.
	 *    | points != null
	 * @pre {@code index} must be greater than or equal to zero, {@code index} must be smaller than or equal to the amount of elements in {@code points}.
	 *    | index >= 0 &&
	 *    | index <= points.length
	 * 
	 * @post The result has the same size as {@code points}, plus 1.
	 *    | result.length == points.length + 1
	 * @post Each element of the result, before index {@code index}, is the same as the corresponding element of {@code points}.
	 *    | IntStream.range(0, index).allMatch(i -> 
	 *    |     result[i].equals(points[i]))
	 * @post The element of the result at index {@code index} is equal to {@code point}.
	 *    | result[index].equals(point)
	 * @post Each element of the result, after index {@code index}, is the same as the element of {@code points} at (the corresponding position - 1)
	 *    | IntStream.range(index + 1, result.length).allMatch(i -> 
	 *    |     result[i].equals(points[i - 1]))
	 */
	public static IntPoint[] insert(IntPoint[] points, int index, IntPoint point) {
		IntPoint[] newArray = new IntPoint[points.length + 1];
		boolean insertDone = false;
		
		for(int i = 0; i < points.length + 1; i++) {
			if(i == index) {
				newArray[i] = point;
				insertDone = true;
			} else if(!insertDone) {
				newArray[i] = points[i];
			} else {
				newArray[i] = points[i - 1];
			}
		}
		return newArray;
	}

	/**
	 * Returns a new array whose elements are the elements of the given array with the element at the given index removed.
	 * 
	 * @creates | result
	 * @inspects | points
	 * 
	 * @pre {@code points} is not {@code null}.
	 *    | points != null
	 * @pre {@code index} must be greater than or equal to zero, {@code index} must be smaller than the amount of elements in {@code points}.
	 *    | index >= 0 &&
	 *    | index < points.length
	 * 
	 * @post The result has the same size as {@code points}, minus 1.
	 *    | result.length == points.length - 1
	 * @post Each element of the result, before index {@code index}, is the same as the corresponding element of {@code points}.
	 *    | IntStream.range(0, index).allMatch(i -> 
	 *    |     result[i].equals(points[i]))
	 * @post Each element of the result, starting from index {@code index}, is the same as the element of {@code points} at (the corresponding position + 1)
	 *    | IntStream.range(index, result.length).allMatch(i -> 
	 *    |     result[i].equals(points[i + 1]))
	 */
	public static IntPoint[] remove(IntPoint[] points, int index) {
		IntPoint[] newArray = new IntPoint[points.length - 1];
		boolean removeDone = false;
		
		for(int i = 0; i < points.length; i++) {
			if(i == index) {
				removeDone = true;
			} else if(!removeDone) {
				newArray[i] = points[i];
			} else {
				newArray[i - 1] = points[i];
			}
		}
		return newArray;
	}	
}
