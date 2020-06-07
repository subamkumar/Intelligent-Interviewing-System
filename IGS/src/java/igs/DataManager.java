/*
A backend class that deals with the database.
It is currently connecting to oracle 11g express edition.
It uses ojdbc6.jar for java-oracle connection (driver).
It is coded as a singleton class.
It is accessed by the middle tier for data storage and retrieval.
For storage it takes data in the form of parameters.
For retrieval it returns the data in some object or data structure form.
*/
package igs;
import java.sql.*;
import java.util.LinkedList;

public class DataManager 
{
    private Connection conn;
    private static DataManager reference = null;
    
    private DataManager() throws Exception
    {
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
        String userName = "system";
        String password= "manager";
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        conn = DriverManager.getConnection(url, userName, password );
    }
    
    public static DataManager getInstance()
    {
        try
        {
            if(reference == null)
                reference = new DataManager();

            return reference;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return null;
        }
    }//getInstance

    //auto generation of the PK value
    //look up approach
    private int getNextId(String table, String column) throws Exception
    {
        String sql = "select max("+ column + ") from "+ table;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(rs.next())
        {//max fetched
            int id = rs.getInt(1);//1 means first column
            id++;//next id
            rs.close();
            stmt.close();
            return id;
        }
        else
        {//rs is empty ( table is empty)
            rs.close();
            stmt.close();
            return 1;//first value
        }
    }
    

/*
sys_user
(
 suid number primary key,
 suname varchar(50),
 suemail varchar(50),
 suphone varchar(20)
)
*/
    String [] getUserDetails(int suid)
    {
        try
        {
            String arr[] = null;
            String sql = "select suname, suemail, suphone from sys_user where suid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, suid);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                arr = new String[3];
                arr[0] = rs.getString(1);//suname
                arr[1] = rs.getString(2);//suemail
                arr[2] = rs.getString(3);//suphone
            }
            rs.close();
            
            return arr;
       }
        catch(Exception ex)
        {
            System.out.println(ex);
            return null;
        }
    }//setUserDetails
    

/*
usr_login
(
 suid number primary key references sys_user(suid),
 loginid varchar(30),
 loginpass varchar(30)
)    
 */   
    
    int validateLogin(String uname, String upass)
    {
        try
        {
            int id = -1;
            String sql = "select suid from usr_login where lower(loginid) = lower(?) and loginpass = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, uname);
            pstmt.setString(2, upass);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {//data row present
                id = rs.getInt(1);//get the suid 
            }
            rs.close();
            return id;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return -1;
        }
    }
    
/*
company
(
 cid number primary key,
 cname varchar(30),
 curl varchar(30),
 cdescription varchar(500) 
);
*/

    public boolean addCompany(String cName, String cUrl, String cDesc)  
    {
        try
        {
            int id = getNextId("company", "cid");
            String sql = "insert into company values(?,?,?,?)";
            
            //to avoid sql injection, as PreparedStatement gets the SQL compiled from the database before execution.
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);//first ? gets value of id
            pstmt.setString(2, cName);//second ? gets value of cName
            pstmt.setString(3, cUrl);//third ? gets value of cUrl
            pstmt.setString(4, cDesc);//fourth ? gets value of cDesc

            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addCompany


    public boolean deleteCompany(int cid)  
    {
        try
        {
            String sql = "delete from company where cid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteCompany
    
    LinkedList<String[]> getCompanies(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from company";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }
    
/*
interview
(
 iid number primary key,
 ititle varchar(100),
 idescription varchar(500),
 ipaypackage varchar(20),
 cid number references company(cid)
)

*/

    public boolean addInterview(String iTitle, String iDesc, String iPayPackage, int cid)  
    {
        try
        {
            int id = getNextId("interview", "iid");
            String sql = "insert into interview values(?,?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, iTitle);
            pstmt.setString(3, iDesc);
            pstmt.setString(4, iPayPackage);
            pstmt.setInt(5, cid);

            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addInterview


    public boolean deleteInterview(int iid)  
    {
        try
        {
            String sql = "delete from interview where iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteInterview
    
  
    public LinkedList<String[]> getInterviews(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select iid , ititle, idescription, ipaypackage, cname from interview inner join company on interview.cid = company.cid order by cname";
            //apply SQL paging here
            //String sql = "select * from ( select iid , ititle, idescription, ipaypackage, cname , row_number() over (order by cname) rn from interview inner join company on interview.cid = company.cid  ) where rn between ? and ? order by rn";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            //pstmt.setInt(1, from);
            //pstmt.setInt(2, to);
            
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }
    
    public String getInterviewName(int iid)
    {
        String iname = null;
            
        try
        {
            String sql = "select ititle, cname from interview inner join company on interview.cid = company.cid where iid = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
               iname = rs.getString(1).toUpperCase() + " (" +  rs.getString(2).toUpperCase() +  ")";
                
            }//if
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return iname;
    }        
    
/*
subject
(
 sid number primary key,
 sname varchar(30),
 sdescription varchar(500)
)

*/
 
    public boolean addSubject(String sName)  
    {
        try
        {
            int id = getNextId("subject", "sid");
            String sql = "insert into subject values(?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, sName);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addSubject


    public boolean deleteSubject(int sid)  
    {
        try
        {
            String sql = "delete from subject where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteSubject
    
    LinkedList<String[]> getSubjects(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from subject";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjects
 
    public String getSubjectName(int sid)
    {
        String name="";    
        try
        {
            //to apply SQL paging here
            String sql = "select sname from subject where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            if(rs.next())
            {
                name = rs.getString(1);
            }
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return name;
    }
    
    public int getSubjectScale(int sid, int iid)
    {
        int scale = -1;    
        try
        {
            //to apply SQL paging here
            String sql = "select scale from scaling  where sid = ? and iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            pstmt.setInt(2, iid);
            
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            if(rs.next())
            {
                scale = rs.getInt(1);
            }
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return scale;
    }
    
    public LinkedList<Integer> getSubjectIdsInterviewWise(int iid)
    {//sid
        LinkedList<Integer> rows = new LinkedList<Integer>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select sid from subject inner join scaling on subject.sid = scaling.sid where scaling.iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            //fetch resultset content
            while(rs.next())
            {
                rows.add(rs.getInt(1));
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjectIdsInterviewWise
    
    LinkedList<String[]> getSubjectsInterviewWise(int iid)
    {//subject.sid, sname, scale 
        
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select subject.sid, sname, scale from subject inner join scaling on subject.sid = scaling.sid where scaling.iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjectsInterviewWise
    
    /*
scaling
(
 scid number primary key,
 sid number references subject(sid),
 iid number references interview(iid),
 scale number
)
*/

 
    public boolean addScaling(int sid, int iid, double scale)  
    {
        try
        {
            int id = getNextId("scaling", "scid");
            String sql = "insert into scaling values(?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, sid);
            pstmt.setInt(3, iid);
            pstmt.setDouble(4, scale);
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addScaling


    public boolean deleteScaling(int scid)  
    {
        try
        {
            String sql = "delete from scaling where scid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteScaling
    
    LinkedList<String[]> getScaling(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from scaling";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getScaling
 
/*
topic
(
 tid number primary key,
 tname varchar(30),
 trank number,
 sid number references subject(sid)
)
*/
    public boolean addTopic(String tName, double tRank, int sid)  
    {
        try
        {
            int id = getNextId("topic", "tid");
            String sql = "insert into topic values(?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, tName);
            pstmt.setDouble(3, tRank);
            pstmt.setInt(4, sid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addTopic


    public boolean deleteTopic(int tid)  
    {
        try
        {
            String sql = "delete from topic where tid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteTopic

    public boolean updateTopicRank()
    {
        try
        {
            String sql1 = "Select tid from topic";
            String sql2 = "update topic set trank = ( select avg(qrank) from question where tid = ? ) where tid = ?";
            int tid;
            
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            
            ResultSet rs = pstmt1.executeQuery();
            
            while(rs.next())
            {
                tid = rs.getInt(1);//data of first col
                pstmt2.setInt(1, tid);
                pstmt2.setInt(2, tid);
                pstmt2.executeUpdate();
            }//while
            rs.close();
            pstmt1.close();
            pstmt2.close();
            return true;
        }
        catch(Exception ex)
        {
            System.out.println(ex); 
            return false;
        }
    }//updateTopicRank    
    
    LinkedList<String[]> getTopics(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from topic";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopics

    LinkedList<String[]> getTopicsSubjectwise(int subjectId)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select tid, tname, trank from topic where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopicsSubjectwise

    LinkedList<Topic> getTopicObjectsSubjectwise(int subjectId)
    {
        LinkedList<Topic> rows = new LinkedList<Topic>();
            
        try
        {
            String sql = "select tid, trank from topic where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            Topic t;
            while(rs.next())
            {
                //to hold data of this row of the resultset
                t = new Topic();
                t.setTid(rs.getInt(1));
                t.setRank(rs.getDouble(2));
                
                //put the array into the linkedlist
                rows.add(t);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopicObjectsSubjectwise

    
/*
question
(
 qid number primary key,
 qtext varchar(500),
 qca number,
 qrank number,
 tid number references topic(tid)
)
*/
    
    public boolean addQuestion(String qText, int qca, int qRank, int tid)  
    {
        try
        {
            int id = getNextId("question", "qid");
            String sql = "insert into question values(?,?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, qText);
            pstmt.setInt(3, qca);
            pstmt.setInt(4, qRank);
            pstmt.setInt(5, tid);
            
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addQuestion


    public boolean deleteQuestion(int qid)  
    {
        try
        {
            String sql = "delete from question where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteQuestion
    
    boolean updateQuestionRank(int qid, int val)
    {
        try
        {
            String sql = "update question set qrank = qrank + ? where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, val);
            pstmt.setInt(2, qid);
            
            int temp = pstmt.executeUpdate();
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }
    
    LinkedList<String[]> getQuestions(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from question";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQuestions

    
    LinkedList<LinkedList<String>> getQuestionsByTopic(int flag, int tid)
    {//qid, qtext, qrank, qca, qoid, qoption, qoid, qoption, ...
        //flag 1 : easy questions
        //flag 2 : moderate questions
        //flag 3 : difficult questions
        
        LinkedList<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();
            
        try
        {
            double min, avg, max;
            
            String sql0 = "select min(qrank), avg(qrank), max(qrank) from question where tid = ?";
            String sql1 = "select qid, qtext, qrank, qca from question where tid = ? and qrank between ? and ? order by qrank";
            String sql2 = "select qoid, qoption from qoption where qid = ?";
                    
            PreparedStatement pstmt0 = conn.prepareStatement(sql0);
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            
            ResultSet rs0 = pstmt0.executeQuery();
            rs0.next();
            min = rs0.getDouble(1);//30
            avg = rs0.getDouble(2);//50
            max = rs0.getDouble(3);//70
            
            pstmt1.setInt(1, tid);
            
            if(flag== 1)
            {//easy
                pstmt1.setDouble(2, min);//30
                pstmt1.setDouble(3, avg);//50
            }
            else if(flag== 2)
            {//moderate
                pstmt1.setDouble(2, avg - (avg-min)/2 );//40
                pstmt1.setDouble(3, avg + (max-avg)/2);//60
            }
            else if(flag== 3)
            {//difficult
                pstmt1.setDouble(2, avg );//50
                pstmt1.setDouble(3, max);//70
            }
            else
                return rows;
            
            
            ResultSet rs1 = pstmt1.executeQuery();
            ResultSet rs2;
            
            //fetch resultset content
            LinkedList <String> col;
            while(rs1.next())
            {
                col = new LinkedList<String>();
                col.add(rs1.getString(1));//qid
                col.add(rs1.getString(2));//qtext
                col.add(rs1.getString(3));//qrank
                col.add(rs1.getString(4));//qca
                
                //fetching options
                pstmt2.setInt(1, Integer.parseInt(col.get(1)));
                rs2 = pstmt2.executeQuery();
                
                while(rs2.next())
                {//qoid, qoption
                    col.add(rs2.getString(1));
                    col.add(rs2.getString(2));
                }
                
                rs2.close();
                rows.add(col);
            }//while
            
            rs1.close();
            pstmt1.close();
            pstmt2.close();
            
        }//while
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQuestionsByTopic

/*
qoption
(
 qoid number primary key,
 qoption varchar(500),
 qid number references question(qid) 
)
    
*/
    public boolean addQoption(String qOption, int qid)  
    {
        try
        {
            int id = getNextId("qoption", "qoid");
            String sql = "insert into qoption values(?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, qOption);
            pstmt.setInt(3,qid);
            
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addQoption


    public boolean deleteQoption(int qoid)  
    {
        try
        {
            String sql = "delete from question where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qoid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteQoption
    
    LinkedList<String[]> getQoptions(int from, int to)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            //to apply SQL paging here
            String sql = "select * from qoption";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQoptions

    
}