package drawit.shapegroups1;

import static org.junit.Assert.assertNotNull;

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
	private List<ShapeGroup> subgroups;
	private ShapeGroup parentShapegroup;
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
		this.subgroups = null;
		this.parentShapegroup = null;
	}
	
	// What if they already are in a subgroup ?
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
			} else if (subgroup.getExtent().getBottom() > bottom) {
				bottom = subgroup.getExtent().getBottom();
			}
		}
		
		this.extent = Extent.ofLeftTopRightBottom(left, top, right, bottom);
		this.shape = null;
		this.subgroups = new ArrayList<ShapeGroup>(Arrays.asList(subgroups));
		this.parentShapegroup = null;
	}
	
	public void bringToFront() {
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent shape group");
		}

		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(0, this);
	}
	
	public void sendToBack() {
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent shape group");
		}

		parentShapegroup.subgroups.remove(this);
		parentShapegroup.subgroups.add(this);
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
		
		return getSubgroups().get(index);
	}
	
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
	
	public int getSubgroupCount() {
		if (getSubgroups() == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group"); //TODO: Or return 0 ?
		}
		
		return getSubgroups().size();
	}
	
	// TODO: Make copy of list here?
	public List<ShapeGroup> getSubgroups() {
		return subgroups;
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
