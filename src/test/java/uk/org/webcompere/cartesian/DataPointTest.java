package uk.org.webcompere.cartesian;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DataPointTest {
	@Test
	public void twoDataPointsEqual() {
		DataPoint point1 = new DataPoint(1,23);
		DataPoint point2 = new DataPoint(1,23);
		
		assertThat(point1, is(point2));
	}
	
	@Test
	public void twoDataPointsEqualWhenUsingCopyConstructor() {
		DataPoint point1 = new DataPoint(1,23);
		DataPoint point2 = new DataPoint(point1);
		
		assertThat(point1, not(sameInstance(point2)));
		assertThat(point1, is(point2));
	}
	
	@Test
	public void canMakePointsEqual() {
		DataPoint point1 = new DataPoint(1,23);
		DataPoint point2 = new DataPoint(99,88);

		point2.setX(1);
		point2.setY(23);

		assertThat(point1, is(point2));
	}
}
