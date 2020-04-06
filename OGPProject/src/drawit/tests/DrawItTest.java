package drawit.tests;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import drawit.DoublePoint;
import drawit.DoubleVector;
import drawit.IntPoint;
import drawit.IntVector;
import drawit.PointArrays;
import drawit.RoundedPolygon;
import drawit.shapegroups1.Extent;
import drawit.shapegroups1.ShapeGroup;

class DrawItTest {

	@Test
	void testIntVector() {
		// IntVector: Constructor, getX, getY tests
		int intVector1x = -3;
		int intVector1y = 9;
		int intVector2x = 5;
		int intVector2y = 2;
		IntVector intVector1 = new IntVector(intVector1x, intVector1y);
		IntVector intVector2 = new IntVector(intVector2x, intVector2y);
		assert intVector1.getX() == intVector1x;
		assert intVector1.getY() == intVector1y;
		assert intVector2.getX() == intVector2x;
		assert intVector2.getY() == intVector2y;
		
		// IntVector: Dot product tests		
		long intVectorDotProduct = (long) intVector1.getX() * intVector2.getX()
				+ (long) intVector1.getY() * intVector2.getY();
		assert intVector1.dotProduct(intVector2) == intVectorDotProduct;
		assert intVector2.dotProduct(intVector1) == intVectorDotProduct;

		// IntVector: Cross product tests
		long intVectorCrossProduct1 = (long) intVector1.getX() * intVector2.getY() - (long) intVector1.getY() * intVector2.getX();
		long intVectorCrossProduct2 = (long) intVector2.getX() * intVector1.getY() - (long) intVector2.getY() * intVector1.getX();
		assert intVector1.crossProduct(intVector2) == intVectorCrossProduct1;
		assert intVector2.crossProduct(intVector1) == intVectorCrossProduct2;
		
		// IntVector: Collinear tests
		int intVector3x = -10;
		int intVector3y = -4;
		IntVector intVector3 = new IntVector(intVector3x, intVector3y);
		assert !intVector1.isCollinearWith(intVector3);
		assert !intVector3.isCollinearWith(intVector1);
		assert intVector2.isCollinearWith(intVector3);
		assert intVector3.isCollinearWith(intVector2);
		
		// IntVector: Convert to DoubleVector tests
		DoubleVector doubleVector = intVector1.asDoubleVector();
		assert doubleVector.getX() == intVector1.getX();
		assert doubleVector.getY() == intVector1.getY();
	}
	
	@Test
	void testDoubleVector() {
		// DoubleVector: Constructor, getX, getY tests
		double doubleVector1x = 3.2;
		double doubleVector1y = 7;
		double doubleVector2x = 4.2;
		double doubleVector2y = -0.3;
		DoubleVector doubleVector1 = new DoubleVector(doubleVector1x, doubleVector1y);
		DoubleVector doubleVector2 = new DoubleVector(doubleVector2x, doubleVector2y);
		assert doubleVector1.getX() == doubleVector1x;
		assert doubleVector1.getY() == doubleVector1y;
		assert doubleVector2.getX() == doubleVector2x;
		assert doubleVector2.getY() == doubleVector2y;
			
		// DoubleVector: Dot product tests		
		double doubleDotProduct = doubleVector1.getX() * doubleVector2.getX()
				+ doubleVector1.getY() * doubleVector2.getY();
		assert doubleVector1.dotProduct(doubleVector2) == doubleDotProduct;
		assert doubleVector2.dotProduct(doubleVector1) == doubleDotProduct;

		// DoubleVector: Cross product tests
		double doubleCrossProduct1 = doubleVector1.getX() * doubleVector2.getY()
				- doubleVector1.getY() * doubleVector2.getX();
		double doubleCrossProduct2 = doubleVector2.getX() * doubleVector1.getY()
				- doubleVector2.getY() * doubleVector1.getX();
		assert doubleVector1.crossProduct(doubleVector2) == doubleCrossProduct1;
		assert doubleVector2.crossProduct(doubleVector1) == doubleCrossProduct2;
		
		// DoubleVector: Plus tests
		double doubleVectorAdditionx = doubleVector1x + doubleVector2x;
		double doubleVectorAdditiony = doubleVector1y + doubleVector2y;
		DoubleVector doubleVectorAddition1 = doubleVector1.plus(doubleVector2);
		DoubleVector doubleVectorAddition2 = doubleVector2.plus(doubleVector1);
		assert doubleVectorAddition1.getX() == doubleVectorAdditionx;
		assert doubleVectorAddition2.getX() == doubleVectorAdditionx;
		assert doubleVectorAddition1.getY() == doubleVectorAdditiony;
		assert doubleVectorAddition2.getY() == doubleVectorAdditiony;
		
		// DoubleVector: Scale tests
		double doubleVector1Scaler = 3;
		double doubleVector2Scaler = -7.6;
		double doubleVector1Scaledx = doubleVector1x * doubleVector1Scaler;
		double doubleVector1Scaledy = doubleVector1y * doubleVector1Scaler;
		double doubleVector2Scaledx = doubleVector2x * doubleVector2Scaler;
		double doubleVector2Scaledy = doubleVector2y * doubleVector2Scaler;
		DoubleVector doubleVectorScaled1 = doubleVector1.scale(doubleVector1Scaler);
		DoubleVector doubleVectorScaled2 = doubleVector2.scale(doubleVector2Scaler);
		assert doubleVectorScaled1.getX() == doubleVector1Scaledx;
		assert doubleVectorScaled1.getY() == doubleVector1Scaledy;
		assert doubleVectorScaled2.getX() == doubleVector2Scaledx;
		assert doubleVectorScaled2.getY() == doubleVector2Scaledy;
		
		// DoubleVector: asAngle tests
		double doubleVector3x = -5.1;
		double doubleVector3y = 0;
		DoubleVector doubleVector3 = new DoubleVector(doubleVector3x, doubleVector3y);
		double doubleVector4x = 0;
		double doubleVector4y = 1.9;
		DoubleVector doubleVector4 = new DoubleVector(doubleVector4x, doubleVector4y);
		assert doubleVector1.asAngle() == Math.atan(doubleVector1y / doubleVector1x);
		assert 0 > doubleVector2.asAngle() && doubleVector2.asAngle() > -Math.PI / 2;
		assert doubleVector3.asAngle() == Math.PI;
		assert doubleVector4.asAngle() == Math.PI / 2;
		
		// DoubleVector: getSize tests
		DoubleVector doubleVector5 = new DoubleVector(0, 0);
		assert doubleVector5.getSize() == 0;
		assert doubleVector1.getSize() == Math.sqrt(Math.pow(doubleVector1x, 2) + Math.pow(doubleVector1y, 2));
		assert doubleVector2.getSize() == Math.sqrt(Math.pow(doubleVector2x, 2) + Math.pow(doubleVector2y, 2));
	}
	
	@Test
	void testIntPoint() {
		// IntPoint: constructor, asDoublePoint, getX and getY tests
		IntPoint intPoint1 = new IntPoint(5, -9);
		IntPoint intPoint2 = new IntPoint(2, 4);
		DoublePoint doublePoint1 = intPoint1.asDoublePoint();
		assert intPoint1.getX() == 5;
		assert intPoint1.getY() == -9;
		assert intPoint2.getX() == 2;
		assert intPoint2.getY() == 4;
		assert intPoint1.getX() == doublePoint1.getX();
		assert intPoint1.getY() == doublePoint1.getY();
		
		// IntPoint: equals, plus and minus tests
		IntPoint intPoint3 = new IntPoint(2, 4);
		assert intPoint2.equals(intPoint3);
		assert !intPoint1.equals(intPoint3);
		IntVector intVectorPlus = new IntVector(1, -5);
		IntPoint intPointPlus = intPoint1.plus(intVectorPlus);
		assert intPointPlus.getX() == 6;
		assert intPointPlus.getY() == -14;
		IntVector intVectorMinus = intPoint1.minus(intPoint2);
		assert intVectorMinus.getX() == 3;
		assert intVectorMinus.getY() == -13;
		
		// IntPoint: lineSegmentsIntersect tests
		IntPoint intPoint4 = new IntPoint(-5, -5);
		IntPoint intPoint5 = new IntPoint(10, 4);
		IntPoint intPoint6 = new IntPoint(6, -8);
		IntPoint intPoint7 = new IntPoint(3, 5);
		assert IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint4, intPoint5);
		assert !IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint1, intPoint4); // Common point
		assert !IntPoint.lineSegmentsIntersect(intPoint1, intPoint4, intPoint2, intPoint5); // Carrier lines intersect, line segments don't
		assert !IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint6, intPoint7);
		IntPoint intPoint8 = new IntPoint(3, 10);
		IntPoint intPoint9 = new IntPoint(6, 5);
		IntPoint intPoint10 = new IntPoint(18, -3);
		IntPoint intPoint11 = new IntPoint(-1, -2);
		assert !IntPoint.lineSegmentsIntersect(intPoint8, intPoint9, intPoint10, intPoint11);
		
		// IntPoint: isOneLineSegment tests
		assert !intPoint5.isOnLineSegment(intPoint1, intPoint2);
		assert !intPoint1.isOnLineSegment(intPoint1, intPoint2);
		IntPoint intPointOnLine = new IntPoint(6, 4);
		assert intPointOnLine.isOnLineSegment(intPoint3, intPoint5);
	}
	
	@Test
	void testDoublePoint() {
		// DoublePoint: constructor, round, getX and getY tests
		DoublePoint doublePoint2 = new DoublePoint(2.75, -3.40);
		DoublePoint doublePoint3 = new DoublePoint(1, 10.01);
		IntPoint doublePointRounded = doublePoint2.round();
		assert doublePoint2.getX() == 2.75;
		assert doublePoint2.getY() == -3.40;
		assert doublePoint3.getX() == 1;
		assert doublePoint3.getY() == 10.01;
		assert doublePointRounded.getX() == 3;
		assert doublePointRounded.getY() == -3;
		
		// DoublePoint: plus and minus tests
		DoubleVector doubleVectorPlus = new DoubleVector(1.5, 0.3);
		DoublePoint doublePointPlus = doublePoint2.plus(doubleVectorPlus);
		assert doublePointPlus.getX() == 4.25;
		assert doublePointPlus.getY() == -3.10;
		DoubleVector doubleVectorMinus = doublePoint2.minus(doublePoint3);
		assert doubleVectorMinus.getX() == 1.75;
		assert doubleVectorMinus.getY() == -13.41;
	}
	
	@Test
	void testPointArrays() {
		IntPoint intPoint1 = new IntPoint(5, -9);
		IntPoint intPoint2 = new IntPoint(2, 4);
		IntPoint intPoint3 = new IntPoint(2, 4);
		IntPoint intPoint4 = new IntPoint(-5, -5);
		IntPoint intPoint5 = new IntPoint(10, 4);
		IntPoint intPoint6 = new IntPoint(6, -8);
		
		// PointArrays: copy test
		IntPoint[] array1 = {intPoint1, intPoint2, intPoint3, intPoint4, intPoint5};
		IntPoint[] arrayCopy = PointArrays.copy(array1);
		assert Arrays.equals(array1, arrayCopy);
		
		// PointArrays: update test
		IntPoint[] array1Updated = {intPoint1, intPoint2, intPoint6, intPoint4, intPoint5};
		IntPoint[] arrayUpdate = PointArrays.update(array1, 2, intPoint6);
		assert Arrays.equals(array1Updated, arrayUpdate);
		
		// PointArrays: insert test
		IntPoint[] array1Inserted = {intPoint1, intPoint2, intPoint6, intPoint3, intPoint4, intPoint5};
		IntPoint[] arrayInsert = PointArrays.insert(array1, 2, intPoint6);
		assert Arrays.equals(array1Inserted, arrayInsert);
		
		// PointArrays: remove test
		IntPoint[] array1Removed = {intPoint1, intPoint2, intPoint4, intPoint5};
		IntPoint[] arrayRemove = PointArrays.remove(array1, 2);
		assert Arrays.equals(array1Removed, arrayRemove);
	}
	
	@Test
	void testRoundedPolygon() {
		// RoundedPolygon: constructor tests
		RoundedPolygon polygon1 = new RoundedPolygon();
		RoundedPolygon polygon2 = new RoundedPolygon();
		assert polygon1.getVertices().length == 0;
		assert polygon2.getVertices().length == 0;
		assert PointArrays.checkDefinesProperPolygon(polygon1.getVertices()) == null;
		assert PointArrays.checkDefinesProperPolygon(polygon2.getVertices()) == null;
		assert polygon1.getRadius() == 0;
		assert polygon2.getRadius() == 0;
		
		// RoundedPolygon: getVertices, setVertices tests
		// PointArrays: checkDefinesProperPolygon, insert, remove tests
		IntPoint[] vertices0 = new IntPoint[] {
				new IntPoint(0, 0),
				new IntPoint(5, 5),
				new IntPoint(0, 5),
				new IntPoint(5, 0)
		};
		IntPoint[] vertices1 = new IntPoint[] {
				new IntPoint(0, 0),
				new IntPoint(7, 5),
				new IntPoint(5, -3)
		};
		IntPoint[] vertices2 = new IntPoint[] {
				new IntPoint(0, 5),
				new IntPoint(3, 10),
				new IntPoint(6, 5),
				new IntPoint(9, 10),
				new IntPoint(12, 5),
				new IntPoint(15, 10),
				new IntPoint(18, 5),
				new IntPoint(20, 3),
				new IntPoint(18, 1),
				new IntPoint(20, -1),
				new IntPoint(18, -3),
				new IntPoint(-1, -2),
		};
		assert PointArrays.checkDefinesProperPolygon(vertices0).equals(
				"Edge from (0, 0) at index 0 to (5, 5) at index 1 intersects with edge from (0, 5) at index 2 to (5, 0) at index 3");
		vertices0 = PointArrays.insert(vertices0, 0, new IntPoint(0, 0));
		assert PointArrays.checkDefinesProperPolygon(vertices0).equals(
				"Vertices at index 0 and 1 coincide: (0, 0)");
		vertices0 = PointArrays.remove(vertices0, 0);
		assert PointArrays.checkDefinesProperPolygon(vertices0).equals(
				"Edge from (0, 0) at index 0 to (5, 5) at index 1 intersects with edge from (0, 5) at index 2 to (5, 0) at index 3");
		assert PointArrays.checkDefinesProperPolygon(vertices1) == null;
		assert PointArrays.checkDefinesProperPolygon(vertices2) == null;
		boolean thrownVertices = false;
		try {
			polygon1.setVertices(vertices0);
		} catch (IllegalArgumentException e) {
			thrownVertices = true;
		}
		assert thrownVertices;
		assert polygon1.getVertices().length == 0;
		polygon1.setVertices(vertices1);
		polygon2.setVertices(vertices2);
		for (int i = 0; i < polygon1.getVertices().length; i++) {
			assert polygon1.getVertices()[i].equals(vertices1[i]);
		}
		for (int i = 0; i < polygon2.getVertices().length; i++) {
			assert polygon2.getVertices()[i].equals(vertices2[i]);
		}
		IntPoint wrongPoint1 = new IntPoint(-999, -999);
		IntPoint tempPoint1 = vertices1[0];
		vertices1[0] = wrongPoint1;
		assert !polygon1.getVertices()[0].equals(wrongPoint1);
		vertices1[0] = tempPoint1;
		polygon1.getVertices()[0] = wrongPoint1;
		assert !polygon1.getVertices()[0].equals(wrongPoint1);
		
		// RoundedPolygon: getRadius, setRadius tests
		int radius0 = -3;
		int radius1 = 5;
		int radius2 = 0;
		boolean thrownRadius = false;
		try {
			polygon1.setRadius(radius0);
		} catch (IllegalArgumentException e) {
			thrownRadius = true;
		}
		assert thrownRadius;
		assert polygon1.getRadius() == 0;
		polygon1.setRadius(radius1);
		polygon2.setRadius(radius2);
		assert polygon1.getRadius() == radius1;
		assert polygon2.getRadius() == radius2;

		IntPoint extraPoint1 = new IntPoint(6, 100);
		IntPoint extraPoint2 = new IntPoint(8, -75);

		// RoundedPolygon: insert tests
		boolean thrownInsert = false;
		try {
			polygon1.insert(0, null);
		} catch (IllegalArgumentException e) {
			thrownInsert = true;
		}
		assert thrownInsert;
		thrownInsert = false;
		try {
			polygon1.insert(-1, wrongPoint1);
		} catch (IllegalArgumentException e) {
			thrownInsert = true;
		}
		assert thrownInsert;
		thrownInsert = false;
		try {
			polygon1.insert(polygon1.getVertices().length + 1, wrongPoint1);
		} catch (IllegalArgumentException e) {
			thrownInsert = true;
		}
		assert thrownInsert;
		IntPoint[] prevVertices1 = polygon1.getVertices();
		int insertIndex = 1;
		polygon1.insert(insertIndex, extraPoint1);
		assert polygon1.getVertices().length == prevVertices1.length + 1;
		assert polygon1.getVertices()[insertIndex].equals(extraPoint1);
		for (int i = insertIndex; i < prevVertices1.length; i++) {
			assert prevVertices1[i].equals(polygon1.getVertices()[i + 1]);
		}
		
		// RoundedPolygon: update tests
		IntPoint[] prevVertices2 = polygon1.getVertices();
		polygon1.update(insertIndex, extraPoint2);
		assert polygon1.getVertices().length == prevVertices2.length;
		assert polygon1.getVertices()[insertIndex].equals(extraPoint2);

		// RoundedPolygon: remove tests
		IntPoint[] prevVertices3 = polygon1.getVertices();
		polygon1.remove(insertIndex);
		assert polygon1.getVertices().length == prevVertices3.length - 1;
		for (int i = insertIndex; i < polygon1.getVertices().length; i++) {
			assert prevVertices3[i + 1].equals(polygon1.getVertices()[i]);
		}
		
		// RoundedPolygon: contains test
		IntPoint testPoint1 = new IntPoint(15, 8);
		IntPoint testPoint2 = new IntPoint(12, 8);
		IntPoint testPoint3 = new IntPoint(15, 10);
		IntPoint testPoint4 = new IntPoint(-2, 0);
		IntPoint testPoint5 = new IntPoint(2, 0);
		IntPoint testPoint6 = new IntPoint(10, -5);
		IntPoint testPoint7 = new IntPoint(19, 1);
		IntPoint testPoint8 = new IntPoint(19, 3);
		IntPoint testPoint9 = new IntPoint(20, 3);
		IntPoint testPoint10 = new IntPoint(19, 4);
		IntPoint testPoint11 = new IntPoint(19, 2);
		IntPoint testPoint12 = new IntPoint(22, 1);
		assert polygon2.contains(testPoint1);
		assert !polygon2.contains(testPoint2);
		assert polygon2.contains(testPoint3);
		assert !polygon2.contains(testPoint4);
		assert polygon2.contains(testPoint5);
		assert !polygon2.contains(testPoint6);
		assert !polygon2.contains(testPoint7);
		assert polygon2.contains(testPoint8);
		assert polygon2.contains(testPoint9);
		assert polygon2.contains(testPoint10);
		assert polygon2.contains(testPoint11);
		assert !polygon2.contains(testPoint12);
		IntPoint soloPoint = new IntPoint(0, 0);
		RoundedPolygon polygonSolo = new RoundedPolygon();
		assert PointArrays.checkDefinesProperPolygon(new IntPoint[] {soloPoint}) == null;
		polygonSolo.setVertices(new IntPoint[] {
				soloPoint
		});
		assert polygonSolo.contains(soloPoint);
		
		// RoundedPolygon: getDrawingCommands tests
		IntPoint[] vertices3 = new IntPoint[] {
				new IntPoint(100, 100),
				new IntPoint(200, 100),
				new IntPoint(200, 200),
				new IntPoint(100, 200),
		};
		RoundedPolygon polygon3 = new RoundedPolygon();
		polygon3.setVertices(vertices3);
		assert polygon3.getDrawingCommands().equals(
				"line 100.0 100.0 200.0 100.0\n" + 
				"line 200.0 100.0 200.0 200.0\n" + 
				"line 200.0 200.0 100.0 200.0\n" + 
				"line 100.0 200.0 100.0 100.0\n" +
				"fill 255 255 255");
		polygon3.setRadius(10);
		assert polygon3.getDrawingCommands().equals(
				"line 110.0 100.0 190.0 100.0\n" + 
				"arc 190.0 110.0 10.0 -1.5707963267948966 1.5707963267948966\n" + 
				"line 200.0 110.0 200.0 190.0\n" + 
				"arc 190.0 190.0 10.0 0.0 1.5707963267948966\n" + 
				"line 190.0 200.0 110.0 200.0\n" + 
				"arc 110.0 190.0 10.0 1.5707963267948966 1.5707963267948966\n" + 
				"line 100.0 190.0 100.0 110.0\n" + 
				"arc 110.0 110.0 10.0 3.141592653589793 1.5707963267948966\n" + 
				"fill 255 255 255");
		assert polygon1.getDrawingCommands().equals(
				"line 2.3724203629159777 1.6945859735114128 3.644890947114124 2.6034935336529457\n" + 
				"arc 4.527252921402365 1.3681867696494092 1.5180729412194702 2.1910458127777184 -2.4360244759045826\n" + 
				"line 6.0 1.0 5.707106781186548 -0.1715728752538097\n" + 
				"arc 3.6114106887179966 0.3523511478633279 2.160194087110508 -0.24497866312686406 -1.8662371639386166\n" + 
				"line 2.5 -1.5 2.5 -1.5\n" + 
				"arc 3.4834699396501465 0.13911656608357686 1.911521969375471 -2.111215827065481 -1.9809236673363877\n" + 
				"fill 255 255 255");
		assert polygon2.getDrawingCommands().equals(
				"line 0.0 5.0 3.0 10.0\n" + 
				"line 3.0 10.0 6.0 5.0\n" + 
				"line 6.0 5.0 9.0 10.0\n" + 
				"line 9.0 10.0 12.0 5.0\n" + 
				"line 12.0 5.0 15.0 10.0\n" + 
				"line 15.0 10.0 18.0 5.0\n" + 
				"line 18.0 5.0 20.0 3.0\n" + 
				"line 20.0 3.0 18.0 1.0\n" + 
				"line 18.0 1.0 20.0 -1.0\n" + 
				"line 20.0 -1.0 18.0 -3.0\n" + 
				"line 18.0 -3.0 -1.0 -2.0\n" + 
				"line -1.0 -2.0 0.0 5.0\n" +
				"fill 255 255 255");
	}
	
	@Test
	void testExtent() {
		// Extent: Static factory methods and getters tests
		drawit.shapegroups1.Extent extent1 = drawit.shapegroups1.Extent.ofLeftTopRightBottom(5, 2, 10, 10);
		drawit.shapegroups2.Extent extent2 = drawit.shapegroups2.Extent.ofLeftTopRightBottom(5, 2, 10, 10);
		drawit.shapegroups1.Extent extent3 = drawit.shapegroups1.Extent.ofLeftTopWidthHeight(1, 8, 3, 11);
		drawit.shapegroups2.Extent extent4 = drawit.shapegroups2.Extent.ofLeftTopWidthHeight(1, 8, 3, 11);
		
		assert extent1.getLeft() == 5;
		assert extent1.getTop() == 2;
		assert extent1.getRight() == 10;
		assert extent1.getBottom() == 10;
		assert extent1.getWidth() == 10-5;
		assert extent1.getHeight() == 10-2;
		
		assert extent2.getLeft() == 5;
		assert extent2.getTop() == 2;
		assert extent2.getRight() == 10;
		assert extent2.getBottom() == 10;
		assert extent2.getWidth() == 10-5;
		assert extent2.getHeight() == 10-2;
		
		assert extent3.getLeft() == 1;
		assert extent3.getTop() == 8;
		assert extent3.getRight() == 1+3;
		assert extent3.getBottom() == 8+11;
		assert extent3.getWidth() == 3;
		assert extent3.getHeight() == 11;
		
		assert extent4.getLeft() == 1;
		assert extent4.getTop() == 8;
		assert extent4.getRight() == 1+3;
		assert extent4.getBottom() == 8+11;
		assert extent4.getWidth() == 3;
		assert extent4.getHeight() == 11;
		
		drawit.shapegroups1.Extent extent5 = extent1.withLeft(4);
		drawit.shapegroups1.Extent extent6 = extent1.withTop(9);
		drawit.shapegroups1.Extent extent7 = extent1.withRight(6);
		drawit.shapegroups1.Extent extent8 = extent1.withBottom(21);
		drawit.shapegroups1.Extent extent9 = extent1.withWidth(7);
		drawit.shapegroups1.Extent extent10 = extent1.withHeight(1);
		drawit.shapegroups2.Extent extent11 = extent2.withLeft(4);
		drawit.shapegroups2.Extent extent12 = extent2.withTop(9);
		drawit.shapegroups2.Extent extent13 = extent2.withRight(6);
		drawit.shapegroups2.Extent extent14 = extent2.withBottom(21);
		drawit.shapegroups2.Extent extent15 = extent2.withWidth(7);
		drawit.shapegroups2.Extent extent16 = extent2.withHeight(1);
		
		assert extent5.getLeft() == 4;
		assert extent5.getTop() == 2;
		assert extent5.getRight() == 10;
		assert extent5.getBottom() == 10;
		assert extent5.getWidth() == 10-4;
		assert extent5.getHeight() == 10-2;
		assert extent11.getLeft() == 4;
		assert extent11.getTop() == 2;
		assert extent11.getRight() == 10;
		assert extent11.getBottom() == 10;
		assert extent11.getWidth() == 10-4;
		assert extent11.getHeight() == 10-2;
		
		assert extent6.getLeft() == 5;
		assert extent6.getTop() == 9;
		assert extent6.getRight() == 10;
		assert extent6.getBottom() == 10;
		assert extent6.getWidth() == 10-5;
		assert extent6.getHeight() == 10-9;
		assert extent12.getLeft() == 5;
		assert extent12.getTop() == 9;
		assert extent12.getRight() == 10;
		assert extent12.getBottom() == 10;
		assert extent12.getWidth() == 10-5;
		assert extent12.getHeight() == 10-9;
		
		assert extent7.getLeft() == 5;
		assert extent7.getTop() == 2;
		assert extent7.getRight() == 6;
		assert extent7.getBottom() == 10;
		assert extent7.getWidth() == 6-5;
		assert extent7.getHeight() == 10-2;
		assert extent13.getLeft() == 5;
		assert extent13.getTop() == 2;
		assert extent13.getRight() == 6;
		assert extent13.getBottom() == 10;
		assert extent13.getWidth() == 6-5;
		assert extent13.getHeight() == 10-2;
		
		assert extent8.getLeft() == 5;
		assert extent8.getTop() == 2;
		assert extent8.getRight() == 10;
		assert extent8.getBottom() == 21;
		assert extent8.getWidth() == 10-5;
		assert extent8.getHeight() == 21-2;
		assert extent14.getLeft() == 5;
		assert extent14.getTop() == 2;
		assert extent14.getRight() == 10;
		assert extent14.getBottom() == 21;
		assert extent14.getWidth() == 10-5;
		assert extent14.getHeight() == 21-2;
		
		assert extent9.getLeft() == 5;
		assert extent9.getTop() == 2;
		assert extent9.getRight() == 12;
		assert extent9.getBottom() == 10;
		assert extent9.getWidth() == 12-5;
		assert extent9.getHeight() == 10-2;
		assert extent15.getLeft() == 5;
		assert extent15.getTop() == 2;
		assert extent15.getRight() == 12;
		assert extent15.getBottom() == 10;
		assert extent15.getWidth() == 12-5;
		assert extent15.getHeight() == 10-2;
		
		assert extent10.getLeft() == 5;
		assert extent10.getTop() == 2;
		assert extent10.getRight() == 10;
		assert extent10.getBottom() == 3;
		assert extent10.getWidth() == 10-5;
		assert extent10.getHeight() == 3-2;
		assert extent16.getLeft() == 5;
		assert extent16.getTop() == 2;
		assert extent16.getRight() == 10;
		assert extent16.getBottom() == 3;
		assert extent16.getWidth() == 10-5;
		assert extent16.getHeight() == 3-2;
		
		// Extent: getTopLeft, getBottomRight, contains tests
		IntPoint extentIntPoint1 = extent1.getTopLeft();
		IntPoint extentIntPoint2 = extent1.getBottomRight();
		IntPoint extentIntPoint3 = extent2.getTopLeft();
		IntPoint extentIntPoint4 = extent2.getBottomRight();
		
		assert extentIntPoint1.getX() == extent1.getLeft();
		assert extentIntPoint1.getY() == extent1.getTop();
		assert extentIntPoint2.getX() == extent1.getRight();
		assert extentIntPoint2.getY() == extent1.getBottom();
		
		assert extentIntPoint3.getX() == extent2.getLeft();
		assert extentIntPoint3.getY() == extent2.getTop();
		assert extentIntPoint4.getX() == extent2.getRight();
		assert extentIntPoint4.getY() == extent2.getBottom();
		
		IntPoint extentIntPoint5 = new IntPoint(5, 2);
		IntPoint extentIntPoint6 = new IntPoint(10, 6);
		IntPoint extentIntPoint7 = new IntPoint(6, 7);
		IntPoint extentIntPoint8 = new IntPoint(11, 1);
		
		assert extent1.contains(extentIntPoint5);
		assert extent1.contains(extentIntPoint6);
		assert extent1.contains(extentIntPoint7);
		assert !extent1.contains(extentIntPoint8);
		
		assert extent2.contains(extentIntPoint5);
		assert extent2.contains(extentIntPoint6);
		assert extent2.contains(extentIntPoint7);
		assert !extent2.contains(extentIntPoint8);
	}
	
	@Test
	void testShapeGroup() {
		RoundedPolygon triangle = new RoundedPolygon();
		triangle.setVertices(new IntPoint[] {new IntPoint(10, 10), new IntPoint(30, 10), new IntPoint(20, 20)});
		
		// ShapeGroup: leaf group constructor, getExtent tests
		drawit.shapegroups1.ShapeGroup di1leaf1 = new drawit.shapegroups1.ShapeGroup(triangle);
		drawit.shapegroups2.ShapeGroup di2leaf1 = new drawit.shapegroups2.ShapeGroup(triangle);
		assert di1leaf1.getExtent().getTopLeft().equals(new IntPoint(10, 10)) && di1leaf1.getExtent().getBottomRight().equals(new IntPoint(30, 20));
		assert di2leaf1.getExtent().getTopLeft().equals(new IntPoint(10, 10)) && di2leaf1.getExtent().getBottomRight().equals(new IntPoint(30, 20));
		
		// ShapeGroup: setExtent, getExtent, getOriginalExtent tests
		di1leaf1.setExtent(drawit.shapegroups1.Extent.ofLeftTopWidthHeight(0, 0, 20, 10));
		di2leaf1.setExtent(drawit.shapegroups2.Extent.ofLeftTopWidthHeight(0, 0, 20, 10));
		assert di1leaf1.getOriginalExtent().getTopLeft().equals(new IntPoint(10, 10)) && di1leaf1.getOriginalExtent().getBottomRight().equals(new IntPoint(30, 20));
		assert di2leaf1.getOriginalExtent().getTopLeft().equals(new IntPoint(10, 10)) && di2leaf1.getOriginalExtent().getBottomRight().equals(new IntPoint(30, 20));
		assert di1leaf1.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di1leaf1.getExtent().getBottomRight().equals(new IntPoint(20, 10));
		assert di2leaf1.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di2leaf1.getExtent().getBottomRight().equals(new IntPoint(20, 10));
		drawit.shapegroups1.ShapeGroup di1leaf2 = new drawit.shapegroups1.ShapeGroup(triangle);
		drawit.shapegroups2.ShapeGroup di2leaf2 = new drawit.shapegroups2.ShapeGroup(triangle);
		di1leaf2.setExtent(drawit.shapegroups1.Extent.ofLeftTopWidthHeight(0, 0, 20, 10));
		di2leaf2.setExtent(drawit.shapegroups2.Extent.ofLeftTopWidthHeight(0, 0, 20, 10));

		// ShapeGroup: non-leaf group constructor, getExtent tests
		drawit.shapegroups1.ShapeGroup di1nonLeaf = new drawit.shapegroups1.ShapeGroup(new drawit.shapegroups1.ShapeGroup[] {di1leaf1, di1leaf2});
		drawit.shapegroups2.ShapeGroup di2nonLeaf = new drawit.shapegroups2.ShapeGroup(new drawit.shapegroups2.ShapeGroup[] {di2leaf1, di2leaf2});
		assert di1nonLeaf.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di1nonLeaf.getExtent().getBottomRight().equals(new IntPoint(20, 10));
		assert di2nonLeaf.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di2nonLeaf.getExtent().getBottomRight().equals(new IntPoint(20, 10));
		di1nonLeaf.setExtent(drawit.shapegroups1.Extent.ofLeftTopWidthHeight(0, 0, 10, 5));
		di2nonLeaf.setExtent(drawit.shapegroups2.Extent.ofLeftTopWidthHeight(0, 0, 10, 5));

		// ShapeGroup: setExtent, getExtent, getOriginalExtent tests
		di1leaf1.setExtent(drawit.shapegroups1.Extent.ofLeftTopWidthHeight(1000, 2000, 20, 10));
		di2leaf1.setExtent(drawit.shapegroups2.Extent.ofLeftTopWidthHeight(1000, 2000, 20, 10));
		di1leaf2.setExtent(drawit.shapegroups1.Extent.ofLeftTopWidthHeight(1000, 2000, 20, 10));
		di2leaf2.setExtent(drawit.shapegroups2.Extent.ofLeftTopWidthHeight(1000, 2000, 20, 10));
		assert di1leaf1.getOriginalExtent().getTopLeft().equals(new IntPoint(10, 10)) && di1leaf1.getOriginalExtent().getBottomRight().equals(new IntPoint(30, 20));
		assert di2leaf1.getOriginalExtent().getTopLeft().equals(new IntPoint(10, 10)) && di2leaf1.getOriginalExtent().getBottomRight().equals(new IntPoint(30, 20));
		assert di1leaf1.getExtent().getTopLeft().equals(new IntPoint(1000, 2000)) && di1leaf1.getExtent().getBottomRight().equals(new IntPoint(1020, 2010));
		assert di2leaf1.getExtent().getTopLeft().equals(new IntPoint(1000, 2000)) && di2leaf1.getExtent().getBottomRight().equals(new IntPoint(1020, 2010));
		assert di1nonLeaf.getOriginalExtent().getTopLeft().equals(new IntPoint(0, 0)) && di1nonLeaf.getOriginalExtent().getBottomRight().equals(new IntPoint(20, 10));
		assert di2nonLeaf.getOriginalExtent().getTopLeft().equals(new IntPoint(0, 0)) && di2nonLeaf.getOriginalExtent().getBottomRight().equals(new IntPoint(20, 10));
		assert di1nonLeaf.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di1nonLeaf.getExtent().getBottomRight().equals(new IntPoint(10, 5));
		assert di2nonLeaf.getExtent().getTopLeft().equals(new IntPoint(0, 0)) && di2nonLeaf.getExtent().getBottomRight().equals(new IntPoint(10, 5));
		
		// ShapeGroup: toInnerCoordinates, toGlobalCoordinates tests
		assert di1leaf1.toInnerCoordinates(new IntPoint(500, 1000)).equals(new IntPoint(10, 10));
		assert di2leaf1.toInnerCoordinates(new IntPoint(500, 1000)).equals(new IntPoint(10, 10));
		assert di1leaf1.toGlobalCoordinates(new IntPoint(10, 10)).equals(new IntPoint(500, 1000));
		assert di2leaf1.toGlobalCoordinates(new IntPoint(10, 10)).equals(new IntPoint(500, 1000));

		// ShapeGroup: constructor exceptions tests
		boolean thrown = false;
		try {
			RoundedPolygon nullPolygon = null;
			drawit.shapegroups1.ShapeGroup excGroup = new drawit.shapegroups1.ShapeGroup(nullPolygon);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			RoundedPolygon nullPolygon = null;
			drawit.shapegroups2.ShapeGroup excGroup = new drawit.shapegroups2.ShapeGroup(nullPolygon);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			drawit.shapegroups1.ShapeGroup[] smallPolygonGroup = new drawit.shapegroups1.ShapeGroup[] {new drawit.shapegroups1.ShapeGroup(new RoundedPolygon())};
			drawit.shapegroups1.ShapeGroup excGroup = new drawit.shapegroups1.ShapeGroup(smallPolygonGroup);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			drawit.shapegroups2.ShapeGroup[] smallPolygonGroup = new drawit.shapegroups2.ShapeGroup[] {new drawit.shapegroups2.ShapeGroup(new RoundedPolygon())};
			drawit.shapegroups2.ShapeGroup excGroup = new drawit.shapegroups2.ShapeGroup(smallPolygonGroup);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			drawit.shapegroups1.ShapeGroup[] nullPolygonGroup = new drawit.shapegroups1.ShapeGroup[] {new drawit.shapegroups1.ShapeGroup(new RoundedPolygon()), null};
			drawit.shapegroups1.ShapeGroup excGroup = new drawit.shapegroups1.ShapeGroup(nullPolygonGroup);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			drawit.shapegroups2.ShapeGroup[] nullPolygonGroup = new drawit.shapegroups2.ShapeGroup[] {new drawit.shapegroups2.ShapeGroup(new RoundedPolygon()), null};
			drawit.shapegroups2.ShapeGroup excGroup = new drawit.shapegroups2.ShapeGroup(nullPolygonGroup);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown;
		
		// ShapeGroup: constructor, getShape, getParentGroup tests
		RoundedPolygon rp1 = new RoundedPolygon();
		rp1.setVertices(new IntPoint[]{new IntPoint(0, 0), new IntPoint(20, 0), new IntPoint(10, 10)});
		RoundedPolygon rp2 = new RoundedPolygon();
		rp2.setVertices(new IntPoint[]{new IntPoint(-20, 0), new IntPoint(-5, 0), new IntPoint(-10, 10)});
		RoundedPolygon rp3 = new RoundedPolygon();
		rp3.setVertices(new IntPoint[]{new IntPoint(0, 20), new IntPoint(20, 20), new IntPoint(10, 40)});
		RoundedPolygon rp4 = new RoundedPolygon();
		rp4.setVertices(new IntPoint[]{new IntPoint(-20, 20), new IntPoint(-5, 20), new IntPoint(-15, 40)});
		drawit.shapegroups1.ShapeGroup di1lsg1 = new drawit.shapegroups1.ShapeGroup(rp1);
		drawit.shapegroups1.ShapeGroup di1lsg2 = new drawit.shapegroups1.ShapeGroup(rp2);
		drawit.shapegroups1.ShapeGroup di1lsg3 = new drawit.shapegroups1.ShapeGroup(rp3);
		drawit.shapegroups1.ShapeGroup di1lsg4 = new drawit.shapegroups1.ShapeGroup(rp4);
		assert di1lsg1.getShape() == rp1;
		assert di1lsg2.getShape() == rp2;
		assert di1lsg3.getShape() == rp3;
		assert di1lsg4.getShape() == rp4;
		drawit.shapegroups1.ShapeGroup[] di1lsgGroup = new drawit.shapegroups1.ShapeGroup[] {di1lsg1, di1lsg2, di1lsg3, di1lsg4};
		drawit.shapegroups1.ShapeGroup di1psg = new drawit.shapegroups1.ShapeGroup(di1lsgGroup);
		drawit.shapegroups2.ShapeGroup di2lsg1 = new drawit.shapegroups2.ShapeGroup(rp1);
		drawit.shapegroups2.ShapeGroup di2lsg2 = new drawit.shapegroups2.ShapeGroup(rp2);
		drawit.shapegroups2.ShapeGroup di2lsg3 = new drawit.shapegroups2.ShapeGroup(rp3);
		drawit.shapegroups2.ShapeGroup di2lsg4 = new drawit.shapegroups2.ShapeGroup(rp4);
		assert di2lsg1.getShape() == rp1;
		assert di2lsg2.getShape() == rp2;
		assert di2lsg3.getShape() == rp3;
		assert di2lsg4.getShape() == rp4;
		drawit.shapegroups2.ShapeGroup[] di2lsgGroup = new drawit.shapegroups2.ShapeGroup[] {di2lsg1, di2lsg2, di2lsg3, di2lsg4};
		drawit.shapegroups2.ShapeGroup di2psg = new drawit.shapegroups2.ShapeGroup(di2lsgGroup);

		// ShapeGroup: getSubroups, getSubgroup, getSubgroupCount, getParen(), order and hierarchy tests
		assert di1psg.getSubgroupCount() == di1lsgGroup.length;
		for (int i = 0; i < di1psg.getSubgroupCount(); i++) {
			assert di1psg.getSubgroups().get(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i).getParentGroup() == di1psg;
		}
		assert di2psg.getSubgroupCount() == di2lsgGroup.length;
		for (int i = 0; i < di2psg.getSubgroupCount(); i++) {
			assert di2psg.getSubgroups().get(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i).getParentGroup() == di2psg;
		}
		
		// ShapeGroup: bringToFront, sendToBack exceptions tests
		thrown = false;
		try {
			di1psg.bringToFront();
		} catch (IllegalStateException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			di1psg.sendToBack();
		} catch (IllegalStateException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			di2psg.bringToFront();
		} catch (IllegalStateException e) {
			thrown = true;
		}
		assert thrown;
		thrown = false;
		try {
			di2psg.sendToBack();
		} catch (IllegalStateException e) {
			thrown = true;
		}
		assert thrown;
		assert di1psg.getSubgroupCount() == di1lsgGroup.length;
		for (int i = 0; i < di1psg.getSubgroupCount(); i++) {
			assert di1psg.getSubgroups().get(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i).getParentGroup() == di1psg;
		}
		assert di2psg.getSubgroupCount() == di2lsgGroup.length;
		for (int i = 0; i < di2psg.getSubgroupCount(); i++) {
			assert di2psg.getSubgroups().get(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i).getParentGroup() == di2psg;
		}

		// ShapeGroup: bringToFront, sendToBack, order and hierarchy  tests
		di1lsg1.bringToFront();
		di2lsg1.bringToFront();
		assert di1psg.getSubgroupCount() == di1lsgGroup.length;
		for (int i = 0; i < di1psg.getSubgroupCount(); i++) {
			assert di1psg.getSubgroups().get(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i).getParentGroup() == di1psg;
		}
		assert di2psg.getSubgroupCount() == di2lsgGroup.length;
		for (int i = 0; i < di2psg.getSubgroupCount(); i++) {
			assert di2psg.getSubgroups().get(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i).getParentGroup() == di2psg;
		}
		di1lsg4.sendToBack();
		di2lsg4.sendToBack();
		assert di1psg.getSubgroupCount() == di1lsgGroup.length;
		for (int i = 0; i < di1psg.getSubgroupCount(); i++) {
			assert di1psg.getSubgroups().get(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i) == di1lsgGroup[i];
			assert di1psg.getSubgroup(i).getParentGroup() == di1psg;
		}
		assert di2psg.getSubgroupCount() == di2lsgGroup.length;
		for (int i = 0; i < di2psg.getSubgroupCount(); i++) {
			assert di2psg.getSubgroups().get(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i) == di2lsgGroup[i];
			assert di2psg.getSubgroup(i).getParentGroup() == di2psg;
		}
		di1lsg3.bringToFront();
		assert di1psg.getSubgroup(0) == di1lsg3;
		assert di1psg.getSubgroup(1) == di1lsg1;
		assert di1psg.getSubgroup(2) == di1lsg2;
		assert di1psg.getSubgroup(3) == di1lsg4;
		di2lsg3.bringToFront();
		assert di2psg.getSubgroup(0) == di2lsg3;
		assert di2psg.getSubgroup(1) == di2lsg1;
		assert di2psg.getSubgroup(2) == di2lsg2;
		assert di2psg.getSubgroup(3) == di2lsg4;
		di1lsg1.sendToBack();
		assert di1psg.getSubgroup(0) == di1lsg3;
		assert di1psg.getSubgroup(1) == di1lsg2;
		assert di1psg.getSubgroup(2) == di1lsg4;
		assert di1psg.getSubgroup(3) == di1lsg1;
		di2lsg1.sendToBack();
		assert di2psg.getSubgroup(0) == di2lsg3;
		assert di2psg.getSubgroup(1) == di2lsg2;
		assert di2psg.getSubgroup(2) == di2lsg4;
		assert di2psg.getSubgroup(3) == di2lsg1;

		// ShapeGroup: toInnerCoordinates test
		RoundedPolygon rp100 = new RoundedPolygon();
		rp100.setVertices(new IntPoint[] {new IntPoint(50, 50), new IntPoint(100, 50), new IntPoint(100, 100), new IntPoint(50, 100)});
		drawit.shapegroups1.ShapeGroup di1sg100 = new drawit.shapegroups1.ShapeGroup(rp100);
		assert di1sg100.getExtent().getTopLeft().equals(new IntPoint(50, 50)) && di1sg100.getExtent().getBottomRight().equals(new IntPoint(100, 100));
		assert di1sg100.toInnerCoordinates(new IntVector(100, 100)).getX() == 100 && di1sg100.toInnerCoordinates(new IntVector(100, 100)).getY() == 100;
		di1sg100.setExtent(Extent.ofLeftTopWidthHeight(di1sg100.getExtent().getLeft(), di1sg100.getExtent().getTop(), di1sg100.getExtent().getWidth() * 2, di1sg100.getExtent().getHeight() * 2));
		assert di1sg100.getExtent().getTopLeft().equals(new IntPoint(50, 50)) && di1sg100.getExtent().getBottomRight().equals(new IntPoint(150, 150));
		System.out.println("X: " + di1sg100.toInnerCoordinates(new IntVector(150, 150)).getX() + ", Y: " + di1sg100.toInnerCoordinates(new IntVector(150, 150)).getY());
		// If you moved from the point at (150, 150) from (150, 150) to (250, 250) which is a displacement of (100, 100) in Global, what is the displacement in Inner
		// The displacement in Inner is (50, 50) because you want to move the point from (100, 100) to (150, 150)
		assert di1sg100.toInnerCoordinates(new IntVector(150, 150)).getX() == 100 && di1sg100.toInnerCoordinates(new IntVector(150, 150)).getY() == 100;
		
		// ShapeGroup: getDrawingCommands tests
	}
}
