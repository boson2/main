
package org.tok.cust.admin.userpass.web;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;

import org.tok.cust.admin.userpass.service.UserpassVO;
import org.tok.cust.admin.userpass.service.UserpassPK;

/**  
 * @Class Name : UserpassForm.java
 * @Description : 개인정보 Form
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassForm
{
	// primary key
	
	private String userId = null;
	
	// fields
	
	private String nmKor = null;
	private String columnValue = null;
	private String gradeCd = null;
	private String emailAddr = null;
	private String homeTel = null;
	private String mobileTel = null;
	private String homeZip = null;
	private String homeAddr1 = null;
	private String homeAddr2 = null;
	private Date birthYmd = new Date(System.currentTimeMillis());
	private String intro = null;
	private String isActive = "off";
	private Integer sexFlag = 0;
	private Integer accessFlag = 1;
	private Integer age = 0;
	private String guide = null;
	
	public UserpassForm()
	{
	
    }
	
	
	public String getNmKor(){ return nmKor; }
    public void setNmKor(String nmKor){ if(nmKor != null && nmKor.length() > 0) this.nmKor=nmKor; }			
	public String getUserId(){ return userId; }
    public void setUserId(String userId){ if(userId != null && userId.length() > 0) this.userId=userId; }			
	public String getColumnValue(){ return columnValue; }
    public void setColumnValue(String columnValue){ if(columnValue != null && columnValue.length() > 0) this.columnValue=columnValue; }			
	public String getGradeCd(){ return gradeCd; }
    public void setGradeCd(String gradeCd){ if(gradeCd != null && gradeCd.length() > 0) this.gradeCd=gradeCd; }			
	public String getEmailAddr(){ return emailAddr; }
    public void setEmailAddr(String emailAddr){ if(emailAddr != null && emailAddr.length() > 0) this.emailAddr=emailAddr; }			
	public String getHomeTel(){ return homeTel; }
    public void setHomeTel(String homeTel){ if(homeTel != null && homeTel.length() > 0) this.homeTel=homeTel; }			
	public String getMobileTel(){ return mobileTel; }
    public void setMobileTel(String mobileTel){ if(mobileTel != null && mobileTel.length() > 0) this.mobileTel=mobileTel; }			
	public String getHomeZip(){ return homeZip; }
    public void setHomeZip(String homeZip){ if(homeZip != null && homeZip.length() > 0) this.homeZip=homeZip; }			
	public String getHomeAddr1(){ return homeAddr1; }
    public void setHomeAddr1(String homeAddr1){ if(homeAddr1 != null && homeAddr1.length() > 0) this.homeAddr1=homeAddr1; }			
	public String getHomeAddr2(){ return homeAddr2; }
    public void setHomeAddr2(String homeAddr2){ if(homeAddr2 != null && homeAddr2.length() > 0) this.homeAddr2=homeAddr2; }			
	public String getBirthYmd(){ return (birthYmd != null) ? birthYmd.toString() : ""; }
    public void setBirthYmd(String birthYmd){ try { if(birthYmd != null && birthYmd.length() >= 10) this.birthYmd=DateUtil.toDate( birthYmd, "yyyy-MM-dd" ); } catch(Exception e) { e.printStackTrace(); } }
	public Date getBirthYmd4Date(){ return birthYmd; }
	public void setBirthYmd4Date(Date birthYmd){ this.birthYmd=birthYmd; } 			
	public String getIntro(){ return intro; }
    public void setIntro(String intro){ if(intro != null && intro.length() > 0) this.intro=intro; }			
	public String getIsActive(){ return isActive; }
    public void setIsActive(String isActive){ this.isActive=isActive; }			
	public Integer getSexFlag(){ return sexFlag; }
    public void setSexFlag(Integer sexFlag){ this.sexFlag=sexFlag; }
	public Integer getAccessFlag() { return accessFlag; }
	public void setAccessFlag(Integer accessFlag) { this.accessFlag = accessFlag; }

	public Integer getAge(){ return age; }
    public void setAge(Integer age){ this.age=age; }	
    
    public String getGuide() { return guide; }
	public void setGuide(String guide) { this.guide = guide; }

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
	
	private String gradeCdCond = null;
	private String userIdCond = null;
	private String nmKorCond = null;
	public String getGradeCdCond() { return gradeCdCond; }
	public void setGradeCdCond(String gradeCdCond) { if(gradeCdCond != null && gradeCdCond.length() > 0 && gradeCdCond.indexOf("*")==-1 ) this.gradeCdCond = gradeCdCond; } 
	public String getUserIdCond() { return this.userIdCond.toString(); }
	public void setUserIdCond(String userIdCond) { if(userIdCond != null && userIdCond.length() > 0 && userIdCond.indexOf("*")==-1 ) this.userIdCond = userIdCond; }
	public String getNmKorCond() { return this.nmKorCond.toString(); }
	public void setNmKorCond(String nmKorCond) { if(nmKorCond != null && nmKorCond.length() > 0 && nmKorCond.indexOf("*")==-1 ) this.nmKorCond = nmKorCond; }
	
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
	public UserpassPK getPrimaryKey()
	{
		return new UserpassPK(this.userId);
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
		
		if(gradeCdCond != null) condition.put("gradeCdCond", this.gradeCdCond);
		if(userIdCond != null) condition.put("userIdCond", this.userIdCond);
		if(nmKorCond != null) condition.put("nmKorCond", this.nmKorCond);
	
		return condition;
	}

	// for update or insert information
	public UserpassVO getUpdateObject()
	{
		//System.out.println("########## sexFlag=" + sexFlag + ", accessFlag=" + accessFlag);
		UserpassVO userpassVO = new UserpassVO();
		userpassVO.setNmKor( this.nmKor );		
			userpassVO.setUserId( this.userId );		
			userpassVO.setColumnValue( this.columnValue );		
			userpassVO.setGradeCd( this.gradeCd );		
			userpassVO.setEmailAddr( this.emailAddr );		
			userpassVO.setHomeTel( this.homeTel );		
			userpassVO.setMobileTel( this.mobileTel );		
			userpassVO.setHomeZip( this.homeZip );		
			userpassVO.setHomeAddr1( this.homeAddr1 );		
			userpassVO.setHomeAddr2( this.homeAddr2 );		
			userpassVO.setBirthYmd( this.birthYmd );		
			userpassVO.setIntro( this.intro );		
			userpassVO.setIsActive( this.isActive );		
			userpassVO.setSexFlag( this.sexFlag );		
			userpassVO.setAccessFlag( this.accessFlag );	
			userpassVO.setAge( this.age );		
			userpassVO.setGuide( this.guide );		
			
		
		return userpassVO;
	}
	
	// for remove information
	private String[] pks;
	private List removeKeyList = null;
	private List updateKeyList = null;

	public List getRemoveKeyList() { return (removeKeyList != null) ? removeKeyList : new ArrayList(); }
	public List getUpdateKeyList() { return (updateKeyList != null) ? updateKeyList : new ArrayList(); }
	public void setPks(String[] pks) {
		if( pks != null && pks.length > 0 ) {
			this.pks = pks;
			this.removeKeyList = new ArrayList();
			this.updateKeyList = new ArrayList();
			for(int i=0; i<pks.length; i++) {
				if( pks[i]!=null && pks[i].length()>0 ) {
					String[] pk = pks[i].split(":");
					UserpassPK userpassPK = new UserpassPK(
					pk[0]);
        			this.removeKeyList.add(userpassPK);
        			this.updateKeyList.add(userpassPK);
				}
			}
		}
	}
    
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[Userpass] ");
		
		buffer.append(" NmKor=\"").append( this.nmKor ).append("\"");
		buffer.append(" UserId=\"").append( this.userId ).append("\"");
		buffer.append(" ColumnValue=\"").append( this.columnValue ).append("\"");
		buffer.append(" GradeCd=\"").append( this.gradeCd ).append("\"");
		buffer.append(" EmailAddr=\"").append( this.emailAddr ).append("\"");
		buffer.append(" HomeTel=\"").append( this.homeTel ).append("\"");
		buffer.append(" MobileTel=\"").append( this.mobileTel ).append("\"");
		buffer.append(" HomeZip=\"").append( this.homeZip ).append("\"");
		buffer.append(" HomeAddr1=\"").append( this.homeAddr1 ).append("\"");
		buffer.append(" HomeAddr2=\"").append( this.homeAddr2 ).append("\"");
		buffer.append(" BirthYmd=\"").append( this.birthYmd ).append("\"");
		buffer.append(" Intro=\"").append( this.intro ).append("\"");
		buffer.append(" IsActive=\"").append( this.isActive ).append("\"");
		buffer.append(" SexFlag=\"").append( this.sexFlag ).append("\"");
		buffer.append(" Age=\"").append( this.age ).append("\"");

		return buffer.toString();      
	}
}
