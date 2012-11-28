
package org.tok.cust.admin.todayWord.service;

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
 * @Class Name : TodayWordPK.java
 * @Description : 오늘의 말씀 Primary Key Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordPK
{
	// primary key
	
	private Integer wordId = 0;

    public TodayWordPK()
	{
	
    }
	
    public TodayWordPK(Integer wordId)
	{
		this();
		this.wordId = wordId;
    }
	
	public Integer getWordId(){ return wordId; }
    public void setWordId(Integer wordId){ this.wordId=wordId; }			

	// master key
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TodayWord] ");
		
		buffer.append(" WordId=\"").append( this.wordId ).append("\"");

		return buffer.toString();      
	}
}
