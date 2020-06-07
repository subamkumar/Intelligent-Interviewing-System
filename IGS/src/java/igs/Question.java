package igs;

public class Question 
{
    String text;
    String optionAnswers[];
    String correctAnswer;
    int rank;
    
    Question(String tx, String oa[], String ca, int r)
    {
        text = tx;
        optionAnswers = oa;
        correctAnswer = ca;
        rank = r;
    }
}
