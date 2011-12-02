<%-- 
    Document   : submissionerror
    Created on : 2.12.2011, 22:25:15
    Author     : Milan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="validationErrors" scope="request" class="net.milanvit.iforum.models.ValidationErrors" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | submission error</title>
    </head>
    <body>
        <h1>iForum | submission error</h1>

		<p>There have been following errors with your submission:</p>

		<ul>
			<c:forEach var="errorMessage" items="${validationErrors.errorMessages}">
				<li>${errorMessage}</li>
			</c:forEach>
			<% validationErrors.emptyMessages (); %>
		</ul>

		<p><a href="javascript:history.back ()">Try again!</a></p>
    </body>
</html>
