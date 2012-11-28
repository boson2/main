
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
<c:forEach items="${results}" var="grade" varStatus="status">
	{
"code": "<c:out value="${grade.code}"/>","codeName": "<c:out value="${grade.codeName}"/>","sortOrder": "<c:out value="${grade.sortOrder}"/>","remark": "<c:out value="${grade.remark}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
