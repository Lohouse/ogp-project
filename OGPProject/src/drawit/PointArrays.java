package drawit;

import java.util.Arrays;

/**
 * Declares a number of methods useful for working with arrays of IntPoint objects.
 */
public class PointArrays {
	
	private PointArrays() {}
	
	/**
	 * Returns null if the given array of points defines a proper polygon; otherwise, returns a string describing why it does not.
	 */
	public static String checkDefinesProperPolygon(IntPoint[] points) {
		if (points.length < 3) {
			return "Less than 3 vertices";
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
				
				if (p1.equals(pA)) {
					return "Vertices at index " + i + " and " + x + " coincide: (" + points[i].getX() + ", " + points[i].getY() + ")";
				}
				
				if (IntPoint.lineSegmentsIntersect(p1, p2, pA, pB)) {
					return "Edge from (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " to (" + p2.getX() + ", " + p2.getY() + ") at index " + j
							+ " intersects with edge from (" + pA.getX() + ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
				}
				
				if (i == y) {
					continue;
				}
				
				if (pA.getY() == pB.getY()) {
					if (p1.getY() == pA.getY() && ((p1.getX() > pA.getX() && p1.getX() < pB.getX())
							|| (p1.getX() > pB.getX() && p1.getX() < pA.getX()))) {
						return "Vertex (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " lies on edge from (" + pA.getX() 
						+ ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
					}
				} else if ((p1.getY() > pA.getY() && p1.getY() < pB.getY())
							|| (p1.getY() > pB.getY() && p1.getY() < pA.getY())){
					int intersectX = pA.getX() + (pB.getX() - pA.getX()) * (p1.getY() - pA.getY()) / (pB.getY() - pA.getY());
					if (p1.getX() == intersectX) {
						return "Vertex (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " lies on edge from (" + pA.getX() 
								+ ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getY() + ") at index " + y;
					}				
				}			
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a new array with the same contents as the given array.
	 * 
	 * @post The result has the same size as points.
	 *    | result.length == points.length
	 * @post Each element of result is the same as the corresponding element of points.
	 *    | Arrays.equals(result, points) == true
	 */
	public static IntPoint[] copy(IntPoint[] points) {
		IntPoint[] newArray = new IntPoint[points.length];
		
		for(int i = 0; i < points.length; i++) {
			newArray[i] = points[i];
		}
		return newArray;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the element at the given index replaced by the given point.
	 * @pre index must be greater than or equal to zero, index must be smaller than or equal to the amount of elements.
	 *    | index >= 0 &&
	 *    | index <= points.length
	 * @post The result has the same size as points.
	 *    | result.length == points.length
	 * @post Each element of result, before index, is the same as the corresponding element of points.
	 *    | Arrays.equals(result, 0, index - 1, points, 0, index - 1) == true
	 * @post The element of result at index 'index' equals the given value.
	 *    | result[index] == value
	 * @post Each element of result, after index, is the same as the corresponding element of points.
	 *    | Arrays.equals(result, index + 1, result.length, points, index + 1, result.length) == true	  	 
	 */
	public static IntPoint[] update(IntPoint[] points, int index, IntPoint value) {
		IntPoint[] newArray = new IntPoint[points.length];
		
		for(int i = 0; i < points.length; i++) {
			if(i == index) {
				newArray[i] = value;
			} else {
				newArray[i] = points[i];
			}
		}
		return newArray;
	}
	
	/**
	 * Returns a new array whose elements are the elements of the given array with the given point inserted at the given index.
	 * @pre index must be greater than or equal to zero, index must be smaller than or equal to the amount of elements.
	 *    | index >= 0 &&
	 *    | index <= points.length
	 * @post The result has the same size as points, plus 1.
	 *    | result.length == points.length + 1
	 * @post Each element of result, before index, is the same as the corresponding element of points.
	 *    | Arrays.equals(result, 0, index - 1, points, 0, index - 1) == true
	 * @post The element of result at index 'index' equals the given value.
	 *    | result[index] == point
	 * @post Each element of result, after index, is the same as the element of points at (the corresponding position - 1)
	 *    | Arrays.equals(result, index + 1, result.length, points, index, result.length - 1) == true	
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
	 * @pre index must be greater than or equal to zero, index must be smaller than or equal to the amount of elements.
	 *    | index >= 0 &&
	 *    | index <= points.length
	 * @post The result has the same size as points, minus 1.
	 *    | result.length == points.length - 1
	 * @post Each element of result, before index, is the same as the corresponding element of points.
	 *    | Arrays.equals(result, 0, index - 1, points, 0, index - 1) == true
	 * @post Each element of result, starting from index, is the same as the element of points at (the corresponding position + 1)
	 *    | Arrays.equals(result, index, result.length, points, index + 1, result.length + 1) == true	
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
