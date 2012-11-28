<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
	<table id="resultMsgTable">
		<tr id="resultMsgTableRow">
			<td>
				&nbsp;&nbsp;&nbsp;<label class="result">검색 결과&nbsp;(해당되는 주소를 선택해 주세요.)</label>
			</td>
		</tr>
	</table>
<div id="resultSet">
	<table id="resultSetTable">
	<c:if test="${empty zipCodes }" var="result" >
		<tr id="resultSetTableRow">
			<td>
				&nbsp;&nbsp;&nbsp;<label class="error">'<c:out value="${dong}"/>' 검색 결과가 없습니다. 다시 입력해주세요.</label>
			</td>
		</tr>
	</c:if>
	<c:if test="${result == false}">
		<c:forEach items="${zipCodes}" var="zips">
			<tr id="resultSetTableRow">
				<td id="resultSetTableData">
					<a class="data" 
					   href="#" 
					   onclick='selectAddress("<c:out value="${zips.zipCode}"/>", "<c:out value="${zips.simpleAddress}"/>")'>
					   <c:out value="${zips.zipCode}"/>&nbsp;&nbsp;<c:out value="${zips.address}"/>
					</a>			
				</td>
			</tr>
		</c:forEach>
	</c:if>
	</table>
</div>
