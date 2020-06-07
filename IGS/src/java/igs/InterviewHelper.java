package igs;

import java.util.*;
import org.json.*;

public class InterviewHelper 
{
    private int intrws[];
    HashMap<Integer, SubjectScale> allSubjectsScale;
    LinkedList<Integer> currentSubjectTopics;
    LinkedList<Question> portionA, portionB;
    LinkedList<Integer> keys;        
    LinkedList<String> currentTopicQuestions;
    
    public void setIntrws(int arr[])
    {
        intrws = arr;
    }
    
    public void setSelfScale(String s)
    {
        try
        {
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.getJSONArray("scaling");
            Iterator itr;
            int  k;
            
            for(int i =0; i < array.length(); i++)
            {
                itr = array.getJSONObject(i).keys();
                if(itr.hasNext())
                {
                    k = Integer.parseInt(itr.next().toString());
                    if(allSubjectsScale.containsKey(k))
                        allSubjectsScale.get(k).selfRate = array.getJSONObject(i).getDouble(String.valueOf(k));
                }
            }//for
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    double getCompanyScaleForSubject(int s)
    {
        if(allSubjectsScale.containsKey(s))
        {
            return allSubjectsScale.get(s).getCompanyScale();
        }
        else
        {
            return -1;
        }
    }
   
    public boolean evaluateSelfScale()
    {
        try
        {
            Collection<SubjectScale> c = allSubjectsScale.values();
            for(SubjectScale ss : c)
            {
                if(ss.companyRate > ss.selfRate)
                    return false;
            }
            return true;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }
    
    private int getNextSubject()
    {
        if(keys.size() >0 )
            return keys.remove();
        return -1;
    }

    public boolean loadTopicsForSubject()
    {
        int id = getNextSubject();
        if(id == -1)
            return false;
        int capacity = (int)Math.round(allSubjectsScale.get(id).companyRate );
        
        currentSubjectTopics = TopicManager.loadTopicsForSubject(id, capacity);
        return true;
    }
    
    public boolean loadQuestionForTopic(int flag)
    {
        try
        {
            int id = currentSubjectTopics.remove(0);
            if(flag == 1)
                currentTopicQuestions = QuestionManager.setBasicSet(id);
            else if(flag == 2)
                currentTopicQuestions = QuestionManager.setModerateSet(id);
            else
                return false;
            
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    public String[] getInterviewTitle()
    {
        String interviews[] = new String[intrws.length];
        
        int i;
        for( i =0 ; i <  intrws.length; i++)
        {
            interviews[i] = DataManager.getInstance().getInterviewName(intrws[i]);
        }
        
        return interviews;        
    }
    
    public String[] fetchHeader()
    {
        String arr[] = { "Subject", "Scale(1-10)"};
        return arr;
    }
    
    public HashMap<Integer, SubjectScale> getSubjectsInterviewWise()
    {
        return getSubjectsInterviewWise(intrws);
    }   
    
    public HashMap<Integer, SubjectScale> getSubjectsInterviewWise(int arr[])
    {
        LinkedList<LinkedList<String[]>> allInterviews = new LinkedList<LinkedList<String[]>>();
        DataManager dm = DataManager.getInstance();
        
        for(int x : arr)//each interview
        {
            allInterviews.add(dm.getSubjectsInterviewWise(x));
        }
        
        LinkedList<String[]> subjects = reduceCommonSubjects(allInterviews);
        
        allSubjectsScale = new HashMap<Integer, SubjectScale>();
        keys = new LinkedList<Integer>();
        
        for(String s[] : subjects)
        {
            allSubjectsScale.put(Integer.parseInt(s[0]), new SubjectScale(Integer.parseInt(s[0]), s[1], Double.parseDouble(s[2])));
            keys.add(Integer.parseInt(s[0]));
        }
        
        return allSubjectsScale;
    }
    
    LinkedList<String[]> reduceCommonSubjects(LinkedList<LinkedList<String[]>> allInterviews)
    {
        LinkedList<String[]> set1 = allInterviews.remove();
        
        LinkedList<String[]> current;
        
        while(allInterviews.size() >0)
        {
            current = allInterviews.remove();
            union(set1, current);
        }
        return set1;
    }//reduceCommonSubjects
    
    void union(LinkedList<String[]> a, LinkedList<String[]> b)
    {
        //unique elements of set b to be copied into set a
        int flag;
        for(String arr[] : b)
        {
            flag = search(arr, a);
            if(flag == -1)
                a.add(arr);
            else
            {//scale manage : arr[2]
                if(Double.parseDouble(arr[2]) > Double.parseDouble(a.get(flag)[2]))
                    a.get(flag)[2]= arr[2];
            }
        }
    }
    
    int search(String arr[], LinkedList<String []> a)
    {
        int i;
        
        for(i =0; i < a.size(); i++)
        {
            if(a.get(i)[0].equals(arr[0]))//sid match
                return i;
        }
        return -1;
    }
}
