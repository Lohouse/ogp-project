package drawit.shapegroups1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import drawit.IntPoint;
import logicalcollections.LogicalList;

/**
 * Each instance of this class represents a shape group that defines a
 * transformation (i.e. a displacement and/or a horizontal and/or vertical scaling) 
 * of the shapes it contains.
 * A non-leaf shape group directly contains two or more subgroups, which are themselves shape groups.
 * 
 * @invar | LogicalList.distinct(getSubgroups())
 * @invar | getSubgroups().stream().allMatch(g -> g != null && g.getParentGroup() == this)
 */
public class NonleafShapeGroup extends ShapeGroup {
	
	/**
	 * @invar | subgroups != null
	 * @invar | LogicalList.distinct(subgroups)
	 * @invar | subgroups.stream().allMatch(g -> g != null && g.parentShapegroup == this)
	 *
	 * @representationObject
	 * @peerObjects
	 */
	List<ShapeGroup> subgroups;
	

	//TODO: Add @mutates_properties | this, ...Arrays.stream(subgroups).peek(subgroup -> subgroup.getParentGroup()).toArray(). Currently gives compilation errors when added.
	/**
	 * Initializes this non-leaf shape group.
	 * 
	 * @inspects | subgroups
	 * 
	 * @throws IllegalArgumentException if argument {@code subgroups} is null.
	 *    | subgroups == null
	 * @throws IllegalArgumentException if less than two shapegroups are present in {@code subgroups}.
	 *    | subgroups.length < 2
	 * @throws IllegalArgumentException if any subgroup in {@code subgroups} is null.
	 *    | Arrays.stream(subgroups).filter(subgroup -> subgroup == null).findAny() != null 
	 * @throws IllegalArgumentException if any subgroup in {@code subgroups} is already in a group or has multiple occurences in the given array.
	 *    | Arrays.stream(subgroups).filter(subgroup -> subgroup.getParentGroup() != null).findAny() != null 
	 *    
	 * @post This extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getExtent().getLeft() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getLeft()).min().getAsInt() &&
	 *    |	getExtent().getTop() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getTop()).min().getAsInt() &&
	 *    |	getExtent().getRight() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getRight()).max().getAsInt() &&
	 *    |	getExtent().getBottom() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getBottom()).max().getAsInt()
	 * @post This original extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getOriginalExtent().getLeft() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getLeft()).min().getAsInt() &&
	 *    |	getOriginalExtent().getTop() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getTop()).min().getAsInt() &&
	 *    |	getOriginalExtent().getRight() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getRight()).max().getAsInt() &&
	 *    |	getOriginalExtent().getBottom() == Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getBottom()).max().getAsInt()
	 * @post This subgroups contains the given subgroups
	 *    | getSubgroups().equals(new ArrayList<ShapeGroup>(Arrays.asList(subgroups)))
	 */
	public NonleafShapeGroup(ShapeGroup[] subgroups) {
		super();
		
		if (subgroups == null) {
			throw new IllegalArgumentException("argument subgroups is null");
		}
		if (subgroups.length < 2) {
			throw new IllegalArgumentException("less than 2 elements in argument subgroups");	
		}
		
		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		for (ShapeGroup subgroup : subgroups) {
			if (subgroup == null) {
				throw new IllegalArgumentException("element of argument subgroups is null");
			}
			if (subgroup.getParentGroup() != null)
			{
				throw new IllegalArgumentException("element of argument subgroups is already in a ShapeGroup or has multiple occurences in the given array");
			}
			subgroup.parentShapegroup = this;
			
			if (subgroup.getExtent().getRight() > right) {
				right = subgroup.getExtent().getRight();
			}
			if (subgroup.getExtent().getLeft() < left) {
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
		this.subgroups = new ArrayList<ShapeGroup>(Arrays.asList(subgroups));
		this.parentShapegroup = null;
	}
	
	/**
	 * Returns the subgroup at the given (zero-based) index in this non-leaf shape group's list of subgroups.
	 * 
	 * @throws IllegalArgumentException if the given index is smaller than zero or greater than or equal to the amount of subgroups.
	 *    | index < 0 || index >= this.getSubgroupCount()
	 * 
	 * @post The result is the subgroup at the given index.
	 * 	  | result.equals(getSubgroups().get(index)) == true
	 */
	public ShapeGroup getSubgroup(int index) {
		if (index < 0 || index >= getSubgroupCount()) {
			throw new IllegalArgumentException("argument index is out of bounds");
		}
		
		return getSubgroups().get(index);
	}
	
	/**
	 * Return the first subgroup in this non-leaf shape group's list of subgroups whose extent contains the given point,
	 * expressed in this shape group's inner coordinate system.
	 * 
	 * @throws IllegalArgumentException if argument {@code innerCoordinates} is {@code null}.
	 *    | innerCoordinates == null
	 *     
	 * @post The result equals the first subgroup whose extent contains the given point.
	 * 	  | result == Arrays.stream(this.getSubgroups().toArray(new ShapeGroup[getSubgroupCount()]))
	 * 	  |		.filter(subgroup -> subgroup.getExtent().contains(innerCoordinates)).findFirst().orElse(null)
	 */
	public ShapeGroup getSubgroupAt(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}
		
		for (ShapeGroup subgroup : getSubgroups()) {
			if (subgroup.getExtent().contains(innerCoordinates)) {
				return subgroup;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the number of subgroups of this non-leaf shape group.
	 *  
	 * @post The result equals the amount of subgroups.
	 * 	  | result == this.getSubgroups().size()
	 */
	public int getSubgroupCount() {
		return getSubgroups().size();
	}
	
	/**
	 * Returns the list of subgroups of this shape group.
	 * 
	 * @post The elements of the resulting List are not null.
	 *    | Arrays.stream(result.toArray(new ShapeGroup[] {})).allMatch(e -> e != null)
	 *    
	 * @peerObjects
	 * @basic
	 */
	public List<ShapeGroup> getSubgroups() {
			return new ArrayList<ShapeGroup>(subgroups); 
	}
	
}
