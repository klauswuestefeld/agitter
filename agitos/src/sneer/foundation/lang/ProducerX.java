package sneer.foundation.lang;


public interface ProducerX<T, X extends Throwable> {

	T produce() throws X;
	
}
