package edu.cs3500.spreadsheets.model.formula.function;

import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.ParseSexpError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

/**
 * Tests the cell function factory.
 */
public class CellFunctionFactoryTest {

  @Test
  public void basicConcatFunction() {
    assertEquals(new ConcatCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true)
        ))),
        CellFunctionFactory.create("CONCAT", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true)
        ))));

    assertEquals(new ConcatCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true),
        new StringCellValue("Hello"),
        new NumberCellValue(1),
        new SingleReference("A1"),
        new SingleReference("A2"),
        new SingleReference("A3"),
        new SingleReference("B2"),
        new SingleReference("B3")
        ))),
        CellFunctionFactory.create("CONCAT", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true),
            new StringCellValue("Hello"),
            new NumberCellValue(1),
            new SingleReference("A1"),
            new MultiReference("A2:B3")
        ))));
  }

  @Test
  public void concatFunctionZeroArg() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("CONCAT", new ArrayList<>(Arrays.asList(
        ))));
  }

  @Test
  public void basicLessThanFunction() {
    assertEquals(new LessThanCellFunction(new ArrayList<>(Arrays.asList(
        new NumberCellValue(1),
        new NumberCellValue(2)
        ))),
        CellFunctionFactory.create("<", new ArrayList<>(Arrays.asList(
            new NumberCellValue(1),
            new NumberCellValue(2)
        ))));

    assertEquals(new LessThanCellFunction(new ArrayList<>(Arrays.asList(
        new NumberCellValue(2),
        new NumberCellValue(1)
        ))),
        CellFunctionFactory.create("<", new ArrayList<>(Arrays.asList(
            new NumberCellValue(2),
            new NumberCellValue(1)
        ))));
  }

  @Test
  public void lessThanFunctionMultiRef() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("<", new ArrayList<>(Arrays.asList(
            new MultiReference("A1:A2"),
            new NumberCellValue(2)
        ))));
  }

  @Test
  public void lessThanFunctionZeroArg() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("<", new ArrayList<>(Arrays.asList(
        ))));
  }

  @Test
  public void lessThanFunctionOneArg() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("<", new ArrayList<>(Arrays.asList(
            new NumberCellValue(2)
        ))));
  }

  @Test
  public void basicProductFunction() {
    assertEquals(new ProductCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true)
        ))),
        CellFunctionFactory.create("PRODUCT", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true)
        ))));

    assertEquals(new ProductCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true),
        new StringCellValue("Hello"),
        new NumberCellValue(1),
        new SingleReference("A1"),
        new SingleReference("A2"),
        new SingleReference("A3"),
        new SingleReference("B2"),
        new SingleReference("B3")
        ))),
        CellFunctionFactory.create("PRODUCT", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true),
            new StringCellValue("Hello"),
            new NumberCellValue(1),
            new SingleReference("A1"),
            new MultiReference("A2:B3")
        ))));
  }

  @Test
  public void productFunctionZeroArg() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("PRODUCT", new ArrayList<>(Arrays.asList(
        ))));
  }

  @Test
  public void basicSumFunction() {
    assertEquals(new SumCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true)
        ))),
        CellFunctionFactory.create("SUM", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true)
        ))));

    assertEquals(new SumCellFunction(new ArrayList<>(Arrays.asList(
        new BooleanCellValue(true),
        new StringCellValue("Hello"),
        new NumberCellValue(1),
        new SingleReference("A1"),
        new SingleReference("A2"),
        new SingleReference("A3"),
        new SingleReference("B2"),
        new SingleReference("B3")
        ))),
        CellFunctionFactory.create("SUM", new ArrayList<>(Arrays.asList(
            new BooleanCellValue(true),
            new StringCellValue("Hello"),
            new NumberCellValue(1),
            new SingleReference("A1"),
            new MultiReference("A2:B3")
        ))));
  }

  @Test
  public void sumFunctionZeroArg() {
    assertEquals(new ParseSexpError(),
        CellFunctionFactory.create("SUM", new ArrayList<>(Arrays.asList(
        ))));
  }
}
