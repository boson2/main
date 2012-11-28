package org.tok.cust.security.passcodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MD5 인코딩 구현체.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public final class Md5PasswordEncoder implements PasswordEncoder {

    /** The name of the algorithm to use. */
    private static final String ALGORITHM_NAME = "MD5";

    protected Log log = LogFactory.getLog(this.getClass());

    public String encode(final String password) {

        if (password == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest
                .getInstance(ALGORITHM_NAME);
            messageDigest.update(password.getBytes());

            final byte[] digest = messageDigest.digest();
            StringBuffer hexString = new StringBuffer();

            synchronized (hexString) {
                for (int i = 0; i < digest.length; i++) {
                    final String plainText = Integer
                        .toHexString(0xFF & digest[i]);

                    if (plainText.length() < 2) {
                        hexString.append("0");
                    }

                    hexString.append(plainText);
                }

            }
            //log.info("Password encoded=["+hexString.toString()+"]");
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    public String encode(final String usename, final String password) {
    	return encode(password);
    }
}
