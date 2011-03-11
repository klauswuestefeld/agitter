package guardachuva.agitos.server.resources;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.comparators.EventDateTimeComparator;
import guardachuva.agitos.server.application.Application;
import guardachuva.agitos.shared.BusinessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import sneer.bricks.hardware.clock.Clock;

public class EventsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		long twoHours = 2*60*60*1000;
		Date nowMinusTwoHours = new Date(my(Clock.class).time().currentValue() - twoHours);
		System.out.println(nowMinusTwoHours.toString());
		ArrayList<Event> eventsList = _user.listEventsSince(nowMinusTwoHours);
		Event[] eventsArray = (Event[])  eventsList.toArray(new Event[eventsList.size()]);
		
		Arrays.sort(eventsArray, new EventDateTimeComparator());
		return eventsArray;
	}

	@Override
	protected Object doPost() throws Exception {
		Date date = Application.strToDate(_request.getParameter("date"));
		// TODO: my(Clock) deve ter a data correta da chamada original.
		// Não tem tanto problema usar a data atual porque eventos antigos não são exibidos.
		Date now = new Date(my(Clock.class).time().currentValue());
		System.out.println(now.toString() + " / " + date.toString());
		boolean dateInPast = date.before(now);
		
		if (dateInPast)
			throw new BusinessException("Você não pode criar agitos no passado.");
		
		_application._eventHome.createFor(
			_user,
			_request.getParameter("description"),
			date
		);
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		int id = Integer.parseInt(_request.getParameter("id"));
		_application._eventHome.removeFor(_user, id);
	}

}
