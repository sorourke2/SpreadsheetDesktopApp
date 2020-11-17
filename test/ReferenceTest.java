import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import org.junit.Test;

/**
 * Tests cell references.
 */
public class ReferenceTest {

  private Formula cellRef;

  @Test
  public void singleReference() {
    cellRef = new SingleReference("A1");
    assertEquals("A1", cellRef.getCellRef());

    Graph<Coord, Cell> cells = new ReferentialGraph<>();
    cells.addNode(Coord.strToCoord("A1"), CellFactory.create("1"));

    assertEquals(new NumberCellValue(1),
        cellRef.accept(new EvaluateFormula(cells)));

    assertFalse(cellRef.isCellValue());
    assertFalse(cellRef.isFunction());
    assertFalse(cellRef.isMultiRef());
    assertTrue(cellRef.isReference());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void singleReferenceCompute() {
    cellRef = new SingleReference("A1");
    cellRef.compute(new EvaluateFormula(new ReferentialGraph<>()));
  }

  @Test
  public void multiReference() {
    cellRef = new MultiReference("A1:C1");
    assertEquals("A1:C1", cellRef.getCellRef());

    assertEquals(new EvaluationError(),
        cellRef.accept(new EvaluateFormula(new ReferentialGraph<>())));

    assertFalse(cellRef.isCellValue());
    assertFalse(cellRef.isFunction());
    assertTrue(cellRef.isMultiRef());
    assertTrue(cellRef.isReference());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void multiReferenceCompute() {
    cellRef = new MultiReference("A1:C1");
    cellRef.compute(new EvaluateFormula(new ReferentialGraph<>()));
  }
}
