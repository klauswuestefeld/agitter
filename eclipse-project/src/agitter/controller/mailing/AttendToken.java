package agitter.controller.mailing;

import java.util.Map;

import agitter.controller.AuthenticationToken;
import agitter.domain.emails.EmailAddress;
import agitter.domain.events.Event;
import basis.lang.exceptions.Refusal;

public class AttendToken extends AuthenticationToken {

	public AttendToken(Map<String, String[]> params) throws Refusal {
		super(params);
	}
	
	public AttendToken(EmailAddress email, long eventId, Event.Attendance choice) {
		super(email);
		addParamToUri("event", ""+eventId);
		addParamToUri("choice", choice.name());
	}

	@Override
	protected String command() {
		return "attend";
	}

}
