package drawit.shapegroups1;

import drawit.IntPoint;

public class Extent {
	
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	private Extent(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public int getWidth() {
		return (right - left);
	}
	
	public int getHeight() {
		return (bottom - top);
	}
	
	public IntPoint getTopLeft() {
		return new IntPoint(left, top);
	}
	
	public IntPoint getBottomRight() {
		return new IntPoint(right, bottom);
	}
	
	public boolean contains(IntPoint point) {
		int x = point.getX();
		int y = point.getY();
		
		if(x >= left && x <= right && y >= top && y <= bottom) {
			return true;
		}
		return false;
	}
	
	public static Extent ofLeftTopWidthHeight(int left, int top, int width, int height) {
		return new Extent(left, top, left + width, top + height);
	}
	
	public static Extent ofLeftTopRightBottom(int left, int top, int right, int bottom) {
		return new Extent(left, top, right, bottom);
	}
	
	public Extent withLeft(int newLeft) {
		return new Extent(newLeft, top, right, bottom);
	}
	
	public Extent withTop(int newTop) {
		return new Extent(left, newTop, right, bottom);
	}
	
	public Extent withRight(int newRight) {
		return new Extent(left, top, newRight, bottom);
	}
	
	public Extent withBottom(int newBottom) {
		return new Extent(left, top, right, newBottom);
	}
	
	public Extent withWidth(int newWidth) {
		return new Extent(left, top, left + newWidth, bottom);
	}
	
	public Extent withHeight(int newHeight) {
		return new Extent(left, top, right, top + newHeight);
	}
}
