package sneer.foundation.lang;


public interface FunctorX<A, B, X extends Throwable> {
	
	B evaluate(A value) throws X;

}
