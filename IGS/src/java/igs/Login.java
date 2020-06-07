package igs;

public class Login 
{
    private String loginId, loginPass;
    
    public void setLoginId(String x)
    {
        loginId = x;
    }
    
    public void setLoginPass(String x)
    {//encryption possible here
        loginPass = x;
    }

    public int authenticate()
    {
        return DataManager.getInstance().validateLogin(loginId, loginPass);
    }
}
