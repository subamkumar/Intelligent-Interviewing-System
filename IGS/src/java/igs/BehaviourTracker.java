package igs;
import java.util.LinkedList;

public class BehaviourTracker 
{
    LinkedList<ResponseMetaData> listRSMD;
    
    public BehaviourTracker() 
    {
        listRSMD = new LinkedList<ResponseMetaData>();
    }
    
    void updateMetaData(double tt, double s, double qr)
    {
        listRSMD.add(new ResponseMetaData(tt, s, qr));
    }
    
    
    
}
