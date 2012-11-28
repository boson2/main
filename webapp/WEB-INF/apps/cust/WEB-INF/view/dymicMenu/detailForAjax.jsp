
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <dymicMenu>

		<menuName><![CDATA[<c:out value="${dymicMenu.menuName}"/>]]></menuName>	
		<menuId><c:out value="${dymicMenu.menuId}"/></menuId>	
		<desc><![CDATA[<c:out value="${dymicMenu.desc}"/>]]></desc>	
		<sortOrder><c:out value="${dymicMenu.sortOrder}"/></sortOrder>	
		<applyFeed><![CDATA[<c:out value="${dymicMenu.applyFeed}"/>]]></applyFeed>	
		<writeAuth><![CDATA[<c:out value="${dymicMenu.writeAuth}"/>]]></writeAuth>	
		<readAuth><![CDATA[<c:out value="${dymicMenu.readAuth}"/>]]></readAuth>	
		<url><![CDATA[<c:out value="${dymicMenu.url}"/>]]></url>	
		<isService><c:out value="${dymicMenu.isService}"/></isService>	
	</dymicMenu>
</js>

