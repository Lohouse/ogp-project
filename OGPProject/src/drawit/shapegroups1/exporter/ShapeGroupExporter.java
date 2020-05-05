package drawit.shapegroups1.exporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import drawit.shapegroups1.Extent;
import drawit.shapegroups1.NonleafShapeGroup;
import drawit.shapegroups1.ShapeGroup;

public class ShapeGroupExporter {
	
	public static Object toPlainData(ShapeGroup shapeGroup) {
		Extent originalExtent = shapeGroup.getOriginalExtent();
		Extent extent = shapeGroup.getExtent();
		
		Map<String, Object> plainData = Map.of(
				"originalExtent", Map.of("left", originalExtent.getLeft(),
										 "top", originalExtent.getTop(),
										 "right", originalExtent.getRight(),
										 "bottom", originalExtent.getBottom()),
				"extent", Map.of("left", extent.getLeft(),
								 "top", extent.getTop(),
								 "right", extent.getRight(),
								 "bottom", extent.getBottom())
		);
		
		if (shapeGroup instanceof NonleafShapeGroup) {
			NonleafShapeGroup nonleafShapeGroup = (NonleafShapeGroup) shapeGroup;
			List<Object> subGroupsData = new ArrayList<Object>();
			
			for (ShapeGroup subGroup : nonleafShapeGroup.getSubgroups()) {
				subGroupsData.add(toPlainData(subGroup));
			}
			
			plainData.put("subgroups", subGroupsData);
		}		
		
		return plainData;
	}

}
