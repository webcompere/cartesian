package uk.org.webcompere.cartesian;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import org.junit.Test;

/**
 * Tests for the gradient class
 */
public class GradientTest {
	@Test
	public void cannotConstructGradientWhenXsAreSame() {
		assertNull(Gradient.createFrom(new DataPoint(0,0), new DataPoint(0,1)));
	}
	
	@Test
	public void correctGradientFromTwoPoints() {
		Gradient gradient = Gradient.createFrom(new DataPoint(0,2), new DataPoint(1,3));
		assertThat(gradient.getM(), is(1.0));
		assertThat(gradient.getC(), is(2.0));
	}
	
	@Test
	public void averageWith() {
		Gradient g1 = new Gradient(10,5);
		Gradient g2 = new Gradient(7.5, 2);
		
		DataPoint dataPoint = new DataPoint(10,7);
		Gradient average = g1.averageWith(g2, dataPoint);
		
		// ensure that the average passes through the right y
		assertThat(average.getYFromX(10.0), is(7.0));
	}
}
