import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import org.junit.Test;

/**
 * Tests the primitive cell class.
 */
public class PrimitiveCellTest {

  private Cell cell;

  @Test
  public void booleanCell() {
    cell = CellFactory.create("true");
    assertEquals(new BooleanCellValue(true), cell.evaluate());
    assertEquals(new BooleanCellValue(true), cell.evaluate(new ReferentialGraph<>()));
    assertEquals(new BooleanCellValue(true), cell.getCellValue());
    assertEquals("true", cell.getUserString());
    assertFalse(cell.isFormula());
    try {
      cell.setError(new SelfReferentialError());
    } catch (UnsupportedOperationException e) {
      assertEquals(new BooleanCellValue(true), cell.getCellValue());
    }
    Cell cell = CellFactory.create("true");
    assertEquals(this.cell, cell);
    assertEquals(this.cell.hashCode(), cell.hashCode());

    cell = CellFactory.create("false");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());
  }

  @Test
  public void numberCell() {
    Cell numCell1 = CellFactory.create("1");
    Cell numCell2 = CellFactory.create("1/1");
    Cell numCell3 = CellFactory.create("1.0");

    assertEquals(numCell1.getCellValue(), numCell2.getCellValue());
    assertEquals(numCell1.getCellValue(), numCell3.getCellValue());

    cell = numCell1;

    assertEquals(new NumberCellValue(1), cell.evaluate());
    assertEquals(new NumberCellValue(1), cell.evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(1), cell.getCellValue());
    assertEquals("1", cell.getUserString());
    assertFalse(cell.isFormula());
    try {
      cell.setError(new SelfReferentialError());
    } catch (UnsupportedOperationException e) {
      assertEquals(new NumberCellValue(1), cell.getCellValue());
    }

    Cell cell = CellFactory.create("1");
    assertEquals(this.cell, cell);
    assertEquals(this.cell.hashCode(), cell.hashCode());

    cell = CellFactory.create("1.1");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());

    cell = CellFactory.create("1.0");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());
  }

  @Test
  public void stringCell() {
    cell = CellFactory.create("\"true\"");
    assertEquals(new StringCellValue("\"true\""), cell.evaluate());
    assertEquals(new StringCellValue("\"true\""), cell.evaluate(new ReferentialGraph<>()));
    assertEquals(new StringCellValue("\"true\""), cell.getCellValue());
    assertEquals("\"true\"", cell.getUserString());
    assertFalse(cell.isFormula());
    try {
      cell.setError(new SelfReferentialError());
    } catch (UnsupportedOperationException e) {
      assertEquals(new StringCellValue("\"true\""), cell.getCellValue());
    }
    Cell cell = CellFactory.create("\"true\"");
    assertEquals(this.cell, cell);
    assertEquals(this.cell.hashCode(), cell.hashCode());

    cell = CellFactory.create("\"1.0\"");
    assertNotEquals(this.cell, cell);
    assertNotEquals(this.cell.hashCode(), cell.hashCode());
  }
}
