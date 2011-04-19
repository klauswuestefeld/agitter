package agitter;

import java.util.Date;
import java.util.SortedSet;

public interface AgitoHome {

	public Agito create(String description, Date date);
	public SortedSet<Agito> all();
	public SortedSet<Agito> toHappen();

}
