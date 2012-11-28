
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <grade>

		<code><![CDATA[<c:out value="${grade.code}"/>]]></code>	
		<codeName><![CDATA[<c:out value="${grade.codeName}"/>]]></codeName>	
		<remark><![CDATA[<c:out value="${grade.remark}"/>]]></remark>	
	</grade>
</js>

