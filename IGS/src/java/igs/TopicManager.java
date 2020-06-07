package igs;

import java.util.*;
public class TopicManager 
{
    private static final int MAX_TOPIC_COUNT = 6;
    public static LinkedList<Integer> loadTopicsForSubject(int sid, int capacity)
    {
        DataManager dm = DataManager.getInstance();
        //tid, trank
        LinkedList<Topic> topics = dm.getTopicObjectsSubjectwise(sid);
        
        if(topics.size() > MAX_TOPIC_COUNT)
        {//go for KnapSack
            KnapSack ks = new KnapSack();
            topics = ks.resolveKnapSackProblem(topics, capacity);
        }
        
        LinkedList<Integer> testTopics = new LinkedList<Integer>();
        for(Topic t : topics)
        {
            testTopics.add(t.getTid());
        }
        return testTopics;
    }
}
