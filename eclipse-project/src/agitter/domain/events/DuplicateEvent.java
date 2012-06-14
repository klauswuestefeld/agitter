package agitter.domain.events;

import basis.lang.exceptions.Refusal;


public class DuplicateEvent extends Refusal {

	public DuplicateEvent() {
		super("Já existe um agito seu na mesma data com a mesma descrição.");
	}

}
