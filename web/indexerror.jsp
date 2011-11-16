<%-- 
    Document   : indexerror
    Created on : 16.11.2011, 8:52:23
    Author     : Milan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="validationErrors" scope="request" class="net.milanvit.iforum.models.ValidationErrors" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | login error</title>
    </head>
    <body>
        <h1>iForum | login error</h1>

		<p>There have been following errors with your login:</p>

		<ul>
			<c:forEach var="errorMessage" items="${validationErrors.errorMessages}">
				<li><c:out value="${errorMessage}" /></li>
			</c:forEach>
			<% validationErrors.emptyMessages (); %>
		</ul>

		<p><a href="javascript:history.back ()">Try again!</a></p>
    </body>
</html>
