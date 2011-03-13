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
	
	private String name;
	private String userName;
	private String password;
	private String email;
	
	private ArrayList<Event> myEvents = new ArrayList<Event>();

	private HashSet<User> contacts = new HashSet<User>();
	private HashSet<User> ignoredProducers = new HashSet<User>();
	private HashSet<User> producers = new HashSet<User>();
	
	public User() {
		super();
	}
	
	private User(String name, String userName, String password, String email) {
		this.name = name;
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
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
		contacts.add(newContact);
	}

	public void removeContact(User contact) {
		contact.producers.remove(this);
		contacts.remove(contact);
	}

	public Set<User> getContacts() {
		return contacts;
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
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		
		return (String[]) errors.toArray(new String[errors.size()]);
	}

	public boolean isValidPassword(String passwordToCheck) {
		return password.equals(passwordToCheck);
	}
	
}
