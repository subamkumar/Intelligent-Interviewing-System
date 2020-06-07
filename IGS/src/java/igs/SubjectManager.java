package igs;
import java.util.*;

public class SubjectManager 
{
    private static SubjectManager ref = null;
    LinkedList<Subject> manySubjects;

    private SubjectManager()
    {
        manySubjects = new LinkedList<Subject>();
    }
    
    LinkedList<Subject> allotSubjectsForInterview(Interview iv)
    {
        LinkedList<Integer> sids;
        LinkedList<Subject> allocation = new LinkedList<Subject>();
        DataManager dm = DataManager.getInstance();
        
        sids = dm.getSubjectIdsInterviewWise(iv.getIid());
        int q;
        for(Integer x : sids)
        {
            q =manySubjects.indexOf(x); 
            if( q != -1)
            {
                allocation.add(manySubjects.get(q));
            }
            else
            {//load the Subject
                Subject temp = new Subject();
                temp.setSid(x);
                manySubjects.add(temp);
                allocation.add(temp);
            }
        }
        
        return allocation;
    }
    
    public void reduceLoadedSubject(int sid)
    {
        int q;
        q =manySubjects.indexOf(sid); 
        if( q != -1)
            manySubjects.remove(q);
    
    }//reduceLoadedSubject
    
    public static SubjectManager getInstance()
    {
        if(ref == null)
            ref = new SubjectManager();
        return ref;
    }
    
}
