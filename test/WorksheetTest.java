import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetInterface;
import edu.cs3500.spreadsheets.model.value.ErrorValue;
import edu.cs3500.spreadsheets.model.value.NumberValue;
import edu.cs3500.spreadsheets.model.value.StringValue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for our implementation of the {@code WorksheetInterface}.
 */
public class WorksheetTest {

  private WorksheetInterface ws;
  private Graph<Coord, Cell> cells;

  private final String SELF_REF = "SELF REF";
  private final String INV_FORM = "INV FORMULA";

  @Before
  public void resetData() {
    this.ws = null;
  }

  private void initData0() {
    ws = new Worksheet.WorksheetConstructor().build();
  }

  private void initData1() {
    cells = new ReferentialGraph<>();
    // A1 : 1
    // A2 : 2
    // A3 : 3
    // B1 : =(SUM A1:A3)
    // B2 : =(SUM A1 A2 A3)
    // C1 : =C2
    // C2 : =C1
    cells.addNode(Coord.strToCoord("A1"), CellFactory.create("1"));
    cells.addNode(Coord.strToCoord("A2"), CellFactory.create("2"));
    cells.addNode(Coord.strToCoord("A3"), CellFactory.create("3"));
    cells.addNode(Coord.strToCoord("B1"), CellFactory.create("=(SUM A1:A3)"));
    cells.addNode(Coord.strToCoord("B2"), CellFactory.create("=(SUM A1 A2 A3)"));
    cells.addNode(Coord.strToCoord("C1"), CellFactory.create("=C2"));
    cells.addNode(Coord.strToCoord("C2"), CellFactory.create("=C1"));

    ws = new Worksheet.WorksheetConstructor().cells(cells).build();
  }

  private void initData2() {
    cells = new ReferentialGraph<>();
    // A1 : 1
    // A2 : 2
    // B1 : =(SUM A1 A2)
    // C1 : =(SUM A1 (SUM A1 B1))
    // C2 : =(SUM (SUM A1 B1) A1)
    // C3 : =(SUM A1 (SUM A1 B1) A1)
    cells.addNode(Coord.strToCoord("A1"), CellFactory.create("1"));
    cells.addNode(Coord.strToCoord("A2"), CellFactory.create("2"));
    cells.addNode(Coord.strToCoord("B1"), CellFactory.create("=(SUM A1 A2)"));
    cells.addNode(Coord.strToCoord("C1"), CellFactory.create("=(SUM A1 (SUM A1 B1))"));
    cells.addNode(Coord.strToCoord("C2"), CellFactory.create("=(SUM (SUM A1 B1) A1)"));
    cells.addNode(Coord.strToCoord("C3"), CellFactory.create("=(SUM A1 (SUM A1 B1) A1)"));

    ws = new Worksheet.WorksheetConstructor().cells(cells).build();
  }

  // =============================================

  @Test
  public void constructorNoArg() {
    ws = new Worksheet.WorksheetConstructor().build();
    assertEquals(67108864, ws.getRows());
    assertEquals(67108864, ws.getColumns());
    assertEquals("", ws.getUserString(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertFalse(ws.isCellError(1, 1));
  }

  @Test
  public void constructorRowColumn() {
    ws = new Worksheet.WorksheetConstructor().row(69).column(420).build();
    assertEquals(69, ws.getRows());
    assertEquals(420, ws.getColumns());
    assertEquals("", ws.getUserString(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertFalse(ws.isCellError(1, 1));
  }

  @Test
  public void constructorColumnRow() {
    ws = new Worksheet.WorksheetConstructor().column(420).row(69).build();
    assertEquals(69, ws.getRows());
    assertEquals(420, ws.getColumns());
    assertEquals("", ws.getUserString(1, 1));
    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertFalse(ws.isCellError(1, 1));
  }

  @Test
  public void constructorRowColumnGraph1() {
    initData1();

    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 1));
    assertEquals("1", ws.getUserString(1, 1));

    assertEquals(new NumberValue(2.0), ws.getCellValue(1, 2));
    assertEquals("2", ws.getUserString(1, 2));

    assertEquals(new NumberValue(3.0), ws.getCellValue(1, 3));
    assertEquals("3", ws.getUserString(1, 3));

    assertEquals(new NumberValue(6.0), ws.getCellValue(2, 1));
    assertEquals("=(SUM A1:A3)", ws.getUserString(2, 1));

    assertEquals(new NumberValue(6.0), ws.getCellValue(2, 2));
    assertEquals("=(SUM A1 A2 A3)", ws.getUserString(2, 2));

    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(3, 1));
    assertEquals("=C2", ws.getUserString(3, 1));

    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(3, 2));
    assertEquals("=C1", ws.getUserString(3, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorNonPositiveRow() {
    ws = new Worksheet.WorksheetConstructor().row(0).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorNonPositiveColumn() {
    ws = new Worksheet.WorksheetConstructor().column(-1).build();
  }

  // =============================================

  @Test
  public void clearCellDNE() {
    ws = new Worksheet.WorksheetConstructor().build();
    ws.clearCell(1, 1);
    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
  }

  @Test
  public void clearCellDoesExist() {
    initData1();

    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 1));
    assertEquals("1", ws.getUserString(1, 1));

    ws.clearCell(1, 1);

    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertEquals("", ws.getUserString(1, 1));

    assertEquals(new NumberValue(5.0), ws.getCellValue(2, 1));
    assertEquals("=(SUM A1:A3)", ws.getUserString(2, 1));

    assertEquals(new NumberValue(5.0), ws.getCellValue(2, 2));
    assertEquals("=(SUM A1 A2 A3)", ws.getUserString(2, 2));

    ws.clearCell(3, 1);

    assertEquals(new StringValue(""), ws.getCellValue(3, 2));
    assertEquals("=C1", ws.getUserString(3, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void clearCellNonPositiveRow() {
    initData0();
    ws.clearCell(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void clearCellTooLargeRow() {
    initData0();
    ws.clearCell(5, 67108864*10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void clearCellNonPositiveColumn() {
    initData0();
    ws.clearCell(-5, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void clearCellTooLargeColumn() {
    initData0();
    ws.clearCell(67108864*10, 3);
  }

  // =============================================

  @Test
  public void setCellDNEtoDNE() {
    initData1();

    ws.setCell(5, 5, "=D1");
    assertEquals(new StringValue(""), ws.getCellValue(5, 5));
    assertEquals("=D1", ws.getUserString(5, 5));
  }

  @Test
  public void setCellDNEtoDoesExist() {
    initData1();

    ws.setCell(5, 5, "=B1");
    assertEquals(new NumberValue(6.0), ws.getCellValue(5, 5));
    assertEquals("=B1", ws.getUserString(5, 5));
  }

  @Test
  public void setCellDNEtoDoesExistError() {
    initData1();

    ws.setCell(5, 5, "=C1");
    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(5, 5));
    assertEquals("=C1", ws.getUserString(5, 5));
  }

  @Test
  public void setCellDoesExistToDNE() {
    initData1();

    ws.setCell(1, 1, "=D1");

    assertEquals(new StringValue(""), ws.getCellValue(1, 1));
    assertEquals("=D1", ws.getUserString(1, 1));

    assertEquals(new NumberValue(5.0), ws.getCellValue(2, 1));
    assertEquals("=(SUM A1:A3)", ws.getUserString(2, 1));
    assertEquals(new NumberValue(5.0), ws.getCellValue(2, 2));
    assertEquals("=(SUM A1 A2 A3)", ws.getUserString(2, 2));
  }

  @Test
  public void setCellDoesExistToDoesExist() {
    initData1();

    ws.setCell(2, 2, "=A1");
    assertEquals(new NumberValue(1.0), ws.getCellValue(2, 2));
  }

  @Test
  public void setCellDoesExistToDoesExistError() {
    initData1();

    ws.setCell(1, 1, "=C1");
    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(2, 1));
    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(2, 2));
    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(3, 1));
    assertEquals(new ErrorValue(SELF_REF), ws.getCellValue(3, 2));
  }

  @Test
  public void setCellFunction() {
    initData1();

    ws.setCell(5, 5, "=(PRODUCT A1:A3)");
    assertEquals(new NumberValue(6.0), ws.getCellValue(5, 5));

    ws.setCell(5, 5, "=(PRODUCT A1:A3 B1:B2)");
    assertEquals(new NumberValue(216.0), ws.getCellValue(5, 5));
  }

  @Test
  public void setCellDNEInvFormula() {
    initData0();

    ws.setCell(5, 5, "=PRODUCT A1:A1");
    assertEquals(new ErrorValue(INV_FORM), ws.getCellValue(5, 5));
    assertEquals("=PRODUCT A1:A1", ws.getUserString(5, 5));
  }

  @Test
  public void setCellDoesExistInvFormula() {
    initData1();
    final String evalErr = "EVAL ERROR";

    ws.setCell(1, 1, "=weird");
    assertEquals(new ErrorValue(INV_FORM), ws.getCellValue(1, 1));
    assertEquals(new ErrorValue(evalErr), ws.getCellValue(2, 1));
    assertEquals(new ErrorValue(evalErr), ws.getCellValue(2, 2));
  }

  @Test
  public void setCellComplex1() {
    // sets the cell1 to some cell2 that does not yet exist in the graph
    // when we update cell2's value, cell1's value should also update
    initData0();

    ws.setCell(1, 1, "=A2");
    ws.setCell(1, 2, "1");

    assertEquals(new NumberValue(1.0), ws.getCellValue(1, 1));
  }

  @Test
  public void setCellComplex2() {
    // if C1 refers to C2 and C2 refers to C1,
    // but we update C1 such that it is an invalid formula,
    // C2 should now display an invalid formula error
    initData1();
    ws.setCell(3, 1, "=invalid");
    assertEquals(new ErrorValue(INV_FORM), ws.getCellValue(3, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellNonPositiveColumn() {
    initData0();
    ws.setCell(0, 1, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellTooLargeColumn() {
    initData0();
    ws.setCell(67108864*10, 1, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellNonPositiveRow() {
    initData0();
    ws.setCell(5, -5, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellTooLargeRow() {
    initData0();
    ws.setCell(5, 67108864*10, "");
  }

  // =============================================

  @Test
  public void addRowsColumns() {
    ws = new Worksheet.WorksheetConstructor().column(3).build();

    assertEquals(67108864, ws.getRows());
    assertEquals(67108864, ws.addRows(67108864));
    assertEquals(67108864, ws.getRows());

    assertEquals(3, ws.getColumns());
    assertEquals(8, ws.addColumns(5));
    assertEquals(8, ws.getColumns());
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRowsNonPositive() {
    initData0();
    ws.addRows(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addColumnsNonPositive() {
    initData0();
    ws.addColumns(-3);
  }

  // =============================================

  @Test
  public void isCellError0() {
    // all empty cells should not be errors
    initData0();
    assertFalse(ws.isCellError(1, 1));
    assertFalse(ws.isCellError(2, 3));
    assertFalse(ws.isCellError(100, 100));
  }

  @Test
  public void isCellError1() {
    // checks that appropriate cells are errors
    initData1();
    assertFalse(ws.isCellError(1, 1));
    assertFalse(ws.isCellError(1, 2));
    assertFalse(ws.isCellError(1, 3));
    assertFalse(ws.isCellError(2, 1));
    assertFalse(ws.isCellError(2, 2));
    assertTrue(ws.isCellError(3, 1));
    assertTrue(ws.isCellError(3, 2));
  }

  // =============================================

  @Test
  public void nestedFormula() {
    initData2();
    assertEquals(new NumberValue(5.0), ws.getCellValue(3, 1));
    assertEquals(new NumberValue(5.0), ws.getCellValue(3, 2));
    assertEquals(new NumberValue(6.0), ws.getCellValue(3, 3));
  }

  // =============================================

  @Test
  public void setCellFunctionCellValue() {
    initData0();
    ws.setCell(1, 1, "=(CONCAT \"A\" \"B\")");
    assertEquals(new StringValue("AB"), ws.getCellValue(1, 1));
  }
}
