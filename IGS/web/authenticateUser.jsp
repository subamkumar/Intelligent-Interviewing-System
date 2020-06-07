<jsp:useBean id="lgn" scope="page" class="igs.Login"/>
<jsp:setProperty name="lgn" property="*"/>
<%
    int id = lgn.authenticate();
    if(id == -1)
    {
        out.print("0");//login failure
    }
    else
    {
        //create a new session 
        HttpSession s = request.getSession(true);
        s.setAttribute("suid", id);
        out.print("1");//login success
    }
%>
