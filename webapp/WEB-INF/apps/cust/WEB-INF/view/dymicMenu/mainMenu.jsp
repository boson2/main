<%@ page contentType="text/json;chartset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%--@ page import="java.util.*"--%>
<%--@ page import="org.tok.cust.admin.dymicMenu.service.DymicMenuVO"--%>
<%--
	List menuList = (ArrayList)request.getAttribute("menuList");
	for(int i=0; i<menuList.size(); i++) {
		DymicMenuVO dmVO = (DymicMenuVO)menuList.get(i);
		System.out.println("menuName=["+dmVO.getMenuName()+"]");
	}
	List cafeList = (ArrayList)request.getAttribute("cafeList");
	for(int i=0; i<cafeList.size(); i++) {
		Map cafeVO = (HashMap)cafeList.get(i);
		System.out.println("cmntNm=["+cafeVO.get("cmntNm")+"]");
	}
--%>
{
  "Status": "<c:out value="${acForm.status}"/>",
  "Menu":
  [
<c:forEach items="${menuList}" var="menu" varStatus="status">    
	{
		"menuId": "<c:out value="${menu.menuId}"/>",
		"menuName": "<c:out value="${menu.menuName}"/>",
		"url": "<c:out value="${menu.url}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ],
  "Cafe":
  [
<c:forEach items="${cafeList}" var="cafe" varStatus="status">
	{
		"cmntId": "<c:out value="${cafe.cmntId}"/>",
		"cmntUrl": "<c:out value="${cafe.cmntUrl}"/>",
		"cateId": "<c:out value="${cafe.cateId}"/>",
		"cmntNm": "<c:out value="${cafe.cmntNm}"/>"
	} <c:if test="${!status.last}">,</c:if>
</c:forEach>
  ]
}
