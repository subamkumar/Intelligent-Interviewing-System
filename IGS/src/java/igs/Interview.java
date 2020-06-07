package igs;

import java.util.LinkedList;

public class Interview 
{
    int iid;
    
    public void setIid(int x)
    {
        iid =x;
    }
    
    public int getIid()
    {
        return iid;
    }
    
    public String[] fetchHeader()
    {
        String arr[] = {"Select", "Title", "Description", "Payment Package", "Company"};
        return arr;
    }
    
    public LinkedList<String[]> fetchInterviews()
    {//iid, ititle, idesc, ipayPack, cname
        return DataManager.getInstance().getInterviews(0, 0);
    }
    
}
