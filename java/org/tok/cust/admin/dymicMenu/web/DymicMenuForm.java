
package org.tok.cust.admin.dymicMenu.web;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;

import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.dymicMenu.service.DymicMenuPK;

/**  
 * @Class Name : DymicMenuForm.java
 * @Description : 동적메뉴관리 Form
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see 
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class DymicMenuForm
{
	// primary key
	
	private Integer menuId = 0;
	
	// fields
	
	private String menuName = null;
	private String desc = null;
	private String applyFeed = null;
	private String writeAuth = null;
	private String readAuth = null;
	private String url = null;
	private String isService = "0";
	
	private HttpServletRequest request = null;
	
    public DymicMenuForm()
	{
	
    }
	
	
	public String getMenuName(){ return menuName; }
    public void setMenuName(String menuName){ if(menuName != null && menuName.length() > 0) this.menuName=menuName; }			
	public Integer getMenuId(){ return menuId; }
    public void setMenuId(Integer menuId){ this.menuId=menuId; }			
	public String getDesc(){ return desc; }
    public void setDesc(String desc){ if(desc != null && desc.length() > 0) this.desc=desc; }			
	public String getApplyFeed(){ return applyFeed; }
    public void setApplyFeed(String applyFeed){ if(applyFeed != null && applyFeed.length() > 0) this.applyFeed=applyFeed; }			
	public String getWriteAuth(){ return writeAuth; }
    public void setWriteAuth(String writeAuth){ if(writeAuth != null && writeAuth.length() > 0) this.writeAuth=writeAuth; }			
	public String getReadAuth(){ return readAuth; }
    public void setReadAuth(String readAuth){ if(readAuth != null && readAuth.length() > 0) this.readAuth=readAuth; }			
	public String getUrl(){ return url; }
    public void setUrl(String url){ if(url != null && url.length() > 0) this.url=url; }			
	public String getIsService(){ return isService; }
    public void setIsService(String isService){ this.isService=isService; }			
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
	
	public HttpServletRequest getRequest() { return request; }
	public void setRequest(HttpServletRequest request) { this.request = request; }
	
	// for detail information
	public DymicMenuPK getPrimaryKey()
	{
		return new DymicMenuPK(this.menuId);
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
	
		return condition;
	}

	// for update or insert information
	public DymicMenuVO getUpdateObject()
	{
		DymicMenuVO dymicMenuVO = new DymicMenuVO();
		dymicMenuVO.setMenuName( this.menuName );		
		dymicMenuVO.setMenuId( this.menuId );		
		dymicMenuVO.setDesc( this.desc );		
		dymicMenuVO.setApplyFeed( this.applyFeed );		
		dymicMenuVO.setWriteAuth( this.writeAuth );		
		dymicMenuVO.setReadAuth( this.readAuth );		
		dymicMenuVO.setUrl( this.url );		
		dymicMenuVO.setIsService( (this.isService != null) ? Integer.valueOf(this.isService) : 0 );		
		dymicMenuVO.setRequest(request);
		
		return dymicMenuVO;
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
					DymicMenuPK dymicMenuPK = new DymicMenuPK(
					Integer.valueOf( pk[0] ));
					dymicMenuPK.setRequest(request);
        			this.removeKeyList.add(dymicMenuPK);
				}
			}
		}
	}
    
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[DymicMenu] ");
		
		buffer.append(" MenuName=\"").append( this.menuName ).append("\"");
		buffer.append(" MenuId=\"").append( this.menuId ).append("\"");
		buffer.append(" Desc=\"").append( this.desc ).append("\"");
		buffer.append(" ApplyFeed=\"").append( this.applyFeed ).append("\"");
		buffer.append(" WriteAuth=\"").append( this.writeAuth ).append("\"");
		buffer.append(" ReadAuth=\"").append( this.readAuth ).append("\"");
		buffer.append(" Url=\"").append( this.url ).append("\"");
		buffer.append(" IsService=\"").append( this.isService ).append("\"");

		return buffer.toString();      
	}
}
