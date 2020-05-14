package drawit.shapegroups1;

import drawit.IntPoint;

/**
 * Each instance of this class represents a nonempty rectangular area in a 2D coordinate system, 
 * whose edges are parallel to the coordinate axes.
 * 
 * @immutable
 * @invar This objects left coordinate is smaller than the right coordinate
 *    | getLeft() < getRight()
 * @invar This objects top coordinate is smaller than the bottom coordinate
 *    | getTop() < getBottom()
 * @invar This objects width and height are greater than zero
 *    | getWidth() > 0 && getHeight() > 0
 */
public class Extent {

	 /**
     * @invar | left < right
     * @invar | top < bottom
     */
	private final int left;
	private final int top;
	private final int right;
	private final int bottom;
	
	private Extent(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	/**
	 * Returns the X coordinate of the edge parallel to the Y axis with the smallest X coordinate.
	 */
	public int getLeft() {
		return left;
	}
	
	/**
	 * Returns the Y coordinate of the edge parallel to the X axis with the smallest Y coordinate.
	 */
	public int getTop() {
		return top;
	}
	
	/**
	 * Returns the X coordinate of the edge parallel to the Y axis with the largest X coordinate.
	 */
	public int getRight() {
		return right;
	}
	
	/**
	 * Returns the Y coordinate of the edge parallel to the X axis with the largest Y coordinate.
	 */
	public int getBottom() {
		return bottom;
	}
	
	/**
	 * Returns the distance between the edges that are parallel to the Y axis.
	 * 
	 * @post The result equals the difference between the X coordinate of the edge parallel to the Y axis with the largest X coordinate and that of the smallest coordinate
	 *    | result == this.getRight() - this.getLeft()
	 */
	public int getWidth() {
		return (right - left);
	}
	
	/**
	 * Returns the distance between the edges that are parallel to the X axis.
	 * 
	 * @post The result equals the difference between the Y coordinate of the edge parallel to the X axis with the largest Y coordinate and that of the smallest coordinate
	 *    | result == this.getBottom() - this.getTop()
	 */
	public int getHeight() {
		return (bottom - top);
	}
	
	/**
	 * Returns an IntPoint object that represents the top left point of the Extent
	 * 
	 * @post The result's X coordinate equals the X coordinate of the edge parallel to the Y axis with the smallest X coordinate.
	 *    | result.getX() == this.getLeft()
	 * @post The result's Y coordinate equals the Y coordinate of the edge parallel to the X axis with the smallest Y coordinate.
	 *    | result.getY() == this.getTop()
	 */
	public IntPoint getTopLeft() {
		return new IntPoint(left, top);
	}

	/**
	 * Returns an IntPoint object that represents the bottom right point of the Extent
	 * 
	 * @post The result's X coordinate equals the X coordinate of the edge parallel to the Y axis with the largest X coordinate.
	 *    | result.getX() == this.getRight()
	 * @post The result's Y coordinate equals the Y coordinate of the edge parallel to the X axis with the largest Y coordinate.
	 *    | result.getY() == this.getBottom()
	 */
	public IntPoint getBottomRight() {
		return new IntPoint(right, bottom);
	}
	
	/**
	 * Returns whether this extent, considered as a closed set of points (i.e. including its edges and its vertices), contains the given point.
	 * 
	 * @throws IllegalArgumentException if argument {@code point} is {@code null}.
     *    | point == null
	 * 
	 * @post The result equals {@code true} iff the given point is contained within the Extent
	 *    | result == ((point.getX() >= this.getLeft() && point.getX() <= this.getRight() && point.getY() >= this.getTop() && point.getY() <= this.getBottom()) ? true: false)
	 */
	public boolean contains(IntPoint point) {
		if (point == null) {
			throw new IllegalArgumentException("argument point is null");
		}
		
		int x = point.getX();
		int y = point.getY();
		
		if(x >= left && x <= right && y >= top && y <= bottom) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns an Extent object with the given left, top, width and height parameters.
	 * 
	 * @throws IllegalArgumentException if argument {@code width} or {@code height} is negative or equal to zero.
     *    | width <= 0 || height <= 0
     *    
     * @post The result is an Extent with given argument {@code left}
     * 	  | result.getLeft() == left
	 * @post The result is an Extent with given top {@code top}
     * 	  | result.getTop() == top
     * @post The result is an Extent with given width {@code width}
     * 	  | result.getWidth() == width
     * @post The result is an Extent with given height {@code height}
     * 	  | result.getHeight() == height
	 */
	public static Extent ofLeftTopWidthHeight(int left, int top, int width, int height) {
		if(width <= 0 || height <= 0) {
			throw new IllegalArgumentException("argument width or/and height is negative or zero");
		}
		
		return new Extent(left, top, left + width, top + height);
	}
	
	/**
	 * Returns an Extent object with the given left, top, right and bottom parameters.
	 * 
	 * @throws IllegalArgumentException if argument {@code left} is greater than or equal to {@code right}.
     *    | left >= right
	 * @throws IllegalArgumentException if argument {@code top} is greater than or equal to {@code bottom}.
     *    | top >= bottom
     *    
     * @post The result is an Extent with given argument {@code left}
     * 	  | result.getLeft() == left
	 * @post The result is an Extent with given argument {@code top}
     * 	  | result.getTop() == top
     * @post The result is an Extent with given argument {@code right}
     * 	  | result.getRight() == right
     * @post The result is an Extent with given argument {@code bottom}
     * 	  | result.getBottom() == bottom
	 */
	public static Extent ofLeftTopRightBottom(int left, int top, int right, int bottom) {
		if(left >= right) {
			throw new IllegalArgumentException("argument left is greater than or equal to argument right");
		}
		if(top >= bottom) {
			throw new IllegalArgumentException("argument top is greater than or equal to argument bottom");
		}
		
		return new Extent(left, top, right, bottom);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newLeft} is greater than or equal to {@code this.getRight()}.
     *    | newLeft >= this.getRight()
     *    
     * @post The result is an Extent with given argument {@code newLeft}
     * 	  | result.getLeft() == newLeft
	 * @post The result is an Extent with the same {@code top} as this object
     * 	  | result.getTop() == this.getTop()
     * @post The result is an Extent with the same {@code right} as this object
     * 	  | result.getRight() == this.getRight()
     * @post The result is an Extent with the same {@code bottom} as this object
     * 	  | result.getBottom() == this.getBottom()
	 */
	public Extent withLeft(int newLeft) {
		if(newLeft >= right) {
			throw new IllegalArgumentException("argument newLeft is greater than or equal to this.getRight()");
		}		
		
		return new Extent(newLeft, top, right, bottom);
	}
	
	/**
	 * Returns an object that has the given top coordinate and the same left, right, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newTop} is greater than or equal to {@code this.getBottom()}.
     *    | newTop >= this.getBottom()
     *    
     * @post The result is an Extent with the same {@code left} as this object
     * 	  | result.getLeft() == this.getLeft()
	 * @post The result is an Extent with given argument {@code newTop}
     * 	  | result.getTop() == newTop
     * @post The result is an Extent with the same {@code right} as this object
     * 	  | result.getRight() == this.getRight()
     * @post The result is an Extent with the same {@code bottom} as this object
     * 	  | result.getBottom() == this.getBottom()
	 */
	public Extent withTop(int newTop) {
		if(newTop >= bottom) {
			throw new IllegalArgumentException("argument newTop is greater than or equal to this.getBottom()");
		}		
		
		return new Extent(left, newTop, right, bottom);
	}
	
	/**
	 * Returns an object that has the given right coordinate and the same left, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newRight} is smaller than or equal to {@code this.getLeft()}.
     *    | newRight <= this.getLeft()
     *    
     * @post The result is an Extent with the same {@code left} as this object
     * 	  | result.getLeft() == this.getLeft()
	 * @post The result is an Extent with the same {@code top} as this object
     * 	  | result.getTop() == this.getTop()
     * @post The result is an Extent with given argument {@code newRight}
     * 	  | result.getRight() == newRight
     * @post The result is an Extent with the same {@code bottom} as this object
     * 	  | result.getBottom() == this.getBottom()
	 */
	public Extent withRight(int newRight) {
		if(newRight <= left) {
			throw new IllegalArgumentException("argument newRight is smaller than or equal to this.getLeft()");
		}		
		
		return new Extent(left, top, newRight, bottom);
	}
	
	/**
	 * Returns an object that has the given bottom coordinate and the same left, top, and right coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newBottom} is smaller than or equal to {@code this.getTop()}.
     *    | newBottom <= this.getTop()
     *    
     * @post The result is an Extent with the same {@code left} as this object
     * 	  | result.getLeft() == this.getLeft()
	 * @post The result is an Extent with the same {@code top} as this object
     * 	  | result.getTop() == this.getTop()
     * @post The result is an Extent with the same {@code right} as this object
     * 	  | result.getRight() == this.getRight()
     * @post The result is an Extent with given argument {@code newBottom}
     * 	  | result.getBottom() == newBottom
	 */
	public Extent withBottom(int newBottom) {
		if(newBottom <= top) {
			throw new IllegalArgumentException("argument newBottom is smaller than or equal to this.getTop()");
		}		
		
		return new Extent(left, top, right, newBottom);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newWidth} is smaller than or equal to 0.
     *    | newWidth <= 0
     *    
     * @post The result is an Extent with the same {@code left} as this object
     * 	  | result.getLeft() == this.getLeft()
	 * @post The result is an Extent with the same {@code top} as this object
     * 	  | result.getTop() == this.getTop()
     * @post The result is an Extent with given argument {@code newWidth}
     * 	  | result.getWidth() == newWidth
     * @post The result is an Extent with the same {@code bottom} as this object
     * 	  | result.getBottom() == this.getBottom()
	 */
	public Extent withWidth(int newWidth) {
		if(newWidth <= 0) {
			throw new IllegalArgumentException("argument newWidth is smaller than or equal to 0");
		}		
		
		return new Extent(left, top, left + newWidth, bottom);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newHeight} is smaller than or equal to 0.
     *    | newHeight <= 0
     *    
     * @post The result is an Extent with the same {@code left} as this object
     * 	  | result.getLeft() == this.getLeft()
	 * @post The result is an Extent with the same {@code top} as this object
     * 	  | result.getTop() == this.getTop()
     * @post The result is an Extent with the same {@code right} as this object
     * 	  | result.getRight() == this.getRight()
     * @post The result is an Extent with given argument {@code newHeight}
     * 	  | result.getHeight() == newHeight
	 */
	public Extent withHeight(int newHeight) {
		if(newHeight <= 0) {
			throw new IllegalArgumentException("argument newHeight is smaller than or equal to 0");
		}		
		
		return new Extent(left, top, right, top + newHeight);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Extent other = (Extent) obj;
		if (getBottom() != other.getBottom())
			return false;
		if (getLeft() != other.getLeft())
			return false;
		if (getRight() != other.getRight())
			return false;
		if (getTop() != other.getTop())
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getBottom();
		result = prime * result + getLeft();
		result = prime * result + getRight();
		result = prime * result + getTop();
		return result;
	}
	
	@Override
	public String toString() {
		return "Extent[L:" + getLeft() + " T:" + getTop() + " R:" + getRight() + " B:" + getBottom() + "]";
	}
}
