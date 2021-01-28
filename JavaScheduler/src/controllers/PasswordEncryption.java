package controllers;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * The Class PasswordEncryption is used to encrypt and decrypt a Password
 * @author Wizard107
 */
public class PasswordEncryption {
	
	/** The Constant iterations determines how often hashing is executed. */
	private static final int iterations = 10000;
	
	/** The Constant algorithm determines which hashing method is used. */
	private static final String algorithm = "PBKDF2WithHmacSHA512";
	
	/** The integer keylen represents the number of bits for the hashcode. */
	private static final int hash_len = 128;
	
	/**
	 * Creates the password how it is stored in database.
	 * 
	 * @param password the password that is encrypted 
	 * @return Encoded Password in Format Salt$Hash
	 */
	public static String createHash(String password) {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];		//salt length
		random.nextBytes(salt);			//salt is filled with secure random bytes
		byte[] hash = pbkdf2(password.toCharArray(), salt, iterations);		//hash is created with password, salt and number of iterations
		return Base64.getEncoder().withoutPadding().encodeToString(salt) + "$" + Base64.getEncoder().withoutPadding().encodeToString(hash);		//Returns encrypted password without ==(padding) 
	}

	/**
	 * Verifies whether entered password matches password from database
	 * 
	 * @param password the password entered 
	 * @param stored_pw encrypted password from database 
	 * @return return the hash that is created with entered password
	 */
	public static String verify(String password, String stored_pw) {
		String[] split = stored_pw.split("\\$");		//stored_pw is split into salt and hash
		if(split.length!=2) throw new IllegalArgumentException("Form has to be Salt$Hash");		//has to have format salt$hash
		byte[] check_hash = pbkdf2(password.toCharArray(), Base64.getDecoder().decode(split[0]), iterations);	//create hash with entered password and stored salt
		String test=Base64.getEncoder().withoutPadding().encodeToString(check_hash);		//translate check_hash from byte to string
		//return(test.equals(split[1]));		//return true or false
		return split[0] + "$" + test;
	}
	
	/**
	 * Creates hash with given password and given salt, hashes "iterations" number of times
	 *
	 * @param password the entered password
	 * @param salt entered salt
	 * @param iterations number of hash iterations
	 * @return encoded hash which results from concatination of password and salt
	 */
	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations){
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
			KeySpec spec = new PBEKeySpec(password, salt, iterations, hash_len);
			SecretKey key = skf.generateSecret(spec);
			return key.getEncoded();
		} 
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Algorithm Invalid" + e);
		}
		catch (InvalidKeySpecException e) {
			throw new IllegalStateException("SecretKeyFactory invalid" + e);
		}
	}

}
