package drawit;

import org.junit.jupiter.api.Test;

class DrawItTest {

	@Test
	void test() {
		// IntVector: Constructor tests
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
	}
}
