
package org.tok.cust.admin.userpass.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tok.view.util.DateUtil;

/**  
 * @Class Name : UserpassPK.java
 * @Description : 개인정보 Primary Key Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassPK
{
	// primary key
	
	private String userId = null;

    public UserpassPK()
	{
	
    }
	
    public UserpassPK(String userId)
	{
		this();
		this.userId = userId;
    }
	
	public String getUserId(){ return userId; }
    public void setUserId(String userId){ if(userId != null && userId.length() > 0) this.userId=userId; }			

	// master key
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Userpass] ");
		
		buffer.append(" UserId=\"").append( this.userId ).append("\"");

		return buffer.toString();      
	}
}
