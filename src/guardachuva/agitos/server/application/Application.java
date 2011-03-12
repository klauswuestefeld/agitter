package guardachuva.agitos.server.application;

import guardachuva.agitos.server.application.homes.EventHome;
import guardachuva.agitos.server.application.homes.UserHome;
import guardachuva.mailer.core.ScheduledMails;

import java.io.Serializable;


public class Application implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final UserHome _userHome = new UserHome();
	private final EventHome _eventHome = new EventHome();
	private final ScheduledMails _scheduledMails = new ScheduledMails();
	
	public UserHome getUserHome() {
		return _userHome;
	}
	
	public EventHome getEventHome() {
		return _eventHome;
	}
	
	public ScheduledMails getScheduledMails() {
		return _scheduledMails;
	}

}

