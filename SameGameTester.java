import org.junit.*;
import static org.junit.Assert.*;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * test for the same game
 * @author Andrew Duffield
 */
public class SameGameTester {
  
  // 2D array of buttons for tests
  Button[][] buttons = new Button[4][4];
  Color r = Color.RED;
  Color g = Color.GREEN;
  Color b = Color.BLUE;
  
  /**
   * sets up 4 x 4 button array
   * r = red, g = green, b = blue
   *   0 1 2 3
   * 0 r r g g
   * 1 r b b b
   * 2 r g b b
   * 3 g g g g
   */
  public void setUpTest() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        buttons[i][j] = new Button();
        Circle c = new Circle(10);
        buttons[i][j].setGraphic(c);
      }
    }
    ((Circle)buttons[0][0].getGraphic()).setFill(r);
    ((Circle)buttons[1][0].getGraphic()).setFill(r);
    ((Circle)buttons[2][0].getGraphic()).setFill(g);
    ((Circle)buttons[3][0].getGraphic()).setFill(g);
    
    ((Circle)buttons[0][1].getGraphic()).setFill(r);
    ((Circle)buttons[1][1].getGraphic()).setFill(b);
    ((Circle)buttons[2][1].getGraphic()).setFill(b);
    ((Circle)buttons[3][1].getGraphic()).setFill(b);
    
    ((Circle)buttons[0][2].getGraphic()).setFill(r);
    ((Circle)buttons[1][2].getGraphic()).setFill(g);
    ((Circle)buttons[2][2].getGraphic()).setFill(b);
    ((Circle)buttons[3][2].getGraphic()).setFill(b);
    
    ((Circle)buttons[0][3].getGraphic()).setFill(g);
    ((Circle)buttons[1][3].getGraphic()).setFill(g);
    ((Circle)buttons[2][3].getGraphic()).setFill(g);
    ((Circle)buttons[3][3].getGraphic()).setFill(g);
  }
  
  /**
   * tests methods that get the number to the left, right, up and down of the same color
   */
  @Test
  public void testNumberDirection() {
    setUpTest();
    assertEquals(3, SameGame.getNumberLeft(new int[] {3, 3}, g, buttons));
    assertEquals(1, SameGame.getNumberLeft(new int[] {2, 1}, b, buttons));
    assertEquals(0, SameGame.getNumberLeft(new int[] {2, 0}, g, buttons));
    assertEquals(0, SameGame.getNumberLeft(new int[] {0, 0}, r, buttons));
    
    assertEquals(1, SameGame.getNumberRight(new int[] {0, 0}, r, buttons, 4));
    assertEquals(3, SameGame.getNumberRight(new int[] {0, 3}, g, buttons, 4));
    assertEquals(0, SameGame.getNumberRight(new int[] {1, 2}, g, buttons, 4));
    assertEquals(0, SameGame.getNumberRight(new int[] {3, 1}, r, buttons, 4));
    
    assertEquals(0, SameGame.getNumberAbove(new int[] {0, 0}, r, buttons));
    assertEquals(1, SameGame.getNumberAbove(new int[] {1, 3}, g, buttons));
    assertEquals(2, SameGame.getNumberAbove(new int[] {0, 2}, r, buttons));
    assertEquals(0, SameGame.getNumberAbove(new int[] {2, 1}, b, buttons));
    
    assertEquals(2, SameGame.getNumberBelow(new int[] {0, 0}, r, buttons, 4));
    assertEquals(1, SameGame.getNumberBelow(new int[] {2, 1}, b, buttons, 4));
    assertEquals(0, SameGame.getNumberBelow(new int[] {2, 2}, b, buttons, 4));
    assertEquals(0, SameGame.getNumberBelow(new int[] {2, 3}, g, buttons, 4));
  }
  
  /**
   * tests deleting horizonal buttons
   */
  @Test
  public void testHorizontalDeleting() {
    setUpTest();
    SameGame.deleteHorizontal(new int[] {2, 3}, 2, 1, buttons);
    assertEquals(SameGame.getButtonColor(buttons[0][0]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[0][3]), r);
    assertEquals(SameGame.getButtonColor(buttons[2][3]), g);
    assertEquals(SameGame.getButtonColor(buttons[3][3]), b);
    
    setUpTest();
    SameGame.deleteHorizontal(new int[] {0, 0}, 0, 1, buttons);
    assertEquals(SameGame.getButtonColor(buttons[1][0]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[1][1]), b);
    assertEquals(SameGame.getButtonColor(buttons[2][0]), g);
    
    setUpTest();
    Button[][] originalButtons = buttons;
    SameGame.deleteHorizontal(new int[] {1, 2}, 0, 0, buttons);
    assertTrue(buttons.equals(originalButtons));
  }
  
  /**
   * tests deleting vertical buttons
   */
  @Test
  public void testVerticalDeleting() {
    setUpTest();
    ((Circle)buttons[0][3].getGraphic()).setFill(r);
    /**
     * button array now:
     *   0 1 2 3
     * 0 r r g g
     * 1 r b b b
     * 2 r g b b
     * 3 r g g g
     */
    SameGame.deleteVertical(new int[] {0, 2}, 2, 1, buttons);
    assertEquals(SameGame.getButtonColor(buttons[0][3]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[0][1]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[2][0]), g);
    assertEquals(SameGame.getButtonColor(buttons[1][0]), r);
    assertEquals(SameGame.getButtonColor(buttons[1][2]), g);
    
    setUpTest();
    // back to original button array
    SameGame.deleteVertical(new int[] {2, 1}, 0, 1, buttons);
    assertEquals(SameGame.getButtonColor(buttons[2][1]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[2][0]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[1][2]), g);
    assertEquals(SameGame.getButtonColor(buttons[2][2]), g);
    assertEquals(SameGame.getButtonColor(buttons[3][2]), b);
    
    setUpTest();
    SameGame.deleteVertical(new int[] {2, 0}, 0, 0, buttons);
    assertEquals(SameGame.getButtonColor(buttons[2][0]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[2][1]), b);
    assertEquals(SameGame.getButtonColor(buttons[1][0]), r);
    assertEquals(SameGame.getButtonColor(buttons[3][0]), g);
    
  }
  
  /**
   * tests shifting buttons to left if a column is empty
   */
  @Test
  public void testLeftShfit() {
    setUpTest();
    ((Circle)buttons[1][0].getGraphic()).setFill(Color.LIGHTGREY);
    ((Circle)buttons[1][1].getGraphic()).setFill(Color.LIGHTGREY);
    ((Circle)buttons[1][2].getGraphic()).setFill(Color.LIGHTGREY);
    ((Circle)buttons[1][3].getGraphic()).setFill(Color.LIGHTGREY);
    /**
     * button array now:
     *   0 1 2 3
     * 0 r l g g
     * 1 r l b b
     * 2 r l b b
     * 3 g l g g
     * l = light grey
     */
    assertTrue(SameGame.isEmpty(1, buttons, 4));
    assertFalse(SameGame.isEmpty(2, buttons, 4));
    
    SameGame.leftShift(1, buttons, 4, 4);
    assertEquals(SameGame.getButtonColor(buttons[0][1]), r);
    assertEquals(SameGame.getButtonColor(buttons[1][1]), b);
    assertEquals(SameGame.getButtonColor(buttons[2][2]), b);
    assertEquals(SameGame.getButtonColor(buttons[3][1]), Color.LIGHTGREY);
    assertEquals(SameGame.getButtonColor(buttons[3][3]), Color.LIGHTGREY);
  }
  
  /**
   * tests getting the button location
   */
  @Test
  public void testButtonLoc() {
    setUpTest();
    int[] loc = SameGame.getButtonLocation(buttons[0][0], buttons, 4, 4);
    assertEquals(loc[0], 0);
    assertEquals(loc[1], 0);
    
    loc = SameGame.getButtonLocation(buttons[2][3], buttons, 4, 4);
    assertEquals(loc[0], 2);
    assertEquals(loc[1], 3);
  }
  
  
}