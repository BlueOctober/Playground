package kr.co.azurepassion.common.cipher.symmetric.seed.old;

public interface CryptoPadding {

	
	byte[] addPadding(byte[] source, int blockSize);

	
	byte[] removePadding(byte[] source, int blockSize);

}

