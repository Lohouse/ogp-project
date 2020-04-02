package drawit.shapegroups2;

import drawit.IntPoint;

//TODO: Immutable object?
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
 *    | getWidth > 0 && getHeight() > 0
 */
public class Extent {

    /**
     * @invar | width > 0
     * @invar | height > 0
     */
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
	 * 
	 * @post The result equals the sum of the X coordinate of the edge parallel to the Y axis with the smallest X coordinate and the width.
	 *    | result == this.getLeft() + this.getWidth()
	 */
	public int getRight() {
		return left + width;
	}
	
	/**
	 * Returns the Y coordinate of the edge parallel to the X axis with the largest Y coordinate.
	 * 
	 * @post The result equals the sum of the Y coordinate of the edge parallel to the X axis with the smallest Y coordinate and the height.
	 *    | result == this.getTop() + this.getHeight()
	 */
	public int getBottom() {
		return top + height;
	}
	
	/**
	 * Returns the distance between the edges that are parallel to the Y axis.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the distance between the edges that are parallel to the X axis.
	 */
	public int getHeight() {
		return height;
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
		return new IntPoint(getRight(), getBottom());
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
		
		if(x >= left && x <= getRight() && y >= top && y <= getBottom()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns an Extent object with the given left, top, width and height parameters.
	 * 
	 * @throws IllegalArgumentException if argument {@code width} or {@code height} is negative.
     *    | width < 0 || height < 0
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
		return new Extent(left, top, width, height);
	}
	
	/**
	 * Returns an Extent object with the given left, top, right and bottom parameters.
	 * 
	 * @throws IllegalArgumentException if argument {@code left} is greater than {@code right}.
     *    | left > right
	 * @throws IllegalArgumentException if argument {@code top} is greater than {@code bottom}.
     *    | top > bottom
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
		return new Extent(left, top, right - left, bottom - top);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newLeft} is greater than {@code this.getRight()}.
     *    | newLeft > this.getRight()
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
		return new Extent(newLeft, top, width, height);
	}
	
	/**
	 * Returns an object that has the given top coordinate and the same left, right, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newTop} is greater than {@code this.getBottom()}.
     *    | newTop > this.getBottom()
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
		return new Extent(left, newTop, width, height);
	}
	
	/**
	 * Returns an object that has the given right coordinate and the same left, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newRight} is smaller than {@code this.getLeft()}.
     *    | newRight < this.getLeft()
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
		return new Extent(left, top, newRight - left, height);
	}
	
	/**
	 * Returns an object that has the given bottom coordinate and the same left, top, and right coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newBottom} is smaller than {@code this.getTop()}.
     *    | newBottom < this.getTop()
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
		return new Extent(left, top, width, newBottom - top);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newWidth} is smaller than 0.
     *    | newWidth < 0
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
		return new Extent(left, top, newWidth, height);
	}
	
	/**
	 * Returns an object that has the given left coordinate and the same right, top, and bottom coordinate as this object.
	 * 
	 * @throws IllegalArgumentException if argument {@code newHeight} is smaller than 0.
     *    | newHeight < 0
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
		return new Extent(left, top, width, newHeight);
	}
}