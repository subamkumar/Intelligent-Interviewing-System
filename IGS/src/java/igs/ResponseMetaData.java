package igs;

public class ResponseMetaData 
{
    private double timeTaken;
    private double score;
    private double qRank;
    
    public ResponseMetaData(double tt, double s, double qr)
    {
        setTimeTaken(tt);
        setQRank(qr);
        setScore(s);
    }
    
    void setTimeTaken(double tt)
    {
        timeTaken = tt;
    }
    
    void setScore(double s)
    {
        score = s;
    }
    
    void setQRank(double qr)
    {
        qRank = qr;
    }
    
    double getTimeTaken()
    {
        return timeTaken;
    }
    
    double getScore()
    {
        return score;
    }
        
    double getQRank()
    {
        return qRank;
    }

}//responseMetaData
