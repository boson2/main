package org.tok.cust.security.passcodec;

/**
 * 인코더 인터페이스
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public interface PasswordEncoder {

    String encode(String password);
    String encode(String username, String password);
}
