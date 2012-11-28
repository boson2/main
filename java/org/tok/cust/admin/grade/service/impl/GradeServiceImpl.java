
package org.tok.cust.admin.grade.service.impl;

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
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;
import org.tok.view.exception.BaseException;
import org.tok.view.util.ResultSetList;
import org.tok.view.idgenerator.IdGenerator;

import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.grade.service.GradeVO;
import org.tok.cust.admin.grade.service.GradePK;
import org.tok.cust.admin.grade.service.GradeService;
import org.tok.cust.admin.grade.service.impl.GradeDAO;

/**  
 * @Class Name : GradeServiceImpl.java
 * @Description : 등급관리 Service Implementation
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class GradeServiceImpl implements GradeService
{
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	
	public GradeServiceImpl()
	{
		this.idGenerator = (IdGenerator)Talk.getComponentManager().getComponent("IdGenerator");
	}
	
	private GradeDAO gradeDAO;
	public void setGradeDAO(GradeDAO gradeDAO) {
		this.gradeDAO = gradeDAO;
	}
	
	
	/**
	 * Retrieve grade list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException 
	{
		ResultSetList resultList = new ResultSetList();
    	try {
			int totalCount = gradeDAO.getTotalCount(paramMap);
    		List keys = gradeDAO.findByPage(paramMap);
			for(Iterator it=keys.iterator(); it.hasNext(); ) {
				resultList.add( gradeDAO.detail((GradePK)it.next()) );
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
	 * Acquire detail grade information
	 * @param PK - primary key GradePK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return GradeVO
	 * @exception BaseException
	 */
	public GradeVO detail(GradePK gradePK, boolean lazyRetrieve) throws BaseException 
	{
		GradeVO gradeVO = null;
    	try {
    		gradeVO = gradeDAO.detail(gradePK);
			
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return gradeVO;
	}
	
	/**
	 * Check exist grade by key
	 * @param VO - modify data GradeVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(GradeVO gradeVO) throws BaseException
	{
		return gradeDAO.exist(gradeVO);
	}
	
	/**
	 * Add new grade information
	 * @param VO - new data GradeVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(GradeVO gradeVO) throws BaseException
	{
		ConnectionContext connCtx = null;
    	try {
    		connCtx = new ConnectionContextForRdbms( true );
    		
			gradeDAO.insert(gradeVO);
			if( gradeDAO.existPrincipalParent(gradeVO) == false ) {
				gradeDAO.insertPrincipalParent(gradeVO);
			}
			if( gradeDAO.existPrincipal(gradeVO) == false ) {
				gradeVO.setPrincipalId( this.idGenerator.getNextIntegerId("SECURITY_PRINCIPAL") );
				gradeDAO.insertPrincipal(gradeVO);
			}
			
			connCtx.commit();
    	}
		catch(Exception e) 
		{
			if( connCtx != null ) connCtx.rollback();
			throw new BaseException(e);
        }
		finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
	}
	
	/**
	 * Modify grade information
	 * @param VO - modify data GradeVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(GradeVO gradeVO) throws BaseException
	{
    	try {
			gradeDAO.update(gradeVO);
			if( gradeDAO.existPrincipalParent(gradeVO) == false ) {
				gradeDAO.insertPrincipalParent(gradeVO);
			}
			if( gradeDAO.existPrincipal(gradeVO) == false ) {
				gradeVO.setPrincipalId( this.idGenerator.getNextIntegerId("SECURITY_PRINCIPAL") );
				gradeDAO.insertPrincipal(gradeVO);
			}
			else {
				gradeDAO.updatePrincipal(gradeVO);
			}
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove grade information
	 * @param List - primary key GradePK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException
	{
    	try {
			gradeDAO.delete(removeKeys);
			
			GradeVO gradeVO = new GradeVO();
			if( gradeDAO.existPrincipalParent(gradeVO) == false ) {
				gradeDAO.insertPrincipalParent(gradeVO);
			}
			else {
				gradeDAO.deletePrincipal(removeKeys);
			}
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Modify grade order information
	 * @param paramMap - primary key (id) / order direction (toDown)
	 * @return void
	 * @exception BaseException
	 */
	public void changeOrder(Map paramMap) throws BaseException
	{
    	try {
			String gradeCode = (String)paramMap.get("id");
			String toDown = (String)paramMap.get("toDown");
			List newList = new ArrayList();
			int currentIdx = 0;
			GradeVO currentVO = null;
			
			List childList = gradeDAO.findAll();
			//System.out.println("###### childList=" + childList);
			
			Iterator it = childList.iterator();
			for(int i=0; i<childList.size(); i++) {
				GradeVO gradeVO = (GradeVO)it.next();
				String key = gradeVO.getCode();
				//System.out.println("###### i=" + i + ", key=" + key + ", gradeCode=" + gradeCode);
				if( key.equals(gradeCode) == true ) {
					currentIdx = i;
					currentVO = gradeVO ;
				}
				else {
					newList.add( gradeVO  );
				}
			}
			
			//System.out.println("###### newList=" + newList + ", currentIdx=" + currentIdx + ", childCount=" + childList.size());
			int childCount = childList.size();
			if( "true".equals(toDown) ) {
				if( currentIdx == (childCount-1) ) return;
				newList.add(currentIdx+1, currentVO);
			}
			else {
				if( currentIdx == 0 ) return;
				newList.add(currentIdx-1, currentVO);
			}
			//System.out.println("###### newList=" + newList);
			
			gradeDAO.changeOrder(newList);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
}
