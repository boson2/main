
package org.tok.cust.admin.dymicMenu.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import org.tok.board.dao.AdminDAO;
import org.tok.board.form.AdminAuxilForm;
import org.tok.board.form.AdminBoardForm;
import org.tok.board.security.SecurityMngr;
import org.tok.board.util.Constants;
import org.tok.board.util.ValidateUtil;
import org.tok.board.vo.ParamMap;
import org.tok.cust.admin.dymicMenu.service.DymicMenuVO;
import org.tok.cust.admin.dymicMenu.service.DymicMenuPK;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.codebase.Codebase;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;
import org.tok.view.components.dao.DAOFactory;
import org.tok.view.exception.BaseException;
import org.tok.view.sso.TalkSSOManager;



/**  
 * @Class Name : DymicMenuDAO.java
 * @Description : 동적메뉴관리 Data Access Object
 * @
 * @author kevin
 * @since 2012.11.16 16:23:338
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Saltware All right reserved.
 */
public class DymicMenuDAO extends SqlMapClientDaoSupport
{
	/**
	 * Acquire dymicMenu total row size
	 * @param paramMap - Search Condition Map
	 * @return total row size
	 * @exception DataAccessException
	 */
	public int getTotalCount(Map paramMap) throws DataAccessException 
	{
		return (Integer)getSqlMapClientTemplate().queryForObject("dymicMenu.totalCount", paramMap);
	}
	
	/**
	 * Retrieve all dymicMenu list
	 * @param void
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findAll() throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("dymicMenu.findAll");
	}
	
	/**
	 * Retrieve dymicMenu list (pageUnit) by condition
	 * @param paramMap - Search Condition Map
	 * @return Data Map List
	 * @exception DataAccessException
	 */
	public List findByPage(Map paramMap) throws DataAccessException 
	{
		return getSqlMapClientTemplate().queryForList("dymicMenu.findByPage", paramMap);
	}

	/**
	 * Acquire detail dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @return DymicMenuVO
	 * @exception DataAccessException
	 */
	public DymicMenuVO detail(DymicMenuPK dymicMenuPK) throws DataAccessException 
	{
		return (DymicMenuVO)getSqlMapClientTemplate().queryForObject("dymicMenu.detail", dymicMenuPK);
	}
	
	/**
	 * Check exist dymicMenu by key
	 * @param VO - modify data DymicMenuVO
	 * @return boolean
	 * @exception DataAccessException
	 */
	public boolean exist(DymicMenuVO dymicMenuVO) throws DataAccessException
	{
		Integer cnt = (Integer)getSqlMapClientTemplate().queryForObject("dymicMenu.exist", dymicMenuVO);
		return (cnt != 0) ? true : false;
	}
	
	/*********************************************************************************************************
	 * Add new dymicMenu information
	 * @param VO - new data DymicMenuVO
	 * @return void
	 * @exception DataAccessException
	 * 
	 * 게시판 생성.2012.11.22.KWShin.
	 *  - 메뉴가 추가되면 MENU_ID 를 BOARD_ID 로 하여 게시판을 하나 만든다.
	 *  - BOARD.BOARD='BOARD.BASE.MBL.BASE' 인 데이터를 복사하여 게시판 속성들을 설정한다.
	 *  - READ_AUTH/WRITE_AUTH 에 따라 해당 ROLE 에 게시판 권한을 부여한다.
	 *  - DYMIC_MENU.IS_SERVICE 와 BOARD.BOARD_ACTIVE 를 연동한다.
	 *  - CATE_BOARD.CATE_ID 는 무조건 '10'(모바일 기본 카테고리) 으로 한다. 
	 *********************************************************************************************************/
	public void insert(DymicMenuVO dymicMenuVO) throws DataAccessException, org.tok.board.exception.BaseException 
	{
		PreparedStatement pstmt = null;
		ResultSet         rslt  = null;

		try {
	        /*------------------------------------------------------------------------------------------------
	         * 게시판 생성 준비.
	         *-----------------------------------------------------------------------------------------------*/
			ConnectionContext  connCtxt = new ConnectionContextForRdbms (getDataSource().getConnection(), true);
	        AdminDAO           adminDAO = (AdminDAO)DAOFactory.getInst().getDAO(AdminDAO.DAO_NAME_PREFIX);
	        HttpServletRequest request  = dymicMenuVO.getRequest();
	        Map                uiMap    = TalkSSOManager.getUserInfo (request);
	        String             langKnd  = (String)uiMap.get("lang_knd");
			CodeBundle         cdBun    = TalkCodeManager.getInstance().getBundle (langKnd);
	        /*------------------------------------------------------------------------------------------------
	         * 게시판 생성.
	         *-----------------------------------------------------------------------------------------------*/
	        String upload = Talk.getConfiguration().getString(Constants.PROP_UPLOAD_PATH);
			if (ValidateUtil.isEmpty(upload)) upload = request.getSession().getServletContext().getRealPath("/board/upload");
	
			ParamMap paramMap = new ParamMap();
			paramMap.putString ("cateId",      "10");
			paramMap.putString ("langKnd",     langKnd);
			paramMap.putString ("boardId",     String.valueOf (dymicMenuVO.getMenuId()));
			paramMap.putString ("boardActive", "1".equals (String.valueOf (dymicMenuVO.getIsService())) ? "Y" : "N");
			paramMap.putString ("boardSys",    "PT");
			paramMap.putString ("updUserId",   (String)uiMap.get("user_id"));
			paramMap.putString ("boardNm",     dymicMenuVO.getMenuName());
			paramMap.putString ("boardTtl",    dymicMenuVO.getMenuName());
			paramMap.putString ("uploadPath",  upload);
			paramMap.putString ("gradeFix",    "/PT/");
			paramMap.putString ("refBoardId",  Constants.DEFAULT_KEY_BOARD + ".MBL.BASE");
			
			adminDAO.boardCreate (paramMap, connCtxt);
	        /*------------------------------------------------------------------------------------------------
	         * 게시판 권한 설정.
	         *-----------------------------------------------------------------------------------------------*/
			pstmt = connCtxt.getConnection().prepareStatement("SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=?");
			long prinId = 0;
			
			List grades = (ArrayList)cdBun.getCodes ("PT", "118", TalkCodeManager.DESCENDING_SORT, TalkCodeManager.CODETAG_SORTITEM, false);
			String readPmsn  = "1,2,4,8,16";
			String writePmsn = "1,2,4,8,16,32,64,128,256";
			
			AdminAuxilForm aaForm = new AdminAuxilForm();
			aaForm.setLangKnd   (langKnd);
			aaForm.setBoardId   (String.valueOf (dymicMenuVO.getMenuId()));
			aaForm.setAct       ("roleAuth");

			aaForm.setRguIds ("");
			for (int i=0; i<grades.size(); i++) {
				Codebase grd = (Codebase)grades.get(i);
				
				prinId = 0;
				pstmt.setString (1, grd.getCode());
				rslt = pstmt.executeQuery();
				if (rslt.next()) prinId = rslt.getLong (1);
				rslt.close();
				
				aaForm.setRguIds (aaForm.getRguIds() + "," + String.valueOf (prinId));

				System.out.println("############# read code=["+grd.getCode()+"],prinId=["+prinId+"],rguIds=["+aaForm.getRguIds()+"]");
				
				if (grd.getCode().equals (dymicMenuVO.getReadAuth())) break;
			}
			if (! "".equals(aaForm.getRguIds())) aaForm.setRguIds (aaForm.getRguIds().substring(1)); // 맨 앞자리 ',' 제거.
			aaForm.setAuthGrcdR (readPmsn);
			adminDAO.setBoardAuth (aaForm, connCtxt); // 선택된 등급과 그보다 높은 등급에 읽기권한을 부여한다.
			
			aaForm.setRguIds ("");
			for (int i=0; i<grades.size(); i++) {
				Codebase grd = (Codebase)grades.get(i);
				
				prinId = 0;
				pstmt.setString (1, grd.getCode());
				rslt = pstmt.executeQuery();
				if (rslt.next()) prinId = rslt.getLong (1);
				rslt.close();
				
				aaForm.setRguIds (aaForm.getRguIds() + "," + String.valueOf (prinId));
				
				System.out.println("############# write code=["+grd.getCode()+"],prinId=["+prinId+"],rguIds=["+aaForm.getRguIds()+"]");

				if (grd.getCode().equals (dymicMenuVO.getWriteAuth())) break;
			}
			if (! "".equals(aaForm.getRguIds())) aaForm.setRguIds (aaForm.getRguIds().substring(1)); // 맨 앞자리 ',' 제거.
			aaForm.setAuthGrcdR (writePmsn);
			adminDAO.setBoardAuth (aaForm, connCtxt); // 선택된 등급과 그보다 높은 등급에 쓰기권한을 부여한다.
			
			pstmt.close();
	        /*-----------------------------------------------------------------------------------------------*/
			
		} catch (org.tok.board.exception.BaseException be) {
			be.printStackTrace();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			throw new org.tok.board.exception.BaseException (e) ;
		} finally {
			if (rslt != null) try { rslt.close(); } catch (Exception e) {}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
		}
		
		dymicMenuVO.setUrl ("/board/list.bbs?boardId="+dymicMenuVO.getMenuId()); // 게시판 호출 URL을 할당해준다.
        getSqlMapClientTemplate().insert("dymicMenu.insert", dymicMenuVO);
        System.out.println("### insert DYMIC_MENU from dao");
	}

	/*********************************************************************************************************
	 * Modify dymicMenu information
	 * @param VO - modify data DymicMenuVO
	 * @return void
	 * @exception DataAccessException
	 * 
	 * 게시판 설정 변경.2012.11.22.KWShin.
	 *  - DYMIC_MENU.IS_SERVICE 가 변하면 BOARD.BOARD_ACTIVE 를 바꾸어준다.
	 *  - DYMIC_MENU.MENU_NAME 이 변하면 BOARD_LANG.BOARD_NM/BOARD_TTL 을 바꾸어 준다.
	 *  - DYMIC_MENU.READ_AUTH/WRITE_AUTH 가 변하면 권한설정을 바꾸어 준다.
	 *   -> 읽기권한에 설정된 등급과 그보다 높은 등급에 "파일다운로드"까지의 권한을 일괄 부여한다.
	 *   -> 쓰기권한에 설정된 등급과 그보다 높은 등급에 "댓답글쓰기"까지의 권한을 일괄 부여한다.
	 *   -> 읽기권한에 설정된 등급보다 낮은 등급의 권한을 일괄 제거한다.
	 *********************************************************************************************************/
	public void update(DymicMenuVO dymicMenuVO) throws DataAccessException, org.tok.board.exception.BaseException
	{
		
		DymicMenuPK dmPK = new DymicMenuPK();
		dmPK.setMenuId (dymicMenuVO.getMenuId());
		
		DymicMenuVO curDmVO = detail (dmPK);

		getSqlMapClientTemplate().update("dymicMenu.update", dymicMenuVO);
		
		PreparedStatement pstmt = null;
		ResultSet         rslt  = null;

		try {
	        /*------------------------------------------------------------------------------------------------
	         * 게시판 수정 준비.
	         *-----------------------------------------------------------------------------------------------*/
			ConnectionContext  connCtxt = new ConnectionContextForRdbms (getDataSource().getConnection(), true);
	        AdminDAO           adminDAO = (AdminDAO)DAOFactory.getInst().getDAO(AdminDAO.DAO_NAME_PREFIX);
	        HttpServletRequest request  = dymicMenuVO.getRequest();
	        Map                uiMap    = TalkSSOManager.getUserInfo (request);
	        String             langKnd  = (String)uiMap.get("lang_knd");
			CodeBundle         cdBun    = TalkCodeManager.getInstance().getBundle (langKnd);
			/*------------------------------------------------------------------------------------------------
	         * 게시판 수정.
	         *-----------------------------------------------------------------------------------------------*/
			if (curDmVO.getIsService().intValue() != dymicMenuVO.getIsService().intValue()) { // 서비스상태가 변하면...
				
				AdminBoardForm abForm = new AdminBoardForm();
				abForm.setBoardId (String.valueOf (dymicMenuVO.getMenuId()));
				abForm.setAct (dymicMenuVO.getIsService().intValue() == 1 ? "active" : "inactive");
				
				adminDAO.setBoardBase (abForm, connCtxt);
			}
			if (! curDmVO.getMenuName().equals(dymicMenuVO.getMenuName())) { // 메뉴명이 변하면...
				
				AdminBoardForm abForm = new AdminBoardForm();
				abForm.setLangKnd (langKnd);
				abForm.setAct     ("upd");
				abForm.setBoardId (String.valueOf (dymicMenuVO.getMenuId()));
				abForm.setBoardNm (dymicMenuVO.getMenuName());
				abForm.setBoardTtl(dymicMenuVO.getMenuName());
				
				adminDAO.setBoardLang (abForm, connCtxt);
			}
	        /*------------------------------------------------------------------------------------------------
	         * 게시판 권한 설정.
	         *-----------------------------------------------------------------------------------------------*/
			if (!curDmVO.getReadAuth().equals(dymicMenuVO.getReadAuth()) || !curDmVO.getWriteAuth().equals(dymicMenuVO.getWriteAuth())) { // 읽기등급 또는 쓰기등급 중 둘 중 하나라도 변하면...
			
				pstmt = connCtxt.getConnection().prepareStatement("SELECT principal_id FROM security_principal WHERE principal_type='R' AND short_path=?");
				long prinId = 0;

				List grdDesc = (ArrayList)cdBun.getCodes ("PT", "118", TalkCodeManager.DESCENDING_SORT, TalkCodeManager.CODETAG_SORTITEM, false);
				List grdAsc  = (ArrayList)cdBun.getCodes ("PT", "118", TalkCodeManager.ASCENDING_SORT, TalkCodeManager.CODETAG_SORTITEM, false);
				String readPmsn  = "1,2,4,8,16";
				String writePmsn = "1,2,4,8,16,32,64,128,256";
				
				AdminAuxilForm aaForm = new AdminAuxilForm();
				aaForm.setLangKnd   (langKnd);
				aaForm.setBoardId   (String.valueOf (dymicMenuVO.getMenuId()));
				aaForm.setAct       ("roleAuth");

				aaForm.setRguIds ("");
				for (int i=0; i<grdDesc.size(); i++) {
					Codebase grd = (Codebase)grdDesc.get(i);
					
					prinId = 0;
					pstmt.setString (1, grd.getCode());
					rslt = pstmt.executeQuery();
					if (rslt.next()) prinId = rslt.getLong (1);
					rslt.close();
					
					aaForm.setRguIds (aaForm.getRguIds() + "," + String.valueOf (prinId));
					if (grd.getCode().equals (dymicMenuVO.getReadAuth())) break;
				}
				if (! "".equals(aaForm.getRguIds())) aaForm.setRguIds (aaForm.getRguIds().substring(1)); // 맨 앞자리 ',' 제거.
				aaForm.setAuthGrcdR (readPmsn);
				adminDAO.setBoardAuth (aaForm, connCtxt); // 선택된 읽기등급과 그보다 높은 등급에 읽기권한을 부여한다.

				aaForm.setRguIds ("");
				for (int i=0; i<grdDesc.size(); i++) {
					Codebase grd = (Codebase)grdDesc.get(i);
					
					prinId = 0;
					pstmt.setString (1, grd.getCode());
					rslt = pstmt.executeQuery();
					if (rslt.next()) prinId = rslt.getLong (1);
					rslt.close();
					
					aaForm.setRguIds (aaForm.getRguIds() + "," + String.valueOf (prinId));
					if (grd.getCode().equals (dymicMenuVO.getWriteAuth())) break;
				}
				if (! "".equals(aaForm.getRguIds())) aaForm.setRguIds (aaForm.getRguIds().substring(1)); // 맨 앞자리 ',' 제거.
				aaForm.setAuthGrcdR (writePmsn);
				adminDAO.setBoardAuth (aaForm, connCtxt); // 선택된 쓰기등급과 그보다 높은 등급에 쓰기권한을 부여한다.

				aaForm.setRguIds ("");
				for (int i=0; i<grdAsc.size(); i++) {
					Codebase grd = (Codebase)grdAsc.get(i);
					if (grd.getCode().equals (dymicMenuVO.getReadAuth())) break;
					
					prinId = 0;
					pstmt.setString (1, grd.getCode());
					rslt = pstmt.executeQuery();
					if (rslt.next()) prinId = rslt.getLong (1);
					rslt.close();
					
					aaForm.setRguIds (aaForm.getRguIds() + "," + String.valueOf (prinId));
				}
				if (! "".equals(aaForm.getRguIds())) aaForm.setRguIds (aaForm.getRguIds().substring(1)); // 맨 앞자리 ',' 제거.
				aaForm.setAuthGrcdR ("0");
				adminDAO.setBoardAuth (aaForm, connCtxt); // 선택된 읽기등급보다 낮은 등급의 권한을 제거한다.
				
				pstmt.close();
			}
	        /*-----------------------------------------------------------------------------------------------*/
			
		} catch (org.tok.board.exception.BaseException be) {
			be.printStackTrace();
			throw be;
		} catch (Exception e) {
			e.printStackTrace();
			throw new org.tok.board.exception.BaseException (e) ;
		} finally {
			if (rslt != null) try { rslt.close(); } catch (Exception e) {}
			if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
		}
	}
	
	/**
	 * Remove dymicMenu information
	 * @param PK - primary key DymicMenuPK
	 * @return int
	 * @exception DataAccessException
	 */
	public int delete(DymicMenuPK dymicMenuPK) throws DataAccessException
	{
		return getSqlMapClientTemplate().delete("dymicMenu.delete", dymicMenuPK);
	}
	
	/*********************************************************************************************************
	 * Remove dymicMenu information
	 * @param List - primary key DymicMenuPK
	 * @return int
	 * @exception DataAccessException, SQLException
	 * 게시판 삭제.2012.11.22.KWShin.
	 * - 
	 *********************************************************************************************************/
	public int delete(List removeKeys) throws DataAccessException, SQLException, org.tok.board.exception.BaseException
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		for(Iterator it=removeKeys.iterator(); it.hasNext(); ) {
			DymicMenuPK dymicMenuPK = (DymicMenuPK)it.next();
			
			try {
				/*--------------------------------------------------------------------------------------------
				 * 게시판 삭제 준비
				 *-------------------------------------------------------------------------------------------*/
				ConnectionContext  connCtxt = new ConnectionContextForRdbms (getDataSource().getConnection(), true);
		        AdminDAO           adminDAO = (AdminDAO)DAOFactory.getInst().getDAO(AdminDAO.DAO_NAME_PREFIX);
		        HttpServletRequest request  = dymicMenuPK.getRequest();
		        Map                uiMap    = TalkSSOManager.getUserInfo (request);
				/*--------------------------------------------------------------------------------------------
				 * 게시판 삭제.
				 *-------------------------------------------------------------------------------------------*/
		        String upload = Talk.getConfiguration().getString(Constants.PROP_UPLOAD_PATH);
				if (ValidateUtil.isEmpty(upload)) upload = request.getSession().getServletContext().getRealPath("/board/upload");
	
				ParamMap paramMap = new ParamMap();
				paramMap.putString ("updUserId",   (String)uiMap.get("user_id"));
				paramMap.putString ("boardId",     String.valueOf(dymicMenuPK.getMenuId()));
				paramMap.putString ("uploadPath",  upload);
				paramMap.putString ("boardSys",    "BABO"); // "boardSys" 에 "PT"를 넘겨주면 비밀번호와 관리자 여부를 체크한다. 그래서 걍 엉뚱한 값을 넘긴다...
				
				adminDAO.boardDelete (paramMap, connCtxt);
				/*-------------------------------------------------------------------------------------------*/
			} catch (org.tok.board.exception.BaseException be) {
				be.printStackTrace();
				throw be;
			} catch (Exception e) {
				e.printStackTrace();
				throw new org.tok.board.exception.BaseException (e) ;
			}

			getSqlMapClientTemplate().delete("dymicMenu.delete", dymicMenuPK);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
		
		return 0;
	}
	
	/**
	 * Modify dymicMenu order information (all sibling)
	 * @param paramList - modify order sibling map List (menuId, sortOrder)
	 * @return void
	 * @exception DataAccessException
	 */
	public void changeTreeOrder(List paramList) throws DataAccessException, SQLException 
	{
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		Iterator it = paramList.iterator();
		for(int i=0; i<paramList.size(); i++) {
			DymicMenuVO dymicMenuVO = (DymicMenuVO)it.next();
			//System.out.println("####### i=" + i + ", dymicMenuVO=" + dymicMenuVO);
			dymicMenuVO.setSortOrder(i);
			getSqlMapClientTemplate().update("dymicMenu.changeTreeOrder", dymicMenuVO);
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
	
	/*********************************************************************************************************
	 * 현재 로그인된 사용자가 접근권한을 가진 모든 메뉴 목록을 메뉴 순서대로 구해와서 리턴한다.
	 * 2012.11.23.KWShin.
	 *********************************************************************************************************/
	public List getMainMenuList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException {
		Connection        conn  = null;
		PreparedStatement pstmt = null;
		ResultSet         rslt  = null;
		StringBuffer      sb    = new StringBuffer();
		
		List menuList = new ArrayList();
        
		try {
	        Map    uiMap   = TalkSSOManager.getUserInfo (request);
	        String langKnd = (String)uiMap.get("lang_knd");
	        String userId  = (String)uiMap.get("user_id");
	        org.tok.board.security.SecurityMngr sm = org.tok.board.security.SecurityMngr.getInst();
	        boolean isAdmin = sm.isAdmin(userId) || sm.isMidAdmin(userId) ? true : false;

	        conn = connCtxt.getConnection();
			sb.append(    "SELECT a.menu_id, a.menu_name, a.url FROM dymic_menu a");
			sb.append(    " WHERE is_service=1"); // 모바일화면에서는 서비스 중인 것들만 보여준다.
			if (! isAdmin) {
				sb.append(  " AND a.read_auth");
				sb.append(   " IN (SELECT b.code FROM codebase b");
				sb.append(        " WHERE b.system_code='PT' AND b.code_id='118' AND b.lang_knd='"+langKnd+"' AND b.code_tag1 IS NOT NULL");
				sb.append(          " AND b.code_tag1 <= (SELECT c.code_tag1 FROM codebase c JOIN userpass d ON d.user_id='"+userId+"' AND c.code=d.grade_cd");
				sb.append(                               " WHERE c.system_code='PT' AND c.code_id='118' AND c.lang_knd='"+langKnd+"'");
				sb.append(                              ")");
				sb.append(      " )");
			}
			sb.append(    " ORDER BY a.sort_order");
			pstmt = conn.prepareStatement (sb.toString());
			rslt = pstmt.executeQuery();
			DymicMenuVO dmVO = null;
			while (rslt.next()) {
				dmVO = new DymicMenuVO();
				int idx = 0;
				dmVO.setMenuId   (rslt.getInt   (++idx));
				dmVO.setMenuName (rslt.getString(++idx));
				dmVO.setUrl      (rslt.getString(++idx));
				menuList.add (dmVO);
			}

		} catch( Exception e ) {
			logger.error("DymicMenuDAO.getMainMenuList()",e);
			throw new BaseException("pt.eb.common.sql.problem");
		} finally {
			close (rslt);
			close (pstmt);
		}
		return menuList;
	}

	/*********************************************************************************************************
	 * 현재 로그인된 사용자가 회원이고 상태(CMNT_MMBR.STATE_FALG)가 정상(20)인 모든 커뮤니티의 목록을 리턴한다.
	 * 2012.11.23.KWShin. 
	 *********************************************************************************************************/
	public List getMainCafeList (HttpServletRequest request, ConnectionContext connCtxt) throws BaseException {
		Connection        conn  = null;
		PreparedStatement pstmt = null;
		ResultSet         rslt  = null;
		StringBuffer      sb    = new StringBuffer();

		List cafeList = new ArrayList();
		
		try {
	        Map    uiMap   = TalkSSOManager.getUserInfo (request);
	        String langKnd = (String)uiMap.get("lang_knd");
	        String userId  = (String)uiMap.get("user_id");
	        org.tok.board.security.SecurityMngr sm = org.tok.board.security.SecurityMngr.getInst();
	        boolean isAdmin = sm.isAdmin(userId) || sm.isMidAdmin(userId) ? true : false;

	        conn = connCtxt.getConnection();
			sb.append(    "SELECT a.cmnt_id, a.cmnt_url, a.cate_id, b.cmnt_nm");
			sb.append(     " FROM community a JOIN cmnt_lang b ON b.cmnt_id=a.cmnt_id AND lang_knd='"+langKnd+"'");
			if (! isAdmin) {
				sb.append( " JOIN cmnt_mmbr c ON c.user_id='"+userId+"' AND a.cmnt_id=c.cmnt_id AND state_flag='20'");
			}
			sb.append(    " WHERE cmnt_type='CF' AND cmnt_state='11'");
			sb.append(    " ORDER BY a.cate_id, a.mile_tot DESC");
			pstmt = conn.prepareStatement (sb.toString());
			rslt = pstmt.executeQuery();
			Map cmnt = null;
			while (rslt.next()) {
				cmnt = new HashMap();
				int idx = 0;
				cmnt.put ("cmntId",  rslt.getString(++idx));
				cmnt.put ("cmntUrl", rslt.getString(++idx));
				cmnt.put ("cateId",  String.valueOf(rslt.getInt(++idx)));
				cmnt.put ("cmntNm",  rslt.getString(++idx));
				cafeList.add (cmnt);
			}
		} catch (Exception e) {
			logger.error("DymicMenuDAO.getMainCafeList()",e);
			throw new BaseException("pt.eb.common.sql.problem");
		} finally {
			close (rslt);
			close (pstmt);
		}
		logger.info("END::AdminDAO.getgetCateOfBoard()");
		return cafeList;
	}

	protected void close (PreparedStatement pstmt) {
		if( pstmt != null) {
			try {
				pstmt.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
    }
	protected void close (ResultSet result) {
		if( result != null) {
			try {
				result.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
    }

}
