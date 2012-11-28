
package org.tok.cust.admin.dymicMenu.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.util.DateUtil;

/**  
 * @Class Name : DymicMenuPK.java
 * @Description : 동적메뉴관리 Primary Key Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class DymicMenuPK
{
	// primary key
	
	private Integer menuId = 0;
	private HttpServletRequest request = null;
	
    public DymicMenuPK()
	{
	
    }
	
    public DymicMenuPK(Integer menuId)
	{
		this();
		this.menuId = menuId;
    }
	
	public Integer getMenuId(){ return menuId; }
    public void setMenuId(Integer menuId){ this.menuId=menuId; }			
	public HttpServletRequest getRequest() { return request; }
	public void setRequest(HttpServletRequest request) { this.request = request; }

	// master key
	

	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("[DymicMenu] ");
		
		buffer.append(" MenuId=\"").append( this.menuId ).append("\"");

		return buffer.toString();      
	}
}
