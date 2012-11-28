package org.tok.cust.security.passcodec;

/**
 * Dummy 인코더.
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public final class PlainTextPasswordEncoder implements PasswordEncoder {

    public String encode(final String password) {
        return password;
    }

    public String encode(final String username, final String password) {
        return password;
    }
}
