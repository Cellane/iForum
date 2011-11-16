<%-- 
    Document   : index
    Created on : 15.11.2011, 16:07:03
    Author     : Milan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | login</title>
    </head>
    <body>
        <h1>iForum | login</h1>

		<form action="loginUser" method="post">
			<table>
				<tr>
					<td>Username:</td>
					<td><input type="text" name="username" value="" /></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" name="password" value="" /></td>
				</tr>
				<tr>
					<td><a href="register.jsp">Register</a></td>
					<td><input type="submit" value="Log in" /></td>
				</tr>
			</table>
		</form>
    </body>
</html>
