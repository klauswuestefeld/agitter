package guardachuva.agitos.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EventDTO implements IsSerializable {

	private static final long serialVersionUID = 1L;
	private int _id;
	private String _description;
	private Date _date;
	private UserDTO _moderator;
	
	@SuppressWarnings("unused")
	private EventDTO() {
	}
	
	public EventDTO(int id, String description, Date date, UserDTO moderator) {
		this._id = id;
		this._description = description;
		this._date = date;
		this._moderator = moderator;
	}

	public String getDescription() {
		return _description;
	}

	public Date getDate() {
		return _date;
	}

	public int getId() {
		return _id;
	}

	public UserDTO getModerator() {
		return _moderator;
	}
	
	
}
