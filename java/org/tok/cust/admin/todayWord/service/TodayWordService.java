
package org.tok.cust.admin.todayWord.service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import org.tok.view.exception.BaseException;

import org.tok.cust.admin.todayWord.service.TodayWordVO;
import org.tok.cust.admin.todayWord.service.TodayWordPK;


/**  
 * @Class Name : TodayWordService.java
 * @Description : 오늘의 말씀 Service Interface
 * @
 * @author snoopy
 * @since 2012.11.16 16:47:628
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public interface TodayWordService
{
	
	/**
	 * Retrieve todayWord list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException;

	/**
	 * Acquire detail todayWord information
	 * @param PK - primary key TodayWordPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return TodayWordVO
	 * @exception BaseException
	 */
	public TodayWordVO detail(TodayWordPK todayWordPK, boolean lazyRetrieve) throws BaseException;

	/**
	 * Check exist todayWord by key
	 * @param VO - modify data TodayWordVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(TodayWordVO todayWordVO) throws BaseException;
	
	/**
	 * Add new todayWord information
	 * @param VO - new data TodayWordVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(TodayWordVO todayWordVO) throws BaseException;

	/**
	 * Modify todayWord information
	 * @param VO - modify data TodayWordVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(TodayWordVO todayWordVO) throws BaseException;
	
	/**
	 * Remove todayWord information
	 * @param VO - TodayWord Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(TodayWordVO todayWordVO) throws BaseException;

	/**
	 * Remove todayWord information
	 * @param PK - primary key TodayWordPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(TodayWordPK todayWordPK, boolean cascade) throws BaseException;
	
	/**
	 * Remove todayWord information
	 * @param List - primary key TodayWordPK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException;
	
}
