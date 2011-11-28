<%-- 
    Document   : listofpeople
    Created on : 17.11.2011, 0:08:04
    Author     : Milan
--%>

<%@ page import="net.milanvit.iforum.controllers.UserController" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%! UserController userController = new UserController (null, null); %>
<c:set var="users" value="<%= userController.findUserEntities () %>" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | list of users</title>
    </head>
    <body>
        <h1>iForum | list of users</h1>
		
		<p>
			When you get bored from looking at this list, maybe you'd like to
			<a href="index.jsp">go back</a>?
		</p>

		<table>
			<tr>
				<th>Username</th>
				<th>Age</th>
				<th>Avatar</th>
				<th>Sex</th>
				<th>Login count</th>
				<th>Last login</th>
			</tr>
			<c:forEach var="user" items="${users}">
				<tr>
					<td>${user.username}</td>
					<td>${user.age}</td>
					<td><img src="${user.avatar}" width="64" heighth="64" /></td>
					<td>
						<c:choose>
							<c:when test="${user.sex}">
								Male
							</c:when>
							<c:otherwise>
								Female
							</c:otherwise>
						</c:choose>
					</td>
					<td>${user.loginCount}</td>
					<td>
						<fmt:formatDate pattern="dd. MM. yyyy, HH.mm:ss"
										value="${user.loginLast}" />
					</td>
				</tr>
			</c:forEach>
		</table>
    </body>
</html>
