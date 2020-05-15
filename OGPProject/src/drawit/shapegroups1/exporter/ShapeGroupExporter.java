package drawit.shapegroups1.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawit.IntPoint;
import drawit.shapegroups1.Extent;
import drawit.shapegroups1.LeafShapeGroup;
import drawit.shapegroups1.NonleafShapeGroup;
import drawit.shapegroups1.ShapeGroup;

public class ShapeGroupExporter {
	
	public static Object toPlainData(ShapeGroup shapeGroup) {
		Extent originalExtent = shapeGroup.getOriginalExtent();
		Extent extent = shapeGroup.getExtent();
		
		Map<String, Object> plainData = new HashMap<String, Object>();
		plainData.put("originalExtent", Map.of(
				"left", originalExtent.getLeft(),
				"top", originalExtent.getTop(),
				"right", originalExtent.getRight(),
				"bottom", originalExtent.getBottom()));
		plainData.put("extent", Map.of(
				"left", extent.getLeft(),
				"top", extent.getTop(),
				"right", extent.getRight(),
				"bottom", extent.getBottom()));
		
		if (shapeGroup instanceof NonleafShapeGroup) {
			NonleafShapeGroup nonleafShapeGroup = (NonleafShapeGroup) shapeGroup;
			List<Object> subGroupsData = new ArrayList<Object>();
			
			for (ShapeGroup subGroup : nonleafShapeGroup.getSubgroups()) {
				subGroupsData.add(toPlainData(subGroup));
			}
			
			plainData.put("subgroups", subGroupsData);
		} else if (shapeGroup instanceof LeafShapeGroup) {
			LeafShapeGroup leafShapeGroup = (LeafShapeGroup) shapeGroup;
			List<Object> vertices = new ArrayList<Object>();
			
			for (IntPoint vertex : leafShapeGroup.getShape().getVertices()) {
				vertices.add(Map.of(
						"x", vertex.getX(),
						"y", vertex.getY()));
			}
			
			plainData.put("shape", Map.of(
					"vertices", vertices,
					"radius", leafShapeGroup.getShape().getRadius(),
					"color", Map.of(
							"red", leafShapeGroup.getShape().getColor().getRed(),
							"green", leafShapeGroup.getShape().getColor().getGreen(),
							"blue", leafShapeGroup.getShape().getColor().getBlue())));
		}
		
		return plainData;
	}

}
