package com.platform.security.util;

import com.sun.crypto.provider.SunJCE;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import sun.security.provider.Sun;

public class Encrypts {
	
	/**
	 * 加密
	 * @param paramString 明文
	 * @return 密文
	 */
	public static String encryptPassword(String paramString) {
		try {
			Key localKey = o00000();
			Cipher localCipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
			localCipher.init(1, localKey);
			byte[] arrayOfByte = localCipher.doFinal(paramString
					.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(arrayOfByte), "UTF-8");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			throw new RuntimeException(localNoSuchAlgorithmException);
		} catch (NoSuchPaddingException localNoSuchPaddingException) {
			throw new RuntimeException(localNoSuchPaddingException);
		} catch (Exception localException) {
			throw new RuntimeException(localException);

		}

	}

	/**
	 * 解密
	 * @param paramString 密文
	 * @return 明文
	 */
	public static String decryptPassword(String paramString) {
		try {
			Key localKey = o00000();
			Cipher localCipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
			localCipher.init(2, localKey);
			byte[] arrayOfByte = localCipher.doFinal(Base64
					.decodeBase64(paramString.getBytes("UTF-8")));
			return new String(arrayOfByte, "UTF-8");
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			throw new RuntimeException(localNoSuchAlgorithmException);
		} catch (NoSuchPaddingException localNoSuchPaddingException) {
			throw new RuntimeException(localNoSuchPaddingException);
		} catch (Exception localException) {
			throw new RuntimeException(localException);
		}

	}

	private static Key o00000() {
		try {
			Security.addProvider(new Sun());
			SecureRandom localSecureRandom = SecureRandom.getInstance(
					"SHA1PRNG", "SUN");
			localSecureRandom.setSeed("seed".getBytes("UTF-8"));
			Security.addProvider(new SunJCE());
			KeyGenerator localKeyGenerator = KeyGenerator.getInstance("DESEDE",
					"SunJCE");
			localKeyGenerator.init(168, localSecureRandom);
			return localKeyGenerator.generateKey();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(Encrypts.encryptPassword("111"));
	}
}
