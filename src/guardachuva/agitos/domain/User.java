package guardachuva.agitos.domain;

import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.ValidationException;
import guardachuva.agitos.shared.Validations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String _name;
	private String _userName;
	private String _password;
	private String _email;
	
	private ArrayList<Event> myEvents = new ArrayList<Event>();

	private HashSet<User> _contacts = new HashSet<User>();
	private HashSet<User> ignoredProducers = new HashSet<User>();
	private HashSet<User> producers = new HashSet<User>();
	
	public User() {
		super();
	}
	
	private User(String name, String userName, String password, String email) {
		this._name = name;
		this._userName = userName;
		this._password = password;
		this._email = email;
	}

	public String getName() {
		return _name;
	}

	public String getUserName() {
		return _userName;
	}

	public String getEmail() {
		return _email;
	}
	
	public static User createFor(String name, String userName, String password, String email) throws ValidationException {
		String[] errors = User.errorsForConstruction(name, userName, password, email);
		if (errors.length > 0)
			throw new ValidationException(User.class, errors);
		
		User user = new User(name, userName, password, email);
		
		return user;
	}
	
	public ArrayList<Event> myEvents() {
		// TODO encapsular coleção
		return myEvents;
	}

	public void addContacts(List<User> contacts) throws BusinessException {
		for (User contact : contacts)
			addContact(contact);
	}
	
	public void addContact(User newContact) throws BusinessException {
		if (this.equals(newContact))
			throw new BusinessException("Você não pode ser um dos seus contatos.");
		
		newContact.producers.add(this);
		_contacts.add(newContact);
	}

	public void removeContact(User contact) {
		contact.producers.remove(this);
		_contacts.remove(contact);
	}

	public Set<User> getContacts() {
		return _contacts;
	}

	public Set<User> getProducers() {
		@SuppressWarnings("unchecked")
		HashSet<User> notIgnoredProducers = (HashSet<User>) producers.clone();
		notIgnoredProducers.removeAll(ignoredProducers);
		return notIgnoredProducers;
	}
	
	public ArrayList<Event> listEventsSince(Date since) {
		ArrayList<Event> filteredEvents = new ArrayList<Event>();
		
		for (Event event : listEvents())
			if (event.getDate().after(since))
				filteredEvents.add(event);
		
		return filteredEvents;
	}
	
	public ArrayList<Event> listEvents() {
		ArrayList<Event> events = new ArrayList<Event>(myEvents());
		for (User user : getProducers())
			events.addAll(user.myEvents());
		return events;
	}
	
	public void ignoreProducer(User producer) throws BusinessException {
		if (this.equals(producer))
			throw new BusinessException("Você não pode ignorar a si mesmo.");
		
		ignoredProducers.add(producer);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_email == null) ? 0 : _email.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
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
		User other = (User) obj;
		if (_email == null) {
			if (other._email != null)
				return false;
		} else if (!_email.equals(other._email))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}
	
	public static String[] errorsForConstruction(String name, String userName, String password, String email) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (name != null && !Validations.validateMinLength(name, 3))
			errors.add("O nome deve possuir no mínimo 3 caracteres.");
		
		if (userName != null && !Validations.validateMinLength(userName, 3))
			errors.add("O nome de usuário deve possuir no mínimo 3 caracteres.");

		if (password != null && !Validations.validateMinLength(password, 3))
			errors.add("A senha deve possuir no mínimo 3 caracteres.");

		if (email != null && !Validations.validateEmail(email))
			errors.add("O email não parece ser válido.");
		
		return errors.toArray(new String[errors.size()]);
	}

	public boolean isValidPassword(String passwordToCheck) {
		return _password.equals(passwordToCheck);
	}
	
}
