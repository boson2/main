
package org.tok.cust.admin.dymicMenu.service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.exception.BaseException;

import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.dymicMenu.service.DymicMenuPK;


/**  
 * @Class Name : DymicMenuService.java
 * @Description : 동적메뉴관리 Service Interface
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public interface DymicMenuService
{
	
	/**
	 * Retrieve dymicMenu list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException;

	/**
	 * Acquire detail dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return DymicMenuVO
	 * @exception BaseException
	 */
	public DymicMenuVO detail(DymicMenuPK dymicMenuPK, boolean lazyRetrieve) throws BaseException;

	/**
	 * Check exist dymicMenu by key
	 * @param VO - modify data DymicMenuVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(DymicMenuVO dymicMenuVO) throws BaseException;
	
	/**
	 * Add new dymicMenu information
	 * @param VO - new data DymicMenuVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(DymicMenuVO dymicMenuVO) throws BaseException;

	/**
	 * Modify dymicMenu information
	 * @param VO - modify data DymicMenuVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(DymicMenuVO dymicMenuVO) throws BaseException;
	
	/**
	 * Remove dymicMenu information
	 * @param VO - DymicMenu Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(DymicMenuVO dymicMenuVO) throws BaseException;

	/**
	 * Remove dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(DymicMenuPK dymicMenuPK, boolean cascade) throws BaseException;
	
	/**
	 * Remove dymicMenu information
	 * @param List - primary key DymicMenuPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException;
	
	/**
	 * Modify page order information
	 * @param paramMap - primary key (id) / order direction (toDown)
	 * @return void
	 * @exception BaseException
	 */
	public void changeTreeOrder(Map paramMap) throws BaseException;

	public List getMainMenuList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException;
	public List getMainCafeList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException;
	
}
