package drawit.shapegroups2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private RoundedPolygon shape;
	private ShapeGroup parentShapegroup;
	private ShapeGroup firstChildShapegroup;
	private ShapeGroup nextShapegroup;
	private ShapeGroup previousShapegroup;
	private Extent extent;
	
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
		
		this.extent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = shape;
		this.firstChildShapegroup = null;
		this.nextShapegroup = null;
		this.previousShapegroup = null;
		this.parentShapegroup = null;
	}
	
	// What if they already are in a subgroup?
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
		for (int i = 0; i < subgroups.length; i++) {
			ShapeGroup subgroup = subgroups[i];
			
			subgroup.parentShapegroup = this;
			subgroup.nextShapegroup = subgroups[(i + 1) % subgroups.length];
			subgroup.previousShapegroup = subgroups[(i - 1) % subgroups.length];
			
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
		
		this.extent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = null;
		this.firstChildShapegroup = subgroups[0];
		this.nextShapegroup = null;
		this.previousShapegroup = null;
		this.parentShapegroup = null;
	}
	
	public void bringToFront() {
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent shape group");
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
	
	public void sendToBack() {
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent shape group");
		}
		
		if (parentShapegroup.firstChildShapegroup.previousShapegroup == this) {
			return;
		}

		previousShapegroup.nextShapegroup = nextShapegroup;
		nextShapegroup.previousShapegroup = previousShapegroup;

		nextShapegroup = parentShapegroup.firstChildShapegroup;
		previousShapegroup = parentShapegroup.firstChildShapegroup.previousShapegroup;

		parentShapegroup.firstChildShapegroup.previousShapegroup.nextShapegroup = this;
		parentShapegroup.firstChildShapegroup.previousShapegroup = this;
	}
	
	public IntPoint toGlobalCoordinates(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}

		return new IntPoint(getExtent().getLeft() + innerCoordinates.getX(), getExtent().getTop() + innerCoordinates.getY());
	}
	
	public IntPoint toInnerCoordinates(IntPoint globalCoordinates) {
		if (globalCoordinates == null) {
			throw new IllegalArgumentException("argument globalCoordinates is null");
		}
		
		return new IntPoint(globalCoordinates.getX() - getExtent().getLeft(), globalCoordinates.getY() - getExtent().getTop());
	}
	
	public IntVector toInnerCoordinates(IntVector relativeGlobalCoordinates) {
		if (relativeGlobalCoordinates == null) {
			throw new IllegalArgumentException("argument relativeGlobalCoordinates is null");
		}
		
		return new IntVector(relativeGlobalCoordinates.getX() - getExtent().getLeft(), relativeGlobalCoordinates.getY() - getExtent().getTop());
	}
	
	public void getDrawingCommands() {
		throw new RuntimeException("not yet implemented");		
	}
	
	// TODO: Give copy?
	public RoundedPolygon getShape() {
		return shape;
	}
	
	public ShapeGroup getSubgroup(int index) {
		if (index < 0 || index >= getSubgroupCount()) {
			throw new IllegalArgumentException("argument index is out of bounds");
		}
		
		List<ShapeGroup> subGroups = getSubgroups();
		
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group"); //TODO: Or return null ?
		}
		
		return subGroups.get(index);
	}
	
	public ShapeGroup getSubgroupAt(IntPoint innerCoordinates) {
		if (innerCoordinates == null) {
			throw new IllegalArgumentException("argument innerCoordinates is null");
		}

		List<ShapeGroup> subGroups = getSubgroups();
		
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group"); //TODO: Or return null ?
		}
		
		for (ShapeGroup subgroup : getSubgroups()) {
			if (subgroup.getOriginalExtent().contains(innerCoordinates)) {
				return subgroup;
			}
		}
		
		//TODO: Or return null ?
		throw new IllegalStateException("this shape group does not contain a subgroup of which the extent contains the given coordinates"); //TODO: Or return null?
	}
	
	public int getSubgroupCount() {
		List<ShapeGroup> subGroups = getSubgroups();
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group"); //TODO: Or return 0 ?
		}
		
		return subGroups.size();
	}
	
	public List<ShapeGroup> getSubgroups() {
		if (firstChildShapegroup == null) {
			return null;
		}
		
		List<ShapeGroup> subgroupsList = new ArrayList<ShapeGroup>();	
		
		ShapeGroup currentShapegroup = this;
		do {
			subgroupsList.add(currentShapegroup);
			currentShapegroup = currentShapegroup.nextShapegroup;
		} while (currentShapegroup != this);
		
		return subgroupsList;
	}
	
	public ShapeGroup getParentShapegroup() {
		return parentShapegroup;
	}
	
	public Extent getOriginalExtent() {
		return Extent.ofLeftTopWidthHeight(0, 0, getExtent().getRight() - getExtent().getLeft(), getExtent().getBottom() - getExtent().getTop());
	}
	
	public Extent getExtent() {
		return extent;
	}
	
	public void setExtent(Extent newExtent) {
		if (newExtent == null) {
			throw new IllegalArgumentException("argument newExtent is null");
		}
		
		this.extent = newExtent;
	}
}
