<%@page import="igs.SubjectScale"%>
<%@page import="java.util.*"%>
<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>
<jsp:setProperty name="ih" property="intrws"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            var req, result;
            function evaluateScale(text)
            {
                req = new XMLHttpRequest();
                var url = "evaluateScale.jsp?val="+text;
                req.onreadystatechange = replyEvaluation;
                req.open("GET", url, false);
                req.send();
            }
            
            function replyEvaluation()
            {
                if(req.readyState === 4)
                {
                    if(req.status=== 200)
                    {
                        result = req.responseText;
                    }
               }
            }

            function scaleCheck()
            {
                var flag=1;
                
                //var text = '{ "scaling" : [{ "sub1":"val1" }, { "sub2":"val2"},{ "sub3":"val3"} ]}';
                var text = '{"scaling" : [ ';
                
                if(document.forms[0].scaleValue.length === undefined)
                {
                    text = text + ' { "' +forms[0].scaleSubject.value + '":"' + forms[0].scaleValue.value +'" }';
                    if(document.forms[0].scaleValue.value < 1 || document.forms[0].scaleValue.value >10)
                    {
                        flag = 0;
                    }
                }
                else
                {
                    var i;
                    for(i =0; i < document.forms[0].scaleValue.length; i++)
                    {
                        if(document.forms[0].scaleValue[i].value !== "")
                        {
                            text = text + ' { "' + document.forms[0].scaleSubject[i].value + '":"' + document.forms[0].scaleValue[i].value +'" }';
                            if(document.forms[0].scaleValue[i].value < 1 || document.forms[0].scaleValue[i].value >10)
                            {
                                flag = 0;
                                break;
                            }
                
                        }
                        
                        if( i+1 < document.forms[0].scaleValue.length )
                        {
                            text = text +', ';
                        }
                    }
                    
                }//else
                if(flag === 0)
                {
                    alert("Scale Value Must Be In Range 1-10");
                }
                else
                {
                    text = text + ' ]}';
                    evaluateScale(text);    
                    if(eval(result) === 1)
                    {
                        return true;
                    }
                    else
                    {
                        document.forms[0].action = "userHome.jsp";
                        alert("Self Scaling Doesnt Fit Company Criteria, Interview Cancelled");
                        return true;
                    }
                }
                
                return false;
            }
            
        </script>
    </head>
    <body>
        <jsp:include page="navBar.jsp"/>
        <h1>Interviews</h1>
        
        <div>
            <table width="100%">
                <%
                    HashMap<Integer, SubjectScale> hmap = ih.getSubjectsInterviewWise();
                    
                    if(hmap.size() > 0)
                    {
                        String arr[] = ih.fetchHeader();
                        int x =  arr.length;
                    
                        out.println("<tr>");
                        
                        for(String s : arr)
                        {
                            out.println("<td> <b>" + s.toUpperCase() + " </b> </td>");
                        }
                        out.println("</tr>");

                        out.println("<form method=\"post\" action=\"interviewDescision.jsp\" onsubmit=\"return scaleCheck()\">");
                        
                        Collection<SubjectScale> c = hmap.values();
                        for(SubjectScale ss : c)
                        {
                            out.println("<tr>");
                            out.println("<td> "+ ss.getSubjectName() );
                            out.println("<input type = \"hidden\" name=\"scaleSubject\" value = \""+ ss.getSubjectId() +"\" /></td>");
                            out.println("<td> <input type = \"number\" name=\"scaleValue\" value = \"\" /></td>");
                            out.println("</tr>"); 
                            
                        }//for
                        out.println("<tr>");
                        out.println("<td colspan = "+ x+">");
                        out.println("<input type = \"submit\" value = \"Evaluate Eligibility\" />");
                        out.println("</td>");
                        out.println("</tr>");
                        
                        out.println("</form>");
                    }
                    else
                    {
                        out.println("No Subject Available");
                    }
                %>
                
            </table>
        </div>
    </body>
</html>
