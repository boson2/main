package org.tok.cust.tool.dao;

import org.tok.view.exception.EnfaceException;



/**
 * BaseException
 **/

public class ToolException extends EnfaceException 
{
	private static final long serialVersionUID = 1L;
    public ToolException() 
    {
        super();
    }
    
    public ToolException(String messageKey) 
    {
        super(messageKey);
    }
    
    public ToolException(Throwable nested)
    {
        super(nested);
    }
    
}
