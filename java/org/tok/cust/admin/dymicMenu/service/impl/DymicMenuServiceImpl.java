
package org.tok.cust.admin.dymicMenu.service.impl;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.sql.Timestamp;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.dao.DataAccessException;
import org.tok.view.Talk;
import org.tok.view.admin.page.service.PagePK;
import org.tok.view.admin.page.service.PageVO;
import org.tok.view.cache.CacheMngr;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.codebase.Codebase;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;
import org.tok.view.components.dao.DAOFactory;
import org.tok.view.exception.BaseException;
import org.tok.view.session.SessionManager;
import org.tok.view.sso.TalkSSOManager;
import org.tok.view.util.ResultSetList;
import org.tok.view.idgenerator.IdGenerator;

import org.tok.board.dao.AdminDAO;
import org.tok.board.form.AdminAuxilForm;
import org.tok.board.security.SecurityMngr;
import org.tok.board.util.Constants;
import org.tok.board.util.ValidateUtil;
import org.tok.board.vo.ParamMap;
import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.dymicMenu.service.DymicMenuPK;
import org.tok.cust.admin.dymicMenu.service.DymicMenuService;
import org.tok.cust.admin.dymicMenu.service.impl.DymicMenuDAO;

import com.sun.org.omg.SendingContext.CodeBase;

/**  
 * @Class Name : DymicMenuServiceImpl.java
 * @Description : 동적메뉴관리 Service Implementation
 * @
 * @author snoopy
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by kiki All right reserved.
 */
public class DymicMenuServiceImpl implements DymicMenuService
{
	private final Log log = LogFactory.getLog(getClass());
	private IdGenerator idGenerator = null;
	private TalkCodeManager codeManager;
	
	public DymicMenuServiceImpl()
	{
		this.idGenerator = (IdGenerator)Talk.getComponentManager().getComponent("IdGenerator");
		this.codeManager = TalkCodeManager.getInstance();
	}
	
	private DymicMenuDAO dymicMenuDAO;
	public void setDymicMenuDAO(DymicMenuDAO dymicMenuDAO) {
		this.dymicMenuDAO = dymicMenuDAO;
	}
	
	
	/**
	 * Retrieve dymicMenu list (page unit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map Collection
	 * @exception BaseException
	 */
	public Collection findByPage(Map paramMap) throws BaseException 
	{
		ResultSetList resultList = new ResultSetList();
    	try {
			int totalCount = dymicMenuDAO.getTotalCount(paramMap);
    		List keys = dymicMenuDAO.findByPage(paramMap);
			for(Iterator it=keys.iterator(); it.hasNext(); ) {
				DymicMenuVO dymicMenuVO = (DymicMenuVO)dymicMenuDAO.detail((DymicMenuPK)it.next());
				dymicMenuVO.setApplyFeed( codeManager.getCodeName("PT", "119", dymicMenuVO.getApplyFeed(), "ko") );
				dymicMenuVO.setReadAuth( codeManager.getCodeName("PT", "118", dymicMenuVO.getReadAuth(), "ko") );
				dymicMenuVO.setWriteAuth( codeManager.getCodeName("PT", "118", dymicMenuVO.getWriteAuth(), "ko") );
				dymicMenuVO.setServiceStatus( (dymicMenuVO.getIsService() == 0) ? "off" : "on" );
				
				resultList.add( dymicMenuVO );
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
	 * Acquire detail dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @param lazyRetrieve - retrieve reference data  boolean
	 * @return DymicMenuVO
	 * @exception BaseException
	 */
	public DymicMenuVO detail(DymicMenuPK dymicMenuPK, boolean lazyRetrieve) throws BaseException 
	{
		DymicMenuVO dymicMenuVO = null;
    	try {
    		dymicMenuVO = dymicMenuDAO.detail(dymicMenuPK);
			
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
		return dymicMenuVO;
	}
	
	/**
	 * Check exist dymicMenu by key
	 * @param VO - modify data DymicMenuVO
	 * @return boolean
	 * @exception BaseException
	 */
	public boolean exist(DymicMenuVO dymicMenuVO) throws BaseException
	{
		return dymicMenuDAO.exist(dymicMenuVO);
	}
	
	/**
	 * Add new dymicMenu information
	 * @param VO - new data DymicMenuVO
	 * @return void
	 * @exception BaseException
	 */
	public void insert(DymicMenuVO dymicMenuVO) throws BaseException
	{
    	try {
			System.out.println("### insert DYMIC_MENU");
			// get parent data
			dymicMenuVO.setMenuId( this.idGenerator.getNextIntegerId("DYMIC_MENU") );
			// before insert
			dymicMenuDAO.insert(dymicMenuVO);
			
			// insert reference object
			System.out.println("### insert DYMIC_MENU finished");
			
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Modify dymicMenu information
	 * @param VO - modify data DymicMenuVO
	 * @return void
	 * @exception BaseException
	 */
	public void update(DymicMenuVO dymicMenuVO) throws BaseException
	{
    	try {
			dymicMenuDAO.update(dymicMenuVO);
			
			// update reference object
			Map cmdMap = new HashMap();
			cmdMap.put("cmd", String.valueOf (org.tok.board.cache.CacheMngr.CMD_UPD_BOARD));
			cmdMap.put("boardId", String.valueOf (dymicMenuVO.getMenuId()));
			org.tok.board.cache.CacheMngr cacheMngr = (org.tok.board.cache.CacheMngr)Talk.getComponentManager().getComponent ("org.tok.board.cache.CacheMngr");
			cacheMngr.reset (cmdMap); // 게시판에 영향을 미쳤으므로, 캐시를 갱신한다.

    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove dymicMenu information
	 * @param VO - DymicMenu Value Object
	 * @return void
	 * @exception BaseException
	 */
	public void delete(DymicMenuVO dymicMenuVO) throws BaseException
	{
		try {
			// remove reference object
			dymicMenuDAO.delete( dymicMenuVO.getPrimaryKey() );

			Map cmdMap = new HashMap();
			cmdMap.put("cmd", String.valueOf (org.tok.board.cache.CacheMngr.CMD_UPD_BOARD));
			cmdMap.put("boardId", String.valueOf (dymicMenuVO.getMenuId()));
			org.tok.board.cache.CacheMngr cacheMngr = (org.tok.board.cache.CacheMngr)Talk.getComponentManager().getComponent ("org.tok.board.cache.CacheMngr");
			cacheMngr.reset (cmdMap); // 게시판에 영향을 미쳤으므로, 캐시를 갱신한다.
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(DymicMenuPK dymicMenuPK, boolean cascade) throws BaseException
	{
		try {
			dymicMenuDAO.delete(dymicMenuPK);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Remove dymicMenu information
	 * @param List - primary key DymicMenuPK
	 * @param boolean - cascading delete children
	 * @return void
	 * @exception BaseException
	 */
	public void delete(List removeKeys, boolean cascade) throws BaseException
	{
    	try {
			dymicMenuDAO.delete(removeKeys);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	/**
	 * Modify page order information
	 * @param paramMap - primary key (id) / order direction (toDown)
	 * @return void
	 * @exception BaseException
	 */
	public void changeTreeOrder(Map paramMap) throws BaseException
	{
    	try {
			String menu_id = (String)paramMap.get("id");
			String toDown = (String)paramMap.get("toDown");
			List newList = new ArrayList();
			int currentIdx = 0;
			DymicMenuVO currentVO = null;
			
			List childList = dymicMenuDAO.findAll();
			
			Iterator it = childList.iterator();
			for(int i=0; i<childList.size(); i++) {
				DymicMenuVO dymicMenuVO = (DymicMenuVO)it.next();
				String key = dymicMenuVO.getMenuId().toString();
				//System.out.println("###### i=" + i + ", key=" + key + ", menu_id=" + menu_id);
				if( key.equals(menu_id) == true ) {
					currentIdx = i;
					currentVO = dymicMenuVO ;
				}
				else {
					newList.add( dymicMenuVO  );
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
			
			dymicMenuDAO.changeTreeOrder(newList);
    	}
		catch(Exception e) 
		{
			throw new BaseException(e);
        }
	}
	
	public List getMainMenuList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException {
    	try {
			return dymicMenuDAO.getMainMenuList (request, connCtxt);
    	} catch (Exception e) {
			throw new BaseException(e);
        }
	}
	public List getMainCafeList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException {
    	try {
			return dymicMenuDAO.getMainCafeList (request, connCtxt);
    	} catch (Exception e) {
			throw new BaseException(e);
        }
	}

}
