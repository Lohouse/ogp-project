package drawit.shapegroups1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.RoundedPolygon;

/**
 * Each instance of this class represents a shape group. A shape group is either a leaf group,
 * in which case it directly contains a single shape,or it is a non-leaf group, in which case
 * it directly contains two or more subgroups, which are themselves shape groups.
 * Besides directly or indirectly grouping one or more shapes, a shape group defines a
 * transformation (i.e. a displacement and/or a horizontal and/or vertical scaling) of the
 * shapes it contains.
 */
public class ShapeGroup {
	
	//TODO: @mutates, @creates, @inspects
	
	private RoundedPolygon shape;
	private List<ShapeGroup> subgroups;
	private ShapeGroup parentShapegroup;
	private Extent extent;
	private Extent originalExtent;

	// What if this polygon already is in a subgroup ?
	public ShapeGroup(RoundedPolygon shape) {
		if (shape == null) {
			throw new IllegalArgumentException("argument shape is null");
		}

		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		for (IntPoint vertex : shape.getVertices()) {
			if (vertex.getX() > right) {
				right = vertex.getX();
			}
			if (vertex.getX() < left) {
				left = vertex.getX();
			}
			if (vertex.getY() < top) {
				top = vertex.getY();
			}
			if (vertex.getY() > bottom) {
				bottom = vertex.getY();
			}
		}
		
		this.extent = this.originalExtent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = shape;
		this.subgroups = null;
		this.parentShapegroup = null;
	}
	
	// What if they already are in a subgroup ?
	// Throw exception if subgroups contains the same instance twice?
	public ShapeGroup(ShapeGroup[] subgroups) {
		if (subgroups == null) {
			throw new IllegalArgumentException("argument subgroups is null");
		}
		if (subgroups.length < 2) {
			throw new IllegalArgumentException("less than 2 elements in argument subgroups");	
		}
		if (Arrays.stream(subgroups).anyMatch(e -> e == null)) {
			throw new IllegalArgumentException("element of argument subgroups is null");			
		}
		
		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		for (ShapeGroup subgroup : subgroups) {
			subgroup.parentShapegroup = this;
			
			if (subgroup.getExtent().getRight() > right) {
				right = subgroup.getExtent().getRight();
			} else if (subgroup.getExtent().getLeft() < left) {
				left = subgroup.getExtent().getLeft();
			}
			
			if (subgroup.getExtent().getTop() < top) {
				top = subgroup.getExtent().getTop();
			}
			if (subgroup.getExtent().getBottom() > bottom) {
				bottom = subgroup.getExtent().getBottom();
			}
		}
		
		this.extent = this.originalExtent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = null;
		this.subgroups = new ArrayList<ShapeGroup>(Arrays.asList(subgroups));
		this.parentShapegroup = null;
	}
	
	/**
	 * Moves this shape group to the front of its parent's list of subgroups.
	 * 
	 * @throws IllegalStateException if this ShapeGroup is not part of a parent group.
	 *    | this.getParentGroup() == null
	 * 
	 * @post This ShapeGroup is the first element in its parent's subgroup list.
	 *    | this.getParentGroup().getSubgroups().get(0) == this
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from index 1 to the index where
	 *      this ShapeGroup was (inclusive), are shifted up by 1 compared to the previous state.
	 *    | IntStream.range(1, old(this.getParentGroup().getSubgroups()).indexOf(this) + 1).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup().getSubgroups()).get(i - 1))
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from the index after the
	 *      index where this ShapeGroup was to the end of the list, remain at the same index.
	 *    | IntStream.range(old(this.getParentGroup().getSubgroups()).indexOf(this) + 1, this.getParentGroup().getSubgroupCount()).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup().getSubgroups()).get(i))
	 */
	public void bringToFront() {
		ShapeGroup parentShapegroup = getParentGroup();
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}

		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(0, this);
	}
	
	/**
	 * Moves this shape group to the front of its parent's list of subgroups.
	 * 
	 * @throws IllegalStateException if this ShapeGroup is not part of a parent group.
	 *    | this.getParentGroup() == null
	 * 
	 * @post This ShapeGroup is the last element in its parent's subgroup list.
	 *    | this.getParentGroup().getSubgroups().get(this.getSubgroupCount() - 1) == this
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from index 0 to the
	 *      index before the index where this ShapeGroup was, remain at the same index.
	 *    | IntStream.range(0, old(this.getParentGroup().getSubgroups()).indexOf(this)).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup().getSubgroups()).get(i))
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from the index where this ShapeGroup was
	 *      to the index before end of the list, are shifted down by 1 compared to the previous state.
	 *    | IntStream.range(old(this.getParentGroup().getSubgroups()).indexOf(this), this.getParentGroup().getSubgroupCount() - 1).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup().getSubgroups()).get(i + 1))
	 */
	public void sendToBack() {
		ShapeGroup parentShapegroup = getParentGroup();
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}
		
		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(this);
	}

	/**
	 * Returns the coordinates in the global coordinate system of the point whose coordinates
	 * in this shape group's inner coordinate system are the given coordinates.
	 * 
	 * @throws IllegalArgumentException if argument {@code innerCoordinates} is {@code null}.
	 *    | innerCoordinates == null
	 */
	public IntPoint toGlobalCoordinates(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}

		int x = innerCoordinates.getX();
		int y = innerCoordinates.getY();
		
		ShapeGroup operatingShapegroup = this;
		while (operatingShapegroup != null) {
			x = operatingShapegroup.getExtent().getLeft() + (x - operatingShapegroup.getOriginalExtent().getLeft()) /
					operatingShapegroup.getOriginalExtent().getWidth() * operatingShapegroup.getExtent().getWidth();
			y = operatingShapegroup.getExtent().getTop() + (y - operatingShapegroup.getOriginalExtent().getTop()) /
					operatingShapegroup.getOriginalExtent().getHeight() * operatingShapegroup.getExtent().getHeight();
			
			operatingShapegroup = operatingShapegroup.getParentGroup();
		}
		
		return new IntPoint(x, y);
	}
	
	/**
	 * Returns the coordinates in this shape group's inner coordinate system of the point whose coordinates
	 * in the global coordinate system are the given coordinates.
	 */
	public IntPoint toInnerCoordinates(IntPoint globalCoordinates) {
		if (globalCoordinates == null) {
			throw new IllegalArgumentException("argument globalCoordinates is null");
		}
		
		int x = globalCoordinates.getX();
		int y = globalCoordinates.getY();
		
		List<ShapeGroup> operatingShapegroups = new ArrayList<ShapeGroup>();
		ShapeGroup testOperatingShapegroup = this;
		while (testOperatingShapegroup != null) {
			operatingShapegroups.add(0, testOperatingShapegroup);
			testOperatingShapegroup = testOperatingShapegroup.getParentGroup();
		}
		
		for (ShapeGroup operatingShapegroup : operatingShapegroups) {
			x = operatingShapegroup.getOriginalExtent().getLeft() + (x - operatingShapegroup.getExtent().getLeft()) /
					operatingShapegroup.getExtent().getWidth() * operatingShapegroup.getOriginalExtent().getWidth();
			y = operatingShapegroup.getOriginalExtent().getTop() + (y - operatingShapegroup.getExtent().getTop()) /
					operatingShapegroup.getExtent().getHeight() * operatingShapegroup.getOriginalExtent().getHeight();
		}
		
		return new IntPoint(x, y);
	}
	
	/**
	 * Returns the coordinates in this shape group's inner coordinate system of the vector whose coordinates
	 * in the global coordinate system are the given coordinates.
	 */
	public IntVector toInnerCoordinates(IntVector relativeGlobalCoordinates) {
		if (relativeGlobalCoordinates == null) {
			throw new IllegalArgumentException("argument relativeGlobalCoordinates is null");
		}
		
		int x = relativeGlobalCoordinates.getX();
		int y = relativeGlobalCoordinates.getY();
		
		List<ShapeGroup> operatingShapegroups = new ArrayList<ShapeGroup>();
		ShapeGroup testOperatingShapegroup = this;
		while (testOperatingShapegroup != null) {
			operatingShapegroups.add(0, testOperatingShapegroup);
			testOperatingShapegroup = testOperatingShapegroup.getParentGroup();
		}
		
		for (ShapeGroup operatingShapegroup : operatingShapegroups) {
			x = operatingShapegroup.getOriginalExtent().getLeft() + (x - operatingShapegroup.getExtent().getLeft()) /
					operatingShapegroup.getExtent().getWidth() * operatingShapegroup.getOriginalExtent().getWidth();
			y = operatingShapegroup.getOriginalExtent().getTop() + (y - operatingShapegroup.getExtent().getTop()) /
					operatingShapegroup.getExtent().getHeight() * operatingShapegroup.getOriginalExtent().getHeight();
		}
		
		return new IntVector(x, y);
	}
	
	public void getDrawingCommands() {
		throw new RuntimeException("not yet implemented");		
	}
	
	/**
	 * Returns the shape directly contained by this shape group, or null if this is a non-leaf shape group.
	 */
	public RoundedPolygon getShape() {
		return shape;
	}
	
	/**
	 * Returns the subgroup at the given (zero-based) index in this non-leaf shape group's list of subgroups.
	 */
	public ShapeGroup getSubgroup(int index) {
		if (index < 0 || index >= getSubgroupCount()) {
			throw new IllegalArgumentException("argument index is out of bounds");
		}
		
		return getSubgroups().get(index);
	}
	
	//TODO: Does 'outer' mean absolute coordinate system (same reference point for all ShapeGroups in the program) or coordinate system of the parent ShapeGroup ?
	/**
	 * Return the first subgroup in this non-leaf shape group's list of subgroups whose extent contains the given point,
	 * expressed in this shape group's inner coordinate system.
	 */
	public ShapeGroup getSubgroupAt(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}
		
		for (ShapeGroup subgroup : getSubgroups()) {
			if (subgroup.getOriginalExtent().contains(innerCoordinates)) {
				return subgroup;
			}
		}
		
		throw new IllegalStateException("this shape group does not contain a subgroup of which the extent contains the given coordinates"); //TODO: Or return null?
	}
	
	/**
	 * Returns the number of subgroups of this non-leaf shape group.
	 * 
	 * @throws IllegalStateException if this ShapeGroup is a leaf shape group.
	 *    | this.getSubgroups() == null
	 */
	public int getSubgroupCount() {
		List<ShapeGroup> subGroups = getSubgroups();
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is a leaf shape group"); //TODO: Or return 0 ?
		}
		
		return subGroups.size();
	}
	
	/**
	 * Returns the list of subgroups of this shape group, or null if this is a leaf shape group.
	 * 
	 * @post If this ShapeGroup is not a leaf shape group, the result and its elements are not null.
	 *    | result == null ||
	 *    | Arrays.stream(result.toArray(new ShapeGroup[] {})).allMatch(e -> e != null)
	 */
	public List<ShapeGroup> getSubgroups() {
		return subgroups == null ? null : new ArrayList<ShapeGroup>(subgroups); 
	}
	
	/**
	 * Returns the shape group that directly contains this shape group, or null if no shape group directly contains this shape group.
	 */
	public ShapeGroup getParentGroup() {
		return parentShapegroup;
	}

	/**
	 * Returns the extent of this shape group, expressed in its inner coordinate system.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 */
	public Extent getOriginalExtent() {
		return originalExtent;
	}
	
	/**
	 * Returns the extent of this shape group, expressed in its outer coordinate system.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 */
	public Extent getExtent() {
		return extent;
	}
	
	/**
	 * Registers the given extent as this shape group's extent, expressed in this shape group's outer coordinate system.
	 * 
	 * @mutates | this
	 * 
	 * @throws IllegalArgumentException if argument {@code newExtent} is {@code null}.
	 *    | newExtent == null
	 * 
	 * @post This ShapeGroup's extent is equal to {@code newExtent}.
	 *    | this.getExtent() == newExtent
	 * @post This ShapeGroup's original extent remains unchanged.
	 *    | this.getOriginalExtent() == old(this.getOriginalExtent())
	 */
	public void setExtent(Extent newExtent) {
		if (newExtent == null) {
			throw new IllegalArgumentException("argument newExtent is null");
		}
		
		this.extent = newExtent;
	}
}
