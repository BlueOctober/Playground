package kr.co.azurepassion.common.cipher.encode;

import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64CryptoEncoder implements CryptoEncoder {
	
	private String defaultCharset = "UTF-8";
	
	private String charset = defaultCharset;
	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getString(byte[] bytes) {
		try {
			return new String(Base64.encodeBase64(bytes), charset);
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}

	public byte[] getBytes(String str) {
		try {
			return Base64.decodeBase64(str.getBytes(charset));
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}
}