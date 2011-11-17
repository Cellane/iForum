<%-- 
    Document   : index
    Created on : 16.11.2011, 10:08:30
    Author     : Milan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="true" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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
			to see the <a href="listOfUsers">list of all users</a>?</p>
		
		<p><a href="../logout.jsp">Or maybe it's time to log out</a>?</p>
    </body>
</html>
