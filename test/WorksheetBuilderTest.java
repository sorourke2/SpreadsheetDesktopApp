import static junit.framework.TestCase.assertEquals;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.value.BooleanValue;
import edu.cs3500.spreadsheets.model.value.ErrorValue;
import edu.cs3500.spreadsheets.model.value.NumberValue;
import edu.cs3500.spreadsheets.model.value.StringValue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the worksheet builder.
 */
public class WorksheetBuilderTest {

  private WorksheetBuilder ws = new WorksheetBuilder();

  @Before
  public void init() {
    this.ws = null;
  }

  @Test
  public void createTwoCellsTest() {

    ws = new WorksheetBuilder();
    ws.createCell(1, 1, "1");

    assertEquals(67108864, ws.createWorksheet().getRows());
    assertEquals(67108864, ws.createWorksheet().getColumns());

    ws.createCell(1, 2, "2");

    assertEquals(67108864, ws.createWorksheet().getRows());
    assertEquals(67108864, ws.createWorksheet().getColumns());

    assertEquals("1", ws.createWorksheet().getUserString(1, 1));
    assertEquals("2", ws.createWorksheet().getUserString(1, 2));
  }


  @Test
  public void createComplexSheet() {

    /* A1: 3.0
     * A2: =(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))
     *          (6) * (6) = 36
     *
     * A3: =(SUM A2 B2)
     * B1: 4.0
     * B2: =(PRODUCT(SUM D1 (PRODUCT B1 -1.0)) (SUM (D1 (PRODUCT (B1 -1.0)))
     * B3: =(< A3 100.0)
     * C1: 9.0
     * D1: 12.0
     */

    ws = new WorksheetBuilder();
    ws.createCell(1, 1, "3.0");
    ws.createCell(1, 2,
        "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    ws.createCell(1, 3, "=(SUM A2 B2)");
    ws.createCell(2, 1, "4.0");
    ws.createCell(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    ws.createCell(2, 3, "=(< A3 100.0)");
    ws.createCell(3, 1, "9.0");
    ws.createCell(4, 1, "12.0");

    assertEquals(new NumberValue(3.0), ws.createWorksheet().getCellValue(1, 1));
    assertEquals(new NumberValue(36.0), ws.createWorksheet().getCellValue(1, 2));
    assertEquals(new NumberValue(4.0), ws.createWorksheet().getCellValue(2, 1));
    assertEquals(new NumberValue(64.0), ws.createWorksheet().getCellValue(2, 2));
    assertEquals(new BooleanValue(false), ws.createWorksheet().getCellValue(2, 3));
    assertEquals(new NumberValue(9.0), ws.createWorksheet().getCellValue(3, 1));
    assertEquals(new NumberValue(12.0), ws.createWorksheet().getCellValue(4, 1));
    assertEquals(new NumberValue(100.0), ws.createWorksheet().getCellValue(1, 3));
  }


  @Test
  public void createMoreComplexCellsTest() {

    ws = new WorksheetBuilder();

    /* A1: 2
     * A3: =(CONCAT A5 A4 A6)
     * A4:"second"
     * A5:"first"
     * A6:"third"
     * B3: =(SUM A1 C2)
     * B6: =3
     * C1: =(SUM 1 D1:E2)
     * C2: 3
     * C4: =(< 1 D1:E2)
     * C5: =(< 1 2 3)
     * C6: =(PRODUCT (SUM B3 A1) B6)
     * D1: 1
     * D2: 1
     * D3: =(< B6 4)
     * D5: =(PRODUCT B3 A1)
     * E1: 1
     * E2: 1
     * E3: =(SUM (SUM B3 A1) A1)
     * E4: =(SUM 1 C5)
     * E5: =(SUM E6 5)
     * E6: =(SUM E5 6)
     */

    ws.createCell(1, 1, "2");
    ws.createCell(1, 3, "=(CONCAT A5 A4 A6)");
    ws.createCell(1, 4, "second");
    ws.createCell(1, 5, "first");
    ws.createCell(1, 6, "third");
    ws.createCell(2, 3, "=(SUM A1 C2)");
    ws.createCell(2, 6, "3");
    ws.createCell(3, 1, "=(SUM 1 D1:E2)");
    ws.createCell(3, 2, "3");
    ws.createCell(3, 4, "=(< 1 D1:E2)");
    ws.createCell(3, 5, "=(< 1 2 3)");
    ws.createCell(3, 6, "=(PRODUCT (SUM B3 A1) B6)");
    ws.createCell(4, 1, " 1");
    ws.createCell(4, 2, " 1");
    ws.createCell(4, 3, "=(< B6 4)");
    ws.createCell(4, 5, "=(PRODUCT B3 A1)");
    ws.createCell(5, 1, "1");
    ws.createCell(5, 2, "1");
    ws.createCell(5, 3, "=(SUM (SUM B3 A1) A1)");
    ws.createCell(5, 4, "=(SUM 1 C5)");
    ws.createCell(5, 5, "=(SUM E6 5)");
    ws.createCell(5, 6, "=(SUM E5 6)");

    Worksheet createdWorksheet = ws.createWorksheet();

    assertEquals(new NumberValue(2.0), createdWorksheet.getCellValue(1, 1));
    assertEquals(new StringValue("firstsecondthird"), createdWorksheet.getCellValue(1, 3));
    assertEquals(new StringValue("second"), createdWorksheet.getCellValue(1, 4));
    assertEquals(new StringValue("first"), createdWorksheet.getCellValue(1, 5));
    assertEquals(new StringValue("third"), createdWorksheet.getCellValue(1, 6));
    assertEquals(new NumberValue(5.0), createdWorksheet.getCellValue(2, 3));
    assertEquals(new NumberValue(3.0), createdWorksheet.getCellValue(2, 6));
    assertEquals(new NumberValue(5.0), createdWorksheet.getCellValue(3, 1));
    assertEquals(new NumberValue(3.0), createdWorksheet.getCellValue(3, 2));
    assertEquals(new ErrorValue("INV FORMULA"), createdWorksheet.getCellValue(3, 4));
    assertEquals(new ErrorValue("INV FORMULA"), createdWorksheet.getCellValue(3, 5));
    assertEquals(new NumberValue(21.0), createdWorksheet.getCellValue(3, 6));
    assertEquals(new NumberValue(1.0), createdWorksheet.getCellValue(4, 1));
    assertEquals(new NumberValue(1.0), createdWorksheet.getCellValue(4, 2));
    assertEquals(new BooleanValue(true), createdWorksheet.getCellValue(4, 3));
    assertEquals(new NumberValue(10.0), createdWorksheet.getCellValue(4, 5));
    assertEquals(new NumberValue(1.0), createdWorksheet.getCellValue(4, 1));
    assertEquals(new NumberValue(1.0), createdWorksheet.getCellValue(5, 2));
    assertEquals(new NumberValue(9.0), createdWorksheet.getCellValue(5, 3));
    assertEquals(new ErrorValue("EVAL ERROR"), createdWorksheet.getCellValue(5, 4));
    assertEquals(new ErrorValue("SELF REF"), createdWorksheet.getCellValue(5, 5));
    assertEquals(new ErrorValue("SELF REF"), createdWorksheet.getCellValue(5, 6));
  }
}