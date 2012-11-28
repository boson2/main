
package org.tok.cust.admin.userpass.service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import org.tok.view.exception.BaseException;

import org.tok.cust.admin.userpass.service.UserpassVO;
import org.tok.cust.admin.userpass.service.UserpassPK;


/**  
 * @Class Name : UserpassService.java
 * @Description : 개인정보 Service Interface
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public interface UserpassService
{
	
	/**
	 * Retrieve userpass list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException;
	
	/**
	 * Retrieve userpass list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findNewByPage(Map paramMap) throws BaseException;

	/**
	 * Acquire detail userpass information
	 * @param PK - primary key UserpassPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return UserpassVO
	 * @exception BaseException
	 */
	public UserpassVO detail(UserpassPK userpassPK, boolean lazyRetrieve) throws BaseException;

	/**
	 * Check exist userpass by key
	 * @param VO - modify data UserpassVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(UserpassVO userpassVO) throws BaseException;
	
	/**
	 * Add new userpass information
	 * @param VO - new data UserpassVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(UserpassVO userpassVO) throws BaseException;

	/**
	 * Modify userpass information
	 * @param VO - modify data UserpassVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(UserpassVO userpassVO) throws BaseException;
	
	/**
	 * Remove userpass information
	 * @param VO - Userpass Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(UserpassVO userpassVO) throws BaseException;

	/**
	 * Remove userpass information
	 * @param PK - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(UserpassPK userpassPK, boolean cascade) throws BaseException;
	
	/**
	 * Remove userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException;
	
	/**
	 * AccessDeny userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void accessDeny(List updateKeys, boolean cascade) throws BaseException;
	
	/**
	 * Approve userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void approve(List updateKeys, boolean cascade) throws BaseException;
	
}
