<%-- 
testUser
--%>

<%@page import="igs.SubjectScale"%>
<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
        var req, result, maxTime, myTime =0, timerId;
        maxTime= 0;
        function processQA(qid, ua, ti)
        {
            req = new XMLHttpRequest();
            var url = "processQA.jsp?qid="+qid+"&ua="+ua+"&ti="+ti;
            req.onreadystatechange = responseProcessQA;
            req.open("GET", url, false);
            req.send();
        }
        
        function responseProcessQA()
        {
            if(req.readyState === 4)
            {
                if(req.status === 200)
                {
                    result = req.responseText;
                    updateClientArea();
                }
                
            }
            
        }
        
        function updateClientArea()
        {
            /*
             {
                "qid":7,
                "qtext":"What is a constructor?",
                "qRank":1,
                "qoptions":[ "17", "A special Method of The Class", "18", "An Object Initializer", "19", "Both A and B" ]
             }
             */
            
            var obj = JSON.parse(result);
            maxTime = obj.qRank * 30 ;
            myTime = maxTime;
            
            var innerH ;
            var flag =false;    
            innerH  = "<form><table><tr><td colspan = '2'><input type = 'hidden' name ='qid' value = '" + obj.qid+ "'/>"+ obj.qtext + "</td> </tr>";

            for(i in obj.qoptions)
            {
                if(flag === false)
                    innerH = innerH + "<tr><td><input  type = 'radio' name = 'qoption' value = '" + obj.qoptions[i] + "'/></td>";
                else
                    innerH = innerH + "<td>" + obj.qoptions[i] + "</td></tr>";
              
                flag = !flag;
            }//for
            innerH = innerH + "<tr><td colspan ='2'><input type = 'button' value = 'Reply' onClick = 'userReply()'/> </td></tr>";
            innerH = innerH + "</table></form>";
        
            document.getElementById("divTest").innerHTML = innerH;
            timerId = setInterval(updateTimer, 1000);
            alert(innerH);
        }
        
        
        function postReply()
        {
            var ua = -1;
            var qid = -1;
            var t = -1;
            try
            {
                ua = document.forms[0].qoption.value;
                qid = document.forms[0].qid.value;
                t = maxTime - myTime;
            }
            catch(err)
            {}
            if(ua === "" || ua === undefined)
                ua = -1;
            
            alert(ua + " " + qid + " " + t);
            processQA(ua,qid,t);
        }
        
        function updateTimer()
        {
            if(myTime === 0)
            {
                clearInterval(timerId);
                postReply();
            }
            document.getElementById("timerHere").innerHTML = myTime;
            myTime--;
        }
        
        function userReply()
        {
            clearInterval(timerId);
            postReply();
        }
        
        timerId = setInterval(updateTimer, 1000);
        </script>
    </head>
    <body >
        
        <jsp:include page="navBar.jsp"/>
        <h1>Interview : </h1>
        
        <%
            String interviews[] = ih.getInterviewTitle();
            out.println("<ul>");
            for(String s : interviews)
            {
                out.println("<li>" + s + "</li>");
            }
            out.println("<ul>");
            
        %>
        
        <div id ="divTimer">
            <table>
                <tr>
                    <td colspan="2" id="timerHere"></td>
                </tr>
            </table>
        </div>
        <div id="divTest">
            
        </div>
    </body>
</html>
