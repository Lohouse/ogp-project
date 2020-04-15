package drawit.shapegroups2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import drawit.IntPoint;
import drawit.IntVector;
import drawit.RoundedPolygon;
import logicalcollections.LogicalList;
import logicalcollections.LogicalSet;

/**
 * Each instance of this class represents a shape group. A shape group is either a leaf group,
 * in which case it directly contains a single shape,or it is a non-leaf group, in which case
 * it directly contains two or more subgroups, which are themselves shape groups.
 * Besides directly or indirectly grouping one or more shapes, a shape group defines a
 * transformation (i.e. a displacement and/or a horizontal and/or vertical scaling) of the
 * shapes it contains.
 * 
 * @invar This shape group either has a shape or subgroups.
 *    | (getShape() == null) != (getSubgroups() == null)
 * @invar If there are subgroups, none may be equal to {@code null}
 * 		  They must all be distinct and they must all have this shape group as parent group.
 * 		  If there aren't any subgroups, the shape isn't equal to {@code null}.
 *    | (getSubgroups() != null &&
 *    |		getSubgroups().stream().allMatch(subgroup -> subgroup != null && subgroup.getParentGroup() == this) &&
 *    |		LogicalList.distinct(getSubgroups())) || 
 *    |		getShape() != null
 * @invar The extent is not equal to {@code null}.
 * 	  | getExtent() != null
 * @invar The original extent is not equal to {@code null}.
 * 	  | getOriginalExtent() != null
 * @invar The shape group is a root group, or it is among its parent's subgroups.
 * 	  | getParentGroup() == null || getParentGroup().getSubgroups().contains(this)
 * @invar The shape group does not have itself as an ancestor
 * 	  | !getAncestors().contains(this)
 */
public class ShapeGroup {
	
	/**
	 * @invar | (shape == null) != (firstChildShapegroup == null)
	 * @invar | nextShapegroup == null || nextShapegroup.previousShapegroup == this
	 * @invar | previousShapegroup == null || previousShapegroup.nextShapegroup == this
	 * @invar | parentShapegroup == null || LogicalList.distinct(parentShapegroup.getSubgroups())
	 * @invar | parentShapegroup == null || parentShapegroup.getSubgroups().contains(this)
	 * @invar | (parentShapegroup == null || nextShapegroup == null) || parentShapegroup.getSubgroups().contains(nextShapegroup)
	 * @invar | (parentShapegroup == null || previousShapegroup == null)|| parentShapegroup.getSubgroups().contains(previousShapegroup)
	 * @invar | firstChildShapegroup == null || firstChildShapegroup.parentShapegroup == this
	 * @invar | extent != null
	 * @invar | originalExtent != null
	 * 
	 * @peerObject
	 */
	private ShapeGroup parentShapegroup;
	
	/**
	 * @peerObject
	 */
	private ShapeGroup firstChildShapegroup;
	
	/**
	 * @peerObject
	 */
	private ShapeGroup nextShapegroup;
	
	/**
	 * @peerObject
	 */
	private ShapeGroup previousShapegroup;
	
	private RoundedPolygon shape;
	private Extent extent;
	private Extent originalExtent;
	
	private Set<ShapeGroup> getAncestorsPrivate() {
		return LogicalSet.<ShapeGroup>matching(ancestors ->
		    (parentShapegroup == null || ancestors.contains(parentShapegroup)) &&
		    ancestors.allMatch(ancestor ->
		        ancestor.parentShapegroup == null || ancestors.contains(ancestor.parentShapegroup))
		);
	}

	//TODO: Add @mutates | nothing. Currently gives compilation errors when added.
	/**
	 * Returns the set of ancestors for this shape group.
	 * 
	 * @creates | result
	 * @inspects | this
	 * 
	 * @post | result != null
	 * @post
	 *    | result.equals(LogicalSet.<ShapeGroup>matching(ancestors ->
	 *    |     (getParentGroup() == null || ancestors.contains(getParentGroup())) &&
	 *    |     ancestors.allMatch(ancestor ->
	 *    |         ancestor.getParentGroup() == null || ancestors.contains(ancestor.getParentGroup()))))
	 */
	public Set<ShapeGroup> getAncestors() {
		return getAncestorsPrivate();
	}	

	//TODO: Add @mutates_properties | this. Currently gives compilation errors when added.
	/**
	 * Initializes this object to represent a leaf shape group that directly contains the given shape.
	 * 
	 * @inspects | shape
	 * 
	 * @throws IllegalArgumentException if argument {@code shape} is null.
	 *    | shape == null
	 *    
	 * @post This extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getExtent() == Extent.ofLeftTopRightBottom(Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt())
	 * @post This original extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getOriginalExtent() == Extent.ofLeftTopRightBottom(Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).min().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).min().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getX()).max().getAsInt(), 
	 *    |												Arrays.stream(shape.getVertices()).mapToInt(vertex -> vertex.getY()).max().getAsInt())
	 * @post This shape equals the given {@code shape}
	 *    | getShape() == shape
	 * @post This subgroups equals {@code null}
	 *    | getSubgroups() == null
	 * @post This parent shape group equals {@code null}
	 *    | getParentGroup() == null
	 */
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
		this.firstChildShapegroup = null;
		this.nextShapegroup = null;
		this.previousShapegroup = null;
		this.parentShapegroup = null;
	}

	//TODO: Add @mutates_properties | this, ...Arrays.stream(subgroups).peek(subgroup -> subgroup.getParentGroup()).toArray(). Currently gives compilation errors when added.
	/**
	 * Initializes this object to represent a non-leaf shape group that directly contains the given subgroups, in the given order.
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
	 *    | getExtent() == Extent.ofLeftTopRightBottom(Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getLeft()).min().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getTop()).min().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getRight()).max().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getBottom()).max().getAsInt())
	 * @post This original extent has the smallest left and top of all vertices of the given {@code shape} and the largest right and bottom.
	 *    | getOriginalExtent() == Extent.ofLeftTopRightBottom(Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getLeft()).min().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getTop()).min().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getRight()).max().getAsInt(), 
	 *    |												Arrays.stream(subgroups).mapToInt(subgroup -> subgroup.getExtent().getBottom()).max().getAsInt())
	 * @post This shape equals {@code null}
	 *    | getShape() == null
	 * @post This subgroups contains the given subgroups
	 *    | getSubgroups() == new ArrayList<ShapeGroup>(Arrays.asList(subgroups))
	 * @post This parent shape group equals {@code null}
	 *    | getParentGroup() == null
	 */
	public ShapeGroup(ShapeGroup[] subgroups) {
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
		for (int i = 0; i < subgroups.length; i++) {
			ShapeGroup subgroup = subgroups[i];
			
			if (subgroup == null) {
				throw new IllegalArgumentException("element of argument subgroups is null");
			}
			if (subgroup.getParentGroup() != null)
			{
				throw new IllegalArgumentException("element of argument subgroups is already in a ShapeGroup or has multiple occurences in the given array");
			}
			
			subgroup.parentShapegroup = this;
			subgroup.nextShapegroup = subgroups[(i + 1) % subgroups.length];
			subgroup.previousShapegroup = subgroups[Math.floorMod(i - 1, subgroups.length)];
			
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
		this.shape = null;
		this.firstChildShapegroup = subgroups[0];
		this.nextShapegroup = null;
		this.previousShapegroup = null;
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
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}
		
		if (parentShapegroup.firstChildShapegroup == this) {
			return;
		}
		
		previousShapegroup.nextShapegroup = nextShapegroup;
		nextShapegroup.previousShapegroup = previousShapegroup;

		nextShapegroup = parentShapegroup.firstChildShapegroup;
		previousShapegroup = parentShapegroup.firstChildShapegroup.previousShapegroup;

		parentShapegroup.firstChildShapegroup.previousShapegroup.nextShapegroup = this;
		parentShapegroup.firstChildShapegroup.previousShapegroup = this;		
		
		parentShapegroup.firstChildShapegroup = this;
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
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
		}
		
		if (parentShapegroup.firstChildShapegroup.previousShapegroup == this) {
			return;
		}
		
		if (parentShapegroup.firstChildShapegroup == this) {
			parentShapegroup.firstChildShapegroup = parentShapegroup.firstChildShapegroup.nextShapegroup;
			return;
		}

		previousShapegroup.nextShapegroup = nextShapegroup;
		nextShapegroup.previousShapegroup = previousShapegroup;

		nextShapegroup = parentShapegroup.firstChildShapegroup;
		previousShapegroup = parentShapegroup.firstChildShapegroup.previousShapegroup;

		parentShapegroup.firstChildShapegroup.previousShapegroup.nextShapegroup = this;
		parentShapegroup.firstChildShapegroup.previousShapegroup = this;
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
		
		if (shape != null) {
			commands += shape.getDrawingCommands() + bc;
		} else {
			List<ShapeGroup> subgroups = getSubgroups();
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
	 * Returns the shape directly contained by this shape group, or null if this is a non-leaf shape group.
	 * 
	 * @basic
	 */
	public RoundedPolygon getShape() {
		return shape;
	}
	
	/**
	 * Returns the subgroup at the given (zero-based) index in this non-leaf shape group's list of subgroups.
	 * 
     * @throws IllegalStateException if this is a leaf shape group.
	 *    | this.getSubgroups() == null
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
		
		List<ShapeGroup> subGroups = getSubgroups();
		
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group");
		}
		
		return subGroups.get(index);
	}
	
	/**
	 * Return the first subgroup in this non-leaf shape group's list of subgroups whose extent contains the given point,
	 * expressed in this shape group's inner coordinate system.
	 * 
	 * @throws IllegalArgumentException if argument {@code innerCoordinates} is {@code null}.
	 *    | innerCoordinates == null
	 * @throws IllegalStateException if this is a leaf shape group.
	 *    | this.getSubgroups() == null
	 *     
	 * @post The result equals the first subgroup whose extent contains the given point.
	 * 	  | result == Arrays.stream(this.getSubgroups().toArray(new ShapeGroup[getSubgroupCount()]))
	 * 	  |		.filter(subgroup -> subgroup.getExtent().contains(innerCoordinates)).findFirst().orElse(null)
	 */
	public ShapeGroup getSubgroupAt(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}

		List<ShapeGroup> subgroups = getSubgroups();
		
		if (subgroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group");
		}

		for (ShapeGroup subgroup : subgroups) {
			if (subgroup.getExtent().contains(innerCoordinates)) {
				return subgroup;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the number of subgroups of this non-leaf shape group.
	 * 
	 * @throws IllegalStateException if this is a leaf shape group.
	 *    | this.getSubgroups() == null
	 *    
	 * @post The result equals the amount of subgroups.
	 * 	  | result == this.getSubgroups().size()
	 */
	public int getSubgroupCount() {
		List<ShapeGroup> subGroups = getSubgroups();
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group");
		}
		
		return subGroups.size();
	}
	
	/**
	 * Returns the list of subgroups of this shape group, or null if this is a leaf shape group.
	 * 
	 * @post The result is null if this.getShape is not null, else the elements of the resulting List are not null.
	 *    | (this.getShape() != null) ? result == null : Arrays.stream(result.toArray(new ShapeGroup[] {})).allMatch(e -> e != null)
	 *    
	 * @peerObjects
	 * @basic
	 */
	public List<ShapeGroup> getSubgroups() {
		List<ShapeGroup> subgroupsList = new ArrayList<ShapeGroup>();
		
		if (firstChildShapegroup == null) {
			return null;
		}
		
		ShapeGroup currentShapegroup = firstChildShapegroup;
		do {
			subgroupsList.add(currentShapegroup);
			currentShapegroup = currentShapegroup.nextShapegroup;
		} while (currentShapegroup != firstChildShapegroup);
		
		return subgroupsList;
	}
	
	/**
	 * Returns the shape group that directly contains this shape group, or null if no shape group directly contains this shape group.
	 * 
	 * @peerObject
	 * @basic
	 */
	public ShapeGroup getParentGroup() {
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
