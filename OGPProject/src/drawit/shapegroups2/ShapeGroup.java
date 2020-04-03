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
	private Extent originalExtent;
	
	// What if this polygon already is in a subgroup ?
	// Throw exception if subgroups contains the same instance twice?
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
	
	public void sendToBack() {
		if (parentShapegroup == null) {
			throw new IllegalStateException("this shape group is not part of a parent group");
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

		int x = innerCoordinates.getX();
		int y = innerCoordinates.getY();
		
		List<ShapeGroup> operatingShapegroups = new ArrayList<ShapeGroup>();
		ShapeGroup testOperatingShapegroup = this;
		while (testOperatingShapegroup != null) {
			operatingShapegroups.add(0, testOperatingShapegroup);
			testOperatingShapegroup = testOperatingShapegroup.getParentGroup();
		}
		
		for (ShapeGroup operatingShapegroup : operatingShapegroups) {
			x = operatingShapegroup.getExtent().getLeft() + (x - operatingShapegroup.getOriginalExtent().getLeft()) /
					operatingShapegroup.getOriginalExtent().getWidth() * operatingShapegroup.getExtent().getWidth();
			y = operatingShapegroup.getExtent().getTop() + (y - operatingShapegroup.getOriginalExtent().getTop()) /
					operatingShapegroup.getOriginalExtent().getHeight() * operatingShapegroup.getExtent().getHeight();
		}
		
		return new IntPoint(x, y);
	}
	
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
			for (ShapeGroup childShapegroup : getSubgroups()) {
				commands += childShapegroup.getDrawingCommands() + bc;				
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
			if (subgroup.getExtent().contains(innerCoordinates)) {
				return subgroup;
			}
		}
		
		return null;
	}
	
	public int getSubgroupCount() {
		List<ShapeGroup> subGroups = getSubgroups();
		if (subGroups == null) {
			throw new IllegalStateException("this shape group is not a non-leaf shape group"); //TODO: Or return 0 ?
		}
		
		return subGroups.size();
	}
	
	public List<ShapeGroup> getSubgroups() {
		List<ShapeGroup> subgroupsList = new ArrayList<ShapeGroup>();
		
		if (firstChildShapegroup == null) {
			return subgroupsList;
		}
		
		ShapeGroup currentShapegroup = firstChildShapegroup;
		do {
			subgroupsList.add(currentShapegroup);
			currentShapegroup = currentShapegroup.nextShapegroup;
		} while (currentShapegroup != firstChildShapegroup);
		
		return subgroupsList;
	}
	
	public ShapeGroup getParentGroup() {
		return parentShapegroup;
	}
	
	public Extent getOriginalExtent() {
		return originalExtent;
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
