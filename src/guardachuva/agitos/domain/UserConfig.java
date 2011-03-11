package guardachuva.agitos.domain;

import java.sql.Date;

public class UserConfig {
	
	private long lastSignIn;
	private long lastMailSent;

	public void setLastSignIn(Date lastSignIn) {
		this.lastSignIn = lastSignIn.getTime();
	}
	
	public Date getLastSignIn() {
		return new Date(lastSignIn);
	}
	
	public void setLastMailSent(Date lastMailSent) {
		this.lastMailSent = lastMailSent.getTime();
	}
	
	public Date getLastMailSent() {
		return new Date(lastMailSent);
	}
	
}
