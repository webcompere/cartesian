package uk.org.webcompere.cartesian;

import java.util.List;

/**
 * Extrapolate from a set of datapoints to get the likely convergence
 */
public class Extrapolator {
	
	/**
	 * Provide the convergence point for two lines expressed as data points
	 * The lists must contain more than 1 point each or you are asking for convergence
	 * of a single point with another which is impossible
	 * If the lines are parallel, or already converge, then this cannot be done
	 * It is assumed that convergence is expected at some X > the largest X in the lists
	 * @param list1 - first list
	 * @param list2 - second list
	 * @return the data point where the two lines meet
	 */
	public static DataPoint getConvergenceOf(List<DataPoint> list1, List<DataPoint> list2) {
		if (list1.size()<2 || list2.size()<2) {
			return null;
		}
		
		Gradient gradient1 = getGradient(list1);
		Gradient gradient2 = getGradient(list2);
		
		if (gradient1 == null || gradient2 == null) {
			return null;
		}
		
		// special case - same gradient means use the last data point
		DataPoint lastPointList1 = list1.get(list1.size()-1);
		if (gradient1.equals(gradient2)) {
			return lastPointList1;
		}
			
		DataPoint intersection =  intersectionOf(gradient1, gradient2);
		
		// if they intersect before the end of the line then they're divergent
		if (intersection==null || areDivergent(lastPointList1, intersection)) {
			return null;
		}
		
		return intersection;
		
	}

	private static boolean areDivergent(DataPoint lastPoint,
			DataPoint intersection) {
		return intersection.getX() < lastPoint.getX();
	}

	private static DataPoint intersectionOf(Gradient gradient1,
			Gradient gradient2) {
		// from http://www.mathopenref.com/coordintersection.html
		// y1 = m1*x + c1
		// y2 = m2*x + c2
		// m1*x+c1 = m2x+c2
		// m1*x - m2*x = c2 - c1
		// (m1-m2) * x = (c2 - c1)
		// x = (c2 - c1) / (m1 + m2)
		
		double gradientSum = gradient1.getM() - gradient2.getM();
		
		// can't do it if both lines are flat
		if (gradientSum == 0) {
			return null;
		}
		
		double x = (gradient2.getC() - gradient1.getC()) / gradientSum;
		
		// do a y = mx + c on one of the input gradients to the X intersection point
		double y = gradient1.getYFromX(x);
		
		
		return new DataPoint(x,y);
	}

	/**
	 * Find the gradient given a set of points
	 * @param data data points to read the gradient from
	 * @return a gradient object
	 */
	public static Gradient getGradient(List<DataPoint> data) {
		if (data.size()<2) {
			return null;
		}
		Gradient currentGradient = Gradient.createFrom(data.get(0), data.get(1));
		for (int i=1; i<data.size()-1; i++) {
			Gradient nextGradient = Gradient.createFrom(data.get(i), data.get(i+1));
			
			if (currentGradient == null || nextGradient == null) {
				return null;
			}
			
			currentGradient = currentGradient.averageWith(nextGradient, data.get(i+1));
		}
		return currentGradient;
	}

	public static DataPoint getConvergenceWithY(List<DataPoint> data, double y) {
		Gradient gradient = getGradient(data);
		if (gradient == null) {
			return null;
		}
		return gradient.dataPointFromY(y);
	}

	public static DataPoint getConvergenceWithX(List<DataPoint> data, double x) {
		Gradient gradient = getGradient(data);
		if (gradient == null) {
			return null;
		}		
		return gradient.dataPointFromX(x);
	}
}
