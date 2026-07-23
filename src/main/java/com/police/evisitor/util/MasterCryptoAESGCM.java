package com.police.evisitor.util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MasterCryptoAESGCM {
	private static final String ALGO = "AES/GCM/NoPadding";

	public String encrypt(String plainText, String secretKey, String ivBase64) {
		try {
		byte[] iv = Base64.getDecoder().decode(ivBase64);
		Cipher cipher = Cipher.getInstance(ALGO);
		GCMParameterSpec spec = new GCMParameterSpec(128, iv);
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		byte[] cipherText = cipher.doFinal(plainText.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
		} catch (Exception ex) {
			log.error("Error in MasterCryptoAESGCM encrypt ex {} ", ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}

	public String decrypt(String cipherTextEncoded, String secretKey, String ivBase64) {
		try {
		byte[] iv = Base64.getDecoder().decode(ivBase64);
		byte[] cipherText = Base64.getDecoder().decode(cipherTextEncoded);
		Cipher cipher = Cipher.getInstance(ALGO);
		GCMParameterSpec spec = new GCMParameterSpec(128, iv);
		SecretKey key = new SecretKeySpec(secretKey.getBytes(), "AES");
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] plainText = cipher.doFinal(cipherText);
		return new String(plainText);
		} catch (Exception ex) {
			log.error("Error in MasterCryptoAESGCM decrypt  ex {}", ex.getMessage());
		}
		return null;
	}
	
	public  String generateBase64IV() {
		byte[] iv = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(iv);
		return Base64.getEncoder().encodeToString(iv);
	}

	public static void main(String[] args) throws Exception {
		MasterCryptoAESGCM masterCryptoAESGCM = new MasterCryptoAESGCM();
		String key = "I7mX4pQ2nV7rT8aL5cFwY1hJ6uNz3NgY";
		String encodedIV = masterCryptoAESGCM.generateBase64IV();
		String plainText = "{\r\n"
				+ "    \"ssoId\":\"jituDhukiya\",\r\n"
				+ "    \"pin\":\"898324\"\r\n"
				+ "}";
		String encryptedText = masterCryptoAESGCM.encrypt(plainText,key, encodedIV);
		System.out.println("encodedIV : "+encodedIV+", encryptedText : " + encryptedText);
		String decryptedText = masterCryptoAESGCM.decrypt(encryptedText,key, encodedIV);
		System.out.println("Decrypted Text: " + decryptedText);
	}
}
