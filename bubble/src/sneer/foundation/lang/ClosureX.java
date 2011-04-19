package sneer.foundation.lang;

/** A runnable that can throw a checked exception. */
public interface ClosureX<X extends Throwable> {
	
	void run() throws X;

}
