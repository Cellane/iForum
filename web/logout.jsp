<%-- 
    Document   : logout
    Created on : 16.11.2011, 13:24:16
    Author     : Milan
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | logout</title>
    </head>
    <body>
        <h1>iForum | logout</h1>
		<% session.invalidate (); %>
		<p>
			If you don't see any error, you have been successfully logged off!
			Have you changed <a href="index.jsp">your mind</a>?
		</p>
    </body>
</html>
