package edu.cs3500.spreadsheets.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.DivByZeroError;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.ParseSexpError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import org.junit.Test;

/**
 * Tests for cell factory.
 */
public class CellFactoryTest {

  @Test
  public void nonFormulaCell() {
    assertEquals(new PrimitiveCell("true", new BooleanCellValue(true)),
        CellFactory.create("true"));
    assertEquals(new PrimitiveCell("false", new BooleanCellValue(false)),
        CellFactory.create("false"));
    assertEquals(new PrimitiveCell(" true ", new BooleanCellValue(true)),
        CellFactory.create(" true "));
    assertEquals(new PrimitiveCell(" false ", new BooleanCellValue(false)),
        CellFactory.create(" false "));

    assertEquals(new PrimitiveCell("1", new NumberCellValue(1)),
        CellFactory.create("1"));
    assertEquals(new PrimitiveCell("0.5", new NumberCellValue(0.5)),
        CellFactory.create("0.5"));
    assertEquals(new PrimitiveCell("1/2", new NumberCellValue(1.0 / 2)),
        CellFactory.create("1/2"));
    assertEquals(new PrimitiveCell("1/0", new DivByZeroError()),
        CellFactory.create("1/0"));
    assertEquals(new PrimitiveCell(" 1 ", new NumberCellValue(1)),
        CellFactory.create(" 1 "));
    assertEquals(new PrimitiveCell(" 0.5 ", new NumberCellValue(0.5)),
        CellFactory.create(" 0.5 "));
    assertEquals(new PrimitiveCell(" 1/2 ", new NumberCellValue(1.0 / 2)),
        CellFactory.create(" 1/2 "));
    assertEquals(new PrimitiveCell(" 1/0 ", new DivByZeroError()),
        CellFactory.create(" 1/0 "));

    assertEquals(new PrimitiveCell("\"1\"", new StringCellValue("\"1\"")),
        CellFactory.create("\"1\""));
    assertEquals(new PrimitiveCell("\"true\"", new StringCellValue("\"true\"")),
        CellFactory.create("\"true\""));
    assertEquals(new PrimitiveCell(" \"1\" ", new StringCellValue("\"1\"")),
        CellFactory.create(" \"1\" "));
    assertEquals(new PrimitiveCell(" \"true\" ", new StringCellValue("\"true\"")),
        CellFactory.create(" \"true\" "));

    assertEquals(new PrimitiveCell(" =(SUM A1 A2)", new StringCellValue(" =(SUM A1 A2)")),
        CellFactory.create(" =(SUM A1 A2)"));
  }

  @Test
  public void testPrimitiveNumberFormula() {
    assertEquals(new FormulaCell("=1", new NumberCellValue(1)),
        CellFactory.create("=1"));
    assertEquals(new FormulaCell("=2.5", new NumberCellValue(2.5)),
        CellFactory.create("=2.5"));
    assertEquals(new FormulaCell("=-15  ", new NumberCellValue(-15)),
        CellFactory.create("=-15  "));
  }

  @Test
  public void testPrimitiveStringFormula() {
    assertEquals(new FormulaCell("=\"h\"    ", new StringCellValue("h")),
        CellFactory.create("=\"h\"    "));
    assertEquals(new FormulaCell("=\"h\"", new StringCellValue("h")),
        CellFactory.create("=\"h\""));
    assertEquals(new FormulaCell("=\"=\"", new StringCellValue("=")),
        CellFactory.create("=\"=\""));
    assertEquals(new FormulaCell("=\" \"", new StringCellValue(" ")),
        CellFactory.create("=\" \""));
    assertEquals(new FormulaCell("=\"A1\"", new StringCellValue("A1")),
        CellFactory.create("=\"A1\""));
    assertEquals(new FormulaCell("=\"A1:A1\"", new StringCellValue("A1:A1")),
        CellFactory.create("=\"A1:A1\""));
    assertEquals(new FormulaCell("=\"false\"", new StringCellValue("false")),
        CellFactory.create("=\"false\""));
    assertEquals(new FormulaCell("=\"1\"", new StringCellValue("1")),
        CellFactory.create("=\"1\""));
  }

  @Test
  public void testPrimitiveBooleanFormula() {
    assertEquals(new FormulaCell("=true", new BooleanCellValue(true)),
        CellFactory.create("=true"));
    assertEquals(new FormulaCell("=false", new BooleanCellValue(false)),
        CellFactory.create("=false"));
  }

  @Test
  public void testStringLeftoverInputFormula() {
    assertEquals(new FormulaCell("=\"\"1\"\"", new ParseSexpError()),
        CellFactory.create("=\"\"1\"\""));
  }

  @Test
  public void testCellSingleReferenceFormula() {
    assertEquals(new FormulaCell("=A1", new SingleReference("A1")),
        CellFactory.create("=A1"));
    assertEquals(new FormulaCell("=    Z4", new SingleReference("Z4")),
        CellFactory.create("=    Z4"));
    assertEquals(new FormulaCell("=AB1", new SingleReference("AB1")),
        CellFactory.create("=AB1"));
    assertEquals(new FormulaCell("=A9    ", new SingleReference("A9")),
        CellFactory.create("=A9    "));
    assertEquals(new FormulaCell("=ZA9", new SingleReference("ZA9")),
        CellFactory.create("=ZA9"));

    assertEquals(new FormulaCell("=A", new ParseSexpError()),
        CellFactory.create("=A"));
    assertEquals(new FormulaCell("=AZ", new ParseSexpError()),
        CellFactory.create("=AZ"));
    assertEquals(new FormulaCell("=A0", new ParseSexpError()),
        CellFactory.create("=A0"));
    assertEquals(new FormulaCell("=A0", new ParseSexpError()),
        CellFactory.create("=A0"));
    assertEquals(new FormulaCell("=A1A", new ParseSexpError()),
        CellFactory.create("=A1A"));
  }

  @Test
  public void testBadlyFormattedSingleReferenceFormula() {
    assertEquals(new FormulaCell("=A   0", new ParseSexpError()),
        CellFactory.create("=A   0"));
  }

  @Test
  public void testCellMultiReferenceFormula() {
    assertEquals(new FormulaCell("=A1:A1", new MultiReference("A1:A1")),
        CellFactory.create("=A1:A1"));
    assertEquals(new FormulaCell("=A1:Z4", new MultiReference("A1:Z4")),
        CellFactory.create("=A1:Z4"));
    assertEquals(new FormulaCell("=Z9:A1   ", new MultiReference("Z9:A1")),
        CellFactory.create("=Z9:A1   "));
    assertNotEquals(new FormulaCell("=   Z9:A1", new MultiReference("   Z9:A1")),
        CellFactory.create("   =Z9:A1"));

    assertEquals(new FormulaCell("=Z0:A1", new ParseSexpError()),
        CellFactory.create("=Z0:A1"));
    assertEquals(new FormulaCell("=Z1:", new ParseSexpError()),
        CellFactory.create("=Z1:"));
    assertEquals(new FormulaCell("=Z0:A1", new ParseSexpError()),
        CellFactory.create("=Z0:A1"));
    assertEquals(new FormulaCell("=A4A:A1", new ParseSexpError()),
        CellFactory.create("=A4A:A1"));
  }

  @Test
  public void testBadlyFormattedMultiReferenceFormula() {
    assertEquals(new FormulaCell("=Z9:  A1", new ParseSexpError()),
        CellFactory.create("=Z9:  A1"));
  }
}
