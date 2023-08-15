<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>minis request</title>
</head>
<body>

<div>

    <h2>hello world</h2>
    <%= request.getAttribute("msg").toString() %>
    <p><%= request.getAttribute("msg") %></p>
</div>


</body>

</html>