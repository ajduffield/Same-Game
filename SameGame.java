import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.lang.UnsupportedOperationException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;

/**
 * class to run "plus" version of the same game
 * @author Andrew Duffield
 */
public class SameGame extends Application {
  
  // width of board
  private static int width;
  // height of board
  private static int height;
  // number of colors on board
  private static int numColors;
  
  // 2D array of buttons on board
  static Button[][] buttonsArray;
  // choices of colors on board
  Color[] colors = new Color[] {new Color(.644, .808, .964, 1), Color.RED, Color.GREEN, Color.YELLOW, 
    Color.ORANGE, Color.MAROON, Color.PINK, Color.WHITE};
  
  /**
   * the entry point to launch the app
   * @param primaryStage the primary stage for the application
   */
  @Override
  public void start(Stage primaryStage) {
    GridPane gridPane = new GridPane();
    // runs through 2D array of buttons, setting up each new button
    for (int i = 0; i < getWidth(); i++) {
      for (int j = 0; j < getHeight(); j++) {
        buttonsArray[i][j] = new Button();
        buttonsArray[i][j].setOnAction(new buttonPress());
        // circle to put on button
        Circle c = new Circle(10);
        c.setFill(colors[(int)(Math.random() * getNumColors())]);
        buttonsArray[i][j].setGraphic(c);
        gridPane.add(buttonsArray[i][j], i, j);
      }
    }
    // scene to put the grid pane on
    Scene scene = new Scene(gridPane);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
  
  /**
   * nested class for when a button is pressed
   */
  private class buttonPress implements EventHandler<ActionEvent> {
    /**
     * called when button is pressed
     * @param e event which occured
     */
    @Override
    public void handle(ActionEvent e) {
      // stores the button that was clicked
      Button b = (Button)e.getSource();
      // stores the location of the button clicked
      int[] loc = getButtonLocation(b, buttonsArray, getWidth(), getHeight());
      // stores the color of the button clicked
      Color c = getButtonColor(b);
      if (!c.equals(Color.LIGHTGREY)) {
        // stores number of same color buttons to the left
        int left = getNumberLeft(loc, c, buttonsArray);
        // stores number of same color buttons to the right
        int right = getNumberRight(loc, c, buttonsArray, getWidth());
        // stores number of same color buttons above
        int up = getNumberAbove(loc, c, buttonsArray);
        // stores number of same color buttons below
        int down = getNumberBelow(loc, c, buttonsArray, getHeight());
        if (left > 0 || right > 0 || up > 0 || down > 0) {
          deleteVertical(loc, up, down, buttonsArray);
          deleteHorizontal(loc, left, right, buttonsArray);
          // runs through all colums where buttons where deleted from to see if it is empty
          for (int i = loc[0] - left; i <= loc[0] + right; i++) {
            if (isEmpty(i, buttonsArray, getHeight())) {
              leftShift(i, buttonsArray, getWidth(), getHeight());
              i--;
              right--;
            }
          }
        }
      }
    }
  }
  
  /**
   * sets width of board
   * @param bWidth width
   */
  public static void setWidth(int bWidth) {
    width = bWidth;
  }
  
  /**
   * retuns width of board
   * @return width of board
   */
  public static int getWidth() {
    return width;
  }
  
  /**
   * sets height of board
   * @param bHeight height
   */
  public static void setHeight(int bHeight) {
    height = bHeight;
  }
  
  /**
   * returns height of board
   * @return height of board
   */
  public static int getHeight() {
    return height;
  }
  
  /**
   * sets number of colors on board
   * @param bNumColors number of colors
   */
  public static void setNumColors(int bNumColors) {
    numColors = bNumColors;
  }
  
  /**
   * returns number of colors on board
   * @return number of colors on board
   */
  public static int getNumColors() {
    return numColors;
  }
  
  /** 
    * returns number of same color buttons in a row to the left of the clicked button
    * @param loc location of button clicked
    * @param color color of button clicked
    * @param buttonArray 2D array of buttons
    * @return number of same color buttons in a row to the left of the clicked button
    */
  public static int getNumberLeft(int[] loc, Color color, Button[][] buttonArray) {
    // stores number of buttons counted in a row
    int num = 0;
    // runs through buttons to the left of the clicked button
    for (int i = loc[0] - 1; i >= 0; i--) {
      if (color.equals(getButtonColor(buttonArray[i][loc[1]]))) {
        num++;
      } else {
        return num;
      }
    }
    return num;
  }
  
  /** 
    * returns number of same color buttons in a row to the right of the clicked button
    * @param loc location of button clicked
    * @param color color of button clicked
    * @param buttonArray 2D array of buttons
    * @param width width of board
    * @return number of same color buttons in a row to the right of the clicked button
    */
  public static int getNumberRight(int[] loc, Color color, Button[][] buttonArray, int width) {
    // stores number of buttons counted in a row
    int num = 0;
    // runs through buttons to the right of the clicked button
    for (int i = loc[0] + 1; i < width; i++) {
      if (color.equals(getButtonColor(buttonArray[i][loc[1]]))) {
        num++;
      } else {
        return num;
      }
    }
    return num;
  }
  
  /** 
    * returns number of same color buttons in a row above the clicked button
    * @param loc location of button clicked
    * @param color color of button clicked
    * @param buttonArray 2D array of buttons
    * @return number of same color buttons in a row above the clicked button
    */
  public static int getNumberAbove(int[] loc, Color color, Button[][] buttonArray) {
    // stores number of buttons counted in a row
    int num = 0;
    // runs through buttons above the clicked button
    for (int i = loc[1] - 1; i >= 0; i--) {
      if (color.equals(getButtonColor(buttonArray[loc[0]][i]))) {
        num++;
      } else {
        return num;
      }
    }
    return num;
  }
  
  /** 
    * returns number of same color buttons in a row below the clicked button
    * @param loc location of button clicked
    * @param color color of button clicked
    * @param buttonArray 2D array of buttons
    * @param height height of board
    * @return number of same color buttons in a row below the clicked button
    */
  public static int getNumberBelow(int[] loc, Color color, Button[][] buttonArray, int height) {
    // stores number of buttons counted in a row
    int num = 0;
    // runs through buttons below the clicked button
    for (int i = loc[1] + 1; i < height; i++) {
      if (color.equals(getButtonColor(buttonArray[loc[0]][i]))) {
        num++;
      } else {
        return num;
      }
    }
    return num;
  }
  
  /**
   * moves all buttons in a column down one row
   * @param loc location of button to be deleted
   * @param buttonArray 2D array of buttons
   */
  public static void moveDownOne(int[] loc, Button[][] buttonArray) {
    // runs through all buttons above the one deleted
    for (int i = loc[1] - 1; i >= 0; i--) {
      setButtonColor(buttonArray[loc[0]][i + 1], getButtonColor(buttonArray[loc[0]][i]));
    }
    setButtonColor(buttonArray[loc[0]][0], Color.LIGHTGREY);
  }
  
  /**
   * deletes buttons to the left and right of a button
   * @param loc location of button clicked
   * @param numLeft number of buttons to be deleted to the left of the clicked button
   * @param numRight number of buttons to be deleted to the right of the clicked button
   * @param buttonArray 2D array of buttons
   */
  public static void deleteHorizontal(int[] loc, int numLeft, int numRight, Button[][] buttonArray) {
    // runs through all same color buttons to the left of the deleted button
    // moves ones above deleted button down
    for (int i = loc[0] - 1; i >= loc[0] - numLeft; i--) {
      moveDownOne(new int[] {i, loc[1]}, buttonArray);
    }
    // runs through all same color buttons to the right of the deleted button
    // moves ones above deleted button down
    for (int i = loc[0] + 1; i <= loc[0] + numRight; i++) {
      moveDownOne(new int[] {i, loc[1]}, buttonArray);
    }
  }
  
  /**
   * deletes buttons above and below a button
   * @param loc location of button clicked
   * @param numUp number of buttons to be deleted above the clicked button
   * @param numDown number of buttons to be deleted below the clicked button
   * @param buttonArray 2D array of buttons
   */
  public static void deleteVertical(int[] loc, int numUp, int numDown, Button[][] buttonArray) {
    // runs through same color buttons above and below the deleted button
    // moves ones above the group down
    for (int i = loc[1] - numUp; i <= loc[1] + numDown; i++) {
      moveDownOne(new int[] {loc[0], i}, buttonArray);
    }
  }
  
  /**
   * checks to see if column is empty
   * @param column column to check if empty
   * @param buttonArray 2D array of buttons
   * @param height height of board
   * @return boolean if shift is needed
   */
  public static boolean isEmpty(int column, Button[][] buttonArray, int height) {
    // keeps track of if the column is empty
    boolean shift = true;
    // runs through all buttons in a column to see if it is empty
    for (int i = 0; i < height; i++) {
      if (!getButtonColor(buttonArray[column][i]).equals(Color.LIGHTGREY)) {
        shift = false;
      }
    }
    return shift;
  }
  
  /**
   * delets a column and shifts all others to the left
   * @param column to be deleted
   * @param buttonArray 2D array of buttons
   * @param width width of board
   * @param height height of board
   */
  public static void leftShift(int column, Button[][] buttonArray, int width, int height) {
    // runs through all buttons to the right of an empty column, moving them to the left
    for (int i = column + 1; i < width; i++) {
      for (int j = 0; j < height; j++) {
        setButtonColor(buttonArray[i-1][j], getButtonColor(buttonArray[i][j]));
      }
    }
    // runs through all buttons in the last column, turning them light grey
    for (int j = 0; j < height; j++) {
      setButtonColor(buttonArray[width - 1][j], Color.LIGHTGREY);
    }
  }
  
  /**
   * returns location of a button
   * @param b button to find location of
   * @param buttonArray 2D array of buttons
   * @param width width of board
   * @param height height of board
   * @return array of column and row
   */
  public static int[] getButtonLocation(Button b, Button[][] buttonArray, int width, int height) {
    // runs through all buttons to find the location of the clicked button
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (b.equals(buttonArray[i][j])) {
          return new int[] {i, j};
        }
      }
    }
    return null;
  }
  
  /**
   * returns color of button
   * @param b button to find color of
   * @return color of button
   */
  public static Color getButtonColor(Button b) {
    return (Color)((Circle)b.getGraphic()).getFill();
  }
  
  /**
   * sets color of button
   * @param b button to set to color
   * @param c color to set button to
   */
  public static void setButtonColor(Button b, Color c) {
    ((Circle)b.getGraphic()).setFill(c);
  }
  
  /**
   * main method
   * @param args arguments from the command line
   */
  public static void main(String[] args) {
    // thread to allow for testing
    Thread thread = new Thread() {
      public void run() {
        try {
          if (args.length == 3) {
            setWidth(Integer.parseInt(args[0]));
            setHeight(Integer.parseInt(args[1]));
            setNumColors(Integer.parseInt(args[2]));
          } else if (args.length == 0) {
            setWidth(12);
            setHeight(12);
            setNumColors(3);
          } else {
            throw new UnsupportedOperationException();
          }
          if (getWidth() > 30 || getHeight() > 20 || getNumColors() > 8 ||
              getWidth() < 1 || getHeight() < 1 || getNumColors() < 1 ) {
            throw new IllegalArgumentException();
          }
          buttonsArray = new Button[getWidth()][getHeight()];
          launchApp(args);
        }
        catch (IllegalArgumentException e) {
          System.out.println("Unable to launch: Required width range: 1 - 30. Required height " + 
                             "range: 1 - 20. Required colors range: 1 - 8.");
        }
        catch (UnsupportedOperationException e) {
          System.out.println("Unable to launch: Must declare either 0 or 3 values.");
        }
      }
    };
    thread.start();
  }
  
  /**
   * launches the app
   * @param args arguments from the command line
   */
  public static void launchApp(String[] args) {
    Application.launch(args);
  }
}