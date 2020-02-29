package drawit;

public class PointArrays {
	
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
						+ ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getX() + " at index " + y;
					}
				} else {
					int intersectX = pA.getX() + (pB.getX() - pA.getX()) * (p1.getY() - pA.getY()) / (pB.getY() - pA.getY());
					if (p1.getX() == intersectX) {
						return "Vertex (" + p1.getX() + ", " + p1.getY() + ") at index " + i + " lies on edge from (" + pA.getX() 
								+ ", " + pA.getY() + ") at index " + x + " to (" + pB.getX() + ", " + pB.getX() + " at index " + y;
					}				
				}			
			}
		}
		
		return null;
	}
	
	public static IntPoint[] copy(IntPoint[] points) {
		IntPoint[] newArray = new IntPoint[points.length];
		
		for(int i = 0; i < points.length; i++) {
			newArray[i] = points[i];
		}
		return newArray;
	}
	
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
