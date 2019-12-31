/*
 *  BoxObject associates a Rectangle shape with
 *  the necessary information for GameBoard.java
 *
 *  @author
 *       Franklin Abbey
 *  @date
 *       4 / 22 / 19
 */
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BoxObject {

    private Rectangle box;
    private Text answer;
    private Text question;
    private String image;

    /*
    *  No-argument constructor initializes the size of the Rectangle
    *  and all other data members
     */
    public BoxObject() {
        box = new Rectangle(150, 100);
        answer = new Text("DEFAULT");
        question = new Text("DEFAULT");
        image = "none";
    }

    /*
     *  Constructor initializes all data members to
     *  given parameters (note: image is a String representing
     *  a file address).
     *
     *  @param int wide, int high, String line1, String line2, String line3
     *      wide - width of Rectangle
     *      high - height of Rectangle
     *      line1 - answer text
     *      line2 - question text
     *      line3 - image file address
     */
    public BoxObject(int wide, int high, String line1, String line2, String line3) {
        box = new Rectangle(wide, high);
        answer = new Text(line1);
        question = new Text(line2);
        image = line3;
    }

    /*
    *  Accessor method returns the Rectangle shape
    *
    *  @returns Rectangle box
    *       a Rectangle object
     */
    public Rectangle getBox() {
        return box;
    }

    /*
     *  Accessor method returns the object's
     *  "answer"
     *
     *  @returns Text answer
     *       the individual Rectangle's answer
     */
    public Text getAnswer() {
        return answer;
    }

    /*
     *  Accessor method returns the object's
     *  "answer"
     *
     *  @returns Text question
     *       the individual Rectangle's question
     */
    public Text getQuestion() {
        return question;
    }

    /*
     *  Accessor method returns the image file address
     *
     *  @returns String image
     *       a String representing an image file address
     */
    public String getImage() {
        return image;
    }

}

