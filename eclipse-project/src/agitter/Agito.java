package agitter;


import java.io.Serializable;
import java.util.Date;

public class Agito implements Serializable {

	private static final long serialVersionUID = 1L;

	final private Date _date;
	final private String _description;

	public Agito(String description, Date date) {
		if(null==description) { throw new IllegalArgumentException("description can not be null"); }
		if(null==date) { throw new IllegalArgumentException("date can not be null"); }
		_description = description;
		_date = date;
	}

	public String description() {
		return _description;
	}

	public Date date() {
		return _date;
	}

	@Override
	public boolean equals(Object o) {
		if(this==o) { return true; }
		if(o==null || getClass()!=o.getClass()) { return false; }

		Agito agito = (Agito) o;

		return _date.equals(agito._date) && _description.equals(agito._description);
	}

	@Override
	public int hashCode() {
		int result = _date.hashCode();
		result = 31*result+_description.hashCode();
		return result;
	}

}
