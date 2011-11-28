<%-- 
    Document   : index
    Created on : 16.11.2011, 10:08:30
    Author     : Milan
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="net.milanvit.iforum.controllers.ThreadController" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%! ThreadController threadController = new ThreadController (null, null); %>
<c:set var="threads" value="<%= threadController.findThreadEntities ()%>" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | secured area</title>
    </head>
    <body>
        <h1>iForum | secured area</h1>

		<p>
			Hello, <c:out value="${sessionScope.username}" />! Maybe you want
			to see the <a href="listofusers.jsp">list of all users</a>?</p>

		<p><a href="../logout.jsp">Or maybe it's time to log out</a>?</p>

		<p>
			Here is the list of all threads. If you'd like to discuss something
			new, feel free to <a href="createthread.jsp">create new thread</a>!
		</p>

		<table>
			<tr>
				<th>Locked</th>
				<th>Title</th>
				<th>Author</th>
				<th>Created</th>
			</tr>
			<c:forEach var="thread" items="${threads}">
				<tr>
					<td>
						<c:choose>
							<c:when test="${thread.locked}">
								Locked
							</c:when>
							<c:otherwise>
								Unlocked
							</c:otherwise>
						</c:choose>
					</td>
					<td><a href="showthread.jsp?id=${thread.id}">${thread.title}</a></td>
					<td>${thread.author.username}</td>
					<td><fmt:formatDate pattern="dd. MM. yyyy, hh.mm:ss"
									value="${thread.created}" /></td>
				</tr>
			</c:forEach>
		</table>
    </body>
</html>
