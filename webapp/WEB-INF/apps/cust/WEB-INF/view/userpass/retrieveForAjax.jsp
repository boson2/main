
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
<c:forEach items="${results}" var="userpass" varStatus="status">
	{
"userId": "<c:out value="${userpass.userId}"/>","nmKor": "<c:out value="${userpass.nmKor}"/>","gradeCd": "<c:out value="${userpass.gradeCd}"/>","mobileTel": "<c:out value="${userpass.mobileTel}"/>","isActive": "<c:out value="${userpass.isActive}"/>","sexFlag": "<c:out value="${userpass.sex}"/>","regDatim": "<c:out value="${userpass.regDatimValue}"/>","age": "<c:out value="${userpass.age}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
