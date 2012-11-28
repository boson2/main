package org.tok.cust.user.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.view.components.dao.ConnectionContext;


public interface SiteUserManagerDAO
{
	public void authenticate(ConnectionContext connCtx, String userId, String passwd) throws UserException;

	public Map getUserInfoMap(ConnectionContext connCtx, String userId) throws UserException;
	
	public void changePassword(ConnectionContext connCtx, String userId, String password, String passwordNew) throws UserException, Exception;
	
	public void log(ConnectionContext connCtx, Map userInfoMap, int status) throws Exception;
	
	public void insert(ConnectionContext connCtx, UserVo user, String encPasswd) throws Exception;

	public boolean isOverlap(ConnectionContext connCtx, String userId) throws Exception;

	public List getUserList(ConnectionContext connCtx, String name,
			String regNo) throws UserException;
	
	public void update(ConnectionContext connCtx, UserVo user) throws Exception;
	
	public String authenticate(ConnectionContext connCtx, String userId, String userName, String regNo) throws UserException;
	
	public Map reissuePassword(ConnectionContext connCtx, String userId, String temp_pw, String userName, String regNo) throws UserException, Exception;

	public Collection getZipCodes(ConnectionContext connCtx, String dong, String langKnd);

	public Collection getJoinGroups(ConnectionContext connCtx, String user_id);
	
	public void addUserInfo(ConnectionContext connCtx, Map userInfoMap) throws Exception;
	
	public void updateUserInfo(ConnectionContext connCtx, Map userInfoMap) throws Exception;
	
	public void updateUserExtraInfo(ConnectionContext connCtx, Map paramMap) throws Exception;
}
