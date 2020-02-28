package drawit;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

class DrawItTest {

	@Test
	void test() {		
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
		
		// DoubleVector: Constructor tests
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
		assert 0 > doubleVector2.asAngle() && doubleVector2.asAngle() > -Math.PI / 2; // Is in 4th quadrant ?
		assert doubleVector3.asAngle() == Math.PI;
		assert doubleVector4.asAngle() == Math.PI / 2;
		
		// DoubleVector: getSize tests
		DoubleVector doubleVector5 = new DoubleVector(0, 0);
		assert doubleVector5.getSize() == 0;
		assert doubleVector1.getSize() == Math.sqrt(Math.pow(doubleVector1x, 2) + Math.pow(doubleVector1y, 2));
		assert doubleVector2.getSize() == Math.sqrt(Math.pow(doubleVector2x, 2) + Math.pow(doubleVector2y, 2));
		
		// IntPoint: constructor, asDoublePoint, getX and getY tests
		IntPoint intPoint1 = new IntPoint(5, -9);
		IntPoint intPoint2 = new IntPoint(2, 4);
		DoublePoint doublePoint1 = intPoint1.asDoublePoint();
		assert intPoint1.getX() == 5;
		assert intPoint1.getY() == -9;
		assert intPoint1.getX() == 2;
		assert intPoint1.getY() == 4;
		assert intPoint1.getX() == doublePoint1.getX();
		assert intPoint1.getY() == doublePoint1.getY();
		
		// IntPoint: equals, plus and minus tests
		IntPoint intPoint3 = new IntPoint(2, 4);
		assert intPoint2.equals(intPoint3) == true;
		assert intPoint1.equals(intPoint3) == false;
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
		assert IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint4, intPoint5) == true;
		assert IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint1, intPoint3) == true;
		assert IntPoint.lineSegmentsIntersect(intPoint1, intPoint4, intPoint2, intPoint5) == true;
		assert IntPoint.lineSegmentsIntersect(intPoint1, intPoint2, intPoint6, intPoint7) == false;
		
		// IntPoint: isOneLineSegment tests
		assert intPoint5.isOnLineSegment(intPoint1, intPoint2) == false;
		assert intPoint1.isOnLineSegment(intPoint1, intPoint2) == false;
		IntPoint intPointOnLine = new IntPoint(6, 4);
		assert intPointOnLine.isOnLineSegment(intPoint3, intPoint5) == true;
		
		// DoublePoint: constructor, round, getX and getY tests
		DoublePoint doublePoint2 = new DoublePoint(2.75, -3.40);
		DoublePoint doublePoint3 = new DoublePoint(1, 10.01);
		IntPoint doublePointRounded = doublePoint2.round();
		assert doublePoint2.getX() == 2.75;
		assert doublePoint2.getY() == -3.50;
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
			
		
		// RoundedPolygon: constructor tests
		RoundedPolygon polygon1 = new RoundedPolygon();
		RoundedPolygon polygon2 = new RoundedPolygon();
		assert polygon1.getVertices().length == 0;
		assert polygon2.getVertices().length == 0;
		assert polygon1.getRadius() == 0;
		assert polygon2.getRadius() == 0;
		
		// RoundedPolygon: getVertices, setVertices tests
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
			assert polygon1.getVertices()[i].getX() == vertices1[i].getX() 
					&& polygon1.getVertices()[i].getY() == vertices1[i].getY();
		}
		for (int i = 0; i < polygon2.getVertices().length; i++) {
			assert polygon2.getVertices()[i].getX() == vertices2[i].getX() 
					&& polygon2.getVertices()[i].getY() == vertices2[i].getY();
		}
		
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
		IntPoint[] prevVertices1 = polygon1.getVertices();
		int insertIndex = 1;
		polygon1.insert(insertIndex, extraPoint1);
		assert polygon1.getVertices().length == prevVertices1.length + 1;
		assert polygon1.getVertices()[insertIndex].getX() == extraPoint1.getX() 
				&& polygon1.getVertices()[insertIndex].getY() == extraPoint1.getY();
		for (int i = insertIndex; i < prevVertices1.length; i++) {
			assert prevVertices1[i].getX() == polygon1.getVertices()[i + 1].getX() 
					&& prevVertices1[i].getY() == polygon1.getVertices()[i + 1].getY();
		}
		
		// RoundedPolygon: update tests
		IntPoint[] prevVertices2 = polygon1.getVertices();
		polygon1.update(insertIndex, extraPoint2);
		assert polygon1.getVertices().length == prevVertices2.length;
		assert polygon1.getVertices()[insertIndex].getX() == extraPoint2.getX() 
				&& polygon1.getVertices()[insertIndex].getY() == extraPoint2.getY();

		// RoundedPolygon: remove tests
		IntPoint[] prevVertices3 = polygon1.getVertices();
		polygon1.remove(insertIndex);
		assert polygon1.getVertices().length == prevVertices3.length - 1;
		for (int i = insertIndex; i < polygon1.getVertices().length; i++) {
			assert prevVertices3[i + 1].getX() == polygon1.getVertices()[i].getX() 
					&& prevVertices3[i + 1].getY() == polygon1.getVertices()[i].getY();
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
		
		// RoundedPolygon: getDrawingCommands tests
		IntPoint[] vertices3 = new IntPoint[] {
				new IntPoint(100, 100),
				new IntPoint(200, 100),
				new IntPoint(200, 200),
				new IntPoint(100, 200),
		};
		RoundedPolygon polygon3 = new RoundedPolygon();
		polygon3.setVertices(vertices3);
		polygon3.setRadius(10);
		//TODO: Implement tests for getDrawingCommands
	}
}
