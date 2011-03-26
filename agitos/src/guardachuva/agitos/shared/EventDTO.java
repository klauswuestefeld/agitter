package guardachuva.agitos.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class EventDTO extends Immutable implements Serializable, IsSerializable {

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

	public static String[] errorsForConstruction(String moderador, String description, Date date) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (moderador == null)
			errors.add("Não foi selecionado um moderador.");
		
		if (!Validations.validateMinLength(description, 3))
			errors.add("A descrição deve possuir no mínimo 3 caracteres.");
		
		if (date == null)
			errors.add("A data ou a hora é inválida.");
		
		return errors.toArray(new String[errors.size()]);
	}
	
	
}
