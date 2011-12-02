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
<%! PostController postController = new PostController (); %>
<%! ThreadController threadController = new ThreadController (); %>
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

		<%@ include file="../WEB-INF/jspf/threadtools.jspf" %>

		<h2>${thread.title}</h2>

		<div style="float: right">
			${thread.author.username} on
			<fmt:formatDate pattern="dd. MM. yyyy, HH.mm:ss"
							value="${thread.created}" />
		</div>

		<p>${thread.post}</p>

		<%@ include file="../WEB-INF/jspf/showreplies.jspf" %>
		<%@ include file="../WEB-INF/jspf/postreply.jspf" %>
	</body>
</html>
