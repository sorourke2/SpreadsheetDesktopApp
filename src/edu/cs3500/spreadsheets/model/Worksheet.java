package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.function.FunctionObject;
import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.function.EvaluateCell;
import edu.cs3500.spreadsheets.model.function.MultiToSingle;
import edu.cs3500.spreadsheets.model.function.SetSelfReferentialError;
import edu.cs3500.spreadsheets.model.value.BooleanValue;
import edu.cs3500.spreadsheets.model.value.ErrorValue;
import edu.cs3500.spreadsheets.model.value.NumberValue;
import edu.cs3500.spreadsheets.model.value.StringValue;
import edu.cs3500.spreadsheets.model.value.Value;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A worksheet in a spreadsheet. Consists of the cells in the worksheet as well as the values
 * contained within the cells.
 */
public class Worksheet implements WorksheetInterface {

  private final int MAX = 67108864;

  private int numRow;
  private int numCol;
  private final Graph<Coord, Cell> cells;

  /**
   * A builder for worksheets.
   */
  public static class WorksheetConstructor {

    private int numRow = (int) Math.pow(2, 32);
    private int numCol = (int) Math.pow(2, 32);
    private Graph<Coord, Cell> cells = new ReferentialGraph<>();

    /**
     * Builds the worksheet.
     * @return the worksheet
     */
    public Worksheet build() {
      return new Worksheet(numRow, numCol, cells);
    }

    /**
     * Sets the number of rows in the work sheet initially.
     * @param numRow the number of rows
     * @return the current constructor
     */
    public WorksheetConstructor row(int numRow) {
      this.numRow = numRow;
      return this;
    }

    /**
     * Sets the number of columns in the work sheet initially.
     * @param numCol the number of columns
     * @return the current constructor
     */
    public WorksheetConstructor column(int numCol) {
      this.numCol = numCol;
      return this;
    }

    /**
     * Sets the initial graph of cells of the worksheet.
     * @param cells the initial graph of cells
     * @return the current constructor
     */
    public WorksheetConstructor cells(Graph<Coord, Cell> cells) {
      this.cells = cells;
      return this;
    }
  }

  /**
   * Constructor with given initial number of rows and columns,
   * and with an initial graph of cells to use.
   *
   * @param numRow number of rows
   * @param numCol number of columns
   * @param cells  graph of cells
   */
  private Worksheet(int numRow, int numCol, Graph<Coord, Cell> cells) {
    if (numRow < 1) {
      throw new IllegalArgumentException("Number of rows must be at least 1.");
    } else if (numCol < 1) {
      throw new IllegalArgumentException("Number of columns must be at least 1.");
    } else if (cells == null) {
      throw new IllegalArgumentException("Cells must be a non-null graph.");
    }
    else {
      this.numRow = numRow > MAX ? MAX : numRow;
      this.numCol = numCol > MAX ? MAX : numCol;
      this.cells = cells;

      for (Coord coord : this.cells.getNodes().keySet()) {
        Cell cell = this.cells.get(coord);
        if (cell.isFormula()) {
          String userString = cell.getUserString();
          List<String> cellRefs = this.listCellRef(userString);

          for (String cellRef : cellRefs) {
            this.cells.addEdge(coord, Coord.strToCoord(cellRef));
          }
        }
      }
    }

    this.cells.applyCycleTree(new SetSelfReferentialError(), new EvaluateCell(this.cells));
  }

  @Override
  public Set<Coord> getFilledCells() {
    return new HashSet<>(this.cells.getNodes().keySet());
  }

  @Override
  public int getRows() {
    return this.numRow;
  }

  @Override
  public int getColumns() {
    return this.numCol;
  }

  @Override
  public void clearCell(int column, int row) {
    // check valid row column
    this.assertValidRowColumn(row, column);

    // if cell has nothing pointing to it, delete it from graph
    // otherwise, "replace" cell with blank cell
    if (this.cells.contains(new Coord(column, row))) {
      this.cells.replaceNode(new Coord(column, row), CellFactory.create(""));
      this.cells.applyCycleTreeAncestors(new Coord(column, row),
          new SetSelfReferentialError(), new EvaluateCell(this.cells));
      this.cells.removeNode(new Coord(column, row));
    }
  }

  @Override
  public void setCell(int column, int row, String userString) {
    // check valid row column
    this.assertValidRowColumn(row, column);

    Coord coord = new Coord(column, row);
    Cell newCell = CellFactory.create(userString);

    if (!this.cells.contains(coord)) {
      this.cells.addNode(coord, newCell);
    } else {
      this.cells.replaceNode(coord, newCell);
    }

    if (newCell.isFormula()) {
      List<String> allOutEdges = this.listCellRef(userString);

      Coord current = new Coord(column, row);
      for (String cell : allOutEdges) {
        this.cells.addEdge(current, Coord.strToCoord(cell));
      }
    }

    this.cells.applyCycleTreeAncestors(new Coord(column, row),
        new SetSelfReferentialError(), new EvaluateCell(this.cells));
  }

  @Override
  public int addColumns(int numCol) {
    this.assertPositiveNumber(numCol);

    this.numCol += numCol;
    this.numCol = this.numCol > MAX ? MAX : this.numCol;

    return this.numCol;
  }

  @Override
  public int addRows(int numRow) {
    this.assertPositiveNumber(numRow);

    this.numRow += numRow;
    this.numRow = this.numRow > MAX ? MAX : this.numRow;

    return this.numRow;
  }

  @Override
  public String getUserString(int column, int row) {
    Cell cell = this.cells.get(new Coord(column, row));
    return cell == null ? "" : cell.getUserString();
  }

  @Override
  public Value<?> getCellValue(int column, int row) {
    Cell cell = this.cells.get(new Coord(column, row));

    if (cell == null) {
      return new StringValue("");
    } else {
      CellValue cellVal = cell.getCellValue();

      if (cellVal.isString()) {
        return new StringValue((String) cellVal.getValue());
      } else if (cellVal.isNumber()) {
        return new NumberValue((Double) cellVal.getValue());
      } else if (cellVal.isBoolean()) {
        return new BooleanValue((Boolean) cellVal.getValue());
      } else if (cellVal.isError()) {
        return new ErrorValue(cellVal.getValue().toString());
      } else {
        return new StringValue("");
      }
    }
  }

  @Override
  public boolean isCellError(int column, int row) {
    this.assertValidRowColumn(row, column);

    Cell cell =  this.cells.get(new Coord(column, row));
    return cell != null && cell.getCellValue().isError();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Worksheet
        && this.numRow == ((Worksheet) obj).numRow
        && this.numCol == ((Worksheet) obj).numCol
        && this.cells.equals(((Worksheet) obj).cells);
  }

  @Override
  public int hashCode() {
    return this.numRow + this.numCol + this.cells.hashCode();
  }

  /**
   * List all cell references in the given string, flattening out multi-cell references.
   * @param userString the given string
   * @return a list of all cell references in the string
   */
  private List<String> listCellRef(String userString) {
    List<String> allOutEdges = new ArrayList<>();
    Matcher matcher;

    matcher = Pattern.compile("([A-Z]+[1-9][0-9]*)").matcher(userString);
    while (matcher.find()) {
      for (int i = 1; i <= matcher.groupCount(); i++) {
        allOutEdges.add(matcher.group(i));
      }
    }

    matcher = Pattern.compile("([A-Z]+[1-9][0-9]*:[A-Z]+[1-9][0-9]*)").matcher(userString);
    while (matcher.find()) {
      for (int i = 1; i <= matcher.groupCount(); i++) {
        allOutEdges.addAll(this.multiToSingle(matcher.group(i)));
      }
    }

    return allOutEdges;
  }

  /**
   * Assert that the row and column are positive and within the size of this worksheet.
   * @param row the row
   * @param column the column
   */
  private void assertValidRowColumn(int row, int column) {
    if (row < 1 || row > this.numRow) {
      throw new IllegalArgumentException("Invalid row.");
    } else if (column < 1 || column > this.numCol) {
      throw new IllegalArgumentException("Invalid column.");
    }
  }

  /**
   * Assert that the given number is positive.
   * @param num the number
   */
  private void assertPositiveNumber(int num) {
    if (num <= 0) {
      throw new IllegalArgumentException("Must be positive.");
    }
  }

  /**
   * Converts a multi-cell reference to a list of single cell references.
   * @param multi the multi-cell reference
   * @return a list of single=cell references
   */
  private List<String> multiToSingle(String multi) {
    if (!multi.matches("^[A-Z]+[1-9][0-9]*:[A-Z]+[1-9][0-9]*$")) {
      throw new IllegalArgumentException("String is not a multi-cell reference.");
    } else {
      FunctionObject<MultiReference, List<SingleReference>> func = new MultiToSingle();
      List<SingleReference> singleRefs = func.evaluate(new MultiReference(multi));
      List<String> strSingleRefs = new ArrayList<>();
      for (SingleReference singleRef : singleRefs) {
        strSingleRefs.add(singleRef.getCellRef());
      }
      return strSingleRefs;
    }
  }
}
