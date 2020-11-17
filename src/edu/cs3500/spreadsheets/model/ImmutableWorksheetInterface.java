package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.Value;
import java.util.Set;

/**
 * A single worksheet in a spreadsheet document. The row and column indicies start at 1. There are
 * no mutable methods.
 */
public interface ImmutableWorksheetInterface {

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

