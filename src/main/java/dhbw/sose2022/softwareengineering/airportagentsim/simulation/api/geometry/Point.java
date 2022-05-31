package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry;

import org.apache.commons.lang3.Validate;

public final class Point {
	
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Integer x, Integer y) {
		Validate.notNull(x);
		Validate.notNull(y);
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	/**
	 * Calculates the two-dimensional distance from two points and returns the value as a double.<p>
	 * 
	 * @param target The point to calculate the distance to.
	 * @return the distance as a double.
	 */
	public double getDistance(Point target) {
		if(target == null)
			throw new NullPointerException("target must not be null");
		double x2 = Math.pow(target.getX() - this.getX(), 2);
		double y2 = Math.pow(target.getY() - this.getY(), 2);
		return Math.sqrt(x2 + y2);
	}
	
	/**
	 * This method returns whether a point is within a radius r of that point.<p>
	 * 
	 * @param p The point to be examined.
	 * @param r the radius in which the point must lie.
	 * @return whether the point is within the radius or not.
	 */
	public boolean isInRadius(Point p, int r) {
		return (getDistance(p) < r);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point))
			return false;
		Point p = (Point) obj;
		return p.x == this.x && p.y == this.y;
	}
	
	@Override
	public int hashCode() {
		return this.x * this.y;
	}
	
}
