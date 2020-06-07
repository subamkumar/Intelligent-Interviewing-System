<%@page import="igs.SubjectScale"%>
<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>


<%
    String resp = "{\"qtext\":\"What is a constructor?\",\"qid\":7,\"qRank\":1,\"qoptions\":[  \"17\", \"A special Method of The Class\", \"18\", \"An Object Initializer\", \"19\", \"Both A and B\" ]}";
    out.println(resp);

%>