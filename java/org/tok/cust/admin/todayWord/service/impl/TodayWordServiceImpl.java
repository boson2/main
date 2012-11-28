
package org.tok.cust.admin.todayWord.service.impl;

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
import org.tok.view.exception.BaseException;
import org.tok.view.util.ResultSetList;
import org.tok.view.idgenerator.IdGenerator;

import org.tok.cust.admin.todayWord.service.TodayWordVO;
import org.tok.cust.admin.todayWord.service.TodayWordPK;
import org.tok.cust.admin.todayWord.service.TodayWordService;
import org.tok.cust.admin.todayWord.service.impl.TodayWordDAO;

/**  
 * @Class Name : TodayWordServiceImpl.java
 * @Description : 오늘의 말씀 Service Implementation
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class TodayWordServiceImpl implements TodayWordService
{
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	
	public TodayWordServiceImpl()
	{
		this.idGenerator = (IdGenerator)Talk.getComponentManager().getComponent("IdGenerator");
	}
	
	private TodayWordDAO todayWordDAO;
	public void setTodayWordDAO(TodayWordDAO todayWordDAO) {
		this.todayWordDAO = todayWordDAO;
	}
	
	
	/**
	 * Retrieve todayWord list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException 
	{
		ResultSetList resultList = new ResultSetList();
    	try {
			int totalCount = todayWordDAO.getTotalCount(paramMap);
    		List keys = todayWordDAO.findByPage(paramMap);
			for(Iterator it=keys.iterator(); it.hasNext(); ) {
				resultList.add( todayWordDAO.detail((TodayWordPK)it.next()) );
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
	 * Acquire detail todayWord information
	 * @param PK - primary key TodayWordPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return TodayWordVO
	 * @exception BaseException
	 */
	public TodayWordVO detail(TodayWordPK todayWordPK, boolean lazyRetrieve) throws BaseException 
	{
		TodayWordVO todayWordVO = null;
    	try {
    		todayWordVO = todayWordDAO.detail(todayWordPK);
			
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return todayWordVO;
	}
	
	/**
	 * Check exist todayWord by key
	 * @param VO - modify data TodayWordVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(TodayWordVO todayWordVO) throws BaseException
	{
		return todayWordDAO.exist(todayWordVO);
	}
	
	/**
	 * Add new todayWord information
	 * @param VO - new data TodayWordVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(TodayWordVO todayWordVO) throws BaseException
	{
    	try {
			
			// get parent data
			todayWordVO.setWordId( this.idGenerator.getNextIntegerId("TODAY_WORD") );
			// before insert
			todayWordDAO.insert(todayWordVO);
			
			// insert reference object
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Modify todayWord information
	 * @param VO - modify data TodayWordVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(TodayWordVO todayWordVO) throws BaseException
	{
    	try {
			todayWordDAO.update(todayWordVO);
			
			// update reference object
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove todayWord information
	 * @param VO - TodayWord Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(TodayWordVO todayWordVO) throws BaseException
	{
		try {
			// remove reference object
			todayWordDAO.delete( todayWordVO.getPrimaryKey() );
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove todayWord information
	 * @param PK - primary key TodayWordPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(TodayWordPK todayWordPK, boolean cascade) throws BaseException
	{
		try {
			todayWordDAO.delete(todayWordPK);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove todayWord information
	 * @param List - primary key TodayWordPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException
	{
    	try {
			todayWordDAO.delete(removeKeys);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
}
