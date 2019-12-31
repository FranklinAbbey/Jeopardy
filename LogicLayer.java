/*
*  LogicLayer.java provided by Professor C.Boettrich
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LogicLayer {

    public static ArrayList<Category> getCategories (String[] inFiles) throws IOException {
        ArrayList<Category> gameData = new ArrayList<>( );
        for (String inputFileName : inFiles) // for each file, create a Category
        {
            File inFile = new File(inputFileName);
            Scanner fileScanner = new Scanner(inFile);
            String categoryName = readHeader(fileScanner);
            AQPair[] aQPs = readAQs(fileScanner);
            Category aCategory = new Category(categoryName, aQPs);
            gameData.add(aCategory);
        }
        return gameData;
    }

    public static String readHeader(Scanner fileScanner)
    {
        for(int i = 0; i < 3; i++)    // header consists of three blank lines and the category name
            fileScanner.nextLine();
        return fileScanner.nextLine(); // returns the Category name
    }

    public static AQPair[] readAQs(Scanner fileScanner)
    {
        final int AQ_PAIR_COUNT = 5;
        AQPair[] aQPs = new AQPair[AQ_PAIR_COUNT];
        for(int i = 0; i < AQ_PAIR_COUNT; i++)
        {
            aQPs[i] = new AQPair(Integer.parseInt(fileScanner.nextLine()), // read the point value
                                 fileScanner.nextLine(),  // read the answer
                                 fileScanner.nextLine(),  // read the question
                                 fileScanner.nextLine()); // read image file name
        }
        return aQPs;
    }
}
