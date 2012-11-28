
package org.tok.cust.admin.grade.web;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;

import org.tok.cust.admin.grade.service.GradeVO;
import org.tok.cust.admin.grade.service.GradePK;

/**  
 * @Class Name : GradeForm.java
 * @Description : 등급관리 Form
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradeForm
{
	// primary key
	
	private String code = null;
	
	// fields
	
	private String codeName = null;
	private String remark = null;
	
    public GradeForm()
	{
	
    }
	
	
	public String getCode(){ return code; }
    public void setCode(String code){ if(code != null && code.length() > 0) this.code=code; }			
	public String getCodeName(){ return codeName; }
    public void setCodeName(String codeName){ if(codeName != null && codeName.length() > 0) this.codeName=codeName; }			
	public String getRemark(){ return remark; }
    public void setRemark(String remark){ if(remark != null && remark.length() > 0) this.remark=remark; }			
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
	public GradePK getPrimaryKey()
	{
		return new GradePK(this.code);
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
		
		
	
		return condition;
	}

	// for update or insert information
	public GradeVO getUpdateObject()
	{
		GradeVO gradeVO = new GradeVO();
		gradeVO.setCode( this.code );		
			gradeVO.setCodeName( this.codeName );		
			gradeVO.setRemark( this.remark );		
			
		
		return gradeVO;
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
					GradePK gradePK = new GradePK(
					pk[0]);
        			this.removeKeyList.add(gradePK);
				}
			}
		}
	}
    
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Grade] ");
		
		buffer.append(" Code=\"").append( this.code ).append("\"");
		buffer.append(" CodeName=\"").append( this.codeName ).append("\"");
		buffer.append(" Remark=\"").append( this.remark ).append("\"");

		return buffer.toString();      
	}
}
