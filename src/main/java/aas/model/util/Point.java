package aas.model.util;

public class Point {
	
	private double x;
	private double y;
	
	public Point() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Point(double x, double y) {
		this.x = 0.0;
		this.y = 0.0;
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getDirection(Point target) {
		double radian = Math.atan2(target.getY() - this.getY(), target.getX() - this.getX());
		if(radian < 0.0)
			radian += 6.283185307179586;
		return Math.toDegrees(radian);
	}
	
	public double getDistance(Point target) {
		if (target == null)
			throw new NullPointerException("target must not be null");
		double x2 = Math.pow(target.getX() - this.getX(), 2.0);
		double y2 = Math.pow(target.getY() - this.getY(), 2.0);
		return Math.sqrt(x2 + y2);
	}
	
	public void translate(double dx, double dy) {
		setX(this.x + dx);
		setY(this.y + dy);
	}
	
	public Point moveTo(Point start, Point target, double maximumSpeed) {
		double distance = start.getDistance(target);
		double speed = Math.min(distance, maximumSpeed);
		double direction = Math.toRadians(start.getDirection(target));
		double dy = speed * Math.cos(direction);
		double dx = speed * Math.sin(direction);
		Point newPosition = new Point(start.getX(), start.getY());
		newPosition.translate(dx, dy);
		return newPosition;
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object)
			return true;
		if(object == null || getClass() != object.getClass())
			return false;
		Point point = (Point) object;
		return Math.abs(point.getX() - getX()) < Double.MIN_NORMAL
				&& Math.abs(point.getX() - getX()) < Double.MIN_NORMAL;
	}
	
	@Override
	public String toString() {
		return "Point [x=" + this.x + ", y=" + this.y + "]";
	}
	
	public static Point valueOf(String pointString) {
		String stringValue = pointString.replace("Point [x=", "");
		stringValue = stringValue.replace(" y=", "");
		stringValue = stringValue.replace("]", "");
		String[] tokens = stringValue.split(",");
		if(Math.abs(tokens.length - 2) > 0)
			return null;
		Double x = Double.valueOf(tokens[0]);
		Double y = Double.valueOf(tokens[1]);
		return new Point(x, y);
	}
	
}
