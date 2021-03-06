/*
 *  AQPair.java provided by Professor C.Boettrich
 */
public class AQPair {
    private int pointValue;
    private String answer;
    private String question;
    private String image;

    public AQPair( )
    {
    }

    public AQPair(int pts, String ans, String qst, String img)
    {
        pointValue = pts;
        answer = ans;
        question = qst;
        image = img;
    }

    public int getPointValue( )
    {
        return pointValue;
    }

    public String getAnswer( )
    {
        return answer;
    }

    public String getQuestion( )
    {
        return question;
    }

    public String getImage( )
    {
        return image;
    }

    public String toString()
    {
        return ("\n Point Value: " + pointValue +
                "\n      Answer: " + answer +
                "\n    Question: " + question +
                "\n       Image: " + image);
    }
}
