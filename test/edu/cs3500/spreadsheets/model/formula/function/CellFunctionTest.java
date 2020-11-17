package edu.cs3500.spreadsheets.model.formula.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests that methods of cell functions work as intended.
 */
public class CellFunctionTest {
  private Formula cellFunc;

  @Test(expected = UnsupportedOperationException.class)
  public void cellFunctionGetCellRef() {
    cellFunc = new ConcatCellFunction(new ArrayList<>());
    cellFunc.getCellRef();
  }

  @Test
  public void ConcatCellFunction() {
    List<Formula> list = new ArrayList<>();
    list.add(new BooleanCellValue(true));
    list.add(new NumberCellValue(1));
    list.add(new StringCellValue("Hello"));
    list.add(new StringCellValue("World"));
    cellFunc = new ConcatCellFunction(list);
    assertEquals(new StringCellValue("HelloWorld"),
        cellFunc.compute(new EvaluateFormula(new ReferentialGraph<>())));
    assertEquals(new StringCellValue("HelloWorld"),
        cellFunc.accept(new EvaluateFormula(new ReferentialGraph<>())));

    assertFalse(cellFunc.isCellValue());
    assertFalse(cellFunc.isMultiRef());
    assertFalse(cellFunc.isReference());
    assertTrue(cellFunc.isFunction());
  }
}
