
package org.tok.cust.admin.userpass.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tok.view.Talk;
import org.tok.view.util.DateUtil;
import org.tok.view.util.StringUtil;



/**  
 * @Class Name : UserpassVO.java
 * @Description : 개인정보 Value Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassVO
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
	private Date birthYmd = null;
	private String birthYmdValue = null;
	private String intro = null;
	private String isActive = "off";
	private Integer sexFlag = 1;
	private String sex = "남성";
	private Integer accessFlag = 1;
	private String access = "접속허용";
	private Date regDatim = null;
	private String regDatimValue = null;
	private Integer age = 0;
	private Date lastAccess = null;
	
	private String guide = null;
	
	public UserpassVO()
	{
	
    }
	
    public UserpassVO(String userId)
	{
		this();
		this.userId = userId;
		
    }
	
	public UserpassPK getPrimaryKey()
	{
		UserpassPK userpassPK = new UserpassPK(this.userId);
		
		return userpassPK;
	}
	
	public String getNmKor(){ return nmKor; }
    public void setNmKor(String nmKor){ this.nmKor=nmKor; }
	public String getUserId(){ return userId; }
    public void setUserId(String userId){ this.userId=userId; }
	public String getColumnValue(){ return columnValue; }
    public void setColumnValue(String columnValue){ this.columnValue=columnValue; }
	public String getGradeCd(){ return gradeCd; }
    public void setGradeCd(String gradeCd){ this.gradeCd=gradeCd; }
	public String getEmailAddr(){ return emailAddr; }
    public void setEmailAddr(String emailAddr){ this.emailAddr=emailAddr; }
	public String getHomeTel(){ return homeTel; }
    public void setHomeTel(String homeTel){ this.homeTel=homeTel; }
	public String getMobileTel(){ return mobileTel; }
    public void setMobileTel(String mobileTel){ this.mobileTel=mobileTel; }
	public String getHomeZip(){ return homeZip; }
    public void setHomeZip(String homeZip){ this.homeZip=homeZip; }
	public String getHomeAddr1(){ return homeAddr1; }
    public void setHomeAddr1(String homeAddr1){ this.homeAddr1=homeAddr1; }
	public String getHomeAddr2(){ return homeAddr2; }
    public void setHomeAddr2(String homeAddr2){ this.homeAddr2=homeAddr2; }
	public Date getBirthYmd(){ return birthYmd; }
    public void setBirthYmd(Date birthYmd){ 
    	if( birthYmd != null) {
    		int currentYear = DateUtil.getCurrentYear();
    		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy");
    		String yearStr = simpledateformat.format(birthYmd);
    		int birthYear = Integer.parseInt(yearStr);
    		this.age = currentYear - birthYear;
    		//long birthYmdTime = birthYmd.getTime();
    		//this.age = (int)((System.currentTimeMillis() - birthYmdTime) / (1000*3600*24*365));
    		//System.out.println("####### birth=" + birthYmdTime + ", diff=" + (System.currentTimeMillis() - birthYmdTime) + ", age=" + age);
        	this.birthYmd = birthYmd; 
        	this.birthYmdValue = DateUtil.toString(birthYmd, "yyyy-MM-dd");
    	}
    }
	public String getBirthYmdByFormat(){ return DateUtil.toString(birthYmd, "yyyy-MM-dd"); }
	public String getBirthYmdValue() { return birthYmdValue; }
	public void setBirthYmdValue(String birthYmdValue) { this.birthYmdValue = birthYmdValue; }

	public String getIntro(){ return intro; }
    public void setIntro(String intro){ this.intro=intro; }
	public String getIsActive(){ return isActive; }
    public void setIsActive(String isActive){ this.isActive=isActive; }
	public Integer getSexFlag(){ return (sexFlag != null) ? sexFlag : 1; }
    public void setSexFlag(Integer sexFlag){ this.sexFlag=sexFlag; }
	public String getSex() { return sex; }
	public void setSex(String sex) { this.sex = sex; }
	public Integer getAccessFlag() { return accessFlag; }
	public void setAccessFlag(Integer accessFlag) { 
		this.accessFlag = accessFlag; 
		if( accessFlag == 0 ) {
			this.access = "접속차단";
		}
		else {
			this.access = "접속허용";
		}
	}
	public void setAccess(String access) { this.access = access; }
	public String getAccess() { return access; }
	public Date getRegDatim(){ return regDatim; }
    public void setRegDatim(Date regDatim){ this.regDatim=regDatim; }
	public String getRegDatimByFormat(){ return DateUtil.toString(regDatim, "yyyy-MM-dd"); }
	public String getRegDatimValue() { return DateUtil.toString(regDatim, "yyyy-MM-dd"); }
	public void setRegDatimValue(String regDatimValue) { this.regDatimValue = regDatimValue; }
	public Integer getAge(){ return age; }
    public void setAge(Integer age){ this.age=age; }
    public Date getLastAccess() { return null; }
	public void setLastAccess(Date lastAccess) { 
		long lastAccessDuration = Talk.getConfiguration().getLong("portal.lastAccess.duration", 604800000);
		if( lastAccess != null && lastAccess.getTime() >= (System.currentTimeMillis()-lastAccessDuration) ) {
			setIsActive("on");
		}
		else {
			setIsActive("off");
		}
		//if( lastAccess != null ) {
		//	System.out.println("####### lastAccess=" + lastAccess + ", lastAccessTime=" + lastAccess.getTime() + ", lastAccessDuration=" + (System.currentTimeMillis()-lastAccessDuration) + ", active=" + getIsActive());
		//}
		//else {
		//	System.out.println("####### lastAccess is null");
		//}
	}
	
	public String getGuide() { return guide; }
	public void setGuide(String guide) { this.guide = guide; }

	// reference objects
	
	public String forJSON(String str) {
		return str.replace("\\", "\\\\")
				  .replace("\r\n", "\\n")
				  .replace("\n", "\\n");
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
		buffer.append(" RegDatim=\"").append( this.regDatim ).append("\"");
		buffer.append(" Age=\"").append( this.age ).append("\"");

		return buffer.toString();      
	}
}
