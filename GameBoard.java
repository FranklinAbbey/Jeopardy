/*
*  GamBoard.java develops a "Jeopardy" style UI game
*  using JavaFX
*
*  @authors
*       Franklin Abbey / C.Boettrich
*  @date
*       4 / 22 / 19
*/

//JAVAFX Imports
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.event.EventHandler;

//JAVA imports
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class GameBoard extends Application {

    //"Size" of game board is determined here
    private final int GAME_ROWS = 5;
    private final int GAME_COLUMNS = 6;
    //global JavaFX objects
    Pane root = new Pane();
    Pane aqPane = new Pane();
    GridPane grid = new GridPane();
    StackPane stackPane = new StackPane();
    Text pointLabel;
    ImageView image;
    //Added "BoxObject" class used to associated answer/questions
    //with specific boxes on the board
    BoxObject boxObj;
    ArrayList<BoxObject> boxList = new ArrayList<BoxObject>();
    //buttons used to advance through answer/questions
    Button btNext = new Button("Next");
    Button btClose = new Button("Close");
    //"rect" is referenced often and begins as the initial
    //size for each of the boxes on the board
    Rectangle rect = new Rectangle( 150, 100);

    private String framework = "embedded";
    private String protocol = "jdbc:derby:";
    String fileNames[] = {"columnA.txt", "columnB.txt", "columnC.txt", "columnD.txt", "columnE.txt", "columnF.txt", "columnG.txt", "columnH.txt"};
    public static Statement s;

    ArrayList<Category> jeopardyData = new ArrayList<Category>();

    public void start(Stage primaryStage) {
        //creating a new "LogicLayer" object to access the game data
        //LogicLayer logicLayer = new LogicLayer();
        //all txt files are put into an array
        try {
            jeopardyData = new GameBoard().go();
        }
        catch (IOException e) {
            System.out.println("Error: Problem with the input files!");
            System.exit(1); // terminates the program, code other than 0 indicates abend
        }

        //creating black "background"
        Rectangle background = new Rectangle(0,0, 1150, 800);
        background.setFill(Color.BLACK);

        //for loop creates, formats, and places the CATEGORY boxes at the
        //top of the board
        int index = 0;
        for(Category current : jeopardyData) {

            index++;
            Text columnLabel = new Text(current.getCategoryName());
            columnLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 26));
            columnLabel.setFill(Color.WHITE);
            columnLabel.setWrappingWidth(170);
            columnLabel.setTextAlignment(TextAlignment.CENTER);
            Rectangle topicBox = new Rectangle(170, 120);
            topicBox.setFill(Color.NAVY);
            grid.add(topicBox, index, 1);
            grid.add(columnLabel, index, 1);
        }
        //initialize "rect" fill color
        rect.setFill(Color.NAVY);

        //primary EventHandler to be used for animation.
        EventHandler<ActionEvent> eventHandlerRectangle = e -> {
            //expanding "rect"
            if(rect.getHeight() < 790)
                rect.setHeight(rect.getHeight() + 2);
            if(rect.getWidth() < 1090)
                rect.setWidth(rect.getWidth() + 2);
            //positioning "rect" in the upper left hand corner
            if(stackPane.getLayoutX() > 0)
                stackPane.setLayoutX(stackPane.getLayoutX() - 1);
            else if(stackPane.getLayoutX() < 0)
                stackPane.setLayoutX(stackPane.getLayoutX() + 1);
            if(stackPane.getLayoutY() > 0)
                stackPane.setLayoutY(stackPane.getLayoutY() - 1);
            else if(stackPane.getLayoutY() < 0)
                stackPane.setLayoutY(stackPane.getLayoutY() + 1);
            //once the animation is done, the last EventHandler call
            //will "turn on" the Next button and the image if needed
            else {
                btNext.setVisible(true);
                if(image != null)
                    image.setVisible(true);
            }
        };

        //Timeline/ Keyframe to be attached to EventHandler
        Timeline animationRectangle = new Timeline(
                new KeyFrame(Duration.millis(1), eventHandlerRectangle));
        animationRectangle.setCycleCount(Timeline.INDEFINITE);
        animationRectangle.pause();

        //loop structure creates and places boxes underneath each Category
        //"offset" is used to be able to count within a double loop
        int offset = 0;
        for(int k = 0; k < GAME_ROWS; k++) {

            for(int i = 0; i < GAME_COLUMNS; i++) {
                //placing point/ dollar values on each box
                if(jeopardyData.size() > 0) {
                    Integer pointValue = jeopardyData.get(i).getAQPair(k).getPointValue();
                    String pointString = pointValue.toString();
                    pointLabel = new Text("$" + pointString);
                    pointLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 50));
                    pointLabel.setFill(Color.YELLOW);

                    //BoxObject objects are created to associate each box with an answer, question, and image
                    boxObj = new BoxObject( 170, 120, jeopardyData.get(i).getAQPair(k).getAnswer(),
                            jeopardyData.get(i).getAQPair(k).getQuestion(), jeopardyData.get(i).getAQPair(k).getImage());
                    System.out.println(jeopardyData.get(i) + "5 - Printed");
                    boxObj.getBox().setFill(Color.NAVY);
                    boxList.add(boxObj);
                }
                final int total = offset + i;

                //event created to handle user clicking the actual "point label" instead of
                //the boxes
                if(jeopardyData.size() > 0) {

                    pointLabel.setOnMouseClicked(e -> {

                        aqPane.getChildren().clear();
                        //"getSceneX()" and "getSceneY()" allows access to the position
                        //of each individual mouse click
                        stackPane.setLayoutX(e.getSceneX() - 250);
                        stackPane.setLayoutY(e.getSceneY());
                        //display text
                        Text answer = boxList.get(total).getAnswer();
                        answer.setFont(new Font("ITC Korinna", 22));
                        answer.setFill(Color.WHITE);
                        stackPane.getChildren().add(answer);
                        aqPane.getChildren().addAll(stackPane, btNext);
                        aqPane.toFront();
                        //adding the image, if there is one

                        if (!(boxList.get(total).getImage().equalsIgnoreCase("none"))) {
                            image = new ImageView(new Image(boxList.get(total).getImage()));
                            image.setLayoutX(350);
                            image.setLayoutY(100);
                            aqPane.getChildren().add(image);
                            image.setVisible(false);
                        }

                        //implementing the function of the "Next" button for each
                        //"pointLabel" event
                        btNext.setOnAction(ev -> {
                            aqPane.getChildren().clear();
                            //displaying a new version of "rect"
                            rect = new Rectangle(1090, 790);
                            rect.setFill(Color.NAVY);
                            stackPane.getChildren().add(rect);
                            Text question = boxList.get(total).getQuestion();
                            question.setFont(new Font("ITC Korinna", 20));
                            question.setFill(Color.WHITE);
                            stackPane.getChildren().add(question);
                            //note: the "Close" button is added here but defined elsewhere
                            aqPane.getChildren().addAll(stackPane, btClose);
                        });

                        animationRectangle.play();

                    });


                    //add each box, along with it's corresponding "pointLabel"
                    grid.add(boxObj.getBox(), i + 1, k + 2);
                    grid.add(pointLabel, i + 1, k + 2);
                    grid.setHalignment(pointLabel, HPos.CENTER);

                }

            }
            //incrementing offset to keep an ordered count of processes
            offset += 6;

        }

        //loop through entire ArrayList of BoxObjects and assign their
        //individual actions
        for(int i = 0; i < boxList.size(); i++) {

            final int current = i;
            final Rectangle box = boxList.get(i).getBox();

            box.setOnMouseClicked(e -> {
                //grey the box to indicate it has been used
                box.setFill(Color.GREY);
                box.toBack();
                aqPane.getChildren().clear();
                stackPane.setLayoutX(e.getSceneX() - 250);
                stackPane.setLayoutY(e.getSceneY());
                //retrieve the text to be displayed
                Text answer = boxList.get(current).getAnswer();
                answer.setFont(new Font("ITC Korinna" , 22));
                answer.setFill(Color.WHITE);

                stackPane.getChildren().add(answer);
                aqPane.getChildren().addAll(stackPane, btNext);

                aqPane.toFront();
                //display the image, if there is one

                if(!(boxList.get(current).getImage().equalsIgnoreCase("none"))) {
                    image = new ImageView(new Image(boxList.get(current).getImage()));
                    image.setLayoutX(350);
                    image.setLayoutY(100);
                    aqPane.getChildren().add(image);
                    image.setVisible(false);
                }

                //define the actions of the "Next" button
                btNext.setOnAction(ev-> {
                    aqPane.getChildren().clear();
                    rect = new Rectangle(1090, 790);
                    rect.setFill(Color.NAVY);
                    stackPane.getChildren().add(rect);
                    Text question = boxList.get(current).getQuestion();
                    question.setFont(new Font("ITC Korinna" , 20));
                    question.setFill(Color.WHITE);
                    stackPane.getChildren().add(question);
                    //note: the "Close" button is added here but defined elsewhere
                    aqPane.getChildren().addAll(stackPane, btClose);

                });

                animationRectangle.play();

            });
        }
        //grid formatting
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //"Close" button functionality defined
        btClose.setOnAction(e-> {
            stackPane.getChildren().clear();
            aqPane.toBack();
            rect = new Rectangle( 150, 100);
            rect.setFill(Color.NAVY);
            stackPane.getChildren().add(rect);
            animationRectangle.pause();
        });

        btClose.setLayoutX(515);
        btClose.setLayoutY(600);
        btNext.setLayoutX(515);
        btNext.setLayoutY(600);
        btNext.setVisible(false);
        aqPane.getChildren().add(btNext);
        stackPane.getChildren().add(rect);
        aqPane.getChildren().add(stackPane);
        stackPane.toBack();

        root.getChildren().addAll(aqPane, background, grid);

        Scene scene = new Scene(root, 1090, 790);
        primaryStage.setTitle("Game Board");
        primaryStage.setScene(scene);
        primaryStage.show();

    }//ends start() method

    ArrayList<Category> go() throws IOException {

        Connection conn = null;
        ArrayList<Category> list = new ArrayList<>();
        ArrayList<Statement> statements = new ArrayList<Statement>();
        PreparedStatement psInsert;
        //Statement s;

        try {

            String dbName = "LogicLayerDB";
            conn = DriverManager.getConnection(protocol + dbName + ";create=true");
            conn.setAutoCommit(false);

            s = conn.createStatement();
            statements.add(s);
            //table is set up Category, point value, answer, question, image in each row
            s.execute("create table JeopardyInfoTable(category varchar(40), point int, answer varchar(200), question varchar(200), image varchar(100) )");

            psInsert = conn.prepareStatement("insert into JeopardyInfoTable values(?, ?, ?, ?, ?)");

            //for the length of the filenames array
            for(int k = 0; k < fileNames.length; k++) {
                File currentFile = new File(fileNames[k]);
                Scanner fileScanner = new Scanner(currentFile);
                // header consists of three blank lines and the category name
                for(int i = 0; i < 3; i++)
                    fileScanner.nextLine();

                //only get the name once, but insert multiple times
                String name = fileScanner.nextLine();

                //insert the category name for every AQPair, and therefore every row in the table
                while(fileScanner.hasNext()) {
                    psInsert.setString(1, name);
                    psInsert.setInt(2, Integer.parseInt(fileScanner.nextLine()));
                    psInsert.setString(3, fileScanner.nextLine());
                    psInsert.setString(4, fileScanner.nextLine());
                    psInsert.setString(5, fileScanner.nextLine());
                    psInsert.executeUpdate();
                }

            }

            Scanner input = new Scanner(System.in);
            int chosenAmount = 0;
            while(!(chosenAmount >= 6)) {
                String catName;
                System.out.println("Select from the list of categories: \n");
                //this will only select one of each category
                ResultSet resultSetA = s.executeQuery("SELECT category FROM JeopardyInfoTable WHERE point = 100");
                printResultSet(resultSetA, true);

                System.out.print("choice --> ");
                int choice = input.nextInt();
                chosenAmount++;
                switch (choice) {

                    case 1:
                        catName = "Inherit This";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 2:
                        catName = "Wu Reminds Us";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 3:
                        catName = "Numeric Data";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 4:
                        catName = "Java Details";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 5:
                        catName = "Classes and Methods";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 6:
                        catName = "Loops Anyone?";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 7:
                        catName = "Problem Solving";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                    case 8:
                        catName = "Wu! Wu! Wu!";
                        try {
                            //"getCategories" returns an ArrayList
                            getCategory(list, catName);
                        } catch (IOException e) {
                            System.out.println("Error: Problem with the input files!");
                            System.exit(1); // terminates the program, code other than 0 indicates abend
                        }
                        break;

                }

            }


        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;
    }

    private static void printResultSet(ResultSet resultSet, boolean addNumber) throws SQLException {

        int number = 0;
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        //System.out.println("Results of query ... ");

        while (resultSet.next()) {

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                number++;
                if (addNumber)
                    System.out.print(number + " ");
                System.out.println(resultSet.getObject(i) + " ");
            }
            System.out.println();

        }

        //System.out.println(" ...end of result set");

    }

    public static ArrayList<Category> getCategory(ArrayList<Category> list, String name) throws IOException {
        AQPair[] aqPairsArray = new AQPair[5];
        ResultSet resultSet = null;
        int point = 0;
        String answer = "";
        String question = "";
        String image = "";
        try{

            for(int k = 1; k < 6; k++) {

                resultSet = s.executeQuery("SELECT category, point, answer, question, image FROM JeopardyInfoTable WHERE category = '" + name + "' AND point = " + (k * 100));
                if(resultSet.next())
                    point = resultSet.getInt(2);
                resultSet = s.executeQuery("SELECT category, point, answer, question, image FROM JeopardyInfoTable WHERE category = '" + name + "' AND point = " + (k * 100));
                if(resultSet.next())
                    answer = resultSet.getString(3);
                resultSet = s.executeQuery("SELECT category, point, answer, question, image FROM JeopardyInfoTable WHERE category = '" + name + "' AND point = " + (k * 100));
                if(resultSet.next())
                    question = resultSet.getString(4);
                resultSet = s.executeQuery("SELECT category, point, answer, question, image FROM JeopardyInfoTable WHERE category = '" + name + "' AND point = " + (k * 100));
                if(resultSet.next())
                    image = resultSet.getString(5);

                AQPair currentAQPair = new AQPair(point, answer, question, image);
                aqPairsArray[k - 1] = currentAQPair;

            }

            list.add(new Category(name, aqPairsArray));

        }

        catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return list;

    }

} //ends GameBoard class
