import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.DivByZeroError;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;
import edu.cs3500.spreadsheets.model.formula.value.ParseSexpError;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import org.junit.Test;

/**
 * Tests for all different types of cell values.
 */
public class CellValueTest {

  private CellValue cellVal;

  @Test
  public void errorCellValue() {
    cellVal = new EvaluationError();
    assertEquals("EVAL ERROR", cellVal.getValue());

    cellVal = new DivByZeroError();
    assertEquals("DIV BY 0", cellVal.getValue());

    cellVal = new ParseSexpError();
    assertEquals("INV FORMULA", cellVal.getValue());

    cellVal = new SelfReferentialError();
    assertEquals("SELF REF", cellVal.getValue());

    assertFalse(cellVal.isBoolean());
    assertFalse(cellVal.isFunction());
    assertFalse(cellVal.isMultiRef());
    assertFalse(cellVal.isNumber());
    assertFalse(cellVal.isReference());
    assertFalse(cellVal.isString());
    assertTrue(cellVal.isCellValue());
    assertTrue(cellVal.isError());

    assertEquals(new SelfReferentialError(),
        cellVal.accept(new EvaluateFormula(new ReferentialGraph<>())));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void errorCellGetCellRef() {
    cellVal = new EvaluationError();
    cellVal.getCellRef();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void errorCellCompute() {
    cellVal = new EvaluationError();
    cellVal.compute(new EvaluateFormula(new ReferentialGraph<>()));
  }

  @Test
  public void primitiveCellValue() {
    cellVal = new BooleanCellValue(true);
    assertEquals(true, cellVal.getValue());
    assertEquals(new BooleanCellValue(true),
        cellVal.accept(new EvaluateFormula(new ReferentialGraph<>())));

    assertFalse(cellVal.isError());
    assertFalse(cellVal.isFunction());
    assertFalse(cellVal.isMultiRef());
    assertFalse(cellVal.isNumber());
    assertFalse(cellVal.isReference());
    assertFalse(cellVal.isString());
    assertTrue(cellVal.isBoolean());
    assertTrue(cellVal.isCellValue());
  }
}
