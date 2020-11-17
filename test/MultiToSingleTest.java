import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.function.MultiToSingle;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests the class {@code MultiToSingle}.
 */
public class MultiToSingleTest {

  private MultiToSingle func = new MultiToSingle();

  @Test
  public void singleCellRange() {
    List<SingleReference> list = new ArrayList<>();
    list.add(new SingleReference("A1"));

    assertEquals(list, func.evaluate(new MultiReference("A1:A1")));
  }

  @Test
  public void singleColumnRange() {
    List<SingleReference> list = new ArrayList<>();
    list.add(new SingleReference("A1"));
    list.add(new SingleReference("A2"));
    list.add(new SingleReference("A3"));

    assertEquals(list, func.evaluate(new MultiReference("A1:A3")));
  }

  @Test
  public void singleRowRange() {
    List<SingleReference> list = new ArrayList<>();
    list.add(new SingleReference("A1"));
    list.add(new SingleReference("B1"));
    list.add(new SingleReference("C1"));

    assertEquals(list, func.evaluate(new MultiReference("A1:C1")));
  }

  @Test
  public void rangeReference1() {
    List<SingleReference> list = new ArrayList<>();
    list.add(new SingleReference("A1"));
    list.add(new SingleReference("A2"));
    list.add(new SingleReference("A3"));
    list.add(new SingleReference("B1"));
    list.add(new SingleReference("B2"));
    list.add(new SingleReference("B3"));
    list.add(new SingleReference("C1"));
    list.add(new SingleReference("C2"));
    list.add(new SingleReference("C3"));

    assertEquals(list, func.evaluate(new MultiReference("A1:C3")));
  }
}
