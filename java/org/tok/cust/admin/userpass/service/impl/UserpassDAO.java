
package org.tok.cust.admin.userpass.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import org.tok.cust.admin.userpass.service.UserpassVO;
import org.tok.cust.admin.userpass.service.UserpassPK;



/**  
 * @Class Name : UserpassDAO.java
 * @Description : 개인정보 Data Access Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class UserpassDAO extends SqlMapClientDaoSupport
{
	/**
	 * Acquire userpass total row size
	 * @param paramMap - Search Condition Map
	 * @return total row size
	 * @exception DataAccessException
	 */
	public int getTotalCount(Map paramMap) throws DataAccessException 
	{
		return (Integer)getSqlMapClientTemplate().queryForObject("userpass.totalCount", paramMap);
	}
	
	/**
	 * Retrieve all userpass list
	 * @param void
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findAll() throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userpass.findAll");
	}
	
	/**
	 * Retrieve userpass list (pageUnit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findByPage(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userpass.findByPage", paramMap);
	}
	
	/**
	 * Acquire userpass total row size
	 * @param paramMap - Search Condition Map
	 * @return total row size
	 * @exception DataAccessException
	 */
	public int getNewTotalCount(Map paramMap) throws DataAccessException 
	{
		return (Integer)getSqlMapClientTemplate().queryForObject("userpass.newTotalCount", paramMap);
	}
	
	/**
	 * Retrieve userpass list (pageUnit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findNewByPage(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("userpass.findNewByPage", paramMap);
	}

	/**
	 * Acquire detail userpass information
	 * @param PK - primary key UserpassPK
	 * @return UserpassVO
	 * @exception DataAccessException
	 */
	public UserpassVO detail(UserpassPK userpassPK) throws DataAccessException 
	{
		return (UserpassVO)getSqlMapClientTemplate().queryForObject("userpass.detail", userpassPK);
	}
	
	/**
	 * Check exist userpass by key
	 * @param VO - modify data UserpassVO
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(UserpassVO userpassVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("userpass.exist", userpassVO);
		return (cnt != 0) ? true : false;
	}
	
	/**
	 * Add new userpass information
	 * @param VO - new data UserpassVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(UserpassVO userpassVO) throws DataAccessException 
	{
        getSqlMapClientTemplate().insert("userpass.insert", userpassVO);
	}

	/**
	 * Modify userpass information
	 * @param VO - modify data UserpassVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void update(UserpassVO userpassVO) throws DataAccessException 
	{
		getSqlMapClientTemplate().update("securityPrincipal.access", userpassVO);
		getSqlMapClientTemplate().update("userpass.update", userpassVO);
	}
	
	/**
	 * Remove userpass information
	 * @param PK - primary key UserpassPK
	 * @return int
	 * @exception DataAccessException
	 */
	public int delete(UserpassPK userpassPK) throws DataAccessException
	{
		return getSqlMapClientTemplate().delete("userpass.delete", userpassPK);
	}
	
	/**
	 * Remove userpass information
	 * @param List - primary key UserpassPK
	 * @return int
	 * @exception DataAccessException, SQLException
	 */
	public int delete(List removeKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=removeKeys.iterator(); it.hasNext(); ) {
			UserpassPK userpassPK = (UserpassPK)it.next();
			getSqlMapClientTemplate().delete("userpass.deleteSecurityPermission", userpassPK);
			getSqlMapClientTemplate().delete("userpass.deleteUserRole", userpassPK);
			getSqlMapClientTemplate().delete("userpass.delete", userpassPK);
			getSqlMapClientTemplate().delete("userpass.deleteSecurityPrincipal", userpassPK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	/**
	 * AccessDeny userpass information
	 * @param List - primary key UserpassPK
	 * @return int
	 * @exception DataAccessException, SQLException
	 */
	public int accessDeny(List updateKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=updateKeys.iterator(); it.hasNext(); ) {
			UserpassPK userpassPK = (UserpassPK)it.next();
			getSqlMapClientTemplate().update("userpass.accessDeny", userpassPK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	/**
	 * Approve userpass information
	 * @param List - primary key UserpassPK
	 * @return int
	 * @exception DataAccessException, SQLException
	 */
	public int approve(List updateKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=updateKeys.iterator(); it.hasNext(); ) {
			UserpassPK userpassPK = (UserpassPK)it.next();
			getSqlMapClientTemplate().update("userpass.approve", userpassPK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
}
