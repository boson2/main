package org.tok.cust.security.passcodec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SHA 해시 인코더 구현체.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public final class ShaPasswordEncoder implements PasswordEncoder {
	
    MessageDigest digester;
    boolean simpleEncryption = false;
    protected Log log = LogFactory.getLog(this.getClass());

    public ShaPasswordEncoder() throws NoSuchAlgorithmException {
        this.digester = MessageDigest.getInstance("SHA-1");
    }
    
    public String encode(final String password) {
        return encode(null, password);
    }    

    public String encode(final String username, final String password) {
        if (username == null) {
            return null;
        }
        if (password == null) {
            return null;
        }

        //log.info("algorithm=[SHA-1]::password=["+password+"]");
        byte[] value;
        synchronized(digester) {
            digester.reset();
            value = digester.digest(password.getBytes());
            if (!simpleEncryption)
            {
                // don't allow copying of encoded passwords
                digester.update(username.getBytes());
            }
            value = digester.digest(value);
        }
        return new String(Base64.encodeBase64(value));
    }    
}
