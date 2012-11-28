
package org.tok.cust.admin.userpass.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tok.view.Talk;
import org.tok.view.admin.credential.service.CredentialService;
import org.tok.view.admin.credential.service.CredentialVO;
import org.tok.view.admin.user.service.UserVO;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.exception.BaseException;
import org.tok.view.util.ResultSetList;
import org.tok.view.idgenerator.IdGenerator;

import org.tok.cust.admin.userpass.service.UserpassVO;
import org.tok.cust.admin.userpass.service.UserpassPK;
import org.tok.cust.admin.userpass.service.UserpassService;
import org.tok.cust.admin.userpass.service.impl.UserpassDAO;

/**  
 * @Class Name : UserpassServiceImpl.java
 * @Description : 개인정보 Service Implementation
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassServiceImpl implements UserpassService
{
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	private TalkCodeManager codeManager;
	private CredentialService credentialService;
	
	public UserpassServiceImpl()
	{
		this.idGenerator = (IdGenerator)Talk.getComponentManager().getComponent("IdGenerator");
		this.codeManager = TalkCodeManager.getInstance();
		this.credentialService = (CredentialService)Talk.getComponentManager().getComponent("org.tok.view.admin.credential.service.CredentialService");
	}
	
	private UserpassDAO userpassDAO;
	public void setUserpassDAO(UserpassDAO userpassDAO) {
		this.userpassDAO = userpassDAO;
	}
	
	
	/**
	 * Retrieve userpass list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException 
	{
		ResultSetList resultList = new ResultSetList();
    	try {
			int totalCount = userpassDAO.getTotalCount(paramMap);
    		List results = userpassDAO.findByPage(paramMap);
			for(Iterator it=results.iterator(); it.hasNext(); ) {
				UserpassVO userpassVO = (UserpassVO)it.next();
				switch( userpassVO.getSexFlag() ) {
				case 1 :
					userpassVO.setSex("남성");
					break;
				case 2 :
					userpassVO.setSex("여성");
					break;
				}
				if( userpassVO.getGradeCd() != null && userpassVO.getGradeCd().length() > 0 ) {
					userpassVO.setGradeCd( codeManager.getCodeName("PT", "118", userpassVO.getGradeCd(), "ko") );
				}
				resultList.add( userpassVO );
			}
	        resultList.setTotalCount(totalCount);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return resultList;
	}
	
	/**
	 * Retrieve userpass list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findNewByPage(Map paramMap) throws BaseException 
	{
		ResultSetList resultList = new ResultSetList();
    	try {
			int totalCount = userpassDAO.getNewTotalCount(paramMap);
    		List results = userpassDAO.findNewByPage(paramMap);
			for(Iterator it=results.iterator(); it.hasNext(); ) {
				UserpassVO userpassVO = (UserpassVO)it.next();
				switch( userpassVO.getSexFlag() ) {
				case 1 :
					userpassVO.setSex("남성");
					break;
				case 2 :
					userpassVO.setSex("여성");
					break;
				}
				resultList.add( userpassVO );
			}
	        resultList.setTotalCount(totalCount);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return resultList;
	}
	
	/**
	 * Acquire detail userpass information
	 * @param PK - primary key UserpassPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return UserpassVO
	 * @exception BaseException
	 */
	public UserpassVO detail(UserpassPK userpassPK, boolean lazyRetrieve) throws BaseException 
	{
		UserpassVO userpassVO = null;
    	try {
    		userpassVO = userpassDAO.detail(userpassPK);
			
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return userpassVO;
	}
	
	/**
	 * Check exist userpass by key
	 * @param VO - modify data UserpassVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(UserpassVO userpassVO) throws BaseException
	{
		return userpassDAO.exist(userpassVO);
	}
	
	/**
	 * Add new userpass information
	 * @param VO - new data UserpassVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(UserpassVO userpassVO) throws BaseException
	{
    	try {
			userpassDAO.insert(userpassVO);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Modify userpass information
	 * @param VO - modify data UserpassVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(UserpassVO userpassVO) throws BaseException
	{
    	try {
			userpassDAO.update(userpassVO);
			
			// update reference object
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove userpass information
	 * @param VO - Userpass Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(UserpassVO userpassVO) throws BaseException
	{
		try {
			// remove reference object
			userpassDAO.delete( userpassVO.getPrimaryKey() );
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove userpass information
	 * @param PK - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(UserpassPK userpassPK, boolean cascade) throws BaseException
	{
		try {
			userpassDAO.delete(userpassPK);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException
	{
    	try {
			userpassDAO.delete(removeKeys);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * AccessDeny userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void accessDeny(List updateKeys, boolean cascade) throws BaseException
	{
    	try {
			userpassDAO.accessDeny(updateKeys);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Approve userpass information
	 * @param List - primary key UserpassPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void approve(List updateKeys, boolean cascade) throws BaseException
	{
    	try {
			userpassDAO.approve(updateKeys);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
}
