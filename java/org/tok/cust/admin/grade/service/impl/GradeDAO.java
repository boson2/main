
package org.tok.cust.admin.grade.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.grade.service.GradeVO;
import org.tok.cust.admin.grade.service.GradePK;



/**  
 * @Class Name : GradeDAO.java
 * @Description : 등급관리 Data Access Object
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradeDAO extends SqlMapClientDaoSupport
{
	/**
	 * Acquire grade total row size
	 * @param paramMap - Search Condition Map
	 * @return total row size
	 * @exception DataAccessException
	 */
	public int getTotalCount(Map paramMap) throws DataAccessException 
	{
		return (Integer)getSqlMapClientTemplate().queryForObject("grade.totalCount", paramMap);
	}
	
	/**
	 * Retrieve all grade list
	 * @param void
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findAll() throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("grade.findAll");
	}
	
	/**
	 * Retrieve grade list (pageUnit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findByPage(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("grade.findByPage", paramMap);
	}

	/**
	 * Acquire detail grade information
	 * @param PK - primary key GradePK
	 * @return GradeVO
	 * @exception DataAccessException
	 */
	public GradeVO detail(GradePK gradePK) throws DataAccessException 
	{
		return (GradeVO)getSqlMapClientTemplate().queryForObject("grade.detail", gradePK);
	}
	
	/**
	 * Check exist grade by key
	 * @param VO - modify data GradeVO
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(GradeVO gradeVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("grade.exist", gradeVO);
		return (cnt != 0) ? true : false;
	}
	
	/**
	 * Add new grade information
	 * @param VO - new data GradeVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void insert(GradeVO gradeVO) throws DataAccessException 
	{
        getSqlMapClientTemplate().insert("grade.insert", gradeVO);
	}

	/**
	 * Modify grade information
	 * @param VO - modify data GradeVO
	 * @return void
	 * @exception DataAccessException
	 */
	public void update(GradeVO gradeVO) throws DataAccessException 
	{
		getSqlMapClientTemplate().update("grade.update", gradeVO);
	}
	
	/**
	 * Remove grade information
	 * @param PK - primary key GradePK
	 * @return int
	 * @exception DataAccessException
	 */
	public int delete(GradePK gradePK) throws DataAccessException
	{
		return getSqlMapClientTemplate().delete("grade.delete", gradePK);
	}
	
	/**
	 * Remove grade information
	 * @param List - primary key GradePK
	 * @return int
	 * @exception DataAccessException, SQLException
	 */
	public int delete(List removeKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=removeKeys.iterator(); it.hasNext(); ) {
			GradePK gradePK = (GradePK)it.next();
			getSqlMapClientTemplate().delete("grade.delete", gradePK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	/**
	 * Modify grade order information (all sibling)
	 * @param paramList - modify order sibling map List (gradeCode, sortOrder)
	 * @return void
	 * @exception DataAccessException
	 */
	public void changeOrder(List paramList) throws DataAccessException, SQLException 
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		Iterator it = paramList.iterator();
		for(int i=0; i<paramList.size(); i++) {
			GradeVO gradeVO = (GradeVO)it.next();
			//System.out.println("####### i=" + i + ", dymicMenuVO=" + dymicMenuVO);
			gradeVO.setSortOrder( String.valueOf(i+1) );
			getSqlMapClientTemplate().update("grade.changeOrder", gradeVO);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
	
	public boolean existPrincipalParent(GradeVO gradeVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("securityPrincipal.existParent", gradeVO);
		return (cnt != 0) ? true : false;
	}
	
	public boolean existPrincipal(GradeVO gradeVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("securityPrincipal.exist", gradeVO);
		return (cnt != 0) ? true : false;
	}
	
	public void insertPrincipalParent(GradeVO gradeVO) throws DataAccessException 
	{
        getSqlMapClientTemplate().insert("securityPrincipal.insertParent", gradeVO);
	}
	
	public void insertPrincipal(GradeVO gradeVO) throws DataAccessException 
	{
        getSqlMapClientTemplate().insert("securityPrincipal.insert", gradeVO);
	}
	
	public void updatePrincipal(GradeVO gradeVO) throws DataAccessException 
	{
		getSqlMapClientTemplate().update("securityPrincipal.update", gradeVO);
	}
	
	public int deletePrincipal(List removeKeys) throws DataAccessException, SQLException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=removeKeys.iterator(); it.hasNext(); ) {
			GradePK gradePK = (GradePK)it.next();
			
			getSqlMapClientTemplate().delete("securityPrincipal.deleteSecurityPermission", gradePK);
			getSqlMapClientTemplate().delete("securityPrincipal.deleteUserRole", gradePK);
			getSqlMapClientTemplate().delete("securityPrincipal.delete", gradePK);
			getSqlMapClientTemplate().update("securityPrincipal.updateUserpass", gradePK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
}
