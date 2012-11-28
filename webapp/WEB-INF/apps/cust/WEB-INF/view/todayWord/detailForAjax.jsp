
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <todayWord>

		<wordId><c:out value="${todayWord.wordId}"/></wordId>	
		<title><![CDATA[<c:out value="${todayWord.title}"/>]]></title>	
		<content><![CDATA[<c:out value="${todayWord.content}"/>]]></content>	
		<startDate><c:out value="${todayWord.startDate}"/></startDate>	
	</todayWord>
</js>

