package uk.org.webcompere.cartesian;

/**
 * Represents a Y=MX+c equation
 */
public class Gradient {
	private double m;
	private double c;
	
	/**
	 * Construct a gradient equation - Y=mx+c 
	 * @param m the gradient of the line - i.e the gain per x
	 * @param c the height above the origin at X =0
	 */
	public Gradient(double m, double c) {
		this.m = m;
		this.c = c;
	}
	
	public double getM() {
		return m;
	}
	public void setM(double m) {
		this.m = m;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	
	public double getYFromX(double x) {
		return (m * x) + c;
	}
	
	public double getXFromY(double y) {
		return (y - c) / m;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(c);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(m);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gradient other = (Gradient) obj;
		if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
			return false;
		if (Double.doubleToLongBits(m) != Double.doubleToLongBits(other.m))
			return false;
		return true;
	}

	/**
	 * Factory method to create gradient from two data points
	 * @param first left hand point
	 * @param second right hand point
	 * @return new gradient or null if impossible
	 */
	public static Gradient createFrom(DataPoint first, DataPoint second) {
		double yDiff = second.getY() - first.getY();
		double xDiff = second.getX() - first.getX();
		
		if (xDiff==0) {
			return null;
		}
		
		double gradient = yDiff / xDiff;
		double c = -(gradient*first.getX()) + first.getY();
		
		return new Gradient(gradient, c);
	}
	
	public String toString() {
		return "m=>" + m + " c=>" + c;
	}

	/**
	 * Take this gradient and average it with another
	 * ensuring that the resulting equation would hit the given point
	 * The provided DataPoint is there to help adjust for rounding errors
	 * when this is used to extrapolate on graphs
	 * @param next other gradient
	 * @param mustPassThrough point the result must pass through
	 * @return new gradient
	 */
	public Gradient averageWith(Gradient next, DataPoint mustPassThrough) {
		double newM = (m+next.m) / 2;
		Gradient average = new Gradient(newM, 0);
		
		double predictedY = average.getYFromX(mustPassThrough.getX());
		average.setC(mustPassThrough.getY() - predictedY);
		return average;
	}

	/**
	 * Use the gradient to find the X from a Y
	 * @param y y
	 * @return data point at which y=y and x = predicted x
	 */
	public DataPoint dataPointFromY(double y) {
		return new DataPoint(getXFromY(y), y);
	}
	
	/**
	 * Use the gradient to find the Y from an X
	 * @param x x
	 * @return data point at which x=x and y = predicted y
	 */
	public DataPoint dataPointFromX(double x) {
		return new DataPoint(x, getYFromX(x));
	}
	
}
