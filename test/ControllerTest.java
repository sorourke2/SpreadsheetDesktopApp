import static junit.framework.TestCase.assertEquals;

import edu.cs3500.spreadsheets.controller.MockController;
import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.controller.WorksheetControllerInterface;
import edu.cs3500.spreadsheets.model.ImmutableWorksheet;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.value.BooleanValue;
import edu.cs3500.spreadsheets.model.value.ErrorValue;
import edu.cs3500.spreadsheets.model.value.NumberValue;
import edu.cs3500.spreadsheets.model.value.StringValue;
import edu.cs3500.spreadsheets.view.AdvancedWorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;
import java.io.IOException;
import org.junit.Test;

/**
 * Tests various features of the controller including the constructor and if the correct methods are
 * being called.
 */
public class ControllerTest {


  @Test
  public void testMockController() {

    StringBuilder ap;
      ap = new StringBuilder();

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
    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    WorksheetViewInterface wv = new AdvancedWorksheetView(iws);
    MockController wc = new MockController(ap);
    wc.go(ws, wv);

    wc.editCellContents(1, 1, "3.0");
    wc.editCellContents(1, 2,
        "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    wc.editCellContents(1, 3, "=(SUM A2 B2)");
    wc.editCellContents(2, 1, "4.0");
    wc.editCellContents(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    wc.editCellContents(2, 3, "=(< A3 100.0)");
    wc.editCellContents(3, 1, "9.0");
    wc.editCellContents(4, 1, "12.0");

   wc.cellContentsDeleted(1, 1);
   wc.cellContentsDeleted(1, 2);
   wc.cellContentsDeleted(1, 3);
   wc.cellContentsDeleted(2, 1);
   wc.cellContentsDeleted(2, 2);
   wc.cellContentsDeleted(2, 3);
   wc.cellContentsDeleted(3, 1);
   wc.cellContentsDeleted(4, 1);

    assertEquals(ap.toString(), new StringBuilder("go(..., ...)\n"
        + "editCellContents(1, 1, 3.0)\n"
        + "editCellContents(1, 2, =(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0))))\n"
        + "editCellContents(1, 3, =(SUM A2 B2))\n"
        + "editCellContents(2, 1, 4.0)\n"
        + "editCellContents(2, 2, =(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0))))\n"
        + "editCellContents(2, 3, =(< A3 100.0))\n"
        + "editCellContents(3, 1, 9.0)\n"
        + "editCellContents(4, 1, 12.0)\n"
        + "cellContentsDeleted(1, 1)\n"
        + "cellContentsDeleted(1, 2)\n"
        + "cellContentsDeleted(1, 3)\n"
        + "cellContentsDeleted(2, 1)\n"
        + "cellContentsDeleted(2, 2)\n"
        + "cellContentsDeleted(2, 3)\n"
        + "cellContentsDeleted(3, 1)\n"
        + "cellContentsDeleted(4, 1)\n").toString());
  }

  @Test
  public void testMockControllerNoGo() {

    StringBuilder ap;
    ap = new StringBuilder();
    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    WorksheetViewInterface wv = new AdvancedWorksheetView(iws);
    MockController wc = new MockController(ap);
    wc.editCellContents(1, 1, "3.0");
    wc.editCellContents(1, 2,
        "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    wc.editCellContents(1, 3, "=(SUM A2 B2)");
    wc.editCellContents(2, 1, "4.0");
    wc.editCellContents(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    wc.editCellContents(2, 3, "=(< A3 100.0)");
    wc.editCellContents(3, 1, "9.0");
    wc.editCellContents(4, 1, "12.0");

    wc.cellContentsDeleted(1, 1);
    wc.cellContentsDeleted(1, 2);
    wc.cellContentsDeleted(1, 3);
    wc.cellContentsDeleted(2, 1);
    wc.cellContentsDeleted(2, 2);
    wc.cellContentsDeleted(2, 3);
    wc.cellContentsDeleted(3, 1);
    wc.cellContentsDeleted(4, 1);

    assertEquals(ap.toString(), new StringBuilder("").toString());

  }

  @Test
  public void testControllerNoGo() {
    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    WorksheetViewInterface wv = new AdvancedWorksheetView(iws);
    WorksheetController c = new WorksheetController();

    c.editCellContents(1, 1, "2");
    c.editCellContents(1, 3, "=(CONCAT A5 A4 A6)");
    c.editCellContents(1, 4, "second");
    c.editCellContents(1, 5, "first");
    c.editCellContents(1, 6, "third");
    c.editCellContents(2, 3, "=(SUM A1 C2)");
    c.editCellContents(2, 6, "3");
    c.editCellContents(3, 1, "=(SUM 1 D1:E2)");
    c.editCellContents(3, 2, "3");
    c.editCellContents(3, 4, "=(< 1 D1:E2)");
    c.editCellContents(3, 5, "=(< 1 2 3)");
    c.editCellContents(3, 6, "=(PRODUCT (SUM B3 A1) B6)");
    c.editCellContents(4, 1, " 1");
    c.editCellContents(4, 2, " 1");
    c.editCellContents(4, 3, "=(< B6 4)");
    c.editCellContents(4, 5, "=(PRODUCT B3 A1)");
    c.editCellContents(5, 1, "1");
    c.editCellContents(5, 2, "1");
    c.editCellContents(5, 3, "=(SUM (SUM B3 A1) A1)");
    c.editCellContents(5, 4, "=(SUM 1 C5)");
    c.editCellContents(5, 5, "=(SUM E6 5)");
    c.editCellContents(5, 6, "=(SUM E5 6)");

    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 3));
    assertEquals(new StringValue(""), ws.getCellValue(1, 4));
    assertEquals(new StringValue(""), ws.getCellValue(1, 5));
    assertEquals(new StringValue(""), ws.getCellValue(1, 6));
    assertEquals(new StringValue(""), ws.getCellValue(2, 3));
    assertEquals(new StringValue(""), ws.getCellValue(2, 6));
    assertEquals(new StringValue(""), ws.getCellValue(3, 1));
    assertEquals(new StringValue(""), ws.getCellValue(3, 2));
    assertEquals(new StringValue(""), ws.getCellValue(3, 4));
    assertEquals(new StringValue(""), ws.getCellValue(3, 5));
    assertEquals(new StringValue(""), ws.getCellValue(3, 6));
    assertEquals(new StringValue(""), ws.getCellValue(4, 1));
    assertEquals(new StringValue(""), ws.getCellValue(4, 2));
    assertEquals(new StringValue(""), ws.getCellValue(4, 3));
    assertEquals(new StringValue(""), ws.getCellValue(4, 5));
    assertEquals(new StringValue(""), ws.getCellValue(4, 1));
    assertEquals(new StringValue(""), ws.getCellValue(5, 2));
    assertEquals(new StringValue(""), ws.getCellValue(5, 3));
    assertEquals(new StringValue(""), ws.getCellValue(5, 4));
    assertEquals(new StringValue(""), ws.getCellValue(5, 5));
    assertEquals(new StringValue(""), ws.getCellValue(5, 6));
  }

  @Test
  public void testControllerComplexCells() {


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

    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    WorksheetViewInterface wv = new AdvancedWorksheetView(iws);
    WorksheetController c = new WorksheetController();
    c.go(ws, wv);

    c.editCellContents(1, 1, "2");
    c.editCellContents(1, 3, "=(CONCAT A5 A4 A6)");
    c.editCellContents(1, 4, "second");
    c.editCellContents(1, 5, "first");
    c.editCellContents(1, 6, "third");
    c.editCellContents(2, 3, "=(SUM A1 C2)");
    c.editCellContents(2, 6, "3");
    c.editCellContents(3, 1, "=(SUM 1 D1:E2)");
    c.editCellContents(3, 2, "3");
    c.editCellContents(3, 4, "=(< 1 D1:E2)");
    c.editCellContents(3, 5, "=(< 1 2 3)");
    c.editCellContents(3, 6, "=(PRODUCT (SUM B3 A1) B6)");
    c.editCellContents(4, 1, " 1");
    c.editCellContents(4, 2, " 1");
    c.editCellContents(4, 3, "=(< B6 4)");
    c.editCellContents(4, 5, "=(PRODUCT B3 A1)");
    c.editCellContents(5, 1, "1");
    c.editCellContents(5, 2, "1");
    c.editCellContents(5, 3, "=(SUM (SUM B3 A1) A1)");
    c.editCellContents(5, 4, "=(SUM 1 C5)");
    c.editCellContents(5, 5, "=(SUM E6 5)");
    c.editCellContents(5, 6, "=(SUM E5 6)");

    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 1));
    assertEquals(new StringValue("firstsecondthird"), ws.getCellValue(1, 3));
    assertEquals(new StringValue("second"), ws.getCellValue(1, 4));
    assertEquals(new StringValue("first"), ws.getCellValue(1, 5));
    assertEquals(new StringValue("third"), ws.getCellValue(1, 6));
    assertEquals(new NumberValue(5.0), ws.getCellValue(2, 3));
    assertEquals(new NumberValue(3.0), ws.getCellValue(2, 6));
    assertEquals(new NumberValue(5.0), ws.getCellValue(3, 1));
    assertEquals(new NumberValue(3.0), ws.getCellValue(3, 2));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(3, 4));
    assertEquals(new ErrorValue("INV FORMULA"), ws.getCellValue(3, 5));
    assertEquals(new NumberValue(21.0), ws.getCellValue(3, 6));
    assertEquals(new NumberValue(1.0), ws.getCellValue(4, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(4, 2));
    assertEquals(new BooleanValue(true), ws.getCellValue(4, 3));
    assertEquals(new NumberValue(10.0), ws.getCellValue(4, 5));
    assertEquals(new NumberValue(1.0), ws.getCellValue(4, 1));
    assertEquals(new NumberValue(1.0), ws.getCellValue(5, 2));
    assertEquals(new NumberValue(9.0), ws.getCellValue(5, 3));
    assertEquals(new ErrorValue("EVAL ERROR"), ws.getCellValue(5, 4));
    assertEquals(new ErrorValue("SELF REF"), ws.getCellValue(5, 5));
    assertEquals(new ErrorValue("SELF REF"), ws.getCellValue(5, 6));
  }

  @Test
  public void testControllerComplexSheet() {
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
    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    WorksheetViewInterface wv = new AdvancedWorksheetView(iws);
    WorksheetController c = new WorksheetController();
    c.go(ws, wv);

    c.editCellContents(1, 1, "3.0");
    c.editCellContents(1, 2,
        "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    c.editCellContents(1, 3, "=(SUM A2 B2)");
    c.editCellContents(2, 1, "4.0");
    c.editCellContents(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    c.editCellContents(2, 3, "=(< A3 100.0)");
    c.editCellContents(3, 1, "9.0");
    c.editCellContents(4, 1, "12.0");

    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(36.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(4.0), ws.getCellValue(2, 1));
    assertEquals(new NumberValue(64.0), ws.getCellValue(2, 2));
    assertEquals(new BooleanValue(false), ws.getCellValue(2, 3));
    assertEquals(new NumberValue(9.0), ws.getCellValue(3, 1));
    assertEquals(new NumberValue(12.0), ws.getCellValue(4, 1));
    assertEquals(new NumberValue(100.0), ws.getCellValue(1, 3));

    c.cellContentsDeleted(1, 1);
    c.cellContentsDeleted(1, 2);
    c.cellContentsDeleted(1, 3);
    c.cellContentsDeleted(2, 1);
    c.cellContentsDeleted(2, 2);
    c.cellContentsDeleted(2, 3);
    c.cellContentsDeleted(3, 1);
    c.cellContentsDeleted(4, 1);

    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 2));
    assertEquals(new StringValue(""), ws.getCellValue(2, 1));
    assertEquals(new StringValue(""), ws.getCellValue(2, 2));
    assertEquals(new StringValue(""), ws.getCellValue(2, 3));
    assertEquals(new StringValue(""), ws.getCellValue(3, 1));
    assertEquals(new StringValue(""), ws.getCellValue(4, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 3));
  }


  @Test
  public void testControllerWithTextual() {


    WorksheetBuilder wsb = new WorksheetBuilder();
    Worksheet ws = wsb.createWorksheet();
    ImmutableWorksheetInterface iws = new ImmutableWorksheet(ws);
    StringBuilder ap = new StringBuilder();
    WorksheetViewInterface wv = new WorksheetTextualView(iws,ap);
    WorksheetController wc = new WorksheetController();
    wc.go(ws, wv);

    wc.editCellContents(1, 1, "3.0");
    wc.editCellContents(1, 2,
       "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    wc.editCellContents(1, 3, "=(SUM A2 B2)");
    wc.editCellContents(2, 1, "4.0");
    wc.editCellContents(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    wc.editCellContents(2, 3, "=(< A3 100.0)");
    wc.editCellContents(3, 1, "9.0");
    wc.editCellContents(4, 1, "12.0");
    System.out.println(ap.toString());

    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 1));
    assertEquals(new NumberValue(36.0), ws.getCellValue(1, 2));
    assertEquals(new NumberValue(4.0), ws.getCellValue(2, 1));
    assertEquals(new NumberValue(64.0), ws.getCellValue(2, 2));
    assertEquals(new BooleanValue(false), ws.getCellValue(2, 3));
    assertEquals(new NumberValue(9.0), ws.getCellValue(3, 1));
    assertEquals(new NumberValue(12.0), ws.getCellValue(4, 1));
    assertEquals(new NumberValue(100.0), ws.getCellValue(1, 3));
  }




}
