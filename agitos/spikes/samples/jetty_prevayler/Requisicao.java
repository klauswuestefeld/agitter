package samples.jetty_prevayler;

import java.util.Date;

import org.prevayler.SureTransactionWithQuery;

final class Requisicao implements SureTransactionWithQuery {
	
	private static final long serialVersionUID = 1L;
	private final String _target;

	public Requisicao(String target) {
		this._target = target;
	}

	@Override
	public Object executeAndQuery(Object aplicacao, Date arg1) {
		return ((Aplicacao) aplicacao).processa(_target);
	}
}