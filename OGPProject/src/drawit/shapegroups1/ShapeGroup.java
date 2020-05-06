package drawit.shapegroups1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import drawit.IntPoint;
import drawit.IntVector;
import logicalcollections.LogicalMap;
import logicalcollections.LogicalSet;
import logicalcollections.LogicalList;

/**
 * Each instance of this class represents a shape group that defines a
 * transformation (i.e. a displacement and/or a horizontal and/or vertical scaling) 
 * of the shapes it contains.
 * 
 * @invar | getParentGroup() == null ||
 *        | getParentGroup().getSubgroups() != null && getParentGroup().getSubgroups().contains(this)
 * @invar | !getAncestors().contains(this)
 */
public class ShapeGroup {
	
	/**
	 * @invar | parentShapegroup == null || parentShapegroup.subgroups != null && parentShapegroup.subgroups.contains(this)
	 * @invar | !getAncestorsPrivate().contains(this)
	 * 
	 * @peerObject
	 */
	NonleafShapeGroup parentShapegroup;

	Extent extent;
	Extent originalExtent;
	
	
	/**
	 * Returns the set of the ancestors of this shape group.
	 * 
	 * @post | result != null
	 * @post | result.equals(LogicalSet.<ShapeGroup>matching(ancestors ->
	 *       |     getParentGroup() == null || ancestors.contains(getParentGroup()) &&
	 *       |     ancestors.allMatch(ancestor ->
	 *       |         ancestor.getParentGroup() == null || ancestors.contains(ancestor.getParentGroup()))))
	 */
	public Set<ShapeGroup> getAncestors() {
		return getAncestorsPrivate();
	}
	
	Set<ShapeGroup> getAncestorsPrivate() {
		return LogicalSet.<ShapeGroup>matching(ancestors ->
			parentShapegroup == null || ancestors.contains(parentShapegroup) &&
			ancestors.allMatch(ancestor -> ancestor.parentShapegroup == null || ancestors.contains(ancestor.parentShapegroup))
		);
	}
	
	//TODO: Add @mutates_properties | this. Currently gives compilation errors when added.
	/**
	 * Initializes this object.
	 * 
	 * @post This parent shape group equals {@code null}
	 *    | getParentGroup() == null
	 */
	public ShapeGroup() {
		this.parentShapegroup = null;
	}

	
	/**
	 * Moves this shape group to the front of its parent's list of subgroups.
	 * 
	 * @mutates_properties | getParentGroup().getSubgroups()
	 * @inspects | this
	 * 
	 * @throws IllegalStateException if this ShapeGroup is not part of a parent group.
	 *    | this.getParentGroup() == null
	 * 
	 * @post This ShapeGroup is the first element in its parent's subgroup list.
	 *    | this.getParentGroup().getSubgroups().get(0) == this
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from index 1 to the index where
	 *      this ShapeGroup was (inclusive), are shifted up by 1 compared to the previous state.
	 *    | IntStream.range(1, old(this.getParentGroup()).getSubgroups().indexOf(this) + 1).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup()).getSubgroups().get(i - 1))
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from the index after the
	 *      index where this ShapeGroup was to the end of the list, remain at the same index.
	 *    | IntStream.range(old(this.getParentGroup()).getSubgroups().indexOf(this) + 1, this.getParentGroup().getSubgroupCount()).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup()).getSubgroups().get(i))
	 */
	public void bringToFront() {
		NonleafShapeGroup parentShapegroup = getParentGroup();
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}

		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(0, this);
	}
	
	/**
	 * Moves this shape group to the front of its parent's list of subgroups.
	 * 
	 * @mutates_properties | getParentGroup().getSubgroups()
	 * @inspects | this
	 * 
	 * @throws IllegalStateException if this ShapeGroup is not part of a parent group.
	 *    | this.getParentGroup() == null
	 * 
	 * @post This ShapeGroup is the last element in its parent's subgroup list.
	 *    | this.getParentGroup().getSubgroups().get(this.getParentGroup().getSubgroupCount() - 1) == this
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from index 0 to the
	 *      index before the index where this ShapeGroup was, remain at the same index.
	 *    | IntStream.range(0, old(this.getParentGroup()).getSubgroups().indexOf(this)).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup()).getSubgroups().get(i))
	 * @post
	 *      The elements in this ShapeGroup's parent's subgroup list from the index where this ShapeGroup was
	 *      to the index before end of the list, are shifted down by 1 compared to the previous state.
	 *    | IntStream.range(old(this.getParentGroup()).getSubgroups().indexOf(this), this.getParentGroup().getSubgroupCount() - 1).allMatch(i ->
	 *    | 	this.getParentGroup().getSubgroups().get(i) == old(this.getParentGroup()).getSubgroups().get(i + 1))
	 */
	public void sendToBack() {
		NonleafShapeGroup parentShapegroup = getParentGroup();
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}
		
		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(this);
	}

	//TODO: Add @mutates | nothing. Currently gives compilation errors when added.
	/**
	 * Returns the coordinates in the global coordinate system of the point whose coordinates
	 * in this shape group's inner coordinate system are the given coordinates.
	 * 
	 * @inspects | this
	 * 
	 * @throws IllegalArgumentException if argument {@code innerCoordinates} is {@code null}.
	 *    | innerCoordinates == null
	 */
	public IntPoint toGlobalCoordinates(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}

		double x = innerCoordinates.getX();
		double y = innerCoordinates.getY();
		
		ShapeGroup operatingShapegroup = this;
		while (operatingShapegroup != null) {
			x = (double) operatingShapegroup.getExtent().getLeft() + ((double) x - (double) operatingShapegroup.getOriginalExtent().getLeft()) /
					(double) operatingShapegroup.getOriginalExtent().getWidth() * (double) operatingShapegroup.getExtent().getWidth();
			y = (double) operatingShapegroup.getExtent().getTop() + ((double) y - (double) operatingShapegroup.getOriginalExtent().getTop()) /
					(double) operatingShapegroup.getOriginalExtent().getHeight() * (double) operatingShapegroup.getExtent().getHeight();
			
			operatingShapegroup = operatingShapegroup.getParentGroup();
		}
		
		return new IntPoint((int) x, (int) y);
	}

	//TODO: Add @mutates | nothing. Currently gives compilation errors when added.
	/**
	 * Returns the coordinates in this shape group's inner coordinate system of the point whose coordinates
	 * in the global coordinate system are the given coordinates.
	 * 
	 * @inspects | this
	 * 
	 * @throws IllegalArgumentException if argument {@code globalCoordinates} is {@code null}.
	 *    | globalCoordinates == null
	 */
	public IntPoint toInnerCoordinates(IntPoint globalCoordinates) {
		if (globalCoordinates == null) {
			throw new IllegalArgumentException("argument globalCoordinates is null");
		}
		
		double x = globalCoordinates.getX();
		double y = globalCoordinates.getY();
		
		List<ShapeGroup> operatingShapegroups = new ArrayList<ShapeGroup>();
		ShapeGroup testOperatingShapegroup = this;
		while (testOperatingShapegroup != null) {
			operatingShapegroups.add(0, testOperatingShapegroup);
			testOperatingShapegroup = testOperatingShapegroup.getParentGroup();
		}
		
		for (ShapeGroup operatingShapegroup : operatingShapegroups) {
			x = (double) operatingShapegroup.getOriginalExtent().getLeft() + (x - (double) operatingShapegroup.getExtent().getLeft()) /
					(double) operatingShapegroup.getExtent().getWidth() * (double) operatingShapegroup.getOriginalExtent().getWidth();
			y = (double) operatingShapegroup.getOriginalExtent().getTop() + (y - (double) operatingShapegroup.getExtent().getTop()) /
					(double) operatingShapegroup.getExtent().getHeight() * (double) operatingShapegroup.getOriginalExtent().getHeight();
		}
		
		return new IntPoint((int) x, (int) y);
	}

	//TODO: Add @mutates | nothing. Currently gives compilation errors when added.
	/**
	 * Returns the coordinates in this shape group's inner coordinate system of the vector whose coordinates
	 * in the global coordinate system are the given coordinates.
	 * 
	 * @inspects | this
	 * 
	 * @throws IllegalArgumentException if argument {@code relativeGlobalCoordinates} is {@code null}.
	 *    | relativeGlobalCoordinates == null
	 */
	public IntVector toInnerCoordinates(IntVector relativeGlobalCoordinates) {
		if (relativeGlobalCoordinates == null) {
			throw new IllegalArgumentException("argument relativeGlobalCoordinates is null");
		}
		
		double x = relativeGlobalCoordinates.getX();
		double y = relativeGlobalCoordinates.getY();
		
		List<ShapeGroup> operatingShapegroups = new ArrayList<ShapeGroup>();
		ShapeGroup testOperatingShapegroup = this;
		while (testOperatingShapegroup != null) {
			operatingShapegroups.add(0, testOperatingShapegroup);
			testOperatingShapegroup = testOperatingShapegroup.getParentGroup();
		}
		
		for (ShapeGroup operatingShapegroup : operatingShapegroups) {
			x *= ((double) operatingShapegroup.getOriginalExtent().getWidth()) / ((double) operatingShapegroup.getExtent().getWidth());
			y *= ((double) operatingShapegroup.getOriginalExtent().getHeight()) / ((double) operatingShapegroup.getExtent().getHeight());
		}		
		
		return new IntVector((int) x, (int) y);
	}

	//TODO: Add @mutates | nothing. Currently gives compilation errors when added.
	/**
	 * Returns a textual representation of a sequence of drawing commands for drawing the shapes contained directly or indirectly by this shape group,
	 * expressed in this shape group's outer coordinate system.
	 * 
	 * @inspects | this
	 */
	public String getDrawingCommands() {
		String bc = "\n";
		
		int operations = 0;
		String commands = "";
		
		int translateX = getExtent().getLeft() - getOriginalExtent().getLeft();
		int translateY = getExtent().getTop() - getOriginalExtent().getTop();
		double scaleX = (double) getExtent().getWidth() / (double) getOriginalExtent().getWidth();
		double scaleY = (double) getExtent().getHeight() / (double) getOriginalExtent().getHeight();
		
		if (scaleX != 1 || scaleY != 1) {
			commands += "pushTranslate " + (translateX + getOriginalExtent().getLeft()) + " " + (translateY + getOriginalExtent().getTop()) + bc;
			operations++;
			
			commands += "pushScale " + scaleX + " " + scaleY + bc;
			operations++;
			
			if (getOriginalExtent().getLeft() != 0 || getOriginalExtent().getTop() != 0) {
				commands += "pushTranslate " + -getOriginalExtent().getLeft() + " " + -getOriginalExtent().getTop() + bc;
				operations++;
			}
		} else {
			if (translateX != 0 || translateY != 0) {
				commands += "pushTranslate " + (translateX) + " " + (translateY) + bc;
				operations++;
			}
		}
		
		if (this instanceof LeafShapeGroup) {
			LeafShapeGroup leafGroup = (LeafShapeGroup) this;
			commands += leafGroup.shape.getDrawingCommands() + bc;
		} else {
			NonleafShapeGroup nonleafGroup = (NonleafShapeGroup) this;
			List<ShapeGroup> subgroups = nonleafGroup.getSubgroups();
			for (int i = subgroups.size() - 1; i >= 0; i--) {
				commands += subgroups.get(i).getDrawingCommands() + bc;				
			}
		}
		
		for (int i = 0; i < operations; i++) {
			commands += "popTransform";
			if (i < operations - 1) {
				commands += bc;
			}
		}
		
		return commands;
	}
	
	/**
	 * Returns the shape group that directly contains this shape group, or null if no shape group directly contains this shape group.
	 * 
	 * @peerObject
	 * @basic
	 */
	public NonleafShapeGroup getParentGroup() {
		return parentShapegroup;
	}

	/**
	 * Returns the extent of this shape group, expressed in its inner coordinate system.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 *    
	 * @basic
	 */
	public Extent getOriginalExtent() {
		return originalExtent;
	}
	
	/**
	 * Returns the extent of this shape group, expressed in its outer coordinate system.
	 * 
	 * @post The result is not {@code null}
	 *    | result != null
	 *    
	 * @basic
	 */
	public Extent getExtent() {
		return extent;
	}
	
	/**
	 * Registers the given extent as this shape group's extent, expressed in this shape group's outer coordinate system.
	 * 
	 * @mutates_properties | this.getExtent()
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
