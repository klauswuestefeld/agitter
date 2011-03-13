package guardachuva.agitos.shared;

import java.io.Serializable;
import java.util.Date;

public class EventDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String description;
	private Date date;
	private UserDTO moderator;
	
	@SuppressWarnings("unused")
	private EventDTO() {
	}
	
	public EventDTO(int id, String description, Date date, UserDTO moderator) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.moderator = moderator;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return date;
	}

	public int getId() {
		return id;
	}

	public UserDTO getModerator() {
		return moderator;
	}
	
	
}
