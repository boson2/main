
package org.tok.cust.admin.dymicMenu.service;

import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;
import org.tok.view.util.StringUtil;



/**  
 * @Class Name : DymicMenuVO.java
 * @Description : 동적메뉴관리 Value Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class DymicMenuVO
{
	// primary key
	
	private Integer menuId = 0;
	
	// fields
	
	private String menuName = null;
	private String desc = null;
	private Integer sortOrder = 0;
	private String applyFeed = null;
	private String writeAuth = null;
	private String readAuth = null;
	private String url = null;
	private Integer isService = 0;
	private String serviceStatus = "on";
	private HttpServletRequest request = null;
	
	public DymicMenuVO()
	{
	
    }
	
    public DymicMenuVO(Integer menuId)
	{
		this();
		this.menuId = menuId;
		
    }
	
	public DymicMenuPK getPrimaryKey()
	{
		DymicMenuPK dymicMenuPK = new DymicMenuPK(this.menuId);
		
		return dymicMenuPK;
	}
	
	public String getMenuName(){ return menuName; }
    public void setMenuName(String menuName){ this.menuName=menuName; }
	public Integer getMenuId(){ return menuId; }
    public void setMenuId(Integer menuId){ this.menuId=menuId; }
	public String getDesc(){ return desc; }
    public void setDesc(String desc){ this.desc=desc; }
	public Integer getSortOrder(){ return sortOrder; }
    public void setSortOrder(Integer sortOrder){ this.sortOrder=sortOrder; }
	public String getApplyFeed(){ return applyFeed; }
    public void setApplyFeed(String applyFeed){ this.applyFeed=applyFeed; }
	public String getWriteAuth(){ return writeAuth; }
    public void setWriteAuth(String writeAuth){ this.writeAuth=writeAuth; }
	public String getReadAuth(){ return readAuth; }
    public void setReadAuth(String readAuth){ this.readAuth=readAuth; }
	public String getUrl(){ return url; }
    public void setUrl(String url){ this.url=url; }
	public Integer getIsService(){ return isService; }
    public void setIsService(Integer isService){ this.isService=isService; }
    public String getServiceStatus() { return serviceStatus; }
	public void setServiceStatus(String serviceStatus) { this.serviceStatus = serviceStatus; }
	public HttpServletRequest getRequest() { return request; }
	public void setRequest(HttpServletRequest request) { this.request = request; }

	// reference objects
	
	public String forJSON(String str) {
		return str.replace("\\", "\\\\")
				  .replace("\r\n", "\\n")
				  .replace("\n", "\\n");
	}
	
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[DymicMenu] ");
		
		buffer.append(" MenuName=\"").append( this.menuName ).append("\"");
		buffer.append(" MenuId=\"").append( this.menuId ).append("\"");
		buffer.append(" Desc=\"").append( this.desc ).append("\"");
		buffer.append(" SortOrder=\"").append( this.sortOrder ).append("\"");
		buffer.append(" ApplyFeed=\"").append( this.applyFeed ).append("\"");
		buffer.append(" WriteAuth=\"").append( this.writeAuth ).append("\"");
		buffer.append(" ReadAuth=\"").append( this.readAuth ).append("\"");
		buffer.append(" Url=\"").append( this.url ).append("\"");
		buffer.append(" IsService=\"").append( this.isService ).append("\"");

		return buffer.toString();      
	}
}
