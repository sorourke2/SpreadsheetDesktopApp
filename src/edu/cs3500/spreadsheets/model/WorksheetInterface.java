package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.Value;
import java.util.Set;

/**
 * A single worksheet in a spreadsheet document. The row and column indicies start at 1. A worksheet
 * consists of cells that have contains data.
 */
public interface WorksheetInterface {

  /**
   * Clears the cell.
   *
   * @param column the column
   * @param row the row
   */
  void clearCell(int column, int row);

  /**
   * Sets the cell expression to the given string.
   *
   * @param column the column of the cell
   * @param row the row of the cell
   * @param userString the given string
   */
  void setCell(int column, int row, String userString);

  /**
   * Add columns to the right.
   *
   * @param numCol the number of columns to add
   * @return the number of columns
   */
  int addColumns(int numCol);

  /**
   * Add rows to the bottom.
   *
   * @param numRow the number of rows to add
   * @return the number of rows
   */
  int addRows(int numRow);

  /**
   * Gets the user string at the given row and column.
   *
   * @param column the column
   * @param row the row
   * @return the user string at the row and column
   */
  String getUserString(int column, int row);

  /**
   * Gets the value at the given row and column.
   *
   * @param column the column
   * @param row the row
   * @return the value at the row and column
   */
  Value<?> getCellValue(int column, int row);

  /**
   * Is the cell at the given row and column contains an error.
   *
   * @param column the column
   * @param row the row
   * @return true if the cell contains an error
   */
  boolean isCellError(int column, int row);

  /**
   * Returns the number of rows in the worksheet.
   *
   * @return the number of rows.
   */
  int getRows();

  /**
   * Returns the number of columns in the worksheet.
   *
   * @return the number of columns
   */
  int getColumns();

  /**
   * Return the list of coordinates of the cells that contain something.
   *
   * @return the list of coordinates of the cells that contain something
   */
  Set<Coord> getFilledCells();
}
