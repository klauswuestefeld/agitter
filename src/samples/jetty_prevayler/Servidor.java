package samples.jetty_prevayler;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.foundation.DeepCopier;

public class Servidor {

	private final Prevayler prevayler;
	
	public Servidor() throws IOException, ClassNotFoundException {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configureTransactionFiltering(false);
		factory.configurePrevalentSystem(new Aplicacao());
		this.prevayler = factory.create();
	}
	
	public String processa(String target) {
		//TODO teste para verificar se está funcionando...
		String copia = (String) DeepCopier.deepCopy(target);
		
		return (String) prevayler.execute(new Requisicao(copia));
	}

}
