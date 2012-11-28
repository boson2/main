
package org.tok.cust.admin.todayWord.web;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;

import org.tok.cust.admin.todayWord.service.TodayWordVO;
import org.tok.cust.admin.todayWord.service.TodayWordPK;

/**  
 * @Class Name : TodayWordForm.java
 * @Description : 오늘의 말씀 Form
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordForm
{
	// primary key
	
	private Integer wordId = 0;
	
	// fields
	
	private String title = null;
	private String content = null;
	private Date startDate = new Date(System.currentTimeMillis());
	
    public TodayWordForm()
	{
	
    }
	
	
	public Integer getWordId(){ return wordId; }
    public void setWordId(Integer wordId){ this.wordId=wordId; }			
	public String getTitle(){ return title; }
    public void setTitle(String title){ if(title != null && title.length() > 0) this.title=title; }			
	public String getContent(){ return content; }
    public void setContent(String content){ if(content != null && content.length() > 0) this.content=content; }			
	public String getStartDate(){ return (startDate != null) ? startDate.toString() : ""; }
    public void setStartDate(String startDate){ try { if(startDate != null && startDate.length() >= 10) this.startDate=DateUtil.toDate( startDate, "yyyy-MM-dd" ); } catch(Exception e) { e.printStackTrace(); } }
	public Date getStartDate4Date(){ return startDate; }
	public void setStartDate4Date(Date startDate){ this.startDate=startDate; } 			
	// reference field
	
	
	// search conds
	private int pageNo = 1;
	private int pageSize = 10;
	private String sortColumn = null;
	private String sortMethod = "ASC";

	public Integer getPageNo() { return pageNo; }
	public void setPageNo(Integer pageNo) { if( pageNo != null ) this.pageNo = pageNo.intValue(); }
	public Integer getPageSize() { return pageSize; }
	public void setPageSize(Integer pageSize) { if( pageSize != null ) this.pageSize = pageSize.intValue(); }
	public String getSortColumn() { return sortColumn; }
	public void setSortColumn(String sortColumn) { if( sortColumn != null && sortColumn.length() > 0) this.sortColumn = sortColumn; }
	public String getSortMethod() { return this.sortMethod; }
	public void setSortMethod(String sortMethod) { this.sortMethod = sortMethod; }
	public int getStartRow() { return (pageNo-1) * pageSize + 1; }
	public int getEndRow() { return pageNo * pageSize; }
	
	private String titleCond = null;
	public String getTitleCond() { return this.titleCond.toString(); }
	public void setTitleCond(String titleCond) { if(titleCond != null && titleCond.length() > 0 && titleCond.indexOf("*")==-1 ) this.titleCond = titleCond; }
	private String titleCond2 = null;
	public String getTitleCond2() { return this.titleCond2.toString(); }
	public void setTitleCond2(String titleCond2) { if(titleCond2 != null && titleCond2.length() > 0 && titleCond2.indexOf("*")==-1 ) this.titleCond2 = titleCond2; }
	
	// reference objects
	
	
	// for result information
	private String resultStatus = null;
	private String failureReason = null;
	private int totalSize = 0;
	private int resultSize = 0;

	public String getResultStatus() { return resultStatus; }
	public void setResultStatus(String resultStatus) { this.resultStatus = resultStatus; }
	public String getFailureReason() { return failureReason; }
	public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
	public int getTotalSize() { return totalSize; }
	public void setTotalSize(int totalSize) { this.totalSize = totalSize; }
	public int getResultSize() { return resultSize; }
	public void setResultSize(int resultSize) { this.resultSize = resultSize; }
	
	// for detail information
	public TodayWordPK getPrimaryKey()
	{
		return new TodayWordPK(this.wordId);
	}
	
	// for retrieve list information
	public Map getSearchCondition()
	{
		Map condition = new HashMap();
		condition.put("pageNo", String.valueOf(this.pageNo));
		condition.put("pageSize", String.valueOf(this.pageSize));
		condition.put("sortColumn", this.sortColumn);
		condition.put("sortMethod", this.sortMethod);
		condition.put("startRow", String.valueOf((this.pageNo-1) * this.pageSize + 1));
		condition.put("endRow", String.valueOf(this.pageNo * this.pageSize));
		
		
		if(titleCond != null) condition.put("titleCond", this.titleCond);
		if(titleCond2 != null) condition.put("titleCond2", this.titleCond2);
	
		return condition;
	}

	// for update or insert information
	public TodayWordVO getUpdateObject()
	{
		TodayWordVO todayWordVO = new TodayWordVO();
		todayWordVO.setWordId( this.wordId );		
			todayWordVO.setTitle( this.title );		
			todayWordVO.setContent( this.content );		
			todayWordVO.setStartDate( this.startDate );		
			
		
		return todayWordVO;
	}
	
	// for remove information
	private String[] pks;
	private List removeKeyList = null;

	public List getRemoveKeyList() { return (removeKeyList != null) ? removeKeyList : new ArrayList(); }
	public void setPks(String[] pks) {
		if( pks != null && pks.length > 0 ) {
			this.pks = pks;
			this.removeKeyList = new ArrayList();
			for(int i=0; i<pks.length; i++) {
				if( pks[i]!=null && pks[i].length()>0 ) {
					String[] pk = pks[i].split(":");
					TodayWordPK todayWordPK = new TodayWordPK(
					Integer.valueOf( pk[0] ));
        			this.removeKeyList.add(todayWordPK);
				}
			}
		}
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
