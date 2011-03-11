package guardachuva.agitos.domain;

import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.Validations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public final class Event implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final int id;
	private final User moderator;
	private final String description;
	private final long date;
	
	private Event(int id, User moderator, String description, Date date) {
		this.id = id;
		this.moderator = moderator;
		this.description = description;
		this.date = date.getTime();
	}
	
	public int getId() {
		return id;
	}

	public User getModerator() {
		return moderator;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return new Date(date);
	}

	public static Event createFor(int id, User moderator, String description, Date date) throws BusinessException {
		if (Event.errorsForConstruction(moderator, description, date).length > 0)
			throw new BusinessException("Erros encontrados. Valide antes da criação.");
		
		Event event = new Event(id, moderator, description, date);
		
		return event;
	}
	
	public static String[] errorsForConstruction(User moderator, String description, Date date) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (moderator == null)
			errors.add("Não foi selecionado um moderador.");
		
		if (!Validations.validateMinLength(description, 3))
			errors.add("A descrição deve possuir no mínimo 3 caracteres.");
		
		if (date == null)
			errors.add("A data ou a hora é inválida.");
		
		return (String[]) errors.toArray(new String[errors.size()]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (date ^ (date >>> 32));
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((moderator == null) ? 0 : moderator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (date != other.date)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (moderator == null) {
			if (other.moderator != null)
				return false;
		} else if (!moderator.equals(other.moderator))
			return false;
		return true;
	}
	
}
