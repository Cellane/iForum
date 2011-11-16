/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.milanvit.iforum.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milan
 */
public class Hash {
	public static String toSHA256 (String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance ("SHA-256");
			StringBuilder stringBuilder = new StringBuilder ();
			
			messageDigest.update (password.getBytes ());
			byte byteData[] = messageDigest.digest ();

			for (int i = 0; i < byteData.length; i++) {
				stringBuilder.append (Integer.toString ((byteData[i] & 0xff) + 0x100, 16).substring (1));
			}

			return (stringBuilder.toString ());
		} catch (NoSuchAlgorithmException nsae) {
			Logger.getLogger (Hash.class.getName()).log (Level.SEVERE, null, nsae);
		}
		
		return (null);
	}
}
