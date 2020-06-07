<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    
    <script>
        var req, result;
        function authenticate(a, b)
        {
            req = new XMLHttpRequest();
            var url = "authenticateUser.jsp?loginId="+a+"&loginPass="+b;
            //var data = "loginId="+a+"&loginPass="+b;
            req.onreadystatechange = authenticateResult;
            req.open("GET", url, false);
            req.send();//go
        }
        
        function authenticateResult()
        {
            if(req.readyState === 4)
            {
                if(req.status === 200)
                {
                    result = req.responseText;
                }
            }
        }
        
        function showMessage(ele, msg)
        {
            var e = document.getElementById(ele);
            e.innerHTML = msg;
        }
        
        function login()
        {
            var a, b;
            a = document.getElementById("loginId").value;
            b = document.getElementById("loginPass").value;
            
            if(a === "")
            {
                showMessage("divMessage", "Login ID Missing");
            }
            else if(b === "")
            {
                showMessage("divMessage", "Password Missing");
            }
            else
            {
                authenticate(a, b);
                if(eval(result) === 1)
                {//success
                    
                    //showMessage("divMessage", "Login SUCCESS");
                    document.f.submit();
                }
                else
                {//failure
                    showMessage("divMessage", "Login Failed");
                }
            }
        }
        
    </script>
</head>
<body>
    <div id="divUserLogin">
    <table>
        <form name = "f" method = "post" action="userHome.jsp">
            <tr>
                <td>User Name </td>
                <td><input type ="text" id="loginId" name="loginId" ></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type ="password" id="loginPass" name="loginPass" ></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type ="button" id="bttnLogin" name="bttnLogin" value = "Login" onClick="login()"></td>
            </tr>
            <tr>
                <td colspan="2">
                    <div id="divMessage"></div>
                </td>
            </tr>
        </form>
    </table
    </div>
</body>
</html>
