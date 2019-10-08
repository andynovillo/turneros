package web.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WebCipher {

	public static String MD2 = "MD2";

	public static String MD5 = "MD5";

	public static String SHA1 = "SHA-1";

	public static String SHA256 = "SHA-256";

	public static String SHA384 = "SHA-384";

	public static String SHA512 = "SHA-512";

	private static String toHexadecimal(byte[] digest) {

		String hash = "";

		for (byte aux : digest) {

			int index = aux & 0xff;

			if (Integer.toHexString(index).length() == 1)
				hash += "0";

			hash += Integer.toHexString(index);

		}

		return hash;

	}

	public static String getStringMessageDigest(String message, String algorithm) {

		byte[] digest = null;

		byte[] buffer = message.getBytes();

		try {

			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

			messageDigest.reset();

			messageDigest.update(buffer);

			digest = messageDigest.digest();

		} catch (NoSuchAlgorithmException exception) {

			System.out.println("Exception in " + WebCipher.class.toString() + " - " + exception.getMessage());

		}

		return toHexadecimal(digest);

	}

}
