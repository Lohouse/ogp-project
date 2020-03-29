package drawit.shapegroups2;

import drawit.IntPoint;

//TODO: Immutable object?
/**
 * Each instance of this class represents a nonempty rectangular area in a 2D coordinate system, 
 * whose edges are parallel to the coordinate axes.
 */
public class Extent {
	
	private int left;
	private int top;
	private int width;
	private int height;
	
	private Extent(int left, int top, int width, int height) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return left + width;
	}
	
	public int getBottom() {
		return top + height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public IntPoint getTopLeft() {
		return new IntPoint(left, top);
	}
	
	public IntPoint getBottomRight() {
		return new IntPoint(getRight(), getBottom());
	}
	
	public boolean contains(IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("argument point is null");
		}
		
		int x = point.getX();
		int y = point.getY();
		
		if(x >= left && x <= getRight() && y >= top && y <= getBottom()) {
			return true;
		}
		return false;
	}
	
	public static Extent ofLeftTopWidthHeight(int left, int top, int width, int height) {
		return new Extent(left, top, width, height);
	}
	
	public static Extent ofLeftTopRightBottom(int left, int top, int right, int bottom) {
		return new Extent(left, top, right - left, bottom - top);
	}
	
	public Extent withLeft(int newLeft) {
		return new Extent(newLeft, top, width, height);
	}
	
	public Extent withTop(int newTop) {
		return new Extent(left, newTop, width, height);
	}
	
	public Extent withRight(int newRight) {
		return new Extent(left, top, newRight - left, height);
	}
	
	public Extent withBottom(int newBottom) {
		return new Extent(left, top, width, newBottom - top);
	}
	
	public Extent withWidth(int newWidth) {
		return new Extent(left, top, newWidth, height);
	}
	
	public Extent withHeight(int newHeight) {
		return new Extent(left, top, width, newHeight);
	}
}