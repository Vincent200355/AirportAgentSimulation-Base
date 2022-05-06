package aas.unit.model.util;

import org.junit.Test;
import aas.model.util.Point;

public class PointTest {
	
	@Test
	public void testGetDirection() {
		Point point = new Point(0.0, 0.0);
		Point p0 = new Point(1.0, 0.0);
		Point p2 = new Point(1.0, 1.0);
		Point p3 = new Point(0.0, 1.0);
		Point p4 = new Point(-1.0, 1.0);
		Point p5 = new Point(-1.0, 0.0);
		Point p6 = new Point(-1.0, -1.0);
		Point p7 = new Point(0.0, -1.0);
		Point p8 = new Point(1.0, -1.0);
		assert Math.abs(point.getDirection(p0)) < 1.0;
		assert Math.abs(point.getDirection(p2) - 45.0) < 1.0;
		assert Math.abs(point.getDirection(p3) - 90.0) < 1.0;
		assert Math.abs(point.getDirection(p4) - 135.0) < 1.0;
		assert Math.abs(point.getDirection(p5) - 180.0) < 1.0;
		assert Math.abs(point.getDirection(p6) - 225.0) < 1.0;
		assert Math.abs(point.getDirection(p7) - 270.0) < 1.0;
		assert Math.abs(point.getDirection(p8) - 315.0) < 1.0;
	}
	
	@Test
	public void testGetDistance() {
		Point point = new Point(0.0, 0.0);
		Point p0 = new Point(0.0, 0.0);
		Point p2 = new Point(1.0, 0.0);
		Point pMinus1 = new Point(-1.0, 0.0);
		Point p3 = new Point(1.0, 1.0);
		assert point.getDistance(p0) < 1.0;
		assert point.getDistance(p2) - 1.0 < 1.0;
		assert point.getDistance(pMinus1) - 1.0 < 1.0;
		assert point.getDistance(p3) - 1.41 < 1.0;
	}
	
	@Test
	public void testTranslate() {
		Point point = new Point(0.0, 0.0);
		double dx = 1.0;
		double dy = 1.0;
		point.translate(dx, dy);
		assert Math.abs(point.getX() - dx) < 1.0;
		assert Math.abs(point.getY() - dy) < 1.0;
	}
	
	@Test
	public void testMoveTo() {
		Point start = new Point(0.0, 0.0);
		Point target = new Point(1.0, 1.0);
		double speedMax = Double.MAX_VALUE;
		double speed1 = 1.0;
		Point s1Point = start.moveTo(start, target, speed1);
		Point sMaxPoint = start.moveTo(start, target, speedMax);
		assert s1Point.getDistance(target) < start.getDistance(target) - speed1;
		assert sMaxPoint.equals(target);
	}
	
	@Test
	public void testValueOf() {
		final Point point1 = new Point(1.0, 2.0);
		final Point point2 = Point.valueOf(point1.toString());
		assert point2.equals(point1);
		assert Math.abs(point2.getX() - point1.getX()) < 1.0;
		assert Math.abs(point2.getY() - point1.getY()) < 1.0;
	}
	
}
