package kr.co.azurepassion.common.cipher.encode;

import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class SunBase64CryptoEncoder implements CryptoEncoder {

	public String getString(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	public byte[] getBytes(String str) {
		try {
			return new BASE64Decoder().decodeBuffer(str);
		} catch(Exception e) {
			throw new CryptoEncodingException(e);
		}
	}
}