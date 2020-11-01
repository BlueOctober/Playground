package kr.co.azurepassion.common.cipher.encode;

import kr.co.azurepassion.common.cipher.exception.CryptoEncodingException;

import java.io.UnsupportedEncodingException;

public class ByteCryptoEncoder implements CryptoEncoder {

	private String charset;
	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getString(byte[] bytes) {
		try {
			if (charset == null) {
				return new String(bytes);
			} else {
				return new String(bytes, charset);
			}
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}

	public byte[] getBytes(String str) {
		try {
			if (charset == null) {
				return str.getBytes();
			} else {
				return str.getBytes(charset);
			}
		} catch (UnsupportedEncodingException uee) {
			throw new CryptoEncodingException(uee);
		}
	}
}