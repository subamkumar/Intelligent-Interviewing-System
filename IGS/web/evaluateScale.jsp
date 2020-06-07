<%@page import="igs.SubjectScale"%>
<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>
<jsp:setProperty name="ih" property="selfScale" param="val" />

<%
    if(ih.evaluateSelfScale())
    {
        out.print("1");
    }
    else
    {
        out.print("0");
    }
%>

