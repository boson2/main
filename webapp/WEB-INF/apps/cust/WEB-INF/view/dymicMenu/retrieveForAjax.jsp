
<%@ page contentType="text/json; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="util" uri="/WEB-INF/tld/utility.tld" %>
{
  "Status": "<c:out value="${inform.resultStatus}"/>",
  "Reason": "<c:out value="${inform.failureReason}"/>",
  "TotalSize": "<c:out value="${inform.totalSize}"/>",
  "ResultSize": "<c:out value="${inform.resultSize}"/>",
  "Data":
  [
<c:forEach items="${results}" var="dymicMenu" varStatus="status">
	{
"menuId": "<c:out value="${dymicMenu.menuId}"/>","menuName": "<c:out value="${dymicMenu.menuName}"/>","applyFeed": "<c:out value="${dymicMenu.applyFeed}"/>","writeAuth": "<c:out value="${dymicMenu.writeAuth}"/>","readAuth": "<c:out value="${dymicMenu.readAuth}"/>","desc": "<c:out value="${dymicMenu.desc}"/>","serviceStatus": "<c:out value="${dymicMenu.serviceStatus}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
