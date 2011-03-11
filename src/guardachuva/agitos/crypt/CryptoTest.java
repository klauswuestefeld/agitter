package guardachuva.agitos.crypt;

public class CryptoTest {
	public static void main(String[] args) throws Exception {
		String frase = "This is a secret";
		System.out.println(frase);
		String encoded = new Cryptor().encode(frase);
		System.out.println(encoded);
		System.out.println(new Cryptor().decode(encoded));
	}
}
