package kr.co.azurepassion.common.cipher.symmetric;

import kr.co.azurepassion.common.cipher.encode.Base64CryptoEncoder;
import kr.co.azurepassion.common.cipher.encode.ByteCryptoEncoder;
import kr.co.azurepassion.common.cipher.Crypto;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

/**
 * AES/CBC/(PKCS7/PKCS5)Padding 128 bit 암호화 콤포넌트 for 클립서비스
 */
public class ClipServiceAes256Crypto extends AbstractSymmetricWithIvCrypto {
	
	Base64CryptoEncoder encoder = new Base64CryptoEncoder();
	ByteCryptoEncoder keyEncoder = new ByteCryptoEncoder();
	
	public ClipServiceAes256Crypto() {
		commonConstructor(null, Crypto.DEFAULT_CHARSET);
	}
	
	public ClipServiceAes256Crypto(String secretKey) {
		commonConstructor(secretKey, Crypto.DEFAULT_CHARSET);
	}
	
	public ClipServiceAes256Crypto(String secretKey, String charset) {
		commonConstructor(secretKey, charset);
	}
	
	private void commonConstructor(String secretKey, String charset) {
		initializeAlgorithm();
		this.setEncoder(encoder);
		this.setKeyEncoder(keyEncoder);
		if (secretKey != null) {
			this.setSecretKey(secretKey);
		}
		if (charset != null) {
			this.setCharset(charset);
		}
	}
	
	// AES/CBC/(PKCS7/PKCS5)
	private void initializeAlgorithm() {
		super.setCipher(new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()), new PKCS7Padding()));
	}

	@Override
	public void setSecretKey(String secretKey) {
		if (secretKey == null) {
			throw new IllegalArgumentException("'secretKey' must not be null");
		}
		if (keyEncoder.getBytes(secretKey).length != 32) {
			throw new IllegalArgumentException("'secretKey' length must be 16 byte (128 bit) with byte encoded : " + keyEncoder.getBytes(secretKey).length);
		}
		super.setSecretKey(secretKey);
		super.setIv(secretKey.substring(0, 16));
		super.initializeKey();
	}

	@Override
	public void setCharset(String charset) {
		keyEncoder.setCharset(charset);
		encoder.setCharset(charset);
		super.setCharset(charset);
	}
}
