<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<c:if test="${empty zipCodes }" var="result" >
	<c:if test="${isFirst == false}">
		<table>
			<tr>
				<th>
					&nbsp;&nbsp;&nbsp;<label class="error">'<c:out value="${dong}"/>' 검색 결과가 없습니다. 다시 입력해주세요.</label>
				</th>
			</tr>
		</table>
	</c:if>
</c:if>
<c:if test="${result == false}">
	<table>
		<tr>
			<th>
				&nbsp;&nbsp;&nbsp;<label class="result">검색 결과&nbsp;(해당되는 주소를 선택해 주세요.)</label>
			</th>
		</tr>
	</table>
	<div style="overflow-x:hidden;overflow-y:auto;width:450;height:300;padding:0px;">
		<table>
			<c:forEach items="${zipCodes}" var="zips">
				<tr>
					<td>
						<a class="data" 
						   href="#" 
						   onclick='selectAddress("<c:out value="${zips.zipCode}"/>", "<c:out value="${zips.simpleAddress}"/>")'>
						   <c:out value="${zips.zipCode}"/>&nbsp;&nbsp;<c:out value="${zips.address}"/>
						</a>			
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</c:if>
