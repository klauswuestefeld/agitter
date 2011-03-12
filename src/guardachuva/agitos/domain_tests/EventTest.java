package guardachuva.agitos.domain_tests;

import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.domain.comparators.EventDateTimeComparator;
import guardachuva.agitos.server.application.DateTimeUtils;
import guardachuva.agitos.server.application.homes.EventHome;
import guardachuva.agitos.server.application.homes.UserHome;
import guardachuva.agitos.shared.BusinessException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class EventTest extends Assert {
	
	private UserHome _userHome = new UserHome();
	private EventHome _eventHome = new EventHome();
	private User _altieres;
	private User _maoki;
	
	public EventTest() throws BusinessException {
		_altieres = _userHome.produceUser("altiereslopes@gmail.com");
		_maoki = _userHome.produceUser("maoki@gmail.com");
	}
	
	@Test
	public void Equals() throws BusinessException, ParseException {
		Event event = Event.createFor(1, _altieres, "descrição", DateTimeUtils.strToDate("12/05/2010 10:32"));
		Event anotherEvent = Event.createFor(1, _altieres, "descrição", DateTimeUtils.strToDate("12/05/2010 10:32"));
		
		assertEquals(event, anotherEvent);
	}
	
	@Test
	public void EventCreateFor() throws BusinessException, ParseException {
		Event event = _eventHome.createFor(_altieres, "meu nascimento!", DateTimeUtils.strToDate("25/12/1981 10:30"));
		
		assertEquals(1, _eventHome.count());
		
		assertSame(_altieres, event.getModerator());
		assertEquals("meu nascimento!", event.getDescription());
		assertEquals(DateTimeUtils.strToDate("25/12/1981 10:30"), event.getDate());
	}
	
	@Test
	public void EventErrorsForConstruction() throws ParseException {
		String[] errorsNotFound = Event.errorsForConstruction(_altieres, "meu nascimento!", DateTimeUtils.strToDate("25/12/1981 10:30"));
		String[] errorsFound = Event.errorsForConstruction(null, "me", null);
		
		assertEquals(3, errorsFound.length);
		assertEquals(0, errorsNotFound.length);
	}
	
	@Test
	public void removeEvent() throws BusinessException, ParseException {
		_eventHome.createFor(_altieres, "meu nascimento!", DateTimeUtils.strToDate("25/12/1981 10:30"));
		
		try {
			_eventHome.removeFor(_maoki, 1);
			fail();
		} catch (BusinessException e) { }
		
		_eventHome.removeFor(_altieres, 1);
		
		assertEquals(0, _eventHome.count());
	}
	
	@Test
	public void sortEvents() throws BusinessException, ParseException {
		Comparator<Event> byDateTime = new EventDateTimeComparator();
		
		_maoki.addContact(_altieres);
		
		Event evento3 = _eventHome.createFor(_maoki, "nascimento!", DateTimeUtils.strToDate("16/04/1984 09:20"));
		Event evento4 = _eventHome.createFor(_altieres, "festa em 2100!", DateTimeUtils.strToDate("25/12/2050 11:00"));
		Event evento2 = _eventHome.createFor(_altieres, "meu nascimento!", DateTimeUtils.strToDate("25/12/1981 10:30"));
		Event evento1 = _eventHome.createFor(_altieres, "meia hora antes do meu nascimento!", DateTimeUtils.strToDate("25/12/1981 10:00"));
		
		Event[] sortedEvents = (Event[]) _altieres.listEvents().toArray(new Event[_altieres.listEvents().size()]);
		
		Arrays.sort(sortedEvents, byDateTime);
		
		assertEquals(evento1, sortedEvents[0]);
		assertEquals(evento2, sortedEvents[1]);
		assertEquals(evento3, sortedEvents[2]);
		assertEquals(evento4, sortedEvents[3]);
	}
	
	@Test
	public void listEventsIgnoreOlders() throws Exception {
		Date sinceDate = DateTimeUtils.strToDate("10/10/2010 10:00");
		
		Event evento1 = _eventHome.createFor(_altieres, "meia hora depois", DateTimeUtils.strToDate("10/10/2010 10:30"));
		_eventHome.createFor(_altieres, "meia hora antes", DateTimeUtils.strToDate("10/10/2010 09:30"));
		_eventHome.createFor(_altieres, "tres horas antes", DateTimeUtils.strToDate("10/10/2010 07:00"));
		
		List<Event> eventsList = _altieres.listEventsSince(sinceDate);
			
		assertEquals(1, eventsList.size());
		assertEquals(evento1, eventsList.get(0));
	}
	
	@Test
	public void homeIdGeneration() throws BusinessException, ParseException {
		Event event = _eventHome.createFor(_altieres, "festa em 2100!", DateTimeUtils.strToDate("25/12/2050 11:00"));
		assertEquals(1, event.getId());
		
		event = _eventHome.createFor(_altieres, "festa em 2100!", DateTimeUtils.strToDate("25/12/2050 11:00"));
		assertEquals(2, event.getId());
		
		_eventHome.removeFor(_altieres, 1);
		
		event = _eventHome.createFor(_altieres, "festa em 2100!", DateTimeUtils.strToDate("25/12/2050 11:00"));
		assertEquals(3, event.getId());
	}
	
}
