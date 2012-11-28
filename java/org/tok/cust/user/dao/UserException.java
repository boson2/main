package org.tok.cust.user.dao;

import org.tok.view.exception.EnfaceException;



/**
 * BaseException
 **/

public class UserException extends EnfaceException 
{
	private static final long serialVersionUID = 1L;
    public UserException() 
    {
        super();
    }
    
    public UserException(String messageKey) 
    {
        super(messageKey);
    }
    
    public UserException(Throwable nested)
    {
        super(nested);
    }
    
}
