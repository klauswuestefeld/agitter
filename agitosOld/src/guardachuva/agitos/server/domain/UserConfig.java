package guardachuva.agitos.server.domain;

import java.sql.Date;

public class UserConfig {
	
	private long lastSignIn;
	private long _lastMailSent;

	public void setLastSignIn(Date _lastSignIn) {
		this.lastSignIn = _lastSignIn.getTime();
	}
	
	public Date getLastSignIn() {
		return new Date(lastSignIn);
	}
	
	public void setLastMailSent(Date lastMailSent) {
		this._lastMailSent = lastMailSent.getTime();
	}
	
	public Date getLastMailSent() {
		return new Date(_lastMailSent);
	}
	
}
