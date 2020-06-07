package igs;

import java.util.*;

public class QuestionManager 
{
    private static Random rnd = new Random();
    
    private static String generateJSONString(LinkedList<String> l)
    {
        //qid, qtext, qrank, qca, qoid, qoption, qoid, qoption, ...
        //"{\"qtext\":\"What is a constructor?\",\"qid\":7,\"qRank\":1,\"qoptions\":[  \"17\", \"A special Method of The Class\", \"18\", \"An Object Initializer\", \"19\", \"Both A and B\" ]}";
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("{\"qtext\":");
        sbuff.append(l.remove(1));
        sbuff.append("?\",\"qid\":");
        sbuff.append(l.remove(0));
        sbuff.append(",\"qRank\":");
        sbuff.append(l.remove(0));
        sbuff.append(",\"qoptions\":[");
        
        
        while(l.size() > 1)
        {
            sbuff.append("\"");
            sbuff.append(l.remove(1));
            sbuff.append("\"");
            
            if(l.size() >1)
                sbuff.append(",");
        }
        
        sbuff.append("]}");
        return sbuff.toString();
     
    }
    
    static LinkedList<String> setBasicSet(int tid)
    {//Portion A : 2,4,4 and Portion B : 4,6,6
        DataManager dm = DataManager.getInstance();
        LinkedList<LinkedList<String>> easy = dm.getQuestionsByTopic(1, tid);
        LinkedList<LinkedList<String>> moderate = dm.getQuestionsByTopic(2, tid);        
        LinkedList<LinkedList<String>> difficult = dm.getQuestionsByTopic(3, tid);

        //1 easy question
        LinkedList<String> e1 = easy.remove(rnd.nextInt(easy.size()));
        //3 moderate questions
        LinkedList<String> m1 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m2 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m3 = moderate.remove(rnd.nextInt(moderate.size()));
        //2 difficult questions
        LinkedList<String> d1 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d2 = difficult.remove(rnd.nextInt(difficult.size()));
        
        
        LinkedList<String> portion = new LinkedList<String>();
        //portion A
        portion.add( generateJSONString(e1));
        portion.add( e1.remove(0));
        portion.add( generateJSONString(m1));
        portion.add( m1.remove(0));
        portion.add( generateJSONString(m2));
        portion.add( m2.remove(0));
        
        //portion B
        portion.add( generateJSONString(m3));
        portion.add( m3.remove(0));
        portion.add( generateJSONString(d1));
        portion.add( d1.remove(0));
        portion.add( generateJSONString(d2));
        portion.add( d2.remove(0));
        
        return portion;
    }
    
    static LinkedList<String > setModerateSet(int tid)
    {//Portion A : 4,4,4 and Portion B : 6,6,6
        DataManager dm = DataManager.getInstance();
        LinkedList<LinkedList<String>> moderate = dm.getQuestionsByTopic(2, tid);        
        LinkedList<LinkedList<String>> difficult = dm.getQuestionsByTopic(3, tid);

        //3 moderate questions
        LinkedList<String> m1 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m2 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m3 = moderate.remove(rnd.nextInt(moderate.size()));
        //3 difficult questions
        LinkedList<String> d1 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d2 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d3 = difficult.remove(rnd.nextInt(difficult.size()));

        
        LinkedList<String> portion = new LinkedList<String>();
        //portion A
        portion.add( generateJSONString(m1));
        portion.add( m1.remove(0));
        portion.add( generateJSONString(m2));
        portion.add( m2.remove(0));
        portion.add( generateJSONString(m3));
        portion.add( m3.remove(0));
        
        //portion B
        portion.add( generateJSONString(d1));
        portion.add( d1.remove(0));
        portion.add( generateJSONString(d2));
        portion.add( d2.remove(0));
        portion.add( generateJSONString(d3));
        portion.add( d3.remove(0));
        
        return portion;

    }
    
}
