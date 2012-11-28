
package org.tok.cust.admin.todayWord.service;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tok.view.util.DateUtil;
import org.tok.view.util.StringUtil;



/**  
 * @Class Name : TodayWordVO.java
 * @Description : 오늘의 말씀 Value Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordVO
{
	// primary key
	
	private Integer wordId = 0;
	
	// fields
	
	private String title = null;
	private String content = null;
	private Date startDate = null;
	
    public TodayWordVO()
	{
	
    }
	
    public TodayWordVO(Integer wordId)
	{
		this();
		this.wordId = wordId;
		
    }
	
	public TodayWordPK getPrimaryKey()
	{
		TodayWordPK todayWordPK = new TodayWordPK(this.wordId);
		
		return todayWordPK;
	}
	
	public Integer getWordId(){ return wordId; }
    public void setWordId(Integer wordId){ this.wordId=wordId; }
	public String getTitle(){ return title; }
    public void setTitle(String title){ this.title=title; }
	public String getContent(){ return content; }
    public void setContent(String content){ this.content=content; }
	public Date getStartDate(){ return startDate; }
    public void setStartDate(Date startDate){ this.startDate=startDate; }
	public String getStartDateByFormat(){ return DateUtil.toString(startDate, "yyyy-MM-dd"); }
	
	// reference objects
	
	public String forJSON(String str) {
		return str.replace("\\", "\\\\")
				  .replace("\r\n", "\\n")
				  .replace("\n", "\\n");
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[TodayWord] ");
		
		buffer.append(" WordId=\"").append( this.wordId ).append("\"");
		buffer.append(" Title=\"").append( this.title ).append("\"");
		buffer.append(" Content=\"").append( this.content ).append("\"");
		buffer.append(" StartDate=\"").append( this.startDate ).append("\"");

		return buffer.toString();      
	}
}
