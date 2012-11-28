package org.tok.cust.security;

import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.plug.util.DateUtil;
import org.tok.view.Talk;
import org.tok.view.administration.BatchSchedule;
import org.tok.view.administration.PortalConfiguration;
import org.tok.view.cache.CacheMngr;
import org.tok.view.engine.GatewayException;
import org.tok.view.engine.GatewayValve;
import org.tok.view.login.LoginConstants;
import org.tok.view.om.page.Page;
import org.tok.view.page.PageManager;
import org.tok.view.pipeline.PipelineException;
import org.tok.view.pipeline.valve.AbstractTalkValve;
import org.tok.view.request.RequestContext;
import org.tok.view.security.CommonUserManager;
import org.tok.view.security.DefaultSecurityPermission;
import org.tok.view.security.TalkSecurityPermission;
import org.tok.view.security.JSSubject;
import org.tok.view.security.UserServiceDAC;
import org.tok.view.session.SessionManager;
import org.tok.view.sso.TalkSSOManager;
import org.tok.view.util.ClusterControlConstants;
import org.tok.view.util.PageNavigationUtil;
import org.tok.view.util.SystemUtil;


public class TalkMobileAuthentication implements CacheMngr
{
	private Log log = LogFactory.getLog(TalkMobileAuthentication.class);
	
	public final static String USER_ID_KEY = "userId"; 
	public final static String USER_DEVICE_KEY = "imei";
	public final static String USER_LAST_ACCESS = "lastAccess";
	
	private SessionManager sessionManager;
	private PageManager pageManager;
	private CommonUserManager commonUserManager;
	private UserServiceDAC userServiceDAC = null;
	
	protected PortalConfiguration configuration;
	
	private Hashtable<String, Map> userInfoRepository = new Hashtable<String, Map>();
    
	public TalkMobileAuthentication()
	{
		this.sessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
		this.pageManager = (PageManager)Talk.getComponentManager().getComponent("org.tok.view.page.PageManager");
		this.commonUserManager = (CommonUserManager)Talk.getComponentManager().getComponent("org.tok.cust.user.control.UserManager");
		this.userServiceDAC = (UserServiceDAC)Talk.getComponentManager().getComponent("org.tok.view.admin.user.service.UserService");
		this.configuration = Talk.getConfiguration();
		
		initialize();
	}
	
	public void initialize()
    {
		userInfoRepository.clear();
    }
	
	public int getInvalidateCode()
    {
    	return ClusterControlConstants.INVALIDATE_CACHE_USERAUTH;
    }
    
    public int getInvalidateOneCode()
    {
    	return ClusterControlConstants.INVALIDATE_CACHE_USERAUTH_ONE;
    }
    
    public void invalidateCache()
    {
    	initialize();
    }
    
	public void invalidateCache(Map cmd)
    {
		String userId = (String)cmd.get("key");
    	String method = (String)cmd.get("method");
    	try {
    		if( "update".equals(method) ) {
    			userInfoRepository.remove(userId);
    		}
	    	else if( "insert".equals(method) ) {
	    		
	    	}
	    	else if( "remove".equals(method) ) {
	    		userInfoRepository.remove(userId);
	    	}
    	}
    	catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    public void syncCache(HttpServletRequest req, String key, String method)
    {
    	if( SystemUtil.getInstance().getContextPath() == null ) {
    		SystemUtil.getInstance().setContextPath(req.getContextPath());
            PageNavigationUtil.getInstance().setContextPath(req.getContextPath());
    	}
    	
    	Map cmd = new HashMap();
		cmd.put("action", String.valueOf(ClusterControlConstants.INVALIDATE_CACHE_USERAUTH_ONE));
		cmd.put("key", key);
		cmd.put("method", method);
		
    	if( configuration.getBoolean("portal.cluster.autoSync", false) == true ) {
			log.debug("*** syncCache method=" + method);
			
	    	int totalSessionCount = 0;
	    	List resultList = SystemUtil.getInstance().execute( cmd );
	    	for(Iterator it=resultList.iterator(); it.hasNext(); )
	    	{
	    		Map result = (Map)it.next();
	    		log.debug("*** system:ip=" + (String)result.get("systemKey") + ", result=" + (String)result.get("result"));
	    	}
    	}
    }
	
	public boolean invoke(HttpServletRequest req, HttpServletResponse res) throws Exception
	{
		java.util.Enumeration em = req.getParameterNames();
        for( ; em.hasMoreElements(); ) {
            String k = (String)em.nextElement();
            String val = req.getParameter( k );
            log.debug("TalkServlet Request : " + k + " = " + val);
        }
        
		try {
	        
			Map userInfoMap = null;
			String userId = null;
			String imei = null;
			String name = null;
			String update = null;
			
			// cookie를 먼저 체크하여 로그인 여부를 파악
        	Map<String, String> cookieMap = new HashMap<String, String>();
	        Cookie[] cookies = req.getCookies();
	        if( cookies != null ) {
		        for(int i=0; i<cookies.length; i++) {
	        		cookieMap.put(cookies[i].getName(), cookies[i].getValue());
	        	}
	        	
	        	userId = (String)cookieMap.get(this.USER_ID_KEY);
	        	imei = (String)cookieMap.get(this.USER_DEVICE_KEY);
	        }
        	
	        log.debug("*** cookie userId=" + userId + ", imei=" + imei);
	        
	        if( cookies == null || cookieMap.containsKey(this.USER_DEVICE_KEY) == false ) {
	        	log.info("*** CASE 1 : cookie가 없는 경우이므로  최초로 사용");
	        	// cookie가 없는 경우이므로  최초로 사용하는 경우
				userId = req.getParameter("pn"); // phoneNumber
				imei = req.getParameter("imei"); // 모바일기기번호
				name = req.getParameter("name"); // 사용자이름
				update = req.getParameter("update"); // 사용자 정보 확인여부
				
				log.debug("*** path=" + req.getRequestURI() + ", userId=" + userId + ", imei=" + imei + ", update=" + update);
				
	        	userInfoMap = getUserInfoMap(userId);
				if( userInfoMap != null ) {
					if( update != null ) {
						if( update.equals("true") ) {
							// 실제로 존재하는 사용자이고 사용자정보 확인을 하였기 때문에 변경정보를 저장하고, 쿠키를 생성하고 로그인을 시킨다.
							log.info("*** CASE 2 : 실제로 존재하는 사용자. 변경정보를 저장. 쿠키를 생성하고 로그인을 시킨다");
							//userInfoMap.put("user_id", userId);
							//userInfoMap.put("imei", imei);
							userInfoMap.put("nm_kor", req.getParameter("nm_kor"));
							userInfoMap.put("sex_flag", req.getParameter("sex_flag"));
							userInfoMap.put("birth_ymd", req.getParameter("birth_ymd"));
							userInfoMap.put("home_addr1", req.getParameter("home_addr1"));
							userInfoMap.put("home_addr2", req.getParameter("home_addr2"));
							userInfoMap.put("intro", req.getParameter("intro"));
							userInfoMap.put("open_flag", req.getParameter("open_flag"));
							userInfoMap.put("authrity", req.getParameter("authrity"));
							
							//System.out.println("*** user_id=" + (String)userInfoMap.get("user_id"));
							
							userInfoMap = updateUserInfo(req, userInfoMap);
							createCookie(req, res, userInfoMap);
							
							res.sendRedirect( Talk.getConfiguration().getString("mobile.index.page") );
							return false;
						}
						else {
							log.info("*** CASE 3 : 실제로 존재하는 사용자. 변경정보를 저장하지않음. 쿠키를 생성하고 로그인을 시킨다");
							createCookie(req, res, userInfoMap);
							
							userInfoRepository.put(userId, userInfoMap);
							HttpSession session = req.getSession(true);
					        session.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);
							
							res.sendRedirect( Talk.getConfiguration().getString("mobile.index.page") );
							return false;
						}
					}
					else {
						// 사용자 정보 확인화면
						log.info("*** CASE 4 : 사용자 정보 확인화면");
						//log.info("*** userInfoMap=" + userInfoMap);
						req.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);
						req.getRequestDispatcher("/user/mobile/profile.cust").forward(req, res);
						//res.sendRedirect( Talk.getConfiguration().getString("mobile.confirm.page", "/cust/statics/process/m_confirm.jsp") );
						return false;
					}
				}
				else {
					// Guest로 사용하기를 희망하는 사용자이므로 기기등록을 하고 역할을 최하등급으로하고 cache에 보관한다.
					log.info("*** CASE 5 : Guest로 사용하기를 희망하는 사용자이므로 기기등록을 하고 역할을 최하등급으로하고 cache에 보관");
					
					String uId = req.getParameter("pn");
					userInfoMap = new HashMap();
					userInfoMap.put("user_id", uId);
					userInfoMap.put("imei", req.getParameter("imei"));
					userInfoMap.put("name", req.getParameter("name"));
					
					//log.debug("*** userInfoMap=" + userInfoMap);
					if( uId == null || uId.length() == 0 ) {
						log.error("*** UserID is null");
						throw new Exception("사용자 ID는 반드시 입력되어야 합니다.");
					}
			        
					createUserInfo(req, res, userInfoMap);
					
					res.sendRedirect( Talk.getConfiguration().getString("mobile.index.page") );
					return false;
				}

	        }
	        else {
		        // Cookie에 사용자 ID가 있는지 체크. 사용자ID가 있으면 한번 로그인 한 사용자이므로 Cache를 살펴보고 userInfoMap이 있는지 찾아본다. 없으면 DB로 부터 읽어서 보관한다. 
		        if( cookieMap.containsKey(this.USER_ID_KEY) ) {
			        // 일단사용자ID로 조회를하여 존재여부 체크
			        if( userInfoRepository.containsKey(userId) == true ) {
			        	//System.out.println("*** userId=" + userId);
						userInfoMap = (Map)userInfoRepository.get(userId);
						//System.out.println("*** userInfoMap=" + userInfoMap);
						
						// userInfoMap이 존재하면 IMIE값이 변조 됐는지 여부를 체크
						return checkDevice(req, res, userInfoMap, imei);
					}
			        else {
			        	userInfoMap = getUserInfoMap(userId);
			        	if( userInfoMap != null ) {
			        		log.info("*** CASE 6 : 등록한 사용자 이지만 cache에 userInfoMap이 없으므로 DB로 부터 읽고 cache에 보관");
				        	// 등록한 사용자 이지만 cache에 userInfoMap이 없으므로 DB로 부터 읽고 cache에 보관한다. 
				        	//System.out.println("*** userId=" + userId);
							userInfoRepository.put(userId, userInfoMap);
							
							// userInfoMap이 존재하면 IMIE값이 변조 됐는지 여부를 체크
							return checkDevice(req, res, userInfoMap, imei);
			        	}
			        	else {
			        		log.info("*** CASE 7 : cookie에는 존재하지만 DB에 없으므로 Guest권한으로 등록시킴");
			        		userInfoMap = new HashMap();
							userInfoMap.put("user_id", userId);
							userInfoMap.put("imei", imei);
							
							createUserInfo(req, res, userInfoMap);
							
							res.sendRedirect( Talk.getConfiguration().getString("mobile.index.page") );
							return false;
			        	}
			        }
		        }
		        else {
		        	log.info("*** CASE 8 : cookie에 userId가 없는 경우이므로 로그인 화면을 출력한다.");
		        	// cookie에 userId가 없는 경우이므로 로그인 화면을 내려보낸다. 이 경우는 로그아웃할 때 Cookie에 userId의 값을 없애기 때문
		        	// 이때 이 화면에는 사용자정보 동의 화면은 제거한다.
		        	req.getRequestDispatcher("/user/mobile/login.cust").forward(req, res);
		        	//res.sendRedirect( Talk.getConfiguration().getString("mobile.login.page", "/cust/statics/process/m_login.jsp") );
		        	return false;
		        }
	        }
		}
		catch(Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			//throw e;
			
			res.sendRedirect( Talk.getConfiguration().getString("mobile.error.page", "/cust/statics/process/m_error.jsp") );
			return false;
		}
	}
	
	private Map getUserInfoMap(String userId) throws Exception
	{
		Map userInfoMap = commonUserManager.getUserInfoMap(userId);
		if( userInfoMap != null ) {
			userServiceDAC.getUserPermission( userInfoMap );
		}
		
		return userInfoMap;
	}
	
	private boolean checkDevice(HttpServletRequest req, HttpServletResponse res, Map userInfoMap, String imei) throws Exception
	{
		String dbImei = (String)userInfoMap.get("imei");
		if( dbImei == null ) {
			System.out.println("*** 기기등록을 하지 않은 사용자");
			// 기기등록을 하지 않은 새로운 사용자이므로 DB에 기기등록을 해주고 userInfoMap에 imei를 저장한다.
			userInfoMap.put("imei", imei);
			updateUserExtraInfo(req, userInfoMap);
		}
		else {
			if( dbImei.equals(imei) == false ) {
				log.info("*** CASE 9 : 사용자정보 도용");
				// DB에 저장된 IMEI와 틀리므로 사용자 도용이거나 기기변경이 일어난 것이므로 관리자에게 문의 
				throw new Exception("사용자 정보가 도용 되었거나 기기변경이 일어난 것이므로 관리자에게 문의 하시기 바랍니다.");
			}
			else {
				String userId = (String)userInfoMap.get("user_id");
				log.debug("*** CASE 10 : userId=" + userId + ", userInfoMap=" + userInfoMap);
				log.info("*** CASE 10 : 사용자정보 일치...통과 !!!");
				// UserInfoMap도 존재하고 IMEI도 일치하므로 통과 시킨다. 
				
				updateCookie(req, res);
				
				HttpSession session = req.getSession(true);
		        session.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);
		        session.setAttribute(LoginConstants.SSO_LOGIN_ID, userId);

		        String path = req.getRequestURI();
				if( path.indexOf("m_index.page") > -1 ) {
					res.sendRedirect( Talk.getConfiguration().getString("mobile.index.page") );
					return false;
				}
			}
		}
		
		return true;
	}
	
	private Map updateUserInfo(HttpServletRequest req, Map userInfoMap) throws Exception
	{
		String userId = (String)userInfoMap.get("user_id");
		
		commonUserManager.updateUserInfo(userInfoMap);
		
		userInfoMap = getUserInfoMap(userId);
		//System.out.println("######### updateUserInfo userInfoMap=" + userInfoMap);
		userInfoRepository.put(userId, userInfoMap);
		
		HttpSession session = req.getSession(true);
        session.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);
		
		syncCache(req, userId, "update");
		
		return userInfoMap;
	}
	
	private void updateUserExtraInfo(HttpServletRequest req, Map userInfoMap) throws Exception
	{
		String userId = (String)userInfoMap.get("user_id");
		
		commonUserManager.updateUserExtraInfo(userInfoMap);
		
		userInfoMap = getUserInfoMap(userId);
		userInfoRepository.put(userId, userInfoMap);
		
		HttpSession session = req.getSession(true);
        session.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);
		
		syncCache(req, userId, "update");
	}
	
	private void createUserInfo(HttpServletRequest req, HttpServletResponse res, Map userInfoMap) throws Exception
	{
		String userId = (String)userInfoMap.get("user_id");
		String imei = (String)userInfoMap.get("imei");
		String name = (String)userInfoMap.get("name");
		
		commonUserManager.addUserInfo(userInfoMap);
		userInfoMap = getUserInfoMap(userId);
		
		createCookie(req, res, userInfoMap);

		userInfoRepository.put(userId, userInfoMap);
	
        HttpSession session = req.getSession(true);
        session.setAttribute(LoginConstants.SSO_LOGIN_INFO, userInfoMap);

		syncCache(req, userId, "update");
	}
	
	private void createCookie(HttpServletRequest req, HttpServletResponse res, Map userInfoMap)
	{
		String userId = (String)userInfoMap.get("user_id");
		String imei = (String)userInfoMap.get("imei");
		log.debug("*** userId=" + userId + ", imei=" + imei);
		
		Cookie tmp = new Cookie(this.USER_ID_KEY, userId);
        tmp.setPath("/");
        //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "tok") );
        int maxAge = Integer.parseInt( Talk.getConfiguration().getString("mobile.cookie.maxage", "315360000") ); //10년
        tmp.setMaxAge( maxAge );
        res.addCookie( tmp );
        
        tmp = new Cookie(this.USER_DEVICE_KEY, imei);
        tmp.setPath("/");
        //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "tok") );
        tmp.setMaxAge( maxAge );
        res.addCookie( tmp );
        
        String lastAccess = DateUtil.getCurrentDate();
        tmp = new Cookie(this.USER_LAST_ACCESS, lastAccess);
        tmp.setPath("/");
        //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "tok") );
        tmp.setMaxAge( maxAge );
        res.addCookie( tmp );
	}
	
	private void updateCookie(HttpServletRequest req, HttpServletResponse res)
	{
		int maxAge = Integer.parseInt( Talk.getConfiguration().getString("mobile.cookie.maxage", "315360000") ); //10년
        String lastAccess = DateUtil.getCurrentDate();
        Cookie tmp = new Cookie(this.USER_LAST_ACCESS, lastAccess);
        tmp.setPath("/");
        //tmp.setDomain( "." + Talk.getConfiguration().getString("portal.domain", "tok") );
        tmp.setMaxAge( maxAge );
        res.addCookie( tmp );
	}
	
}
