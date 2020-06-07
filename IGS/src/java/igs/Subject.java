package igs;

import java.util.LinkedList;

public class Subject implements Comparable<Integer>
{
    int sid;
    String name;
    Topic topics[];
    
    public void setSid(int x)
    {
        try
        {
            sid = x;
            loadNameAndTopics();
        }
        catch(Exception ex)
        {
            sid = 0;
            name = "";
            topics = null;
        }
    }
    
    public void loadNameAndTopics() throws Exception
    {
        /*
        DataManager dm = DataManager.getInstance();
        name = dm.getSubjectName(sid);
        LinkedList <String []> t = dm.getTopicsSubjectwise(sid);
        topics = new Topic[t.size()];
        int i;
        for(i =0; i < topics.length; i++)
        {
            topics[i] = new Topic(t.get(i));
        }
        */
    }
    
    public int getSid()
    {
        return sid;
    }
    
    public int compareTo(Integer x) 
    {
        return Integer.compare(sid, x.intValue());
    }
}
