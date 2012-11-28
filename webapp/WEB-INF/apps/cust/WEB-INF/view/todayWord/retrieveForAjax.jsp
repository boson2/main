
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
<c:forEach items="${results}" var="todayWord" varStatus="status">
	{
"wordId": "<c:out value="${todayWord.wordId}"/>","title": "<c:out value="${todayWord.title}"/>","content": "<c:out value="${todayWord.content}"/>","startDate": "<c:out value="${todayWord.startDate}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
