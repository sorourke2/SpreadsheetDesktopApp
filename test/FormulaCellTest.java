import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.value.EmptyCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;
import org.junit.Test;

/**
 * Tests the different formulas in a cell.
 */
public class FormulaCellTest {

  private Cell cell;

  @Test(expected = UnsupportedOperationException.class)
  public void formulaEvaluateUnsupported() {
    cell = CellFactory.create("=1");
    cell.evaluate();
  }

  @Test
  public void valueFormula() {
    cell = CellFactory.create("=1");
    assertEquals(new EmptyCellValue(), cell.getCellValue());
    assertEquals(new NumberCellValue(1), cell.evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(1), cell.getCellValue());
    assertEquals("=1", cell.getUserString());
    assertTrue(cell.isFormula());
    cell.setError(new SelfReferentialError());
    assertEquals(new SelfReferentialError(), cell.getCellValue());
    cell.evaluate(new ReferentialGraph<>());
    assertEquals(new NumberCellValue(1), cell.getCellValue());

    Cell cell = CellFactory.create("=1");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());
    cell.evaluate(new ReferentialGraph<>());
    assertEquals(this.cell, cell);
    assertEquals(this.cell.hashCode(), cell.hashCode());
  }

  @Test
  public void referenceFormula() {
    Graph<Coord, Cell> cells = new ReferentialGraph<>();

    cell = CellFactory.create("=A1");
    cells.addNode(Coord.strToCoord("A1"), CellFactory.create("1"));
    assertEquals(new EmptyCellValue(), cell.getCellValue());
    assertEquals(new NumberCellValue(1), cell.evaluate(cells));
    assertEquals(new NumberCellValue(1), cell.evaluate(cells));
    assertEquals("=A1", cell.getUserString());
    assertTrue(cell.isFormula());
    cell.setError(new SelfReferentialError());
    assertEquals(new SelfReferentialError(), cell.getCellValue());
    cell.evaluate(cells);
    assertEquals(new NumberCellValue(1), cell.getCellValue());

    Cell cell = CellFactory.create("=A1");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());
    cell.evaluate(cells);
    assertEquals(this.cell, cell);
    assertEquals(this.cell.hashCode(), cell.hashCode());
  }
}
