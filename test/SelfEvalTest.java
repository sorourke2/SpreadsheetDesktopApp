import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.value.BooleanValue;
import edu.cs3500.spreadsheets.model.value.ErrorValue;
import edu.cs3500.spreadsheets.model.value.NumberValue;
import edu.cs3500.spreadsheets.model.value.StringValue;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the self evaluation.
 */
public class SelfEvalTest {

  private WorksheetBuilder builder = new WorksheetBuilder();
  private Worksheet ws;

  @Before
  public void init() {
    this.builder = new WorksheetBuilder();
  }

  @Test
  public void testEmpty() {
    // test creating an empty spreadsheet

    ws = builder.createWorksheet();

    assertEquals(67108864, ws.getRows());
    assertEquals(67108864, ws.getColumns());
    assertEquals(new HashSet<>(), ws.getFilledCells());
  }

  @Test
  public void testAddCells() {
    // test all cells added to spreadsheet are in fact there

    builder.createCell(1, 1, "1");
    builder.createCell(1, 2, "2");
    builder.createCell(1, 3, "3");
    builder.createCell(1, 4, "4");

    builder.createCell(6, 1, "5");
    builder.createCell(20, 2, "6");
    builder.createCell(1, 50, "7");
    builder.createCell(70, 4, "8");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 3));
    assertEquals(new NumberValue(4.0), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(5.0), ws.getCellValue(6, 1));
    assertEquals(new NumberValue(6.0), ws.getCellValue(20, 2));
    assertEquals(new NumberValue(7.0), ws.getCellValue(1, 50));
    assertEquals(new NumberValue(8.0), ws.getCellValue(70, 4));
  }

  @Test
  public void testSelfReferentialFormula() {
    // test self-referential formula is prohibited

    builder.createCell(1, 1, "=(SUM A1 2)");

    ws = builder.createWorksheet();

    assertEquals(new ErrorValue("SELF REF"), ws.getCellValue(1, 1));
  }

  @Test
  public void testIndirectSelfReferentialFormula() {
    // test that indirect self-referential formula is prohibited

    builder.createCell(1, 1, "=(SUM A2 2)");
    builder.createCell(1, 2, "=(SUM A1 2)");

    ws = builder.createWorksheet();

    assertEquals(new ErrorValue("SELF REF"), ws.getCellValue(1, 1));
  }

  @Test
  public void testBlankCells() {
    // test blank cells

    builder.createCell(1, 1, "");
    builder.createCell(1, 2, "=");

    ws = builder.createWorksheet();

    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 2));
  }

  @Test
  public void testNumericValues() {
    // test numeric values

    builder.createCell(1, 1, "0");
    builder.createCell(1, 2, "1");
    builder.createCell(1, 3, "-1");
    builder.createCell(1, 4, "-1.67694");
    builder.createCell(1, 5, "-605040.67694");
    builder.createCell(2, 1, "=0.0");
    builder.createCell(2, 2, "=1");
    builder.createCell(2, 3, "=-1");
    builder.createCell(2, 4, "=-1.67694");
    builder.createCell(2, 5, "=-605040.67694");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(0.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(-1.0), ws.getCellValue(1, 3));
    assertEquals(new NumberValue(-1.67694), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(-605040.67694), ws.getCellValue(1, 5));
    assertEquals(new NumberValue(0.0), ws.getCellValue(2, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(2, 2));
    assertEquals(new NumberValue(-1.0), ws.getCellValue(2, 3));
    assertEquals(new NumberValue(-1.67694), ws.getCellValue(2, 4));
    assertEquals(new NumberValue(-605040.67694), ws.getCellValue(2, 5));
  }

  @Test
  public void testBooleanValues() {
    // test boolean values

    builder.createCell(1, 1, "true");
    builder.createCell(1, 2, "false");
    builder.createCell(2, 1, "=true");
    builder.createCell(2, 2, "=false");

    ws = builder.createWorksheet();

    assertEquals(new BooleanValue(true), ws.getCellValue(1, 1));
    assertEquals(new BooleanValue(false), ws.getCellValue(1, 2));
    assertNotEquals(new StringValue("true"), ws.getCellValue(1, 1));
    assertNotEquals(new StringValue("false"), ws.getCellValue(1, 2));
    assertEquals(new BooleanValue(true), ws.getCellValue(2, 1));
    assertEquals(new BooleanValue(false), ws.getCellValue(2, 2));
    assertNotEquals(new StringValue("true"), ws.getCellValue(2, 1));
    assertNotEquals(new StringValue("false"), ws.getCellValue(2, 2));
  }

  @Test
  public void testStringValues() {
    // test string values

    builder.createCell(1, 1, "a");
    builder.createCell(1, 12, " a");
    builder.createCell(1, 2, "");
    builder.createCell(1, 3, " ");
    builder.createCell(1, 4, " \" \" "); // " "
    builder.createCell(1, 5, "Hello everybody this is a string \\ a message !!");
    builder.createCell(2, 1, "=\"a\"");
    builder.createCell(2, 2, "=\"\"");
    builder.createCell(2, 3, "=\" \"");
    builder.createCell(2, 4, "=\"out\"inner string\"out\"");
    builder.createCell(2, 5, "=\"Hello everybody this is a string \\ a message !!\"");
    builder.createCell(2, 6, "=\"a\\b \\\\ c   \"");

    ws = builder.createWorksheet();

    assertEquals(new StringValue("a"), ws.getCellValue(1, 1));
    assertEquals(new StringValue(" a"), ws.getCellValue(1, 12));
    assertEquals(new StringValue(""), ws.getCellValue(1, 2));
    assertEquals(new StringValue(" "), ws.getCellValue(1, 3));
    assertEquals(new StringValue("\" \""), ws.getCellValue(1, 4));
    assertEquals(new StringValue("Hello everybody this is a string \\ a message !!"),
        ws.getCellValue(1, 5));

    assertEquals(new StringValue("a"), ws.getCellValue(2, 1));
    assertEquals(new StringValue(""), ws.getCellValue(2, 2));
    assertEquals(new StringValue(" "), ws.getCellValue(2, 3));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(2, 4));
    assertEquals(new StringValue("Hello everybody this is a string \\ a message !!"),
        ws.getCellValue(2, 5));

  }

  @Test
  public void testFormulas() {
    // test formulas

    builder.createCell(1, 1, "=");
    builder.createCell(1, 2, "=\"a\"");
    builder.createCell(1, 3, "=3");
    builder.createCell(1, 4, "=true");
    builder.createCell(1, 5, "=(SUM 1 2)");
    builder.createCell(1, 6, "=(PRODUCT 1 2)");
    builder.createCell(1, 7, "=(< 1 2)");
    builder.createCell(1, 8, "=(CONCAT \"A\" \"B\")");
    builder.createCell(1, 9, "=STRING");
    builder.createCell(1, 10, "=()");

    ws = builder.createWorksheet();

    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 1));
    assertEquals(new StringValue("a"), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 3));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 5));
    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 6));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 7));
    assertEquals(new StringValue("AB"), ws.getCellValue(1, 8));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 9));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 10));
  }

  @Test
  public void testSum() {
    // test SUM

    builder.createCell(1, 1, "=(SUM 1 2)");
    builder.createCell(1, 2, "=(SUM -1 2)");
    builder.createCell(1, 3, "=(SUM -1 -1)");
    builder.createCell(1, 4, "=(SUM 1 20 \"STRING\" 3 5)");
    builder.createCell(1, 5, "=(SUM 1)");
    builder.createCell(1, 6, "=(SUM )");
    builder.createCell(1, 7, "=(     SUM -1.4 4.5)");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(-2.0), ws.getCellValue(1, 3));
    assertEquals(new NumberValue(29.0), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 5));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 6));
    assertEquals(new NumberValue(3.1), ws.getCellValue(1, 7));
  }

  @Test
  public void testProduct() {
    // test PRODUCT

    builder.createCell(1, 1, "=(PRODUCT 1 2)");
    builder.createCell(1, 2, "=(PRODUCT -1 2)");
    builder.createCell(1, 3, "=(PRODUCT -1 -1)");
    builder.createCell(1, 4, "=(PRODUCT 1 20 \"STRING\" 3 5)");
    builder.createCell(1, 5, "=(PRODUCT 1)");
    builder.createCell(1, 6, "=(PRODUCT )");
    builder.createCell(1, 7, "=(     PRODUCT -1.4 4.5)");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(-2.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 3));
    assertEquals(new NumberValue(300.0), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 5));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 6));
    assertEquals(new NumberValue(-6.3), ws.getCellValue(1, 7));
  }

  @Test
  public void testLessThan() {
    // test <

    builder.createCell(1, 1, "=(< 1 2)");
    builder.createCell(1, 2, "=(< -1 2)");
    builder.createCell(1, 3, "=(< -1 -1)");
    builder.createCell(1, 4, "=(< 1 20 \"STRING\" 3 5)");
    builder.createCell(1, 5, "=(< 1)");
    builder.createCell(1, 6, "=(< )");
    builder.createCell(1, 7, "=(     < -1.4 4.5)");
    builder.createCell(1, 8, "=(     < 2 B1:B2)");
    builder.createCell(2, 1, "1");
    builder.createCell(2, 2, "1");

    ws = builder.createWorksheet();

    assertEquals(new BooleanValue(true), ws.getCellValue(1, 1));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 2));
    assertEquals(new BooleanValue(false), ws.getCellValue(1, 3));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 4));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 5));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 6));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 7));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 8));
  }

  @Test
  public void testConcat() {
    // test CONCAT

    builder.createCell(1, 1, "=(CONCAT \"a\"          \"b\")");
    builder.createCell(1, 2, "=(CONCAT \"\" \"\")");
    builder.createCell(1, 3, "=(CONCAT \"b\")");
    builder.createCell(1, 4, "=(CONCAT \"\")");
    builder.createCell(1, 5, "=(CONCAT A)");
    builder.createCell(1, 6, "=(CONCAT 1 2)");
    builder.createCell(1, 7, "=(CONCAT \"FIRST\" 4 \"SECOND\")");
    builder.createCell(1, 8, "=(CONCAT \"IN-\"STRING\"-OUT\" 4 \"SECOND\")");
    builder.createCell(1, 9, "=(CONCAT )");
    builder.createCell(1, 10, "=(CONCAT \" \" \" \")");

    ws = builder.createWorksheet();

    assertEquals(new StringValue("ab"), ws.getCellValue(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 2));
    assertEquals(new StringValue("b"), ws.getCellValue(1, 3));
    assertEquals(new StringValue(""), ws.getCellValue(1, 4));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 5));
    assertEquals(new StringValue(""), ws.getCellValue(1, 6));
    assertEquals(new StringValue("FIRSTSECOND"), ws.getCellValue(1, 7));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 8));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 9));
    assertEquals(new StringValue("  "), ws.getCellValue(1, 10));
  }

  @Test
  public void testSameCellRefMultipleTimes() {
    // test Formulas that ref to the same cell multiple times

    builder.createCell(2, 1, "1");
    builder.createCell(2, 2, "\"STRING\"");
    builder.createCell(2, 3, "2");

    builder.createCell(1, 1, "=(SUM B1 B1)");
    builder.createCell(1, 2, "=(PRODUCT B1 B1)");
    builder.createCell(1, 3, "=(CONCAT B2 B2)");
    builder.createCell(1, 4, "=(< B3 B3)");

    builder.createCell(1, 5, "=(SUM B1 B1 B1 B1 B1 B1)");
    builder.createCell(1, 6, "=(PRODUCT B1 B1 B1 B1 B1)");
    builder.createCell(1, 7, "=(CONCAT B2 B2 B2 B2 B2)");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 2));
    assertEquals(new StringValue("STRINGSTRING"), ws.getCellValue(1, 3));
    assertEquals(new BooleanValue(false), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(6.0), ws.getCellValue(1, 5));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 6));
    assertEquals(new StringValue("STRINGSTRINGSTRINGSTRINGSTRING"), ws.getCellValue(1, 7));
  }

  @Test
  public void testRegionReferences() {
    // test region references in formula

    builder.createCell(2, 1, "1");
    builder.createCell(2, 2, "2");
    builder.createCell(2, 3, "3");

    builder.createCell(3, 1, "1");
    builder.createCell(3, 2, "2");
    builder.createCell(3, 3, "3");

    builder.createCell(4, 1, "\"a\"");
    builder.createCell(4, 2, "\"b\"");
    builder.createCell(4, 3, "\"c\"");

    builder.createCell(5, 1, "\"a\"");
    builder.createCell(5, 2, "\"b\"");
    builder.createCell(5, 3, "\"c\"");

    builder.createCell(1, 1, "=(SUM B1:C3)");
    builder.createCell(1, 2, "=(PRODUCT B1:C3)");
    builder.createCell(1, 3, "=(CONCAT D1:E3)");
    builder.createCell(1, 4, "=(< B1:B2)");
    builder.createCell(1, 5, "=(SUM B1:B2)");
    builder.createCell(1, 6, "=(PRODUCT B1:B1)");
    builder.createCell(1, 7, "=(CONCAT D2:D2)");
    builder.createCell(1, 8, "=B1:C3");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(12.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(36.0), ws.getCellValue(1, 2));
    assertEquals(new StringValue("abcabc"), ws.getCellValue(1, 3));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 5));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 6));
    assertEquals(new StringValue("b"), ws.getCellValue(1, 7));
    assertEquals(new ErrorValue("EVAL ERROR"), ws.getCellValue(1, 8));
  }


  @Test
  public void testIncorrectArguments() {
    // test arguments with incorrect type

    builder.createCell(1, 1, "=(SUM 1 \"string\" 2)");
    builder.createCell(1, 2, "=(PRODUCT \"string\" 2)");
    builder.createCell(1, 3, "=(< 1 true)");
    builder.createCell(1, 4, "=(CONCAT \"A\" true \"B\" false)");
    builder.createCell(1, 5, "=(SUM true true)");
    builder.createCell(1, 6, "=(< true false)");
    builder.createCell(1, 7, "=(SUM 3 \"\")");
    builder.createCell(1, 8, "=(CONCAT 1 2 3 4 5)");
    builder.createCell(1, 9, "=(CONCAT string string string)");
    builder.createCell(1, 10, "=(CONCAT )");
    builder.createCell(1, 11, "=(SUM (SUM) )");
    builder.createCell(1, 12, "=(SUM                 )");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 2));
    assertEquals(new ErrorValue("EVAL ERROR"), ws.getCellValue(1, 3));
    assertEquals(new StringValue("AB"), ws.getCellValue(1, 4));
    assertEquals(new NumberValue(0.0), ws.getCellValue(1, 5));
    assertEquals(new ErrorValue("EVAL ERROR"), ws.getCellValue(1, 6));
    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 7));
    assertEquals(new StringValue(""), ws.getCellValue(1, 8));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 9));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 10));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 11));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(1, 12));
  }

  @Test
  public void testRenderValuesAsStrings() {
    // test rendering values as strings works

    builder.createCell(1, 1, "1");
    builder.createCell(1, 2, "true");
    builder.createCell(1, 3, "\"true\"");
    builder.createCell(1, 4, "=true");
    builder.createCell(1, 5, "=\"true\"");
    builder.createCell(1, 6, "=(PRODUCT A1:A5)");
    builder.createCell(1, 7, "=(SUM A1:A5)");
    builder.createCell(1, 8, "=(< A1 A1)");

    ws = builder.createWorksheet();

    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 1));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 2));
    // we treat the cell with just "true" as the string containing the word
    // the word true encompassed by double quotes
    assertEquals(new StringValue("\"true\""), ws.getCellValue(1, 3));
    assertEquals(new BooleanValue(true), ws.getCellValue(1, 4));
    assertEquals(new StringValue("true"), ws.getCellValue(1, 5));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 6));
    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 7));
    assertEquals(new BooleanValue(false), ws.getCellValue(1, 8));
  }

}
