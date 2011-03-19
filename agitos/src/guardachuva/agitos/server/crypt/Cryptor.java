package guardachuva.agitos.server.crypt;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class Cryptor {
	private final String _passPhrase = "altieres+mahmoud&klaus";
	private final int _iterationCount = 17;
	private final byte[] _salt = { (byte) 0x35, (byte) 0x28, (byte) 0x23,
			(byte) 0x84, (byte) 0x11, (byte) 0x32, (byte) 0x44, (byte) 0x43 };
	
	private Cipher _ecipher = null;
	private Cipher _dcipher = null;

	public Cryptor() throws CryptorException {
		try {
			KeySpec keySpec = new PBEKeySpec(_passPhrase.toCharArray(), _salt,
					_iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);
			_ecipher = Cipher.getInstance(key.getAlgorithm());
			_dcipher = Cipher.getInstance(key.getAlgorithm());

			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(_salt,
					_iterationCount);
			
			_ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			_dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (Exception ex) {
			throw new CryptorException(ex);
		}
	}

	public String encode(String text) throws CryptorException {
		try {
			byte[] original = text.getBytes("UTF8");
			return new Base64Encoder().encode(_ecipher.doFinal(original));
		} catch (Exception ex) {
			throw new CryptorException(ex);
		}
	}

	public String decode(String encodedText) throws CryptorException {
		try {
			byte[] decriptado = _dcipher.doFinal(new Base64Encoder().decode(encodedText));
			return new String(decriptado, "UTF-8");
		} catch (Exception ex) {
			throw new CryptorException(ex);
		}
	}

}
