<%-- 
    Document   : index
    Created on : 16.11.2011, 10:08:30
    Author     : Milan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="net.milanvit.iforum.controllers.ThreadController" %>
<%@ page session="true" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%! ThreadController threadController = new ThreadController (); %>
<c:set var="threads" value="<%= threadController.findThreadEntities () %>" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | secured area</title>
    </head>
    <body>
        <h1>iForum | secured area</h1>

		<p>
			Hello, ${sessionScope.user.username}! Long time, no see! Maybe you'd
			like to see the	<a href="listofusers.jsp">list of all users</a>?
		</p>

		<p>Or maybe it's time to <a href="../logout.jsp">log out</a>?</p>

		<%@ include file="../WEB-INF/jspf/listofthreads.jspf" %>
    </body>
</html>
