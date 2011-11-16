<%-- 
    Document   : register
    Created on : 15.11.2011, 16:16:13
    Author     : Milan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>iForum | register</title>
    </head>
    <body>
        <h1>iForum | register</h1>

		<form action="registerUser" method="post">
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
					<td>Again:</td>
					<td><input type="password" name="passwordagain" value="" /></td>
				</tr>
				<tr>
					<td>Age:</td>
					<td><input type="text" name="age" value="" /></td>
				</tr>
				<tr>
					<td>Avatar:</td>
					<td><input type="text" name="avatar" value="" /></td>
				</tr>
				<tr>
					<td>Sex:</td>
					<td>
						<select name="sex">
							<option value="0">Female</option>
							<option value="1" selected="selected">Male</option>
						</select>
					</td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="Register" /></td>
				</tr>
			</table>
		</form>
    </body>
</html>
