
package org.tok.cust.admin.grade.service;

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
 * @Class Name : GradeVO.java
 * @Description : 등급관리 Value Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradeVO
{
	// primary key
	
	private String code = null;
	
	// fields
	
	private String codeName = null;
	private String remark = null;
	private Integer principalId = 0;
	private String sortOrder = "0";
	
	public GradeVO()
	{
	
    }
	
    public GradeVO(String code)
	{
		this();
		this.code = code;
		
    }
	
	public GradePK getPrimaryKey()
	{
		GradePK gradePK = new GradePK(this.code);
		
		return gradePK;
	}
	
	public String getCode(){ return code; }
    public void setCode(String code){ this.code=code; }
	public String getCodeName(){ return codeName; }
    public void setCodeName(String codeName){ this.codeName=codeName; }
	public String getRemark(){ return remark; }
    public void setRemark(String remark){ this.remark=remark; }
    public Integer getPrincipalId() { return principalId; }
	public void setPrincipalId(Integer principalId) { this.principalId = principalId; }
	public String getSortOrder() { return sortOrder; }
	public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

	// reference objects
	
	public String forJSON(String str) {
		return str.replace("\\", "\\\\")
				  .replace("\r\n", "\\n")
				  .replace("\n", "\\n");
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
