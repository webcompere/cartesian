package uk.org.webcompere.cartesian;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test for extrapolation
 */
public class ExtrapolatorTest {
	@Test
	public void cannotExtrapolateWithNoData() {
		DataPoint value = Extrapolator.getConvergenceOf(new ArrayList<DataPoint>(), new ArrayList<DataPoint>());
		assertNull(value);
	}
	
	@Test
	public void cannotExtrapolateWithOneItemOfData() {
		// one on each side
		assertNull(Extrapolator.getConvergenceOf(fromData(0,0), fromData(0,0)));
		// one on one side
		assertNull(Extrapolator.getConvergenceOf(new ArrayList<DataPoint>(), fromData(0,0)));
		assertNull(Extrapolator.getConvergenceOf(fromData(0,0), new ArrayList<DataPoint>()));
		// two on one side, but one on the other
		assertNull(Extrapolator.getConvergenceOf(fromData(0,0), fromData(0,0,1,1)));
	}
	
	@Test
	public void cannotExtrapolateWhenBothLinesAreFlat() {
		assertNull(Extrapolator.getConvergenceOf(fromData(1,1,1,1), fromData(2,2,2,2)));
	}
	
	@Test
	public void canExtrapolateWhenBothLinesAreSame() {
		assertEquals(new DataPoint(2,2), Extrapolator.getConvergenceOf(fromData(1,1,2,2), fromData(1,1,2,2)));
	}
	
	@Test
	public void cannotExtrapolateWhenBothLinesAreDivergent() {
		assertNull(Extrapolator.getConvergenceOf(fromData(1,1,2,2), fromData(1,1,3,2)));
	}
	
	@Test
	public void canExtrapolateWhenOneLineIsFlatAndOtherMeetsIt() {
		assertEquals(new DataPoint(3,3), Extrapolator.getConvergenceOf(fromData(1,1,2,2), fromData(2,3,3,3)));
	}
	
	@Test
	public void extrapolationFromDifferentOriginToTarget() {
		assertEquals(new DataPoint(2,4), Extrapolator.getConvergenceOf(fromData(0,2,1,3), fromData(0,0,1,2)));
	}
	
	@Test
	public void consistentGradient() {
		assertEquals(new Gradient(1, 0), Extrapolator.getGradient(fromData(0,0,1,1)));
		assertEquals(new Gradient(1, 0), Extrapolator.getGradient(fromData(0,0,1,1,2,2,3,3,4,4)));
	}
	
	@Test
	public void averageOfTwoGradients() {
		// first segment is at 1:1
		// second is at 2:1
		// average would be 1.5
		assertEquals(new Gradient(1.5, 0), Extrapolator.getGradient(fromData(0,0,1,1,2,3)));
	}
	
	@Test
	public void convergenceOfLineWithY() {
		assertEquals(new DataPoint(12, 20), Extrapolator.getConvergenceWithY(fromData(0,0,6,10), 20));
	}

	@Test
	public void impossibleConvergenceOfLineWithYWhenLineIsFlat() {
		assertNull(Extrapolator.getConvergenceWithY(fromData(0,0,0,0), 20));
	}
	
	@Test
	public void convergenceWithYInThePastWhenLineIsGoingWrongWay() {
		assertEquals(new DataPoint(-18, 20), Extrapolator.getConvergenceWithY(fromData(1,1,2,0), 20));
	}

	@Test
	public void canProvideAnXWhenFlat() {
		assertEquals(new DataPoint(12, 0), Extrapolator.getConvergenceWithX(fromData(0,0,1,0), 12));
	}
	
	@Test
	public void canProvideAnXWhenGoingDownhill() {
		assertEquals(new DataPoint(12, -10), Extrapolator.getConvergenceWithX(fromData(1,1,2,0), 12));
	}
	
	private List<DataPoint> fromData(double ... data) {
		List<DataPoint> result = new ArrayList<DataPoint>();
		for(int i=0; i<data.length-1; i+=2) {
			result.add(new DataPoint(data[i], data[i+1]));
		}
		return result;
	}
	
}
