
<%@ page contentType="text/xml; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>

<js>
    <status><c:out value="${inform.resultStatus}"/></status>
    <reason><c:out value="${inform.failureReason}"/></reason>    
    <userpass>

		<nmKor><![CDATA[<c:out value="${userpass.nmKor}"/>]]></nmKor>	
		<userId><![CDATA[<c:out value="${userpass.userId}"/>]]></userId>	
		<columnValue><![CDATA[<c:out value="${userpass.columnValue}"/>]]></columnValue>	
		<gradeCd><![CDATA[<c:out value="${userpass.gradeCd}"/>]]></gradeCd>	
		<emailAddr><![CDATA[<c:out value="${userpass.emailAddr}"/>]]></emailAddr>	
		<homeTel><![CDATA[<c:out value="${userpass.homeTel}"/>]]></homeTel>	
		<mobileTel><![CDATA[<c:out value="${userpass.mobileTel}"/>]]></mobileTel>	
		<homeZip><![CDATA[<c:out value="${userpass.homeZip}"/>]]></homeZip>	
		<homeAddr1><![CDATA[<c:out value="${userpass.homeAddr1}"/>]]></homeAddr1>	
		<homeAddr2><![CDATA[<c:out value="${userpass.homeAddr2}"/>]]></homeAddr2>	
		<birthYmd><c:out value="${userpass.birthYmdValue}"/></birthYmd>	
		<intro><![CDATA[<c:out value="${userpass.intro}"/>]]></intro>	
		<guide><![CDATA[<c:out value="${userpass.guide}"/>]]></guide>	
		<isActive><c:out value="${userpass.isActive}"/></isActive>	
		<sexFlag><c:out value="${userpass.sexFlag}"/></sexFlag>	
		<accessFlag><c:out value="${userpass.accessFlag}"/></accessFlag>	
		<regDatim><c:out value="${userpass.regDatimValue}"/></regDatim>	
		<age><c:out value="${userpass.age}"/></age>	
	</userpass>
</js>

