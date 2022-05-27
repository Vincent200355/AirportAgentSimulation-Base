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
