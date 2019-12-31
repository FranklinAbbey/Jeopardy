/*
 *  Category.java provided by Professor C.Boettrich
 */
public class Category {
    private String categoryName;
    private AQPair[] aQPairs;

    public Category( )
    {
    }

    public Category(String cat, AQPair[] aQPs )
    {
        categoryName = cat;
        aQPairs = aQPs;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public AQPair getAQPair(int i)
    {
        return aQPairs[i];
    }

    public AQPair[] getAQPairs()
    {
        return aQPairs;
    }

    public String toString()
    {
        String gameData = "GameData:\n" + "---- Category ---- " + categoryName;
        for(AQPair aqPair : aQPairs)
            gameData += aqPair;
        return gameData;
    }
}
