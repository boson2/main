
package org.tok.cust.admin.todayWord.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import org.tok.cust.admin.todayWord.service.TodayWordVO;
import org.tok.cust.admin.todayWord.service.TodayWordPK;



/**  
 * @Class Name : TodayWordDAO.java
 * @Description : 오늘의 말씀 Data Access Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordDAO extends SqlMapClientDaoSupport
{
	/**
	 * Acquire todayWord total row size
	 * @param paramMap - Search Condition Map
	 * @return total row size
	 * @exception DataAccessException
	 */
	public int getTotalCount(Map paramMap) throws DataAccessException 
	{
		return (Integer)getSqlMapClientTemplate().queryForObject("todayWord.totalCount", paramMap);
	}
	
	/**
	 * Retrieve all todayWord list
	 * @param void
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findAll() throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("todayWord.findAll");
	}
	
	/**
	 * Retrieve todayWord list (pageUnit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findByPage(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("todayWord.findByPage", paramMap);
	}

	/**
	 * Acquire detail todayWord information
	 * @param PK - primary key TodayWordPK
	 * @return TodayWordVO
	 * @exception DataAccessException
	 */
	public TodayWordVO detail(TodayWordPK todayWordPK) throws DataAccessException 
	{
		return (TodayWordVO)getSqlMapClientTemplate().queryForObject("todayWord.detail", todayWordPK);
	}
	
	/**
	 * Check exist todayWord by key
	 * @param VO - modify data TodayWordVO
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(TodayWordVO todayWordVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("todayWord.exist", todayWordVO);
		return (cnt != 0) ? true : false;
	}
	
	/**
	 * Add new todayWord information
	 * @param VO - new data TodayWordVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(TodayWordVO todayWordVO) throws DataAccessException 
	{
        getSqlMapClientTemplate().insert("todayWord.insert", todayWordVO);
	}

	/**
	 * Modify todayWord information
	 * @param VO - modify data TodayWordVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void update(TodayWordVO todayWordVO) throws DataAccessException 
	{
		getSqlMapClientTemplate().update("todayWord.update", todayWordVO);
	}
	
	/**
	 * Remove todayWord information
	 * @param PK - primary key TodayWordPK
	 * @return int
	 * @exception DataAccessException
	 */
	public int delete(TodayWordPK todayWordPK) throws DataAccessException
	{
		return getSqlMapClientTemplate().delete("todayWord.delete", todayWordPK);
	}
	
	/**
	 * Remove todayWord information
	 * @param List - primary key TodayWordPK
	 * @return int
	 * @exception DataAccessException, SQLException
	 */
	public int delete(List removeKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=removeKeys.iterator(); it.hasNext(); ) {
			TodayWordPK todayWordPK = (TodayWordPK)it.next();
			getSqlMapClientTemplate().delete("todayWord.delete", todayWordPK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	
}
