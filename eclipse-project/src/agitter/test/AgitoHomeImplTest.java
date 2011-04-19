package agitter.test;

import java.util.Date;
import java.util.Set;

import agitter.Agito;
import agitter.AgitoHomeImpl;
import agitter.util.Clock;
import agitter.util.ClockDummyImpl;
import org.junit.Assert;

public class AgitoHomeImplTest {

//	@org.junit.Before
//	public void setUp() throws Exception {
//	}
//	@org.junit.After
//	public void tearDown() throws Exception {
//	}

	@org.junit.Test
	public void testCreateAndAll() throws Exception {
		Clock clock = new ClockDummyImpl();
		AgitoHomeImpl home = new AgitoHomeImpl(clock);
		Date now = clock.date();
		home.create("Test", now);
		Assert.assertTrue("There is 1 Agito", home.all().size()==1);
	}
	@org.junit.Test
	public void testToHappen() throws Exception {
		ClockDummyImpl clock = new ClockDummyImpl();
		AgitoHomeImpl home = new AgitoHomeImpl(clock);

		Date d0 = clock.date();
		long milisAtD0 = d0.getTime();

		clock.setMilis(milisAtD0 + 1L);
		Date d1 = clock.date();

		clock.setMilis(milisAtD0+2L);
		Date d2 = clock.date();

		clock.setMilis(milisAtD0 + 3L);
		Date d3 = clock.date();

		Agito a1 = home.create("D1", d1);
		Agito a2 = home.create("D2", d2);
		Agito a3 = home.create("D3", d3);

		clock.setMilis(milisAtD0);

		Set all = home.toHappen();
		Assert.assertTrue(all.size()==3);
		Assert.assertTrue(all.contains(a1));
		Assert.assertTrue(all.contains(a2));
		Assert.assertTrue(all.contains(a3));

		clock.setMilis(milisAtD0 + 1L);
		Set<Agito> atD1 = home.toHappen();
		Assert.assertTrue(atD1.size()==2);
		Assert.assertFalse(atD1.contains(a1));
		Assert.assertTrue(atD1.contains(a2));
		Assert.assertTrue(atD1.contains(a3));

		clock.setMilis(milisAtD0 + 2L);
		Set<Agito> atD2 = home.toHappen();
		Assert.assertTrue(atD2.size()==1);
		Assert.assertTrue(atD2.contains(a3));


		clock.setMilis(milisAtD0 + 3L);
		Set<Agito> atD3 = home.toHappen();
		Assert.assertTrue(atD3.size()==0);

	}

}
