package org.tok.cust.user.control;

import java.util.List;
import java.util.Map;

import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.view.security.CommonUserManager;


public interface SiteUserManager extends CommonUserManager {

	public void login(Map userInfoMap, String userId, String passwd) throws UserException, Exception;
	
	public Map getUserInfoMap(String userId) throws UserException, Exception;
	
	public void log(Map userInfoMap) throws UserException, Exception;
	
	public void log(Map userInfoMap, int status) throws UserException, Exception;
	
	public void changePassword(Map userInfoMap, String userId, String password, String passwordNew) throws UserException, Exception;
	
	public boolean isOverlap(String userId) throws Exception;

	public List getUserList(String name, String regNo) throws Exception;
	
	public void authenticate(String userId, String passwd) throws UserException, Exception;
	
	public void join(UserVo user) throws UserException, Exception;
	
	public void changeUserInfo(Map userInfoMap) throws UserException, Exception;

	public String reissuePassword(Map userInfoMap) throws UserException;

	public void setUserByUserInfoMap(UserVo user) throws UserException, Exception;
	
	public Map getUserInfoMapFromUserVo(UserVo user) throws Exception;

	public List getZipCodes(String dong, String langKnd);

	public boolean isJoined(UserVo user) throws Exception;
}
