package kr.co.azurepassion.common.cipher.key;

import kr.co.azurepassion.common.cipher.encode.ByteCryptoEncoder;
import kr.co.azurepassion.common.cipher.encode.CryptoEncoder;
import kr.co.azurepassion.common.cipher.exception.CryptoInitializeException;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class SimpleByteKeyGenerator implements StringKeyGenerator {
	
	private final String defaultAlgorithm = "AES";
	private final int defaultBits = 128;
	
	protected CryptoEncoder encoder = new ByteCryptoEncoder();
	
	public String generate(String algorithm, int bits) {
		try{
			if (algorithm == null) {
				algorithm = defaultAlgorithm;
			}
			if (bits == 0) {
				bits = defaultBits;
			}
			KeyGenerator generator = KeyGenerator.getInstance(algorithm);
			generator.init(bits);
	 
			Key key = generator.generateKey();
			byte[] keyBytes = key.getEncoded();
			return encoder.getString(keyBytes);
		} catch (NoSuchAlgorithmException nsae) {
			throw new CryptoInitializeException(nsae);
		}
	}
}
