<%-- 
userHome
--%>
<%
    String uid = "";
    try
    {
        HttpSession s = request.getSession(false);
        uid = s.getAttribute("suid").toString();
    }
    catch(Exception ex)
    {}
    if(uid.equals(""))
    {
        response.sendRedirect("userLogin.jsp");
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="navBar.jsp"/>
        <h1>USER HOME</h1>
        
    </body>
</html>
