package sneer.foundation.lang;

public interface Consumer<T> extends PickyConsumer<T> {

	@Override
	void consume(T value);
	
}
