package org.tok.cust.mobile.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tok.view.Talk;
import org.tok.view.code.TalkCodeManager;
import org.tok.view.codebase.CodeBundle;
import org.tok.view.login.LoginConstants;
import org.tok.view.mobile.push.PushManager;
import org.tok.view.multiresource.TalkMultiResourceManager;
import org.tok.view.multiresource.MultiResourceBundle;
import org.tok.view.portalsite.TalkMenu;
import org.tok.view.portalsite.PortalSiteManager;
import org.tok.view.portalsite.impl.TalkMenuImpl;
import org.tok.view.session.SessionManager;
import org.tok.view.statistics.AggregateStatistics;
import org.tok.view.statistics.PortalStatistics;
import org.tok.view.statistics.StatisticsManager;
import org.tok.view.util.TalkLocale;


/**
 * 전체 페이지를 엑셀로 출력
 * @author snoopy
 */

public class MobileUtilityController extends MultiActionController {

	private final Log   log = LogFactory.getLog(getClass());
	
	private SessionManager enviewSessionManager;
	private PushManager pushManager;
	
	public MobileUtilityController()
	{
		this.enviewSessionManager = (SessionManager)Talk.getComponentManager().getComponent("org.tok.view.session.SessionManager");
        this.pushManager = (PushManager)Talk.getComponentManager().getComponent("org.tok.view.mobile.push.PushManager");
	}
	
	public ModelAndView pushForAJAX(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		Map userInfoMap = (Map)enviewSessionManager.getUserData(request);
		if(userInfoMap == null) {
			throw new Exception("You have to login !!!");
		}
		
		String title = request.getParameter("title");
		String message = request.getParameter("message");
		int deviceType = 1;
		String deviceTypeStr = (String)userInfoMap.get("deviceType");
		if( deviceTypeStr != null ) {
			deviceType = Integer.parseInt(deviceTypeStr);
		}
		String deviceID = "APA91bGmRuInsND6F1p979uotSV3DiUK-G6m7hdLxdLgP9Wuk_oNKqTLHZqu8YDmFx9Z9HbvjHhSLOe3feRK-HMPHY9ausokk6oRKx4_ZLQS7pencvyWbfzCUvvGd2opPCH02GainP6gnARXbArhdcgaDsMFINhi4g"; 
		//(String)userInfoMap.get("deviceID");
		
		this.pushManager.push2device(deviceType, deviceID, title, message);
		
		return null;
	}
	
}
