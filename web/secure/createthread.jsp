<%-- 
    Document   : newthread
    Created on : 23.11.2011, 8:51:13
    Author     : Milan
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | new thread</title>
    </head>
    <body>
        <h1>iForum | new thread</h1>
		
		<form action="createThread" method="post">
			<table>
				<tr>
					<td>Title:</td>
					<td><input type="text" name="title" value="" /></td>
				</tr>
				<tr>
					<td>Post:</td>
					<td>
						<textarea name="post" rows="10" cols="50"></textarea>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Create" /></td>
				</tr>
			</table>
		</form>
    </body>
</html>
