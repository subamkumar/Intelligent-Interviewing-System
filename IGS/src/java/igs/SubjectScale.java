package igs;

public class SubjectScale 
{
    int subjectId;
    String subjectName;
    double selfRate, companyRate, actualRate;
    
    SubjectScale(int sid, String sname,  double sysR)
    {
        subjectId = sid;
        subjectName = sname.toUpperCase();
        selfRate = 0;
        companyRate = sysR;
        actualRate = 0;
    }
    
    
    public String getSubjectName()
    {
        return subjectName;
    }
    
    public int getSubjectId()
    {
        return subjectId;
    }
    
    public double getCompanyScale()
    {
        return companyRate;
    }
    
    public double getActualScale()
    {
        return actualRate;
    }
    
    public void setActualScale(double x)
    {
        actualRate = x;
    }
    
    public void addToActualScale(double x)
    {
        actualRate += x;
    }
    
}
