<%-- 
    Document   : showthread
    Created on : 28.11.2011, 15:22:16
    Author     : Milan
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.milanvit.iforum.controllers.PostController" %>
<%@ page import="net.milanvit.iforum.controllers.ThreadController" %>
<%@ page import="net.milanvit.iforum.models.Thread" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%! PostController postController = new PostController (null, null); %>
<%! ThreadController threadController = new ThreadController (null, null); %>
<% int threadId = Integer.parseInt (request.getParameter ("id")); %>
<% Thread thread = threadController.findThread (threadId); %>
<c:set var="thread" value="<%= thread %>" />
<c:set var="posts" value="<%= postController.getEntityManager ().createNamedQuery ("Post.findByThread").setParameter ("thread", thread).getResultList () %>" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | display thread</title>
    </head>
    <body>
        <h1>iForum | display thread</h1>

		<c:if test="${thread.author.username == sessionScope.username}">
			<h2>Thread tools</h2>

			<p>
				You've created this thread. Do you wish to
				<c:choose>
					<c:when test="${thread.locked}">
						<a href="lockThread?id=${thread.id}">unlock</a>
					</c:when>
					<c:otherwise>
						<a href="lockThread?id=${thread.id}">lock</a>
					</c:otherwise>
				</c:choose>
				it?
			</p>
		</c:if>

		<p>
			Maybe you'd like to return to the
			<a href="index.jsp">title page</a>?
		</p>

		<h2>${thread.title}</h2>

		<div style="float: right">
			${thread.author.username} on
			<fmt:formatDate pattern="dd. MM. yyyy, HH.mm:ss"
							value="${thread.created}" />
		</div>

		<p>${thread.post}</p>

		<h2>Replies</h2>
		<c:forEach var="post" items="${posts}">
			<div style="float: right">
				<c:if test="${post.author.username == sessionScope.username}">
					<a href="deletePost?id=${post.id}">delete</a> |
				</c:if>

				${post.author.username} on
				<fmt:formatDate pattern="dd. MM. yyyy, HH.mm:ss"
								value="${post.created}" />
			</div>

			<p>${post.post}</p>
		</c:forEach>

		<c:if test="${!thread.locked}">
			<h2>Post a reply</h2>

			<form action="createPost" method="post">
				<table>
					<tr>
						<td>Reply:</td>
						<td><textarea name="post" rows="10" cols="50"></textarea></td>
					</tr>
					<tr>
						<td><input type="hidden" name="thread" value="${thread.id}" /></td>
						<td><input type="submit" value="Reply" /></td>
					</tr>
				</table>
			</form>
		</c:if>
	</body>
</html>
