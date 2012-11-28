
package org.tok.cust.admin.grade.service;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import org.tok.view.exception.BaseException;

import org.tok.cust.admin.grade.service.GradeVO;
import org.tok.cust.admin.grade.service.GradePK;


/**  
 * @Class Name : GradeService.java
 * @Description : 등급관리 Service Interface
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public interface GradeService
{
	
	/**
	 * Retrieve grade list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException;

	/**
	 * Acquire detail grade information
	 * @param PK - primary key GradePK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return GradeVO
	 * @exception BaseException
	 */
	public GradeVO detail(GradePK gradePK, boolean lazyRetrieve) throws BaseException;

	/**
	 * Check exist grade by key
	 * @param VO - modify data GradeVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(GradeVO gradeVO) throws BaseException;
	
	/**
	 * Add new grade information
	 * @param VO - new data GradeVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(GradeVO gradeVO) throws BaseException;

	/**
	 * Modify grade information
	 * @param VO - modify data GradeVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(GradeVO gradeVO) throws BaseException;
	
	/**
	 * Remove grade information
	 * @param List - primary key GradePK
	 * @param boolean - cascading delete children
	 * @return int
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException;
	
	/**
	 * Modify grade order information
	 * @param paramMap - primary key (id) / order direction (toDown)
	 * @return void
	 * @exception BaseException
	 */
	public void changeOrder(Map paramMap) throws BaseException;
	
}
