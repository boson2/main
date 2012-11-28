
package org.tok.cust.admin.grade.service;

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
 * @Class Name : GradePK.java
 * @Description : 등급관리 Primary Key Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradePK
{
	// primary key
	
	private String code = null;

    public GradePK()
	{
	
    }
	
    public GradePK(String code)
	{
		this();
		this.code = code;
    }
	
	public String getCode(){ return code; }
    public void setCode(String code){ if(code != null && code.length() > 0) this.code=code; }			

	// master key
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Grade] ");
		
		buffer.append(" Code=\"").append( this.code ).append("\"");

		return buffer.toString();      
	}
}
