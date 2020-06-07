<%-- 
interview descision
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="navBar.jsp"/>
        <h1>Interview Rules</h1>
        <div>
            <ul>
                <li> Rule 1</li>
                <li> Rule 2</li>
                <li> Rule 3</li>
                <li> Rule 4</li>
                <li> Rule 5</li>
                <li> Rule 6</li>
                <li> Rule 7</li>
            </ul>
            <form method ="post" action ="testUser.jsp">
                <input type ="submit" value="Accept and Continue" /> 
                
            </form>
        </div>
    </body>
</html>
