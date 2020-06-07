package igs;

import java.util.LinkedList;

public class Topic implements Comparable<Topic>
{
    int tid;
    double rank, weight;
    
    public void setTid(int x)
    {
        tid = x;
    }
    
    public void setRank(double r)
    {
        rank = r;
    }
    
    public void setWeight(double w)
    {
        weight = w;
    }
    
    public int getTid()
    {
        return tid;
    }
    
    public double getRank()
    {
        return rank;
    }
    
    public double getRatio()
    {
        return rank/weight;
    }
    
    public int compareTo(Topic t) 
    {
        return Double.compare(rank, t.rank);
    }
}
